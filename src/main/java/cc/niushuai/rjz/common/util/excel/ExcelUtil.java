package cc.niushuai.rjz.common.util.excel;

import cc.niushuai.rjz.bill.entity.BillNote;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.netty.util.internal.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * excel 工具类
 *
 * @author ns
 * @return
 * @date 2019/12/7 10:52
 **/
public class ExcelUtil implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelUtil.class);

    private static final long serialVersionUID = -4636785084999832785L;


    private static Map<String, String> getTitleAttrMappingFromJsonAttr(Class<?> clazz, List<ExcelAttr> excelAttrList) throws NoSuchFieldException {

        Map<String, String> map = new HashMap<String, String>();

        for (ExcelAttr excelAttr : excelAttrList) {
            clazz.getDeclaredField(excelAttr.getAttrName());
            map.put(excelAttr.getTitle(), excelAttr.getAttrName());
        }

        return map;
    }

    private static List<String> getOrderedListFromJsonAttr(List<ExcelAttr> excelAttrList) throws NoSuchFieldException {

        List<String> keyList = new ArrayList<String>();

        excelAttrList.sort(Comparator.comparingInt(ExcelAttr::getOrder));

        excelAttrList.forEach(attr -> keyList.add(attr.getTitle()));

        return keyList;
    }

    /**
     * 读取ExcelField 包装到到Map中
     *
     * @param clazz
     * @return List<String>
     *
     * @author ns
     * @date 2019/12/7 11:18
     **/
    private static Map<String, String> getTitleAttrMappingMap(Class<?> clazz) throws Exception {

        Map<String, String> map = new HashMap<String, String>();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = clazz.getDeclaredField(fields[i].getName());
            field.setAccessible(true);
            ExcelField column = field.getAnnotation(ExcelField.class);
            if (column != null) {
                // 别名优先
                String title = column.title();
                if (column.enableAlias()) {
                    title = column.alias();
                }
                map.put(title, field.getName());
            }
        }
        if (map.size() == 0) {
            throw new RuntimeException(clazz + " 并不存在任何字段使用了 @ExcelField 注解");
        }
        return map;
    }

    /**
     * 读取ExcelField 包装到到List中
     *
     * @param clazz
     * @return List<String>
     *
     * @author ns
     * @date 2019/12/7 11:18
     **/
    private static List<String> getList(Class<?> clazz) throws NoSuchFieldException {

        Field[] fields = clazz.getDeclaredFields();
        // 排序map
        Map<Integer, String> orderMap = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            Field field = clazz.getDeclaredField(fields[i].getName());
            field.setAccessible(true);
            ExcelField column = field.getAnnotation(ExcelField.class);
            if (column != null) {
                String title = column.title();
                // 别名优先
                if (column.enableAlias()) {
                    title = column.alias();
                }
                orderMap.put(column.order(), title);
            }
        }

        if (orderMap.size() == 0) {
            throw new RuntimeException(clazz + " 并不存在任何字段使用了 @ExcelField 注解");
        }

        Integer[] keyArray = new Integer[orderMap.size()];
        keyArray = orderMap.keySet().toArray(keyArray);

        Arrays.sort(keyArray);

        List<String> resultList = new ArrayList<String>();
        for (Integer index : keyArray) {
            resultList.add(orderMap.get(index));
        }
        // 按照order排序后的key集合
        return resultList;
    }

    /**
     * 根据参数配置读取结果
     *
     * @param excelParam
     * @return java.util.List
     *
     * @author ns
     * @date 2019/12/7 10:53
     **/
    private static List getResult(ExcelParam excelParam) throws Exception {
        if (null != excelParam.getExcelAttrList() && excelParam.getExcelAttrList().size() > 0) {
            // 从json配置中查找
            LOG.info("从jsonAttr获取相关属性");
            excelParam.setMap(getTitleAttrMappingFromJsonAttr(excelParam.getClazz(), excelParam.getExcelAttrList()));
        } else {
            // 从class中的注解自动查找
            LOG.info("从类注解获取相关属性");
            excelParam.setMap(getTitleAttrMappingMap(excelParam.getClazz()));
        }

        Set keySet = excelParam.getMap().keySet();
        // 读取到的结果集
        List<Object> resultList = new ArrayList<Object>();
        // 文件类型
        String fileType = "";
        InputStream is = null;
        Workbook wb = null;
        if (excelParam.getStream()) {
            is = new ByteArrayInputStream(excelParam.getBuf());
            wb = WorkbookFactory.create(is);
        } else {
            fileType = excelParam.getFilePath().substring(excelParam.getFilePath().lastIndexOf(".") + 1);
            is = new FileInputStream(excelParam.getFilePath());
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook(is);
            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook(is);
            } else {
                throw new RuntimeException("您输入的excel格式不正确");
            }
        }

        // 记录第x行为表头
        int headerRowNum = -1;
        // 存放每一个field字段对应所在的列的序号
        Map<String, Integer> cellMap = new HashMap<String, Integer>();
        // 存放所有的表头字段信息
        List<String> headList = new ArrayList();

        Sheet sheet = wb.getSheetAt(0);

        // 循环行Row
        for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {

            if (excelParam.getRowNumIndex() != null && headerRowNum == -1) {
                // 如果传值指定从第几行开始读，就从指定行寻找，否则自动寻找
                Row row = sheet.getRow(excelParam.getRowNumIndex());
                if (row == null) {
                    throw new RuntimeException("指定的行为空，请检查");
                }
                rowNum = excelParam.getRowNumIndex() - 1;
            }
            // 开始读取数据
            Row numRow = sheet.getRow(rowNum);
            if (numRow == null) {
                continue;
            }
            boolean cellValueExistFlag = false;
            for (int i = 0; i < numRow.getLastCellNum(); i++) {
                if (numRow.getCell(i) != null && !("").equals(numRow.getCell(i).toString().trim())) {
                    cellValueExistFlag = true;
                }
            }
            if (!cellValueExistFlag) {
                continue;
            }

            if (headerRowNum == -1) {
                // 循环列Cell
                for (int cellNum = 0; cellNum <= numRow.getLastCellNum(); cellNum++) {

                    Cell hssfCell = numRow.getCell(cellNum);
                    if (hssfCell == null) {
                        continue;
                    }

                    String tempCellValue = sheet.getRow(rowNum).getCell(cellNum).getStringCellValue();

                    if (StrUtil.isNotEmpty(tempCellValue)) {
                        tempCellValue = tempCellValue.trim();
                    }

                    headList.add(tempCellValue);

                    Iterator it = keySet.iterator();

                    while (it.hasNext()) {
                        Object key = it.next();
                        if (StrUtil.isNotEmpty(tempCellValue)
                                && tempCellValue.equalsIgnoreCase(key.toString())) {
                            headerRowNum = rowNum;
                            cellMap.put(excelParam.getMap().get(key).toString(), cellNum);
                        }
                    }
                    if (headerRowNum == -1) {
                        throw new RuntimeException("没有找到对应的字段或者对应字段行上面含有不为空白的行字段");
                    }
                }

                if (excelParam.getSameHeader()) {
                    // 读取到列后，检查表头是否完全一致--start
                    for (int i = 0; i < headList.size(); i++) {
                        boolean boo = false;
                        Iterator itor = keySet.iterator();
                        while (itor.hasNext()) {
                            String tempName = itor.next().toString();
                            if (tempName.equals(headList.get(i))) {
                                boo = true;
                            }
                        }
                        if (boo == false) {
                            throw new RuntimeException("表头字段和定义的属性字段不匹配，请检查");
                        }
                    }

                    Iterator itor = keySet.iterator();
                    while (itor.hasNext()) {
                        boolean boo = false;
                        String tempname = itor.next().toString();
                        for (int i = 0; i < headList.size(); i++) {
                            if (tempname.equals(headList.get(i))) {
                                boo = true;
                            }
                        }
                        if (boo == false) {
                            throw new RuntimeException("表头字段和定义的属性字段不匹配，请检查");
                        }
                    }
                    // 读取到列后，检查表头是否完全一致--end
                }

            } else {
                Object obj = excelParam.getClazz().newInstance();
                Iterator it = keySet.iterator();
                while (it.hasNext()) {
                    Object key = it.next();
                    Integer cellNum_x = cellMap.get(excelParam.getMap().get(key).toString());
                    if (cellNum_x == null || numRow.getCell(cellNum_x) == null) {
                        continue;
                    }
                    // 得到属性
                    String attr = excelParam.getMap().get(key).toString();

                    Class<?> attrType = BeanUtils.findPropertyType(attr, new Class[]{obj.getClass()});

                    Cell cell = numRow.getCell(cellNum_x);
                    getValue(cell, obj, attr, attrType, rowNum, cellNum_x, key);

                }
                resultList.add(obj);
            }

        }
        is.close();
        return resultList;
    }

    public static Workbook commonExportExcel(ExcelParam excelParam, int x) throws Exception {
        Map<String, String> map;
        List<String> keyList;
        if (null != excelParam.getExcelAttrList() && excelParam.getExcelAttrList().size() > 0) {
            LOG.info("从jsonAttr获取相关属性");
            map = getTitleAttrMappingFromJsonAttr(excelParam.getClazz(), excelParam.getExcelAttrList());
            keyList = getOrderedListFromJsonAttr(excelParam.getExcelAttrList());
        } else {
            LOG.info("从类注解获取相关属性");
            map = getTitleAttrMappingMap(excelParam.getClazz());
            keyList = getList(excelParam.getClazz());
        }

        Object obj = excelParam.getClazz().newInstance();
        // 创建HSSFWorkbook对象(excel的文档对象)
        Workbook wb = new XSSFWorkbook();
        // 建立新的sheet对象（excel的表单）
        Sheet sheet = wb.createSheet("sheet1");
        // 声明样式
        CellStyle style = wb.createCellStyle();
        // 居中显示
        style.setAlignment(HorizontalAlignment.CENTER);
        // 在sheet里创建第一行为表头，参数为行索引(excel的行)
        Row rowHeader = sheet.createRow(0);
        // 创建单元格并设置单元格内容

        // 存储属性信息
        Map<String, String> attMap = new HashMap();

        int index = 0;
        for (String key : keyList) {
            rowHeader.createCell(index).setCellValue(key);
            attMap.put(Integer.toString(index), map.get(key));

            sheet.setColumnWidth(index, 4500);
            index++;
        }

        // 在sheet里创建表头下的数据
        for (int i = 0; i < excelParam.getList().size(); i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < map.size(); j++) {
                Class<?> attrType = BeanUtils.findPropertyType(attMap.get(Integer.toString(j)),
                        new Class[]{obj.getClass()});
                Object value = getAttrValue(excelParam.getList().get(i), attMap.get(Integer.toString(j)), attrType);
                if (null == value) {
                    value = "";
                }
                row.createCell(j).setCellValue(value.toString());
                style.setAlignment(HorizontalAlignment.CENTER);
            }
        }

        return wb;
    }

    /**
     * 通用导出方法
     *
     * @param excelParam
     * @return void
     *
     * @author ns
     * @date 2019/12/7 13:00
     **/
    private static void commonExportExcel(ExcelParam excelParam) throws Exception {
        Map<String, String> map;
        List<String> keyList;
        if (null != excelParam.getExcelAttrList() && excelParam.getExcelAttrList().size() > 0) {
            LOG.info("从jsonAttr获取相关属性");
            map = getTitleAttrMappingFromJsonAttr(excelParam.getClazz(), excelParam.getExcelAttrList());
            keyList = getOrderedListFromJsonAttr(excelParam.getExcelAttrList());
        } else {
            LOG.info("从类注解获取相关属性");
            map = getTitleAttrMappingMap(excelParam.getClazz());
            keyList = getList(excelParam.getClazz());
        }

        Object obj = excelParam.getClazz().newInstance();
        // 创建HSSFWorkbook对象(excel的文档对象)
        Workbook wb = new XSSFWorkbook();
        // 建立新的sheet对象（excel的表单）
        Sheet sheet = wb.createSheet("sheet1");
        // 声明样式
        CellStyle style = wb.createCellStyle();
        // 居中显示
        style.setAlignment(HorizontalAlignment.CENTER);
        // 在sheet里创建第一行为表头，参数为行索引(excel的行)
        Row rowHeader = sheet.createRow(0);
        // 创建单元格并设置单元格内容

        // 存储属性信息
        Map<String, String> attMap = new HashMap();

        int index = 0;
        for (String key : keyList) {
            rowHeader.createCell(index).setCellValue(key);
            attMap.put(Integer.toString(index), map.get(key));

            sheet.setColumnWidth(index, 4500);
            index++;
        }

        // 在sheet里创建表头下的数据
        for (int i = 0; i < excelParam.getList().size(); i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < map.size(); j++) {
                Class<?> attrType = BeanUtils.findPropertyType(attMap.get(Integer.toString(j)),
                        new Class[]{obj.getClass()});
                Object value = getAttrValue(excelParam.getList().get(i), attMap.get(Integer.toString(j)), attrType);
                if (null == value) {
                    value = "";
                }
                row.createCell(j).setCellValue(value.toString());
                style.setAlignment(HorizontalAlignment.CENTER);
            }
        }

        String newFileName = excelParam.getFileName();
        // 如果文件名为空 直接输出当前时间戳为名字的文件
        if (StrUtil.isEmpty(newFileName)) {
            newFileName = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        }

        // 输出Excel文件
        try {
            if (excelParam.getResponse() != null) {
                OutputStream outStream = excelParam.getResponse().getOutputStream();
                excelParam.getResponse().reset();
                excelParam.getResponse().setHeader("Content-disposition",
                        "attachment; filename=" + new String(newFileName.getBytes(), "ISO-8859-1") + ".xlsx");
                excelParam.getResponse().setContentType("application/x-download");
                wb.write(outStream);
                outStream.close();
            } else {
                FileOutputStream out = new FileOutputStream(excelParam.getOutFilePath());
                wb.write(out);
                out.close();
            }

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("导出失败！" + e);
        } catch (IOException e) {
            throw new IOException("导出失败！" + e);
        }

    }

    /**
     * 赋予对象属性值
     *
     * @param obj   待赋值的对象
     * @param att   对象属性名
     * @param value 要赋值的value
     * @param type  待赋值的对象bean类型
     * @param row   excel行数
     * @param col   excel列数
     * @param key   属性字段
     * @return void
     *
     * @author ns
     * @date 2019/12/7 12:31
     **/
    private static void setAttrValue(Object obj, String att, Object value, Class<?> type, int row, int col, Object key)
            throws RuntimeException {
        try {
            Method method = obj.getClass().getMethod("set" + StrUtil.upperFirst(att), type);
            LOG.debug("{}, \t{}, \t{}, \t{}", method.getName(), att, value, type);
            // 如果值为空 直接置null
            if (ObjectUtils.isEmpty(value)) {
                Field field = obj.getClass().getDeclaredField(att);
                field.setAccessible(true);
                field.set(obj, null);
            } else {
                // 如果是日期类型
                if (type.isAssignableFrom(Date.class)) {
                    value = DateUtil.parse(value.toString(), "yyyy-MM-dd HH:mm:ss");
                } else if (type.isAssignableFrom(Integer.class)) {
                    value = Integer.parseInt(value.toString());
                } else if (type.isAssignableFrom(Long.class)) {
                    value = Long.parseLong(value.toString());
                } else if (type.isAssignableFrom(Double.class)) {
                    value = Double.parseDouble(value.toString());
                }
                method.invoke(obj, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("第" + (row + 1) + "行,第" + (col + 1) + "列 属性：" + key + " 赋值异常.\n原属性:" + type + "值:" + value);
        }
    }

    /**
     * 从对象中根据字段属性来获取对应值
     *
     * @param obj  待获取属性值的对象
     * @param att  待获取属性值的对象bean类型
     * @param type 属性字段类型
     * @return Object 属性字段值
     *
     * @author ns
     * @date 2019/12/7 12:33
     **/
    private static Object getAttrValue(Object obj, String att, Class<?> type) throws Exception {
        try {
            Method method = obj.getClass().getMethod("get" + StrUtil.upperFirst(att));
            Object value = method.invoke(obj);
            if (type.isAssignableFrom(Date.class)) {
                value = DateUtil.formatDateTime((Date) value);
            }
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @param cell
     * @param obj
     * @param attr
     * @param attrType
     * @param row
     * @param col
     * @param key
     * @return void
     *
     * @Function 得到Excel列的值
     * @author likaixuan
     * @Date 2019-07-05 15:07
     */
    @SuppressWarnings("deprecation")
    public static void getValue(Cell cell, Object obj, String attr, Class attrType, int row, int col, Object key)
            throws Exception {
        Object val = null;

        if (cell.getCellType() == CellType.BOOLEAN) {
            val = cell.getBooleanCellValue();

        } else if (cell.getCellType() == CellType.NUMERIC) {
            if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    if (attrType == String.class) {
                        val = sdf.format(org.apache.poi.ss.usermodel.DateUtil.getJavaDate(cell.getNumericCellValue()));
                    } else {
                        val = dateConvertFormat(sdf.format(org.apache.poi.ss.usermodel.DateUtil.getJavaDate(cell.getNumericCellValue())));
                    }
                } catch (ParseException e) {
                    throw new RuntimeException("第" + (row + 1) + " 行  " + (col + 1) + "列   属性：" + key + " 日期格式转换错误  ");
                }
            } else {
                if (attrType == String.class) {
                    cell.setCellType(CellType.STRING);
                    val = cell.getStringCellValue();
                } else if (attrType == BigDecimal.class) {
                    val = new BigDecimal(cell.getNumericCellValue());
                } else if (attrType == long.class) {
                    val = (long) cell.getNumericCellValue();
                } else if (attrType == Double.class) {
                    val = cell.getNumericCellValue();
                } else if (attrType == Float.class) {
                    val = (float) cell.getNumericCellValue();
                } else if (attrType == int.class || attrType == Integer.class) {
                    val = (int) cell.getNumericCellValue();
                } else if (attrType == Short.class) {
                    val = (short) cell.getNumericCellValue();
                } else {
                    val = cell.getNumericCellValue();
                }
            }

        } else if (cell.getCellType() == CellType.STRING) {
            if (attrType.equals(double.class) || attrType.equals(Double.class)) {
                val = Double.parseDouble(cell.getStringCellValue());
            } else {
                val = cell.getStringCellValue();
            }

        }

        setAttrValue(obj, attr, val, attrType, row, col, key);
    }

    /**
     * String类型日期转为Date类型
     *
     * @param dateStr
     * @return java.util.Date
     *
     * @throws Exception
     * @author likaixuan
     * @Date 2019-07-05 16:45
     */
    private static Date dateConvertFormat(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(dateStr);
        return date;
    }

    /**
     * 从磁盘文件中读取
     *
     * @param filePath 磁盘文件路径
     * @param clazz    读取类型
     * @return List<T> 读取结果
     *
     * @author ns
     * @date 2019/12/7 11:58
     **/
    public static <T> List<T> read(String filePath, Class<T> clazz, int rowNumIndex) throws Exception {
        return read(filePath, clazz, rowNumIndex, null);
    }

    /**
     * 从磁盘文件中读取
     *
     * @param filePath 磁盘文件路径
     * @param clazz    读取类型
     * @return List<T> 读取结果
     *
     * @author ns
     * @date 2019/12/7 11:58
     **/
    public static <T> List<T> read(String filePath, Class<T> clazz, int rowNumIndex, String jsonAttr) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException(filePath + " not found");
        } else if (file.isDirectory()) {
            throw new RuntimeException(filePath + " is a directory");
        }

        InputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputStream.read(buffer);

        return read(buffer, clazz, 1, jsonAttr);
    }

    /**
     * 使用流读取Excel
     *
     * @param buf         字节流
     * @param clazz       需要读取的bean类型
     * @param rowNumIndex 开始读取数据的行数
     * @return List<T> 读取结果
     *
     * @throws Exception
     */
    public static <T> List<T> read(byte[] buf, Class<T> clazz, int rowNumIndex) throws Exception {

        return read(buf, clazz, rowNumIndex, null);
    }

    /**
     * 使用流读取Excel
     *
     * @param buf         字节流
     * @param clazz       需要读取的bean类型
     * @param rowNumIndex 开始读取数据的行数
     * @return List<T> 读取结果
     *
     * @throws Exception
     */
    public static <T> List<T> read(byte[] buf, Class<T> clazz, int rowNumIndex, String jsonAttr) throws Exception {

        ExcelParam excelParam = new ExcelParam();
        excelParam.setClazz(clazz);
        excelParam.setStream(true);
        excelParam.setBuf(buf);
        excelParam.setRowNumIndex(rowNumIndex);

        if (StrUtil.isNotEmpty(jsonAttr)) {
            List<ExcelAttr> excelAttrs = JSONUtil.toBean(jsonAttr, List.class);
            excelParam.setExcelAttrList(excelAttrs);
        }

        return getResult(excelParam);
    }

    /**
     * <p>导出Excel到指定磁盘位置</p>
     * <p>从ExcelField读取字段作为表头</p>
     *
     * @param outFilePath 磁盘位置
     * @param list        待导出数据
     * @param clazz       JavaBean类型
     * @return void
     *
     * @author ns
     * @date 2019/12/7 10:54
     **/
    public static <T> void exportExcel2Disk(String outFilePath, List<T> list, Class<T> clazz) throws Exception {
        exportExcel2Disk(outFilePath, list, clazz, null);
    }

    /**
     * <p>导出Excel到指定磁盘位置</p>
     * <p>从ExcelField读取字段作为表头</p>
     *
     * @param outFilePath 磁盘位置
     * @param list        待导出数据
     * @param clazz       JavaBean类型
     * @return void
     *
     * @author ns
     * @date 2019/12/7 10:54
     **/
    public static <T> void exportExcel2Disk(String outFilePath, List<T> list, Class<T> clazz, String jsonAttr) throws Exception {
        if (StrUtil.isEmpty(outFilePath)) {
            throw new RuntimeException("导出磁盘位置不能为空");
        }


        ExcelParam excelParam = new ExcelParam();
        excelParam.setOutFilePath(outFilePath);
        excelParam.setList(list);
        excelParam.setClazz(clazz);

        if (StrUtil.isNotEmpty(jsonAttr)) {
            List<ExcelAttr> excelAttrs = JSONUtil.toBean(jsonAttr, List.class);
            excelParam.setExcelAttrList(excelAttrs);
        }

        commonExportExcel(excelParam);
    }

    /**
     * <p>不导出 只读取 返回workbook</p>
     * <p>从ExcelField读取字段作为表头</p>
     *
     * @param outFilePath 磁盘位置
     * @param list        待导出数据
     * @param clazz       JavaBean类型
     * @return void
     *
     * @author ns
     * @date 2019/12/7 10:54
     **/
    public static <T> Workbook getWorkBook(String outFilePath, List<T> list, Class<T> clazz, String jsonAttr) throws Exception {
        if (StrUtil.isEmpty(outFilePath)) {
            throw new RuntimeException("导出磁盘位置不能为空");
        }

        ExcelParam excelParam = new ExcelParam();
        excelParam.setOutFilePath(outFilePath);
        excelParam.setList(list);
        excelParam.setClazz(clazz);

        if (StrUtil.isNotEmpty(jsonAttr)) {
            List<ExcelAttr> excelAttrs = JSONUtil.toBean(jsonAttr, List.class);
            excelParam.setExcelAttrList(excelAttrs);
        }

        return commonExportExcel(excelParam, 1);
    }

    /**
     * 导出到浏览器
     *
     * @param response 响应对象
     * @param list     导出结果集
     * @param clazz    导出结果 bean 类型
     * @param fileName 导出文件名
     * @return void
     *
     * @author ns
     * @date 2019/12/7 12:28
     **/
    public static <T> void exportExcel2Stream(HttpServletResponse response, List<T> list, Class<T> clazz, String fileName) throws Exception {

        exportExcel2Stream(response, list, clazz, fileName, null);
    }

    /**
     * 导出到浏览器
     *
     * @param response 响应对象
     * @param list     导出结果集
     * @param clazz    导出结果 bean 类型
     * @param fileName 导出文件名
     * @return void
     *
     * @author ns
     * @date 2019/12/7 12:28
     **/
    public static <T> void exportExcel2Stream(HttpServletResponse response, List<T> list, Class<T> clazz, String fileName, String jsonAttr) throws Exception {

        if (StrUtil.isEmpty(fileName)) {
            throw new RuntimeException("导出文件名不能为空");
        }

        ExcelParam excelParam = new ExcelParam();
        excelParam.setResponse(response);
        excelParam.setList(list);
        excelParam.setClazz(clazz);
        excelParam.setFileName(fileName);

        if (StrUtil.isNotEmpty(jsonAttr)) {
            List<ExcelAttr> excelAttrs = JSONUtil.toBean(jsonAttr, List.class);
            excelParam.setExcelAttrList(excelAttrs);
        }

        commonExportExcel(excelParam);
    }


}


