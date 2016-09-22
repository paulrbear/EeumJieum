package dev.erica.hyunji.eeumjieum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class ProgramReportWorkerActivity extends FragmentActivity {

    private Button program_btn, observ_report_btn, work_report_btn, notice_btn, schedule_btn;
    private TextView notice_lb, schedule_lb, program_lb, observ_report_lb, work_report_lb;
    private Animation menuup, menudown;
    private int selected_menu = 0;

    private String savedID;
    private int savedMode;
    private int prev_first = 0;

    int cur_year, cur_month;

    ArrayList<ProgramArticleItem> data = new ArrayList<>();
    ProgramArticleListAdapter adapter;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_report_worker);

        //user mode and ID setting
        Intent intent = getIntent();
        savedMode = intent.getExtras().getInt("userMode");
        savedID = intent.getExtras().getString("userID");
        if(savedMode == 0) {
            SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
            savedID = pref.getString("id", "");
            savedMode = pref.getInt("mode", 0);
        }

        notice_btn = (Button) findViewById(R.id.notice_btn);
        schedule_btn = (Button) findViewById(R.id.schedule_btn);
        program_btn = (Button) findViewById(R.id.program_btn);
        observ_report_btn = (Button) findViewById(R.id.observ_report_btn);
        work_report_btn = (Button) findViewById(R.id.work_report_btn);

        notice_lb = (TextView) findViewById(R.id.notice_label);
        schedule_lb = (TextView) findViewById(R.id.schedule_label);
        program_lb = (TextView) findViewById(R.id.program_label);
        observ_report_lb = (TextView) findViewById(R.id.observ_report_label);
        work_report_lb = (TextView) findViewById(R.id.work_report_label);

        //bottom pop-menu
        menuup = AnimationUtils.loadAnimation(this, R.anim.note_up_animation);
        menudown = AnimationUtils.loadAnimation(this, R.anim.note_down_animation);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setColorNormalResId(R.color.colorRedOrange);
        fab.setColorPressedResId(R.color.colorBgDefault);


        listinit();

    }

    public void listinit(){

        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        ArrayList<ProgramArticleItem> articlelist;

        Calendar mCal = Calendar.getInstance();
        int year = mCal.get(mCal.YEAR);
        int month = mCal.get(mCal.MONTH) + 1;

        String day = year + "년 "  + month + "월";
        TextView tv = (TextView) findViewById(R.id.title_day_tv);
        tv.setText(day);
        cur_year = year;
        cur_month = month;

        String day2 = year + "/" + month;
        articlelist = handler.getProgramArticlebyDay(day2);
        ListView listView = (ListView) findViewById(R.id.articlelistview);


        Iterator iterator = articlelist.iterator();

        while (iterator.hasNext()){
            data.add((ProgramArticleItem) iterator.next());
        }

        adapter = new ProgramArticleListAdapter(this, R.layout.article_item3, data);
        listView.setAdapter(adapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setOnScrollListener(itemScrollistenerOfArticleList);
        listView.setOnItemClickListener(itemClickListenerOfArticleList);

    }

    public void updatelist(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        ArrayList<ProgramArticleItem> articlelist;

        String day2 = cur_year + "/" + cur_month;
        articlelist = handler.getProgramArticlebyDay(day2);

        data.clear();

        Iterator iterator = articlelist.iterator();

        while (iterator.hasNext()){
            data.add((ProgramArticleItem) iterator.next());
        }

        adapter.notifyDataSetChanged();
    }

    public void onClick_prevbtn(View v){
        TextView title_day_tv = (TextView)findViewById(R.id.title_day_tv);
        if(cur_month == 1){
            cur_year = cur_year - 1;
            cur_month = 12;

            String str = String.valueOf(cur_year) + "년 " + String.valueOf(cur_month) + "월";
            title_day_tv.setText(str);
        }else {
            cur_month = cur_month - 1;
            if(cur_month<10){
                String str = String.valueOf(cur_year) + "년 " + "0" + String.valueOf(cur_month) + "월";
                title_day_tv.setText(str);
            }else{
                String str = String.valueOf(cur_year) + "년 " +  String.valueOf(cur_month) + "월";
                title_day_tv.setText(str);
            }

        }
        updatelist();
    }

    public void onClick_nextbtn(View v){
        TextView title_day_tv = (TextView)findViewById(R.id.title_day_tv);

        if(cur_month == 12){
            cur_year = cur_year + 1;
            cur_month = 1;
            if(cur_month<10){
                String str =  String.valueOf(cur_year) + "년 " +"0" + String.valueOf(cur_month) + "월";
                title_day_tv.setText(str);
            }else{
                String str =  String.valueOf(cur_year) + "년 " + String.valueOf(cur_month) + "월";
                title_day_tv.setText(str);
            }
        }else {
            cur_month = cur_month + 1;
            if(cur_month<10){
                String str = String.valueOf(cur_year) + "년 " + "0" + String.valueOf(cur_month) + "월";
                title_day_tv.setText(str);
            }else{
                String str =  String.valueOf(cur_year) + "년 " + String.valueOf(cur_month) + "월";
                title_day_tv.setText(str);
            }
        }
        updatelist();
    }





    private AbsListView.OnScrollListener itemScrollistenerOfArticleList = new AbsListView.OnScrollListener(){

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            int tmpposition = view.getFirstVisiblePosition();
            if(tmpposition != prev_first) {
                adapter.setFirstitem(tmpposition);
                adapter.notifyDataSetChanged();
                prev_first = tmpposition;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    };

    private AdapterView.OnItemClickListener itemClickListenerOfArticleList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //String objectName = data.get(position).getObjectname();
            int articleKey = data.get(position).getArticleid();
            //Toast.makeText(getApplicationContext(), objectName, Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(getApplicationContext(), DetailProgramReportViewActivity.class);

            intent.putExtra("userID", savedID);
            intent.putExtra("articleKey", articleKey);

            startActivity(intent);
            overridePendingTransition(0,0);     //activity transition animation delete
        }
    };


    //fab button (wirte btn)
    public void onClick_fabbtn(View v){
        Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, WriteProgramReportActivity.class);
        intent.putExtra("userID",savedID);
        intent.putExtra("userMode", savedMode);
        intent.putExtra("mode", "program");
        startActivityForResult(intent,0);
        overridePendingTransition(0,0);     //activity transition animation delete

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 2:     //programreport registration complete
                updatelist();
                break;

            default:
                break;
        }
    }




    //back button click listner
    public void onClick_backbtn(View v){
        setResult(2);
        finish();
    }

    //bottom menu click listener
    public void onClick_messagebtn(View v) {
        Button temp_btn;
        switch (selected_menu) {
            case 0:
                break;
            case 1:
                setAnimation(1, View.GONE, menudown);       //messagebtn menu param = 1

                selected_menu = 0;
                temp_btn = (Button) findViewById(R.id.messagebtn);
                temp_btn.setBackgroundResource(R.drawable.message);

                return;
            case 2:
                temp_btn = (Button) findViewById(R.id.foodbtn);
                temp_btn.setBackgroundResource(R.drawable.food);
                break;
            case 3:
                temp_btn = (Button) findViewById(R.id.homebtn);
                temp_btn.setBackgroundResource(R.drawable.home);
                break;
            case 4:
                setAnimation(2, View.GONE, menudown);       //notebtn menu param = 2
                temp_btn = (Button) findViewById(R.id.notebtn);
                temp_btn.setBackgroundResource(R.drawable.note);
                break;
            case 5:
                temp_btn = (Button) findViewById(R.id.albumbtn);
                temp_btn.setBackgroundResource(R.drawable.album);
                break;
        }

        notice_btn.setVisibility(View.VISIBLE);
        schedule_btn.setVisibility(View.VISIBLE);
        notice_lb.setVisibility(View.VISIBLE);
        schedule_lb.setVisibility(View.VISIBLE);

        setAnimation(1, View.VISIBLE, menuup);          //messagebtn menu param = 1

        selected_menu = 1;
        temp_btn = (Button) findViewById(R.id.messagebtn);
        temp_btn.setBackgroundResource(R.drawable.message_click);

    }
    public void onClick_foodbtn(View v) {
        Button temp_btn;
        switch (selected_menu) {
            case 0:
                break;
            case 1:
                setAnimation(1, View.GONE, menudown);       //messagebtn menu param = 1
                temp_btn = (Button) findViewById(R.id.messagebtn);
                temp_btn.setBackgroundResource(R.drawable.message);
                break;
            case 2:

                break;
            case 3:
                temp_btn = (Button) findViewById(R.id.homebtn);
                temp_btn.setBackgroundResource(R.drawable.home);
                break;
            case 4:
                setAnimation(2, View.GONE, menudown);       //notebtn menu param = 2
                Button temp_bt = (Button) findViewById(R.id.notebtn);
                temp_bt.setBackgroundResource(R.drawable.note);
                break;
            case 5:
                temp_btn = (Button) findViewById(R.id.albumbtn);
                temp_btn.setBackgroundResource(R.drawable.album);
                break;
        }

        selected_menu = 2;
        temp_btn = (Button) findViewById(R.id.foodbtn);
        temp_btn.setBackgroundResource(R.drawable.food_click);

    }
    public void onClick_homebtn(View v) {
        Button temp_btn;
        switch (selected_menu) {
            case 0:
                break;
            case 1:
                setAnimation(1, View.GONE, menudown);       //messagebtn menu param = 1
                temp_btn = (Button) findViewById(R.id.messagebtn);
                temp_btn.setBackgroundResource(R.drawable.message);
                break;
            case 2:
                temp_btn = (Button) findViewById(R.id.foodbtn);
                temp_btn.setBackgroundResource(R.drawable.food);
                break;
            case 3:

                break;
            case 4:
                setAnimation(2, View.GONE, menudown);       //notebtn menu param = 2
                Button temp_bt = (Button) findViewById(R.id.notebtn);
                temp_bt.setBackgroundResource(R.drawable.note);
                break;
            case 5:
                temp_btn = (Button) findViewById(R.id.albumbtn);
                temp_btn.setBackgroundResource(R.drawable.album);
                break;
        }

        selected_menu = 3;
        temp_btn = (Button) findViewById(R.id.homebtn);
        temp_btn.setBackgroundResource(R.drawable.home_click);
    }
    public void onClick_notebtn(View v) {
        Button temp_btn;
        switch (selected_menu) {
            case 0:
                break;
            case 1:
                setAnimation(1, View.GONE, menudown);       //messagebtn menu param = 1
                temp_btn = (Button) findViewById(R.id.messagebtn);
                temp_btn.setBackgroundResource(R.drawable.message);
                break;
            case 2:
                temp_btn = (Button) findViewById(R.id.foodbtn);
                temp_btn.setBackgroundResource(R.drawable.food);
                break;
            case 3:
                temp_btn = (Button) findViewById(R.id.homebtn);
                temp_btn.setBackgroundResource(R.drawable.home);
                break;
            case 4:
                setAnimation(2, View.GONE, menudown);       //notebtn mode param = 2

                selected_menu = 0;
                temp_btn = (Button) findViewById(R.id.notebtn);
                temp_btn.setBackgroundResource(R.drawable.note);

                return;
            case 5:
                temp_btn = (Button) findViewById(R.id.albumbtn);
                temp_btn.setBackgroundResource(R.drawable.album);
                break;
        }

        selected_menu = 4;
        temp_btn = (Button) findViewById(R.id.notebtn);
        temp_btn.setBackgroundResource(R.drawable.note_click);

        if(savedMode == 1){ //parent
            program_btn.setVisibility(View.VISIBLE);
            observ_report_btn.setVisibility(View.VISIBLE);
            program_lb.setVisibility(View.VISIBLE);
            observ_report_lb.setVisibility(View.VISIBLE);
            setAnimation(2, View.VISIBLE, menuup);          //notebtn mode param = 2

        }else{  //worker
            program_btn.setVisibility(View.VISIBLE);
            observ_report_btn.setVisibility(View.VISIBLE);
            work_report_btn.setVisibility(View.VISIBLE);
            program_lb.setVisibility(View.VISIBLE);
            observ_report_lb.setVisibility(View.VISIBLE);
            work_report_lb.setVisibility(View.VISIBLE);
            setAnimation(2, View.VISIBLE, menuup);         //notebtn mode param = 2
        }

    }
    public void onClick_albumbtn(View v) {
        Button temp_btn;
        switch (selected_menu) {
            case 0:
                break;
            case 1:
                setAnimation(1, View.GONE, menudown);       //messagebtn menu param = 1
                temp_btn = (Button) findViewById(R.id.messagebtn);
                temp_btn.setBackgroundResource(R.drawable.message);
                break;
            case 2:
                temp_btn = (Button) findViewById(R.id.foodbtn);
                temp_btn.setBackgroundResource(R.drawable.food);
                break;
            case 3:
                temp_btn = (Button) findViewById(R.id.homebtn);
                temp_btn.setBackgroundResource(R.drawable.home);
                break;
            case 4:
                setAnimation(2, View.GONE, menudown);       //notebtn menu param = 2
                Button temp_bt = (Button) findViewById(R.id.notebtn);
                temp_bt.setBackgroundResource(R.drawable.note);
                break;
            case 5:
                break;
        }

        selected_menu = 5;
        temp_btn = (Button) findViewById(R.id.albumbtn);
        temp_btn.setBackgroundResource(R.drawable.album_click);


    }

    //bottom pop-menu click listener
    public void onClick_noticebtn(View v){
        Toast.makeText(getApplicationContext(), "open44", Toast.LENGTH_SHORT).show();

        Button tmp_btn = (Button) findViewById(R.id.notice_btn);
        tmp_btn.setSelected(true);
    }
    public void onClick_scheduletbtn(View v){
        Toast.makeText(getApplicationContext(), "open55", Toast.LENGTH_SHORT).show();


    }
    public void onClick_programbtn(View v){
        Toast.makeText(getApplicationContext(), "open", Toast.LENGTH_SHORT).show();
    }
    public void onClick_observreportbtn(View v){
        Toast.makeText(getApplicationContext(), "open22", Toast.LENGTH_SHORT).show();
        Button tmp_btn = (Button) findViewById(R.id.observ_report_btn);
        tmp_btn.setSelected(true);
    }
    public void onClick_workreportbtn(View v){
        Toast.makeText(getApplicationContext(), "open33", Toast.LENGTH_SHORT).show();

    }

    //bottom pop-menu animation
    private void setAnimation(final int menuNum, final int btnStatus, Animation animation){
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                /*if(btnStatus == View.VISIBLE) {
                    program_btn.setVisibility(btnStatus);
                    observ_report_btn.setVisibility(btnStatus);
                    work_report_btn.setVisibility(btnStatus);
                }*/
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(menuNum == 1 && btnStatus == View.GONE){
                    notice_btn.setVisibility(btnStatus);
                    schedule_btn.setVisibility(btnStatus);
                    notice_lb.setVisibility(btnStatus);
                    schedule_lb.setVisibility(btnStatus);
                }
                if(menuNum == 2 && btnStatus == View.GONE) {
                    program_btn.setVisibility(btnStatus);
                    observ_report_btn.setVisibility(btnStatus);
                    work_report_btn.setVisibility(btnStatus);
                    program_lb.setVisibility(btnStatus);
                    observ_report_lb.setVisibility(btnStatus);
                    work_report_lb.setVisibility(btnStatus);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if(menuNum == 1){
            notice_btn.startAnimation(animation);
            schedule_btn.startAnimation(animation);
            notice_lb.startAnimation(animation);
            schedule_lb.startAnimation(animation);

        }else if(menuNum ==2){
            if (savedMode == 2) {    //worker need work report menu
                work_report_btn.startAnimation(animation);
                work_report_lb.startAnimation(animation);
            }
            program_btn.startAnimation(animation);
            observ_report_btn.startAnimation(animation);
            program_lb.startAnimation(animation);
            observ_report_lb.startAnimation(animation);
        }
    }






}