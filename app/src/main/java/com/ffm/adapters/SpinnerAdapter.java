package com.ffm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ffm.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    List<String> itemList;
    LayoutInflater inflter;

    public SpinnerAdapter(Context applicationContext, List<String> itemList) {
        this.context = applicationContext;
        this.itemList = itemList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_custom_layout, null);
        TextView name = (TextView) view.findViewById(R.id.tv_name);
        name.setText(itemList.get(i));
        return view;
    }

    public void setItems(ArrayList<String> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }
}
