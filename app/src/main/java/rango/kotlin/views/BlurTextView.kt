package rango.kotlin.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import rango.tool.androidtool.R

class BlurTextView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyle: Int = 0) : AppCompatTextView(context, attributeSet, defStyle) {

    companion object {
        const val DEFAULT_VALUE = -1111984
    }

    private var blurPaint = Paint()

    private var gradient: LinearGradient? = null
    private var blurStartColor: Int
    private var blurEndColor: Int
    private var blurHeight: Int

    init {
        val styleAttr = context.obtainStyledAttributes(attributeSet, R.styleable.BlurTextView)
        blurStartColor = styleAttr.getColor(R.styleable.BlurTextView_blur_start_color, DEFAULT_VALUE)
        blurEndColor = styleAttr.getColor(R.styleable.BlurTextView_blur_end_color, DEFAULT_VALUE)
        blurHeight = styleAttr.getDimensionPixelSize(R.styleable.BlurTextView_blur_height, DEFAULT_VALUE)
        styleAttr.recycle()
    }

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)

        if (shouldDrawBlur()) {
            if (gradient == null) {
                gradient = LinearGradient(0f, blurHeight.toFloat(), 0f, height.toFloat(), blurStartColor, blurEndColor, Shader.TileMode.CLAMP)
                blurPaint.shader = gradient
            }
            canvas?.drawRect(0f, blurHeight.toFloat(), width.toFloat(), height.toFloat(), blurPaint)
        }
    }

    private fun shouldDrawBlur(): Boolean {
        return blurEndColor != DEFAULT_VALUE && blurStartColor != DEFAULT_VALUE && blurHeight != DEFAULT_VALUE
    }
}