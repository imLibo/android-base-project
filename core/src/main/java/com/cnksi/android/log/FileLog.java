package com.cnksi.android.log;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 *用于将日志输入到文件
 * @author lyongfly
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/6/26
 * @version 1.0
 * @since 1.0
 */
class FileLog extends BaseLog {

    /**
     * 将日志输入到文件
     *
     * @param tag             Tag
     * @param targetDirectory 目标文件夹
     * @param fileName        日志文件名称
     * @param headString      日志头
     * @param msg             日志内容
     */
    static void printFile(String tag, File targetDirectory, String fileName, String headString, String msg) {
        fileName = (fileName == null) ? getFileName() : fileName;
        if (save(targetDirectory, fileName, msg)) {
            Log.d(tag, headString + " save log success ! location is >>>" + targetDirectory.getAbsolutePath() + "/" + fileName);
        } else {
            Log.e(tag, headString + "save log fails !");
        }
    }

    private static boolean save(File dic, String fileName, String msg) {
        File file = new File(dic, fileName);
        try {
            OutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            outputStreamWriter.write(msg);
            outputStreamWriter.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static String getFileName() {
        Random random = new Random();
        return "KLog_" + Long.toString(System.currentTimeMillis() + random.nextInt(10000)).substring(4) + ".txt";
    }
}
