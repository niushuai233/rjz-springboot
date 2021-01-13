package cc.niushuai.rjz.mp.builder;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Slf4j
public abstract class AbstractBuilder {

    public abstract WxMpXmlOutMessage build(String content, WxMpXmlMessage wxMessage, WxMpService service);
}
