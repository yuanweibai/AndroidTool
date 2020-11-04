package rango.kotlin.wanandroid.common.list

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout

abstract class BaseItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr), ViewWrapper.Binder {
    var mData: BaseItemData<*>? = null

    override fun bind(data: BaseItemData<*>?) {
        if (data?.data != null) {
            mData = data
            refreshUI()
        }
    }

    protected abstract fun refreshUI()
}