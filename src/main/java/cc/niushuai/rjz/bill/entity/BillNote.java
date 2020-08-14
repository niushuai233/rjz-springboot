package cc.niushuai.rjz.bill.entity;

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
@TableName(value = "bil_bill_note")
public class BillNote {

    public static final String COL_ID = "id";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_ICON = "icon";
    public static final String COL_CATEGORY_ID = "category_id";
    public static final String COL_CATEGORY_NAME = "category_name";
    public static final String COL_BILL_YEAR = "bill_year";
    public static final String COL_BILL_MONTH = "bill_month";
    public static final String COL_BILL_DAY = "bill_day";
    public static final String COL_BILL_TYPE = "bill_type";
    public static final String COL_PRICE = "price";
    public static final String COL_PAY_INCOME_NOTE = "pay_income_note";
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
     * 分类id
     */
    @TableField(value = "icon")
    private String icon;
    /**
     * 分类id
     */
    @TableField(value = "category_name")
    private String categoryName;
    /**
     * 分类id
     */
    @TableField(value = "category_id")
    private Integer categoryId;
    /**
     * 年
     */
    @TableField(value = "bill_year")
    private Integer billYear;
    /**
     * 月
     */
    @TableField(value = "bill_month")
    private Integer billMonth;
    /**
     * 日
     */
    @TableField(value = "bill_day")
    private Integer billDay;
    /**
     * 单据类型 1支出 2收入
     */
    @TableField(value = "bill_type")
    private Integer billType;
    /**
     * 单据金额
     */
    @TableField(value = "price")
    private String price;
    /**
     * 单据金额
     */
    @TableField(value = "pay_income_note")
    private String payIncomeNote;
    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;
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