package rango.kotlin.bezier

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.LinearInterpolator
import kotlinx.android.synthetic.main.activity_book_page.view.*
import rango.tool.androidtool.R
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

        const val MOVE_DIRECTION_INVALID = 0
        const val MOVE_DIRECTION_RIGHT_BOTTOM = 1
        const val MOVE_DIRECTION_RIGHT_TOP = 2
        const val MOVE_DIRECTION_LEFT_BOTTOM = 3
        const val MOVE_DIRECTION_LEFT_TOP = 4
        const val MOVE_DIRECTION_LEFT_DIRECTLY = 5
        const val MOVE_DIRECTION_RIGHT_DIRECTLY = 6

        const val BOOK_DIRECTION_LEFT = 1
        const val BOOK_DIRECTION_RIGHT = 2
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

    private var viewWidth = 0.toFloat()
    private var viewHeight = 0.toFloat()

    private var moveFromDirection = MOVE_DIRECTION_INVALID

    private val colorArray = intArrayOf(Color.RED, Color.YELLOW)
    private var currentPageColorIndex = 0
    private var nextPageColorIndex = 1

    private var isFlippingOver = false

    private val touchSlop: Int

    private var clickListener: OnClickListener? = null

    private var invalidMoveFromDirectionList: MutableList<Int> = ArrayList()

    private var isMoveToNext = false
    private val bookDirection: Int

    init {
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.BookPageView)
        bookDirection = attrs.getInt(R.styleable.BookPageView_book_direction, BOOK_DIRECTION_RIGHT)
        attrs.recycle()
        initInvalidMoveFromDirectionList()

        setLayerType(LAYER_TYPE_SOFTWARE, null)
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    private fun initInvalidMoveFromDirectionList() {
        invalidMoveFromDirectionList.add(MOVE_DIRECTION_INVALID)
        when (bookDirection) {
            BOOK_DIRECTION_RIGHT -> {
                invalidMoveFromDirectionList.add(MOVE_DIRECTION_LEFT_BOTTOM)
                invalidMoveFromDirectionList.add(MOVE_DIRECTION_LEFT_TOP)
                invalidMoveFromDirectionList.add(MOVE_DIRECTION_LEFT_DIRECTLY)
            }
            BOOK_DIRECTION_LEFT -> {
                invalidMoveFromDirectionList.add(MOVE_DIRECTION_RIGHT_TOP)
                invalidMoveFromDirectionList.add(MOVE_DIRECTION_RIGHT_BOTTOM)
                invalidMoveFromDirectionList.add(MOVE_DIRECTION_RIGHT_DIRECTLY)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        viewWidth = w.toFloat()
        viewHeight = h.toFloat()
    }

    private fun calculateBasePoint() {
        assistG.x = (fingerA.x + screenCornerF.x) / 2f
        assistG.y = (fingerA.y + screenCornerF.y) / 2f

        controlE.x = assistG.x - (screenCornerF.y - assistG.y) * (screenCornerF.y - assistG.y) / (screenCornerF.x - assistG.x)
        controlE.y = screenCornerF.y

        controlH.x = screenCornerF.x
        val tempA = screenCornerF.y - assistG.y
        if (tempA == 0f) {
            // 0.1f 根据效果而调整
            controlH.y = assistG.y - (screenCornerF.x - assistG.x) * (screenCornerF.x - assistG.x) / 0.1f
        } else {
            controlH.y = assistG.y - (screenCornerF.x - assistG.x) * (screenCornerF.x - assistG.x) / (screenCornerF.y - assistG.y)
        }

        pointC.x = controlE.x - (screenCornerF.x - controlE.x) / 2f
    }

    private fun calculatePoint() {

        calculateBasePoint()

        if (!isActionUpAnimRunning() && (pointC.x < 0 || pointC.x > viewWidth)) {
            if (pointC.x < 0) {
                pointC.x = viewWidth - pointC.x
            }

            val tempB = abs(screenCornerF.x - fingerA.x)
            val tempC = viewWidth * tempB / pointC.x
            fingerA.x = abs(screenCornerF.x - tempC)

            val tempD = abs(screenCornerF.x - fingerA.x) * abs(screenCornerF.y - fingerA.y) / tempB
            fingerA.y = abs(screenCornerF.y - tempD)

            calculateBasePoint()
        }

        pointC.y = screenCornerF.y

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
            drawNextPage(canvas)
            drawPageBack(canvas)
            drawPageContent(canvas)
        }
    }


    private fun drawCurrentContent(canvas: Canvas?) {
        canvas?.run {
            drawColor(colorArray[currentPageColorIndex])
        }
    }

    private fun drawPageContent(canvas: Canvas?) {
        canvas?.run {
            when (moveFromDirection) {
                MOVE_DIRECTION_LEFT_TOP -> {
                    setContentPathFromLeftTop()
                }
                MOVE_DIRECTION_LEFT_BOTTOM -> {
                    setContentPathFromLeftBottom()
                }
                MOVE_DIRECTION_RIGHT_TOP -> {
                    setContentPathFromRightTop()
                }
                MOVE_DIRECTION_RIGHT_BOTTOM -> {
                    setContentPathFromRightBottom()
                }
                MOVE_DIRECTION_RIGHT_DIRECTLY -> {
                    setContentPathFromRight()
                }
                MOVE_DIRECTION_LEFT_DIRECTLY -> {
                    setContentPathFromLeft()
                }
                else -> {
                    throw IllegalStateException("DrawPageContent error: moveFrom = $moveFromDirection is not know!!!")
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


    private fun setContentPathFromRight() {
        path.reset()
        path.moveTo(0f, 0f)
        path.lineTo(0f, viewHeight)
        path.lineTo(fingerA.x, viewHeight)
        path.lineTo(fingerA.x, 0f)
        path.close()
    }

    private fun drawPageBack(canvas: Canvas?) {
        canvas?.run {
            when (moveFromDirection) {
                MOVE_DIRECTION_LEFT_DIRECTLY -> {
                    setBackPathFromLeft()
                }
                MOVE_DIRECTION_RIGHT_DIRECTLY -> {
                    setBackPathFromRight()
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

    private fun setBackPathFromRight() {
        path.reset()
        path.moveTo(fingerA.x, 0f)
        path.lineTo(fingerA.x, viewHeight)
        path.lineTo((viewWidth + fingerA.x) / 2f, viewHeight)
        path.lineTo((viewWidth + fingerA.x) / 2f, 0f)
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

    private var downX = 0f
    private var downY = 0f
    private var isDownAgain = false

    private fun actionDown(x: Float, y: Float) {
        isDownAgain = true
        downX = x
        downY = y
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
            fingerA.x = x
            fingerA.y = viewHeight / 2f
        }
    }

    private fun setScreenCornerF() {
        when (moveFromDirection) {
            MOVE_DIRECTION_LEFT_TOP -> {
                screenCornerF.x = 0f
                screenCornerF.y = 0f
            }
            MOVE_DIRECTION_LEFT_BOTTOM -> {
                screenCornerF.x = 0f
                screenCornerF.y = viewHeight
            }
            MOVE_DIRECTION_RIGHT_TOP -> {
                screenCornerF.x = viewWidth
                screenCornerF.y = 0f
            }
            MOVE_DIRECTION_RIGHT_BOTTOM -> {
                screenCornerF.x = viewWidth
                screenCornerF.y = viewHeight
            }
            MOVE_DIRECTION_LEFT_DIRECTLY -> {
                screenCornerF.x = 0f
                screenCornerF.y = viewHeight / 2f
            }
            MOVE_DIRECTION_RIGHT_DIRECTLY -> {
                screenCornerF.x = viewWidth
                screenCornerF.y = viewHeight / 2f
            }
        }
    }

    private fun isMoveDirectly(): Boolean {
        return moveFromDirection == MOVE_DIRECTION_RIGHT_DIRECTLY || moveFromDirection == MOVE_DIRECTION_LEFT_DIRECTLY
    }

    private fun isMoveDirectionInvalid(moveDirection: Int): Boolean {
        return invalidMoveFromDirectionList.contains(moveDirection)
    }

    private fun actionMoving(x: Float, y: Float) {
        if (isDownAgain) {
            val direction = getMoveFrom(x, y)
            if (isMoveDirectionInvalid(direction)) {
                return
            }
            if (isMoveToNext) {
                endActionUpAnim()
            } else {
                cancelActionUpAnim()
            }

            moveFromDirection = direction
            isFlippingOver = true

            setScreenCornerF()
            isDownAgain = false
        }

        setFingerA(x, y)

        if (!isMoveDirectly()) {
            calculatePoint()
        }
        invalidate()
    }

    private fun showNext() {
        val temp = currentPageColorIndex
        currentPageColorIndex = nextPageColorIndex
        nextPageColorIndex = temp
    }

    private fun actionUp() {
        if (!isFlippingOver) {
            clickListener?.onClick(this)
            return
        }

        isMoveToNext = isMoveToNext()
        if (!isMoveDirectly()) {
            startActionUpAnimFromFourCorners()
            return
        }

        when (moveFromDirection) {
            MOVE_DIRECTION_LEFT_DIRECTLY -> {
                val endX = if (isMoveToNext) {
                    viewWidth * 2
                } else {
                    0f
                }
                startActionUpAnimFromDirectly(fingerA.x, endX, ValueAnimator.AnimatorUpdateListener {
                    fingerA.x = it.animatedValue as Float
                    invalidate()
                })
            }
            MOVE_DIRECTION_RIGHT_DIRECTLY -> {
                val endX = if (isMoveToNext) {
                    -viewWidth
                } else {
                    viewWidth
                }
                startActionUpAnimFromDirectly(fingerA.x, endX, ValueAnimator.AnimatorUpdateListener {
                    fingerA.x = it.animatedValue as Float
                    invalidate()
                })
            }
        }
    }

    private fun isMoveToNext(): Boolean {
        val couldFlipOverDistance = viewWidth / 3f
        return abs(fingerA.x - screenCornerF.x) > couldFlipOverDistance || abs(fingerA.y - screenCornerF.y) > couldFlipOverDistance
    }

    private var k = 1f
    private var b = 0f
    private var actionUpAnimator: ValueAnimator? = null

    private fun isActionUpAnimRunning(): Boolean {
        return actionUpAnimator?.isRunning ?: false
    }

    private fun startActionUpAnimFromFourCorners() {

        var endX = screenCornerF.x
        var endY = screenCornerF.y

        if (isMoveToNext) {
            when (moveFromDirection) {
                MOVE_DIRECTION_LEFT_TOP -> {
                    endX = viewWidth * 2f
                    endY = 0f
                }
                MOVE_DIRECTION_LEFT_BOTTOM -> {
                    endX = viewWidth * 2f
                    endY = viewHeight

                }
                MOVE_DIRECTION_RIGHT_BOTTOM -> {
                    endX = -viewWidth
                    endY = viewHeight
                }
                MOVE_DIRECTION_RIGHT_TOP -> {
                    endX = -viewWidth
                    endY = 0f
                }
            }
        }

        k = (fingerA.y - endY) / (fingerA.x - endX)
        b = fingerA.y - k * fingerA.x

        cancelActionUpAnim()
        actionUpAnimator = ValueAnimator.ofFloat(fingerA.x, endX)
        actionUpAnimator?.run {

            addUpdateListener {
                fingerA.x = it.animatedValue as Float
                fingerA.y = k * fingerA.x + b
                calculatePoint()
                invalidate()
            }

            val distance = sqrt(((fingerA.x - endX) * (fingerA.x - endX) + (fingerA.y - endY) * (fingerA.y - endY)))
            duration = getReturnBackDuration(distance)

            interpolator = LinearInterpolator()

            addListener(object : AnimatorListenerAdapter() {
                var isCanceled = false
                override fun onAnimationEnd(animation: Animator?) {
                    if (isCanceled) {
                        return
                    }
                    isFlippingOver = false
                    if (isMoveToNext) {
                        showNext()
                    }
                    invalidate()
                }

                override fun onAnimationCancel(animation: Animator?) {
                    isCanceled = true
                }
            })
            start()
        }
    }

    private fun getReturnBackDuration(distance: Float): Long {
        return (distance / ScreenUtils.getScreenWidthPx() * MAX_DURATION_RETURN_BACK).toLong()
    }

    private fun startActionUpAnimFromDirectly(start: Float, end: Float, updateListener: ValueAnimator.AnimatorUpdateListener) {
        cancelActionUpAnim()
        actionUpAnimator = ValueAnimator.ofFloat(start, end)
        actionUpAnimator?.run {
            addUpdateListener(updateListener)
            duration = getReturnBackDuration(abs(start - end))
            interpolator = LinearInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                private var isCanceled = false
                override fun onAnimationCancel(animation: Animator?) {
                    isCanceled = true
                }

                override fun onAnimationEnd(animation: Animator?) {
                    if (isCanceled) {
                        return
                    }
                    isFlippingOver = false
                    if (isMoveToNext) {
                        showNext()
                    }
                    invalidate()
                }
            })
            start()
        }
    }

    private fun cancelActionUpAnim() {
        actionUpAnimator?.cancel()
        actionUpAnimator = null
    }

    private fun endActionUpAnim() {
        actionUpAnimator?.run {
            if (isRunning) {
                end()
            }
        }
        actionUpAnimator = null
    }

    private fun getMoveFrom(x: Float, y: Float): Int {

        val vectorX = x - downX
        val vectorY = y - downY

        val absVx = abs(vectorX)
        val absVy = abs(vectorY)

        val dirTilt = 0
        val dirHorizontal = 1
        val dirVertical = 2

        val direction = when {
            absVx > 2 * absVy && absVy < touchSlop -> {
                dirHorizontal
            }
            absVy > 2 * absVx && absVx < touchSlop -> {
                dirVertical
            }
            else -> {
                dirTilt
            }
        }
        return when (direction) {
            dirHorizontal -> {
                if (absVx < touchSlop) {
                    MOVE_DIRECTION_INVALID
                } else {
                    if (vectorX > 0) {
                        MOVE_DIRECTION_LEFT_DIRECTLY
                    } else {
                        MOVE_DIRECTION_RIGHT_DIRECTLY
                    }
                }
            }
            dirVertical -> {
                MOVE_DIRECTION_INVALID
            }
            else -> {
                if (absVx < touchSlop || absVy < touchSlop) {
                    MOVE_DIRECTION_INVALID
                } else {
                    if (vectorX > 0 && vectorY < 0) {
                        MOVE_DIRECTION_LEFT_BOTTOM
                    } else if (vectorX > 0 && vectorY > 0) {
                        MOVE_DIRECTION_LEFT_TOP
                    } else if (vectorX < 0 && vectorY < 0) {
                        MOVE_DIRECTION_RIGHT_BOTTOM
                    } else if (vectorX < 0 && vectorY > 0) {
                        MOVE_DIRECTION_RIGHT_TOP
                    } else {
                        MOVE_DIRECTION_INVALID
                    }
                }
            }
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

    override fun setOnClickListener(l: OnClickListener?) {
        clickListener = l
    }
}