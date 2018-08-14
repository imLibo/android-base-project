package com.cnksi.sample.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.cnksi.android.log.KLog
import com.cnksi.android.view.LabelValueLayout
import com.cnksi.sample.R
import com.cnksi.sample.databinding.ActivityMainBinding
import com.cnksi.sample.model.Student
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 *
 */
class MainActivity : BaseActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onGranted(permissions: MutableList<String>?) {
        Toasty.success(mActivity, TextUtils.join(",", permissions)).show()
    }

    override fun onDenied(permissions: MutableList<String>) {
        Toasty.warning(mActivity, TextUtils.join(",", permissions)).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(toolbar)

        initLayout()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            mBinding.contentMain?.labelValueContainer?.lineHeight = 0
            mBinding.contentMain?.labelValueContainer?.lineWidth = 0
            mBinding.contentMain?.labelValueContainer?.spacing = 10
            mBinding.contentMain?.labelValueContainer?.valueTextColor = resources.getColor(R.color.bg_blue_04a5e7)
            mBinding.contentMain?.labelValueContainer?.labelTextColor = resources.getColor(R.color.bg_green_01ce7f)
            mBinding.contentMain?.labelValueContainer?.labelTextSize = 80
            mBinding.contentMain?.labelValueContainer?.valueTextSize = 30
            mBinding.contentMain?.labelValueContainer?.setDrawableResource(R.mipmap.ic_login_user)
            mBinding.contentMain?.labelValueContainer?.drawableText = "密码"
            mBinding.contentMain?.labelValueContainer?.drawableView?.width = 150
            mBinding.contentMain?.labelValueContainer?.drawableView?.height = 90
            mBinding.contentMain?.lineLayout?.setMarginLeft(100)

//            mBinding.contentMain?.cdt?.text = "用户名"
            mBinding.contentMain?.cdt?.setDrawableResource((R.mipmap.ic_login_user))
        }

        // Example of a call to a native method
//        sample_text.text = stringFromJNI()

        mBinding.contentMain?.labelValuehrContainer?.labelView?.text = "发现方式："
        mBinding.contentMain?.labelValuehrContainer?.valueView?.setText("带电检测")

        mBinding.contentMain?.labelValueContainer?.labelView?.text = "设备类型"
        mBinding.contentMain?.labelValueContainer?.valueView?.setText("开关柜")
        mBinding.contentMain?.labelValueContainer?.setValueTextOnClickListener { _ ->
            Toasty.info(this, mBinding.contentMain?.labelValueContainer?.valueText.toString()).show()
        }
        mBinding.contentMain?.labelValueContainer?.setDrawableOnClickListener { _ ->
            Toasty.info(this, mBinding.contentMain?.labelValueContainer?.valueText.toString()).show()
        }

        mBinding.contentMain?.sampleText?.setOnClickListener { _ ->
            mBinding.contentMain?.labelValueContainer?.lineHeight = 25
            mBinding.contentMain?.labelValueContainer?.lineWidth = 25
            mBinding.contentMain?.labelValueContainer?.lineMarginBottom = 15
            mBinding.contentMain?.labelValueContainer?.lineMarginTop = 15
            mBinding.contentMain?.labelValueContainer?.lineMarginLeft = 65
            mBinding.contentMain?.labelValueContainer?.lineMarginRight = 65
        }

        val buttons = Arrays.asList(mBinding.contentMain?.borderButton, mBinding.contentMain?.borderButton2)
        for (button in buttons) {
            button?.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    KLog.d("BorderButton", buttonView.text.toString() + "checked")
                } else {
                    KLog.d("BorderButton", buttonView.text.toString() + "unchecked")
                }
            }
        }

        mBinding.contentMain?.btnChange?.setOnClickListener { _ ->
            mBinding.contentMain?.borderButton?.strokeWidth = 5
            mBinding.contentMain?.borderButton?.shape = 1
            mBinding.contentMain?.borderButton2?.shape = 0
            mBinding.contentMain?.borderButton2?.radius = 20
            mBinding.contentMain?.borderButton2?.checkedColor = resources.getColor(R.color.colorAccent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }


    private fun initLayout() {
        for (row in 1..2) {
            val horContainer = LinearLayout(this)
            horContainer.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            for (column in 1..2) {
                val container = LabelValueLayout(this)
                container.orientation = LinearLayout.VERTICAL
                container.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                container.labelText = "组合设备类型 $row $column"
                container.valueText = "开关柜 $row $column"
                val padding = 30
                container.setPadding(padding, padding, padding, padding)
                container.labelTextColor = resources.getColor(R.color.bg_green_01ce7f)
                container.valueTextColor = resources.getColor(R.color.bg_red_ff7575)
                container.lineHeight = 2
                container.lineColor = resources.getColor(R.color.bg_blue_04a5e7)
                if (column == 2) {
                    container.lineWidth = 2
                    container.lineMarginTop = 35
                    container.lineMarginBottom = 35
                }
                if (column == 1) {
                    container.lineMarginLeft = padding
                }
                container.tag = Student("张三", 20)
                horContainer.addView(container)
            }
            mBinding.contentMain?.container?.addView(horContainer)
        }
    }
}
