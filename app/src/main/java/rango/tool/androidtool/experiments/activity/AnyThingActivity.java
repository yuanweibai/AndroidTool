package rango.tool.androidtool.experiments.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import rango.tool.androidtool.GlideApp;
import rango.tool.androidtool.R;
import rango.tool.androidtool.ToolApplication;
import rango.tool.androidtool.any.Rango;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.market.MarketTools;
import rango.tool.common.utils.ScreenUtils;

public class AnyThingActivity extends BaseActivity {

    private ViewStub viewStub;

    private View view;

    private int d = 0;

    @Override
    public void onAttachedToWindow() {
        ScreenUtils.setCustomDensity(this, getApplication());
        super.onAttachedToWindow();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_any_thing_layout);


//


    }

}
