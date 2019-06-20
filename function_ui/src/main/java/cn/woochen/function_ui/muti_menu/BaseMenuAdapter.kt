package cn.woochen.function_ui.muti_menu

import android.view.View
import android.view.ViewGroup

abstract class BaseMenuAdapter {
    abstract fun getCount():Int
    /**
     * 菜单条目
     */
    abstract fun getMenuTabView(position:Int,parent:ViewGroup):View

    /**
     * 内容视图
     */
    abstract fun getContentView(position:Int,parent:ViewGroup):View

    /**
     * 菜单打开
     */
    abstract fun menuOpen(tabView: View)

    abstract fun menuClose(tabView: View?)


    abstract fun registerObserver(observer: BaseMenuObserver)

    abstract fun unRegisterObserver(observer: BaseMenuObserver)

}