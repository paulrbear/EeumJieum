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
 * Created by Erica on 2016-09-15.
 */
public class ObservArticleListAdapter2 extends BaseAdapter {

    private int layout;
    private LayoutInflater inflater;
    private ArrayList<ObservArticleItem> mListData = new ArrayList<ObservArticleItem>();
    Context mContext;


    public ObservArticleListAdapter2(Context context, int layout, ArrayList<ObservArticleItem> data){
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
        ObservArticleItem articlelistitem = mListData.get(position);

        TextView day = (TextView) convertView.findViewById(R.id.writeday);
        TextView writer = (TextView) convertView.findViewById(R.id.writer_tfd);
        TextView artcontent = (TextView) convertView.findViewById(R.id.article_content_tfd);
        TextView artkey = (TextView) convertView.findViewById(R.id.invisible_content_key);
        TextView comnum = (TextView) convertView.findViewById(R.id.comment_num);
        ImageView mood = (ImageView) convertView.findViewById(R.id.mood_iv);
        ImageView activity = (ImageView) convertView.findViewById(R.id.activity_iv);
        ImageView sleep = (ImageView) convertView.findViewById(R.id.sleep_iv);

        String tmp = articlelistitem.getDay();
        String[] arr = tmp.split("/");
        if(Integer.parseInt(arr[1]) < 10){
            tmp = "0" + arr[1] + "." + arr[2];
        }else {
            tmp = arr[1] + "." + arr[2];
        }
        day.setText(tmp);
        int tmpmood = articlelistitem.getMood();
        switch (tmpmood){
            case 1:
                mood.setImageResource(R.drawable.mood_good);
                break;
            case 2:
                mood.setImageResource(R.drawable.mood_soso);
                break;
            case 3:
                mood.setImageResource(R.drawable.mood_bad);
                break;
        }

        int tmpact = articlelistitem.getActivity();
        switch (tmpact){
            case 1:
                activity.setImageResource(R.drawable.activity_good);
                break;
            case 2:
                activity.setImageResource(R.drawable.activity_soso);
                break;
            case 3:
                activity.setImageResource(R.drawable.activity_bad);
                break;
        }


        int tmpsleep = articlelistitem.getSleep();
        switch (tmpsleep){
            case 1:
                sleep.setImageResource(R.drawable.sleep_good);
                break;
            case 2:
                sleep.setImageResource(R.drawable.sleep_soso);
                break;
            case 3:
                sleep.setImageResource(R.drawable.sleep_bad);
                break;
        }

        tmp = articlelistitem.getWriter()+ " 생활재활교사";
        writer.setText(tmp);
        artcontent.setText(articlelistitem.getTfdcontent());
        int articleid = articlelistitem.getArticleid();
        artkey.setText(""+articleid);
        int count = getCommentNumber(articleid);
        comnum.setText(""+count);

        return convertView;
    }

    public void setChecked(int position){

    }

    public int getCommentNumber(int key){
        MyDBHandler handler = MyDBHandler.open((mContext));
        int result = handler.getCommentNumber("OBSERVECOMMENT", key);
        return result;
    }


}
