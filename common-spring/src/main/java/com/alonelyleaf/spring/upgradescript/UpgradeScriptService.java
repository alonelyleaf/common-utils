package com.alonelyleaf.spring.upgradescript;

import com.alonelyleaf.spring.upgradescript.entity.UpgradeScript;
import com.alonelyleaf.util.PathUtil;
import com.alonelyleaf.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Service
public class UpgradeScriptService {

    private static String scriptFolder = "script/upgrade";

    private static final Logger logger = LoggerFactory.getLogger(UpgradeScriptService.class);

    /**
     * 查询列表
     *
     * @return
     */
    public List<UpgradeScript> getAllList() {

        //TODO 从数据库查询已执行脚本列表

        return new ArrayList<>();
    }

    /**
     * 获得全部脚本目录
     * 并按照版本号排序
     *
     * @return
     */
    public List<File> getScriptFile() {
        // 脚本目录
        File scriptDir = new File(PathUtil.getClassPath(this.getClass()) + scriptFolder);
        File[] subDirs = scriptDir.listFiles();
        if (subDirs == null || subDirs.length == 0) {
            return null;
        }
        List<File> fileList = Arrays.asList(subDirs);
        Collections.sort(fileList, (o1, o2) -> {

            String[] o1List = o1.getName().split("\\.");
            String[] o2List = o2.getName().split("\\.");

            int length = Math.min(o1List.length, o2List.length);

            for (int i = 0; i < length; i++){

                if (Integer.valueOf(o1List[i]) < Integer.valueOf(o2List[i])){
                    return -1;
                }
                if (Integer.valueOf(o1List[i]) > Integer.valueOf(o2List[i])){
                    return 1;
                }
            }
            if (o1List.length < o2List.length){
                return -1;
            }
            if (o1List.length > o2List.length){
                return 1;
            }
            return 0;
        });
        return fileList;
    }

    /**
     * 执行所有目录下的脚本
     *
     * @param scriptFileList
     */
    public void execScriptFileList(List<File> scriptFileList) {
        if (ValidateUtil.isNotEmpty(scriptFileList)) {
            for (File file : scriptFileList) {
                try {
                    execScriptForDir(file);

                    // 保存数据库记录
                } catch (Exception e) {
                    logger.error("execScript {} error,{}", file.getName(), e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 执行该目录下的脚本
     *
     * @param dir
     */
    private void execScriptForDir(File dir) throws Exception {
        // 执行该目录下的脚本
        File[] subFiles = dir.listFiles();

        if (ValidateUtil.isNotEmpty(subFiles)) {
            for (File subFile : subFiles) {
                String extensionName = getFileExtension(subFile.getName());
                if ("js".equalsIgnoreCase(extensionName)) {
                    //执行增量脚本
                } else if ("sh".equalsIgnoreCase(extensionName)) {
                    //执行增量脚本
                }
            }
        }
    }

    private String getFileExtension(String fullName) {

        if (fullName == null){
            return "";
        }

        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}
