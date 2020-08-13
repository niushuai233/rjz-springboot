package cc.niushuai.rjz.category.entity;

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
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "bil_bill_category")
public class BillCategory {
    public static final String COL_ID = "id";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_RECORD_TYPE = "record_type";
    public static final String COL_CATEGORY_CODE = "category_code";
    public static final String COL_CATEGORY_NAME = "category_name";
    public static final String COL_ICON_CLASS_NAME = "icon_class_name";
    public static final String COL_IS_DEFAULT = "is_default";
    public static final String COL_IS_DELETE = "is_delete";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_TIME = "update_time";
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
     * 分类 0支出 1收入
     */
    @TableField(value = "record_type")
    private String recordType;
    /**
     * 分类编码
     */
    @TableField(value = "category_code")
    private String categoryCode;
    /**
     * 分类名称
     */
    @TableField(value = "category_name")
    private String categoryName;
    /**
     * icon
     */
    @TableField(value = "icon_class_name")
    private String iconClassName;
    /**
     * 是否是默认分类
     */
    @TableField(value = "is_default")
    @Builder.Default
    private Integer isDefault = 1;
    /**
     * 删除标志
     */
    @TableField(value = "is_delete")
    @Builder.Default
    private Integer isDelete = 1;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;
}