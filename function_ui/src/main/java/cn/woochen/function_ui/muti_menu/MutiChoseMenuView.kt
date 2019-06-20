package cn.woochen.function_ui.muti_menu

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import java.lang.IllegalArgumentException

/**
 *混合菜单下拉选择器
 *@author woochen
 *@time 2019/6/17 17:01
 */
class MutiChoseMenuView : LinearLayout {
    private val tag = "MutiChoseMenuView"

    private lateinit var mMenuCotainerView: LinearLayout

    private lateinit var mContentContainerView: FrameLayout

    private var mContentContainerViewHeight: Float = 0f


    private val DURATION_TIME: Long = 350

    private var isAnimating: Boolean = false

    private lateinit var mShadowView: View


    private val mShadowViewColor: Int = 0x88888888.toInt()
    var menuAdapter: BaseMenuAdapter? = null
        set(value) {
            if (value == null) throw IllegalArgumentException("please set adapter first")
            field = value
            initMenuView()
        }


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    /**
     * 布局初始化
     */
    override fun onFinishInflate() {
        super.onFinishInflate()
        //1.确定整体构成：LinearLayout(LinearLayout + FrameLayout(View + FrameLayout))
        //2.完成菜单切换功能
        //3.添加动画
        //4.测试调优
        //5.补充功能：使用观察者模式，让adapter调用当前类的方法，如关闭菜单
        orientation = LinearLayout.VERTICAL
        mMenuCotainerView = LinearLayout(context)
        mMenuCotainerView.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        addView(mMenuCotainerView)
        val mBottomContainerView = FrameLayout(context)
        mBottomContainerView.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        addView(mBottomContainerView)
        mShadowView = View(context)
        mShadowView.setBackgroundColor(mShadowViewColor)
        mBottomContainerView.addView(mShadowView)
        mShadowView.setOnClickListener {
            closeMenu()
        }
        mContentContainerView = FrameLayout(context)
        mContentContainerView.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        mBottomContainerView.addView(mContentContainerView)
    }


    private var mCurrentPosition: Int = -1

    private var mMenuObserver: MenuObserver? = null

    inner class MenuObserver : BaseMenuObserver {
        override fun toCloseMenu() {
            closeMenu()
        }
    }

    /**
     * 布局填充
     */
    private fun initMenuView() {
        //注册观察者
        if (mMenuObserver!= null)menuAdapter?.unRegisterObserver(mMenuObserver!!)
        mMenuObserver = MenuObserver()
        menuAdapter?.registerObserver(mMenuObserver!!)
        val count = menuAdapter?.getCount()
        for (index in 0 until count!!) {
            //menu
            val menuTabView = menuAdapter?.getMenuTabView(index, mMenuCotainerView)
            mMenuCotainerView.addView(menuTabView)
            val layoutParams = menuTabView?.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 1f
            menuTabView.layoutParams = layoutParams
            menuTabView.setOnClickListener {
                menuTabClick(index)
            }
            //content
            val contentView = menuAdapter?.getContentView(index, mMenuCotainerView)
            contentView?.visibility = View.GONE
            mContentContainerView.setBackgroundColor(Color.RED)
            mContentContainerView.addView(contentView)
        }
    }

    /**
     * 菜单点击事件
     */
    private fun menuTabClick(index: Int) {
        if (mCurrentPosition == -1) {
            openMenu(index)
        } else {
            if (mCurrentPosition == index) {
                //打开了，关闭菜单
                closeMenu()
            } else {
                //打开了，直接切换显示内容
                mContentContainerView.getChildAt(mCurrentPosition).visibility = View.GONE
                menuAdapter?.menuClose(mMenuCotainerView.getChildAt(mCurrentPosition))
                mContentContainerView.getChildAt(index).visibility = View.VISIBLE
                menuAdapter?.menuOpen(mMenuCotainerView.getChildAt(index))
                mCurrentPosition = index
            }
        }
    }

    /**
     * 关闭菜单
     */
    @SuppressLint("ObjectAnimatorBinding")
    private fun closeMenu() {
        if (isAnimating) return
        //动画
        val objectAnimator =
            ObjectAnimator.ofFloat(mContentContainerView, "translationY", 0f, -mContentContainerViewHeight)
        val shadowAnimator = ObjectAnimator.ofFloat(mShadowView, "alpha", 1f, 0f)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(objectAnimator, shadowAnimator)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                menuAdapter?.menuClose(mMenuCotainerView.getChildAt(mCurrentPosition))
                isAnimating = true
            }

            override fun onAnimationEnd(animation: Animator?) {
                mContentContainerView.getChildAt(mCurrentPosition).visibility = View.GONE
                mShadowView.visibility = View.GONE
                mCurrentPosition = -1
                isAnimating = false
            }
        })
        animatorSet.setDuration(DURATION_TIME).start()
    }


    /**
     * 打开菜单
     */
    @SuppressLint("ObjectAnimatorBinding")
    private fun openMenu(position: Int) {
        if (isAnimating) return
        if (mContentContainerView.visibility == View.GONE) mContentContainerView.visibility = View.VISIBLE
        mContentContainerView.getChildAt(position).visibility = View.VISIBLE
        mShadowView.visibility = View.VISIBLE
        //动画
        val objectAnimator =
            ObjectAnimator.ofFloat(mContentContainerView, "translationY", -mContentContainerViewHeight, 0f)
        val shadowAnimator = ObjectAnimator.ofFloat(mShadowView, "alpha", 0f, 1f)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(objectAnimator, shadowAnimator)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                menuAdapter?.menuOpen(mMenuCotainerView.getChildAt(position))
                isAnimating = true
            }

            override fun onAnimationEnd(animation: Animator?) {
                isAnimating = false
            }
        })
        animatorSet.setDuration(DURATION_TIME).start()
        mCurrentPosition = position
    }


    @SuppressLint("ObjectAnimatorBinding")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        Log.e(tag, "height:$height")
        if (mContentContainerViewHeight == 0f && height > 0) {
            val layoutParams = mContentContainerView.layoutParams
            mContentContainerViewHeight = height * 0.75f
            layoutParams.height = mContentContainerViewHeight.toInt()
            mContentContainerView.layoutParams = layoutParams
            mContentContainerView.visibility = View.GONE
            mShadowView.visibility = View.GONE
        }
    }

}