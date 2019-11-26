package com.alonelyleaf.util.excel;

import com.alonelyleaf.util.ValidateUtil;
import com.alonelyleaf.util.excel.support.ExcelInput;
import com.alonelyleaf.util.excel.support.MockMultipartFile;
import com.alonelyleaf.util.excel.support.PersonVO;
import com.alonelyleaf.util.poi.ExcelUtil;
import jodd.util.StringPool;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.alonelyleaf.util.CommonUtil.asMap;

/**
 * @author bijl
 * @date 2019/11/26
 */
public class ExcelUtilTest {

    private static final String importFilePath = "C:\\Users\\yl1887\\Desktop\\导出测试.xls";

    private static final String exportFilePath = "C:\\Users\\yl1887\\Desktop\\导出测试.xls";

    private static final String templatePath = "C:\\Users\\yl1887\\Desktop\\template.xls";

    private static final String multiSheetsTemplatePath = "C:\\Users\\yl1887\\Desktop\\multiSheetsTemplate.xls";

    @Test
    public void importExcel(){

        File file = new File(importFilePath);
        List<ExcelInput> importList = ExcelUtil.importData(mockMultipartFile(file), 1,
                ExcelInput.class, new ExcelUtil.RemoveEmptyRow());

        if (ValidateUtil.isEmpty(importList)){
            return;
        }

        importList.forEach(input -> {
            System.out.println(input.getName() + " : " + input.getAge());
        });
    }

    @Test
    public void exportExcel() throws FileNotFoundException {
        File outFile = new File(exportFilePath);
        FileOutputStream os = new FileOutputStream(outFile);
        File templte = new File(templatePath);
        FileInputStream in = new FileInputStream(templte);

        writeToExcel(in, mockPersonVOList(10), os, null, 500);
    }

    @Test
    public void exportMultiSheetExcel() throws FileNotFoundException {
        File outFile = new File(exportFilePath);
        FileOutputStream os = new FileOutputStream(outFile);
        File templte = new File(multiSheetsTemplatePath);
        FileInputStream in = new FileInputStream(templte);

        writeToExcel(in, mockPersonVOList(100), os, "详情", 20);
    }

    /**
     * 将数据以excel的形式写到输出流中
     *
     * @param in 模板输入流
     * @param output
     * @param os
     * @param sheetName 模板中sheet的名称，导出多sheet时使用
     * @param sheetCapacity 单个sheet容量
     */
    private static void writeToExcel(InputStream in, List<PersonVO> output, OutputStream os, String sheetName, int sheetCapacity) {

        //sheet为null时，直接导出
        if (ValidateUtil.isEmpty(sheetName)){
            ExcelUtil.exportDataWithJxls2(in, os, asMap("records", output));
            return;
        }

        List<List> outputList = new ArrayList<>();
        List<String> sheetNames = new ArrayList<>();
        String templateSheetName = null;
        if (output.size() < sheetCapacity){
            outputList.add(output);
            sheetNames.add(sheetName);
        }else {
            templateSheetName = sheetName;
            int nameIndex = 1;
            for (int i = 0; i < output.size(); ){
                if (i + sheetCapacity < output.size()){
                    outputList.add(output.subList(i, i = i + sheetCapacity));
                    sheetNames.add(sheetName + StringPool.UNDERSCORE + nameIndex);
                    nameIndex++;
                }else {
                    outputList.add(output.subList(i, output.size()));
                    sheetNames.add(sheetName + StringPool.UNDERSCORE + nameIndex);
                    break;
                }
            }
        }

        ExcelUtil.innerExportMultiSheetWithJxls2(in, os, asMap("datas", outputList, "sheetNames", sheetNames), templateSheetName);
    }

    private static List<PersonVO> mockPersonVOList(int number){

        List<PersonVO> list = new ArrayList<>();

        for (int i = 0; i < number; i++){
            list.add(new PersonVO().setName("name" + i).setAge(i + 18));
        }

        return list;
    }

    private static MultipartFile mockMultipartFile(File file) {

        try {
            FileInputStream inputStream = new FileInputStream(file);
            return new MockMultipartFile(file.getName(), file.getName(), null, FileCopyUtils.copyToByteArray(inputStream));
        } catch (IOException e) {

        }

        return null;
    }

}
