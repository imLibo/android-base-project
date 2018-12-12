package com.cnksi.sample.activity

import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.cnksi.android.https.glide.DecryptUrl
import com.cnksi.android.https.glide.GlideApp
import com.cnksi.android.log.KLog
import com.cnksi.android.utils.BitmapUtil
import com.cnksi.android.utils.FileUtil
import com.cnksi.sample.R
import com.cnksi.sample.databinding.ActivityBitmapBinding
import com.qq.weixin.mp.aes.Utils

/**
 * BitmapUtil 测试
 * @author lyongfly
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/7/17
 * @version 1.0
 * @since 1.0
 */
class BitmapActivity : BaseActivity() {

    private lateinit var mBinding: ActivityBitmapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this@BitmapActivity, R.layout.activity_bitmap)
        val picture_path = pictureFolder + "123.png"

        val bitmap = BitmapUtil.getImageThumbnail(picture_path, 500, 200)
        mBinding.ivImage.setImageBitmap(bitmap)

        mBinding.btnCompressImage.setOnClickListener { _ ->
            //压缩图片到指定宽度
//            BitmapUtil.compressImage(picture_path, 80, 80)
        }

        mBinding.btnSaveImage.setOnClickListener { _ ->
            BitmapUtil.saveViewToPicture(mBinding.container, pictureFolder)
        }

        mBinding.btnFile.setOnClickListener { _ ->
            //            //递归获取文件夹下所有文件
//            val list = mutableListOf<String>()
//            FileUtil.getAllFiles(baseFolder, list, true)
//            list.filter { file ->
//                return@filter FileUtil.isFolderExists(file)
//            }.forEach { file ->
//                println(file)
//            }
            //删除所有文件
//            FileUtil.deleteAllFiles(baseFolder)

            FileUtil.renameFile(sdcard + "download/sample-debug.apk", sdcard + "download/sample-release.apk")
        }

        mBinding.btnLoad.setOnClickListener { _ ->
            //            val url = "http://10.177.98.252/v410/file/download?hSnUMaXbiGP4z5vSA7X/++nPpZCMB0gZr7UpXLwS8pCqaZsLJ5MRqAuIAbWy5wTpALHCNfEGH/j4rzBA09chbgB6JWn2YGnsgp7fnMN+B9GxIYtdPqouY/WjL3gOkMPTSPKyS8UdZ7OzqQI/mue06UFjeoBh6zpKjpiF0I+xta8="
//            val url = getFileUrl("ls_ess", "pictures", "20181101151616695cef.jpg")
            val url = "https://118.122.132.40:9090/upload/resultimg/4141702e484d6f05b4de0a2ccb9725673ca60/4141702e484d6f05b4de0a2ccb9725673ca60_2018121212415931069fb5035.jpg"
            GlideApp.with(mActivity)
                    .asBitmap()
                    .load(DecryptUrl(url))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .placeholder(R.mipmap.ic_launcher)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                            KLog.e(e)
                            return false
                        }

                        override fun onResourceReady(resource: Bitmap, model: Any, target: Target<Bitmap>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                            return false
                        }
                    })
                    .error(R.mipmap.ic_launcher_round)
                    .into(mBinding.ivGlideImage)
        }

//        GlideApp.with(mActivity).load(DecryptUrl(url)).into(mBinding.ivGlideImage)
    }

    /**
     * 得到文件在线浏览地址
     *
     * @param folder   文件夹
     * @param fileName 文件名
     * @return 完整的加密后的地址
     */
    fun getFileUrl(appid: String, folder: String, fileName: String): String {
        val paramStr = "appid=$appid&folder=$folder&filename=$fileName&clientid=0000"
        val url = "http://10.177.98.252/v410/file/download?" + Utils.getEncryptMsg(paramStr)
        //替换掉所有的换行符
        return url.replace("\n".toRegex(), "")
    }
}