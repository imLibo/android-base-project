package com.cnksi.sample.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.cnksi.android.utils.BitmapUtil
import com.cnksi.android.utils.FileUtil
import com.cnksi.sample.R
import com.cnksi.sample.databinding.ActivityBitmapBinding

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
    }
}