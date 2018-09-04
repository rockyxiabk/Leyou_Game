package com.leyou.game.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description : Fragment的基类封装
 *
 * @author : rocky
 * @Create Time : 2017/3/24 上午10:59
 * @Modified By: rocky
 * @Modified Time : 2017/3/24 上午10:59
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int vid = getLayout();
        if (vid <= 0) {
            return getLayoutView();
        } else {
            return inflater.inflate(vid, container, false);
        }
    }

    /**
     * 获取view id
     *
     * @return
     */
    protected abstract int getLayout();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
        initPresenter();
    }

    /**
     * 初始化控件
     *
     * @param view
     * @param savedInstanceState
     */
    protected abstract void initView(View view, @Nullable Bundle savedInstanceState);


    @Override
    public void onClick(View v) {

    }

    /**
     * 创建view
     *
     * @return
     */
    protected View getLayoutView() {
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        Log.d(this.getClass().getName(), "---------------------------LeakActivity has been recycled!");
    }

    protected abstract void initPresenter();
    //    protected abstract void initData();
}
