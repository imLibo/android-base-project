package com.cnksi.sample.activity

import android.Manifest
import android.os.Environment
import com.cnksi.android.base.BaseCoreActivity
import com.cnksi.android.utils.FileUtil
import java.io.File

/**
 *
 * @author lyongfly
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/7/17
 * @version 1.0
 * @since 1.0
 */
open class BaseActivity : BaseCoreActivity() {
    val sdcard = Environment.getExternalStorageDirectory().absolutePath + File.separator
    val baseFolder = sdcard + "Core/"
    val pictureFolder = baseFolder + "picture/"
    val folders = arrayListOf(pictureFolder)

    override fun getPermission(): Array<String> {
        return arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    override fun onGranted(permissions: MutableList<String>?) {
        folders.forEach { folder ->
            FileUtil.makeDirectory(folder)
        }
    }

}