package com.leyou.game.util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.game.GameBean;

import java.io.File;

/**
 * Description : com.leyou.game.util
 *
 * @author : rocky
 * @Create Time : 2018/1/6 下午4:21
 * @Modified Time : 2018/1/6 下午4:21
 */
public class DownloadUtil {
    //下载器
    private DownloadManager downloadManager;
    //上下文
    private Context mContext;
    //下载的ID
    private long downloadId;
    private GameBean gameBean = new GameBean();

    public DownloadUtil(Context context) {
        this.mContext = context;
    }

    //下载apk
    public void downloadAPK(GameBean gameBean) {
        this.gameBean = gameBean;

        if (checkFile()) {
            installAPK();
        } else {
            //创建下载任务
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(gameBean.gameUrl));
            //移动网络情况下是否允许漫游
            request.setAllowedOverRoaming(false);

            //在通知栏中显示，默认就是显示的
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setTitle(gameBean.name);
            request.setDescription(mContext.getResources().getString(R.string.app_name));
            request.setVisibleInDownloadsUi(true);

            //设置下载的路径
            request.setDestinationInExternalPublicDir(Constants.DOWNLOAD_DIR, gameBean.name + ".apk");//保存路径

            //获取DownloadManager
            downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
            downloadId = downloadManager.enqueue(request);

            //注册广播接收者，监听下载状态
            mContext.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

            ToastUtils.showToastShort("开始下载《"+gameBean.name+"》...");
        }
    }

    //广播监听下载的各个状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };

    //检查下载状态
    private void checkStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        //通过下载的id查找
        query.setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    //下载完成安装APK
                    installAPK();
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        c.close();
    }

    private boolean checkFile() {
        File file = new File(Constants.UPDATE_DIR + gameBean.name + ".apk");
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    //下载到本地后执行安装
    private void installAPK() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(Constants.UPDATE_DIR + gameBean.name + ".apk")), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
        mContext.unregisterReceiver(receiver);
    }
}
