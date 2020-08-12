package rango.kotlin.views

import android.content.Context
import android.graphics.*
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.util.Log
import android.view.View
import rango.tool.androidtool.R
import rango.tool.common.utils.ScreenUtils

class BladeFlashImageView(context: Context, attributeSet: AttributeSet?, defStyle: Int) : AppCompatImageView(context, attributeSet, defStyle) {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    private val flashBitmap: Bitmap
    private val flashPaint = Paint()
    private var flashTranslationProgress = 0.toFloat()
    private var totalTranslationX = 0
    private val flashRect = Rect()
    private val flashWidth = ScreenUtils.dp2px(73f)

    init {

        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.BladeFlashImageView)
        val flashBitmapResId = ta.getResourceId(R.styleable.BladeFlashImageView_flash_bitmap, 0)
        ta.recycle()

        setLayerType(View.LAYER_TYPE_HARDWARE, null)

        flashBitmap = if (flashBitmapResId > 0) {
            BitmapFactory.decodeResource(resources, flashBitmapResId)
        } else {
            BitmapFactory.decodeResource(resources, R.drawable.cash_center_flash_light)
        }

        flashPaint.isFilterBitmap = true
        flashPaint.color = Color.parseColor("#FCE91E63")
//        flashPaint.alpha = 240
        flashPaint.isAntiAlias = true
        flashPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)

        Log.e("rango", "isHardwareAccelerated = $isHardwareAccelerated")
    }


    fun setFlashTranslationProgress(progress: Float) {
        flashTranslationProgress = progress
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        totalTranslationX = w + flashWidth
        flashRect.top = 0
        flashRect.bottom = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        Log.e("rango", "Canvas.isHardwareAccelerated = ${canvas?.isHardwareAccelerated}")

        if (flashTranslationProgress > 0f) {
            drawFlash(canvas)
        }
    }

    private fun drawFlash(canvas: Canvas?) {
        flashRect.right = (flashTranslationProgress * totalTranslationX).toInt()
        flashRect.left = flashRect.right - flashWidth
        canvas?.drawBitmap(flashBitmap, null, flashRect, flashPaint)
    }
}