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

import com.github.clans.fab.FloatingActionButton;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class DietActivity extends FragmentActivity {

    private Button program_btn, observ_report_btn, work_report_btn, notice_btn, schedule_btn;
    private TextView notice_lb, schedule_lb, program_lb, observ_report_lb, work_report_lb;
    private Animation menuup, menudown;
    private int selected_menu = 0;

    private String savedID;
    private int savedMode;

    ArrayList<DietListItem> data = new ArrayList<>();
    DietListAdapter adapter;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);


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
        fab.setColorNormalResId(R.color.colorMagenta);
        fab.setColorPressedResId(R.color.colorGray1);

        initlist();

    }

    public void initlist(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        ArrayList<DietListItem> articlelist;

        Calendar mCal = Calendar.getInstance();
        int year = mCal.get(mCal.YEAR);
        int month = mCal.get(mCal.MONTH) + 1;

        String day = year + "/" + month;

        articlelist = handler.getDietbyDay(day);
        ListView listView = (ListView) findViewById(R.id.diet_listview);

        Iterator iterator = articlelist.iterator();
        int i = 0;
        while (iterator.hasNext()){
            data.add((DietListItem) iterator.next());
        }

        adapter = new DietListAdapter(this, R.layout.diet_listview_item, data);
        listView.setAdapter(adapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(itemClickListenerOfDietList);
    }

    public void updateList(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        ArrayList<DietListItem> articlelist;

        Calendar mCal = Calendar.getInstance();
        int year = mCal.get(mCal.YEAR);
        int month = mCal.get(mCal.MONTH) + 1;

        String day = year + "/" + month;

        articlelist = handler.getDietbyDay(day);

        data.clear();

        Iterator iterator = articlelist.iterator();

        while (iterator.hasNext()){
            data.add((DietListItem) iterator.next());
        }
        adapter.notifyDataSetChanged();
    }

    private AdapterView.OnItemClickListener itemClickListenerOfDietList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //String objectName = data.get(position).getObjectname();
            int articleKey = data.get(position).getArticlekey();
            //Toast.makeText(getApplicationContext(), objectName, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), WriteDietActivity.class);

            intent.putExtra("userID", savedID);
            intent.putExtra("userMode", savedMode);
            intent.putExtra("articleKey", articleKey);
            intent.putExtra("mode", "detailview");

            startActivity(intent);
            overridePendingTransition(0,0);     //activity transition animation delete

        }
    };



    //fab button (wirte btn)
    public void onClick_fabbtn(View v){
        Intent intent = new Intent(this, WriteDietActivity.class);
        intent.putExtra("userID",savedID);
        intent.putExtra("userMode", savedMode);
        intent.putExtra("mode", "write");
        intent.putExtra("articleKey", 0);
        startActivityForResult(intent,0);
        overridePendingTransition(0,0);     //activity transition animation delete
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 7:     //diet registration complete
                updateList();
                break;
            default:
                break;
        }
    }


    public void onClick_calenderview(View v){
        Intent intent = new Intent(this, DietCalnedarViewActivity.class);
        intent.putExtra("userID",savedID);
        intent.putExtra("userMode", savedMode);

        startActivity(intent);
        overridePendingTransition(0,0);     //activity transition animation delete
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

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("userID",savedID);
        intent.putExtra("userMode", savedMode);
        startActivityForResult(intent,0);
        overridePendingTransition(0,0);     //activity transition animation delete
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

        Intent intent = new Intent(this, AlbumActivity.class);
        intent.putExtra("userID",savedID);
        intent.putExtra("userMode", savedMode);
        startActivityForResult(intent,0);
        overridePendingTransition(0,0);     //activity transition animation delete


    }

    //bottom pop-menu click listener
    public void onClick_noticebtn(View v){
        Button tmp_btn = (Button) findViewById(R.id.notice_btn);
        tmp_btn.setSelected(true);

        Intent intent = new Intent(this, NoticeAndScheduleActivity.class);
        intent.putExtra("userID",savedID);
        intent.putExtra("userMode", savedMode);
        intent.putExtra("mode", "notice");
        startActivityForResult(intent,0);
        overridePendingTransition(0,0);     //activity transition animation delete

    }
    public void onClick_scheduletbtn(View v){
        Button tmp_btn = (Button) findViewById(R.id.schedule_btn);
        tmp_btn.setSelected(true);

        Intent intent = new Intent(this, NoticeAndScheduleActivity.class);
        intent.putExtra("userID",savedID);
        intent.putExtra("userMode", savedMode);
        intent.putExtra("mode", "schedule");
        startActivityForResult(intent,0);
        overridePendingTransition(0,0);     //activity transition animation delete

    }
    public void onClick_programbtn(View v){
        Button tmp_btn = (Button) findViewById(R.id.program_btn);
        tmp_btn.setSelected(true);

        if(savedMode == 1){         //parent observ report activity

        }else if(savedMode == 2){   //worker observ report activity
            Intent intent = new Intent(this, ProgramReportWorkerActivity.class);
            intent.putExtra("userID",savedID);
            intent.putExtra("userMode", savedMode);
            startActivityForResult(intent,0);
            overridePendingTransition(0,0);     //activity transition animation delete
        }
    }
    public void onClick_observreportbtn(View v){
        Button tmp_btn = (Button) findViewById(R.id.observ_report_btn);
        tmp_btn.setSelected(true);

        if(savedMode == 1){         //parent observ report activity

        }else if(savedMode == 2){   //worker observ report activity
            Intent intent = new Intent(this, ObservReportWorkerActivity.class);
            intent.putExtra("userID",savedID);
            intent.putExtra("userMode", savedMode);
            startActivityForResult(intent,0);
            overridePendingTransition(0,0);     //activity transition animation delete
        }

    }
    public void onClick_workreportbtn(View v){
        Button tmp_btn = (Button) findViewById(R.id.work_report_btn);
        tmp_btn.setSelected(true);

        if(savedMode == 1){         //parent observ report activity

        }else if(savedMode == 2){   //worker observ report activity
            Intent intent = new Intent(this, WorkReportActivity.class);
            intent.putExtra("userID",savedID);
            intent.putExtra("userMode", savedMode);
            startActivityForResult(intent,0);
            overridePendingTransition(0,0);     //activity transition animation delete
        }

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

