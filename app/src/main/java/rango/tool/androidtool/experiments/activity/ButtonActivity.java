package rango.tool.androidtool.experiments.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.view.animation.PathInterpolatorCompat;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.util.GamePackageHelper;
import rango.tool.androidtool.view.CircleEffectTextView;
import rango.tool.androidtool.view.WaveButton;

public class ButtonActivity extends BaseActivity {
    private WaveButton waveButton;
    private CircleEffectTextView textView;

    private TextView boostBtn;
    private ImageView boostEffectView;

    private RelativeLayout containerView;

    private final static String NO_VIBRATE_TAG = "no_vibrate_tag";

    private final static float[] TRANSLATION = new float[]{2, -2, 3, -3, 4, -4};

    private final static long[] DURATION = new long[]{300, 450, 500};

    private Random random;

    private List<ObjectAnimator> animatorList = new ArrayList<>();

    private static String sGamePackages = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_layout);
        containerView = findViewById(R.id.container_view);
        waveButton = findViewById(R.id.wave_btn);
        textView = findViewById(R.id.circle_effect_btn);
        View view = findViewById(R.id.circle_effect_view);
        view.setTag(NO_VIBRATE_TAG);
        textView.setEffectView(view);

        findViewById(R.id.start_btn).setOnClickListener(v -> waveButton.startWave());

        boostEffectView = findViewById(R.id.boost_effect_view);
        boostEffectView.setTag(NO_VIBRATE_TAG);

        boostBtn = findViewById(R.id.boost_text_view);
        boostBtn.setOnClickListener(v -> startBoostEffectAnim());

        random = new Random();

        findViewById(R.id.ripple_transparent_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sGamePackages = GamePackageHelper.initGamePackages();
                boolean result = sGamePackages.contains("com.dragonest.autochess.google");
                Toast.makeText(ButtonActivity.this, "isGame = " + result, Toast.LENGTH_LONG).show();
            }
        });

    }

    private List<View> getAllViews() {
        int count = containerView.getChildCount();
        List<View> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            View child = containerView.getChildAt(i);
            if (!NO_VIBRATE_TAG.equals(child.getTag())) {
                result.add(child);
            }
        }
        return result;
    }


    private void startBoostEffectAnim() {

        ObjectAnimator firstAlphaAnimator = ObjectAnimator.ofFloat(boostEffectView, "alpha", 0f, 0.7f);
        firstAlphaAnimator.setInterpolator(new LinearInterpolator());
        firstAlphaAnimator.setDuration(40);

        PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat("scaleX", 0.1f, 5.5f);
        PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat("scaleY", 0.1f, 5.5f);
        ObjectAnimator scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(boostEffectView, scaleXHolder, scaleYHolder);
        scaleAnimator.setDuration(3000);
        scaleAnimator.setInterpolator(PathInterpolatorCompat.create(0.18f, 0f, 0.04f, 1f));

        ObjectAnimator secondAlphaAnimator = ObjectAnimator.ofFloat(boostEffectView, "alpha", 0.7f, 0f);
        secondAlphaAnimator.setDuration(3000);
        secondAlphaAnimator.setInterpolator(PathInterpolatorCompat.create(0.93f, 0f, 0.83f, 0.83f));
        secondAlphaAnimator.setStartDelay(40);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(firstAlphaAnimator).with(scaleAnimator);
        animatorSet.play(firstAlphaAnimator).before(secondAlphaAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                boostEffectView.setAlpha(0f);
                boostEffectView.setScaleX(0.1f);
                boostEffectView.setScaleY(0.1f);
                boostEffectView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                boostEffectView.setVisibility(View.GONE);
                cancelAnimator();
            }
        });

        animatorSet.start();

        startVibrate();
    }

    private void startVibrate() {

        List<View> views = getAllViews();
        if (views.isEmpty()) {
            return;
        }

        if (!animatorList.isEmpty()) {
            cancelAnimator();
        }
        for (View view : views) {
            long duration = getVibrateDuration();
            float translationY = getVibrateTranslation();
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "translationX", 0f, translationY, -translationY, 0f);
            animatorX.setRepeatCount(ObjectAnimator.INFINITE);
            animatorX.setDuration(duration);
            animatorX.setInterpolator(new LinearInterpolator());

            float translationX = getVibrateTranslation();
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "translationY", 0f, translationX, -translationX, 0f);
            animatorY.setRepeatCount(ObjectAnimator.INFINITE);
            animatorY.setDuration(duration);
            animatorY.setInterpolator(new LinearInterpolator());

            animatorX.start();
            animatorY.start();

            animatorList.add(animatorX);
            animatorList.add(animatorY);
        }
    }

    private void cancelAnimator() {
        if (animatorList.isEmpty()) {
            return;
        }

        for (ObjectAnimator animator : animatorList) {
            animator.cancel();
        }
        animatorList.clear();
    }

    private float getVibrateTranslation() {
        int index = random.nextInt(TRANSLATION.length);
        return TRANSLATION[index];
    }

    private long getVibrateDuration() {
        int index = random.nextInt(DURATION.length);
        return DURATION[index];
    }

    private void setEffectViewLocation() {
        int[] boostLocation = new int[2];
        boostBtn.getLocationOnScreen(boostLocation);


    }
}
