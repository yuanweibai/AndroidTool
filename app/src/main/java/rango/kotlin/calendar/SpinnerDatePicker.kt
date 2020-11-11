package rango.kotlin.calendar

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.NumberPicker
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import rango.tool.androidtool.R
import rango.tool.common.utils.ScreenUtils
import java.lang.IllegalStateException
import java.lang.reflect.Field

class SpinnerDatePicker @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyle: Int = 0) : DatePicker(context, attributeSet, defStyle) {

    companion object {
        const val TAG = "SpinnerDatePicker"
        const val DEFAULT_LINE_WIDTH = 0
        val DEFAULT_LINE_HEIGHT = ScreenUtils.dp2px(1f)
        const val DEFAULT_PICKER_HEIGHT = ViewGroup.LayoutParams.WRAP_CONTENT
        const val DEFAULT_PICKER_ITEM_WIDTH = ViewGroup.LayoutParams.WRAP_CONTENT
        const val DEFAULT_PICKER_ITEM_INTERVAL = -1
        const val DEFAULT_ORIGINAL_DIVIDER_COLOR = -1

        const val ORIGINAL_DIVIDER_COLOR_FIELD = "mSelectionDivider"
    }

    private var lineDrawable: Drawable? = null
    private var lineWidth = DEFAULT_LINE_WIDTH
    private var lineHeight = DEFAULT_LINE_HEIGHT
    private var pickerHeight: Int
    private var pickerItemWidth: Int
    private var pickerItemInterval: Int

    @ColorInt
    private var originalDividerColor: Int


    init {
        calendarViewShown = false

        val styleAttr = context.obtainStyledAttributes(attributeSet, R.styleable.SpinnerDatePicker)
        lineDrawable = styleAttr.getDrawable(R.styleable.SpinnerDatePicker_line_background)
        lineWidth = styleAttr.getDimensionPixelSize(R.styleable.SpinnerDatePicker_line_width, DEFAULT_LINE_WIDTH)
        lineHeight = styleAttr.getDimensionPixelSize(R.styleable.SpinnerDatePicker_line_height, DEFAULT_LINE_HEIGHT)
        pickerHeight = styleAttr.getDimensionPixelSize(R.styleable.SpinnerDatePicker_picker_height, DEFAULT_PICKER_HEIGHT)
        pickerItemWidth = styleAttr.getDimensionPixelSize(R.styleable.SpinnerDatePicker_picker_item_width, DEFAULT_PICKER_ITEM_WIDTH)
        pickerItemInterval = styleAttr.getDimensionPixelSize(R.styleable.SpinnerDatePicker_picker_item_interval, DEFAULT_PICKER_ITEM_INTERVAL)
        originalDividerColor = styleAttr.getColor(R.styleable.SpinnerDatePicker_original_divider_color, DEFAULT_ORIGINAL_DIVIDER_COLOR)
        styleAttr.recycle()

        if (shouldResizePicker()) {
            resizePicker()
        }

        if (shouldResetDividerColor()) {
            setOriginalDividerDrawable(ColorDrawable(originalDividerColor))
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (lineDrawable != null) {
            showNewDivider()
        }
    }

    private fun showNewDivider() {
        if (parent !is ConstraintLayout) {
            throw IllegalStateException("The parent of spinnerDatePicker must be a ConstraintLayout!!! $parent")
        }

        if (!hideOriginalDivider()) {
            Log.e(TAG, "Hide original date picker divider failure!!! So show original divider.")
            return
        }

        val container = parent as ConstraintLayout
        val datePickerId = id
        val topLineView = getLineView()
        val topParams = ConstraintLayout.LayoutParams(lineWidth, lineHeight)
        topParams.topToTop = datePickerId
        topParams.leftToLeft = datePickerId
        topParams.rightToRight = datePickerId
        topParams.bottomToBottom = datePickerId
        topParams.verticalBias = 0.3333f
        container.addView(topLineView, topParams)

        val bottomLineView = getLineView()
        val bottomParams = ConstraintLayout.LayoutParams(lineWidth, lineHeight)
        bottomParams.topToTop = datePickerId
        bottomParams.leftToLeft = datePickerId
        bottomParams.rightToRight = datePickerId
        bottomParams.bottomToBottom = datePickerId
        bottomParams.verticalBias = 0.6666f
        container.addView(bottomLineView, bottomParams)
    }

    private fun getLineView(): View {
        val lineView = View(context)
        lineView.background = lineDrawable
        return lineView
    }

    private fun hideOriginalDivider(): Boolean {
        if (setOriginalDividerDrawable(null)) {
            invalidate()
            return true
        }
        return false
    }

    private fun setOriginalDividerDrawable(drawable: Any?): Boolean {
        val dateContainer: LinearLayout = getChildAt(0) as LinearLayout
        val spinnerContainer: LinearLayout = dateContainer.getChildAt(0) as LinearLayout
        var result = 0
        for (i in 0 until spinnerContainer.childCount) {
            val picker: NumberPicker = spinnerContainer.getChildAt(i) as NumberPicker
            val fieldList: Array<Field> = NumberPicker::class.java.declaredFields
            for (field in fieldList) {
                if (field.name == ORIGINAL_DIVIDER_COLOR_FIELD) {
                    field.isAccessible = true
                    try {
                        field.set(picker, drawable)
                        picker.invalidate()
                        result++
                    } catch (e: IllegalArgumentException) {
                    } catch (e: Resources.NotFoundException) {
                    } catch (e: IllegalAccessException) {
                    }
                    break
                }
            }
        }
        return result == spinnerContainer.childCount
    }


    private fun resizePicker() {
        val result: List<NumberPicker?> = findNumberPicker(this)
        if (result.size != 3) {
            Log.e(TAG, "The count of NumberPick is not 3, that is illegal!!!")
            return
        }
        for (i in result.indices) {
            resizeNumberPicker(i, result[i])
        }
    }


    private fun findNumberPicker(viewGroup: ViewGroup?): List<NumberPicker?> {
        val numberPickerList: MutableList<NumberPicker?> = ArrayList()
        var child: View?
        if (null != viewGroup) {
            for (i in 0 until viewGroup.childCount) {
                child = viewGroup.getChildAt(i)
                if (child is NumberPicker) {
                    numberPickerList.add(child as NumberPicker?)
                } else if (child is LinearLayout) {
                    val result = findNumberPicker(child as ViewGroup?)
                    if (result.isNotEmpty()) {
                        return result
                    }
                }
            }
        }
        return numberPickerList
    }

    private fun shouldResizePicker(): Boolean {
        return pickerHeight != DEFAULT_PICKER_HEIGHT || pickerItemWidth != DEFAULT_PICKER_ITEM_WIDTH || pickerItemInterval != DEFAULT_PICKER_ITEM_INTERVAL
    }

    private fun shouldResetDividerColor(): Boolean {
        return lineDrawable == null && originalDividerColor != DEFAULT_ORIGINAL_DIVIDER_COLOR
    }


    private fun resizeNumberPicker(index: Int, np: NumberPicker?) {
        if (index >= 3) {
            throw IllegalStateException("Index must be smaller than 3!!! index = $index")
        }
        val params = LinearLayout.LayoutParams(pickerItemWidth, pickerHeight)
        if (pickerItemInterval != DEFAULT_PICKER_ITEM_INTERVAL) {
            val halfInterval = pickerItemInterval / 2
            val left: Int
            val right: Int
            when (index) {
                0 -> {
                    left = 0
                    right = halfInterval
                }
                1 -> {
                    left = halfInterval
                    right = halfInterval
                }
                2 -> {
                    left = halfInterval
                    right = 0
                }
                else -> {
                    throw IllegalStateException("Index is illegal!!!")
                }
            }
            params.setMargins(left, 0, right, 0)
        }

        np?.layoutParams = params
    }
}