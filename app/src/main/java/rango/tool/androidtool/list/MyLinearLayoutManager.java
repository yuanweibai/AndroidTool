package rango.tool.androidtool.list;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * Created by baiyuanwei on 17/11/16.
 */

public class MyLinearLayoutManager extends LinearLayoutManager {

    public MyLinearLayoutManager(Context context) {
        super(context);
    }


    public boolean canScrollVertically() {
        return false;
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }
}
