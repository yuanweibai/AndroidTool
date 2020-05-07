package rango.tool.androidtool.list;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.common.utils.ScreenUtils;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private int dividerSize = ScreenUtils.dp2px(6);
    private Drawable dividerDrawable;
    private int orientation;

    DividerItemDecoration(Context context, int orientation) {
        dividerDrawable = context.getResources().getDrawable(R.drawable.recycler_divider);
        setOrientation(orientation);
    }

    private void setOrientation(int orientation) {
        if (orientation != LinearLayoutManager.HORIZONTAL && orientation != LinearLayoutManager.VERTICAL) {
            throw new IllegalArgumentException("invalid orientation");
        }
        this.orientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (orientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            android.support.v7.widget.RecyclerView v = new android.support.v7.widget.RecyclerView(parent.getContext());
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + dividerSize;
            dividerDrawable.setBounds(left, top, right, bottom);
            dividerDrawable.draw(c);
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + dividerSize;
            dividerDrawable.setBounds(left, top, right, bottom);
            dividerDrawable.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int count = parent.getAdapter().getItemCount();
        int lastPosition = count - 1;
        if (position == lastPosition) {
            return;
        }
        if (orientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, dividerSize);
        } else {
            outRect.set(0, 0, dividerSize, 0);
        }
    }
}
