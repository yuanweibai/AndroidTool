package rango.tool.androidtool.list;

import android.content.Context;

import rango.tool.androidtool.base.recyclerview.BaseItemType;
import rango.tool.androidtool.base.recyclerview.BaseItemView;
import rango.tool.androidtool.list.view.ListEmptyItemView;
import rango.tool.androidtool.list.view.ListNormalIteView;

public class ListGenerator {

    public BaseItemView createView(Context context, int itemType) {
        switch (itemType) {
            case BaseItemType.TYPE_LIST_NORMAL:
                return new ListNormalIteView(context);
            case BaseItemType.TYPE_LIST_EMPTY:
                return new ListEmptyItemView(context);
        }
        return null;
    }
}
