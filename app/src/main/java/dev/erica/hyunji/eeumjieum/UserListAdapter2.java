package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Erica on 2016-09-17.
 */
public class UserListAdapter2 extends BaseAdapter{
    private int layout;
    private LayoutInflater inflater;
    private ArrayList<UserListItem> mListData = new ArrayList<UserListItem>();
    private ArrayList<Integer> statusListData = new ArrayList<>();

    public UserListAdapter2(Context context, int layout, ArrayList<UserListItem> data, ArrayList<Integer> statusdata){
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListData = data;
        this.layout = layout;
        this.statusListData = statusdata;
    }

    @Override
    public int getCount(){return mListData.size();}
    @Override
    public Object getItem(int position){
        return mListData.get(position);
    }
    @Override
    public long getItemId(int position){ return position; }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = inflater.inflate(layout, parent, false);
        }
        UserListItem userlistitem = mListData.get(position);

        TextView name = (TextView) convertView.findViewById(R.id.userName_item);
        TextView room = (TextView) convertView.findViewById(R.id.userClass_item);
        Button btn = (Button) convertView.findViewById(R.id.status_btn);

        ImageView uImg = (ImageView)convertView.findViewById(R.id.userImg_item);
        uImg.setClipToOutline(true);

        uImg.setImageResource(userlistitem.getuImg());
        name.setText(userlistitem.getName());
        room.setText(userlistitem.getRoom());

        switch (statusListData.get(position)){
            case 0:
                btn.setBackgroundResource(R.drawable.work_report_status_normal);
                break;
            case 1:
                btn.setBackgroundResource(R.drawable.work_report_status_out);
                break;
            case 2:
                btn.setBackgroundResource(R.drawable.work_report_status_hospital);
                break;
            case 3:
                btn.setBackgroundResource(R.drawable.work_report_status_etc);
                break;
        }

        return convertView;
    }

    public int getStatus(int position){return statusListData.get(position);}


}
