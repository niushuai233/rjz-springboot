package cc.niushuai.rjz.mp.controller;

import cc.niushuai.rjz.mp.router.RouterConfig;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ns
 * @date 2021/1/12
 */
@Slf4j
@RestController
@RequestMapping("/wechat/mp")
public class WxMpController {


    @Resource
    private WxMpService wxMpService;

    @GetMapping("/eventMessage")
    public String eventMessageGet(HttpServletRequest request) throws WxErrorException {

        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        log.info("wechat checkSignature signature={}, timestamp={}, nonce={}, echostr={}", signature, timestamp, nonce, echostr);


        if (wxMpService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }
        return "";
    }


    @PostMapping("/eventMessage")
    public String eventMessagePost(HttpServletResponse response, HttpServletRequest request, @RequestBody String requestBody) throws Exception {

        WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);

        log.info("收到信息: {}", inMessage);

        WxMpXmlOutMessage outMessage = this.route(inMessage);
        if (outMessage == null) {
            return "你说啥，风大没听清! ^_^";
        }

        String out = outMessage.toXml();
        log.info("回复信息：{}", outMessage);

        return out;
    }

    private WxMpXmlOutMessage route(WxMpXmlMessage inMessage) {
        try {
            return RouterConfig.getRouter().route(inMessage);
        } catch (Exception e) {
            log.error("route ex: {}", e.getMessage(), e);
        }
        return null;
    }

}
