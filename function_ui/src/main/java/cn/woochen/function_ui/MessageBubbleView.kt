package cn.woochen.function_ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class MessageBubbleView : View {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        //1.绘制两个圆，一个固定圆，一个拖拽圆
        //2.固定圆的大小与圆心间的距离成反比
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y
                val cicirF = CicirF(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        invalidate()
        return true
    }


    class CicirF( var x:Float, var y:Float)

}