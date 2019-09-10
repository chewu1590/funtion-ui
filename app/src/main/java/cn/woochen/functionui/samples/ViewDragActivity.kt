package cn.woochen.functionui.samples

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cn.woochen.function_ui.MessageBubbleView
import cn.woochen.functionui.R
import kotlinx.android.synthetic.main.activity_view_drag.*

/**
 *视图拖拽演示类
 *@author woochen
 *@time 2019/8/21 17:34
 */
class ViewDragActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_drag)
        initView()
    }

    private fun initView() {
        val messageBubbleView = MessageBubbleView(this)
        messageBubbleView.attachView(btn_hello)
    }
}
