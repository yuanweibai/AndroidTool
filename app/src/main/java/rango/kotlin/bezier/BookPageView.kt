package rango.kotlin.bezier

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import rango.tool.common.utils.ScreenUtils

class BookPageView @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyle: Int = 0
) : View(context, attributeSet, defStyle) {

    companion object {
        const val MOVE_FROM_RIGHT_BOTTOM = 1
        const val MOVE_FROM_RIGHT_TOP = 2
        const val MOVE_FROM_LEFT_BOTTOM = 3
        const val MOVE_FROM_LEFT_TOP = 4
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
        path.moveTo(viewWidth, viewHeight)
        path.lineTo(viewWidth, 0f)
        path.lineTo(0f, 0f)
        path.lineTo(pointJ.x, pointJ.y)
        path.quadTo(controlH.x, controlH.y, pointK.x, pointK.y)
        path.lineTo(fingerA.x, fingerA.y)
        path.lineTo(pointB.x, pointB.y)
        path.quadTo(controlE.x, controlE.y, pointC.x, pointC.y)
        path.close()
    }

    private fun drawPageBack(canvas: Canvas?) {
        canvas?.run {

            path.reset()
            path.moveTo(fingerA.x, fingerA.y)
            path.lineTo(pointB.x, pointB.y)
            path.lineTo(pointD.x, pointD.y)
            path.lineTo(pointI.x, pointI.y)
            path.lineTo(pointK.x, pointK.y)
            path.close()

            paint.style = Paint.Style.FILL
            paint.color = Color.BLUE
            drawPath(path, paint)
        }
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
        isFlippingOver = true

        moveFrom = getMoveFrom(x, y)

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
        }
        fingerA.x = x
        fingerA.y = y
        calculatePoint()
        invalidate()
    }

    private fun actionMoving(x: Float, y: Float) {
        fingerA.x = x
        fingerA.y = y
        calculatePoint()
        invalidate()
    }

    private fun actionUp() {
        isFlippingOver = false
        invalidate()
    }

    private fun getMoveFrom(x: Float, y: Float): Int {
        val halfWidth = viewWidth / 2f
        val halfHeight = viewHeight / 2f
        return if (x < halfWidth && y < halfHeight) {
            MOVE_FROM_LEFT_TOP
        } else if (x < halfWidth && y >= halfHeight) {
            MOVE_FROM_LEFT_BOTTOM
        } else if (x >= halfWidth && y < halfHeight) {
            MOVE_FROM_RIGHT_TOP
        } else {
            MOVE_FROM_RIGHT_BOTTOM
        }
    }

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