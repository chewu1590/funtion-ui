package cn.woochen.functionui.samples

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import cn.woochen.function_ui.bubble_view.DragBubbleViewListener
import cn.woochen.function_ui.bubble_view.MessageBubbleView
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
        messageBubbleView.attachView(btn_hello,object : DragBubbleViewListener.BubbleDisappearListener {
            override fun dismiss(view: View?) {
                Toast.makeText(this@ViewDragActivity,"气泡消失",Toast.LENGTH_SHORT).show()
            }
        })
    }
}
