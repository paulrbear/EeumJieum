package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class DetailWorkReportViewActivity extends FragmentActivity {
    private Button program_btn, observ_report_btn, work_report_btn, notice_btn, schedule_btn, roombtn1, roombtn2, roombtn3;
    private TextView notice_lb, schedule_lb, program_lb, observ_report_lb, work_report_lb;
    private Animation menuup, menudown, roomup, roomdown;
    boolean room_opened = false;
    private int selected_menu = 0;

    Button btn01, btn02, btn03;
    String savedID;
    int savedMode;
    int articleKey, selected_day, selected_date;
    int selected_tab = 1;

    String selected_room;
    String string_selected_day;

    TextView total_count, normal_count, out_count, hospital_count, etc_count;
    ListView firsttab_listview;
    GridView secondtab_gridView;
    LinearLayout thirdtab, secondtab_iv;
    String[] program_strarr;

    ArrayList<PersonalWorkReportItem> personaldata = new ArrayList<>();
    PersonalWorkReportAdapter adapter;

    private ImageAdapter mAdapter;
    public int selected_user = -1;


    class ViewHolder{
        ImageView selectedimg;
        ImageView imageview;
        ImageView disabledimg;
        TextView userName;
        int id;
        boolean enabled;
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_work_report_view);

        Intent intent = getIntent();
        savedID = intent.getExtras().getString("userID");
        savedMode = intent.getExtras().getInt("savedMode");
        articleKey = intent.getExtras().getInt("articleKey");
        selected_day = intent.getExtras().getInt("selected_day");
        selected_date = intent.getExtras().getInt("selected_date");


        btn01 = (Button) findViewById(R.id.btn01);
        btn02 = (Button) findViewById(R.id.btn02);
        btn03 = (Button) findViewById(R.id.btn03);

        firsttab_listview = (ListView) findViewById(R.id.firsttab_listview);
        secondtab_gridView = (GridView) findViewById(R.id.secondtab_grid_view);
        secondtab_iv = (LinearLayout) findViewById(R.id.secondtab_meal_linearview);
        thirdtab = (LinearLayout) findViewById(R.id.third_tab_linearlayout);

        total_count = (TextView) findViewById(R.id.total_count_tv);
        normal_count = (TextView) findViewById(R.id.normal_count_tv);
        out_count = (TextView) findViewById(R.id.out_count_tv);
        hospital_count = (TextView) findViewById(R.id.hospital_count_tv);
        etc_count = (TextView) findViewById(R.id.etc_count_tv);


        roombtn1 = (Button) findViewById(R.id.tfdbtn1);
        roombtn2 = (Button) findViewById(R.id.tfdbtn2);
        roombtn3 = (Button) findViewById(R.id.tfdbtn3);


        //initialization : room, worker, count, tab

        initCalenderDate();
        initializeView(articleKey);

        //room selected animation
        //bottom pop-menu
        menuup = AnimationUtils.loadAnimation(this, R.anim.note_up_animation);
        menudown = AnimationUtils.loadAnimation(this, R.anim.note_down_animation);
        roomdown = AnimationUtils.loadAnimation(this, R.anim.roomlist_down_animation);
        roomup = AnimationUtils.loadAnimation(this, R.anim.roomlist_up_animation);

        mAdapter = new ImageAdapter(this);
        secondtab_gridView.setAdapter(mAdapter);

    }

    private void initCalenderDate(){

        Date date = new Date();
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.setTime(date);

        //cal.set(Calendar.YEAR, 2016);
        //cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
        //cal.set(Calendar.DATE, 30);


        int[] weekdate = {0,0,0,0,0,0,0,0};

        //System.out.println("오늘 날짜 : " + cal.getTime());

        for(int i=0; i<7 ; i++) {
            cal.setTime(date);
            cal.add(Calendar.DATE, (i+1) - cal.get(Calendar.DAY_OF_WEEK));
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
        if(cal.get(Calendar.MONTH)+1 < 10){
            tmp_btn.setText("0"+Integer.toString(cal.get(Calendar.MONTH)+1));
        }else {
            tmp_btn.setText(Integer.toString(cal.get(Calendar.MONTH) + 1));
        }

        cal.setTime(date);


        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        string_selected_day =  year+ "/" + month + "/" + selected_date;

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

    public void initializeView(int key){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        WorkReportArticleItem dbresult = handler.getWorkReportbyID(key);
        //Toast.makeText(getApplicationContext(), ""+dbresult.size(), Toast.LENGTH_SHORT).show();

        selected_room = dbresult.getObjectroom();
        TextView tmptv = (TextView) findViewById(R.id.room_name_tv);
        tmptv.setText(selected_room);
        btn01.setSelected(true);

        int total, nor, out, hos, etc;


        nor = dbresult.getNormalcount();
        out = dbresult.getOutcount();
        hos = dbresult.getHospitalcount();
        etc = dbresult.getEtccount();
        total = nor+out+hos+etc;

        total_count.setText(String.valueOf(total));
        normal_count.setText(String.valueOf(nor));
        out_count.setText(String.valueOf(out));
        hospital_count.setText(String.valueOf(hos));
        etc_count.setText(String.valueOf(etc));

        List<PersonalWorkReportItem> articlelist = handler.getPersonalWorkReportbyID(key);
        Iterator iterator = articlelist.iterator();
        int i = 0;
        while (iterator.hasNext()){
            personaldata.add((PersonalWorkReportItem) iterator.next());
        }

        adapter = new PersonalWorkReportAdapter(this, R.layout.personal_article_item, personaldata);
        firsttab_listview.setAdapter(adapter);

        tmptv = (TextView) findViewById(R.id.room_worker_tv);

        if(selected_room.equals("기쁨방")){
            roombtn1.setText("기쁨방");
            roombtn2.setText("믿음방");
            roombtn3.setText("은혜방");
            tmptv.setText("이유민 선생님, 한남수 선생님");
        }else if(selected_room.equals("믿음방")){
            roombtn2.setText("기쁨방");
            roombtn1.setText("믿음방");
            roombtn3.setText("은혜방");
            tmptv.setText("강지석 선생님");
        }else if(selected_room.equals("은혜방")){
            roombtn3.setText("기쁨방");
            roombtn2.setText("믿음방");
            roombtn1.setText("은혜방");
            tmptv.setText("한지은 선생님, 윤고은 선생님");
        }

        program_strarr = dbresult.getProgramtxtList();
        tmptv = (TextView) findViewById(R.id.morning_program_tv);
        tmptv.setText(program_strarr[0]);

        tmptv = (TextView) findViewById(R.id.afternoon_program_tv);
        tmptv.setText(program_strarr[1]);
        tmptv = (TextView) findViewById(R.id.night_program_tv);
        tmptv.setText(program_strarr[2]);
    }

    public void setTabView(){
        //set view three tab layout
        switch(selected_tab){
            case 1:
                firsttab_listview.setVisibility(View.VISIBLE);
                secondtab_gridView.setVisibility(View.GONE);
                secondtab_iv.setVisibility(View.GONE);
                thirdtab.setVisibility(View.GONE);
                break;
            case 2:
                firsttab_listview.setVisibility(View.GONE);
                secondtab_gridView.setVisibility(View.VISIBLE);
                secondtab_iv.setVisibility(View.VISIBLE);
                thirdtab.setVisibility(View.GONE);
                break;
            case 3:
                firsttab_listview.setVisibility(View.GONE);
                secondtab_gridView.setVisibility(View.GONE);
                secondtab_iv.setVisibility(View.GONE);
                thirdtab.setVisibility(View.VISIBLE);
                break;
        }
    }

    /************************* need to change meal image **************************/
    public void secondTab_meal_imageset(int position){
        ImageView tmpiv = (ImageView) findViewById(R.id.meal_iv_1);
        switch(personaldata.get(position).getMeal1()){
            case 1:
                tmpiv.setImageResource(R.drawable.status_good);
                break;
            case 2:
                tmpiv.setImageResource(R.drawable.status_soso);
                break;
            case 3:
                tmpiv.setImageResource(R.drawable.status_bad);
                break;
            case 4:
                tmpiv.setImageResource(R.drawable.status_bad);
                break;
        }
        tmpiv = (ImageView) findViewById(R.id.meal_iv_2);
        switch(personaldata.get(position).getMeal2()){
            case 1:
                tmpiv.setImageResource(R.drawable.status_good);
                break;
            case 2:
                tmpiv.setImageResource(R.drawable.status_soso);
                break;
            case 3:
                tmpiv.setImageResource(R.drawable.status_bad);
                break;
            case 4:
                tmpiv.setImageResource(R.drawable.status_bad);
                break;
        }
        tmpiv = (ImageView) findViewById(R.id.meal_iv_3);
        switch(personaldata.get(position).getMeal3()){
            case 1:
                tmpiv.setImageResource(R.drawable.status_good);
                break;
            case 2:
                tmpiv.setImageResource(R.drawable.status_soso);
                break;
            case 3:
                tmpiv.setImageResource(R.drawable.status_bad);
                break;
            case 4:
                tmpiv.setImageResource(R.drawable.status_bad);
                break;
        }
    }

    private class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;
        private int pre_selected = -1;
        boolean set_first_select_user = false;

        public ImageAdapter(Context c){
            mContext = c;
            mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return personaldata.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.gridview_userlist_item, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.userImg_item);
                holder.disabledimg = (ImageView) convertView.findViewById(R.id.userImg_disabled_item);
                holder.selectedimg = (ImageView) convertView.findViewById(R.id.userImg_selected_item);
                holder.userName = (TextView) convertView. findViewById(R.id.userName_item);

                convertView.setTag(holder);

                //holder.imageview.setLayoutParams(new FrameLayout.LayoutParams(220,220));
                //holder.imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
                //holder.selectedimg.setLayoutParams(new FrameLayout.LayoutParams(220,220));
                //holder.selectedimg.setScaleType(ImageView.ScaleType.CENTER_CROP);

            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.imageview.setId(position);
            holder.selectedimg.setId(position);
            holder.disabledimg.setId(position);
            holder.userName.setId(position);

            if(!set_first_select_user){
                if(personaldata.get(position).getStatus() == 0 || personaldata.get(position).getStatus() == 3){
                    selected_user = position;
                    set_first_select_user = true;

                    secondTab_meal_imageset(selected_user);


                }
            }

            if(personaldata.get(position).getStatus() == 1){      //go out person
                holder.enabled = false;
                holder.userName.setTextColor(getResources().getColor(R.color.colorLightGray));
                holder.disabledimg.setVisibility(View.VISIBLE);
            }else if(personaldata.get(position).getStatus() == 2){    //hospital
                holder.enabled = false;
                holder.userName.setTextColor(getResources().getColor(R.color.colorLightGray));
                holder.disabledimg.setVisibility(View.VISIBLE);
            }else{
                holder.enabled = true;
            }

            if(selected_user == position){
                holder.selectedimg.setVisibility(View.VISIBLE);
            }
            if(pre_selected == position){
                holder.selectedimg.setVisibility(View.GONE);
            }

            holder.imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    Toast.makeText(getApplicationContext(), "clicked!!!" + id, Toast.LENGTH_SHORT).show();

                    if (holder.enabled && (selected_user != id) ) {
                        pre_selected = selected_user;
                        selected_user = id;

                        holder.selectedimg.setVisibility(View.VISIBLE);

                    }

                    secondTab_meal_imageset(position);

                    notifyDataSetChanged();
                }
            });

            holder.imageview.setImageResource(personaldata.get(position).getObjectImg());
            holder.userName.setText(personaldata.get(position).getObjectName());
            holder.id = position;
            return convertView;
        }

    }

    public void onClick_modbtn(View v){

       // data = intent.getParcelableArrayListExtra("UserListItem");
        //statusdata = intent.getIntegerArrayListExtra("StatusList");
        //selected_room = intent.getStringExtra("selectedRoom");
        //string_selected_day = intent.getExtras().getString("selectedDay");

        ArrayList<UserListItem> userlistdata = new ArrayList<>();
        ArrayList<Integer> statusdata = new ArrayList<>();
        int meal1[] = new int[personaldata.size()];
        int meal2[] = new int[personaldata.size()];
        int meal3[] = new int[personaldata.size()];
        String usercontent[] = new String[personaldata.size()];


        Iterator iterator = personaldata.iterator();
        int i = 0;
        while (iterator.hasNext()){
            PersonalWorkReportItem tmpitem = (PersonalWorkReportItem) iterator.next();
            String tmpname = tmpitem.getObjectName();
            String tmproom = tmpitem.getObjectRoom();
            int tmpimg = tmpitem.getObjectImg();

            UserListItem tmpuser = new UserListItem(tmpimg, tmpname, tmproom);
            userlistdata.add(tmpuser);
            statusdata.add(tmpitem.getStatus());
            meal1[i] = tmpitem.getMeal1();
            meal2[i] = tmpitem.getMeal2();
            meal3[i] = tmpitem.getMeal3();
            usercontent[i] = tmpitem.getObjectContext();
            i++;
        }

        Intent intent;
        intent = new Intent(getApplicationContext(), WriteWorkReportActivity.class);

        intent.putExtra("userID",savedID);
        intent.putExtra("userMode", savedMode);

        intent.putExtra("mode", "modify");
        intent.putExtra("articleKey", articleKey);
        intent.putParcelableArrayListExtra("UserListItem", userlistdata);
        intent.putExtra("StatusList", statusdata);

        intent.putExtra("selectedDay", string_selected_day);
        intent.putExtra("selectedRoom", selected_room);

        intent.putExtra("meal1", meal1);
        intent.putExtra("meal2", meal2);
        intent.putExtra("meal3", meal3);
        intent.putExtra("userContent", usercontent);
        intent.putExtra("programContent", program_strarr);

        startActivityForResult(intent,0);
        overridePendingTransition(0,0);     //activity transition animation delete

    }

    public void onClick_delbtn(View v){
        CustomDialog dialog = new CustomDialog(this);
        dialog.setMode(3);      //mode 2 for work report registeration
        dialog.setBtn1Text("뒤로");
        dialog.setBtn2Text("삭제");
        dialog.setUpdatearticlekey(articleKey);
        dialog.setDialogMsg("근무 일지를 \n삭제하시겠습니까?");

        dialog.show();
    }



    public void onClick_01btn(View v){
        if(btn02.isSelected()){
            btn02.setSelected(false);
            btn02.setTextColor(getResources().getColor(R.color.colorBackgroundGray));
            btn02.setBackgroundColor(Color.TRANSPARENT);
        }else if(btn03.isSelected()){
            btn03.setSelected(false);
            btn03.setTextColor(getResources().getColor(R.color.colorBackgroundGray));
            btn03.setBackgroundColor(Color.TRANSPARENT);
        }
        btn01.setTextColor(getResources().getColor(R.color.colorSelectedBlue));
        btn01.setBackgroundResource(R.drawable.shape_radius_rect_selected_blue);
        btn01.setSelected(true);
        selected_tab = 1;
        setTabView();
    }
    public void onClick_02btn(View v){
        if(btn01.isSelected()){
            btn01.setSelected(false);
            btn01.setTextColor(getResources().getColor(R.color.colorBackgroundGray));
            btn01.setBackgroundColor(Color.TRANSPARENT);
        }else if(btn03.isSelected()){
            btn03.setSelected(false);
            btn03.setTextColor(getResources().getColor(R.color.colorBackgroundGray));
            btn03.setBackgroundColor(Color.TRANSPARENT);
        }
        btn02.setTextColor(getResources().getColor(R.color.colorSelectedBlue));
        btn02.setBackgroundResource(R.drawable.shape_radius_rect_selected_blue);
        btn02.setSelected(true);
        selected_tab = 2;
        setTabView();


    }
    public void onClick_03btn(View v){
        if(btn01.isSelected()){
            btn01.setSelected(false);
            btn01.setTextColor(getResources().getColor(R.color.colorBackgroundGray));
            btn01.setBackgroundColor(Color.TRANSPARENT);
        }else if(btn02.isSelected()){
            btn02.setSelected(false);
            btn02.setTextColor(getResources().getColor(R.color.colorBackgroundGray));
            btn02.setBackgroundColor(Color.TRANSPARENT);
        }
        btn03.setTextColor(getResources().getColor(R.color.colorSelectedBlue));
        btn03.setBackgroundResource(R.drawable.shape_radius_rect_selected_blue);
        btn03.setSelected(true);
        selected_tab = 3;
        setTabView();       //if changed data is not applied, should call in front of chaging data sequence
    }
    public void onClick_calendar(View v){

    }

    public void onClick_backbtn(View v){
        finish();
    }

    //roomlist tfd click listner
    public void onClick_roomlist(View v){
        /*
        if(v.getId() == R.id.tfdbtn1 && room_opened == true){
            roombtn1.bringToFront();
            setAnimation(View.GONE, roomup, 0);
            room_opened = false;
        }else if(v.getId() == R.id.tfdbtn1 && room_opened == false){
            roombtn2.setVisibility(View.VISIBLE);
            roombtn3.setVisibility(View.VISIBLE);
            setAnimation(View.VISIBLE, roomdown, 0);
            room_opened = true;
            return;
        }else if(v.getId() == R.id.tfdbtn2){
            roombtn1.bringToFront();
            selected_room = (String)roombtn2.getText();
            roombtn2.setText(roombtn1.getText());
            roombtn1.setText(selected_room);
            setAnimation(View.GONE, roomup, 2);
            room_opened = false;
        }else if(v.getId() == R.id.tfdbtn3){
            roombtn1.bringToFront();
            selected_room = (String)roombtn3.getText();
            roombtn3.setText(roombtn1.getText());
            roombtn1.setText(selected_room);
            setAnimation(View.GONE, roomup, 3);
            room_opened = false;
        }
        */
        //setArticleSelected(selected_date);
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


            program_btn.setVisibility(View.VISIBLE);
            observ_report_btn.setVisibility(View.VISIBLE);
            work_report_btn.setVisibility(View.VISIBLE);
            program_lb.setVisibility(View.VISIBLE);
            observ_report_lb.setVisibility(View.VISIBLE);
            work_report_lb.setVisibility(View.VISIBLE);
            setAnimation(2, View.VISIBLE, menuup);         //notebtn mode param = 2


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

    //room list animation
    private void setAnimation(final int btnStatus, final Animation animation, final int btn){
        animation.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(btn == 0 && btnStatus == View.GONE) {
                    roombtn2.setVisibility(btnStatus);
                    roombtn3.setVisibility(btnStatus);
                }else if(btn == 2 && btnStatus == View.GONE){
                    roombtn2.setVisibility(btnStatus);
                    roombtn3.setVisibility(btnStatus);

                }else if(btn == 3 && btnStatus == View.GONE){
                    roombtn2.setVisibility(btnStatus);
                    roombtn3.setVisibility(btnStatus);

                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        roombtn2.startAnimation(animation);
        roombtn3.startAnimation(animation);
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

                work_report_btn.startAnimation(animation);
                work_report_lb.startAnimation(animation);

            program_btn.startAnimation(animation);
            observ_report_btn.startAnimation(animation);
            program_lb.startAnimation(animation);
            observ_report_lb.startAnimation(animation);
        }
    }



}
