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
 * Created by Erica on 2016-09-17.
 */
public class WorkReportArticleListAdater extends BaseAdapter{


    private int layout;
    private LayoutInflater inflater;
    private ArrayList<WorkReportArticleItem> mListData = new ArrayList<>();
    Context mContext;


    public WorkReportArticleListAdater(Context context, int layout, ArrayList<WorkReportArticleItem> data){
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
        WorkReportArticleItem articlelistitem = mListData.get(position);

        ImageView objimg = (ImageView)convertView.findViewById(R.id.room_image);
        TextView roomname = (TextView) convertView.findViewById(R.id.room_name_tv);
        TextView workername = (TextView) convertView.findViewById(R.id.worker_name_tv);
        TextView artkey = (TextView) convertView.findViewById(R.id.invisible_content_key);

        TextView normalcount = (TextView) convertView.findViewById(R.id.normal_count_tv);
        TextView outcount = (TextView) convertView.findViewById(R.id.out_count_tv);
        TextView hospitalcount = (TextView) convertView.findViewById(R.id.hospital_count_tv);
        TextView etccount = (TextView) convertView.findViewById(R.id.etc_count_tv);

        //default image
        objimg.setImageResource(R.drawable.s_pic_3);
        String tmp = articlelistitem.getObjectroom();
        roomname.setText(tmp);
        if(tmp.equals("은혜방")){
            workername.setText("한지은 선생님, 윤고은 선생님");
        }else if(tmp.equals("기쁨방")){
            workername.setText("이유민 선생님, 한남수 선생님");
        }else if(tmp.equals("믿음방")){
            workername.setText("강지석 선생님");
        }

        int tmpresult = articlelistitem.getNormalcount();
        String st = ""+tmpresult;
        normalcount.setText(st);

        tmpresult = articlelistitem.getOutcount();
        st = "" + tmpresult;
        outcount.setText(st);

        tmpresult = articlelistitem.getHospitalcount();
        st = "" + tmpresult;
        hospitalcount.setText(st);

        tmpresult = articlelistitem.getEtccount();
        st = "" + tmpresult;
        etccount.setText(st);

        tmpresult = articlelistitem.getArticleid();
        st = "" + tmpresult;
        artkey.setText(st);


        return convertView;
    }



}
