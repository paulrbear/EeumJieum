package dev.erica.hyunji.eeumjieum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class SearchObservReportActivity extends FragmentActivity {
    private int selected_menu = 0;
    private Animation menuup, menudown;
    private Button program_btn, observ_report_btn, work_report_btn, notice_btn, schedule_btn;
    private TextView notice_lb, schedule_lb, program_lb, observ_report_lb, work_report_lb;

    private String savedID, objectName, selectedRoom;
    private int savedMode;


    ArrayList<ObservArticleItem> data = new ArrayList<>();
    ObservArticleListAdapter2 adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_observ_report);

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


        Intent intent = getIntent();
        objectName = intent.getExtras().getString("objectName");
        savedID = intent.getExtras().getString("userID");
        savedMode = intent.getExtras().getInt("userMode");
        selectedRoom = intent.getExtras().getString("selectedRoom");

        listinit();

    }


    public void listinit(){

        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        RoomUserItem userdata;

        userdata = handler.getRoomUserImage(objectName, selectedRoom);

        if(userdata != null){
            ImageView iv  = (ImageView) findViewById(R.id.userimg);
            iv.setImageResource(userdata.getuImg());
            TextView tv = (TextView) findViewById(R.id.username);
            String tmp = objectName + " 가족";
            tv.setText(tmp);
            tv = (TextView) findViewById(R.id.userroom);
            tmp = " | " + selectedRoom;
            tv.setText(tmp);
        }


        ArrayList<ObservArticleItem> articlelist;
        articlelist = handler.getObservArticlebyObjectRoom(objectName, selectedRoom);
        ListView listView = (ListView) findViewById(R.id.articlelv);


      Iterator iterator = articlelist.iterator();

      while (iterator.hasNext()){
          data.add((ObservArticleItem) iterator.next());

      }

      adapter = new ObservArticleListAdapter2(this, R.layout.article_item2, data);
      listView.setAdapter(adapter);
      listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
      listView.setOnItemClickListener(itemClickListenerOfArticleList);

    }

    private AdapterView.OnItemClickListener itemClickListenerOfArticleList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //String objectName = data.get(position).getObjectname();
            int articleKey = data.get(position).getArticleid();
            String tmp = data.get(position).getDay();
            String[] arr = tmp.split("/");
            int year = Integer.parseInt(arr[0]);
            int month = Integer.parseInt(arr[1]);
            int selected_date = Integer.parseInt(arr[2]);

            Date date = new Date();
            Calendar cal = Calendar.getInstance(Locale.KOREA);
            cal.setTime(date);

            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month-1);
            cal.set(Calendar.DATE, selected_date);
            int selected_day = cal.get(Calendar.DAY_OF_WEEK);

            //Toast.makeText(getApplicationContext(), objectName, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), DetailObservReportViewActivity.class);


            intent.putExtra("userID", savedID);
            intent.putExtra("articleKey", articleKey);
            intent.putExtra("selected_day", selected_day);
            intent.putExtra("selected_date", selected_date);

            startActivity(intent);
            overridePendingTransition(0,0);     //activity transition animation delete*/
        }
    };








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
