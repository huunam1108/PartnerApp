package com.example.app2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by framgia on 25/11/2016.
 */

public class PartnerAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mVersionList;

    public PartnerAdapter(Context context, ArrayList<String> versions) {
        mContext = context;
        mVersionList = versions;
    }

    @Override
    public int getCount() {
        return mVersionList.size();
    }

    @Override
    public String getItem(int position) {
        return mVersionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
            holder.tvVersion = (TextView) convertView.findViewById(R.id.tvVersion);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvVersion.setText(getItem(position));
        return convertView;
    }

    static class ViewHolder {
        TextView tvVersion;
    }
}
