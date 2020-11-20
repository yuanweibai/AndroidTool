package rango.kotlin.bezier

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import rango.tool.common.utils.ScreenUtils
import java.lang.IllegalStateException
import kotlin.math.abs
import kotlin.math.sqrt

class BookPageView @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyle: Int = 0
) : View(context, attributeSet, defStyle) {

    companion object {
        const val TAG = "BookPageView"
        const val MAX_DURATION_RETURN_BACK = 500

        const val MOVE_FROM_INVALID = 0
        const val MOVE_FROM_RIGHT_BOTTOM = 1
        const val MOVE_FROM_RIGHT_TOP = 2
        const val MOVE_FROM_LEFT_BOTTOM = 3
        const val MOVE_FROM_LEFT_TOP = 4
        const val MOVE_FROM_LEFT_DIRECTLY = 5
        const val MOVE_FROM_TOP_DIRECTLY = 6
        const val MOVE_FROM_RIGHT_DIRECTLY = 7
        const val MOVE_FROM_BOTTOM_DIRECTLY = 8

    }

    private val paint = Paint()
    private val path = Path()
    private val fingerA = PointF(0f, 0f)
    private val screenCornerF = PointF(0f, 0f)
    private val assistG = PointF(0f, 0f)
    private val controlE = PointF(0f, 0f)
    private val pointC = PointF(0f, 0f)
    private val pointB = PointF(0f, 0f)
    private val controlH = PointF(0f, 0f)
    private val pointJ = PointF(0f, 0f)
    private val pointK = PointF(0f, 0f)
    private val pointD = PointF(0f, 0f)
    private val pointI = PointF(0f, 0f)

    private val width4 = ScreenUtils.dp2px(4f).toFloat()

    private var viewWidth = 0.toFloat()
    private var viewHeight = 0.toFloat()

    private var moveFrom = MOVE_FROM_RIGHT_BOTTOM

    private val colorArray = intArrayOf(Color.RED, Color.YELLOW)
    private var currentPageColorIndex = 0
    private var nextPageColorIndex = 1

    private var isFlippingOver = false

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        viewWidth = w.toFloat()
        viewHeight = h.toFloat()
    }

    private fun calculatePoint() {

        assistG.x = (fingerA.x + screenCornerF.x) / 2f
        assistG.y = (fingerA.y + screenCornerF.y) / 2f

        controlE.x = assistG.x - (screenCornerF.y - assistG.y) * (screenCornerF.y - assistG.y) / (screenCornerF.x - assistG.x)
        controlE.y = screenCornerF.y

        pointC.x = controlE.x - (screenCornerF.x - controlE.x) / 2f
        pointC.y = screenCornerF.y

        controlH.x = screenCornerF.x
        controlH.y = assistG.y - (screenCornerF.x - assistG.x) * (screenCornerF.x - assistG.x) / (screenCornerF.y - assistG.y)

        pointJ.x = screenCornerF.x
        pointJ.y = controlH.y - (screenCornerF.y - controlH.y) / 2f

        calculateIntersectionPoint(fingerA.x, fingerA.y,
                controlE.x, controlE.y,
                pointC.x, pointC.y,
                pointJ.x, pointJ.y, pointB)
        calculateIntersectionPoint(fingerA.x, fingerA.y,
                controlH.x, controlH.y,
                pointJ.x, pointJ.y,
                pointC.x, pointC.y, pointK)

        pointD.x = (pointC.x + 2 * controlE.x + pointB.x) / 4
        pointD.y = (2 * controlE.y + pointC.y + pointB.y) / 4

        pointI.x = (pointJ.x + 2 * controlH.x + pointK.x) / 4
        pointI.y = (2 * controlH.y + pointJ.y + pointK.y) / 4
    }

    private fun calculateIntersectionPoint(x1: Float, y1: Float,
                                           x2: Float, y2: Float,
                                           x3: Float, y3: Float,
                                           x4: Float, y4: Float,
                                           result: PointF
    ) {
        result.x = ((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1)) / ((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4))
        result.y = ((y1 - y2) * (x3 * y4 - x4 * y3) - (x1 * y2 - x2 * y1) * (y3 - y4)) / ((y1 - y2) * (x3 - x4) - (x1 - x2) * (y3 - y4))
    }

    override fun onDraw(canvas: Canvas?) {

        if (!isFlippingOver) {
            drawCurrentContent(canvas)
        } else {
            //  drawPoint(canvas)
            drawNextPage(canvas)
            drawPageBack(canvas)
            drawPageContent(canvas)
        }
    }

    private fun drawPoint(canvas: Canvas?) {
        canvas?.run {
            paint.strokeWidth = width4
            paint.color = Color.BLACK
            paint.strokeCap = Paint.Cap.ROUND
            drawPoint(fingerA.x, fingerA.y, paint)
            drawPoint(screenCornerF.x, screenCornerF.y, paint)
            drawPoint(assistG.x, assistG.y, paint)
            drawPoint(controlE.x, controlE.y, paint)
            drawPoint(pointC.x, pointC.y, paint)
            drawPoint(pointB.x, pointB.y, paint)
            drawPoint(controlH.x, controlH.y, paint)
            drawPoint(pointJ.x, pointJ.y, paint)
            drawPoint(pointK.x, pointK.y, paint)
            drawPoint(pointD.x, pointD.y, paint)
            drawPoint(pointI.x, pointI.y, paint)
        }
    }

    private fun drawCurrentContent(canvas: Canvas?) {
        canvas?.run {
            drawColor(colorArray[currentPageColorIndex])
        }
    }

    private fun drawPageContent(canvas: Canvas?) {
        canvas?.run {
            when (moveFrom) {
                MOVE_FROM_LEFT_TOP -> {
                    setContentPathFromLeftTop()
                }
                MOVE_FROM_LEFT_BOTTOM -> {
                    setContentPathFromLeftBottom()
                }
                MOVE_FROM_RIGHT_TOP -> {
                    setContentPathFromRightTop()
                }
                MOVE_FROM_RIGHT_BOTTOM -> {
                    setContentPathFromRightBottom()
                }
                MOVE_FROM_RIGHT_DIRECTLY -> {
                    setContentPathFromRight()
                }
                MOVE_FROM_LEFT_DIRECTLY -> {
                    setContentPathFromLeft()
                }
                MOVE_FROM_TOP_DIRECTLY -> {
                    setContentPathFromTop()
                }
                MOVE_FROM_BOTTOM_DIRECTLY -> {
                    setContentPathFromBottom()
                }
                else -> {
                    throw IllegalStateException("DrawPageContent error: moveFrom = $moveFrom is not know!!!")
                }
            }

            paint.style = Paint.Style.FILL
            paint.color = colorArray[currentPageColorIndex]
            drawPath(path, paint)
        }
    }

    private fun setContentPathFromRightBottom() {
        path.reset()
        path.lineTo(0f, viewHeight)
        path.lineTo(pointC.x, pointC.y)
        path.quadTo(controlE.x, controlE.y, pointB.x, pointB.y)
        path.lineTo(fingerA.x, fingerA.y)
        path.lineTo(pointK.x, pointK.y)
        path.quadTo(controlH.x, controlH.y, pointJ.x, pointJ.y)
        path.lineTo(viewWidth, 0f)
        path.close()
    }

    private fun setContentPathFromRightTop() {
        path.reset()
        path.moveTo(0f, 0f)
        path.lineTo(0f, viewHeight)
        path.lineTo(viewWidth, viewHeight)
        path.lineTo(pointJ.x, pointJ.y)
        path.quadTo(controlH.x, controlH.y, pointK.x, pointK.y)
        path.lineTo(fingerA.x, fingerA.y)
        path.lineTo(pointB.x, pointB.y)
        path.quadTo(controlE.x, controlE.y, pointC.x, pointC.y)
        path.close()
    }

    private fun setContentPathFromLeftTop() {
        path.reset()
        path.moveTo(0f, viewHeight)
        path.lineTo(viewWidth, viewHeight)
        path.lineTo(viewWidth, 0f)
        path.lineTo(pointC.x, pointC.y)
        path.quadTo(controlE.x, controlE.y, pointB.x, pointB.y)
        path.lineTo(fingerA.x, fingerA.y)
        path.lineTo(pointK.x, pointK.y)
        path.quadTo(controlH.x, controlH.y, pointJ.x, pointJ.y)
        path.close()
    }

    private fun setContentPathFromLeftBottom() {
        path.reset()
        path.moveTo(0f, 0f)
        path.lineTo(pointJ.x, pointJ.y)
        path.quadTo(controlH.x, controlH.y, pointK.x, pointK.y)
        path.lineTo(fingerA.x, fingerA.y)
        path.lineTo(pointB.x, pointB.y)
        path.quadTo(controlE.x, controlE.y, pointC.x, pointC.y)
        path.lineTo(viewWidth, viewHeight)
        path.lineTo(viewWidth, 0f)
        path.close()
    }

    private fun setContentPathFromLeft() {
        path.reset()
        path.moveTo(fingerA.x, 0f)
        path.lineTo(fingerA.x, viewHeight)
        path.lineTo(viewWidth, viewHeight)
        path.lineTo(viewHeight, 0f)
        path.close()
    }

    private fun setContentPathFromTop() {
        path.reset()
        path.moveTo(0f, fingerA.y)
        path.lineTo(viewWidth, fingerA.y)
        path.lineTo(viewWidth, viewHeight)
        path.lineTo(0f, viewHeight)
        path.close()
    }

    private fun setContentPathFromRight() {
        path.reset()
        path.moveTo(0f, 0f)
        path.lineTo(0f, viewHeight)
        path.lineTo(fingerA.x, viewHeight)
        path.lineTo(fingerA.x, 0f)
        path.close()
    }

    private fun setContentPathFromBottom() {
        path.reset()
        path.moveTo(0f, 0f)
        path.lineTo(0f, fingerA.y)
        path.lineTo(viewWidth, fingerA.y)
        path.lineTo(viewWidth, 0f)
        path.close()
    }

    private fun drawPageBack(canvas: Canvas?) {
        canvas?.run {
            when (moveFrom) {
                MOVE_FROM_LEFT_DIRECTLY -> {
                    setBackPathFromLeft()
                }
                MOVE_FROM_TOP_DIRECTLY -> {
                    setBackPathFromTop()
                }
                MOVE_FROM_RIGHT_DIRECTLY -> {
                    setBackPathFromRight()
                }
                MOVE_FROM_BOTTOM_DIRECTLY -> {
                    setBackPathFromBottom()
                }
                else -> {
                    setBackPathFromFourCorners()
                }
            }

            paint.style = Paint.Style.FILL
            paint.color = Color.BLUE
            drawPath(path, paint)
        }
    }

    private fun setBackPathFromFourCorners() {
        path.reset()
        path.moveTo(fingerA.x, fingerA.y)
        path.lineTo(pointB.x, pointB.y)
        path.lineTo(pointD.x, pointD.y)
        path.lineTo(pointI.x, pointI.y)
        path.lineTo(pointK.x, pointK.y)
        path.close()
    }

    private fun setBackPathFromLeft() {
        path.reset()
        path.moveTo(fingerA.x, 0f)
        path.lineTo(fingerA.x, viewHeight)
        path.lineTo(fingerA.x / 2f, viewHeight)
        path.lineTo(fingerA.x / 2f, 0f)
        path.close()
    }

    private fun setBackPathFromTop() {
        path.reset()
        path.moveTo(0f, fingerA.y)
        path.lineTo(viewWidth, fingerA.y)
        path.lineTo(viewWidth, fingerA.y / 2f)
        path.lineTo(0f, fingerA.y / 2f)
        path.close()
    }

    private fun setBackPathFromRight() {
        path.reset()
        path.moveTo(fingerA.x, 0f)
        path.lineTo(fingerA.x, viewHeight)
        path.lineTo((viewWidth + fingerA.x) / 2f, viewHeight)
        path.lineTo((viewWidth + fingerA.x) / 2f, 0f)
        path.close()
    }

    private fun setBackPathFromBottom() {
        path.reset()
        path.moveTo(0f, fingerA.y)
        path.lineTo(viewWidth, fingerA.y)
        path.lineTo(viewWidth, (fingerA.y + viewHeight) / 2f)
        path.lineTo(0f, (fingerA.y + viewHeight) / 2f)
        path.close()
    }

    private fun drawNextPage(canvas: Canvas?) {
        canvas?.run {
            path.reset()
            path.moveTo(0f, 0f)
            path.lineTo(0f, viewHeight)
            path.lineTo(viewWidth, viewHeight)
            path.lineTo(viewWidth, 0f)
            path.close()

            paint.style = Paint.Style.FILL
            paint.color = colorArray[nextPageColorIndex]
            drawPath(path, paint)
        }
    }

    private fun actionDown(x: Float, y: Float) {
        val from = getMoveFrom(x, y)
        if (from == MOVE_FROM_INVALID) {
            Log.d(TAG, "actionDown: moveFrom is invalid, x = $x,y = $y,viewWidth = $viewWidth,viewHeight = $viewHeight")
            return
        }

        cancelActionUpAnim()

        moveFrom = from
        isFlippingOver = true

        setFingerA(x, y)
        setScreenCornerF()

        if (!isMoveDirectly()) {
            calculatePoint()
        }
        invalidate()
    }

    private fun setFingerA(x: Float, y: Float) {
        if (!isMoveDirectly()) {
            fingerA.x = if (x <= 0) {
                1f
            } else {
                x
            }
            fingerA.y = y
        } else {
            when (moveFrom) {
                MOVE_FROM_LEFT_DIRECTLY, MOVE_FROM_RIGHT_DIRECTLY -> {
                    fingerA.x = x
                    fingerA.y = viewHeight / 2f
                }
                MOVE_FROM_TOP_DIRECTLY, MOVE_FROM_BOTTOM_DIRECTLY -> {
                    fingerA.x = viewWidth / 2f
                    fingerA.y = y
                }
            }
        }
    }

    private fun setScreenCornerF() {
        when (moveFrom) {
            MOVE_FROM_LEFT_TOP -> {
                screenCornerF.x = 0f
                screenCornerF.y = 0f
            }
            MOVE_FROM_LEFT_BOTTOM -> {
                screenCornerF.x = 0f
                screenCornerF.y = viewHeight
            }
            MOVE_FROM_RIGHT_TOP -> {
                screenCornerF.x = viewWidth
                screenCornerF.y = 0f
            }
            MOVE_FROM_RIGHT_BOTTOM -> {
                screenCornerF.x = viewWidth
                screenCornerF.y = viewHeight
            }
            MOVE_FROM_LEFT_DIRECTLY -> {
                screenCornerF.x = 0f
                screenCornerF.y = viewHeight / 2f
            }
            MOVE_FROM_TOP_DIRECTLY -> {
                screenCornerF.x = viewWidth / 2f
                screenCornerF.y = 0f
            }
            MOVE_FROM_RIGHT_DIRECTLY -> {
                screenCornerF.x = viewWidth
                screenCornerF.y = viewHeight / 2f
            }
            MOVE_FROM_BOTTOM_DIRECTLY -> {
                screenCornerF.x = viewWidth / 2f
                screenCornerF.y = viewHeight
            }
        }
    }

    private fun isMoveDirectly(): Boolean {
        return moveFrom == MOVE_FROM_RIGHT_DIRECTLY || moveFrom == MOVE_FROM_BOTTOM_DIRECTLY || moveFrom == MOVE_FROM_LEFT_DIRECTLY || moveFrom == MOVE_FROM_TOP_DIRECTLY
    }

    private fun actionMoving(x: Float, y: Float) {
        setFingerA(x, y)

        if (!isMoveDirectly()) {
            calculatePoint()
        }
        invalidate()
    }

    private fun actionUp() {
        if (!isMoveDirectly()) {
            startActionUpAnimFromFourCorners()
        } else {
            when (moveFrom) {
                MOVE_FROM_LEFT_DIRECTLY -> {
                    startActionUpAnimFromDirectly(fingerA.x, 0f, ValueAnimator.AnimatorUpdateListener {
                        fingerA.x = it.animatedValue as Float
                        invalidate()
                    })
                }
                MOVE_FROM_TOP_DIRECTLY -> {
                    startActionUpAnimFromDirectly(fingerA.y, 0f, ValueAnimator.AnimatorUpdateListener {
                        fingerA.y = it.animatedValue as Float
                        invalidate()
                    })
                }
                MOVE_FROM_RIGHT_DIRECTLY -> {
                    startActionUpAnimFromDirectly(fingerA.x, viewWidth, ValueAnimator.AnimatorUpdateListener {
                        fingerA.x = it.animatedValue as Float
                        invalidate()
                    })
                }
                MOVE_FROM_BOTTOM_DIRECTLY -> {
                    startActionUpAnimFromDirectly(fingerA.y, viewHeight, ValueAnimator.AnimatorUpdateListener {
                        fingerA.y = it.animatedValue as Float
                        invalidate()
                    })
                }
            }
        }
    }

    private var k = 1f
    private var b = 0f
    private var actionUpAnimator: ValueAnimator? = null

    private fun startActionUpAnimFromFourCorners() {
        k = (fingerA.y - screenCornerF.y) / (fingerA.x - screenCornerF.x)
        b = fingerA.y - k * fingerA.x

        cancelActionUpAnim()
        actionUpAnimator = ValueAnimator.ofFloat(fingerA.x, screenCornerF.x)
        actionUpAnimator?.run {

            addUpdateListener {
                val x = it.animatedValue as Float
                fingerA.x = if (x <= 0) {
                    1f
                } else {
                    x
                }
                fingerA.y = k * x + b
                calculatePoint()
                invalidate()
            }

            duration = getReturnBackDuration()

            interpolator = LinearInterpolator()

            addListener(object : AnimatorListenerAdapter() {
                var isCanceled = false
                override fun onAnimationEnd(animation: Animator?) {
                    if (isCanceled) {
                        return
                    }
                    isFlippingOver = false
                    invalidate()
                }

                override fun onAnimationCancel(animation: Animator?) {
                    isCanceled = true
                }
            })
            start()
        }
    }

    private fun getReturnBackDuration(): Long {
        val distance = sqrt(((fingerA.x - screenCornerF.x) * (fingerA.x - screenCornerF.x) + (fingerA.y - screenCornerF.y) * (fingerA.y - screenCornerF.y)).toDouble())
        return (distance / ScreenUtils.getScreenWidthPx() * MAX_DURATION_RETURN_BACK).toLong()
    }

    private fun startActionUpAnimFromDirectly(start: Float, end: Float, updateListener: ValueAnimator.AnimatorUpdateListener) {
        cancelActionUpAnim()
        actionUpAnimator = ValueAnimator.ofFloat(start, end)
        actionUpAnimator?.run {
            addUpdateListener(updateListener)
            duration = getReturnBackDuration()
            interpolator = LinearInterpolator()
            start()
        }
    }

    private fun cancelActionUpAnim() {
        actionUpAnimator?.cancel()
        actionUpAnimator = null
    }

    private fun getMoveFrom(x: Float, y: Float): Int {
        val halfWidth = viewWidth / 2f
        val halfHeight = viewHeight / 2f
        val quarterWidth = viewWidth / 4f
        val quarterHeight = viewHeight / 4f
        return if (x < quarterWidth && y < quarterHeight) {
            MOVE_FROM_LEFT_TOP
        } else if (x < quarterWidth && y < (halfHeight + quarterHeight)) {
            MOVE_FROM_LEFT_DIRECTLY
        } else if (x < quarterWidth && y <= viewHeight) {
            MOVE_FROM_LEFT_BOTTOM
        } else if (x < (halfWidth + quarterWidth) && y < quarterHeight) {
            MOVE_FROM_TOP_DIRECTLY
        } else if (x < (halfWidth + quarterWidth) && y < (halfHeight + quarterHeight)) {
            MOVE_FROM_INVALID
        } else if (x < (halfWidth + quarterWidth) && y <= viewHeight) {
            MOVE_FROM_BOTTOM_DIRECTLY
        } else if (x <= viewWidth && y < quarterHeight) {
            MOVE_FROM_RIGHT_TOP
        } else if (x <= viewWidth && y < (halfHeight + quarterHeight)) {
            MOVE_FROM_RIGHT_DIRECTLY
        } else if (x < viewWidth && y <= viewHeight) {
            MOVE_FROM_RIGHT_BOTTOM
        } else {
            throw IllegalStateException("getMoveFrom error: x = $x, y = $y,viewHeight = $viewHeight,viewWidth = $viewWidth")
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.run {
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    actionDown(x, y)
                }
                MotionEvent.ACTION_MOVE -> {
                    actionMoving(x, y)
                }
                else -> {
                    actionUp()
                }
            }
        }
        return true
    }
}