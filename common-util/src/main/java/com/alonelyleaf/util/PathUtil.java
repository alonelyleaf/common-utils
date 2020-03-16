package com.alonelyleaf.util;

public class PathUtil {

    /**
     * 获得class目录
     *
     * @return
     */
    public static String getClassPath(Class cls) {
        //非jar包：/E:/code/server-be/microservice-conference/microservice-conference-app-yms/target/classes/
        //jar包：file:/E:/code/server-be/microservice-conference/microservice-conference-app-yms/target/microconference.jar!/BOOT-INF/classes!/
        String path = cls.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (path.contains(".jar")) {
            path = path.substring(0, path.lastIndexOf(".jar") + 4);
            path = path.substring(0, path.lastIndexOf("/") + 1);
            path = path.replaceFirst("file:", "");
        }
        return path;
    }
}
