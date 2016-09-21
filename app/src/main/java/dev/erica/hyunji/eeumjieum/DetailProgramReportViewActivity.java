package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class DetailProgramReportViewActivity extends FragmentActivity {
    String savedID;
    int articleKey;
    String article_writer;
    ArrayList<Integer> photo = new ArrayList<>();
    int currentimg = 0;

    ArrayList<ArticleCommentItem> data = new ArrayList<>();
    CommentListAdapter adapter;

    RelativeLayout temp_view;
    EditText et;
    InputMethodManager imm;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_program_report_view);

        temp_view = (RelativeLayout) findViewById(R.id.detail_view_mainview);
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


        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        ProgramArticleItem article = handler.getProgramArticlebyID(articleKey);
        // = handler.getLoginUserData(savedID, 2);

        TextView tmptv = (TextView) findViewById(R.id.writer_name_tv);
        article_writer = article.getWriter();
        tmptv.setText(article_writer);      //name
        tmptv = (TextView) findViewById(R.id.content_tv);
        tmptv.setText(article.getTfdcontent());
        tmptv = (TextView) findViewById(R.id.title_tv);
        tmptv.setText(article.getTitle());

        String tmpday = article.getDay();
        String[] arr = tmpday.split("/");
        int year = Integer.parseInt(arr[0]);
        int month = Integer.parseInt(arr[1]);
        int day = Integer.parseInt(arr[2]);
        String day_of_week = "";

        Date date = new Date();
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.setTime(date);

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DATE, day);
        int tmpdayofweek = cal.get(Calendar.DAY_OF_WEEK);
        switch (tmpdayofweek){
            case 1:
                day_of_week = "(일)";
                break;
            case 2:
                day_of_week = "(월)";
                break;
            case 3:
                day_of_week = "(화)";
                break;
            case 4:
                day_of_week = "(수)";
                break;
            case 5:
                day_of_week = "(목)";
                break;
            case 6:
                day_of_week = "(금)";
                break;
            case 7:
                day_of_week = "(토)";
                break;
        }
        tmpday = year + "년 " + month +"월 " + day + "일 " + day_of_week;
        tmptv = (TextView) findViewById(R.id.day_tv);
        tmptv.setText(tmpday);

        ImageView tmpiv = (ImageView) findViewById(R.id.writer_img);
        List<String> tmpuser = handler.getProgramArticleWriter(articleKey);
        String userimg = tmpuser.get(2);
        tmpiv.setImageResource(Integer.parseInt(userimg));      //imgsetting

        String photourl = article.getPhoto();
        String[] tmpphoto = photourl.split("/");
        if(tmpphoto.length > 1) {
            for (int i = 1; i < tmpphoto.length; i++) {
                photo.add(Integer.parseInt(tmpphoto[i]));
            }
        }
        if(photo.size() > 0) {
            tmpiv = (ImageView) findViewById(R.id.image);
            tmpiv.setVisibility(View.VISIBLE);
            tmpiv.setImageResource(photo.get(0));

            Button tmpbtn = (Button) findViewById(R.id.img_left_btn);
            tmpbtn.setVisibility(View.VISIBLE);

            tmpbtn = (Button) findViewById(R.id.img_right_btn);
            tmpbtn.setVisibility(View.VISIBLE);

        }

        commentlistinit();

    }

    public void onClick_prevbtn(View v){
        if(currentimg > 0){
            ImageView tmpiv = (ImageView) findViewById(R.id.image);
            tmpiv.setImageResource(photo.get(currentimg-1));
            currentimg = currentimg -1;
        }
    }

    public void onClick_nextbtn(View v){
        if(currentimg < photo.size()-1){
            ImageView tmpiv = (ImageView) findViewById(R.id.image);
            tmpiv.setImageResource(photo.get(currentimg+1));
            currentimg = currentimg +1;
        }
    }

    public void onClick_backbtn(View v){
        finish();
    }

    public void commentlistinit() {
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        ArrayList<ArticleCommentItem> commlist;

        commlist = handler.getArticleCommentList("PROGRAMCOMMENT", articleKey);
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

        commlist = handler.getArticleCommentList("PROGRAMCOMMENT", articleKey);
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
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);


        String commentContent = et.getText().toString();
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());

        int comnum = handler.getCommentNumber("PROGRAMCOMMENT", articleKey);
        comnum = comnum + 1;

        List<String> tmpcomwriter = handler.getLoginUserData(savedID);
        String comm_writer = tmpcomwriter.get(0);
        String stday = year + "/" + month + "/" + day;
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        String time = hour + ":" + min;
        handler.insertComment("PROGRAMCOMMENT", articleKey, stday, time, comnum, comm_writer, commentContent);

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

}
