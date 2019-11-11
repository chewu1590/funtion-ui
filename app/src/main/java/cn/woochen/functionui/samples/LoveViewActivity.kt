package cn.woochen.functionui.samples

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cn.woochen.functionui.R
import kotlinx.android.synthetic.main.activity_love_view.*

class LoveViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_love_view)
    }


    fun addLoveView(view: View) {
        loveView.addLoveView()
    }
}
