package rango.kotlin.mytest

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout

class MyLinearLayout(context: Context, attributeSet: AttributeSet?, defStyle: Int) : LinearLayout(context, attributeSet, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    companion object {
        const val TAG = "MyLinearLayout";
    }

    init {
        setWillNotDraw(false)
    }

    override fun draw(canvas: Canvas?) {
        Log.e(TAG, "draw: before........")
        super.draw(canvas)
        Log.e(TAG, "draw: after........")
    }

    override fun onDraw(canvas: Canvas?) {
        Log.e(TAG, "onDraw: before........")
        super.onDraw(canvas)
        Log.e(TAG, "onDraw: after........")
    }

    override fun dispatchDraw(canvas: Canvas?) {
        Log.e(TAG, "dispatchDraw: before........")
        super.dispatchDraw(canvas)
        Log.e(TAG, "dispatchDraw: after........")
    }

    override fun onDrawForeground(canvas: Canvas?) {
        Log.e(TAG, "onDrawForeground: before........")
        super.onDrawForeground(canvas)
        Log.e(TAG, "onDrawForeground: after........")
    }



}