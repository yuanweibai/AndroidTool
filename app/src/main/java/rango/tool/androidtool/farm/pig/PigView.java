package rango.tool.androidtool.farm.pig;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

public class PigView extends AppCompatImageView {

    private PigData data;

    public PigView(Context context) {
        this(context, null);
    }

    public PigView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PigView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }

    public void upgrade() {
        if (data == null) {
            return;
        }
        data.upgrade();
        refreshUi();
    }

    public void setData(PigData data) {
        if (data == null) {
            return;
        }
        this.data = data;
        refreshUi();
    }

    public PigData getData() {
        return data;
    }

    public int getPigGrade() {
        if (data == null) {
            return -1;
        }
        return data.getGrade();
    }

    public void release() {
        // 此 view 已隐藏，可以释放其占用的资源了
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof PigView)) {
            return false;
        }

        if (this.data == null) {
            return false;
        }

        return this.data.getGrade() == ((PigView) obj).getPigGrade();
    }

    private void refreshUi() {
        setImageDrawable(getResources().getDrawable(data.getDrawableId()));

    }
}
