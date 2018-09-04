package com.leyou.game.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Description : viewPagerFragment适配器
 *
 * @author : rocky
 * @Create Time : 2017/4/12 下午5:16
 * @Modified By: rocky
 * @Modified Time : 2017/4/12 下午5:16
 */
public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;

    public ViewPagerFragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    public ViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (list != null) {
            ret = list.size();
        }
        return ret;
    }
}
