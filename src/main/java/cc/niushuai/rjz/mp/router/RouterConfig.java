package cc.niushuai.rjz.mp.router;

import cc.niushuai.rjz.mp.handler.*;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author ns
 * @date 2021/1/13
 */
@Component
public class RouterConfig {

    private static WxMpMessageRouter router;

    @Resource
    private WxMpService wxMpService;

    @Resource
    private LogHandler logHandler;

    @Resource
    private LocationHandler locationHandler;

    @Resource
    private NullHandler nullHandler;

    @Resource
    private MenuHandler menuHandler;

    @Resource
    private MsgHandler msgHandler;

    @Resource
    private SubscribeHandler subscribeHandler;

    @Resource
    private UnsubscribeHandler unsubscribeHandler;

    @PostConstruct
    public void initService() {
        router = newRouter();
    }

    public static WxMpMessageRouter getRouter() {
        return router;
    }

    public WxMpMessageRouter newRouter() {

        WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        // 记录所有事件的日志 （异步执行）
        newRouter.rule().handler(this.logHandler).next();

        // 自定义菜单事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.MenuButtonType.CLICK).handler(this.menuHandler).end();

        // 点击菜单链接事件（这里使用了一个空的处理器，可以根据自己需要进行扩展）
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.MenuButtonType.VIEW).handler(this.nullHandler).end();

        // 关注事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.SUBSCRIBE).handler(this.subscribeHandler)
                .end();

        // 取消关注事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.UNSUBSCRIBE)
                .handler(this.unsubscribeHandler).end();

        // 上报地理位置事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.LOCATION).handler(this.locationHandler)
                .end();

        // 接收地理位置消息
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.LOCATION)
                .handler(this.locationHandler).end();

        // 扫码事件（这里使用了一个空的处理器，可以根据自己需要进行扩展）
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.SCAN).handler(this.nullHandler).end();

        // 默认
        newRouter.rule().async(false).handler(this.msgHandler).end();

        return newRouter;

    }
}
