package dev.erica.hyunji.eeumjieum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class DetailObservReportViewActivity extends Activity {
    String savedID;
    int articleKey, mood, activity, sleep, photourl, totalPhotoNum;
    int selected_day;      //요일 1:sun
    int selected_date;  //날짜
    String article_writer, totalphotosurl;

    ArrayList<ArticleCommentItem> data = new ArrayList<>();
    CommentListAdapter adapter;

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
        setContentView(R.layout.activity_detail_observ_report_view);

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
        selected_date = intent.getExtras().getInt("selected_date");
        selected_day = intent.getExtras().getInt("selected_day");

        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        List<String> writer = handler.getObservArticleWriter(articleKey);

        // = handler.getLoginUserData(savedID, 2);

        TextView tmptv = (TextView) findViewById(R.id.main_article_writer);
        article_writer = writer.get(0);
        tmptv.setText(article_writer);      //name
        tmptv = (TextView) findViewById(R.id.main_article_writer_room);
        String tmp = writer.get(1) + " 생활재활교사";
        tmptv.setText(tmp);      //room
        ImageView tmpiv = (ImageView) findViewById(R.id.main_article_writer_img);
        tmp = writer.get(2);
        tmpiv.setImageResource(Integer.parseInt(tmp));      //imgsetting


        commentlistinit();

        initCalendar();
        viewinit();
    }

    public void onClick_backbtn(View v) {
        Toast.makeText(getApplicationContext(), "close", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void initCalendar() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.setTime(date);

        //cal.set(Calendar.YEAR, 2016);
        //cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
        //cal.set(Calendar.DATE, 30);


        int[] weekdate = {0, 0, 0, 0, 0, 0, 0, 0};

        //System.out.println("오늘 날짜 : " + cal.getTime());

        for (int i = 0; i < 7; i++) {
            cal.setTime(date);
            cal.add(Calendar.DATE, (i + 1) - cal.get(Calendar.DAY_OF_WEEK));
            //System.out.println(i + "번째 날짜 : " +    cal.getTime());
            weekdate[i] = cal.get(Calendar.DATE);
        }

        //cal.setTime(date);
        //cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK));
        //System.out.println("마지막 요일(토요일) 날짜 : " + cal.getTime());


        Button tmp_btn = (Button) findViewById(R.id.cal_year);
        tmp_btn.setText(Integer.toString(cal.get(Calendar.YEAR)));
        //System.out.println("올해 : " + cal.get(Calendar.YEAR));

        tmp_btn = (Button) findViewById(R.id.cal_month);
        if (cal.get(Calendar.MONTH) + 1 < 10) {
            tmp_btn.setText("0" + Integer.toString(cal.get(Calendar.MONTH) + 1));
        } else {
            tmp_btn.setText(Integer.toString(cal.get(Calendar.MONTH) + 1));
        }

        cal.setTime(date);

        tmp_btn = (Button) findViewById(R.id.cal_sun_3);
        tmp_btn.setText(Integer.toString(weekdate[0]));
        tmp_btn = (Button) findViewById(R.id.cal_mon_3);
        tmp_btn.setText(Integer.toString(weekdate[1]));
        tmp_btn = (Button) findViewById(R.id.cal_tue_3);
        tmp_btn.setText(Integer.toString(weekdate[2]));
        tmp_btn = (Button) findViewById(R.id.cal_wed_3);
        tmp_btn.setText(Integer.toString(weekdate[3]));
        tmp_btn = (Button) findViewById(R.id.cal_thu_3);
        tmp_btn.setText(Integer.toString(weekdate[4]));
        tmp_btn = (Button) findViewById(R.id.cal_fri_3);
        tmp_btn.setText(Integer.toString(weekdate[5]));
        tmp_btn = (Button) findViewById(R.id.cal_sat_3);
        tmp_btn.setText(Integer.toString(weekdate[6]));


        switch (selected_day) {
            case 1:
                tmp_btn = (Button) findViewById(R.id.cal_sun_1);
                tmp_btn.setBackgroundResource(R.color.colorRedOrange);
                tmp_btn = (Button) findViewById(R.id.cal_sun_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorRedOrange));
                tmp_btn = (Button) findViewById(R.id.cal_sun_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                break;
            case 2:
                tmp_btn = (Button) findViewById(R.id.cal_mon_1);
                tmp_btn.setBackgroundResource(R.color.colorRedOrange);
                tmp_btn = (Button) findViewById(R.id.cal_mon_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorRedOrange));
                tmp_btn = (Button) findViewById(R.id.cal_mon_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                break;
            case 3:
                tmp_btn = (Button) findViewById(R.id.cal_tue_1);
                tmp_btn.setBackgroundResource(R.color.colorRedOrange);
                tmp_btn = (Button) findViewById(R.id.cal_tue_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorRedOrange));
                tmp_btn = (Button) findViewById(R.id.cal_tue_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                break;
            case 4:
                tmp_btn = (Button) findViewById(R.id.cal_wed_1);
                tmp_btn.setBackgroundResource(R.color.colorRedOrange);
                tmp_btn = (Button) findViewById(R.id.cal_wed_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorRedOrange));
                tmp_btn = (Button) findViewById(R.id.cal_wed_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                break;
            case 5:
                tmp_btn = (Button) findViewById(R.id.cal_thu_1);
                tmp_btn.setBackgroundResource(R.color.colorRedOrange);
                tmp_btn = (Button) findViewById(R.id.cal_thu_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorRedOrange));
                tmp_btn = (Button) findViewById(R.id.cal_thu_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                break;
            case 6:
                tmp_btn = (Button) findViewById(R.id.cal_fri_1);
                tmp_btn.setBackgroundResource(R.color.colorRedOrange);
                tmp_btn = (Button) findViewById(R.id.cal_fri_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorRedOrange));
                tmp_btn = (Button) findViewById(R.id.cal_fri_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                break;
            case 7:
                tmp_btn = (Button) findViewById(R.id.cal_sat_1);
                tmp_btn.setBackgroundResource(R.color.colorRedOrange);
                tmp_btn = (Button) findViewById(R.id.cal_sat_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorRedOrange));
                tmp_btn = (Button) findViewById(R.id.cal_sat_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                break;
        }
    }

    public void viewinit() {
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        ObservArticleItem articlelist;
        ImageView tmp;

        articlelist = handler.getObservArticlebyID(articleKey);
        mood = articlelist.getMood();
        activity = articlelist.getActivity();
        sleep = articlelist.getSleep();
        TextView tfd = (TextView) findViewById(R.id.main_content_tv);
        tfd.setText(articlelist.getTfdcontent());
        totalphotosurl = articlelist.getPhoto();
        String arr[] = totalphotosurl.split("/");
        if ((arr[0]).equals("0")) {
            totalPhotoNum = Integer.parseInt(arr[0]);

            tmp = (ImageView) findViewById(R.id.main_image_view);
            //tmp.setLayoutParams(new LinearLayout.LayoutParams(800, 400));
            tmp.setScaleType(ImageView.ScaleType.FIT_XY);
            tmp.setImageResource(R.drawable.pic_2);     //default image

            Button tmpbtn = (Button) findViewById(R.id.countPhoto);
            tmpbtn.setVisibility(View.GONE);

        } else {
            totalPhotoNum = Integer.parseInt(arr[0]);
            photourl = Integer.parseInt(arr[1]);
            tmp = (ImageView) findViewById(R.id.main_image_view);
            //tmp.setLayoutParams(new LinearLayout.LayoutParams(300, 200));
            tmp.setScaleType(ImageView.ScaleType.FIT_XY);
            tmp.setImageResource(photourl);
        }

        Button tmpbtn = (Button) findViewById(R.id.countPhoto);
        tmpbtn.setText("+" + totalPhotoNum);
        //String tmpphoto = articlelist.getPhoto();
        //tmpphoto.

        switch (mood) {
            case 1:
                tmp = (ImageView) findViewById(R.id.mood_iv);
                tmp.setImageResource(R.drawable.status_good);
                break;
            case 2:
                tmp = (ImageView) findViewById(R.id.mood_iv);
                tmp.setImageResource(R.drawable.status_soso);
                break;
            case 3:
                tmp = (ImageView) findViewById(R.id.mood_iv);
                tmp.setImageResource(R.drawable.status_bad);
                break;
        }
        switch (activity) {
            case 1:
                tmp = (ImageView) findViewById(R.id.activity_iv);
                tmp.setImageResource(R.drawable.status_good);
                break;
            case 2:
                tmp = (ImageView) findViewById(R.id.activity_iv);
                tmp.setImageResource(R.drawable.status_soso);
                break;
            case 3:
                tmp = (ImageView) findViewById(R.id.activity_iv);
                tmp.setImageResource(R.drawable.status_bad);
                break;
        }
        switch (sleep) {
            case 1:
                tmp = (ImageView) findViewById(R.id.sleep_iv);
                tmp.setImageResource(R.drawable.status_good);
                break;
            case 2:
                tmp = (ImageView) findViewById(R.id.sleep_iv);
                tmp.setImageResource(R.drawable.status_soso);
                break;
            case 3:
                tmp = (ImageView) findViewById(R.id.sleep_iv);
                tmp.setImageResource(R.drawable.status_bad);
                break;
        }

    }

    public void commentlistinit() {
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        ArrayList<ArticleCommentItem> commlist;

        commlist = handler.getArticleCommentList("OBSERVECOMMENT", articleKey);
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

        commlist = handler.getArticleCommentList("OBSERVECOMMENT", articleKey);
        //ListView listView = (ListView) findViewById(R.id.commentlist);

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


        String commentContent = et.getText().toString();
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());

        int comnum = handler.getCommentNumber("OBSERVECOMMENT", articleKey);
        comnum = comnum + 1;

        List<String> tmpcomwriter = handler.getLoginUserData(savedID);
        String comm_writer = tmpcomwriter.get(0);
        String day = year + "/" + month + "/" + selected_date;
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        String time = hour + ":" + min;
        handler.insertComment("OBSERVECOMMENT", articleKey, day, time, comnum, comm_writer, commentContent);

        comment_update();


        et.setText("내용을 입력해주세요");
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);

    }


    public void onClick_countPhoto(View v){
        Intent intent = new Intent(this, ExtraImageActivity.class);
        intent.putExtra("photo",totalphotosurl);
        startActivity(intent);
        overridePendingTransition(0,0);     //activity transition animation delete
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




}
