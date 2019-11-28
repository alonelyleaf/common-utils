package com.alonelyleaf.spring.javaio;

import java.io.*;

/**
 * 磁盘操作：File
 * 字节操作：InputStream 和 OutputStream
 * 字符操作：Reader 和 Writer
 * 对象操作：Serializable
 * 网络操作：Socket
 * 新的输入/输出：NIO
 *
 * @author bijl
 * @date 2019/11/6
 */
public class FileIOUtils {

    /**
     * 递归地列出一个目录下所有文件
     *
     * @param dir
     */
    public static void listAllFiles(File dir) {
        if (dir == null || !dir.exists()) {
            return;
        }
        if (dir.isFile()) {
            System.out.println(dir.getName());
            return;
        }
        for (File file : dir.listFiles()) {
            listAllFiles(file);
        }
    }

    /**
     * 实现文件复制，字节流
     *
     * @param src 源文件路径
     * @param dist 目标文件路径
     * @throws IOException
     */
    public static void copyFile(String src, String dist) throws IOException {

        FileInputStream in = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dist);

        byte[] buffer = new byte[20 * 1024];
        int cnt;

        // read() 最多读取 buffer.length 个字节
        // 返回的是实际读取的个数
        // 返回 -1 的时候表示读到 eof，即文件尾
        while ((cnt = in.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, cnt);
        }

        in.close();
        out.close();
    }


    /**
     * 实现文件复制，字符流
     *
     * @param src 源文件路径
     * @param dist 目标文件路径
     * @throws IOException
     */
    public void copyFile2(String src, String dist) throws IOException {

        FileReader fileReader = new FileReader(src);

        BufferedReader reader = null;
        FileOutputStream fs = null;
        BufferedWriter bw = null;

        try {
            reader = new BufferedReader(fileReader);

            fs = new FileOutputStream(dist);
            bw = new BufferedWriter(new OutputStreamWriter(fs));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line + "\r\n";
                bw.write(line);
            }
        }finally {
            if (reader != null){
                reader.close();
            }
            if (bw != null){
                bw.close();
            }
            if (fs != null){
                fs.close();
            }
        }
    }

    /**
     * 逐行读取文件内容
     *
     * @param filePath
     * @throws IOException
     */
    public static void readFileContent(String filePath) throws IOException {

        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }

        // 装饰者模式使得 BufferedReader 组合了一个 Reader 对象
        // 在调用 BufferedReader 的 close() 方法时会去调用 Reader 的 close() 方法
        // 因此只要一个 close() 调用即可
        bufferedReader.close();
    }
}
