package rango.tool.androidtool.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;

public class OutlineTextView extends AppCompatTextView {

    private TextPaint strokePaint;

    public OutlineTextView(Context context) {
        this(context, null);
    }

    public OutlineTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OutlineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // lazy load
        if (strokePaint == null) {
            strokePaint = new TextPaint();
        }
        // 复制原来TextViewg画笔中的一些参数
        TextPaint paint = getPaint();
        strokePaint.setTextSize(paint.getTextSize());
        strokePaint.setTypeface(paint.getTypeface());
        strokePaint.setFlags(paint.getFlags());
        strokePaint.setAlpha(paint.getAlpha());

        // 自定义描边效果
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(Color.RED);
        strokePaint.setStrokeWidth(3);

        String text = getText().toString();
        //在文本底层画出带描边的文本
        canvas.drawText(text, (getWidth() - strokePaint.measureText(text)) / 2,
                getBaseline(), strokePaint);

        super.onDraw(canvas);
    }
}
