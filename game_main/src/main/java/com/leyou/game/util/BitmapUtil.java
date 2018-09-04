package com.leyou.game.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.leyou.game.Constants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Description : 图片工具类
 *
 * @author : rocky
 * @Create Time : 2017/5/10 上午10:49
 * @Modified By: rocky
 * @Modified Time : 2017/5/10 上午10:49
 */
public class BitmapUtil {
    /**
     * 根据图片字节数组，对图片可能进行二次采样，不致于加载过大图片出现内存溢出
     *
     * @param bytes * @return
     */
    public static Bitmap getBitmapByBytes(byte[] bytes) {
        int width = 0;
        int height = 0;
        int sampleSize = 1;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        height = options.outHeight;
        width = options.outWidth;
        while ((height / sampleSize > Cache.IMAGE_MAX_HEIGHT) || (width / sampleSize > Cache.IMAGE_MAX_WIDTH)) {
            sampleSize *= 2;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    //默认大小
    static class Cache {
        public static final int IMAGE_MAX_HEIGHT = 854;
        public static final int IMAGE_MAX_WIDTH = 480;
    }

    /**
     * 根据路径获取图片 进行二次采样 压缩
     *
     * @param srcPath
     * @return
     */
    public static Bitmap getImageByPath(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //读入图片，注意此时已经把options.inJustDecodeBounds 设位true
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        //压缩好比例大小后再进行质量压缩
        return compressImage(bitmap);
    }

    /**
     * 图片压缩保存到临时文件
     *
     * @param srcPath 原始图片地址
     * @return 压缩后的图片地址
     */
    public static String getImageUrlCompressed(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //读入图片，注意此时已经把options.inJustDecodeBounds 设位true
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 2560;//这里设置高度1280f
        float ww = 1440;//这里设置宽度为720f
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        bitmap = compressImage(bitmap);
        File outputFile = new File(Constants.IMAGE_CACHE_DIR + File.separator + System.currentTimeMillis() + ".jpg");
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
            } else {
                outputFile.delete();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            return outputFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return srcPath;
        }
    }

    /**
     * 获取道的Bitmap 图片保存道本地
     *
     * @param bitmap
     * @return 图片的绝对路径
     */
    public static String saveBitmap(Bitmap bitmap) {
        String path = "";
        File outputFile = new File(Constants.IMAGE_CACHE_DIR + File.separator + System.currentTimeMillis() + ".jpg");
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
            } else {
                outputFile.delete();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            path = outputFile.getAbsolutePath();
            return path;
        } catch (Exception e) {
            e.printStackTrace();
            return path;
        }
    }

    /**
     * 进行图片压缩
     *
     * @param image
     * @return
     */

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ((baos.toByteArray().length / 1024) > 500) {//循环判断如果压缩后图片是否大于500kb,大于继续压缩
            LogUtil.d("compress", "----" + baos.toByteArray().length / 1024 + "-----" + options);
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
