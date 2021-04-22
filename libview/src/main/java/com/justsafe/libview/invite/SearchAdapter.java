package com.justsafe.libview.invite;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.justsafe.libview.R;
import com.justsafe.libview.invite.bean.OrgBean;
import com.justsafe.libview.invite.bean.StaffBean;

import java.util.List;

public class SearchAdapter extends BaseAdapter {
    private List<StaffBean> staffBeans;
    private Context context;
    private String searchContent;//用户搜索的内容

    public SearchAdapter(List<StaffBean> staffBeans, Context context, String searchContent) {
        this.context = context;
        this.staffBeans = staffBeans;
        this.searchContent = searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    @Override
    public int getCount() {
        return staffBeans.size();
    }

    @Override
    public StaffBean getItem(int position) {
        return staffBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StaffHolder staffHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search, null);
            staffHolder = new StaffHolder(convertView);
            convertView.setTag(staffHolder);
        } else {
            staffHolder = (StaffHolder) convertView.getTag();
            if(!convertView.isEnabled()){
                convertView.setEnabled(true);
            }
        }

        StaffBean sb = (StaffBean) staffBeans.get(position);
        staffHolder.name.setText(sb.getName());
        staffHolder.policeNum.setText("("+sb.getDepartNum()+")");
        staffHolder.address.setText(sb.getDepartName());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    class StaffHolder {
        TextView name;
        TextView policeNum;
        TextView address;
        public StaffHolder(View view) {
            name = view.findViewById(R.id.name_tv);
            policeNum = view.findViewById(R.id.police_num_tv);
            address = view.findViewById(R.id.address_tv);
        }
    }


}

