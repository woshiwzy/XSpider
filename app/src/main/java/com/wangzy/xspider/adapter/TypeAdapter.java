package com.wangzy.xspider.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;
import com.wangzy.xspider.R;
import com.wangzy.xspider.db.DbConstant;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by wangzy on 15/11/10.
 */
public class TypeAdapter extends BaseAdapter {

    private List<ParseObject> parseObjects;
    private Context context;
    private LayoutInflater layoutInflater;

    public TypeAdapter(Context context, List<ParseObject> parseObjects) {
        this.context = context;
        this.parseObjects = parseObjects;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return parseObjects.size();
    }

    @Override
    public Object getItem(int i) {
        return parseObjects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if (null == view) {
            view = layoutInflater.inflate(R.layout.item_layout, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder=(ViewHolder)view.getTag();
        }

        ParseObject pb = parseObjects.get(i);
        viewHolder.textViewTypeName.setText(pb.getString(DbConstant.RESOURCE_TYPE_COL));
        return view;
    }

    class ViewHolder {

        @InjectView(R.id.textViewTypeName)
        TextView textViewTypeName;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
