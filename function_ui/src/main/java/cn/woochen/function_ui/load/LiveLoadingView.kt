package cn.woochen.function_ui.load

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout

/**
 *直播加载动画（生命周期需要优化处理，不能直接使用）
 *@author woochen
 *@time 2019/6/18 14:50
 */
class LiveLoadingView : RelativeLayout {

    val mLeftView by lazy {
        getCircleView()
    }
    val mMiddleView by lazy {
        getCircleView()
    }
    val mRightView by lazy {
        getCircleView()
    }

    val TRANSLATION_DISTANCE = dp2px(20f)

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initView()
    }



    /**
     * 初始化视图
     */
    private fun initView() {
        mLeftView.changeColor(Color.RED)
        mMiddleView.changeColor(Color.GREEN)
        mRightView.changeColor(Color.BLUE)
        addView(mLeftView)
        addView(mRightView)
        addView(mMiddleView)
        post {
            executeExpandAnimator()
        }
    }

    private val DURATION_TIME: Long = 350

    /**
     * 执行展开动画
     */
    private fun executeExpandAnimator() {
        val leftAnimation = ObjectAnimator.ofFloat(mLeftView, "translationX", 0f, -TRANSLATION_DISTANCE.toFloat())
        val rightAnimation = ObjectAnimator.ofFloat(mRightView, "translationX", 0f, TRANSLATION_DISTANCE.toFloat())
        val animatorSet = AnimatorSet()
        animatorSet.interpolator = DecelerateInterpolator()
        animatorSet.playTogether(leftAnimation,rightAnimation)
        animatorSet.addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                executeShrinkAnimator()
            }
        })
        animatorSet.setDuration(DURATION_TIME).start()
    }


    /**
     * 执行收缩动画
     */
    private fun executeShrinkAnimator() {
        val leftAnimation = ObjectAnimator.ofFloat(mLeftView, "translationX",  -TRANSLATION_DISTANCE.toFloat(),0f)
        val rightAnimation = ObjectAnimator.ofFloat(mRightView, "translationX",  TRANSLATION_DISTANCE.toFloat(),0f)
        val animatorSet = AnimatorSet()
        animatorSet.interpolator = AccelerateInterpolator()
        animatorSet.playTogether(leftAnimation,rightAnimation)
        animatorSet.addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                //改变颜色
                val leftColor = mLeftView.getColor()
                val middleColor = mMiddleView.getColor()
                val rightColor = mRightView.getColor()
                mMiddleView.changeColor(leftColor)
                mRightView.changeColor(middleColor)
                mLeftView.changeColor(rightColor)
                executeExpandAnimator()

            }
        })
        animatorSet.setDuration(DURATION_TIME).start()
    }


    fun getCircleView():CircleView {
        val circleView = CircleView(context)
        val layoutParam = RelativeLayout.LayoutParams(dp2px(10f), dp2px(10f))
        layoutParam.addRule(CENTER_IN_PARENT)
        circleView.layoutParams = layoutParam
        return  circleView
    }

    private fun dp2px(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.resources.displayMetrics).toInt()
    }
}