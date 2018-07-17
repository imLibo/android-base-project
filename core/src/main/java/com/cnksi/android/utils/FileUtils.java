package com.cnksi.android.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cnksi.android.log.KLog;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.List;

/**
 * 文件操作工具类
 * makeDirectory(String... pathArray)  创建多个文件夹
 * <p>
 * makeDirectory(String directory) 创建文件夹
 * <p>
 * isFileExists(String filePath) 判断文件是否存在
 * <p>
 * isFolderExists(String filePath)  判断文件夹是否存在
 * <p>
 * deleteAllFiles(File file) 递归删除文件夹下的所有文件
 * <p>
 * deleteAllFiles(String filePath) 递归删除文件夹下的所有文件
 * <p>
 * deleteFile(String filePath) 删除单个文件
 * <p>
 * deleteAllCache(Context context) 清除所有缓存
 * <p>
 * deleteFiles(String filePath, long timeAgo) 删除多久之前的备份文件
 * <p>
 * renameFile(String from, String to)   重命名文件
 * <p>
 * getAllFiles(String filePath, List<String> fileList)  递归找出某个文件夹下的所有文件 默认递归
 * <p>
 * deleteFileBySuffix(String folder, String suffix) 删除后缀名相同的文件
 * <p>
 * copyAssetsToSDCard(Context context, String targetFolder, String... assetsNames)  递归拷贝assets文件到SD卡
 * <p>
 * copyDirectory(String srcDirPath, String destDirPath)  复制整个目录的内容 覆盖
 * <p>
 * copyFile(String srcFileName, String destFileName)  复制单个文件 覆盖
 * <p>
 * isModifyLessThanTime(File file, long time) 文件的最后修改时间是否小于给定的时间
 * <p>
 * deleteFileByModifyTime(String filePath, long time)  递归删除 某个文件夹下 最后修改时间小于time的文件
 */
public class FileUtils {

    private FileUtils() {
    }

    /**
     * 创建多个文件夹
     *
     * @param pathArray 文件夹绝对路径集合 /sdcard/Android/temp/
     * @return true 创建成功  false 创建失败
     */
    public static boolean makeDirectory(String... pathArray) {
        boolean isSuccess = true;
        for (int i = 0, count = pathArray.length; i < count; i++) {
            if (!isFolderExists(pathArray[i])) {
                if (!(isSuccess = makeDirectory(pathArray[i]))) {
                    break;
                }
            }
        }
        return isSuccess;
    }

    /**
     * 创建文件夹
     *
     * @param directory 文件夹
     * @return true 创建成功  false 创建失败
     */
    public static boolean makeDirectory(@NonNull String directory) {
        boolean created;
        try {
            File dir = new File(directory);
            if (dir.isFile()) {
                created = dir.createNewFile();
            } else {
                created = dir.mkdirs();
            }
        } catch (Exception e) {
            created = false;
        }
        return created;
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件绝对路径
     * @return true 存在  false 不存在
     */
    public static boolean isFileExists(@NonNull String filePath) {
        File file = new File(filePath);
        return file.isFile() && file.exists();
    }

    /**
     * 判断文件夹是否存在
     *
     * @param filePath 文件夹绝对路径
     * @return true 存在  false 不存在
     */
    public static boolean isFolderExists(@NonNull String filePath) {
        File file = new File(filePath);
        return file.isDirectory() && file.exists();
    }

    /**
     * 递归删除文件夹下的所有文件
     *
     * @param file 文件夹
     * @return true 删除成功 false删除失败
     */
    public static boolean deleteAllFiles(@NonNull File file) {
        if (!file.exists()) {
            return false;
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteAllFiles(f);
                } else {
                    f.delete();
                }
            }
        }
        return file.delete();
    }

    /**
     * 递归删除文件夹下的所有文件
     *
     * @param filePath 文件绝对路径
     */
    public static void deleteAllFiles(@NonNull String filePath) {
        deleteAllFiles(new File(filePath));
    }

    /**
     * 删除单个文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(@NonNull String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        } else {
            return true;
        }
    }

    /**
     * 清除所有缓存
     *
     * @param context 上下文
     */
    public static void deleteAllCache(@NonNull Context context) {
        if (context.getCacheDir() != null && context.getCacheDir().exists()) {
            deleteAllFiles(context.getCacheDir());
        }
        if (context.getExternalCacheDir() != null && context.getExternalCacheDir().exists()) {
            deleteAllFiles(context.getExternalCacheDir());
        }
    }

    /**
     * 删除多久之前的备份文件
     *
     * @param filePath 文件绝对路径
     * @param timeAgo  多久之前的时间 long oneMonthAgoTime = Long.valueOf(((long) 7 * (long) 24 * (long) 60 * (long) 60 * (long) 1000));
     */
    public static void deleteFiles(@NonNull String filePath, long timeAgo) {
        File bakFile = new File(filePath);
        long time = Calendar.getInstance().getTimeInMillis() - timeAgo;
        if (bakFile.exists()) {
            if (bakFile.isDirectory()) {
                for (File file : bakFile.listFiles()) {
                    if (file.lastModified() < time) {
                        FileUtils.deleteFile(file.getAbsolutePath());
                    }
                }
            } else {
                if (bakFile.lastModified() < time) {
                    FileUtils.deleteFile(bakFile.getAbsolutePath());
                }
            }
        }
    }

    /**
     * 重命名文件
     *
     * @param from 原文件名
     * @param to   新文件名
     * @return true 成功 false 失败
     */
    public static boolean renameFile(@NonNull String from, @NonNull String to) {
        boolean isSuccess = true;
        File oldFile = new File(from);
        if (oldFile.exists()) {
            File newFile = new File(to);
            if (newFile.exists()) {
                isSuccess = newFile.delete();
            }
            if (isSuccess) {
                // 执行重命名
                isSuccess = oldFile.renameTo(newFile);
            }
        }
        return isSuccess;
    }

    /**
     * 递归找出某个文件夹下的所有文件 默认递归
     *
     * @param filePath 文件绝对路径
     * @param fileList 找到的文件集合
     */
    public static void getAllFiles(@NonNull String filePath, @NonNull List<String> fileList) {
        getAllFiles(filePath, fileList, true);
    }

    /**
     * 获取文件夹下面的所有文件
     *
     * @param filePath    文件路径
     * @param fileList    找到的文件集合
     * @param isRecursive 是否递归 是 获取所有子文件夹下的文件
     */
    public static void getAllFiles(@NonNull String filePath, @NonNull List<String> fileList, boolean isRecursive) {
        File root = new File(filePath);
        if (root.isDirectory()) {
            File[] files = root.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        if (isRecursive) {
                            getAllFiles(file.getAbsolutePath(), fileList, true);
                        }
                    } else {
                        fileList.add(file.getAbsolutePath());
                    }
                }
            } else {
                fileList.add(root.getAbsolutePath());
            }
        } else {
            fileList.add(root.getAbsolutePath());
        }
    }

    /**
     * 删除后缀名相同的文件
     *
     * @param folder 文件夹绝对路径
     * @param suffix 文件后缀名
     */
    public static void deleteFileBySuffix(@NonNull String folder, @NonNull String suffix) {
        File rootFile = new File(folder);
        if (rootFile.exists()) {
            File[] files = rootFile.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    String fileAbsolutePath = file.getAbsolutePath();
                    if (fileAbsolutePath.endsWith(suffix)) {
                        deleteFile(fileAbsolutePath);
                    }
                }
            }
        }
    }

    /**
     * 递归拷贝assets文件到SD卡
     *
     * @param context
     * @param targetFolder
     * @param assetsNames
     */
    public static boolean copyAssetsToSDCard(@NonNull Context context, @NonNull String targetFolder, String... assetsNames) {
        return copyAssetsToSDCard(context, targetFolder, false, assetsNames);
    }

    /**
     * 递归拷贝assets文件到SD卡
     *
     * @param context      上下文
     * @param targetFolder 要复制到的文件夹绝对路径  /sdcard/xxx/
     * @param isOverlay    是否覆盖
     * @param assetsNames  需要复制的文件
     * @return true 复制成功  false 复制失败
     */
    public static boolean copyAssetsToSDCard(@NonNull Context context, @NonNull String targetFolder, boolean isOverlay, String... assetsNames) {
        boolean isSuccess = true;
        if (assetsNames != null && assetsNames.length > 0) {
            for (String name : assetsNames) {
                File file = new File(targetFolder, name);
                File folder = new File(targetFolder);
                if (!folder.exists()) {
                    isSuccess = folder.mkdirs();
                }
                if (isOverlay) {
                    if (file.exists()) {
                        isSuccess = file.delete();
                    }
                } else {
                    if (file.exists()) {
                        continue;
                    }
                }
                try {
                    InputStream is = context.getApplicationContext().getClass().getClassLoader().getResourceAsStream("assets/" + name);
                    FileOutputStream fos = new FileOutputStream(file);
                    int len;
                    byte[] buffer = new byte[4096];
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    IOUtils.closeQuietly(is);
                    IOUtils.closeQuietly(fos);
                } catch (IOException e) {
                    isSuccess = false;
                    break;
                }
            }
        }
        return isSuccess;
    }

    /**
     * 复制整个目录的内容 覆盖
     *
     * @param srcDirPath  待复制目录的目录名
     * @param destDirPath 目标目录名
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectory(@NonNull String srcDirPath, @NonNull String destDirPath) {
        return copyDirectory(srcDirPath, destDirPath, true);
    }

    /**
     * 复制整个目录的内容
     *
     * @param srcDirPath  待复制目录的绝对路径
     * @param destDirPath 目标目录的绝对路径
     * @param isOverlay   如果目标目录存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectory(@NonNull String srcDirPath, @NonNull String destDirPath, boolean isOverlay) {
        File srcDir = new File(srcDirPath);
        if (!srcDir.exists()) {
            return false;
        } else if (!srcDir.isDirectory()) {
            return false;
        }
        // 如果目标目录名不是以文件分隔符结尾，则加上文件分隔符
        if (!destDirPath.endsWith(File.separator)) {
            destDirPath = destDirPath + File.separator;
        }
        File destDir = new File(destDirPath);
        // 如果目标文件夹存在
        if (destDir.exists()) {
            // 如果允许覆盖则删除已存在的目标目录
            if (isOverlay) {
                deleteFile(destDirPath);
            } else {
                return true;
            }
        } else {
            // 创建目的目录
            if (!destDir.mkdirs()) {
                return false;
            }
        }
        boolean flag = true;
        File[] files = srcDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    flag = copyFile(file.getAbsolutePath(), destDirPath + file.getName(), isOverlay);
                    if (!flag) {
                        break;
                    }
                } else if (file.isDirectory()) {
                    flag = copyDirectory(file.getAbsolutePath(), destDirPath + file.getName(), isOverlay);
                    if (!flag) {
                        break;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 复制单个文件 覆盖
     *
     * @param srcFileName  待复制的文件绝对路径
     * @param destFileName 目标文件绝对路径
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyFile(@NonNull String srcFileName, @NonNull String destFileName) {
        return copyFile(srcFileName, destFileName, true);
    }

    /**
     * 复制单个文件
     *
     * @param srcFilePath  待复制的文件绝对路径
     * @param destFilePath 目标文件绝对路径
     * @param isOverlay    如果目标文件存在，是否覆盖
     * @return 复制成功返回true，否则返回false
     */
    public static boolean copyFile(@NonNull String srcFilePath, @NonNull String destFilePath, boolean isOverlay) {
        File srcFile = new File(srcFilePath);
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            return false;
        } else if (!srcFile.isFile()) {
            return false;
        }
        // 判断目标文件是否存在
        File destFile = new File(destFilePath);
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            if (isOverlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                if (!deleteFile(destFilePath)) {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    return false;
                }
            }
        }
        // 文件通道的方式来进行文件复制
        try {
            FileInputStream inStream = new FileInputStream(srcFile);
            FileOutputStream outStream = new FileOutputStream(destFile);
            FileDescriptor fd = outStream.getFD();
            FileChannel in = inStream.getChannel();
            FileChannel out = outStream.getChannel();
            in.transferTo(0, in.size(), out);
            fd.sync();
            IOUtils.closeQuietly(inStream);
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(outStream);
            IOUtils.closeQuietly(out);
        } catch (IOException e) {
            KLog.e(e);
            return false;
        }
        return true;
    }

    /**
     * 文件的最后修改时间是否小于给定的时间
     *
     * @param file 文件
     * @param time 对比时间
     * @return true 是小于  false 不小于
     */
    public static boolean isModifyLessThanTime(@NonNull File file, long time) {
        if (file != null && file.exists()) {
            if (file.lastModified() < time) {
                return true;
            }
        }
        return false;
    }

    /**
     * 递归删除 某个文件夹下 最后修改时间小于time的文件
     *
     * @param filePath 文件夹绝对路径
     * @param time     某个时间点 删除一个星期前的备份文件 long time = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L;
     */
    public static void deleteFileByModifyTime(@NonNull String filePath, long time) {
        if (!TextUtils.isEmpty(filePath)) {
            File root = new File(filePath);
            if (root.isDirectory()) {
                File[] files = root.listFiles();
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteFileByModifyTime(file.getAbsolutePath(), time);
                    }
                }
            } else {
                if (isModifyLessThanTime(root, time)) {
                    root.delete();
                }
            }
        }
    }
}
