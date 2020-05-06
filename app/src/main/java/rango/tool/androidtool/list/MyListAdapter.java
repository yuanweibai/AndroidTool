package rango.tool.androidtool.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.list.adapter.BaseItemData;
import rango.tool.androidtool.base.list.adapter.BaseItemType;
import rango.tool.androidtool.list.view.ListBannerView;

public class MyListAdapter extends BaseAdapter {

    private List<BaseItemData> dataList;
    private LayoutInflater layoutInflater;
    private ListBannerView listBannerView;

    public MyListAdapter(Context context, List<BaseItemData> dataList, ListBannerView listBannerView) {
        this.dataList = dataList;
        layoutInflater = LayoutInflater.from(context);
        this.listBannerView = listBannerView;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void update(List<BaseItemData> data) {
        dataList.clear();
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    public void append(List<BaseItemData> data) {
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (dataList.get(position).getType() == BaseItemType.TYPE_LIST_BANNER) {
            return listBannerView;
        } else {
            ViewHolder viewHolder;
            if (convertView == null || convertView.getTag() == null) {
                convertView = layoutInflater.inflate(R.layout.list_normal_item_view, null);
                viewHolder = new ViewHolder();
                viewHolder.textView = convertView.findViewById(R.id.text);
                convertView.findViewById(R.id.image_view).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(parent.getContext(), "iiiiiiiiii", Toast.LENGTH_SHORT).show();
                    }
                });
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(String.valueOf(dataList.get(position).getData()));
            return convertView;
        }
    }

    private static class ViewHolder {
        private TextView textView;

    }
}
