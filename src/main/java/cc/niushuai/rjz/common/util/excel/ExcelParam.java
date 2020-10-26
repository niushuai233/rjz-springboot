package cc.niushuai.rjz.common.util.excel;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Excel 导入导出所使用到的参数
 *
 * @author ns
 * @date 2019/12/7 9:40
 **/
public class ExcelParam implements Serializable {

    private static final long serialVersionUID = -4231868339831975335L;
    /**
     * 文件地址,本地读取时用
     */
    private String filePath;
    /**
     * 反射类
     */
    private Class clazz;
    /**
     * 对表头进行强校验
     */
    private boolean sameHeader;
    /**
     * 从第几行开始扫描
     */
    private Integer rowNumIndex;
    /**
     * 存储属性和表头的对应关系
     */
    private Map map;
    /**
     * 是否流读取
     */
    private Boolean stream = false;
    /**
     * 用流代替本地文件
     */
    private byte[] buf;
    /**
     * 输出流
     */
    private HttpServletResponse response;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件输出路径
     */
    private String outFilePath;
    /**
     * 文件导出封装数据
     */
    private List list;
    /**
     * 文件导出封装数据
     */
    private List<ExcelAttr> excelAttrList;

    public List<ExcelAttr> getExcelAttrList() {
        return excelAttrList;
    }

    public void setExcelAttrList(List<ExcelAttr> excelAttrList) {
        this.excelAttrList = excelAttrList;
    }

    public boolean getSameHeader() {
        return sameHeader;
    }

    public void setSameHeader(boolean sameHeader) {
        this.sameHeader = sameHeader;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Integer getRowNumIndex() {
        return rowNumIndex;
    }

    public void setRowNumIndex(Integer rowNumIndex) {
        this.rowNumIndex = rowNumIndex;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }


    public Boolean getStream() {
        return stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }

    public byte[] getBuf() {
        return buf;
    }

    public void setBuf(byte[] buf) {
        this.buf = buf;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOutFilePath() {
        return outFilePath;
    }

    public void setOutFilePath(String outFilePath) {
        this.outFilePath = outFilePath;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

}
