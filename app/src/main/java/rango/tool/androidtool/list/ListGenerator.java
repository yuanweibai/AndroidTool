package rango.tool.androidtool.list;

import android.content.Context;

import rango.tool.androidtool.base.list.adapter.BaseItemType;
import rango.tool.androidtool.base.list.adapter.BaseItemView;
import rango.tool.androidtool.list.view.ListEmptyItemView;
import rango.tool.androidtool.list.view.ListImageItemView;
import rango.tool.androidtool.list.view.ListNestItemView;
import rango.tool.androidtool.list.view.ListNormalIteView;

public class ListGenerator {

    public BaseItemView createView(Context context, int itemType) {
        switch (itemType) {
            case BaseItemType.TYPE_LIST_NORMAL:
                return new ListNormalIteView(context);
            case BaseItemType.TYPE_LIST_EMPTY:
                return new ListEmptyItemView(context);
            case BaseItemType.TYPE_LIST_IMAGE:
                return new ListImageItemView(context);
            case BaseItemType.TYPE_LIST_NEST:
                return new ListNestItemView(context);
            case BaseItemType.TYPE_LIST_FOOTER:
                return new FooterView(context);
        }
        return null;
    }
}
