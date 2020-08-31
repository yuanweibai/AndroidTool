package rango.tool.androidtool.experiments.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.common.utils.ScreenUtils;

public class WaterMarkActivity extends BaseActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.water_mark_layout);

        imageView = findViewById(R.id.image_view);

        findViewById(R.id.add_water_mark_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                long c = System.currentTimeMillis();
                Bitmap bitmap = createWaterMaskBitmap();
                Log.e("rango", "time: " + (System.currentTimeMillis() - c));
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    private Bitmap createWaterMaskBitmap() {
        Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.wallpaper);
        Bitmap watermark = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap newb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas canvas = new Canvas(newb);
        canvas.drawBitmap(src, 0, 0, null);
        canvas.drawBitmap(watermark, 100, 200, null);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextSize(ScreenUtils.dp2px(12));
        canvas.drawText("Bling Launcher", 100, 400, paint);
        canvas.save();
        canvas.restore();
        return newb;
    }
}
