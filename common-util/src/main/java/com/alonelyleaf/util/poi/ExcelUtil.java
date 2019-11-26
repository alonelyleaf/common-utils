package com.alonelyleaf.util.poi;

import com.alonelyleaf.util.BeanUtil;
import com.alonelyleaf.util.ReflectUtil;
import com.alonelyleaf.util.ValidateUtil;
import com.alonelyleaf.util.exception.business.BaseException;
import com.alonelyleaf.util.poi.excel.ExcelImporter;
import com.alonelyleaf.util.poi.excel.MyJxlsHelper;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alonelyleaf.util.CommonUtil.asMap;

/**
 * Excel工具类
 *
 */
public class ExcelUtil {

    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 导出数据
     *
     * @param templateFile
     * @param out
     * @param params
     */
    public static void exportData(String templateFile, OutputStream out, Map params) {

        innerExport(templateFile, out, params);
    }

    /**
     * 导出数据
     *
     * @param in
     * @param out
     * @param params
     */
    public static void exportData(InputStream in, OutputStream out, Map params) {

        innerExport(in, out, params);
    }

    /**
     * 导出数据, 多个sheet
     *
     * @param in
     * @param out
     * @param outputList
     * @param amountPerSheet
     * @param sheetName
     * @param beanName
     */
    public static void innerExportMultiSheet(InputStream in, OutputStream out, List outputList, int amountPerSheet, String sheetName, String beanName) {

        //数据数量不超过1个sheet，则直接导出，不分sheet
        if (outputList.size() <= amountPerSheet) {

            innerExport(in, out, asMap(beanName, outputList));
        } else {

            List<List> listForSheet = new ArrayList<>();
            List<String> sheetNames = new ArrayList<>();

            for (int i = 0; i < (int) Math.ceil((double) outputList.size() / amountPerSheet); i++) {

                int k = ((i + 1) * amountPerSheet < outputList.size()) ? ((i + 1) * amountPerSheet) : outputList.size();
                List resultList = outputList.subList(i * amountPerSheet, k);

                String perSheetName = sheetName + "_" + String.valueOf(i+1);

                listForSheet.add(resultList);
                sheetNames.add(perSheetName);
            }
            innerExportMultiSheet(in, out, listForSheet, sheetNames, beanName);
        }
    }

    /**
     * 导出数据, 多个sheet
     *
     * @param in
     * @param out
     * @param params
     * @param sheetNames
     * @param beanName
     */
    private static void innerExportMultiSheet(InputStream in, OutputStream out, List params, List sheetNames, String beanName) {
        XLSTransformer transformer = new XLSTransformer();

        InputStream is = null;
        OutputStream os = null;
        try {
            is = new BufferedInputStream(in);
            Workbook workbook = transformer.transformMultipleSheetsList(is, params, sheetNames, beanName, new HashMap(), 0);
            os = new BufferedOutputStream(out);

            workbook.write(os);
            os.flush();
        } catch (Exception e) {
            logger.error("Excel导出出错>>", e);
            throw new BaseException(e);
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                logger.error("Excel导出出错>>", e);
            }
        }
    }

    /**
     * 导出数据，jxls2支持
     *
     * @param in 模板
     * @param out 输出
     * @param params
     * @param templateSheetName 模板sheet名称，导出多sheet时使用，导出单sheet时，填null
     */
    public static void innerExportMultiSheetWithJxls2(InputStream in, OutputStream out, Map<String, Object> params, String templateSheetName) {

        Context context = new Context(params);

        try {
            MyJxlsHelper.getInstance()
                    .setTemplateSheetName(templateSheetName)
                    .processTemplate(in, out, context);
        } catch (Exception e) {
            logger.error("Excel导出出错>>", e);
            throw new BaseException(e);
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                logger.error("Excel导出出错>>", e);
            }
        }
    }

    /**
     * 导出数据，jxls2支持
     *
     * @param in
     * @param out
     * @param params
     */
    public static void exportDataWithJxls2(InputStream in, OutputStream out, Map<String, Object> params) {

        innerExport2(in, out, params);
    }

    /**
     * 导入数据，从第2行读起，且移除空行
     *
     * @param file
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> importData(String file, Class<T> clazz) {

        return importData(file, clazz, new RemoveEmptyRow());
    }

    /**
     * 导入数据，从第2行读起，且移除空行
     *
     * @param file
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> importData(MultipartFile file, Class<T> clazz) {

        return importData(file, clazz, new RemoveEmptyRow());
    }

    /**
     * 导入数据
     *
     * @param file
     * @param headerNum 标题行号，数据行号=标题行号+1
     * @param clazz
     * @param filter
     * @param <T>
     * @return
     */
    public static <T> List<T> importData(MultipartFile file, int headerNum, Class<T> clazz, Filter filter) {

        return filter.accept(innerImport(file, headerNum, clazz));
    }

    /**
     * 导入数据，从第2行读起
     *
     * @param file
     * @param clazz
     * @param filter
     * @param <T>
     * @return
     */
    public static <T> List<T> importData(MultipartFile file, Class<T> clazz, Filter filter) {

        return importData(file, 1, clazz, filter);
    }

    /**
     * 导入数据
     *
     * @param file
     * @param headerNum 标题行号，数据行号=标题行号+1
     * @param clazz
     * @param filter
     * @param <T>
     * @return
     */
    public static <T> List<T> importData(String file, int headerNum, Class<T> clazz, Filter filter) {

        return filter.accept(innerImport(file, headerNum, clazz));
    }

    /**
     * 导入数据，从第2行读起
     *
     * @param file
     * @param clazz
     * @param filter
     * @param <T>
     * @return
     */
    public static <T> List<T> importData(String file, Class<T> clazz, Filter filter) {

        return importData(file, 1, clazz, filter);
    }

    /**
     * 导入数据，从第2行读起
     *
     * @param file
     * @param clazz
     * @param filename
     * @param <T>
     * @return
     */
    public static <T> List<T> importData(String filename, InputStream file, int headerNum, Class<T> clazz, Filter filter) {

        return filter.accept(innerImport(filename, file, headerNum, clazz));
    }


    //--------------------------------support------------------------------------

    /**
     * 导入结果处理
     */
    public interface Filter {

        <T> List<T> accept(List<T> list);
    }

    /**
     * 移除整行为空的数据
     */
    public static class RemoveEmptyRow implements Filter {

        @Override
        public <T> List<T> accept(List<T> list) {
            List<T> result = new ArrayList<>();
            for (T each : list) {
                for (Field f : ReflectUtil.getAllFields(each.getClass())) {
                    f.setAccessible(true);

                    if ("row".equals(f.getName())) {
                        continue;
                    }
                    try {
                        if (ValidateUtil.isNotEmpty(f.get(each))) {
                            result.add(each);
                            break;
                        }
                    } catch (IllegalAccessException e) {
                        throw new BaseException(e.getMessage(), e);
                    }
                }
            }
            return result;
        }
    }

    /**
     * 移除某个字段为空的数据
     */
    public static class RemoveByEmptyField implements Filter {

        private String field;

        public RemoveByEmptyField(String field) {
            this.field = field;
        }

        @Override
        public <T> List<T> accept(List<T> list) {
            if (ValidateUtil.isEmpty(list)) {
                return list;
            }

            List<T> result = new ArrayList<>();
            for (T each : list) {
                each(result, each);
            }
            return result;
        }

        protected <T> void each(List<T> result, T each) {
            if (ValidateUtil.isNotEmpty(BeanUtil.pojo.getProperty(each, field))) {
                result.add(each);
            }
        }
    }

    //--------------------------------impl------------------------------------

    /**
     * 导入数据
     *
     * @param file
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> List<T> innerImport(MultipartFile file, int headerNum, Class<T> clazz) {

        if (ValidateUtil.isEmpty(file)) {
            return new ArrayList<>();
        }

        ExcelImporter importer;
        try {
            importer = new ExcelImporter(file, headerNum, 0);
            return appendRow(importer.getDataList(clazz), headerNum);
        } catch (Exception e) {
            logger.error("Excel导入出错>>" + file.getName(), e);
            throw new BaseException(e);
        }
    }

    /**
     * 导入数据
     *
     * @param file
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> List<T> innerImport(String file, int headerNum, Class<T> clazz) {

        if (ValidateUtil.isEmpty(file)) {
            return new ArrayList<>();
        }

        ExcelImporter importer;
        try {
            importer = new ExcelImporter(file, headerNum, 0);
            return appendRow(importer.getDataList(clazz), headerNum);

        } catch (Exception e) {
            logger.error("Excel导入出错>>" + file, e);
            throw new BaseException(e);
        }
    }

    /**
     * 导入数据
     *
     * @param file
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> List<T> innerImport(String filename, InputStream file, int headerNum, Class<T> clazz) {

        if (ValidateUtil.isEmpty(file)) {
            return new ArrayList<>();
        }

        ExcelImporter importer;
        try {
            importer = new ExcelImporter(filename, file, headerNum, 0);
            return appendRow(importer.getDataList(clazz), headerNum);

        } catch (Exception e) {
            logger.error("Excel导入出错>>" + file, e);
            throw new BaseException(e);
        }
    }

    private static void innerExport(String templateFile, OutputStream out, Map params) {
        try {
            innerExport(new FileInputStream(templateFile), out, params);
        } catch (FileNotFoundException e) {
            logger.error("Excel导出出错>>", e);
            throw new BaseException(e);
        }
    }

    private static void innerExport(InputStream in, OutputStream out, Map params) {
        XLSTransformer transformer = new XLSTransformer();

        InputStream is = null;
        OutputStream os = null;
        try {
            is = new BufferedInputStream(in);
            org.apache.poi.ss.usermodel.Workbook workbook = transformer.transformXLS(is, params);
            os = new BufferedOutputStream(out);
            workbook.write(os);
            os.flush();
        } catch (Exception e) {
            logger.error("Excel导出出错>>", e);
            throw new BaseException(e);
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                logger.error("Excel导出出错>>", e);
            }
        }
    }

    public static void innerExport2(InputStream in, OutputStream out, Map<String, Object> params) {

        Context context = new Context(params);

        try {
            JxlsHelper.getInstance().processTemplate(in, out, context);
        } catch (Exception e) {
            logger.error("Excel导出出错>>", e);
            throw new BaseException(e);
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                logger.error("Excel导出出错>>", e);
            }
        }
    }

    private static <T> List<T> appendRow(List<T> list, int headNum) {
        if (ValidateUtil.isNotEmpty(list)) {
            int i = headNum + 1;
            for (T each : list) {
                BeanUtil.silent.setProperty(each, "row", i++);
            }
        }
        return list;
    }
}
