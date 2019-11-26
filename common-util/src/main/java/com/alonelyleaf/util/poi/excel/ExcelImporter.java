package com.alonelyleaf.util.poi.excel;

import com.alonelyleaf.util.ReflectUtil;
import com.alonelyleaf.util.ValidateUtil;
import com.alonelyleaf.util.poi.excel.annotation.ExcelField;
import jodd.bean.BeanUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * excel导入工具类
 */
public class ExcelImporter {

    private static Logger log = LoggerFactory.getLogger(ExcelImporter.class);

    /**
     * 工作薄对象
     */
    private Workbook wb;

    /**
     * 工作表对象
     */
    private Sheet sheet;

    /**
     * 标题行号
     */
    private int headerNum;

    /**
     * 构造函数
     *
     * @param fileName  导入文件，读取第一个工作表
     * @param headerNum 标题行号，数据行号=标题行号+1
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ExcelImporter(String fileName, int headerNum)
            throws InvalidFormatException, IOException {
        this(new File(fileName), headerNum);
    }

    /**
     * 构造函数
     *
     * @param file      导入文件对象，读取第一个工作表
     * @param headerNum 标题行号，数据行号=标题行号+1
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ExcelImporter(File file, int headerNum)
            throws InvalidFormatException, IOException {
        this(file, headerNum, 0);
    }

    /**
     * 构造函数
     *
     * @param fileName   导入文件
     * @param headerNum  标题行号，数据行号=标题行号+1
     * @param sheetIndex 工作表编号
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ExcelImporter(String fileName, int headerNum, int sheetIndex)
            throws InvalidFormatException, IOException {
        this(new File(fileName), headerNum, sheetIndex);
    }

    /**
     * 构造函数
     *
     * @param file       导入文件对象
     * @param headerNum  标题行号，数据行号=标题行号+1
     * @param sheetIndex 工作表编号
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ExcelImporter(File file, int headerNum, int sheetIndex)
            throws InvalidFormatException, IOException {
        this(file.getName(), new FileInputStream(file), headerNum, sheetIndex);
    }

    /**
     * 构造函数
     *
     * @param multipartFile 导入文件对象
     * @param headerNum     标题行号，数据行号=标题行号+1
     * @param sheetIndex    工作表编号
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ExcelImporter(MultipartFile multipartFile, int headerNum, int sheetIndex)
            throws InvalidFormatException, IOException {
        this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), headerNum, sheetIndex);
    }

    /**
     * 构造函数
     *
     * @param fileName   导入文件对象
     * @param headerNum  标题行号，数据行号=标题行号+1
     * @param sheetIndex 工作表编号
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ExcelImporter(String fileName, InputStream is, int headerNum, int sheetIndex)
            throws InvalidFormatException, IOException {
        if (ValidateUtil.isEmpty(fileName)) {
            throw new RuntimeException("导入文档为空!");
        } else if (fileName.toLowerCase().endsWith("xls")) {
            this.wb = new HSSFWorkbook(is);
        } else if (fileName.toLowerCase().endsWith("xlsx")) {
            this.wb = new XSSFWorkbook(is);
        } else {
            throw new RuntimeException("文档格式不正确!");
        }
        if (this.wb.getNumberOfSheets() < sheetIndex) {
            throw new RuntimeException("文档中没有工作表!");
        }
        this.sheet = this.wb.getSheetAt(sheetIndex);
        this.headerNum = headerNum;
        log.debug("Initialize success.");
    }

    /**
     * 获取行对象
     *
     * @param row
     * @return
     */
    public Row getRow(int row) {
        return this.sheet.getRow(row);
    }

    /**
     * 获取数据行号
     *
     * @return
     */
    public int getDataRowNum() {
        return headerNum + 1;
    }

    /**
     * 获取最后一个数据行号
     *
     * @return
     */
    public int getLastDataRowNum() {
        return this.sheet.getLastRowNum() + headerNum;
    }

    /**
     * 获取最后一个列号
     *
     * @return
     */
    public int getLastCellNum() {
        return this.getRow(headerNum).getLastCellNum();
    }

    /**
     * 获取单元格值
     *
     * @param row    获取的行
     * @param column 获取单元格列号
     * @return 单元格值
     */
    public Object getCellValue(Row row, int column) {
        Object val = "";
        try {
            Cell cell = row.getCell(column);
            if (cell != null) {
                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    val = cell.getNumericCellValue();
                } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    val = cell.getStringCellValue();
                } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                    val = cell.getCellFormula();
                } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                    val = cell.getBooleanCellValue();
                } else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
                    val = cell.getErrorCellValue();
                }
            }
        } catch (Exception e) {
            return val;
        }
        return val;
    }

    /**
     * 获取导入数据列表
     *
     * @param cls 导入对象类型
     */
    public <E> List<E> getDataList(Class<E> cls) throws InstantiationException, IllegalAccessException {
        List<Object[]> annotationList = new ArrayList<>();
        // Get annotation field
        Field[] fields = ReflectUtil.getAllFields(cls);
        for (Field field : fields) {
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            if (excelField != null) {
                annotationList.add(new Object[]{excelField, field});
            }
        }
        // Field sorting
        Collections.sort(annotationList, new Comparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return new Integer(((ExcelField) o1[0]).sort()).compareTo(
                        new Integer(((ExcelField) o2[0]).sort()));
            }

        });
        // Get excel data
        List<E> results = new ArrayList<>();
        for (int i = this.getDataRowNum() - 1; i < this.getLastDataRowNum(); i++) {
            E entity = (E) cls.newInstance();
            int column = 0;
            Row row = this.getRow(i);
            for (Object[] annotation : annotationList) {
                Object value = this.getCellValue(row, column++);
                if (value != null) {
                    // Get param type and type cast
                    Class<?> valType = Class.class;
                    if (annotation[1] instanceof Field) {
                        valType = ((Field) annotation[1]).getType();
                    }
                    try {
                        if (valType == String.class) {
                            value = String.valueOf(value.toString());
                        } else if (valType == Integer.class) {
                            value = Double.valueOf(value.toString()).intValue();
                        } else if (valType == Long.class) {
                            value = Double.valueOf(value.toString()).longValue();
                        } else if (valType == Double.class) {
                            value = Double.valueOf(value.toString());
                        } else if (valType == Float.class) {
                            value = Float.valueOf(value.toString());
                        } else if (valType == Date.class) {
                            value = DateUtil.getJavaDate((Double) value);
                        }
                    } catch (Exception ex) {
                        log.info("Get cell value [" + i + "," + column + "] error: " + ex.toString());
                        value = null;
                    }
                    // set domain value
                    if (annotation[1] instanceof Field) {
                        BeanUtil.declared.setProperty(entity, ((Field) annotation[1]).getName(), value);
                    }
                }
            }
            results.add(entity);
        }
        return results;
    }

}
