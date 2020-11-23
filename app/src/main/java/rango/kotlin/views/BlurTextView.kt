package rango.kotlin.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import rango.tool.androidtool.R

class BlurTextView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyle: Int = 0) : AppCompatTextView(context, attributeSet, defStyle) {

    companion object {
        const val DEFAULT_VALUE = -1111984
    }

    private var gradient: LinearGradient? = null
    private var blurStartColor: Int
    private var blurEndColor: Int
    private var blurLineCount: Int

    init {
        val styleAttr = context.obtainStyledAttributes(attributeSet, R.styleable.BlurTextView)
        blurStartColor = styleAttr.getColor(R.styleable.BlurTextView_blur_start_color, DEFAULT_VALUE)
        blurEndColor = styleAttr.getColor(R.styleable.BlurTextView_blur_end_color, DEFAULT_VALUE)
        blurLineCount = styleAttr.getInt(R.styleable.BlurTextView_blur_line_count, DEFAULT_VALUE)
        styleAttr.recycle()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (shouldDrawBlur() && gradient == null) {
            val height = (bottom - top).toFloat()
            val blurHeight = paint.fontSpacing * blurLineCount
            gradient = LinearGradient(0f, height - blurHeight, 0f, height, blurStartColor, blurEndColor, Shader.TileMode.CLAMP)
            paint.shader = gradient
        }
    }

    private fun shouldDrawBlur(): Boolean {
        return blurEndColor != DEFAULT_VALUE && blurStartColor != DEFAULT_VALUE && lineCount > 0
    }
}