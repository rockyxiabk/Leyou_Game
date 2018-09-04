package com.leyou.game.rong.plugin;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.leyou.game.R;

import io.rong.imkit.emoticon.IEmoticonTab;

/**
 * Description : 自定义表情tab
 *
 * @author : rocky
 * @Create Time : 2017/8/15 上午10:02
 * @Modified Time : 2017/8/15 上午10:02
 */
public class MyCustomEmoticonTab implements IEmoticonTab {
    @Override
    public Drawable obtainTabDrawable(Context context) {
        return context.getResources().getDrawable(R.mipmap.icon_about);
    }

    @Override
    public View obtainTabPager(Context context) {
        //初始化 Tab 中的内容， 返回 View 或 View 的子类皆可。还可参考 EmojiTab 中 obtainTabPager
        TextView textView = new TextView(context);
        textView.setText("Sample EmoticonTabs ");
        return textView;
    }

    @Override
    public void onTableSelected(int i) {

    }
}
