package dev.erica.hyunji.eeumjieum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class WriteNoticeActivity extends FragmentActivity {
    private Button roombtn1, roombtn2, roombtn3, roombtn4, roombtn5, photobtn;
    private Animation roomup, roomdown;
    Button pickdatebtn, picktimebtn;

    EditText et1, et2;
    String str_title, str_content;
    String savedID, writername, writerroom, totalphotoUrl;
    int savedMode, dayorder;

    boolean room_opened = false;
    String selected_room = "시설전체";
    String string_selected_day;
    boolean et1erased = false;
    boolean et2erased = false;

    ArrayList<Integer> mThumbIds= new ArrayList<>();
    EditText tfd;
    GridView gridView;
    int len;
    private ImageAdapter mAdapter;
    int selected_date = 0;

    class ViewHolder{
        ImageView imageview;
        ImageView selectedimg;
        TextView selectedtxt;
        int id;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_notice);

        Intent intent = getIntent();
        savedID = intent.getExtras().getString("userID");
        savedMode = intent.getExtras().getInt("userMode");
        if(savedMode == 0) {
            SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
            savedID = pref.getString("id", "");
            savedMode = pref.getInt("mode", 0);
        }


        pickdatebtn = (Button) findViewById(R.id.pick_date_btn);
        picktimebtn = (Button) findViewById(R.id.pick_time_btn);

        roombtn1 = (Button) findViewById(R.id.tfdbtn1);
        roombtn2 = (Button) findViewById(R.id.tfdbtn2);
        roombtn3 = (Button) findViewById(R.id.tfdbtn3);
        roombtn4 = (Button) findViewById(R.id.tfdbtn4);
        roombtn5 = (Button) findViewById(R.id.tfdbtn5);
        photobtn = (Button) findViewById(R.id.photo_btn);


        roomdown = AnimationUtils.loadAnimation(this, R.anim.roomlist_down_animation);
        roomup = AnimationUtils.loadAnimation(this, R.anim.roomlist_up_animation);

        //if edit text touched, erase default text
        et1 = (EditText)findViewById(R.id.title_et);
        et1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(!et1erased) {
                            et1.setText("");
                            et1.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                            et1.setTextColor(Color.BLACK);
                            et1erased = true;
                            break;
                        }
                }
                return false;
            }
        });

        //if edit text touched, erase default text
        et2 = (EditText)findViewById(R.id.content_tfd);
        et2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(!et2erased) {
                            et2.setText("");
                            et2.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                            et2.setTextColor(Color.BLACK);
                            et2erased = true;
                            break;
                        }
                }
                return false;
            }
        });

        gridView = (GridView) findViewById(R.id.img_grid_view);

        mAdapter = new ImageAdapter(this);
        gridView.setAdapter(mAdapter);
        len = gridView.getCount();

    }

    public void onClick_cancelbtn(View v){
        CustomDialog dialog = new CustomDialog(this);
        dialog.show();
    }

    public void onClick_donebtn(View v){
        setDBdata();
        CustomDialog dialog = new CustomDialog(this);
        dialog.setBtn1Text("뒤로");
        dialog.setBtn2Text("등록");
        dialog.setMode(5);
        dialog.setNoticeData(selected_room, writername, writerroom, string_selected_day, dayorder, str_title, str_content, totalphotoUrl);
        dialog.setDialogMsg("시설 공지를 \n등록하시겠습니까?");
        dialog.show();
    }

    public void setDBdata(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        List<String> dbresult = handler.getLoginUserData(savedID, 2);
        if(dbresult.size() > 0) {
            writername = dbresult.get(0);
            writerroom = dbresult.get(1);
        }else{
            writername = "unRegistered";
            writerroom = "unRegistered";
        }

        str_title = et1.getText().toString();
        str_content = et2. getText().toString();

        if(mThumbIds.size() == 0) {
            totalphotoUrl = mThumbIds.size() + "/";
        }else{
            Iterator iterator = mThumbIds.iterator();
            totalphotoUrl = mThumbIds.size() + "/";
            while (iterator.hasNext()) {
                Integer element = (Integer) iterator.next();
                totalphotoUrl = totalphotoUrl + element.toString() + "/";
            }
        }

        Calendar mCal = Calendar.getInstance();
        int year = mCal.get(mCal.YEAR);
        int month = mCal.get(mCal.MONTH) + 1;
        int intday = mCal.get(mCal.DAY_OF_MONTH);

        String day = year + "/" + month + "/" + intday;
        string_selected_day = day;

        dayorder = handler.getDayOrder(string_selected_day, "NOTICE") + 1;

        System.out.println(writername+"##############################################");
    }



    public void onClick_photobtn(View v){
        //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        //startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
        Intent intent = new Intent(getApplicationContext(), ImageListActivity.class);
        intent.putExtra("mode", "notice");
        startActivityForResult(intent, 1);
    }



    //custom gallery
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1 && data != null) {
            ArrayList<Integer> tmp = data.getIntegerArrayListExtra("result");

            Toast.makeText(getBaseContext(), ""+tmp.size(), Toast.LENGTH_SHORT).show();
            Iterator iterator = tmp.iterator();

            while (iterator.hasNext()) {
                Integer element = (Integer) iterator.next();
                mThumbIds.add(element);
            }
            if(mThumbIds.size() > 0) {
                gridView.setVisibility(View.VISIBLE);
                photobtn.setBackgroundResource(R.color.colorAccent);
                mAdapter.notifyDataSetChanged();
            }
        }
        else{
        }

    }

    private class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;
        //DataSetObservable mDataSetObservable = new DataSetObservable();

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
        public View getView(int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.galleryitem, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
                holder.selectedimg = (ImageView) convertView.findViewById(R.id.selectedImg);
                holder.selectedtxt = (TextView) convertView. findViewById(R.id.selectedTxt);

                convertView.setTag(holder);

                holder.imageview.setLayoutParams(new FrameLayout.LayoutParams(220,220));
                holder.imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.selectedimg.setLayoutParams(new FrameLayout.LayoutParams(220,220));
                holder.selectedimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.selectedimg.setVisibility(View.VISIBLE);
                holder.selectedtxt.setVisibility(View.VISIBLE);
                holder.selectedtxt.setBackgroundResource(R.drawable.shape_oval_photo_delete);
                holder.selectedtxt.setTextColor(getResources().getColor(R.color.colorDDarkGray));
                holder.selectedtxt.setText("X");
                holder.selectedimg.bringToFront();
                holder.selectedtxt.bringToFront();

            }else{
                System.out.println("Here I am!!!!!!!!!!!!!!!!!!");

                holder = (ViewHolder) convertView.getTag();
                holder.selectedimg.setVisibility(View.VISIBLE);
                holder.selectedtxt.setVisibility(View.VISIBLE);
                holder.selectedtxt.setBackgroundResource(R.drawable.shape_oval_photo_delete);
                holder.selectedtxt.setTextColor(getResources().getColor(R.color.colorDDarkGray));
                holder.selectedtxt.setText("X");
                holder.selectedimg.bringToFront();
                holder.selectedtxt.bringToFront();
            }

            holder.imageview.setId(position);
            holder.selectedimg.setId(position);
            holder.selectedtxt.setId(position);
            holder.selectedimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    if(mThumbIds.size() > 1) {
                        System.out.println("hi we have plenty of items");
                        holder.selectedimg.setVisibility(View.GONE);
                        holder.selectedtxt.setVisibility(View.GONE);
                        mThumbIds.remove(id);
                        mAdapter.notifyDataSetChanged();
                    }else if(mThumbIds.size() == 1){
                        System.out.println("this is the last item");
                        gridView.setVisibility(View.GONE);
                        photobtn.setBackgroundResource(R.drawable.album_click);
                        mThumbIds.remove(id);
                    }else{

                    }

                }
            });

            holder.imageview.setImageResource(mThumbIds.get(position));
            holder.id = position;
            return convertView;

        }

    }


    //roomlist tfd click listner
    public void onClick_roomlist(View v){
        if(v.getId() == R.id.tfdbtn1 && room_opened == true){
            roombtn1.bringToFront();
            setAnimation(View.GONE, roomup);
            room_opened = false;
        }else if(v.getId() == R.id.tfdbtn1 && room_opened == false){
            roombtn2.setVisibility(View.VISIBLE);
            roombtn3.setVisibility(View.VISIBLE);
            roombtn4.setVisibility(View.VISIBLE);
            roombtn5.setVisibility(View.VISIBLE);
            setAnimation(View.VISIBLE, roomdown);
            room_opened = true;
            return;
        }else if(v.getId() == R.id.tfdbtn2){
            roombtn1.bringToFront();
            selected_room = "시설전체";
            roombtn1.setText(selected_room);
            setAnimation(View.GONE, roomup);
            room_opened = false;
        }else if(v.getId() == R.id.tfdbtn3){
            roombtn1.bringToFront();
            selected_room = (String)roombtn3.getText();
            roombtn1.setText(selected_room);
            setAnimation(View.GONE, roomup);
            room_opened = false;
        }else if(v.getId() == R.id.tfdbtn4){
            roombtn1.bringToFront();
            selected_room = (String)roombtn4.getText();
            roombtn1.setText(selected_room);
            setAnimation(View.GONE, roomup);
            room_opened = false;
        }else if(v.getId() == R.id.tfdbtn5){
            roombtn1.bringToFront();
            selected_room = (String)roombtn5.getText();
            roombtn1.setText(selected_room);
            setAnimation(View.GONE, roomup);
            room_opened = false;
        }
    }


    //room list animation
    private void setAnimation(final int btnStatus, final Animation animation){
        animation.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(btnStatus == View.GONE) {
                    roombtn2.setVisibility(btnStatus);
                    roombtn3.setVisibility(btnStatus);
                    roombtn4.setVisibility(btnStatus);
                    roombtn5.setVisibility(btnStatus);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        roombtn2.startAnimation(animation);
        roombtn3.startAnimation(animation);
        roombtn4.startAnimation(animation);
        roombtn5.startAnimation(animation);
    }





}
