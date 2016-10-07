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
import java.util.Date;
import java.util.Locale;

/**
 * Created by Erica on 2016-09-19.
 */
public class AlbumListAdapter extends BaseAdapter{

    private int layout;
    private LayoutInflater inflater;
    private ArrayList<ProgramArticleItem> mListData = new ArrayList<>();
    Context mContext;
    public int firstitem = 0;

    public AlbumListAdapter(Context context, int layout, ArrayList<ProgramArticleItem> data){
        mContext = context;
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

        ProgramArticleItem articlelistitem = mListData.get(position);


        ImageView open_photo = (ImageView) convertView.findViewById(R.id.open_iv);
        ImageView open_overlay_iv = (ImageView) convertView.findViewById(R.id.open_overlay_iv);
        ImageView background_iv = (ImageView) convertView.findViewById(R.id.background_iv);
        ImageView overlay_iv = (ImageView) convertView.findViewById(R.id.overlay_iv);
        TextView title_tv = (TextView) convertView.findViewById(R.id.title_tv);
        TextView day_tv = (TextView) convertView.findViewById(R.id.day_tv);
        TextView artkey = (TextView) convertView.findViewById(R.id.invisible_content_key);


        String tmp = articlelistitem.getDay();
        String[] arr = tmp.split("/");

        String str_day = arr[0] + "년 " + arr[1] + "월 " + arr[2] + "일 ";

        Date tmpdate = new Date();
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.setTime(tmpdate);

        cal.set(Calendar.YEAR, Integer.parseInt(arr[0]));
        cal.set(Calendar.MONTH, Integer.parseInt(arr[1])-1);
        cal.set(Calendar.DATE, Integer.parseInt(arr[2]));

        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);

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



        day_tv.setText(str_day);
        title_tv.setText(articlelistitem.getTitle());


        int articleid = articlelistitem.getArticleid();
        artkey.setText(""+articleid);

        String tmpphoto = articlelistitem.getPhoto();
        String arr2[] = tmpphoto.split("/");
        if(Integer.parseInt(arr2[0]) > 0){
            background_iv.setImageResource(Integer.parseInt(arr2[1]));
            open_photo.setImageResource(Integer.parseInt(arr2[1]));
        }

        if(position == firstitem){
            open_photo.setVisibility(View.VISIBLE);
            open_overlay_iv.setVisibility(View.VISIBLE);
            open_photo.setScaleType(ImageView.ScaleType.CENTER_CROP);

            overlay_iv.setVisibility(View.GONE);
            background_iv.setVisibility(View.GONE);

        }else{
            open_photo.setVisibility(View.GONE);
            open_overlay_iv.setVisibility(View.GONE);

            overlay_iv.setVisibility(View.VISIBLE);
            background_iv.setVisibility(View.VISIBLE);
        }


        return convertView;
    }

    public void setFirstitem(int position){
        firstitem = position;
    }



}
