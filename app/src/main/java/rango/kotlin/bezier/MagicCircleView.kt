package rango.kotlin.bezier

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation
import kotlin.math.sin

class MagicCircleView @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyle: Int = 0
) : View(context, attributeSet, defStyle) {
    private var mPath = Path()
    private var mFillCirclePaint = Paint()

    /** View的中心坐标x  */
    private var centerX = 0

    /** View的中心坐标y  */
    private var centerY = 0

    private var maxLength = 0f
    private var mInterpolatedTime = 0f
    private var stretchDistance = 0f
    private var moveDistance = 0f
    private var cDistance = 0f
    private var radius = 0f
    private var c = 0f
    private val blackMagic = 0.551915024494f
    private var p2 = VPoint()
    private var p4 = VPoint()
    private var p1 = HPoint()
    private var p3 = HPoint()


    init {
        setBackgroundColor(Color.WHITE)
        mFillCirclePaint.color = -0x19d93
        mFillCirclePaint.style = Paint.Style.FILL
        mFillCirclePaint.strokeWidth = 1f
        mFillCirclePaint.isAntiAlias = true
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        centerX = width / 2
        centerY = height / 2
        radius = 50f
        c = radius * blackMagic
        stretchDistance = radius
        moveDistance = radius * (3 / 5f)
        cDistance = c * 0.45f
        maxLength = width - radius - radius
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPath.reset()
        canvas.translate(radius, radius*3)
        if (mInterpolatedTime >= 0 && mInterpolatedTime <= 0.2) {
            model1(mInterpolatedTime)
        } else if (mInterpolatedTime > 0.2 && mInterpolatedTime <= 0.5) {
            model2(mInterpolatedTime)
        } else if (mInterpolatedTime > 0.5 && mInterpolatedTime <= 0.8) {
            model3(mInterpolatedTime)
        } else if (mInterpolatedTime > 0.8 && mInterpolatedTime <= 0.9) {
            model4(mInterpolatedTime)
        } else if (mInterpolatedTime > 0.9 && mInterpolatedTime <= 1) {
            model5(mInterpolatedTime)
        }
        var offset = maxLength * (mInterpolatedTime - 0.2f)
        offset = if (offset > 0) offset else 0f
        p1.adjustAllX(offset)
        p2.adjustAllX(offset)
        p3.adjustAllX(offset)
        p4.adjustAllX(offset)
        mPath.moveTo(p1.x, p1.y)
        mPath.cubicTo(p1.right.x, p1.right.y, p2.bottom.x, p2.bottom.y, p2.x, p2.y)
        mPath.cubicTo(p2.top.x, p2.top.y, p3.right.x, p3.right.y, p3.x, p3.y)
        mPath.cubicTo(p3.left.x, p3.left.y, p4.top.x, p4.top.y, p4.x, p4.y)
        mPath.cubicTo(p4.bottom.x, p4.bottom.y, p1.left.x, p1.left.y, p1.x, p1.y)
        canvas.drawPath(mPath, mFillCirclePaint)
    }

    private fun model0() {
        p1.setYPosition(radius)
        p3.setYPosition(-radius)
        p1.x = 0f
        p3.x = p1.x
        p1.left.x = -c
        p3.left.x = p1.left.x
        p1.right.x = c
        p3.right.x = p1.right.x
        p2.setXPosition(radius)
        p4.setXPosition(-radius)
        p4.y = 0f
        p2.y = p4.y
        p4.top.y = -c
        p2.top.y = p4.top.y
        p4.bottom.y = c
        p2.bottom.y = p4.bottom.y
    }

    private fun model1(time: Float) { //0~0.2
        model0()
        p2.setXPosition(radius + stretchDistance * time * 5)
    }

    private fun model2(progress: Float) { //0.2~0.5
        model1(0.2f)
        val time = (progress - 0.2f) * (10f / 3)
        p1.adjustAllX(stretchDistance / 2 * time)
        p3.adjustAllX(stretchDistance / 2 * time)
        p2.adjustY(cDistance * time)
        p4.adjustY(cDistance * time)
    }

    private fun model3(progress: Float) { //0.5~0.8
        model2(0.5f)
        val time = (progress - 0.5f) * (10f / 3)
        p1.adjustAllX(stretchDistance / 2 * time)
        p3.adjustAllX(stretchDistance / 2 * time)
        p2.adjustY(-cDistance * time)
        p4.adjustY(-cDistance * time)
        p4.adjustAllX(stretchDistance / 2 * time)
    }

    private fun model4(progress: Float) { //0.8~0.9
        model3(0.8f)
        val time = (progress - 0.8f) * 10
        p4.adjustAllX(stretchDistance / 2 * time)
    }

    private fun model5(progress: Float) {
        model4(0.9f)
        val time = progress - 0.9f
        p4.adjustAllX((sin(Math.PI * time * 10f) * (2 / 10f * radius)).toFloat())
    }

    class VPoint {
        var x = 0f
        var y = 0f
        var top = PointF()
        var bottom = PointF()
        fun setXPosition(x: Float) {
            this.x = x
            top.x = x
            bottom.x = x
        }

        fun adjustY(offset: Float) {
            top.y -= offset
            bottom.y += offset
        }

        fun adjustAllX(offset: Float) {
            x += offset
            top.x += offset
            bottom.x += offset
        }
    }

    class HPoint {
        var x = 0f
        var y = 0f
        var left = PointF()
        var right = PointF()
        fun setYPosition(y: Float) {
            this.y = y
            left.y = y
            right.y = y
        }

        fun adjustAllX(offset: Float) {
            x += offset
            left.x += offset
            right.x += offset
        }
    }

    private inner class MoveAnimation : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            super.applyTransformation(interpolatedTime, t)
            mInterpolatedTime = interpolatedTime
            invalidate()
        }
    }

    fun startAnimation() {
        mPath.reset()
        mInterpolatedTime = 0f
        val move = MoveAnimation()
        move.duration = 1000
        move.interpolator = AccelerateDecelerateInterpolator()
        move.repeatCount = Animation.INFINITE;
        move.repeatMode = Animation.REVERSE;
        startAnimation(move)
    }
}