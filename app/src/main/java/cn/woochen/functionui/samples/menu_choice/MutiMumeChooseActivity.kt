package cn.woochen.functionui.samples.menu_choice

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cn.woochen.functionui.R
import cn.woochen.functionui.samples.menu_choice.adapter.ChoiceMenuAdapter
import kotlinx.android.synthetic.main.activity_muti_mume_choose.*

/**
 *下拉多选菜单演示类
 *@author woochen
 *@time 2019/6/17 17:25
 */
class MutiMumeChooseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muti_mume_choose)
        initListener()
        initView()
    }

    private fun initListener() {
        tv_test.setOnClickListener {
            Toast.makeText(this,"哈哈哈哈啊",Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() {
        mcv.menuAdapter = ChoiceMenuAdapter(this)
    }
}
