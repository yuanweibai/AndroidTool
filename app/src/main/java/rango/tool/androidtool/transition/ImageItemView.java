package rango.tool.androidtool.transition;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import rango.tool.androidtool.GlideApp;
import rango.tool.androidtool.R;
import rango.tool.androidtool.base.list.adapter.BaseItemView;
import rango.tool.common.utils.ActivityUtils;
import rango.tool.common.utils.ScreenUtils;

public class ImageItemView extends BaseItemView {

    public static final String KEY_IMAGE_URL = "image_url";
    private ImageView imageView;
    private TextView textView;
    private String imageUrl;


    public ImageItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.image_item_view, this);
        setBackgroundColor(getResources().getColor(R.color.white));
        imageView = findViewById(R.id.image_view);
        textView = findViewById(R.id.text);

        int width = (ScreenUtils.getScreenWidthPx() - ScreenUtils.dp2px(12) * 3) / 2;
        LayoutParams params = (LayoutParams) imageView.getLayoutParams();
        params.width = width;
        params.height = width;
        imageView.setLayoutParams(params);

        LayoutParams textParams = (LayoutParams) textView.getLayoutParams();
        textParams.width = width;
        textView.setLayoutParams(textParams);
    }

    @Override
    protected void refreshUI() {

        if (mData == null || mData.getData() == null) {
            return;
        }
        imageUrl = String.valueOf(mData.getData());
        GlideApp.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.load_image_default)
                .error(R.drawable.load_image_error)
                .into(imageView);

        imageView.setOnClickListener(v -> {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(ActivityUtils.contextToActivity(getContext()), v,
                    getContext().getResources().getString(R.string.detail_transition));
            Intent intent = new Intent(getContext(), ImageDetailActivity.class);
            intent.putExtra(KEY_IMAGE_URL, imageUrl);
            ActivityCompat.startActivity(getContext(), intent, optionsCompat.toBundle());
        });
    }
}
