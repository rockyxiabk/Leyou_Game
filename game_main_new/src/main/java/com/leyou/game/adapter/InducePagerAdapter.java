package com.leyou.game.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Description : 引导页adapter
 *
 * @author : rocky
 * @Create Time : 2017/5/6 下午3:37
 * @Modified Time : 2017/5/6 下午3:37
 */
public class InducePagerAdapter extends PagerAdapter {
    private Context context;
    private List<ImageView> viewList;

    public InducePagerAdapter(Context context, List<ImageView> viewList) {
        this.viewList = viewList;
        this.context = context;
    }

    /**
     * @return 返回页面的个数
     */

    @Override
    public int getCount() {
        if (viewList != null) {
            return viewList.size();
        }
        return 0;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 初始化position位置的界面
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));

    }
}
