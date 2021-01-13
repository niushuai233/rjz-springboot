package cc.niushuai.rjz.mp.handler;

import cc.niushuai.rjz.mp.builder.TextBuilder;
import me.chanjar.weixin.common.error.WxErrorException;
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
public class SubscribeHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService cpService,
                                    WxSessionManager sessionManager) throws WxErrorException {

        this.log.info("新关注用户 OPENID: " + wxMessage.getFromUser());

        try {
            return new TextBuilder().build("感谢关注", wxMessage, cpService);
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
     */
    private WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage) {
        //TODO
        return null;
    }

}
