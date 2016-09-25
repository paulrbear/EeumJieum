package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class DetailScheduleViewActivity extends FragmentActivity {

    String savedID, mode;
    int articleKey;

    ArrayList<ArticleCommentItem> data = new ArrayList<>();
    CommentListAdapter adapter;

    String article_writer, writer_room, tfdcontent, article_title;
    String article_day, article_location;
    int totalPhotoNum;

    FrameLayout temp_view;
    EditText et;
    InputMethodManager imm;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_schedule_view);


        temp_view = (FrameLayout)findViewById(R.id.detail_view_mainview);
        temp_view.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        break;
                }
                return false;
            }
        });

        //if edit text touched, erase default text
        et = (EditText)findViewById(R.id.comment_et);
        et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        EditText tmp_et = (EditText) findViewById(R.id.comment_et);
                        tmp_et.setText("");
                        tmp_et.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        break;
                }
                return false;
            }
        });

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        //user mode and ID setting
        Intent intent = getIntent();
        savedID = intent.getExtras().getString("userID");
        articleKey = intent.getExtras().getInt("articleKey");
        mode = intent.getExtras().getString("mode");

        initView();

        commentlistinit();


    }


    public void initView(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        List<String> writer = null;

        if(mode.equals("schedule")) {

            writer = handler.getScheduleWriter(articleKey);

            ScheduleListItem articlelist;
            ImageView tmp;

            articlelist = handler.getSchedulebyID(articleKey);
            TextView tfd = (TextView) findViewById(R.id.main_content_tv);
            tfd.setText(articlelist.getContent());
            String totalphotosurl = articlelist.getPhoto();
            String arr[] = totalphotosurl.split("/");
            if ((arr[0]).equals("0")) {
                totalPhotoNum = Integer.parseInt(arr[0]);
                tmp = (ImageView) findViewById(R.id.main_image_view);
                tmp.setScaleType(ImageView.ScaleType.FIT_XY);
                tmp.setImageResource(R.color.colorDarkGray);     //default image
            } else {
                totalPhotoNum = Integer.parseInt(arr[0]);
                int photourl = Integer.parseInt(arr[1]);
                tmp = (ImageView) findViewById(R.id.main_image_view);
                tmp.setScaleType(ImageView.ScaleType.FIT_XY);
                tmp.setImageResource(photourl);
            }

            tfd = (TextView) findViewById(R.id.location_tv);
            tfd.setText(articlelist.getLocation());
            tfd = (TextView) findViewById(R.id.day_tv);

            String strtmp = articlelist.getDay();
            String[] arrtmp = strtmp.split("/");
            article_day = arrtmp[1] +"월 "+ arrtmp[2] + "일";
            strtmp = articlelist.getTime();
            arrtmp = strtmp.split(":");
            int hour = Integer.parseInt(arrtmp[0]);
            if(hour > 12){
                int kk = hour -12;
                article_day = article_day + " 오후 " + kk + "시";
            }else{
                article_day = article_day + " 오전 " + hour + "시";
            }

            tfd.setText(article_day);
            tfd = (TextView) findViewById(R.id.title_tv);
            tfd.setText(articlelist.getTitle());


            TextView tmptv = (TextView) findViewById(R.id.small_title);
            SpannableString content = new SpannableString("시설일정");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            tmptv.setText(content);

        }else{

            writer = handler.getNoticeWriter(articleKey);

            NoticeListItem articlelist = handler.getNoticebyID(articleKey);
            ImageView tmp;


            TextView tfd = (TextView) findViewById(R.id.main_content_tv);
            tfd.setText(articlelist.getContent());
            String totalphotosurl = articlelist.getPhoto();
            String arr[] = totalphotosurl.split("/");
            if ((arr[0]).equals("0")) {
                totalPhotoNum = Integer.parseInt(arr[0]);
                tmp = (ImageView) findViewById(R.id.main_image_view);
                tmp.setScaleType(ImageView.ScaleType.FIT_XY);
                tmp.setImageResource(R.color.colorDarkGray);     //default image
            } else {
                totalPhotoNum = Integer.parseInt(arr[0]);
                int photourl = Integer.parseInt(arr[1]);
                tmp = (ImageView) findViewById(R.id.main_image_view);
                tmp.setScaleType(ImageView.ScaleType.FIT_XY);
                tmp.setImageResource(photourl);
            }

            tfd = (TextView) findViewById(R.id.location_tv);
            tfd.setVisibility(View.GONE);
            tfd = (TextView) findViewById(R.id.day_tv);

            String strtmp = articlelist.getDay();
            String[] arrtmp = strtmp.split("/");
            int month = Integer.parseInt(arrtmp[1]);
            int intday = Integer.parseInt(arrtmp[2]);

            if(month < 10){
                article_day = "0" + month;
            }else{
                article_day = String.valueOf(month);
            }

            if(intday < 10){
                article_day = article_day + ".0" + intday;
            }else{
                article_day = article_day + "." + String.valueOf(intday);
            }

            tfd.setText(article_day);
            tfd = (TextView) findViewById(R.id.title_tv);
            tfd.setText(articlelist.getTitle());

            TextView tmptv = (TextView) findViewById(R.id.small_title);
            SpannableString content = new SpannableString("시설공지");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            tmptv.setText(content);
            tmptv = (TextView) findViewById(R.id.location_et);
            if(tmptv != null) {
                tmptv.setVisibility(View.GONE);
            }
        }


        TextView tmptv = (TextView) findViewById(R.id.main_article_writer);
        article_writer = writer.get(0);
        tmptv.setText(article_writer);      //name
        tmptv = (TextView) findViewById(R.id.main_article_writer_room);
        String tmp = writer.get(1) + " 생활재활교사";
        tmptv.setText(tmp);      //room
        ImageView tmpiv = (ImageView) findViewById(R.id.main_article_writer_img);
        tmp = writer.get(2);
        tmpiv.setImageResource(Integer.parseInt(tmp));      //imgsetting

    }


    public void commentlistinit() {
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        ArrayList<ArticleCommentItem> commlist;


        if(mode.equals("schedule")) {
            commlist = handler.getArticleCommentList("SCHEDULECOMMENT", articleKey);
        }else{
            commlist = handler.getArticleCommentList("NOTICECOMMENT", articleKey);
        }
        ListView listView = (ListView) findViewById(R.id.commentlist);
        TextView commentcount = (TextView) findViewById(R.id.comment_num_tv);
        commentcount.setText(""+commlist.size());

        Iterator iterator = commlist.iterator();
        while (iterator.hasNext()) {

            ArticleCommentItem tmpresult = (ArticleCommentItem) iterator.next();
            String comm_writer = tmpresult.getWriter();
            List<String> loginuser_data = handler.getLoginUserData(savedID);
            String loginuser_name = loginuser_data.get(0);


            if (loginuser_name.equals(comm_writer)) {
                ArticleCommentItem tmp = new ArticleCommentItem(tmpresult.getArticleKey(), tmpresult.getDay(), tmpresult.getTime(), tmpresult.getWriter(), tmpresult.getComment_content(), 1);
                data.add(tmp);
            } else {
                ArticleCommentItem tmp = new ArticleCommentItem(tmpresult.getArticleKey(), tmpresult.getDay(), tmpresult.getTime(), tmpresult.getWriter(), tmpresult.getComment_content(), 2);
                data.add(tmp);
            }
        }

        adapter = new CommentListAdapter(this, R.layout.comment_not_me_item, data);
        listView.setAdapter(adapter);
        listView.setVerticalScrollBarEnabled(false);
        setListViewHeightBasedOnChildren(adapter, listView);

    }

    public void comment_update() {
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        ArrayList<ArticleCommentItem> commlist;

        if(mode.equals("schedule")) {
            commlist = handler.getArticleCommentList("SCHEDULECOMMENT", articleKey);
            //ListView listView = (ListView) findViewById(R.id.commentlist);
        }else{
            commlist = handler.getArticleCommentList("NOTICECOMMENT", articleKey);
        }

        TextView commentcount = (TextView) findViewById(R.id.comment_num_tv);
        commentcount.setText(""+commlist.size());

        Iterator iterator = commlist.iterator();
        while (iterator.hasNext()) {

            ArticleCommentItem tmpresult = (ArticleCommentItem) iterator.next();
            String comm_writer = tmpresult.getWriter();
            List<String> loginuser_data = handler.getLoginUserData(savedID);
            String loginuser_name = loginuser_data.get(0);



            if (loginuser_name.equals(comm_writer)) {
                ArticleCommentItem tmp = new ArticleCommentItem(tmpresult.getArticleKey(), tmpresult.getDay(), tmpresult.getTime(), tmpresult.getWriter(), tmpresult.getComment_content(), 1);
                data.add(tmp);
            } else {
                ArticleCommentItem tmp = new ArticleCommentItem(tmpresult.getArticleKey(), tmpresult.getDay(), tmpresult.getTime(), tmpresult.getWriter(), tmpresult.getComment_content(), 2);
                data.add(tmp);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void onClick_commentbtn(View v) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.setTime(date);


        int year = cal.get(Calendar.YEAR);
        //System.out.println("올해 : " + cal.get(Calendar.YEAR));


        int month = cal.get(Calendar.MONTH) + 1;
        int intday = cal.get(Calendar.DAY_OF_MONTH);

        String commentContent = et.getText().toString();
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        int comnum = 0;
        if(mode.equals("schedule")) {
            comnum = handler.getCommentNumber("SCHEDULECOMMENT", articleKey);
            comnum = comnum + 1;
        }else{
            comnum = handler.getCommentNumber("NOTICECOMMENT", articleKey);
            comnum = comnum + 1;
        }

        List<String> tmpcomwriter = handler.getLoginUserData(savedID);
        String comm_writer = tmpcomwriter.get(0);
        String day = year + "/" + month + "/" + intday;
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        String time = hour + ":" + min;

        if(mode.equals("schedule")) {
            handler.insertComment("SCHEDULECOMMENT", articleKey, day, time, comnum, comm_writer, commentContent);
        }else{
            handler.insertComment("NOTICECOMMENT", articleKey, day, time, comnum, comm_writer, commentContent);
        }
        comment_update();

        et.setText("내용을 입력해주세요");
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    public static void setListViewHeightBasedOnChildren(BaseAdapter listAdapter, ListView listView) {

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);


    }

    public void onClick_backbtn(View v){
        finish();
    }

}
