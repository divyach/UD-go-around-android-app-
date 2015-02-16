package com.udel;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ud.R;

class CustomAdapter extends BaseAdapter
{
    Context mContext;
    ArrayList<Bean> beandata;
    LayoutInflater mInflater;
    public CustomAdapter(Context context,ArrayList<Bean> itemList) 
    {
        mContext = context;
        beandata = itemList;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return beandata.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder vh;
        if(convertView == null)
        {
            vh= new ViewHolder();
             convertView = mInflater.inflate(R.layout.row, 
                     parent, false);
            vh.tv1 = (TextView) convertView.findViewById(R.id.textView1);
            vh.tv2 = (TextView) convertView.findViewById(R.id.textView2);
          
            convertView.setTag(vh); 
        } else { 
            vh = (ViewHolder) convertView.getTag(); 
        }
            Bean objBean = beandata.get(position);
            vh.tv1.setText(objBean.getTitle());
            vh.tv2.setText(objBean.getDesc());
           

        return convertView;
    }
    static class ViewHolder
    {
        TextView tv1,tv2;
    }
}