package com.cnksi.sample.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.cnksi.android.utils.BitmapUtils
import com.cnksi.android.utils.FileUtils
import com.cnksi.sample.R
import com.cnksi.sample.databinding.ActivityBitmapBinding

/**
 * BitmapUtils 测试
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

        val bitmap = BitmapUtils.getImageThumbnail(picture_path, 500, 200)
        mBinding.ivImage.setImageBitmap(bitmap)

        mBinding.btnCompressImage.setOnClickListener { _ ->
            //压缩图片到指定宽度
//            BitmapUtils.compressImage(picture_path, 80, 80)
        }

        mBinding.btnSaveImage.setOnClickListener { _ ->
            BitmapUtils.saveViewToPicture(mBinding.container, pictureFolder)
        }

        mBinding.btnFile.setOnClickListener { _ ->
            //            //递归获取文件夹下所有文件
//            val list = mutableListOf<String>()
//            FileUtils.getAllFiles(baseFolder, list, true)
//            list.filter { file ->
//                return@filter FileUtils.isFolderExists(file)
//            }.forEach { file ->
//                println(file)
//            }
            //删除所有文件
//            FileUtils.deleteAllFiles(baseFolder)

            FileUtils.renameFile(sdcard + "download/sample-debug.apk", sdcard + "download/sample-release.apk")
        }
    }
}