package cc.niushuai.rjz.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_user_token")
public class UserToken {
    public static final String COL_ID = "id";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_TOKEN = "token";
    public static final String COL_IP = "ip";
    public static final String COL_USER_AGENT = "user_agent";
    public static final String COL_IS_EXPIRE = "is_expire";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_EXPIRE_TIME = "expire_time";
    public static final String COL_OS = "os";
    public static final String COL_BROWSER = "browser";
    public static final String COL_VERSION = "version";
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Integer userId;
    /**
     * token
     */
    @TableField(value = "token")
    private String token;
    /**
     * 登陆ip
     */
    @TableField(value = "ip")
    private String ip;
    /**
     * 完整ua
     */
    @TableField(value = "user_agent")
    private String userAgent;
    /**
     * 是否过期
     */
    @TableField(value = "is_expire")
    private Integer isExpire;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
    /**
     * 过期时间
     */
    @TableField(value = "expire_time")
    private Date expireTime;

    @TableField(exist = false)
    private long expireTimeMills;
    /**
     * 操作系统
     */
    @TableField(value = "os")
    private String os;
    /**
     * 浏览器
     */
    @TableField(value = "browser")
    private String browser;
    /**
     * 浏览器版本
     */
    @TableField(value = "version")
    private String version;
}