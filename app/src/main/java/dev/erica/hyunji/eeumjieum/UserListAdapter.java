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
 * Created by Erica on 2016-09-07.
 */

public class UserListAdapter extends BaseAdapter{
    private int layout;
    private LayoutInflater inflater;
    private ArrayList<UserListItem> mListData = new ArrayList<UserListItem>();


    public UserListAdapter(Context context, int layout, ArrayList<UserListItem> data){
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListData = data;
        this.layout = layout;
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

        ImageView uImg = (ImageView)convertView.findViewById(R.id.userImg_item);
        TextView name = (TextView) convertView.findViewById(R.id.userName_item);
        TextView room = (TextView) convertView.findViewById(R.id.userClass_item);


        uImg.setImageResource(userlistitem.getuImg());
        name.setText(userlistitem.getName());
        room.setText(userlistitem.getRoom());

        return convertView;
    }

   public void setChecked(int position){

   }
}


