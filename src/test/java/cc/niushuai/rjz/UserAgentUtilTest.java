package cc.niushuai.rjz;

import cn.hutool.http.useragent.*;
import org.junit.Test;

/**
 * @author ns
 * @date 2020/8/13
 */
public class UserAgentUtilTest {

    @Test
    public void test1() {
        String uaStr = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1";

        UserAgent ua = UserAgentUtil.parse(uaStr);

        OS os = ua.getOs();

        System.out.println(os.getName());
        System.out.println();

        Engine engine = ua.getEngine();
        System.out.println(engine.getName());
        System.out.println();

        Platform platform = ua.getPlatform();
        System.out.println(platform.getName());
        System.out.println();

        Browser browser = ua.getBrowser();
        System.out.println(browser.getName());
        System.out.println();

        String version = ua.getVersion();
        System.out.println(version);
    }
}
