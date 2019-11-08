package cn.woochen.function_ui.bubble_view

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.PointF
import android.graphics.drawable.AnimationDrawable
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import cn.woochen.function_ui.R

class DragBubbleViewListener : View.OnTouchListener, MessageBubbleView.ActionUpListener {


    private var mDragView: View? = null
    private var mMessageBubbleView: MessageBubbleView? = null
    private var mWindowManager: WindowManager? = null
    private var mContext: Context? = null
    private var mBombFrame :FrameLayout? = null
    private var mBombImageView :ImageView? = null
    private var mWindowManagerLayoutParams :WindowManager.LayoutParams? = null

    constructor(
        view: View,
        context: Context,
        bubbleDisappearListener: BubbleDisappearListener
    ) {
        this.mDragView = view
        this.mContext = context
        this.mBubbleDisappearListener = bubbleDisappearListener
        mMessageBubbleView = MessageBubbleView(mContext)
        mBombFrame = FrameLayout(mContext!!)
        mBombImageView = ImageView(mContext)
        mBombFrame?.addView(mBombImageView,FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT)
        mMessageBubbleView?.mActionUpListener = this
        mWindowManager = mContext?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                //1.按下时隐藏view
                mDragView?.visibility = View.GONE
                //2.将bubbleView放在windowManager上进行，并初始化
                 mWindowManagerLayoutParams = WindowManager.LayoutParams()
                mWindowManagerLayoutParams?.format = PixelFormat.TRANSPARENT
                mWindowManager?.addView(mMessageBubbleView, mWindowManagerLayoutParams)
                //需要保证固定圆的圆心在view的中心
                val location = IntArray(2)
                mDragView?.getLocationOnScreen(location)
                mMessageBubbleView?.initPoint(
                    location[0].toFloat() + mDragView?.width !!/ 2,
                    location[1].toFloat() + mDragView?.height !!/ 2 - getStatusBarHeight(mContext!!)
                )
                mMessageBubbleView?.setDragBitmap(mDragView)
            }
            MotionEvent.ACTION_MOVE -> {
                mMessageBubbleView?.updatePoint(
                    event.rawX,
                    event.rawY - getStatusBarHeight(mContext!!)
                )
            }
            MotionEvent.ACTION_UP -> {
                //3.处理抬起的事件，如果bezier曲线存在，就回弹；否则爆炸
                mMessageBubbleView?.handlerActionUp()
            }

        }
        return true
    }


    /**
     * 获取状态栏的高度
     * @param context
     * @return
     */
    private fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }




    override fun restore() {
        mDragView?.visibility = View.VISIBLE
        mWindowManager?.removeView(mMessageBubbleView)
    }

    override fun bomb(pointF:PointF) {
        mWindowManager?.removeView(mMessageBubbleView)
        //在windowManager上添加一个爆炸的帧动画
        mWindowManager?.addView(mBombFrame,mWindowManagerLayoutParams)
        mBombImageView?.setBackgroundResource(R.drawable.anim_bubble_pop)
        val drawable = mBombImageView?.background as AnimationDrawable
        mBombImageView?.x = pointF.x - drawable.intrinsicWidth / 2
        mBombImageView?.y = pointF.y - drawable.intrinsicHeight / 2
        drawable.start()
        mBombImageView?.postDelayed(
            {
                mWindowManager?.removeView(mBombFrame)
                //通知外面气泡消失
                mBubbleDisappearListener?.dismiss(mDragView)
            },
            getAnimationDuration(drawable))
    }


    /**
     * 获取帧动画执行事件
     */
    private fun getAnimationDuration(drawable: AnimationDrawable): Long {
        val numberOfFrames = drawable.numberOfFrames
        var time = 0L
        for(index in 0 until numberOfFrames){
            time += drawable.getDuration(index)
        }
        return  time
    }

    private var mBubbleDisappearListener:BubbleDisappearListener? = null

    interface  BubbleDisappearListener{
        fun dismiss(view:View?)
    }

}