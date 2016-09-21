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
 * Created by Erica on 2016-09-12.
 */
public class ObservArticleListAdapter extends BaseAdapter{
    private int layout;
    private LayoutInflater inflater;
    private ArrayList<ObservArticleItem> mListData = new ArrayList<ObservArticleItem>();
    Context mContext;


    public ObservArticleListAdapter(Context context, int layout, ArrayList<ObservArticleItem> data){
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

        ImageView objimg = (ImageView)convertView.findViewById(R.id.objImg_item);
        TextView objname = (TextView) convertView.findViewById(R.id.objName_item);
        TextView objroom = (TextView) convertView.findViewById(R.id.objRoom_item);
        TextView artcontent = (TextView) convertView.findViewById(R.id.article_content_tfd);
        TextView artkey = (TextView) convertView.findViewById(R.id.invisible_content_key);
        TextView comnum = (TextView) convertView.findViewById(R.id.comment_num);

        objimg.setImageResource(R.drawable.uimg_circle_1);
        objname.setText(articlelistitem.getObjectname());
        objroom.setText(articlelistitem.getObjectroom());
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
