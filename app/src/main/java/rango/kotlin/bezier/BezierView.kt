package rango.kotlin.bezier

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import rango.tool.common.utils.ScreenUtils
import kotlin.math.abs

class BezierView
@JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyle: Int = 0)
    : View(context, attributeSet, defStyle) {

    companion object {
        const val TWO_STEPS = 2
        const val THREE_STEPS = 3
    }

    private var currentSteps = TWO_STEPS

    private val paint = Paint()
    private val path = Path()
    private val startPoint = PointF(0f, 0f)
    private val endPoint = PointF(0f, 0f)
    private val controlPoint = PointF(0f, 0f)
    private val control2Point = PointF(0f, 0f)

    private val width10 = ScreenUtils.dp2px(10f).toFloat()
    private val width2 = ScreenUtils.dp2px(2f).toFloat()
    private val width4 = ScreenUtils.dp2px(4f).toFloat()

    init {

        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = ScreenUtils.dp2px(5f).toFloat()
        paint.strokeCap = Paint.Cap.ROUND
        setBackgroundColor(Color.WHITE)
    }

    fun setBezierSteps(steps: Int) {
        currentSteps = steps
        requestLayout()
    }

    private fun isThreeSteps(): Boolean {
        return currentSteps == THREE_STEPS
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val w = right - left
        val h = bottom - top

        val centerX = w / 2f
        val centerY = h / 2f

        val offset = w / 3f

        startPoint.x = centerX - offset
        startPoint.y = centerY

        endPoint.x = centerX + offset
        endPoint.y = centerY



        if (isThreeSteps()) {
            val offset2 = w / 4f
            controlPoint.x = centerX - offset2
            controlPoint.y = centerY - offset

            control2Point.x = centerX + offset2
            control2Point.y = centerY - offset
        } else {
            controlPoint.x = centerX
            controlPoint.y = centerY - offset
        }


    }

    override fun onDraw(canvas: Canvas?) {
        drawPoint(canvas)
        drawAssistLine(canvas)
        drawBezier(canvas)
    }

    private fun drawPoint(canvas: Canvas?) {
        canvas?.run {
            paint.color = Color.BLACK
            paint.strokeWidth = width10
            drawPoint(startPoint.x, startPoint.y, paint)
            drawPoint(endPoint.x, endPoint.y, paint)

            paint.color = Color.RED
            drawPoint(controlPoint.x, controlPoint.y, paint)

            if (isThreeSteps()) {
                drawPoint(control2Point.x, control2Point.y, paint)
            }
        }
    }

    private fun drawAssistLine(canvas: Canvas?) {
        canvas?.run {
            paint.color = Color.GRAY
            paint.strokeWidth = width2
            drawLine(startPoint.x, startPoint.y, controlPoint.x, controlPoint.y, paint)

            if (isThreeSteps()) {
                drawLine(controlPoint.x, controlPoint.y, control2Point.x, control2Point.y, paint)
                drawLine(control2Point.x, control2Point.y, endPoint.x, endPoint.y, paint)
            } else {
                drawLine(controlPoint.x, controlPoint.y, endPoint.x, endPoint.y, paint)
            }
        }
    }

    private fun drawBezier(canvas: Canvas?) {
        canvas?.run {
            paint.color = Color.RED
            paint.strokeWidth = width4
            path.reset()
            path.moveTo(startPoint.x, startPoint.y)
            if (isThreeSteps()) {
                path.cubicTo(controlPoint.x, controlPoint.y, control2Point.x, control2Point.y, endPoint.x, endPoint.y)
            } else {
                path.quadTo(controlPoint.x, controlPoint.y, endPoint.x, endPoint.y)
            }

            canvas.drawPath(path, paint)
        }
    }

    private var controlP = controlPoint

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.run {
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    controlP = getControlPoint(x, y)
                }
            }
            controlP.x = x
            controlP.y = y
            invalidate()
        }
        return true
    }

    private fun getControlPoint(x: Float, y: Float): PointF {
        val firstDis = abs(x - controlPoint.x) * abs(x - controlPoint.x) + abs(y - controlPoint.y) * abs(y - controlPoint.y)
        val secondDis = abs(x - control2Point.x) * abs(x - control2Point.x) + abs(y - control2Point.y) * abs(y - control2Point.y)
        return if (firstDis > secondDis) {
            control2Point
        } else {
            controlPoint
        }
    }

}