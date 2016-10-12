package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.Iterator;

public class WriteWorkReportActivity extends FragmentActivity {
    String savedID;
    int savedMode;

    String mode;    //modify and write
    int updatearticlekey;

    String string_selected_day;
    ArrayList<UserListItem> data = new ArrayList<>();
    ArrayList<Integer> statusdata = new ArrayList<>();      //  normal 0/ out 1/ hospital 2/ etc 3
    String selected_room;
    int meal_morning = 0;
    int meal_afternoon = 0;
    int meal_night = 0;
    int selected_tab = 1;
    int len = 0;
    Button btn01, btn02, btn03, btn11, btn12, btn13, btn14, btn21, btn22, btn23, btn24,  btn31, btn32, btn33, btn34;

    GridView gridView;
    EditText et, morning_et, afternoon_et, night_et;
    LinearLayout secondtab, thirdtab;

    private ImageAdapter mAdapter;
    public ArrayList<Integer> mThumbIds = new ArrayList<>();
    public ArrayList<String> userNamelist = new ArrayList<>();

    public String[] userContent;
    public int[] statuslist;
    public int[] meal1;    // lot 1/ soso 2/ small 3/ fast 4
    public int[] meal2;
    public int[] meal3;

    public String[] programContent = new String[3];

    String normalperson = "";
    String outperson = "";
    String hospitalperson = "";
    String etcperson = "";
    String total_program_text;
    String[] objectNamelist;
    int[] objectImagelist;


    //InputMethodManager imm;

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
        setContentView(R.layout.activity_write_work_report);


        Intent intent = getIntent();
        savedID = intent.getExtras().getString("userID");
        savedMode = intent.getExtras().getInt("userMode");
        mode = intent.getExtras().getString("mode");


        data = intent.getParcelableArrayListExtra("UserListItem");
        statusdata = intent.getIntegerArrayListExtra("StatusList");
        selected_room = intent.getStringExtra("selectedRoom");
        string_selected_day = intent.getExtras().getString("selectedDay");

        len = data.size();
        Toast.makeText(getApplicationContext(), "total room member : "+data.size(), Toast.LENGTH_LONG).show();

        TextView tmptv = (TextView) findViewById(R.id.write_room_tv);
        tmptv.setText(selected_room);


        btn01 = (Button) findViewById(R.id.btn01);
        btn02 = (Button) findViewById(R.id.btn02);
        btn03 = (Button) findViewById(R.id.btn03);
        btn11 = (Button) findViewById(R.id.btn11);
        btn12 = (Button) findViewById(R.id.btn12);
        btn13 = (Button) findViewById(R.id.btn13);
        btn14 = (Button) findViewById(R.id.btn14);
        btn21 = (Button) findViewById(R.id.btn21);
        btn22 = (Button) findViewById(R.id.btn22);
        btn23 = (Button) findViewById(R.id.btn23);
        btn24 = (Button) findViewById(R.id.btn24);
        btn31 = (Button) findViewById(R.id.btn31);
        btn32 = (Button) findViewById(R.id.btn32);
        btn33 = (Button) findViewById(R.id.btn33);
        btn34 = (Button) findViewById(R.id.btn34);

        morning_et = (EditText) findViewById(R.id.morning_et);
        afternoon_et = (EditText) findViewById(R.id.afternoon_et);
        night_et = (EditText) findViewById(R.id.night_et);

        btn01.setSelected(true);

        gridView = (GridView)findViewById(R.id.img_grid_view);
        et = (EditText) findViewById(R.id.status_et);
        secondtab = (LinearLayout) findViewById(R.id.meal_linearlayout);
        thirdtab = (LinearLayout) findViewById(R.id.third_tab_linearlayout);

        //imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        mAdapter = new ImageAdapter(this);
        gridView.setAdapter(mAdapter);



        if(mode.equals("modify")){
            meal1 = intent.getExtras().getIntArray("meal1");
            meal2 = intent.getExtras().getIntArray("meal2");
            meal3 = intent.getExtras().getIntArray("meal3");
            programContent = intent.getExtras().getStringArray("programContent");
            userContent = intent.getExtras().getStringArray("userContent");
            updatearticlekey = intent.getExtras().getInt("articleKey");

            listappend();
        }else if(mode.equals("write")){
            listinit();
        }
        setTabView();
    }

    public void listinit(){
        Iterator iterator = data.iterator();
        while(iterator.hasNext()){
            UserListItem tmp = (UserListItem) iterator.next();
            mThumbIds.add(tmp.getuImg());
            userNamelist.add(tmp.getName());
            System.out.println("count****");
        }

        meal1 = new int[len];
        meal2 = new int[len];
        meal3 = new int[len];
        userContent = new String[len];
        statuslist = new int[len];

        for(int i = 0; i <len ; i++){
            meal1[i] = 0;
            meal2[i] = 0;
            meal3[i] = 0;
            userContent[i] = "내용을 입력하세요";
        }

        programContent[0] = "내용을 입력하세요";
        programContent[1] = "내용을 입력하세요";
        programContent[2] = "내용을 입력하세요";

        for(int i = 0; i < len ; i++){
            if(statusdata.get(i) == 0 || statusdata.get(i) == 3){
                selected_user = i;
                break;
            }
        }

    }

    public void listappend(){
        statuslist = new int[len];

        Iterator iterator = data.iterator();

        while(iterator.hasNext()){
            UserListItem tmp = (UserListItem) iterator.next();
            mThumbIds.add(tmp.getuImg());
            userNamelist.add(tmp.getName());
        }

        for(int i = 0; i < len ; i++){
            if(statusdata.get(i) == 0 || statusdata.get(i) == 3){
                selected_user = i;
                et.setText(userContent[i]);
                break;
            }
        }
    }


   private class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;
        private int pre_selected = -1;

        public ImageAdapter(Context c){
            mContext = c;
            mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mThumbIds.size();
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

            if(statusdata.get(position) == 1){      //go out person
                holder.enabled = false;
                holder.userName.setTextColor(getResources().getColor(R.color.colorBlueGray1));
                holder.disabledimg.setVisibility(View.VISIBLE);
            }else if(statusdata.get(position) == 2){    //hospital
                holder.enabled = false;
                holder.userName.setTextColor(getResources().getColor(R.color.colorBlueGray1));
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


                        if(selected_tab == 1){
                            userContent[pre_selected] = et.getText().toString();    //previous user content save
                            et.setText(userContent[id]);            //current user content set
                        }else if(selected_tab == 2){
                            meal_array_set(pre_selected, selected_user);
                        }
                    }
                    notifyDataSetChanged();
                }
            });

            holder.imageview.setImageResource(mThumbIds.get(position));
            holder.userName.setText(userNamelist.get(position));
            holder.id = position;
            return convertView;
        }

    }

    public void meal_array_set(int prev, int current){
        switch (meal1[prev]){
            case 0 :
                break;
            case 1:
                btn11.setSelected(false);
                btn11.setTextColor(getResources().getColor(R.color.colorGray2));
                break;
            case 2:
                btn12.setSelected(false);
                btn12.setTextColor(getResources().getColor(R.color.colorGray2));
                break;
            case 3:
                btn13.setSelected(false);
                btn13.setTextColor(getResources().getColor(R.color.colorGray2));
                break;
            case 4:
                btn14.setSelected(false);
                btn14.setTextColor(getResources().getColor(R.color.colorGray2));
                break;
        }
        meal_morning = meal1[current];

        switch (meal2[prev]){
            case 0 :
                break;
            case 1:
                btn21.setSelected(false);
                btn21.setTextColor(getResources().getColor(R.color.colorGray2));
                break;
            case 2:
                btn22.setSelected(false);
                btn22.setTextColor(getResources().getColor(R.color.colorGray2));
                break;
            case 3:
                btn23.setSelected(false);
                btn23.setTextColor(getResources().getColor(R.color.colorGray2));
                break;
            case 4:
                btn24.setSelected(false);
                btn24.setTextColor(getResources().getColor(R.color.colorGray2));
                break;
        }
        meal_afternoon = meal2[current];

        switch (meal3[prev]){
            case 0 :
                break;
            case 1:
                btn31.setSelected(false);
                btn31.setTextColor(getResources().getColor(R.color.colorGray2));
                break;
            case 2:
                btn32.setSelected(false);
                btn32.setTextColor(getResources().getColor(R.color.colorGray2));
                break;
            case 3:
                btn33.setSelected(false);
                btn33.setTextColor(getResources().getColor(R.color.colorGray2));
                break;
            case 4:
                btn34.setSelected(false);
                btn34.setTextColor(getResources().getColor(R.color.colorGray2));
                break;
        }
        meal_night = meal3[current];

        switch (meal_morning){
            case 0 :
                break;
            case 1:
                btn11.setSelected(true);
                btn11.setTextColor(getResources().getColor(R.color.colorMagenta));
                break;
            case 2:
                btn12.setSelected(true);
                btn12.setTextColor(getResources().getColor(R.color.colorMagenta));
                break;
            case 3:
                btn13.setSelected(true);
                btn13.setTextColor(getResources().getColor(R.color.colorMagenta));
                break;
            case 4:
                btn14.setSelected(true);
                btn14.setTextColor(getResources().getColor(R.color.colorMagenta));
                break;
        }

        switch (meal_afternoon){
            case 0 :
                break;
            case 1:
                btn21.setSelected(true);
                btn21.setTextColor(getResources().getColor(R.color.colorMagenta));
                break;
            case 2:
                btn22.setSelected(true);
                btn22.setTextColor(getResources().getColor(R.color.colorMagenta));
                break;
            case 3:
                btn23.setSelected(true);
                btn23.setTextColor(getResources().getColor(R.color.colorMagenta));
                break;
            case 4:
                btn24.setSelected(true);
                btn24.setTextColor(getResources().getColor(R.color.colorMagenta));
                break;
        }

        switch (meal_night){
            case 0 :
                break;
            case 1:
                btn31.setSelected(true);
                btn31.setTextColor(getResources().getColor(R.color.colorMagenta));
                break;
            case 2:
                btn32.setSelected(true);
                btn32.setTextColor(getResources().getColor(R.color.colorMagenta));
                break;
            case 3:
                btn33.setSelected(true);
                btn33.setTextColor(getResources().getColor(R.color.colorMagenta));
                break;
            case 4:
                btn34.setSelected(true);
                btn34.setTextColor(getResources().getColor(R.color.colorMagenta));
                break;
        }
    }

    public void onClick_cancelbtn(View v){
        CustomDialog dialog = new CustomDialog(this);
        dialog.show();
    }

    public void onClick_donebtn(View v){
        //if usercontent default value, should convert text (default content -> null)
        saveCurrentViewState();
        setDBdata();
        CustomDialog dialog = new CustomDialog(this);
        dialog.setMode(3);      //mode 2 for work report registeration
        if(mode.equals("modify")){
            dialog.setBtn1Text("뒤로");
            dialog.setBtn2Text("수정");
            dialog.setUpdatearticlekey(updatearticlekey);
            dialog.setWorkReportData(selected_room, string_selected_day, normalperson, outperson, hospitalperson, etcperson, total_program_text, objectNamelist, statuslist,
                    userContent, meal1, meal2, meal3, objectImagelist);
            dialog.setDialogMsg("근무 일지를 \n수정하시겠습니까?");
        }else {
            dialog.setBtn1Text("뒤로");
            dialog.setBtn2Text("등록");
            dialog.setWorkReportData(selected_room, string_selected_day, normalperson, outperson, hospitalperson, etcperson, total_program_text, objectNamelist, statuslist,
                    userContent, meal1, meal2, meal3, objectImagelist);
            dialog.setDialogMsg("근무 일지를 \n등록하시겠습니까?");
        }
        dialog.show();
    }

    public void setDBdata(){
        Iterator tmpiterator = statusdata.iterator();
        int nor=0;
        int out=0;
        int hos=0;
        int etc=0;
        while(tmpiterator.hasNext()){
            int tmp = (int) tmpiterator.next();
            if(tmp == 0){
                nor++;
            }else if(tmp==1){
                out++;
            }else if (tmp==2){
                hos++;
            }else if(tmp==3){
                etc++;
            }
        }

        normalperson = String.valueOf(nor);
        outperson= String.valueOf(out);
        hospitalperson = String.valueOf(hos);;
        etcperson = String.valueOf(etc);

        Iterator iterator = statusdata.iterator();
        Iterator iterator1 = data.iterator();

        int i = 0;
        objectNamelist = new String[data.size()];
        objectImagelist = new int[data.size()];
        //statuslist = new int[data.size()];

        while(iterator.hasNext()){
            int tmp = (int) iterator.next();
            UserListItem tmpitem = (UserListItem) iterator1.next();
            String tmpName = tmpitem.getName();
            objectNamelist[i] = tmpName;
            objectImagelist[i] = tmpitem.getuImg();
            statuslist[i] = tmp;
            if(tmp == 0){
                normalperson = normalperson + "/" + tmpName;
                System.out.println("normal person, " + normalperson);
            }else if(tmp == 1){
                outperson = outperson + "/" + tmpName;
                System.out.println("out person, " + outperson);
            }else if (tmp == 2) {
                hospitalperson = hospitalperson + "/" + tmpName;
                System.out.println("hospital person, " + hospitalperson);
            }else if(tmp == 3){
                etcperson = etcperson + "/" + tmpName;
                System.out.println("etc person, " + etcperson);
            }

            if(userContent[i].equals("내용을 입력하세요")){
                if(tmp == 1){
                    userContent[i] = "null";
                }else if(tmp ==2){
                    userContent[i] = "null";
                }
            }
            i++;
        }

        total_program_text = programContent[0] + "/" + programContent[1] + "/" + programContent[2];

    }

    public void onClick_objectlayout(View v){

    }

    public void saveCurrentViewState(){
        if(selected_tab == 1){
            userContent[selected_user] = et.getText().toString();    //previous user content save
            et.setText(userContent[selected_user]);            //current user content set
        }else if(selected_tab == 2){
            if(btn11.isSelected()) {
                meal1[selected_user] = 1;
            }else if(btn12.isSelected()) {
                meal1[selected_user] = 2;
            }else if(btn13.isSelected()) {
                meal1[selected_user] = 3;
            }else if(btn14.isSelected()) {
                meal1[selected_user] = 4;
            }

            if(btn21.isSelected()) {
                meal2[selected_user] = 1;
            }else if(btn22.isSelected()) {
                meal2[selected_user] = 2;
            }else if(btn23.isSelected()) {
                meal2[selected_user] = 3;
            }else if(btn24.isSelected()) {
                meal2[selected_user] = 4;
            }

            if(btn31.isSelected()) {
                meal3[selected_user] = 1;
            }else if(btn32.isSelected()) {
                meal3[selected_user] = 2;
            }else if(btn33.isSelected()) {
                meal3[selected_user] = 3;
            }else if(btn34.isSelected()) {
                meal3[selected_user] = 4;
            }
        }else if(selected_tab == 3){
            programContent[0] = morning_et.getText().toString();
            programContent[1] = afternoon_et.getText().toString();
            programContent[2] = night_et.getText().toString();
        }
    }
    public void loadCurrentUserMealStatus(){
        btn11.setSelected(false);
        btn11.setTextColor(getResources().getColor(R.color.colorGray2));
        btn12.setSelected(false);
        btn12.setTextColor(getResources().getColor(R.color.colorGray2));
        btn13.setSelected(false);
        btn13.setTextColor(getResources().getColor(R.color.colorGray2));
        btn14.setSelected(false);
        btn14.setTextColor(getResources().getColor(R.color.colorGray2));
        btn21.setSelected(false);
        btn21.setTextColor(getResources().getColor(R.color.colorGray2));
        btn22.setSelected(false);
        btn22.setTextColor(getResources().getColor(R.color.colorGray2));
        btn23.setSelected(false);
        btn23.setTextColor(getResources().getColor(R.color.colorGray2));
        btn24.setSelected(false);
        btn24.setTextColor(getResources().getColor(R.color.colorGray2));
        btn31.setSelected(false);
        btn31.setTextColor(getResources().getColor(R.color.colorGray2));
        btn32.setSelected(false);
        btn32.setTextColor(getResources().getColor(R.color.colorGray2));
        btn33.setSelected(false);
        btn33.setTextColor(getResources().getColor(R.color.colorGray2));
        btn34.setSelected(false);
        btn34.setTextColor(getResources().getColor(R.color.colorGray2));

        switch(meal1[selected_user]){
            case 1:
                btn11.setTextColor(getResources().getColor(R.color.colorMagenta));
                btn11.setSelected(true);
                meal_morning = 1;
                break;
            case 2:
                btn12.setTextColor(getResources().getColor(R.color.colorMagenta));
                btn12.setSelected(true);
                meal_morning = 2;
                break;
            case 3:
                btn13.setTextColor(getResources().getColor(R.color.colorMagenta));
                btn13.setSelected(true);
                meal_morning = 3;
                break;
            case 4:
                btn14.setTextColor(getResources().getColor(R.color.colorMagenta));
                btn14.setSelected(true);
                meal_morning = 4;
                break;
        }
        switch(meal2[selected_user]){
            case 1:
                btn21.setTextColor(getResources().getColor(R.color.colorMagenta));
                btn21.setSelected(true);
                meal_afternoon = 1;
                break;
            case 2:
                btn22.setTextColor(getResources().getColor(R.color.colorMagenta));
                btn22.setSelected(true);
                meal_afternoon = 2;
                break;
            case 3:
                btn23.setTextColor(getResources().getColor(R.color.colorMagenta));
                btn23.setSelected(true);
                meal_afternoon = 3;
                break;
            case 4:
                btn24.setTextColor(getResources().getColor(R.color.colorMagenta));
                btn24.setSelected(true);
                meal_afternoon = 4;
                break;
        }
        switch(meal3[selected_user]){
            case 1:
                btn31.setTextColor(getResources().getColor(R.color.colorMagenta));
                btn31.setSelected(true);
                meal_night = 1;
                break;
            case 2:
                btn32.setTextColor(getResources().getColor(R.color.colorMagenta));
                btn32.setSelected(true);
                meal_night = 2;
                break;
            case 3:
                btn33.setTextColor(getResources().getColor(R.color.colorMagenta));
                btn33.setSelected(true);
                meal_night = 3;
                break;
            case 4:
                btn34.setTextColor(getResources().getColor(R.color.colorMagenta));
                btn34.setSelected(true);
                meal_night = 4;
                break;
        }


    }

    public void onClick_01btn(View v){
        saveCurrentViewState();

        if(btn02.isSelected()){
            btn02.setSelected(false);
            btn02.setTextColor(getResources().getColor(R.color.colorGray2));
            btn02.setBackgroundColor(Color.TRANSPARENT);

        }else if(btn03.isSelected()){
            btn03.setSelected(false);
            btn03.setTextColor(getResources().getColor(R.color.colorGray2));
            btn03.setBackgroundColor(Color.TRANSPARENT);
            programContent[0] = morning_et.getText().toString();
            programContent[1] = afternoon_et.getText().toString();
            programContent[2] = night_et.getText().toString();
        }
        btn01.setTextColor(getResources().getColor(R.color.colorCyanMain));
        btn01.setBackgroundResource(R.drawable.shape_radius_rect_selected_blue);
        btn01.setSelected(true);
        selected_tab = 1;
        setTabView();

        et.setText(userContent[selected_user]);


    }
    public void onClick_02btn(View v){
        saveCurrentViewState();

        if(btn01.isSelected()){
            btn01.setSelected(false);
            btn01.setTextColor(getResources().getColor(R.color.colorGray2));
            btn01.setBackgroundColor(Color.TRANSPARENT);
        }else if(btn03.isSelected()){
            btn03.setSelected(false);
            btn03.setTextColor(getResources().getColor(R.color.colorGray2));
            btn03.setBackgroundColor(Color.TRANSPARENT);
            programContent[0] = morning_et.getText().toString();
            programContent[1] = afternoon_et.getText().toString();
            programContent[2] = night_et.getText().toString();
        }
        btn02.setTextColor(getResources().getColor(R.color.colorCyanMain));
        btn02.setBackgroundResource(R.drawable.shape_radius_rect_selected_blue);
        btn02.setSelected(true);
        selected_tab = 2;
        setTabView();

        loadCurrentUserMealStatus();
    }
    public void onClick_03btn(View v){
        saveCurrentViewState();

        if(btn01.isSelected()){
            btn01.setSelected(false);
            btn01.setTextColor(getResources().getColor(R.color.colorGray2));
            btn01.setBackgroundColor(Color.TRANSPARENT);
        }else if(btn02.isSelected()){
            btn02.setSelected(false);
            btn02.setTextColor(getResources().getColor(R.color.colorGray2));
            btn02.setBackgroundColor(Color.TRANSPARENT);
        }
        btn03.setTextColor(getResources().getColor(R.color.colorCyanMain));
        btn03.setBackgroundResource(R.drawable.shape_radius_rect_selected_blue);
        btn03.setSelected(true);
        selected_tab = 3;

        setTabView();       //if changed data is not applied, should call in front of chaging data sequence

        morning_et.setText(programContent[0]);
        afternoon_et.setText(programContent[1]);
        night_et.setText(programContent[2]);

    }

    public void setTabView(){
        //set view three tab layout
        switch(selected_tab){
            case 1:
                gridView.setVisibility(View.VISIBLE);
                et.setVisibility(View.VISIBLE);
                secondtab.setVisibility(View.GONE);
                thirdtab.setVisibility(View.GONE);
                break;
            case 2:
                gridView.setVisibility(View.VISIBLE);
                et.setVisibility(View.GONE);
                secondtab.setVisibility(View.VISIBLE);
                thirdtab.setVisibility(View.GONE);
                break;
            case 3:
                gridView.setVisibility(View.GONE);
                et.setVisibility(View.GONE);
                secondtab.setVisibility(View.GONE);
                thirdtab.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void onClick_11btn(View v){
        if(btn12.isSelected()){
            btn12.setSelected(false);
            btn12.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn13.isSelected()){
            btn13.setSelected(false);
            btn13.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn14.isSelected()){
            btn14.setSelected(false);
            btn14.setTextColor(getResources().getColor(R.color.colorGray2));
        }
        btn11.setTextColor(getResources().getColor(R.color.colorMagenta));
        btn11.setSelected(true);
        meal_morning = 1;
        meal1[selected_user] = 1;
    }
    public void onClick_12btn(View v){
        if(btn11.isSelected()){
            btn11.setSelected(false);
            btn11.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn13.isSelected()){
            btn13.setSelected(false);
            btn13.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn14.isSelected()){
            btn14.setSelected(false);
            btn14.setTextColor(getResources().getColor(R.color.colorGray2));
        }

        btn12.setTextColor(getResources().getColor(R.color.colorMagenta));
        btn12.setSelected(true);
        meal_morning = 2;
        meal1[selected_user] = 2;
    }
    public void onClick_13btn(View v){
        if(btn11.isSelected()){
            btn11.setSelected(false);
            btn11.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn12.isSelected()){
            btn12.setSelected(false);
            btn12.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn14.isSelected()){
            btn14.setSelected(false);
            btn14.setTextColor(getResources().getColor(R.color.colorGray2));
        }
        btn13.setTextColor(getResources().getColor(R.color.colorMagenta));
        btn13.setSelected(true);
        meal_morning = 3;
        meal1[selected_user] = 3;
    }
    public void onClick_14btn(View v){
        if(btn11.isSelected()){
            btn11.setSelected(false);
            btn11.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn12.isSelected()){
            btn12.setSelected(false);
            btn12.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn13.isSelected()){
            btn13.setSelected(false);
            btn13.setTextColor(getResources().getColor(R.color.colorGray2));
        }
        btn14.setTextColor(getResources().getColor(R.color.colorMagenta));
        btn14.setSelected(true);
        meal_morning = 4;
        meal1[selected_user] = 4;
    }
    public void onClick_21btn(View v){
        if(btn22.isSelected()){
            btn22.setSelected(false);
            btn22.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn23.isSelected()){
            btn23.setSelected(false);
            btn23.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn24.isSelected()){
            btn24.setSelected(false);
            btn24.setTextColor(getResources().getColor(R.color.colorGray2));
        }
        btn21.setTextColor(getResources().getColor(R.color.colorMagenta));
        btn21.setSelected(true);
        meal_afternoon = 1;
        meal2[selected_user] = 1;
    }
    public void onClick_22btn(View v){
        if(btn21.isSelected()){
            btn21.setSelected(false);
            btn21.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn23.isSelected()){
            btn23.setSelected(false);
            btn23.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn24.isSelected()){
            btn24.setSelected(false);
            btn24.setTextColor(getResources().getColor(R.color.colorGray2));
        }

        btn22.setTextColor(getResources().getColor(R.color.colorMagenta));
        btn22.setSelected(true);
        meal_afternoon = 2;
        meal2[selected_user] = 2;
    }
    public void onClick_23btn(View v){
        if(btn21.isSelected()){
            btn21.setSelected(false);
            btn21.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn22.isSelected()){
            btn22.setSelected(false);
            btn22.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn24.isSelected()){
            btn24.setSelected(false);
            btn24.setTextColor(getResources().getColor(R.color.colorGray2));
        }
        btn23.setTextColor(getResources().getColor(R.color.colorMagenta));
        btn23.setSelected(true);
        meal2[selected_user] = 3;
    }
    public void onClick_24btn(View v){
        if(btn21.isSelected()){
            btn21.setSelected(false);
            btn21.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn22.isSelected()){
            btn22.setSelected(false);
            btn22.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn23.isSelected()){
            btn23.setSelected(false);
            btn23.setTextColor(getResources().getColor(R.color.colorGray2));
        }
        btn24.setTextColor(getResources().getColor(R.color.colorMagenta));
        btn24.setSelected(true);
        meal_afternoon = 4;
        meal2[selected_user] = 4;
    }
    public void onClick_31btn(View v){
        if(btn32.isSelected()){
            btn32.setSelected(false);
            btn32.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn33.isSelected()){
            btn33.setSelected(false);
            btn33.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn34.isSelected()){
            btn34.setSelected(false);
            btn34.setTextColor(getResources().getColor(R.color.colorGray2));
        }
        btn31.setTextColor(getResources().getColor(R.color.colorMagenta));
        btn31.setSelected(true);
        meal_night = 1;
        meal3[selected_user] = 1;
    }
    public void onClick_32btn(View v){
        if(btn31.isSelected()){
            btn31.setSelected(false);
            btn31.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn33.isSelected()){
            btn33.setSelected(false);
            btn33.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn34.isSelected()){
            btn34.setSelected(false);
            btn34.setTextColor(getResources().getColor(R.color.colorGray2));
        }
        btn32.setTextColor(getResources().getColor(R.color.colorMagenta));
        btn32.setSelected(true);
        meal_night = 2;
        meal3[selected_user] = 2;
    }
    public void onClick_33btn(View v){
        if(btn31.isSelected()){
            btn31.setSelected(false);
            btn31.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn32.isSelected()){
            btn32.setSelected(false);
            btn32.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn34.isSelected()){
            btn34.setSelected(false);
            btn34.setTextColor(getResources().getColor(R.color.colorGray2));
        }
        btn33.setTextColor(getResources().getColor(R.color.colorMagenta));
        btn33.setSelected(true);
        meal_night = 3;
        meal3[selected_user] = 3;
    }
    public void onClick_34btn(View v){
        if(btn31.isSelected()){
            btn31.setSelected(false);
            btn31.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn32.isSelected()){
            btn32.setSelected(false);
            btn32.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn33.isSelected()){
            btn33.setSelected(false);
            btn33.setTextColor(getResources().getColor(R.color.colorGray2));
        }
        btn34.setTextColor(getResources().getColor(R.color.colorMagenta));
        btn34.setSelected(true);
        meal_night = 4;
        meal3[selected_user] = 4;
    }


}
