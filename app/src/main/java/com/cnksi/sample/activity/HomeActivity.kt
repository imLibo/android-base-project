package com.cnksi.sample.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cnksi.android.base.LoadingDialog
import com.cnksi.android.log.KLog
import com.cnksi.sample.R
import com.cnksi.sample.databinding.ActivityHomeBinding
import com.cnksi.sample.dialog.CustomDialog
import com.cnksi.sample.model.HomeItem
import es.dmoral.toasty.Toasty

/**
 *
 * @author lyongfly
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/7/17
 * @version 1.0
 * @since 1.0
 */
class HomeActivity : BaseActivity() {

    private lateinit var mBinding: ActivityHomeBinding
    private lateinit var mAdapter: HomeAdapter
    private val mList = HomeItem.getList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        PreferencesUtil()
        mBinding.rcvContainer.layoutManager = GridLayoutManager(this, 2)
        mAdapter = HomeAdapter(R.layout.home_item_layout, mList)
        mBinding.rcvContainer.adapter = mAdapter

        mAdapter.setOnItemChildClickListener { _, view, position ->
            val clazz = mList[position].activity
            val code = mList[position].code
            if (code == 1) {
                //TODO:自定义Dialog 使用示例
                val dialog = CustomDialog()
                dialog.show(supportFragmentManager)
                dialog.setOnDismissListener({ _ ->
                    Toasty.success(HomeActivity@ this, "dismiss").show()
                })
            } else if (code == 2) {
//                val dialog = LoadingDialog()
//                dialog.setLoadingContent("加载中...")
//                dialog.show(supportFragmentManager)
//                LoadingDialog.show(HomeActivity@ this)
                val dialog = LoadingDialog.showHorizontal("正在同步数据，请稍候...", HomeActivity@ this)
                view.postDelayed({
                    LoadingDialog.showHorizontal("正在同步数据2，请稍候...", HomeActivity@ this)
                }, 2000)
            } else {
                val intent = Intent(this@HomeActivity, clazz)
                startActivity(intent)
            }
        }
    }

    /**
     * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
     */
    class HomeAdapter(layoutResId: Int, data: List<HomeItem>) : BaseQuickAdapter<HomeItem, BaseViewHolder>(layoutResId, data) {
        override fun convert(helper: BaseViewHolder, item: HomeItem) {
            helper.setText(R.id.tv_title, item.title)
            helper.addOnClickListener(R.id.tv_title)
        }
    }


    /**
     * PreferenceUtil 使用示例
     */
    fun PreferencesUtil() {
        com.cnksi.android.utils.PreferencesUtil.put("String", "zhangsan")
        com.cnksi.android.utils.PreferencesUtil.put("Float", 2.3f)
        com.cnksi.android.utils.PreferencesUtil.put("Integer", 10)
        com.cnksi.android.utils.PreferencesUtil.put("Boolean", true)
        com.cnksi.android.utils.PreferencesUtil.put("Long", Long.MAX_VALUE)
        com.cnksi.android.utils.PreferencesUtil.put("preference", "String", "zhangsan")
        com.cnksi.android.utils.PreferencesUtil.put("preference", "Float", 2.3f)
        com.cnksi.android.utils.PreferencesUtil.put("preference", "Integer", 10)
        com.cnksi.android.utils.PreferencesUtil.put("preference", "Boolean", true)
        com.cnksi.android.utils.PreferencesUtil.put("preference", "Long", Long.MAX_VALUE)

        val set = HashSet<String>()
        set.add("zhangsan")
        set.add("lisi")
        set.add("wangwu")
        com.cnksi.android.utils.PreferencesUtil.put("Set", set)
        com.cnksi.android.utils.PreferencesUtil.put("preference", "Set", set)

        val string = com.cnksi.android.utils.PreferencesUtil.get("String", "lisi")
        val long = com.cnksi.android.utils.PreferencesUtil.get("Long", 20L)
        val float = com.cnksi.android.utils.PreferencesUtil.get("Float", 1.1f)
        val boolean = com.cnksi.android.utils.PreferencesUtil.get("Boolean", false)
        val int = com.cnksi.android.utils.PreferencesUtil.get("Integer", 666)
        val sets = com.cnksi.android.utils.PreferencesUtil.get("Set", HashSet())
        KLog.d(("String->" + string + " Long->" + long + " Float->" + float + " Boolean->" + boolean + " Integer->" + int) as Any)

        KLog.d(("Set->" + sets.toString()) as Any)

        val string1 = com.cnksi.android.utils.PreferencesUtil.get("preference", "String", "lisi")
        val long1 = com.cnksi.android.utils.PreferencesUtil.get("preference", "Long", 20L)
        val float1 = com.cnksi.android.utils.PreferencesUtil.get("preference", "Float", 1.1f)
        val boolean1 = com.cnksi.android.utils.PreferencesUtil.get("preference", "Boolean", false)
        val int1 = com.cnksi.android.utils.PreferencesUtil.get("preference", "Integer", 666)
        val sets1 = com.cnksi.android.utils.PreferencesUtil.get("preference", "Set", HashSet())

        KLog.d(("String->" + string1 + " Long->" + long1 + " Float->" + float1 + " Boolean->" + boolean1 + " Integer->" + int1) as Any)

        KLog.d(("Set->" + sets1.toString()) as Any)
    }

}