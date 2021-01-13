package cc.niushuai.rjz.mp.handler;

import cc.niushuai.rjz.mp.builder.TextBuilder;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MsgHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService cpService,
                                    WxSessionManager sessionManager) {
        final String msgType = wxMessage.getMsgType();
        if (msgType == null) {
            // 如果msgType没有，就自己根据具体报文内容做处理
        }

        if (!msgType.equals(WxConsts.XmlMsgType.EVENT)) {
            //TODO 可以选择将消息保存到本地
        }

        //TODO 组装回复消息
        String content = "" + wxMessage.getContent();

        return new TextBuilder().build(content, wxMessage, cpService);
    }

}
