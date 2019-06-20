package cn.woochen.functionui.samples.menu_choice.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.woochen.function_ui.muti_menu.BaseMenuAdapter
import cn.woochen.function_ui.muti_menu.BaseMenuObserver
import cn.woochen.functionui.R

class ChoiceMenuAdapter : BaseMenuAdapter {

    private var mObserver: BaseMenuObserver? = null

    override fun registerObserver(observer: BaseMenuObserver){
        mObserver = observer
    }


    override fun unRegisterObserver(observer: BaseMenuObserver){
        mObserver = null
    }

    override fun menuOpen(tabView: View) {
        val textView = tabView as TextView
        textView.setTextColor(Color.RED)
    }

    override fun menuClose(tabView: View?) {
        val textView = tabView as TextView
        textView.setTextColor(Color.BLACK)
    }

    fun notifyMenuClose(){
        mObserver?.toCloseMenu()
    }

    private val mItems = listOf<String>("汽车", "数码产品", "日用百货", "生活用品")

    private var mContext: Context

    constructor(context: Context) : super() {
        this.mContext = context
    }

    override fun getCount(): Int = mItems.size

    override fun getMenuTabView(position: Int, parent: ViewGroup): View {
        val inflate = LayoutInflater.from(mContext).inflate(R.layout.item_menu_tab, parent, false) as TextView
        inflate.text = mItems[position]
        return inflate
    }

    @SuppressLint("SetTextI18n")
    override fun getContentView(position: Int, parent: ViewGroup): View {
        val inflate = LayoutInflater.from(mContext).inflate(R.layout.item_menu_content, parent, false) as TextView
        inflate.text = mItems[position]
        inflate.setOnClickListener {
            notifyMenuClose()
        }
        return inflate
    }
}