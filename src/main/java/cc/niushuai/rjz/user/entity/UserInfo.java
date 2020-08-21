package cc.niushuai.rjz.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户信息
 */
@Data
@TableName(value = "sys_user_info")
public class UserInfo {
    public static final String COL_ID = "id";
    public static final String COL_OPEN_ID = "open_id";
    public static final String NICK_NAME = "nick_name";
    public static final String GENDER = "gender";
    public static final String COUNTRY = "country";
    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String AVATAR_URL = "avatar_url";
    public static final String COL_LAST_LOGIN_TIME = "last_login_time";
    public static final String COL_IS_DELETE = "is_delete";
    public static final String COL_CREATE_TIME = "create_time";
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 微信openid
     */
    @TableField(value = "open_id")
    private String openId;
    /**
     * 微信昵称
     */
    @TableField(value = "nick_name")
    private String nickName;
    /**
     * 微信用户头像url
     */
    @TableField(value = "avatar_url")
    private String avatarUrl;

    /**
     * 性别
     */
    @TableField(value = "gender")
    private String gender;

    /**
     * 国家
     */
    @TableField(value = "country")
    private String country;

    /**
     * 省份
     */
    @TableField(value = "province")
    private String province;

    /**
     * 城市
     */
    @TableField(value = "city")
    private String city;

    /**
     * 上次登陆时间
     */
    @TableField(value = "last_login_time")
    private Date lastLoginTime;
    /**
     * 是否删除 0已删除 1未删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;
    /**
     * 加入时间
     */
    @TableField(value = "create_time")
    private Date createTime;
    /**
     * 微信临时token 登陆使用
     */
    @TableField(exist = false)
    private String code;
    @TableField(exist = false)
    private String sessionKey;
    @TableField(exist = false)
    private UserToken userToken;
}