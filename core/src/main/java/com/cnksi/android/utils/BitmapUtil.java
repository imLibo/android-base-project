package com.cnksi.android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Images.Thumbnails;
import android.text.TextUtils;
import android.view.View;

import com.cnksi.android.constants.Constants;
import com.cnksi.android.log.KLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Bitmap工具类
 *
 * @author lyongfly
 * @version 1.0
 * @date 2016/12/20
 * @copyRight 四川金信石信息技术有限公司
 * @since 1.0
 */
public class BitmapUtil {

    /**
     * ALPHA_8就是Alpha由8位组成 ARGB_4444就是由4个4位组成即16位， ARGB_8888就是由4个8位组成即32位， RGB_565就是R为5位，G为6位，B为5位共16位
     * <p/>
     * 由此可见： ALPHA_8 代表8位Alpha位图 ARGB_4444 代表16位ARGB位图 ARGB_8888 代表32位ARGB位图 RGB_565 代表8位RGB位图
     * <p/>
     * 位图位数越高代表其可以存储的颜色信息越多，当然图像也就越逼真。
     */
    private static int DEFAULT_QUALITY = 80;

    /**
     * <pre>
     * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处：
     *  1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     *  第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。
     *  2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使用这个工具生成的图像不会被拉伸。
     * </pre>
     *
     * @param imagePath 图像的路径
     * @param width     指定输出图像的宽度
     * @param height    指定输出图像的高度
     * @return 生成的缩略图
     */
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        BitmapFactory.decodeFile(imagePath, options);
        // 设为 false
        options.inJustDecodeBounds = false;
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        return getImageThumbnail(bitmap, width, height);
    }

    /**
     * 根据宽获得等比例压缩的缩略图
     *
     * @param imagePath
     * @param width
     * @return
     */
    public static Bitmap getImageThumbnailByWidth(String imagePath, int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        BitmapFactory.decodeFile(imagePath, options);
        // 设为 false
        options.inJustDecodeBounds = false;
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int be = w / width;
        int height = (int) (h * ((float) width / (float) w));
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        return getImageThumbnail(bitmap, width, height);
    }

    /**
     * 根据高获取等比例压缩的缩略图
     *
     * @param imagePath
     * @param height
     * @return
     */
    public static Bitmap getImageThumbnailByHeight(String imagePath, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        BitmapFactory.decodeFile(imagePath, options);
        // 设为 false
        options.inJustDecodeBounds = false;
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int be = h / height;
        int width = (int) (w * ((float) height / (float) h));
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        return getImageThumbnail(bitmap, width, height);
    }

    /**
     * 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getImageThumbnail(Bitmap bitmap, int width, int height) {
        return ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
    }

    /**
     * * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height) {
        Bitmap bitmap;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, Thumbnails.MICRO_KIND);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。 其中，MINI_KIND: 512 X 384，MICRO_KIND: 96 X 96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 判断调用系统相机 照相返回的照片 是否需要旋转
     *
     * @param imagePath 图片绝对路径
     * @return 旋转后的图片
     */
    public static Bitmap postRotateBitmap(String imagePath) {
        return postRotateBitmap(imagePath, false);
    }

    /**
     * 判断调用系统相机 照相返回的照片 是否需要旋转
     *
     * @param imagePath        图片绝对路径
     * @param isHandPostRotate 是否需要手动旋转
     * @return 旋转后的图片
     */
    public static Bitmap postRotateBitmap(String imagePath, boolean isHandPostRotate) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmapResult;
        int degree = readPictureDegree(imagePath);
        if (degree != 0) {
            // 旋转图片
            Matrix m = new Matrix();
            m.postRotate(degree);
            options.inJustDecodeBounds = false;
            Bitmap bitmapTemp = BitmapFactory.decodeFile(imagePath, options);
            bitmapResult = Bitmap.createBitmap(bitmapTemp, 0, 0, options.outWidth, options.outHeight, m, true);
        } else if (options.outWidth > options.outHeight && isHandPostRotate) {
            // 旋转图片
            Matrix m = new Matrix();
            m.postRotate(90);
            options.inJustDecodeBounds = false;
            Bitmap bitmapTemp = BitmapFactory.decodeFile(imagePath, options);
            bitmapResult = Bitmap.createBitmap(bitmapTemp, 0, 0, options.outWidth, options.outHeight, m, true);
        } else {
            options.inJustDecodeBounds = false;
            bitmapResult = BitmapFactory.decodeFile(imagePath, options);
        }
        return bitmapResult;
    }

    /**
     * 获取图片的高度
     *
     * @param context 上下文
     * @param resId   资源id
     * @return 图片的高度
     */
    public static int getBitmapHeight(Context context, int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        int height = options.outHeight;
        options.inJustDecodeBounds = false;
        return height;
    }

    /**
     * 获取图片的高度
     *
     * @param filePath 图片绝对路径
     * @return 图片的高度
     */
    public static int getBitmapHeight(String filePath) {
        // 获取图片的宽高
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return options.outHeight;
    }

    /**
     * 获取图片的宽度
     *
     * @param filePath 图片绝对路径
     * @return 图片的宽度
     */
    public static int getBitmapWidth(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return options.outWidth;
    }

    /**
     * 获取图片的宽度
     *
     * @param context 上下文
     * @param resId   资源id
     * @return 图片的宽度
     */
    public static int getBitmapWidth(Context context, int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        int width = options.outWidth;
        options.inJustDecodeBounds = false;
        return width;
    }

    /**
     * 获取图片的方向
     *
     * @param imgpath 图片路径
     * @return 图片的角度
     */
    public static int readPictureDegree(String imgpath) {
        int degree = 0;
        ExifInterface exif;
        try {
            exif = new ExifInterface(imgpath);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }
        if (exif != null) {
            // 读取图片中相机方向信息
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            // 计算旋转角度
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
                    break;
            }
        }
        return degree;
    }

    /**
     * 根据新高度缩放图片
     *
     * @param imagePath 源图片 路径
     * @param newHeight 新高度
     * @return 缩放后的图片
     */
    public static Bitmap scaleBitmapByHeight(String imagePath, int newHeight) {
        // 获取这个图片的宽和高，注意此处的bitmap为null
        Bitmap srcBitmap = BitmapFactory.decodeFile(imagePath, new BitmapFactory.Options());
        return srcBitmap == null ? srcBitmap : scaleBitmapByHeight(srcBitmap, newHeight);
    }

    /**
     * 根据新高度缩放图片
     *
     * @param srcBitmap 源图片
     * @param newHeight 新高度
     * @return 缩放后的图片
     */
    public static Bitmap scaleBitmapByHeight(Bitmap srcBitmap, int newHeight) {
        int srcHeight = srcBitmap.getHeight();
        float scaleHeight = ((float) newHeight) / srcHeight;
        float scaleWidth = scaleHeight;
        return createScaledBitmap(srcBitmap, scaleWidth, scaleHeight);
    }

    /**
     * 根据新宽度缩放图片
     *
     * @param imagePath 源图片 路径
     * @param newWidth  新宽度
     * @return 缩放后的图片
     */
    public static Bitmap scaleBitmapByWidth(String imagePath, int newWidth) {
        Bitmap srcBitmap = BitmapFactory.decodeFile(imagePath, new BitmapFactory.Options());
        return srcBitmap == null ? srcBitmap : scaleBitmapByWidth(srcBitmap, newWidth);
    }

    /**
     * 根据新宽度缩放图片
     *
     * @param srcBitmap 源图片
     * @param newWidth  新宽度
     * @return 缩放后的图片
     */
    public static Bitmap scaleBitmapByWidth(Bitmap srcBitmap, int newWidth) {
        int srcWidth = srcBitmap.getWidth();
        float scaleWidth = ((float) newWidth) / srcWidth;
        float scaleHeight = scaleWidth;
        return createScaledBitmap(srcBitmap, scaleWidth, scaleHeight);
    }

    /**
     * 将图片缩放到指定大小
     *
     * @param srcBitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap srcBitmap, int newWidth, int newHeight) {
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / srcWidth;
        float scaleHeight = ((float) newHeight) / srcHeight;
        return createScaledBitmap(srcBitmap, scaleWidth, scaleHeight);
    }

    /**
     * 使用长宽缩放比缩放
     *
     * @param bitmap      源图片
     * @param scaleWidth  源图片的缩放率 如 0.8f 就是 源图片宽度的0.8倍
     * @param scaleHeight 源图片的缩放率 如 0.8f 就是 源图片高度的0.8倍
     * @return 缩放后的图片
     */
    private static Bitmap createScaledBitmap(Bitmap bitmap, float scaleWidth, float scaleHeight) {
        int srcWidth = bitmap.getWidth();
        int srcHeight = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, srcWidth, srcHeight, matrix, true);
        return resizedBitmap;
    }


    /**
     * 保存手写笔记
     *
     * @param view        View
     * @param picturePath 保存文件的绝对路径
     * @param quality     质量
     * @return
     */
    public static boolean saveSignToPicture(View view, String picturePath, int quality) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
        byte[] buffer = bos.toByteArray();
        if (buffer != null) {
            try {
                File file = new File(picturePath);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (file.exists()) {
                    file.delete();
                }
                OutputStream outputStream = new FileOutputStream(file);
                outputStream.write(buffer);
                outputStream.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                view.setDrawingCacheEnabled(false);
            }
        }
        return false;
    }


    /**
     * 将控件保存为图片
     *
     * @param view   空间对象
     * @param folder 保存文件的文件夹路径 如/sdCard/pictures/
     * @return 文件名称
     */
    public static String saveViewToPicture(View view, String folder) {
        String fileName = "";
        try {
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = view.getDrawingCache();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, DEFAULT_QUALITY, bos);
            byte[] buffer = bos.toByteArray();
            if (buffer != null) {
                fileName = System.currentTimeMillis() + Constants.JPG_POSTFIX;
                File file = new File(folder, fileName);
                OutputStream outputStream = new FileOutputStream(file);
                outputStream.write(buffer);
                outputStream.close();
            }
            view.setDrawingCacheEnabled(false);
        } catch (IOException e) {
            KLog.e(e);
        }
        return fileName;
    }

    /**
     * 将Bitmap保存为图片
     *
     * @param bitmap   bitmap对象
     * @param filePath 保存的文件路径，如：/sdcard/namecard/123.jpg
     */
    public static void saveBitmap(Bitmap bitmap, String filePath) {
        saveBitmap(bitmap, filePath, DEFAULT_QUALITY);
    }

    /**
     * 保存图片
     *
     * @param bitmap   bitmap
     * @param filePath 文件保存完整路径  /sdcard/image/xxx.jpg
     * @param quality  图片质量  1-100;
     */
    public static void saveBitmap(Bitmap bitmap, String filePath, int quality) {
        if (bitmap == null) {
            return;
        }
        if (!(0 < quality && quality <= 100)) {
            quality = 100;
        }
        File f = new File(filePath);
        if (f.exists() && f.delete()) {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                KLog.e(e);
            } finally {
                IOUtil.closeQuietly(out);
            }
        }
    }

    /**
     * 保存bitmap到指定路径 文件
     *
     * @param bitmap  bitmap
     * @param folder  文件夹
     * @param picName 文件名称
     */
    public static void saveBitmap(Bitmap bitmap, String folder, String picName) {
        saveBitmap(bitmap, folder + File.separator + picName);
    }

    /**
     * 保存图片
     *
     * @param bitmap   bitmap
     * @param folder   文件夹
     * @param fileName 文件名称
     * @param quality  图片质量 1-100
     */
    public static void saveBitmap(Bitmap bitmap, String folder, String fileName, int quality) {
        saveBitmap(bitmap, folder + File.separator + fileName, quality);
    }

    /**
     * 图片压缩 - 质量压缩方法
     *
     * @param bitmap  bitmap
     * @param quality 质量
     * @return
     */
    public static Bitmap compressImage(Bitmap bitmap, int quality) {
        if (bitmap == null) {
            return null;
        }
        if (!(0 < quality && quality <= 100)) {
            quality = 100;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        int options = quality;
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 100) {
            // 每次都减少10
            options -= 10;
            if (options <= 0) {
                break;
            }
            // 重置baos即清空baos
            baos.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap resultBitmap = BitmapFactory.decodeStream(isBm, null, null);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return resultBitmap;
    }

    /**
     * 图片压缩 质量压缩
     *
     * @param pathName 文件路径
     * @return 压缩后的图片
     */
    public static Bitmap compressImage(String pathName) {
        return compressImage(BitmapFactory.decodeFile(pathName), DEFAULT_QUALITY);
    }

    /**
     * 质量压缩方法
     *
     * @param bitmap bitmap
     * @return 压缩后的图片
     */
    public static Bitmap compressImage(Bitmap bitmap) {
        return compressImage(bitmap, DEFAULT_QUALITY);
    }

    /**
     * 压缩一张存在SDCard上的图片 到指定的高宽
     *
     * @param srcPath   图片绝对路径
     * @param reqWidth  压缩后的宽度
     * @param reqHeight 压缩后的高度
     */
    public static void compressImage(String srcPath, int reqWidth, int reqHeight) {
        if (!TextUtils.isEmpty(srcPath) && FileUtil.isFileExists(srcPath)) {
            int scale;
            // 首先不加载图片,仅获取图片尺寸
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // 当inJustDecodeBounds设为true时,不会加载图片仅获取图片尺寸信息
            options.inJustDecodeBounds = true;
            // 此时仅会将图片信息会保存至options对象内,decode方法不会返回bitmap对象
            BitmapFactory.decodeFile(srcPath, options);
            // 计算缩放率
            scale = calculateInSampleSize(options, reqWidth, reqHeight);
            // 计算压缩比例,如inSampleSize=4时,图片会压缩成原图的1/4
            options.inSampleSize = scale;
            // 当inJustDecodeBounds设为false时,BitmapFactory.decode...就会返回图片对象了
            options.inJustDecodeBounds = false;
            // 利用计算的比例值获取压缩后的图片对象
            Bitmap bitmap = BitmapFactory.decodeFile(srcPath, options);
            // 保存图片
            saveBitmap(bitmap, srcPath);
        }
    }

    /**
     * 计算图片的缩放值
     *
     * @param options   bitmap options
     * @param reqWidth  缩放后的宽度
     * @param reqHeight 缩放后的高度
     * @return 压缩率
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }
        return inSampleSize;
    }

    /**
     * 旋转图片
     *
     * @param imagePath 原图片
     * @param degree    旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmap(String imagePath, int degree) {
        if (FileUtil.isFileExists(imagePath)) {
            return rotateBitmap(BitmapFactory.decodeFile(imagePath), degree);
        }
        return null;
    }

    /**
     * 旋转图片
     *
     * @param bitmap 原图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix mtx = new Matrix();
        mtx.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

}
