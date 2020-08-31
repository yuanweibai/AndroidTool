package rango.tool.androidtool.other;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class OtherActivity extends BaseActivity {

    private MyClass firstClass;
    private MyClass secondClass;

    private int count = 0;
    private Button secondBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_layout);

        findViewById(R.id.first_btn).setOnClickListener(v -> {
            startTestAnim(count);
            count++;
        });

        secondBtn = findViewById(R.id.second_btn);
    }

    private ObjectAnimator objectAnimator;

    private void startTestAnim(int count) {

        if (objectAnimator == null) {
            initAnim();
        }


//        if (objectAnimator.isRunning()) {
//            Log.e("rango", "test anim is ending!!!---------------- cancel_____count = " + num);
        objectAnimator.cancel();
//        }

        objectAnimator.removeAllListeners();
        objectAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {

                Log.e("rango", "test anim is ending!!!---------------- count = " + count);


            }
        });

        objectAnimator.start();
    }

    private void initAnim() {
        objectAnimator = ObjectAnimator.ofFloat(secondBtn, "translationY", 0f, 600f, 0f);
        objectAnimator.setDuration(10000);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    }
}
