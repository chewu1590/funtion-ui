package cn.woochen.function_ui.bubble_view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator


class MessageBubbleView : View {

    private var mDragBitmap: Bitmap? = null
    private var mAbsolutePoint: PointF? = null//固定圆
    private var mDragPoint: PointF? = null//拖拽圆
    private val mFixedCircleRadius = dp2px(15f)

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        //1.绘制两个圆，一个固定圆，一个拖拽圆
        //2.固定圆的大小与圆心间的距离成反比
        //3.绘制贝塞尔路径
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        val paint = Paint()
        paint.color = Color.RED
        paint.isAntiAlias = true
        paint.isDither = true
        if (mAbsolutePoint != null) {
            canvas?.drawCircle(mDragPoint?.x!!, mDragPoint?.y!!, mFixedCircleRadius, paint)
            val mAbosoluteCircleRadius = (mFixedCircleRadius - calcCircleDistance() / 14f).toFloat()
            if (mAbosoluteCircleRadius > dp2px(5f)) {
                canvas?.drawCircle(
                    mAbsolutePoint?.x!!,
                    mAbsolutePoint?.y!!,
                    mAbosoluteCircleRadius,
                    paint
                )
                //计算角a
                val a =
                    Math.atan(((mDragPoint?.y!! - mAbsolutePoint?.y!!) / (mDragPoint?.x!! - mAbsolutePoint?.x!!)).toDouble())
                val p1 = PointF(
                    ((mAbsolutePoint?.x!! - mAbosoluteCircleRadius * Math.sin(a)).toFloat()),
                    ((mAbsolutePoint?.y!! + mAbosoluteCircleRadius * Math.cos(a)).toFloat())
                )
                val p2 = PointF(
                    ((mAbsolutePoint?.x!! + mAbosoluteCircleRadius * Math.sin(a)).toFloat()),
                    ((mAbsolutePoint?.y!! - mAbosoluteCircleRadius * Math.cos(a)).toFloat())
                )
                val p3 = PointF(
                    ((mDragPoint?.x!! + mFixedCircleRadius * Math.sin(a)).toFloat()),
                    ((mDragPoint?.y!! - mFixedCircleRadius * Math.cos(a)).toFloat())
                )
                val p4 = PointF(
                    ((mDragPoint?.x!! - mFixedCircleRadius * Math.sin(a)).toFloat()),
                    ((mDragPoint?.y!! + mFixedCircleRadius * Math.cos(a)).toFloat())
                )
                val p = PointF(
                    ((mDragPoint?.x!! + mAbsolutePoint?.x!!) / 2),
                    (mDragPoint?.y!! + mAbsolutePoint?.y!!) / 2
                )
                //绘制别塞尔曲线
                canvas?.drawPath(getPath(p, p1, p2, p3, p4), paint)
            }
        }
        if (mDragBitmap != null) {
            canvas?.drawBitmap(
                mDragBitmap,
                mDragPoint?.x!! - mDragBitmap?.width!! / 2,
                mDragPoint?.y!! - mDragBitmap?.height!! / 2,
                paint
            )
        }
    }

    /**
     * 得到贝塞尔路径
     */
    private fun getPath(p: PointF, p1: PointF, p2: PointF, p3: PointF, p4: PointF): Path {
        val path = Path()
        path.moveTo(p2.x, p2.y)
        path.quadTo(p.x, p.y, p3.x, p3.y)
        path.lineTo(p4.x, p4.y)
        path.quadTo(p.x, p.y, p1.x, p1.y)
        path.close()
        return path
    }

    /**
     * 计算两个点的距离
     */
    private fun calcCircleDistance(): Double {
        val dx = mDragPoint?.x!! - mAbsolutePoint?.x!!
        val dy = mDragPoint?.y!! - mAbsolutePoint?.y!!
        return Math.sqrt((dx * dx + dy * dy).toDouble())
    }

    private fun dp2px(dip: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            context.resources.displayMetrics
        )
    }

/*    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y
                initPoint(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                val x = event.x
                val y = event.y
                updatePoint(x,y)
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        invalidate()
        return true
    }*/

    /**
     * 更新圆点的位置
     */
    fun updatePoint(x: Float, y: Float) {
        mDragPoint?.x = x
        mDragPoint?.y = y
        invalidate()
    }

    /**
     * 初始化圆点的位置
     */
    fun initPoint(x: Float, y: Float) {
        mAbsolutePoint = PointF(x, y)
        mDragPoint = PointF(x, y)
        invalidate()
    }


    fun attachView(view: View,bubbleDisappearListener: DragBubbleViewListener.BubbleDisappearListener) {
        //1.为view添加touch监听，处理touch事件
        view.setOnTouchListener(DragBubbleViewListener(view, view.context,bubbleDisappearListener))

    }


    /**
     * 设置拖拽view的bitmap
     */
    fun setDragBitmap(view: View?) {
        if (view == null) return
        mDragBitmap = getBitmapByView(view)
    }


    /**
     * 从一个View中获取Bitmap
     *
     * @param view
     * @return
     */
    private fun getBitmapByView(view: View?): Bitmap {
        view?.buildDrawingCache()
        return view?.drawingCache!!
    }


    /**
     * 处理抬起事件
     */
    fun handlerActionUp() {
        if ((mFixedCircleRadius - calcCircleDistance() / 14f).toFloat() > dp2px(5f)) {
            //回弹
            val valueAnimator = ValueAnimator.ofFloat(1f)
            valueAnimator.addUpdateListener {
                val start = PointF(mDragPoint?.x!!, mDragPoint?.y!!)
                val end = PointF(mAbsolutePoint?.x!!, mAbsolutePoint?.y!!)
                val animatedValue = it.animatedValue
                val pointByPercent = BubbleUtils.getPointByPercent(start, end, animatedValue as Float)
                updatePoint(pointByPercent.x,pointByPercent.y)
            }
            valueAnimator.interpolator = OvershootInterpolator(3f)
            valueAnimator.setDuration(350).start()
            valueAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    mActionUpListener?.restore()
                }
            })
        } else {
            //爆炸
            mActionUpListener?.bomb(mDragPoint!!)
        }
    }

     var mActionUpListener:ActionUpListener? = null


    interface ActionUpListener{
        fun restore()
        fun bomb(pointF: PointF)
    }

}