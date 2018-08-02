package com.cnksi.sample.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cnksi.android.log.KLog
import com.cnksi.android.utils.PreferencesUtil
import com.cnksi.sample.R
import com.cnksi.sample.databinding.ActivityHomeBinding
import com.cnksi.sample.dialog.CustomDialog
import com.cnksi.sample.model.HomeItem

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

        mBinding.rcvContainer.layoutManager = GridLayoutManager(this, 2)
        mAdapter = HomeAdapter(R.layout.home_item_layout, mList)
        mBinding.rcvContainer.adapter = mAdapter

        mAdapter.setOnItemChildClickListener { _, _, position ->
//            val intent = Intent(this@HomeActivity, mList[position].activity)
//            startActivity(intent)
            val dialog = CustomDialog()
            dialog.show(supportFragmentManager,"")
        }

        PreferencesUtil.put("String", "zhangsan")
        PreferencesUtil.put("Float", 2.3f)
        PreferencesUtil.put("Integer", 10)
        PreferencesUtil.put("Boolean", true)
        PreferencesUtil.put("Long", Long.MAX_VALUE)
        PreferencesUtil.put("preference", "String", "zhangsan")
        PreferencesUtil.put("preference", "Float", 2.3f)
        PreferencesUtil.put("preference", "Integer", 10)
        PreferencesUtil.put("preference", "Boolean", true)
        PreferencesUtil.put("preference", "Long", Long.MAX_VALUE)

        val set = HashSet<String>()
        set.add("zhangsan")
        set.add("lisi")
        set.add("wangwu")
        PreferencesUtil.put("Set", set)
        PreferencesUtil.put("preference", "Set", set)

        val string = PreferencesUtil.get("String", "lisi")
        val long = PreferencesUtil.get("Long", 20L)
        val float = PreferencesUtil.get("Float", 1.1f)
        val boolean = PreferencesUtil.get("Boolean", false)
        val int = PreferencesUtil.get("Integer", 666)
        val sets = PreferencesUtil.get("Set", HashSet())
        KLog.d(("String->" + string + " Long->" + long + " Float->" + float + " Boolean->" + boolean + " Integer->" + int) as Any)

        KLog.d(("Set->" + sets.toString()) as Any)

        val string1 = PreferencesUtil.get("preference", "String", "lisi")
        val long1 = PreferencesUtil.get("preference", "Long", 20L)
        val float1 = PreferencesUtil.get("preference", "Float", 1.1f)
        val boolean1 = PreferencesUtil.get("preference", "Boolean", false)
        val int1 = PreferencesUtil.get("preference", "Integer", 666)
        val sets1 = PreferencesUtil.get("preference", "Set", HashSet())

        KLog.d(("String->" + string1 + " Long->" + long1 + " Float->" + float1 + " Boolean->" + boolean1 + " Integer->" + int1) as Any)

        KLog.d(("Set->" + sets1.toString()) as Any)
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
}