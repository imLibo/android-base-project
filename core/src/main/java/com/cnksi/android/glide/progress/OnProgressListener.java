package com.cnksi.android.glide.progress;

/**
 * @author by sunfusheng on 2017/6/14.
 * https://github.com/sunfusheng/GlideImageView
 */
public interface OnProgressListener {
    void onProgress(boolean isComplete, int percentage, long bytesRead, long totalBytes);
}
