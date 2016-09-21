package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Erica on 2016-09-18.
 */
public class NoticeListAdapter extends BaseAdapter {
    private int layout;
    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<NoticeListItem> mListData = new ArrayList<>();

    public NoticeListAdapter(Context context, int layout, ArrayList<NoticeListItem> data){
        mContext= context;
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
        NoticeListItem article = mListData.get(position);

        TextView date = (TextView) convertView.findViewById(R.id.notice_day_tv);
        TextView title = (TextView) convertView.findViewById(R.id.notice_title_tv);
        TextView content = (TextView) convertView.findViewById(R.id.notice_content_tv);
        TextView writertv = (TextView) convertView.findViewById(R.id.writer_name_tv);
        TextView comm = (TextView) convertView.findViewById(R.id.comment_num) ;

        String str = article.getDay();
        String[] arr = str.split("/");
        int year = Integer.parseInt(arr[0]);
        int month = Integer.parseInt(arr[1]);
        int day = Integer.parseInt(arr[2]);

        Date tmpdate = new Date();
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.setTime(tmpdate);

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
        title.setText(article.getTitle());
        content.setText(article.getContent());
        writertv.setText(article.getWriter() + "생활재활교사");

        int com = getCommentNumber(article.getArticlekey());
        comm.setText(String.valueOf(com));

        return convertView;
    }

    public int getCommentNumber(int key){
        MyDBHandler handler = MyDBHandler.open((mContext));
        int result = handler.getCommentNumber("NOTICECOMMENT", key);
        return result;
    }



}
