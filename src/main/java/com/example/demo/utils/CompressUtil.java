package com.example.demo.utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * @author zhouyw
 * @date 2021-11-12
 * @describe com.example.demo.utils
 */
public class CompressUtil {

    private static final Logger LOG = LoggerFactory.getLogger(CompressUtil.class);

    /**
     * GZip 解压缩
     *
     * @param byteArray GZip格式的压缩字节数组
     * @return 解压缩后的字符串结果
     */
    public static String unCompress(byte[] byteArray) {
        if (byteArray == null || byteArray.length == 0) {
            return null;
        }
        String unCompressString = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        try {
            GZIPInputStream gzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            unCompressString = out.toString();
            gzip.close();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return unCompressString;
    }

    /**
     * 使用给定密码压缩指定文件或文件夹到指定位置.
     * <p>
     * dest可传最终压缩文件存放的绝对路径,也可以传存放目录,也可以传null或者""
     * 如果传null或者""则将压缩文件存放在当前目录,即跟源文件同目录,压缩文件名取源文件名,以.zip为后缀;
     * 如果以路径分隔符(File.separator)结尾,则视为目录,压缩文件名取源文件名,以.zip为后缀,否则视为文件名.
     *
     * @param src         要压缩的文件或文件夹路径
     * @param dest        压缩文件存放路径
     * @param isCreateDir 是否在压缩文件里创建目录,仅在压缩文件为目录时有效.
     *                    如果为false,将直接压缩目录下文件到压缩文件.
     * @param passwd      压缩使用的密码
     * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
     */
    public static String zip(String src, String dest, boolean isCreateDir, String passwd) {
        File srcFile = new File(src);
        dest = buildDestinationZipFilePath(srcFile, dest);
        ZipParameters parameters = new ZipParameters();
        // 压缩方式
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        // 压缩级别
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        if (!StringUtils.isEmpty(passwd)) {
            parameters.setEncryptFiles(true);
            // 加密方式
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            parameters.setPassword(passwd.toCharArray());
        }
        try {
            ZipFile zipFile = new ZipFile(dest);
            if (srcFile.isDirectory()) {
                // 如果不创建目录的话,将直接把给定目录下的文件压缩到压缩文件,即没有目录结构
                if (!isCreateDir) {
                    File[] subFiles = srcFile.listFiles();
                    ArrayList<File> temp = new ArrayList<File>();
                    Collections.addAll(temp, subFiles);
                    zipFile.addFiles(temp, parameters);
                    return dest;
                }
                zipFile.addFolder(srcFile, parameters);
            } else {
                zipFile.addFile(srcFile, parameters);
            }
            return dest;
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param source
     *            原始文件路径
     * @param dest
     *            解压路径
     * @param password
     *            解压文件密码(可以为空)
     */
    public static void unZip(String source, String dest, String password) throws ZipException {
        try {
            File zipFile = new File(source);
            // 首先创建ZipFile指向磁盘上的.zip文件
            ZipFile zFile = new ZipFile(zipFile);
            zFile.setFileNameCharset("GBK");
            File destDir = new File(dest);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            if (zFile.isEncrypted()) {
                // 设置密码
                zFile.setPassword(password.toCharArray());
            }
            // 将文件抽出到解压目录(解压)
            zFile.extractAll(dest);
            List<FileHeader> headerList = zFile.getFileHeaders();
            List<File> extractedFileList = new ArrayList<File>();
            for (FileHeader fileHeader : headerList) {
                if (!fileHeader.isDirectory()) {
                    extractedFileList.add(new File(destDir, fileHeader.getFileName()));
                }
            }
            File[] extractedFiles = new File[extractedFileList.size()];
            extractedFileList.toArray(extractedFiles);
            for (File f : extractedFileList) {
                System.out.println(f.getAbsolutePath() + "文件解压成功!");
            }
        } catch (ZipException e) {
            throw e;
        }
    }

    /**
     * 构建压缩文件存放路径,如果不存在将会创建
     * 传入的可能是文件名或者目录,也可能不传,此方法用以转换最终压缩文件的存放路径
     *
     * @param srcFile   源文件
     * @param destParam 压缩目标路径
     * @return 正确的压缩文件存放路径
     */
    private static String buildDestinationZipFilePath(File srcFile, String destParam) {
        if (StringUtils.isEmpty(destParam)) {
            if (srcFile.isDirectory()) {
                destParam = srcFile.getParent() + File.separator + srcFile.getName() + ".zip";
            } else {
                String fileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));
                destParam = srcFile.getParent() + File.separator + fileName + ".zip";
            }
        } else {
            // 在指定路径不存在的情况下将其创建出来
            createDestDirectoryIfNecessary(destParam);
            if (destParam.endsWith(File.separator)) {
                String fileName = "";
                if (srcFile.isDirectory()) {
                    fileName = srcFile.getName();
                } else {
                    fileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));
                }
                destParam += fileName + ".zip";
            }
        }
        return destParam;
    }

    /**
     * 在必要的情况下创建压缩文件存放目录,比如指定的存放路径并没有被创建
     *
     * @param destParam 指定的存放路径,有可能该路径并没有被创建
     */
    private static void createDestDirectoryIfNecessary(String destParam) {
        File destDir = null;
        if (destParam.endsWith(File.separator)) {
            destDir = new File(destParam);
        } else {
            destDir = new File(destParam.substring(0, destParam.lastIndexOf(File.separator)));
        }
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    public static void main(String[] args) {
        try {
            generate(10);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        try {
            RandomAccessFile file = new RandomAccessFile("C:\\Users\\Administrator\\Desktop\\新建文件夹\\新建文件夹\\pwd.txt", "r");
            String pwd;
            while ((pwd = file.readLine()) != null) {
                try {
                    System.out.println("尝试密码： " + pwd);
                    unZip("F:\\其它\\FQ\\芬奇.zip", "C:\\Users\\Administrator\\Desktop\\新建文件夹\\新建文件夹", pwd);
                } catch (ZipException e) {
                    System.out.println("解压失败： " + e.getMessage());
                }
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        String pwd = "123456";
//        try {
//            System.out.println("尝试密码： " + pwd);
////            unZip("C:\\Users\\Administrator\\Desktop\\新建文件夹\\新建文件夹.zip", "C:\\Users\\Administrator\\Desktop\\新建文件夹\\新建文件夹", pwd);
//            unZip("F:\\其它\\FQ\\芬奇.zip", "C:\\Users\\Administrator\\Desktop\\新建文件夹\\新建文件夹", pwd);
//        } catch (Exception e) {
//            System.out.println("解压失败： " + e.getMessage());
//        }

    }

    //密码可能会包含的字符集合
    private static char[] fullCharSource = { '1','2','3','4','5','6','7','8','9','0',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',  'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',  'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '{', '}', '|', ':', '"', '<', '>', '?', ';', '\'', ',', '.', '/', '-', '=', '`'};
    // 将可能的密码集合长度
    private static int fullCharLength = fullCharSource.length;
    // maxLength：生成的字符串的最大长度
    public static void generate(int maxLength) throws FileNotFoundException, UnsupportedEncodingException {
        //计数器，多线程时可以对其加锁，当然得先转换成Integer类型。
        int counter = 0;
        StringBuilder buider = new StringBuilder();

        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\Administrator\\Desktop\\新建文件夹\\新建文件夹\\pwd.txt"), "utf-8"));

        while (buider.toString().length() <= maxLength) {
            buider = new StringBuilder(maxLength*2);
            int _counter = counter;
            //10进制转换成26进制
            while (_counter >= fullCharLength) {
                //获得低位
                buider.insert(0, fullCharSource[_counter % fullCharLength]);
                _counter = _counter / fullCharLength;
                //处理进制体系中只有10没有01的问题，在穷举里面是可以存在01的
                _counter--;
            }
            //最高位
            buider.insert(0,fullCharSource[_counter]);
            counter++;

            pw.write(buider.toString()+"\n");
            System.out.println(buider.toString());

//            String pwd = buider.toString();
//            try {
//                System.out.println("尝试密码： " + pwd);
//                unZip("F:\\其它\\FQ\\芬奇.zip", "C:\\Users\\Administrator\\Desktop\\新建文件夹\\新建文件夹", pwd);
//            } catch (Exception e) {
//                System.out.println("解压失败： " + e.getMessage());
//                continue;
//            }
//            System.out.println("解压成功。");
//            break;
        }
    }

}

