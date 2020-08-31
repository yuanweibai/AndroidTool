package rango.tool.androidtool.experiments.views;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class TestRecyclerView extends RecyclerView {

    public TestRecyclerView(Context context) {
        super(context);
    }

    public TestRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        Log.e("rango", "TestRecyclerView, onTouchEvent: action = " + e.getAction());
        return super.onTouchEvent(e);
    }
}
