package com.cnksi.sample.model

import com.cnksi.sample.activity.BitmapActivity
import com.cnksi.sample.activity.MainActivity

/**
 *
 * @author lyongfly
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/7/17
 * @version 1.0
 * @since 1.0
 */
data class HomeItem(val title: String, val activity: Class<*>) {
    companion object {
        fun getList(): List<HomeItem> {
            return arrayListOf(
                    HomeItem("BitmapUtils", BitmapActivity::class.java),
                    HomeItem("自定义View", MainActivity::class.java)
            )
        }
    }
}
