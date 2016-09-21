package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Erica on 2016-09-18.
 */
public class PersonalWorkReportAdapter extends BaseAdapter {
    private int layout;
    private LayoutInflater inflater;
    private ArrayList<PersonalWorkReportItem> mListData = new ArrayList<>();

    public PersonalWorkReportAdapter(Context context, int layout, ArrayList<PersonalWorkReportItem> data){
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListData = data;
        this.layout = layout;
    }


    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(layout, parent, false);
        }
        PersonalWorkReportItem userlistitem = mListData.get(position);

        ImageView uImg = (ImageView)convertView.findViewById(R.id.objImg_item);
        TextView name = (TextView) convertView.findViewById(R.id.objName_item);
        TextView room = (TextView) convertView.findViewById(R.id.article_content_tfd);
        TextView invi = (TextView) convertView.findViewById(R.id.invisible_content_key);
        ImageView iv = (ImageView) convertView.findViewById(R.id.status_iv);

        uImg.setImageResource(userlistitem.getObjectImg());
        name.setText(userlistitem.getObjectName());
        if(userlistitem.getStatus() == 0){
            room.setText(userlistitem.getObjectContext());
            iv.setVisibility(View.GONE);
        }else if(userlistitem.getStatus() == 1){
            room.setText("");
            iv.setVisibility(View.VISIBLE);
            iv.setImageResource(R.drawable.work_report_status_out);
        }else if(userlistitem.getStatus() == 2){
            room.setText("");
            iv.setVisibility(View.VISIBLE);
            iv.setImageResource(R.drawable.work_report_status_hospital);
        }else if(userlistitem.getStatus() == 3){
            room.setText(userlistitem.getObjectContext());
            iv.setVisibility(View.GONE);
        }

        invi.setText(String.valueOf(userlistitem.getId()));

        return convertView;
    }
}
