package cc.niushuai.rjz.mp.handler;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

import java.util.Map;

/**
 * <pre>
 *
 * Created by Binary Wang on 2018/8/27.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Slf4j
public class EnterAgentHandler extends AbstractHandler {
    private static final int TEST_AGENT = 1000002;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService WxMpService, WxSessionManager sessionManager) throws WxErrorException {
        // do something
        return null;
    }
}
