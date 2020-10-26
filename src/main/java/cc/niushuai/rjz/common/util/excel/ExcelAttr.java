package cc.niushuai.rjz.common.util.excel;

import java.io.Serializable;

/**
 * @author ns
 * @date 2019/12/12
 */
public class ExcelAttr implements Serializable {

    private static final long serialVersionUID = -6209130033397718707L;

    /**
     * 标题名称
     */
    private String title;
    /**
     * 属性字段名
     */
    private String attrName;
    /**
     * 排序号
     */
    private Integer order;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
