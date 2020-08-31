package rango.tool.androidtool.earning;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.common.utils.ScreenUtils;

public class EarningActivity extends BaseActivity {

    private EarningAnimSurfaceView earningAnimSurfaceView;

    private LinearLayout rootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earning_layout);

        rootView = findViewById(R.id.root_view);

        int x = ScreenUtils.getScreenWidthPx() / 2;
        int y = (int) (ScreenUtils.getScreenHeightPx() * (438 / 1920f));



        findViewById(R.id.start_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (earningAnimSurfaceView == null) {
                    earningAnimSurfaceView = new EarningAnimSurfaceView(EarningActivity.this, x, y, 100, 60);
                    rootView.addView(earningAnimSurfaceView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    earningAnimSurfaceView.setAllEarningEndListener(new EarningAnimEndListener() {
                        @Override
                        public void onAnimEnd() {
                            Log.e("rango", "end..............");
                            rootView.removeView(earningAnimSurfaceView);
                            earningAnimSurfaceView = null;
                        }
                    });
                }
                earningAnimSurfaceView.start();
            }
        });

        findViewById(R.id.stop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
