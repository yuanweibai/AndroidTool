package rango.kotlin.views.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import rango.tool.androidtool.R


/**
 * 在图片的固定位置，绘制点，且保证不偏移，精准无误
 */
class ImagePointLocateView @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyle: Int = 0
) : View(context, attributeSet, defStyle) {

    private val bitmap: Bitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.lottery_start_normal_icon
    )
    private val paint = Paint()
    private val wRatio = 158 / bitmap.width.toFloat()
    private val hRatio = 63 / bitmap.height.toFloat()
    private var imageMatrix = Matrix()
    private var rect = Rect()

    init {
        paint.color = Color.RED
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = 5f


    }


    override fun onDraw(canvas: Canvas?) {
        drawFirst(canvas)

        drawSecond(canvas)
    }

    private fun drawFirst(canvas: Canvas?) {
        canvas?.run {

//            imageMatrix.reset()
//            imageMatrix.setScale(2f, 2f)
            rect.left = 0
            rect.top = 0
            rect.bottom = bitmap.height
            rect.right = bitmap.width

            drawBitmap(bitmap, null, rect, paint)
            drawPoint(rect.width() * wRatio, rect.height() * hRatio, paint)
        }
    }

    private fun drawSecond(canvas: Canvas?) {
        canvas?.run {
            rect.left = 0
            rect.top = rect.bottom + 20
            rect.bottom = rect.top + bitmap.height * 4
            rect.right = bitmap.width * 4

            drawBitmap(bitmap, null, rect, paint)
            drawPoint(rect.left + rect.width() * wRatio, rect.top + rect.height() * hRatio, paint)
        }
    }
}