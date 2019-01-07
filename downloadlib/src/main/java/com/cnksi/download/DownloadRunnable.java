package com.cnksi.download;

import com.cnksi.okhttp.OkHttpManager;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 下载线程
 */
class DownloadRunnable implements Runnable {

    /**
     * 线程的状态
     */
    private int mStatus = DownloadTask.DownloadStatus.STATUS_DOWNLOADING;
    /**
     * 文件下载的url
     */
    private String url;
    /**
     * 文件的名称
     */
    private String name;
    /**
     * 文件存储路径
     */
    private String folder;
    /**
     * 每个线程下载开始的位置
     */
    private long start;
    /**
     * 每个线程下载结束的位置
     */
    private long end;
    /**
     * 文件的总大小 content-length
     */
    private long mCurrentLength;
    /**
     * 下载回调
     */
    private DownloadCallback downloadCallback;
    /**
     * 断点存储文件,文件名称为 .name.apk.1 信息格式为 start + "-" + end
     */
    private File breakPointFile;

    DownloadRunnable(String folder, String name, String url, long currentLength, int threadId, long start, long end, DownloadCallback downloadCallback) {
        this.folder = folder;
        this.name = name;
        this.url = url;
        this.mCurrentLength = currentLength;
        this.start = start;
        this.end = end;
        this.downloadCallback = downloadCallback;
        this.breakPointFile = new File(folder, "." + name + "." + threadId);
    }

    @Override
    public void run() {
        mStatus = DownloadTask.DownloadStatus.STATUS_DOWNLOADING;
        InputStream inputStream = null;
        RandomAccessFile randomAccessFile = null;
        FileChannel channelOut = null;
        try {
            Response response = OkHttpManager.getInstance().syncResponse(url, start, end);
            ResponseBody responseBody = response.body();
            inputStream = responseBody.byteStream();
            long contentLength = responseBody.contentLength();
            //保存文件的路径
            File file = new File(folder, name + "-tmp");
            randomAccessFile = new RandomAccessFile(file, "rwd");
//            randomAccessFile.seek(start);
            //Chanel NIO中的用法，由于RandomAccessFile没有使用缓存策略，直接使用会使得下载速度变慢，亲测缓存下载3.3秒的文件，用普通的RandomAccessFile需要20多秒。
            channelOut = randomAccessFile.getChannel();
            // 内存映射，直接使用RandomAccessFile，是用其seek方法指定下载的起始位置，使用缓存下载，在这里指定下载位置。
            MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, start, contentLength);
            int length;
            byte[] bytes = new byte[100 * 1024];
            boolean isSuccess = true;
            while ((length = inputStream.read(bytes)) != -1) {
                if (mStatus == DownloadTask.DownloadStatus.STATUS_STOP) {
                    isSuccess = false;
                    downloadCallback.onPause(file);
                    break;
                }
//                randomAccessFile.write(bytes, 0, length);
                mappedBuffer.put(bytes, 0, length);
                //保存下进度，做断点
                start += length;
                //实时去更新下进度条，将每次写入的length传出去
                downloadCallback.onProgress(length, mCurrentLength);
            }
            File resultFile = new File(folder, name);
            if (isSuccess && file.renameTo(resultFile)) {
                deleteFile(breakPointFile);
                deleteFile(file);
                downloadCallback.onSuccess(resultFile);
            }
        } catch (IOException e) {
            downloadCallback.onFailure(e);
        } finally {
            //保存到文件记录断点
            recordProgress(start, end);
            close(inputStream);
            close(channelOut);
            close(randomAccessFile);
        }
    }

    /**
     * 停止下载
     */
    public void stop() {
        mStatus = DownloadTask.DownloadStatus.STATUS_STOP;
    }

    /**
     * 记录当前下载的断点
     */
    private void recordProgress(long start, long end) {
        if (start < end) {
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(breakPointFile);
                String breakPointContent = start + "-" + end;
                fileOutputStream.write(breakPointContent.getBytes("UTF-8"));
                fileOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close(fileOutputStream);
            }
        }
    }

    /**
     * 删除断点记录文件
     */
    private void deleteFile(File file) {
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    /**
     * 关闭流
     */
    private void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
