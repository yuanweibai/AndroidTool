package rango.tool.androidtool.viewpager.transformpage;

import android.util.Log;
import android.view.View;

public class CustomPageTransformer extends BasePageTransformer {

    public CustomPageTransformer(int pageMargin) {
        super(pageMargin);
    }

    @Override
    public void onPageTransform(View page, float process) {
        String tag = (String) page.getTag();
        Log.e("rango", "tag = " + tag + ", process = " + process);


        page.setPivotX(page.getWidth() / 2f);
        page.setPivotY(page.getHeight() / 2f);


        process = process - (int) process;

        page.setRotation(45 * process);


    }
}
