package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Erica on 2016-09-18.
 */
public class ScheduleListAdapter extends BaseAdapter {
    private int layout;
    private LayoutInflater inflater;
    private ArrayList<ScheduleListItem> mListData = new ArrayList<>();

    public ScheduleListAdapter(Context context, int layout, ArrayList<ScheduleListItem> data){
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
        ScheduleListItem schedulelistitem = mListData.get(position);

        TextView date = (TextView) convertView.findViewById(R.id.schedule_date_tv);
        TextView title = (TextView) convertView.findViewById(R.id.schedule_title_tv);
        TextView time = (TextView) convertView.findViewById(R.id.schedule_time_tv);

        String str = schedulelistitem.getDay();
        String[] arr = str.split("/");
        str = arr[1] + "." + arr[2];
        date.setText(str);
        title.setText(schedulelistitem.getTitle());
        str = schedulelistitem.getTime();
        String[] arr2 = str.split(":");
        int hour = Integer.parseInt(arr2[0]);
        if(hour > 12){
            int tmp = hour-12;
            str = "오후 " + tmp + " 시";
        }else{
            str = "오전 " + hour + " 시";
        }

        time.setText(str);


        return convertView;
    }
}
