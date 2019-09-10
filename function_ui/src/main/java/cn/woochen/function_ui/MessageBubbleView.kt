package cn.woochen.function_ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.graphics.Bitmap



class MessageBubbleView : View {

    private var mAbsoluteCircle: CircleF? = null
    private var mFixedCircle: CircleF? = null
    private val mFixedCircleRadius = dp2px(15f)

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
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
        if (mAbsoluteCircle != null){
            canvas?.drawCircle(mFixedCircle?.x!!,mFixedCircle?.y!!,mFixedCircleRadius,paint)
            val mAbosoluteCircleRadius = (mFixedCircleRadius - calcCircleDistance() / 14f).toFloat()
            if (mAbosoluteCircleRadius > dp2px(5f)){
                canvas?.drawCircle(mAbsoluteCircle?.x!!,mAbsoluteCircle?.y!!, mAbosoluteCircleRadius,paint)
                //计算角a
                val a = Math.atan(((mFixedCircle?.y!! - mAbsoluteCircle?.y!!) / (mFixedCircle?.x!! - mAbsoluteCircle?.x!!)).toDouble())
                val p1 =CircleF(((mAbsoluteCircle?.x!! - mAbosoluteCircleRadius*Math.sin(a)).toFloat()),((mAbsoluteCircle?.y!! + mAbosoluteCircleRadius*Math.cos(a)).toFloat()))
                val p2 =CircleF(((mAbsoluteCircle?.x!! + mAbosoluteCircleRadius*Math.sin(a)).toFloat()),((mAbsoluteCircle?.y!! - mAbosoluteCircleRadius*Math.cos(a)).toFloat()))
                val p3 =CircleF(((mFixedCircle?.x!! + mFixedCircleRadius*Math.sin(a)).toFloat()),((mFixedCircle?.y!! - mFixedCircleRadius*Math.cos(a)).toFloat()))
                val p4 =CircleF(((mFixedCircle?.x!! - mFixedCircleRadius*Math.sin(a)).toFloat()),((mFixedCircle?.y!! + mFixedCircleRadius*Math.cos(a)).toFloat()))
                val p =CircleF(((mFixedCircle?.x!! + mAbsoluteCircle?.x!!) / 2),(mFixedCircle?.y!! + mAbsoluteCircle?.y!!) / 2)
                //绘制别塞尔曲线
                canvas?.drawPath(getPath(p,p1,p2,p3,p4),paint)
            }
        }

        if (mDragBitmap != null){
            canvas?.drawBitmap(mDragBitmap,0f,0f,paint)
        }
    }

    /**
     * 得到贝塞尔路径
     */
    private fun getPath(p: CircleF, p1: CircleF, p2: CircleF, p3: CircleF, p4: CircleF): Path {
        val path = Path()
        path.moveTo(p2.x,p2.y)
        path.quadTo(p.x,p.y,p3.x,p3.y)
        path.lineTo(p4.x,p4.y)
        path.quadTo(p.x,p.y,p1.x,p1.y)
        path.close()
        return path
    }

    /**
     * 计算两个点的距离
     */
    private fun calcCircleDistance(): Double {
        val dx = mFixedCircle?.x  !!- mAbsoluteCircle?.x!!
        val dy = mFixedCircle?.y  !!- mAbsoluteCircle?.y!!
       return Math.sqrt((dx*dx + dy*dy).toDouble())
    }

    private fun dp2px(dip:Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip,context.resources.displayMetrics)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y
                mAbsoluteCircle = CircleF(x, y)
                mFixedCircle = CircleF(x, y)
                if (mDragView != null){
                    mDragBitmap = getBitmapByView(mDragView!!)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                mFixedCircle?.x = event.x
                mFixedCircle?.y = event.y
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        invalidate()
        return true
    }


    class CircleF(var x: Float, var y: Float)

    private var mDragBitmap:Bitmap?= null
    private var mDragView:View?= null


    fun attachView(view: View){
        mDragView = view
        //1.为view添加touch监听，处理touch事件
        view.setOnTouchListener()
        //1.按下时隐藏view，并生成一个bitmap，将bitmap绘制在画布上
    }


    /**
     * 从一个View中获取Bitmap
     *
     * @param view
     * @return
     */
    private fun getBitmapByView(view: View): Bitmap {
        view.buildDrawingCache()
        return view.drawingCache
    }

}