package rango.tool.androidtool.list;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by baiyuanwei on 17/11/16.
 */

public class MyLinearLayoutManager extends LinearLayoutManager {

    public MyLinearLayoutManager(Context context) {
        super(context);
    }


    @Override
    public boolean canScrollVertically() {
        return false;
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }
}
