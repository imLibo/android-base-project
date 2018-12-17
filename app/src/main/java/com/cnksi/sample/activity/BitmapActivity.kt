package com.cnksi.sample.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cnksi.android.glide.AppGlideUrl
import com.cnksi.android.glide.GlideApp
import com.cnksi.android.glide.transformation.RadiusTransformation
import com.cnksi.android.glide.widget.GridLayoutHelper
import com.cnksi.android.glide.widget.ImageData
import com.cnksi.android.log.KLog
import com.cnksi.android.utils.BitmapUtil
import com.cnksi.android.utils.FileUtil
import com.cnksi.sample.R
import com.cnksi.sample.databinding.ActivityBitmapBinding
import com.qq.weixin.mp.aes.Utils
import es.dmoral.toasty.Toasty
import java.util.*

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
            BitmapUtil.compressImage(picture_path, 80, 80)
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

        mBinding.btnLoad.setOnClickListener {
            //            val url = "http://10.177.98.252/v410/file/download?hSnUMaXbiGP4z5vSA7X/++nPpZCMB0gZr7UpXLwS8pCqaZsLJ5MRqAuIAbWy5wTpALHCNfEGH/j4rzBA09chbgB6JWn2YGnsgp7fnMN+B9GxIYtdPqouY/WjL3gOkMPTSPKyS8UdZ7OzqQI/mue06UFjeoBh6zpKjpiF0I+xta8="
            val url = getFileUrl("ls_ess", "pictures", "20181101151616695cef.jpg")
            //加载加密地址 用AppGlideUrl(url) 包装一次，解决加密地址缓存问题
            GlideApp.with(mActivity)
                    .asBitmap()
                    .load(AppGlideUrl(url))
                    .transform(RadiusTransformation(mActivity, 10))
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(mBinding.ivGlideImage)

            val url1 = "https://118.122.132.40:9090/upload/resultimg/4141702e484d6f05b4de0a2ccb9725673ca60/4141702e484d6f05b4de0a2ccb9725673ca60_2018121212415931069fb5035.jpg"
            mBinding.ivGlideProgressImage
                    .loadCircle(AppGlideUrl(url1)) { isComplete, percentage, bytesRead, totalBytes ->
                        KLog.d("precent->$percentage")
                        if (isComplete) {
                            mBinding.progressView1.visibility = View.GONE
                        } else {
                            mBinding.progressView1.visibility = View.VISIBLE
                            mBinding.progressView1.progress = percentage
                        }
                    }
        }

        mBinding.nineview.loadGif(true)
                .enableRoundCorner(true)
                .setRoundCornerRadius(5)
                .setData(getImageData(), GridLayoutHelper.getDefaultLayoutHelper(mActivity, getImageData()))

        mBinding.nineview.setOnItemClickListener {
            Toasty.info(mActivity, "position->$it").show()
        }
        mBinding.nineview.setOnLongClickListener {
            Toasty.info(mActivity, "position->$it").show()
            false
        }
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


    fun getImageData(): List<ImageData> {
        var images: MutableList<ImageData> = ArrayList()
        images.add(ImageData(AppGlideUrl("http://img3.imgtn.bdimg.com/it/u=3040385967,1031044866&fm=21&gp=0.jpg")))
        images.add(ImageData(AppGlideUrl("http://img1.imgtn.bdimg.com/it/u=1832737924,144748431&fm=21&gp=0.jpg")))
        images.add(ImageData(AppGlideUrl("http://img.zcool.cn/community/01d6dd554b93f0000001bf72b4f6ec.jpg")))
        images.add(ImageData(AppGlideUrl("http://5b0988e595225.cdn.sohucs.com/images/20171202/a1cc52d5522f48a8a2d6e7426b13f82b.gif")))
        images.add(ImageData(AppGlideUrl("http://img3.imgtn.bdimg.com/it/u=524208507,12616758&fm=206&gp=0.jpg")))
        images.add(ImageData(AppGlideUrl("http://img5.imgtn.bdimg.com/it/u=2091366266,1524114981&fm=21&gp=0.jpg")))
        images.add(ImageData(AppGlideUrl("http://img5.imgtn.bdimg.com/it/u=1424970962,1243597989&fm=21&gp=0.jpg")))
        return images
    }

    fun getImageData2(): List<ImageData> {
        var images: MutableList<ImageData> = ArrayList()
        images.add(ImageData(AppGlideUrl(getFileUrl("ls_ess", "pictures", "20181101151616695cef.jpg"))))
        images.add(ImageData(AppGlideUrl(getFileUrl("ls_ess", "pictures", "20181101151616695cef.jpg"))))
        images.add(ImageData(AppGlideUrl(getFileUrl("ls_ess", "pictures", "20181101151616695cef.jpg"))))
        images.add(ImageData(AppGlideUrl(getFileUrl("ls_ess", "pictures", "20181101151616695cef.jpg"))))
        images.add(ImageData(AppGlideUrl(getFileUrl("ls_ess", "pictures", "20181101151616695cef.jpg"))))
        images.add(ImageData(AppGlideUrl(getFileUrl("ls_ess", "pictures", "20181101151616695cef.jpg"))))
        images.add(ImageData(AppGlideUrl(getFileUrl("ls_ess", "pictures", "20181101151616695cef.jpg"))))
        return images
    }
}