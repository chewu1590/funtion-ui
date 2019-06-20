package cn.woochen.function_ui.load

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CircleView:View {
    private var mColor:Int = 0

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    private val mPaint by lazy {
        Paint().apply {
            this.isAntiAlias = true
            this.isDither = true
        }
    }

    override fun onDraw(canvas: Canvas?) {
        val cx = width
        val cy  = height
        canvas?.drawCircle((cx / 2).toFloat(), (cx / 2).toFloat(), (cx / 2).toFloat(),mPaint)
    }


    /**
     * 改变颜色
     */
    fun changeColor(color :Int){
        mColor = color
        mPaint.color = color
        invalidate()
    }

    fun getColor() = mColor
}