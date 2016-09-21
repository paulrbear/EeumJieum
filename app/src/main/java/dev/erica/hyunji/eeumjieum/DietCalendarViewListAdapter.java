package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Erica on 2016-09-19.
 */
public class DietCalendarViewListAdapter extends BaseAdapter{

    private int layout;
    private LayoutInflater inflater;
    private ArrayList<DietListItem> mListData = new ArrayList<>();
    Context mContext;

    public DietCalendarViewListAdapter(Context context, int layout, ArrayList<DietListItem> data){
        mContext = context;
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

        DietListItem article = mListData.get(position);

        TextView day = (TextView) convertView.findViewById(R.id.day_tv);
        TextView dayofweek =  (TextView) convertView.findViewById(R.id.day_of_week_tv);
        TextView morning = (TextView) convertView.findViewById(R.id.menu1);
        TextView afternoon = (TextView) convertView.findViewById(R.id.menu2);
        TextView night = (TextView) convertView.findViewById(R.id.menu3);

        String str = article.getDay();
        String[] arr = str.split("/");
        int year = Integer.parseInt(arr[0]);
        int month = Integer.parseInt(arr[1]);
        int intday = Integer.parseInt(arr[2]);


        Calendar cal = Calendar.getInstance(Locale.KOREA);
        int today = cal.get(Calendar.DAY_OF_MONTH);

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DATE, intday);
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);

        String str_day;

        switch (day_of_week){
            case 1:
                str_day = "일요일";
                break;
            case 2:
                str_day = "월요일";
                break;
            case 3:
                str_day = "화요일";
                break;
            case 4:
                str_day = "수요일";
                break;
            case 5:
                str_day = "목요일";
                break;
            case 6:
                str_day = "금요일";
                break;
            case 7:
                str_day = "토요일";
                break;
            default:
                str_day = "룰루랄라";
                break;

        }
        //System.out.println("오늘 날짜 : " + cal.getTime());

        if(today == intday){
            day.setTextColor(mContext.getResources().getColor(R.color.colorSelectFont));
        }

        day.setText(arr[2]);
        dayofweek.setText(str_day);
        morning.setText(article.getStr_morning());
        afternoon.setText(article.getStr_afternoon());
        night.setText(article.getStr_night());

        return convertView;
    }







}
