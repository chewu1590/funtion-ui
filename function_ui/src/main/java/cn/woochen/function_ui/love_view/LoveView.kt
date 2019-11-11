package cn.woochen.function_ui.love_view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.PointF
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.RelativeLayout
import kotlin.random.Random
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.AccelerateDecelerateInterpolator
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class LoveView : RelativeLayout {
    private var mWidth :Int = 0
    private var mHeight :Int = 0
    private var mDrawableWidth :Int = 0
    private var mDrawableHeight :Int = 0
    private var mInterpolator: Array<Interpolator>? = null

    private var mContext:Context?=null
    private val intArray by lazy {
        intArrayOf(cn.woochen.function_ui.R.drawable.pl_blue,
            cn.woochen.function_ui.R.drawable.pl_yellow, cn.woochen.function_ui.R.drawable.pl_red)
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.mContext = context
        val drawable = ContextCompat.getDrawable(mContext!!, cn.woochen.function_ui.R.drawable.pl_blue)
        mDrawableWidth = drawable!!.intrinsicWidth
        mDrawableHeight = drawable.intrinsicHeight
        mInterpolator = arrayOf(
            AccelerateDecelerateInterpolator(),
            AccelerateInterpolator(),
            DecelerateInterpolator(),
            LinearInterpolator()
        )
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
         mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
    }


    fun addLoveView(){
        val imageView = ImageView(mContext)
        imageView.setImageResource(intArray[Random.nextInt(intArray.size)])
        val param = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        param.addRule(ALIGN_PARENT_BOTTOM)
        param.addRule(CENTER_HORIZONTAL)
        addView(imageView,param)
        addAppearAnimator(imageView)
    }

    /**
     * 添加组合动画
     */
    private fun addAppearAnimator(iv: ImageView) {
        //1.添加普通属性动画(透明度 放大)
        val allAnimatorSet = AnimatorSet()
        val innerAnimatorSet = AnimatorSet()
        val alphaAnimator = ObjectAnimator.ofFloat(iv, "alpha", 0f, 1f)
        val scaleXAnimator = ObjectAnimator.ofFloat(iv, "scaleX", 0f, 1f)
        val scaleYAnimator = ObjectAnimator.ofFloat(iv, "scaleY", 0f, 1f)
        innerAnimatorSet.playTogether(alphaAnimator,scaleXAnimator,scaleYAnimator)
        innerAnimatorSet.duration = 1000
        //2.添加贝塞尔路径动画
        val point1 = PointF(Random.nextInt(mWidth).toFloat(),Random.nextInt(mHeight / 2).toFloat())
        val point2 = PointF(Random.nextInt(mWidth).toFloat(),mHeight / 2 +Random.nextInt(mHeight / 2).toFloat())
        val bezeierTypeEvaluator = BezeierTypeEvaluator(point1,point2)
        val point0 = PointF((mWidth / 2 - mDrawableWidth / 2).toFloat(), (mHeight - mDrawableHeight).toFloat())
        val point3 = PointF((Random.nextInt(mWidth).toFloat()-mDrawableWidth/2), 0f)
        val bezeierAnimator = ObjectAnimator.ofObject(bezeierTypeEvaluator, point0, point3)
        bezeierAnimator.addUpdateListener {
            val animatedValue = it.animatedValue as PointF
            Log.e("LoveView","x:${animatedValue.x} y:${animatedValue.y}")
            iv.x = animatedValue.x
            iv.y = animatedValue.y
        }
        bezeierAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                    removeView(iv)
            }
        })
        bezeierAnimator.interpolator = mInterpolator!![Random.nextInt(mInterpolator?.size!!)]
        bezeierAnimator.duration = 2000
        //按顺序执行动画
        allAnimatorSet.playSequentially(innerAnimatorSet,bezeierAnimator)
        allAnimatorSet.start()
    }
}