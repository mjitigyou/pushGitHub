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
            copyTodayUpdateFiles("E:\\00�������⡢����⡢QA��\\a02_�����Ͷ���",
                    "E:\\00�������⡢����⡢QA��\\a02_�����Ͷ���\\a01_����\\a01_�����\\JSE_a01����_FileUtils����ɶ�0%��\\a01_���Ĵ���");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * �ѽ����޸ĵ��ļ�ͳһ��������ŵ�һ��
     * @param tagertPath ������Ŀ¼�½����޸ĵ��ļ�
     * @param savePath �������ļ�����ڴ�
     */
    public static void copyTodayUpdateFiles(String tagertPath, String savePath) throws IOException {
        // ����У��
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
        // private����������У��ʡ��
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
     * �ж��ļ����������޸�
     * @param file
     * @return ����ļ��������޸ģ��򷵻�true
     */
    private static boolean isFileUpdateToday(File file){
        // ��������
        LocalDate todayDate = LocalDate.now();
        return isFileUpdateTheday(file, todayDate);
    }

    /**
     * �ж��ļ�����޸������Ƿ��Ǵ�������
     * @param file ���Ƚ��ļ�
     * @param compareDate �Ƚ�����
     * @return
     */
    private static boolean isFileUpdateTheday(File file, LocalDate compareDate){
        // ���У��
        if(null==file || !file.isFile() || null==compareDate){
            return false;
        }

        // ��ȡ�ļ�����
        long timeStamp = file.lastModified();
        LocalDate updateDate = new Date(timeStamp).toInstant().atZone(ZoneOffset.of("+8")).toLocalDate();

        // �Ա�
        return compareDate.equals(updateDate);
    }

    /**
     * �����ļ� ��Դ�ļ����Ƶ�ָ��Ŀ¼�£�Ҫ��Ŀ¼�Ѵ���
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

        // step1 ��ȡ�����ļ����ļ���
        String fileName = sourceFile.getName();
        String copyFileFullPath = targePath+File.separator+fileName;
        System.out.println(sourceFile.getAbsolutePath());

        // ���������Ŀ��ȫ·��һ������return
        if(copyFileFullPath.equals(sourceFile.getAbsolutePath())){
            return;
        }


        // ��ȡԴ�ļ���Ŀ���ļ������������
        try(FileInputStream fis = new FileInputStream(sourceFile);
            FileOutputStream fos = new FileOutputStream(new File(copyFileFullPath));
            // ��ȡ�������ͨ��
            FileChannel inChannel = fis.getChannel();
            FileChannel outChannel = fos.getChannel()
        ){
            // ����������
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                // clear�������軺������ʹ�����Խ��ܶ��������
                buffer.clear();
                // ������ͨ���н����ݶ���������
                int r = inChannel.read(buffer);
                // read�������ض�ȡ���ֽ���������Ϊ�㣬�����ͨ���ѵ�������ĩβ���򷵻�-1
                if (r == -1) {
                    break;
                }
                // flip�����û��������Խ��¶��������д����һ��ͨ��
                buffer.flip();
                // �����ͨ���н�����д�뻺����
                outChannel.write(buffer);
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("�ļ�������"+e);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("io�쳣"+e);
        }
    }
}