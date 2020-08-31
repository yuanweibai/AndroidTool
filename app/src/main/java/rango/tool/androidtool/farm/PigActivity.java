package rango.tool.androidtool.farm;

import android.graphics.RectF;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.falling.BaseFallingSurfaceView;
import rango.tool.androidtool.farm.pig.PigContainerView;
import rango.tool.androidtool.view.PotholerRelativeLayout;

public class PigActivity extends BaseActivity {

    private PigContainerView pigContainerView;
    private BaseFallingSurfaceView fallingSurfaceView;
    private TextView buyBtn;
    private ConstraintLayout rootView;
    private PotholerRelativeLayout potholerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pig_layout);

        rootView = findViewById(R.id.root_view);
        fallingSurfaceView = findViewById(R.id.falling_view);
        buyBtn = findViewById(R.id.buy_button);

        pigContainerView = findViewById(R.id.pig_container_view);
        pigContainerView.initPig(0, 0, 1);
        buyBtn.setOnClickListener(v -> {
            int x = v.getLeft() + v.getWidth() / 2;
            int y = v.getTop() + v.getHeight() / 2;
            pigContainerView.generatePig(0, x, y);
            if (potholerView != null) {
                potholerView.showNext();
            }
        });

        findViewById(R.id.accelerate_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fallingSurfaceView.startFalling(5000, 10);
                fallingSurfaceView.startFalling();
            }
        });

        findViewById(R.id.stop_btn).setOnClickListener(v -> fallingSurfaceView.stopFalling());
        findViewById(R.id.resume_button).setOnClickListener(v -> fallingSurfaceView.resumeFalling());
        findViewById(R.id.pause_btn).setOnClickListener(v -> fallingSurfaceView.pauseFalling());

        findViewById(R.id.guide_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGuideView();
            }
        });
    }

    private void showGuideView() {

        List<List<RectF>> listRectF = new ArrayList<>();
        RectF buyBtnRectF = new RectF();
        buyBtnRectF.left = buyBtn.getLeft();
        buyBtnRectF.top = buyBtn.getTop();
        buyBtnRectF.right = buyBtn.getRight();
        buyBtnRectF.bottom = buyBtn.getBottom();

        List<RectF> buyRectFList = new ArrayList<>();
        buyRectFList.add(buyBtnRectF);

        listRectF.add(buyRectFList);

        List<RectF> firstAndSecondPigRectFList = new ArrayList<>();
        firstAndSecondPigRectFList.add(pigContainerView.getFirstAndSecondPigRectF());

        listRectF.add(firstAndSecondPigRectFList);

        List<RectF> firstPigAndRecyclerBtnRectFList = new ArrayList<>();
        firstPigAndRecyclerBtnRectFList.add(pigContainerView.getFirstPigRectF());
        firstPigAndRecyclerBtnRectFList.add(buyBtnRectF);
        listRectF.add(firstPigAndRecyclerBtnRectFList);


        potholerView = new PotholerRelativeLayout(PigActivity.this);
        potholerView.setRectF(R.color.potholer_bg_color, listRectF);
        rootView.addView(potholerView, new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
    }
}
