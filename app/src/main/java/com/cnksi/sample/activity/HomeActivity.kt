package com.cnksi.sample.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cnksi.android.log.KLog
import com.cnksi.android.utils.PreferencesUtil
import com.cnksi.sample.R
import com.cnksi.sample.databinding.ActivityHomeBinding
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
            val intent = Intent(this@HomeActivity, mList[position].activity)
            startActivity(intent)
        }

        PreferencesUtil.put("String", "zhangsan")
        PreferencesUtil.put("Float", 2.3f)
        PreferencesUtil.put("Integer", 10)
        PreferencesUtil.put("Boolean", true)
        PreferencesUtil.put("Long", Long.MAX_VALUE)
        val set = HashSet<String>()
        set.add("zhangsan")
        set.add("lisi")
        set.add("wangwu")
        PreferencesUtil.put("Set", set)

        val string = PreferencesUtil.get("String1", "lisi")
        val long = PreferencesUtil.get("Long2", 20L)
        val float = PreferencesUtil.get("Float2", 1.1f)
        val boolean = PreferencesUtil.get("Boolean2", false)
        val int = PreferencesUtil.get("Integer2", 666)
        val sets = PreferencesUtil.get("Set2", HashSet())
        KLog.d(("String->" + string + " Long->" + long + " Float->" + float + " Boolean->" + boolean + " Integer->" + int) as Any)

        KLog.d(("Set->" + sets.toString()) as Any)


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