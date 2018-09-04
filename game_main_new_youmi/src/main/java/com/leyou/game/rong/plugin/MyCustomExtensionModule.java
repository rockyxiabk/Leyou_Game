package com.leyou.game.rong.plugin;

import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.RongExtension;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Description : 自定义输入框功能面板
 *
 * @author : rocky
 * @Create Time : 2017/8/15 上午9:41
 * @Modified Time : 2017/8/15 上午9:41
 */
public class MyCustomExtensionModule extends DefaultExtensionModule {
    private MyCustomPluginModule myPlugin = new MyCustomPluginModule();
    private MyCustomEmoticonTab myEmoticon = new MyCustomEmoticonTab();

    @Override
    public void onInit(String appKey) {
        super.onInit(appKey);
    }

    @Override
    public void onDisconnect() {
        super.onDisconnect();
    }

    @Override
    public void onConnect(String token) {
        super.onConnect(token);
    }

    @Override
    public void onAttachedToExtension(RongExtension extension) {
        super.onAttachedToExtension(extension);
    }

    @Override
    public void onDetachedFromExtension() {
        super.onDetachedFromExtension();
    }

    @Override
    public void onReceivedMessage(Message message) {
        super.onReceivedMessage(message);
    }

    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules = super.getPluginModules(conversationType);
        // TODO: 2017/8/15 扩展其他功能区
        pluginModules.add(myPlugin);
        return pluginModules;
    }

    @Override
    public List<IEmoticonTab> getEmoticonTabs() {
        List<IEmoticonTab> emoticonTabs = super.getEmoticonTabs();
        // TODO: 2017/8/15 扩展其他表情区
//        emoticonTabs.add(myEmoticon);
        return emoticonTabs;
    }
}
