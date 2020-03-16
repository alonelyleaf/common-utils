package com.alonelyleaf.spring.upgradescript;

import com.alonelyleaf.spring.upgradescript.entity.UpgradeScript;
import com.alonelyleaf.util.ValidateUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UpgradeScriptInitializeHandler{

    @Autowired
    UpgradeScriptService upgradeScriptService;

    @Value("${system.server.enable:true}")
    private Boolean systemServerEnable;

    public boolean handle() {

        //window系统表示开发环境
        if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            return true;//不再执行
        }

        //TODO 集群情况下只在主节点才执行
        //TODO 增量脚本执行期间需要加锁防止外部请求变更数据

        List<File> files = getUnexecFileList();
        upgradeScriptService.execScriptFileList(files);

        return true;//不再执行
    }

    private List<File> getUnexecFileList() {
        //获得所有版本文件列表
        List<File> fileList = upgradeScriptService.getScriptFile();
        if (ValidateUtil.isEmpty(fileList)) {
            return null;
        }
        //获得已执行版本文件列表
        List<UpgradeScript> upgradeScripts = upgradeScriptService.getAllList();
        List<String> execVersions = Lists.newArrayList();
        if (ValidateUtil.isNotEmpty(upgradeScripts)) {
            execVersions = upgradeScripts.stream().map(UpgradeScript::getVersion).collect(Collectors.toList());
        }

        List<File> unexecFileList = new ArrayList<>();
        for (File file : fileList) {
            String scriptVersion = file.getName();
            if (!file.isDirectory()) {
                continue;
            } else if ("everytime".equals(scriptVersion)) {
                unexecFileList.add(file);
                continue;
            } else if (execVersions.contains(scriptVersion)) {
                continue;
            }
            unexecFileList.add(file);
        }
        return unexecFileList;
    }
}
