package rango.tool.androidtool.earning;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.common.utils.ScreenUtils;

public class EarningActivity extends BaseActivity {

    private EarningAnimSurfaceView earningAnimSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earning_layout);

        earningAnimSurfaceView = findViewById(R.id.earning_surface_view);

        int x = ScreenUtils.getScreenWidthPx() / 2;
        int y = (int) (ScreenUtils.getScreenHeightPx() * (438 / 1920f));
        earningAnimSurfaceView.setStart(x, y);
        earningAnimSurfaceView.setEnd(100, 60);

        findViewById(R.id.start_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                earningAnimSurfaceView.start();
            }
        });

        findViewById(R.id.stop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                earningAnimSurfaceView.stop();
            }
        });
    }
}
