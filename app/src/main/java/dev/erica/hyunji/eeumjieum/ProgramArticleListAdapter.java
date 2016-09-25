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
 * Created by Erica on 2016-09-16.
 */
public class ProgramArticleListAdapter extends BaseAdapter {

    private int layout;
    private LayoutInflater inflater;
    private ArrayList<ProgramArticleItem> mListData = new ArrayList<>();
    Context mContext;
    public int firstitem = 0;

    public ProgramArticleListAdapter(Context context, int layout, ArrayList<ProgramArticleItem> data){
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
        System.out.println("program article get view!!!!!");
        if(convertView == null){
            convertView = inflater.inflate(layout, parent, false);
        }

        ProgramArticleItem articlelistitem = mListData.get(position);


        TextView day = (TextView) convertView.findViewById(R.id.writeday);
        TextView writer = (TextView) convertView.findViewById(R.id.writer_tfd);
        TextView artcontent = (TextView) convertView.findViewById(R.id.article_content_tfd);
        TextView artkey = (TextView) convertView.findViewById(R.id.invisible_content_key);
        TextView comnum = (TextView) convertView.findViewById(R.id.comment_num);
        ImageView photo = (ImageView) convertView.findViewById(R.id.article_first_img);

        String tmp = articlelistitem.getDay();
        String[] arr = tmp.split("/");
        if(Integer.parseInt(arr[1]) < 10){
            tmp = "0" + arr[1] + "." + arr[2];
        }else {
            tmp = arr[1] + "." + arr[2];
        }
        day.setText(tmp);

        tmp = articlelistitem.getWriter()+ " 생활재활교사";
        writer.setText(tmp);
        artcontent.setText(articlelistitem.getTfdcontent());
        int articleid = articlelistitem.getArticleid();
        artkey.setText(""+articleid);
        int count = getCommentNumber(articleid);
        comnum.setText(""+count);
        String tmpphoto = articlelistitem.getPhoto();
        String[] arr2 = tmpphoto.split("/");
        if(arr2[0].equals(null) ) {
            //default image setting
            //photo.setImageResource(R.drawable.s_pic_3);
            photo.setImageResource(R.color.colorDarkGray);
        }else{
            if(arr2.length > 1) {
                photo.setImageResource(Integer.parseInt(arr2[1]));
            }else{
                //default image setting
                //photo.setImageResource(R.drawable.s_pic_3);
                photo.setImageResource(R.color.colorDarkGray);
            }
        }

        if(position == firstitem){
            //Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.image_scaleup_animation);
            photo.setVisibility(View.VISIBLE);
            photo.setScaleType(ImageView.ScaleType.FIT_XY);
            //photo.startAnimation(animation);
        }else{
            photo.setVisibility(View.GONE);
        }


        return convertView;
    }

    public int getCommentNumber(int key){
        MyDBHandler handler = MyDBHandler.open((mContext));
        int result = handler.getCommentNumber("PROGRAMCOMMENT", key);
        return result;
    }

    public void setFirstitem(int position){
        firstitem = position;
    }

}

