package cn.woochen.function_ui.love_view

import android.animation.TypeEvaluator
import android.graphics.PointF

class BezeierTypeEvaluator : TypeEvaluator<PointF> {
    private var point1: PointF?= null
    private var point2: PointF?= null

    constructor(point1: PointF,point2: PointF){
        this.point1 = point1
        this.point2 = point2
    }


    override fun evaluate(t: Float, point0: PointF?, point3: PointF?): PointF {
        val pointF = PointF()
        //p0,p1,p2,p3 ,p0 p3为固定点 p1,p2为动态点
        pointF.x = point0?.x!! *(1-t)*(1-t)*(1-t)+ 3*point1?.x!!*t*(1-t)*(1-t)+ 3*point2?.x!!*t*t*(1-t)+ point3?.x!!*t*t*t
        pointF.y = point0.y*(1-t)*(1-t)*(1-t)+ 3*point1?.y!!*t*(1-t)*(1-t)+ 3*point2?.y!!*t*t*(1-t)+ point3.y*t*t*t
        return pointF
    }
}