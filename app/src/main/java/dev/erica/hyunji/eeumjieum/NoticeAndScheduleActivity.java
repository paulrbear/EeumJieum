package dev.erica.hyunji.eeumjieum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class NoticeAndScheduleActivity extends FragmentActivity {
    private String mode;        //tab mode notice/ schedule
    private String savedID;
    private int savedMode;  //usermode
    private TextView month_tv;
    private GridAdapter gridAdapter;
    private ArrayList<String> dayList;
    private ArrayList<Boolean> ex_article;
    private GridView gridView;
    private Calendar mCal;

    private int current_year, current_month;

    private Button program_btn, observ_report_btn, work_report_btn, notice_btn, schedule_btn, tab1btn, tab2btn;
    private TextView notice_lb, schedule_lb, program_lb, observ_report_lb, work_report_lb;
    private Animation menuup, menudown;
    private int selected_menu = 0;      //bottom navi tab

    ArrayList<ScheduleListItem> schedule_data = new ArrayList<>();
    ScheduleListAdapter sadapter;
    ArrayList<NoticeListItem> notice_data = new ArrayList<>();
    NoticeListAdapter nadapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_and_schedule);

        Intent intent = getIntent();
        savedMode = intent.getExtras().getInt("userMode");
        savedID = intent.getExtras().getString("userID");
        if(savedMode == 0) {
            SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
            savedID = pref.getString("id", "");
            savedMode = pref.getInt("mode", 0);
        }



        mode = intent.getExtras().getString("mode");

        month_tv = (TextView) findViewById(R.id.month_tv);
        gridView = (GridView) findViewById(R.id.gridview);

        initcalendarView();
        initScehduleList();
        initNoticelist();

        tab1btn = (Button) findViewById(R.id.tab1_btn);
        tab2btn = (Button) findViewById(R.id.tab2_btn);

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




        setTabView();

    }

    public void initNoticelist(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        ArrayList<NoticeListItem> articlelist;

        Calendar mCal = Calendar.getInstance();
        int year = mCal.get(mCal.YEAR);
        int month = mCal.get(mCal.MONTH) + 1;

        String day = year + "/" + month;

        articlelist = handler.getNoticebyDay(day);
        ListView listView = (ListView) findViewById(R.id.firsttab_listview);

        Iterator iterator = articlelist.iterator();

        while (iterator.hasNext()){
            notice_data.add((NoticeListItem) iterator.next());
        }

        nadapter = new NoticeListAdapter(this, R.layout.notice_list_item, notice_data);
        listView.setAdapter(nadapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(itemClickListenerOfNoticeList);

    }

    public void updateNoticelist(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        ArrayList<NoticeListItem> articlelist;

        Calendar mCal = Calendar.getInstance();
        int year = mCal.get(mCal.YEAR);
        int month = mCal.get(mCal.MONTH) + 1;

        String day = year + "/" + month;

        articlelist = handler.getNoticebyDay(day);

        notice_data.clear();

        Iterator iterator = articlelist.iterator();

        while (iterator.hasNext()){
            notice_data.add((NoticeListItem) iterator.next());
        }
        nadapter.notifyDataSetChanged();
    }

    public void initScehduleList(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        ArrayList<ScheduleListItem> articlelist;

        Calendar mCal = Calendar.getInstance();
        int year = mCal.get(Calendar.YEAR);
        int month = mCal.get(Calendar.MONTH)+1;

        current_year = year;
        current_month = month;

        String day = year + "/" + month;

        articlelist = handler.getSchedulebyDay(day);
        ListView listView = (ListView) findViewById(R.id.schedule_listview);


        Iterator iterator = articlelist.iterator();

        while (iterator.hasNext()){
            schedule_data.add((ScheduleListItem) iterator.next());
        }

        sadapter = new ScheduleListAdapter(this, R.layout.schedule_list_item, schedule_data);
        listView.setAdapter(sadapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(itemClickListenerOfScheduleList);
    }

    public void updateScheduleList(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        ArrayList<ScheduleListItem> articlelist;


        int year = current_year;
        int month = current_month;

        String day = year + "/" + month;

        articlelist = handler.getSchedulebyDay(day);

        schedule_data.clear();

        Iterator iterator = articlelist.iterator();

        while (iterator.hasNext()){
            schedule_data.add((ScheduleListItem) iterator.next());
        }

        sadapter.notifyDataSetChanged();
    }


    private AdapterView.OnItemClickListener itemClickListenerOfNoticeList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //String objectName = data.get(position).getObjectname();
            int articleKey = notice_data.get(position).getArticlekey();
            //Toast.makeText(getApplicationContext(), objectName, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), DetailScheduleViewActivity.class);

            intent.putExtra("userID", savedID);
            intent.putExtra("articleKey", articleKey);
            intent.putExtra("mode", "notice");

            startActivity(intent);
            overridePendingTransition(0,0);     //activity transition animation delete

        }
    };


    private AdapterView.OnItemClickListener itemClickListenerOfScheduleList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //String objectName = data.get(position).getObjectname();
            int articleKey = schedule_data.get(position).getArticlekey();
            //Toast.makeText(getApplicationContext(), objectName, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), DetailScheduleViewActivity.class);

            intent.putExtra("userID", savedID);
            intent.putExtra("articleKey", articleKey);
            intent.putExtra("mode", "schedule");

            startActivity(intent);
            overridePendingTransition(0,0);     //activity transition animation delete
        }
    };


    public void onClick_tab1(View v){
        mode = "notice";
        setTabView();
        updateNoticelist();
    }

    public void onClick_tab2(View v){
        mode = "schedule";
        setTabView();
        updateScheduleList();
    }

    public void setTabView(){
        RelativeLayout tmprl = (RelativeLayout) findViewById(R.id.second_tab_relativelayout);
        ListView tmpll = (ListView) findViewById(R.id.firsttab_listview);

        if(mode.equals("schedule")){
            tab1btn.setTextColor(getResources().getColor(R.color.colorBackgroundGray));
            tab2btn.setTextColor(getResources().getColor(R.color.colorSelectFont));
            tmpll.setVisibility(View.GONE);
            tmprl.setVisibility(View.VISIBLE);
            //sadapter.notifyDataSetChanged();

        }else{
            tab1btn.setTextColor(getResources().getColor(R.color.colorSelectFont));
            tab2btn.setTextColor(getResources().getColor(R.color.colorBackgroundGray));
            tmpll.setVisibility(View.VISIBLE);
            tmprl.setVisibility(View.GONE);
            //nadapter.notifyDataSetChanged();
        }

    }

    public void initcalendarView(){

        mCal = Calendar.getInstance();

        int year = mCal.get(Calendar.YEAR);
        int month = mCal.get(Calendar.MONTH)+1;
        current_year = year;
        current_month = month;
        if(month < 10){
            String str = "0" + month + "월";
            month_tv.setText(str);
        }else{
            String str = month + "월";
            month_tv.setText(str);
        }

        dayList = new ArrayList<>();
        ex_article = new ArrayList<>();

        setCalendarDate(year, month);
        gridAdapter = new GridAdapter(getApplicationContext(), dayList, ex_article);
        gridView.setAdapter(gridAdapter);

    }

    private void setCalendarDate(int year, int month) {

        MyDBHandler handler = MyDBHandler.open(getApplicationContext());

        if(month < 10){
            String str = "0" + month + "월";
            month_tv.setText(str);
        }else{
            String str = month + "월";
            month_tv.setText(str);
        }

        dayList.clear();
        ex_article.clear();

        //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)


        //gridview 요일 표시

        dayList.add("일");
        ex_article.add(false);
        dayList.add("월");
        ex_article.add(false);
        dayList.add("화");
        ex_article.add(false);
        dayList.add("수");
        ex_article.add(false);
        dayList.add("목");
        ex_article.add(false);
        dayList.add("금");
        ex_article.add(false);
        dayList.add("토");
        ex_article.add(false);


        mCal.set(year,month-1,1);
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
            ex_article.add(false);
        }

        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            int day = i + 1;
            String strday = year + "/" + month + "/" + day;
            boolean tmpbool = handler.isScheduleExist(strday);
            dayList.add(String.valueOf(day));
            ex_article.add(tmpbool);
        }
    }

    public void onClick_prevbtn(View v){
        if(current_month == 1){
            current_year = current_year - 1;
            current_month = 12;
            setCalendarDate(current_year, current_month);
            String str = String.valueOf(current_month) + "월";
            month_tv.setText(str);
        }else {
            current_month = current_month - 1;
            setCalendarDate(current_year, current_month);
            if(current_month<10){
                String str ="0" + String.valueOf(current_month) + "월";
                month_tv.setText(str);
            }else{
                String str = String.valueOf(current_month) + "월";
                month_tv.setText(str);
            }

        }
        updateScheduleList();
        gridAdapter.notifyDataSetChanged();
    }

    public void onClick_nextbtn(View v){
        if(current_month == 12){
            current_year = current_year + 1;
            current_month = 1;
            setCalendarDate(current_year, current_month);
            if(current_month<10){
                String str ="0" + String.valueOf(current_month) + "월";
                month_tv.setText(str);
            }else{
                String str = String.valueOf(current_month) + "월";
                month_tv.setText(str);
            }
        }else {
            current_month = current_month + 1;
            setCalendarDate(current_year, current_month);
            if(current_month<10){
                String str ="0" + String.valueOf(current_month) + "월";
                month_tv.setText(str);
            }else{
                String str = String.valueOf(current_month) + "월";
                month_tv.setText(str);
            }
        }
        updateScheduleList();
        gridAdapter.notifyDataSetChanged();
    }


    private class GridAdapter extends BaseAdapter{
        private List<String> list;
        private List<Boolean> article_exist;
        private LayoutInflater inflater;

        private class ViewHolder {
            TextView day_tv;
            ImageView ex_article_iv;
        }


        public GridAdapter(Context context, List<String> list, List<Boolean> article_exist){
            this.list = list;
            this.article_exist = article_exist;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                convertView = inflater.inflate(R.layout.calendar_gridview_item, parent, false);
                holder = new ViewHolder();

                holder.day_tv = (TextView) convertView.findViewById(R.id.gridview_day_tv);
                holder.ex_article_iv = (ImageView) convertView.findViewById(R.id.article_exist_iv);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            String tmpstr = "" + getItem(position);
            holder.day_tv.setText(tmpstr);
            if(article_exist.get(position)){
                holder.ex_article_iv.setImageResource(R.drawable.shape_oval_cyan);
            }else{
                holder.ex_article_iv.setImageDrawable(null);
            }

            holder.day_tv.setTextColor(getResources().getColor(R.color.colorTitleBlack));
            holder.day_tv.setBackground(null);


            mCal = Calendar.getInstance();
            //오늘 day 가져옴
            if( (mCal.get(Calendar.YEAR) == current_year) && ( mCal.get(Calendar.MONTH) == (current_month-1) )  ) {
                Integer today = mCal.get(Calendar.DAY_OF_MONTH);
                String sToday = String.valueOf(today);
                if (sToday.equals(getItem(position))) { //오늘 day 텍스트 컬러 변경
                    holder.day_tv.setTextColor(Color.WHITE);
                    holder.day_tv.setBackgroundResource(R.drawable.shape_oval_photo_selected);

                }
            }

            return convertView;

        }
    }

    //fab button (wirte btn)
    public void onClick_fabbtn(View v){
        if(mode.equals("schedule")) {
            Intent intent = new Intent(this, WriteScheduleActivity.class);
            intent.putExtra("userID", savedID);
            intent.putExtra("userMode", savedMode);
            startActivityForResult(intent,0);
            overridePendingTransition(0, 0);     //activity transition animation delete
        }else if(mode.equals("notice")){
            Intent intent = new Intent(this, WriteNoticeActivity.class);
            intent.putExtra("userID", savedID);
            intent.putExtra("userMode", savedMode);
            startActivityForResult(intent,0);
            overridePendingTransition(0, 0);     //activity transition animation delete
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 4:     //schedule registration complete
                updateScheduleList();
                break;
            case 5:     //notice registration complete
                updateNoticelist();
            default:
                break;
        }
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

        Intent intent = new Intent(this, DietActivity.class);
        intent.putExtra("userID",savedID);
        intent.putExtra("userMode", savedMode);
        startActivityForResult(intent,0);
        overridePendingTransition(0,0);     //activity transition animation delete

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
