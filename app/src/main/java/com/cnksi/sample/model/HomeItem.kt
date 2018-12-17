package com.cnksi.sample.model

import com.cnksi.sample.activity.BitmapActivity
import com.cnksi.sample.activity.DownloadActivity
import com.cnksi.sample.activity.LoginActivity
import com.cnksi.sample.activity.MainActivity

/**
 *
 * @author lyongfly
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/7/17
 * @version 1.0
 * @since 1.0
 */
data class HomeItem(val title: String, val activity: Class<*>, val code: Int) {
    companion object {
        fun getList(): List<HomeItem> {
            return arrayListOf(
                    HomeItem("BitmapUtil", BitmapActivity::class.java, 0),
                    HomeItem("OkHttp下载", DownloadActivity::class.java, 0),
                    HomeItem("自定义View", MainActivity::class.java, 0),
                    HomeItem("自定义Dialog", String::class.java, 1),
                    HomeItem("LoadingDialog", String::class.java, 2),
                    HomeItem("StepProgressView", String::class.java, 3),
                    HomeItem("LoginActivity", LoginActivity::class.java, 4),
                    HomeItem("主线程异常", LoginActivity::class.java, 5),
                    HomeItem("子线程异常", LoginActivity::class.java, 6),
                    HomeItem("PopupWindow", LoginActivity::class.java, 7),
                    HomeItem("LableValueLayout", LoginActivity::class.java, 8)
            )
        }
    }
}
