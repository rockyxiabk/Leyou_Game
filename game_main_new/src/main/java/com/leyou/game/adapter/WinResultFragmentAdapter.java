package com.leyou.game.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
* Description : 上期获奖结果 页面fragment页面切换适配
* @author : rocky
* @Create Time : 2017/4/24 下午3:07
* @Modified By: rocky
* @Modified Time : 2017/4/24 下午3:07
*/
public class WinResultFragmentAdapter extends FragmentStatePagerAdapter {
    List<? extends Fragment> mFragments;


    public WinResultFragmentAdapter(List<? extends Fragment> fragments, FragmentManager fm) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }
}
