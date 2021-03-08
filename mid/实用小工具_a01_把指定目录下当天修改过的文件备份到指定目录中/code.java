package com.xxx.addmybatisplus.config;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

public class FileUpdateCopyUtil {


    public static void main(String[] args) {
        try {
            copyTodayUpdateFiles("E:\\00问题解决库、代码库、QA库\\a02_代码库和动手",
                    "E:\\00问题解决库、代码库、QA库\\a02_代码库和动手\\a01_初级\\a01_待完成\\JSE_a01超甲_FileUtils【完成度0%】\\a01_核心代码");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把今日修改的文件统一拷贝并存放到一起
     * @param tagertPath 拷贝此目录下今日修改的文件
     * @param savePath 拷贝后文件存放于此
     */
    public static void copyTodayUpdateFiles(String tagertPath, String savePath) throws IOException {
        // 参数校验
        if(null==tagertPath || null==savePath){
            throw new FileNotFoundException("copyPath or targetPath is null");
        }
        File tagetDirect = new File(tagertPath);
        File saveDirect = new File(savePath);
        if(!tagetDirect.isDirectory() || !saveDirect.isDirectory()){
            throw new FileNotFoundException("copyPath or targetPath is not Directory");
        }
        copyFiles(tagetDirect, savePath);
    }

    private static void copyFiles(File file, String savePath) throws IOException {
        // private方法，参数校验省略
        if(file.isFile()){
            // copy
            if(isFileUpdateToday(file)){
                copyFile(file, savePath);
            }
            return;
        }
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for (File file1 : files) {
                copyFiles(file1, savePath);
            }
        }
    }

    /**
     * 判断文件今日有无修改
     * @param file
     * @return 如果文件今日有修改，则返回true
     */
    private static boolean isFileUpdateToday(File file){
        // 今日日期
        LocalDate todayDate = LocalDate.now();
        return isFileUpdateTheday(file, todayDate);
    }

    /**
     * 判断文件最近修改日期是否是传入日期
     * @param file 待比较文件
     * @param compareDate 比较日期
     * @return
     */
    private static boolean isFileUpdateTheday(File file, LocalDate compareDate){
        // 入参校验
        if(null==file || !file.isFile() || null==compareDate){
            return false;
        }

        // 获取文件日期
        long timeStamp = file.lastModified();
        LocalDate updateDate = new Date(timeStamp).toInstant().atZone(ZoneOffset.of("+8")).toLocalDate();

        // 对比
        return compareDate.equals(updateDate);
    }

    /**
     * 复制文件 把源文件复制到指定目录下，要求目录已存在
     * @param sourceFile
     * @param targePath
     * @throws IOException
     */
    public static void copyFile(File sourceFile, String targePath) throws IOException {
        File targetFile = new File(targePath);
        if(!targetFile.isDirectory()){
            throw new FileNotFoundException("targetPath not exits");
        }
        if(null ==sourceFile || !sourceFile.isFile()){
            throw new IOException("sourceFile not exits or not file");
        }

        // step1 获取拷贝文件的文件名
        String fileName = sourceFile.getName();
        String copyFileFullPath = targePath+File.separator+fileName;
        System.out.println(sourceFile.getAbsolutePath());

        // 如果拷贝和目标全路径一样，则return
        if(copyFileFullPath.equals(sourceFile.getAbsolutePath())){
            return;
        }


        // 获取源文件和目标文件的输入输出流
        try(FileInputStream fis = new FileInputStream(sourceFile);
            FileOutputStream fos = new FileOutputStream(new File(copyFileFullPath));
            // 获取输入输出通道
            FileChannel inChannel = fis.getChannel();
            FileChannel outChannel = fos.getChannel()
        ){
            // 创建缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                // clear方法重设缓冲区，使它可以接受读入的数据
                buffer.clear();
                // 从输入通道中将数据读到缓冲区
                int r = inChannel.read(buffer);
                // read方法返回读取的字节数，可能为零，如果该通道已到达流的末尾，则返回-1
                if (r == -1) {
                    break;
                }
                // flip方法让缓冲区可以将新读入的数据写入另一个通道
                buffer.flip();
                // 从输出通道中将数据写入缓冲区
                outChannel.write(buffer);
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("文件不存在"+e);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("io异常"+e);
        }
    }
}