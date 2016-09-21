package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Erica on 2016-09-14.
 */
public class CommentListAdapter extends BaseAdapter {
    private int layout;
    private LayoutInflater inflater;
    private ArrayList<ArticleCommentItem> mListData = new ArrayList<>();

    public CommentListAdapter(Context context, int layout, ArrayList<ArticleCommentItem> data) {
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

            ArticleCommentItem commentlistitem = mListData.get(position);

            TextView comment_writer = (TextView) convertView.findViewById(R.id.name);
            TextView comment = (TextView) convertView.findViewById(R.id.comtext);
            TextView comment_time = (TextView) convertView.findViewById(R.id.comtime);

            if(commentlistitem.getType() == 1){
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) comment_writer.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                comment_writer.setLayoutParams(params);

                params = (RelativeLayout.LayoutParams) comment.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                comment.setLayoutParams(params);

                comment.setBackgroundColor(Color.BLUE);

                params = (RelativeLayout.LayoutParams) comment_time.getLayoutParams();
                params.removeRule(RelativeLayout.RIGHT_OF);
                params.addRule(RelativeLayout.LEFT_OF, comment.getId());
                comment_time.setLayoutParams(params);
            }

            comment_writer.setText(commentlistitem.getWriter());
            comment.setText(commentlistitem.getComment_content());
            comment_time.setText(commentlistitem.getTime());

            System.out.println("Adapter entered");
            return convertView;
        }

}
