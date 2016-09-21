package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Erica on 2016-09-19.
 */
public class DietListAdapter extends BaseAdapter{

    private int layout;
    private LayoutInflater inflater;
    private ArrayList<DietListItem> mListData = new ArrayList<>();
    Context mContext;

    public DietListAdapter(Context context, int layout, ArrayList<DietListItem> data){
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

        TextView date = (TextView) convertView.findViewById(R.id.diet_day_tv);
        ImageView morning_iv = (ImageView) convertView.findViewById(R.id.morning_iv);
        ImageView afternoon_iv = (ImageView) convertView.findViewById(R.id.afternoon_iv);
        ImageView night_iv = (ImageView) convertView.findViewById(R.id.night_iv);


        String str = article.getDay();
        String[] arr = str.split("/");
        int year = Integer.parseInt(arr[0]);
        int month = Integer.parseInt(arr[1]);
        int day = Integer.parseInt(arr[2]);


        Calendar cal = Calendar.getInstance(Locale.KOREA);

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DATE, day);
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
        String str_day = year + "년 " + month + "월 " + day + "일 " ;
        switch (day_of_week){
            case 1:
                str_day = str_day + "(일)";
                break;
            case 2:
                str_day = str_day + "(월)";
                break;
            case 3:
                str_day = str_day + "(화)";
                break;
            case 4:
                str_day = str_day + "(수)";
                break;
            case 5:
                str_day = str_day + "(목)";
                break;
            case 6:
                str_day = str_day + "(금)";
                break;
            case 7:
                str_day = str_day + "(토)";
                break;

        }
        //System.out.println("오늘 날짜 : " + cal.getTime());

        date.setText(str_day);
        morning_iv.setImageResource(article.getPhoto_morning());
        afternoon_iv.setImageResource(article.getPhoto_afternoon());
        night_iv.setImageResource(article.getPhoto_night());


        return convertView;
    }
}
