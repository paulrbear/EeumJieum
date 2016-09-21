package dev.erica.hyunji.eeumjieum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class HomeActivity extends Activity {

    //private final String[] navItems = {"Brown", "Cadet Blue", "Dark Olive Green", "Dark Orange", "Golden Rod"};
    private ListView lvNavList;
    private ListViewAdapter mAdapter;
    private FrameLayout flContainer;
    private DrawerLayout dlDrawer;

    private Button btn, program_btn, observ_report_btn, work_report_btn, notice_btn, schedule_btn;
    private TextView notice_lb, schedule_lb, program_lb, observ_report_lb, work_report_lb;
    private Animation menuup, menudown, fastmenudown;
    private int selected_menu = 0;

    private String savedID;
    private int savedMode;
    String userName;
    String userRoom;
    int imgurl;


    ArrayList<ViewPagerItem> data = new ArrayList<>();
    private static ViewPager mPager;
    private static ViewPagerAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // left menu init and set the menu
        lvNavList = (ListView) findViewById(R.id.lv_activity_main_nav_list);
        flContainer = (FrameLayout) findViewById(R.id.fl_activity_main_container);
        btn = (Button) findViewById(R.id.menubtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "open", Toast.LENGTH_SHORT).show();
                dlDrawer.openDrawer(lvNavList);
            }
        });

        dlDrawer = (DrawerLayout) findViewById(R.id.dl_activity_main_drawer);
        mAdapter = new ListViewAdapter(this);
        lvNavList.setAdapter(mAdapter);


        //user mode and ID setting
        Intent intent = getIntent();
        savedMode = intent.getExtras().getInt("userMode");
        savedID = intent.getExtras().getString("userID");
        if(savedMode == 0) {
            SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
            savedID = pref.getString("id", "");
            savedMode = pref.getInt("mode", 0);
        }

        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        List<String> dbresult = handler.getLoginUserData(savedID, savedMode);
        if(dbresult.size() > 0) {
            String tmpimgurl = dbresult.get(2);
            imgurl = Integer.parseInt(tmpimgurl);
            userName = dbresult.get(0);
            userRoom = dbresult.get(1);
            mAdapter.addItem(getResources().getDrawable(imgurl), userName, userRoom, null);
        }else{
            mAdapter.addItem(getResources().getDrawable(R.drawable.uimg_park), "default", "default", null);
        }

        mAdapter.addItem(null, null, null, "내정보");
        mAdapter.addItem(null, null, null, "로그아웃");
        mAdapter.addItem(null, null, null, "시설 설정");
        mAdapter.addItem(null, null, null, "환경설정");

        //lvNavList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
        lvNavList.setOnItemClickListener(new DrawerItemClickListener());

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

        fastmenudown =  AnimationUtils.loadAnimation(this, R.anim.fast_note_down_animation);


        initViewPagerData();

        mPager = (ViewPager) findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(this, data);
        mPager.setAdapter(adapter);
    }

    public void initViewPagerData(){

        ArrayList<ProgramArticleItem> p_list = new ArrayList<>();

        Calendar mCal = Calendar.getInstance();
        int year = mCal.get(mCal.YEAR);
        int month = mCal.get(mCal.MONTH) + 1;
        int intday = mCal.get(mCal.DAY_OF_MONTH);

        String day = year + "/" + month + "/" + intday;

        noticeArticleAdd(day);
        scheduleArticleAdd(day);
        //observereport and program report should be selected by user mode
        if(savedMode == 1){//parent
            observereportArticleAdd(savedID, day);
        }else{
            observereportArticleAdd(day);
        }
        Toast.makeText(getApplicationContext(),"total item : " + data.size(), Toast.LENGTH_SHORT).show();

    }

    public void noticeArticleAdd(String day){
        ArrayList<NoticeListItem> n_list = new ArrayList<>();
        ViewPagerItem tmp;
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());

        n_list = handler.getNoticebyDay(day);
        Iterator iterator = n_list.iterator();
        while (iterator.hasNext()){
            NoticeListItem ntmp = (NoticeListItem) iterator.next();
            int artkey = ntmp.getArticlekey();
            int photo;
            String tmpphoto = ntmp.getPhoto();
            String[] arr2 = tmpphoto.split("/");
            if(arr2[0].equals(null) ) {
                //default image setting
                photo = R.drawable.pic_20;
            }else{
                if(arr2.length > 1) {
                    photo = Integer.parseInt(arr2[1]);
                }else {
                    //default image setting
                    photo = R.drawable.pic_20;
                }
            }
            String subtitle = "시설공지";
            String title = ntmp.getTitle();
            String content = ntmp.getContent();

            tmp = new ViewPagerItem(1, subtitle, title, content, artkey, photo);
            data.add(tmp);
        }
    }
    public void scheduleArticleAdd(String day){
        ViewPagerItem tmp;
        ArrayList<ScheduleListItem> s_list = new ArrayList<>();
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());

        s_list = handler.getSchedulebyDay(day);
        Iterator iterator = s_list.iterator();
        while (iterator.hasNext()){
            ScheduleListItem stmp = (ScheduleListItem) iterator.next();
            int artkey = stmp.getArticlekey();
            int photo;
            String tmpphoto = stmp.getPhoto();
            String[] arr2 = tmpphoto.split("/");
            if(arr2[0].equals(null) ) {
                //default image setting
                photo = R.drawable.pic_20;
            }else{
                if(arr2.length > 1) {
                    photo = Integer.parseInt(arr2[1]);
                }else{
                    //default image setting
                    photo = R.drawable.pic_20;
                }
            }
            String subtitle = stmp.getDay();
            String title = stmp.getTitle();
            String content = stmp.getContent();

            tmp = new ViewPagerItem(2, subtitle, title, content, artkey, photo);
            data.add(tmp);
        }

    }
    public void observereportArticleAdd(String day){
        ViewPagerItem tmp;
        ArrayList<ObservArticleItem> o_list = new ArrayList<>();
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());

        o_list = handler.getObserveArticlebyDay(day);
        Iterator iterator = o_list.iterator();
        while (iterator.hasNext()){
            ObservArticleItem otmp = (ObservArticleItem) iterator.next();
            int artkey = otmp.getArticleid();
            int photo;
            String tmpphoto = otmp.getPhoto();
            String[] arr2 = tmpphoto.split("/");
            if(arr2[0].equals(null) ) {
                //default image setting
                photo = R.drawable.pic_20;
            }else{
                if(arr2.length > 1) {
                    photo = Integer.parseInt(arr2[1]);
                }else{
                    //default image setting
                    photo = R.drawable.pic_20;
                }
            }
            String subtitle = otmp.getDay();
            String title = "관찰일지";

            int tmpState = otmp.getMood();
            String content="";
            switch (tmpState){
                case 1:
                    content = "기분 | 좋음 \n";
                   break;
                case 2:
                    content = "기분 | 보통 \n";
                    break;
                case 3:
                    content = "기분 | 나쁨 \n";
                    break;
            }

            tmpState = otmp.getActivity();
            switch (tmpState){
                case 1:
                    content = content + "활동량 | 좋음 \n";
                    break;
                case 2:
                    content = content + "활동량 | 보통 \n";
                    break;
                case 3:
                    content = content + "활동량 | 나쁨 \n";
                    break;
            }

            tmpState = otmp.getSleep();
            switch (tmpState){
                case 1:
                    content = content + "수면 | 좋음 \n";
                    break;
                case 2:
                    content = content + "수면 | 보통 \n";
                    break;
                case 3:
                    content = content + "수면 | 나쁨 \n";
                    break;
            }


            tmp = new ViewPagerItem(3, subtitle, title, content, artkey, photo);
            data.add(tmp);
        }
    }
    public void observereportArticleAdd(String savedID, String day){
        ViewPagerItem tmp;
        ArrayList<ObservArticleItem> o_list = new ArrayList<>();
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());

        o_list = handler.getObserveArticlebyUserIDDay(savedID, day);
        Iterator iterator = o_list.iterator();
        while (iterator.hasNext()){
            ObservArticleItem otmp = (ObservArticleItem) iterator.next();
            int artkey = otmp.getArticleid();
            int photo;
            String tmpphoto = otmp.getPhoto();
            String[] arr2 = tmpphoto.split("/");
            if(arr2[0].equals(null) ) {
                //default image setting
                photo = R.drawable.pic_20;
            }else{
                if(arr2.length > 1) {
                    photo = Integer.parseInt(arr2[1]);
                }else{
                    //default image setting
                    photo = R.drawable.pic_20;
                }
            }
            String subtitle = otmp.getDay();
            String title = "관찰일지";

            int tmpState = otmp.getMood();
            String content="";
            switch (tmpState){
                case 1:
                    content = "기분 | 좋음 \n";
                    break;
                case 2:
                    content = "기분 | 보통 \n";
                    break;
                case 3:
                    content = "기분 | 나쁨 \n";
                    break;
            }

            tmpState = otmp.getActivity();
            switch (tmpState){
                case 1:
                    content = content + "활동량 | 좋음 \n";
                    break;
                case 2:
                    content = content + "활동량 | 보통 \n";
                    break;
                case 3:
                    content = content + "활동량 | 나쁨 \n";
                    break;
            }

            tmpState = otmp.getSleep();
            switch (tmpState){
                case 1:
                    content = content + "수면 | 좋음 \n";
                    break;
                case 2:
                    content = content + "수면 | 보통 \n";
                    break;
                case 3:
                    content = content + "수면 | 나쁨 \n";
                    break;
            }


            tmp = new ViewPagerItem(3, subtitle, title, content, artkey, photo);
            data.add(tmp);
        }
    }

    //onClick속성이 지정된 View를 클릭했을때 자동으로 호출되는 메소드
    public void mOnClick(View v) {
        int position;
        switch (v.getId()) {
            case R.id.main_content_prev_btn://이전버튼 클릭
                position = mPager.getCurrentItem();
                //현재 보여지는 아이템의 위치를 리
                //현재 위치(position)에서 -1 을 해서 이전 position으로 변경
                //이전 Item으로 현재의 아이템 변경 설정(가장 처음이면 더이상 이동하지 않음)
                //첫번째 파라미터: 설정할 현재 위치
                //두번째 파라미터: 변경할 때 부드럽게 이동하는가? false면 팍팍 바뀜
                Toast.makeText(getApplicationContext(), "prevbtn clicked", Toast.LENGTH_SHORT).show();
                mPager.setCurrentItem(position - 1, true);
                break;
            case R.id.main_content_next_btn://다음버튼 클릭
                position = mPager.getCurrentItem();//현재 보여지는 아이템의 위치를 리턴
                //현재 위치(position)에서 +1 을 해서 다음 position으로 변경
                //다음 Item으로 현재의 아이템 변경 설정(가장 마지막이면 더이상 이동하지 않음)
                //첫번째 파라미터: 설정할 현재 위치
                //두번째 파라미터: 변경할 때 부드럽게 이동하는가? false면 팍팍 바뀜
                Toast.makeText(getApplicationContext(), "nextbtn clicked", Toast.LENGTH_SHORT).show();
                mPager.setCurrentItem(position + 1, true);
                break;
        }
    }

    public static View.OnClickListener detailViewListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            int position = mPager.getCurrentItem();
            int articleKey = adapter.getCurrentArticlekey(position);
            int articleType = adapter.getCurrentArticleType(position);
            Toast.makeText(v.getContext(), "article key : " + articleKey + ", type : "+ articleType, Toast.LENGTH_SHORT).show();

        }
    };



///////left navi menu setting
    private class ViewHolder {
        public ImageView uImg;
        public TextView uName;
        public TextView uClass;
        public TextView mText;
    }
    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ListData> mListData = new ArrayList<ListData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater Inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = Inflater.inflate(R.layout.listview_item, null);

                holder.uImg = (ImageView) convertView.findViewById(R.id.userImg);
                holder.uName = (TextView) convertView.findViewById(R.id.userName);
                holder.uClass = (TextView) convertView.findViewById(R.id.userClass);
                holder.mText = (TextView) convertView.findViewById(R.id.menuText);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ListData mData = mListData.get(position);

            if (mData.uImg == null) {
                holder.uImg.setVisibility(View.GONE);
                holder.uName.setVisibility(View.GONE);
                holder.uClass.setVisibility(View.GONE);
                holder.mText.setVisibility(View.VISIBLE);
                holder.mText.setText(mData.mText);
            } else {
                holder.uImg.setVisibility(View.VISIBLE);
                holder.uName.setVisibility(View.VISIBLE);
                holder.uClass.setVisibility(View.VISIBLE);
                holder.mText.setVisibility(View.GONE);
                holder.uImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                holder.uImg.setImageDrawable(mData.uImg);
                holder.uName.setText(mData.uName);
                holder.uClass.setText(mData.uClass);

            }

            return convertView;

        }
        public void addItem(Drawable uImg, String uName, String uClass, String mText) {
            ListData addInfo = null;
            addInfo = new ListData();
            addInfo.uImg = uImg;
            addInfo.uName = uName;
            addInfo.uClass = uClass;
            addInfo.mText = mText;

            mListData.add(addInfo);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

    }
/////////////////////////////////////////////////////////end

    //android back press -> close left navi menu
    public void onBackPressed() {
        if (dlDrawer.isDrawerOpen(lvNavList)) {
            dlDrawer.closeDrawer(lvNavList);
        } else {
            super.onBackPressed();
        }
    }

    //left menu click listener
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

            switch (position) {
                case 0:
                    break;
                case 1:
                    break;
                case 2: //logout btn
                    SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    editor.clear();
                    editor.commit();
                    finish();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    } else {

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    }
                    startActivity(intent);
                    break;
                case 3:
                    break;
                case 4:
                    //flContainer.setBackgroundColor(Color.parseColor("#DAA520"));
                    break;
            }
            dlDrawer.closeDrawer(lvNavList);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:     //ObservReportWorkerActivity finished
                Button tmp_btn = (Button) findViewById(R.id.observ_report_btn);
                tmp_btn.setSelected(false);
                tmp_btn.setPressed(false);
                setAnimation(2, View.GONE, fastmenudown);       //notebtn mode param = 2
                selected_menu = 0;
                tmp_btn = (Button) findViewById(R.id.notebtn);
                tmp_btn.setBackgroundResource(R.drawable.note);

                break;
            case 2:     //ProgramReportWorkerActivity finished
                break;
            case 3:     //WorkReportActivity finished
                break;
            default:
                break;
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




