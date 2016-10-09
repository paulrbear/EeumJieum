package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

public class WriteObservReport2Activity extends FragmentActivity {
    String savedID;
    String objectName;
    String selectedRoom;
    int mood = 2;
    int activity = 2;
    int sleep = 2;
    String writername;
    String writerroom;
    String day;
    int dayorder;
    String tfdContent;
    String totalphotoUrl;
    Button btn11, btn12, btn13, btn21, btn22, btn23, btn31, btn32, btn33, photobtn;
    //final int REQ_CODE_SELECT_IMAGE = 100;
    //private final int imgWidth = 50;
    //private final int imgHeight = 50;
    ArrayList<Integer> mThumbIds= new ArrayList<>();
    EditText tfd;
    GridView gridView;
    int len;
    private ImageAdapter mAdapter;
    boolean tfderased = false;
    int selected_date = 0;

    //ImageView iv;

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
        setContentView(R.layout.activity_write_observ_report2);

        Intent intent = getIntent();
        savedID = intent.getExtras().getString("userID");
        objectName = intent.getExtras().getString("objectName");
        selectedRoom = intent.getExtras().getString("selectedRoom");
        selected_date= intent.getExtras().getInt("selected_date");

        TextView tmp_fam = (TextView) findViewById(R.id.write_object_fam_tfd);
        TextView tmp_room = (TextView) findViewById(R.id.write_object_room_tfd);

        tmp_fam.setText(objectName+" 가족");
        tmp_fam.setPaintFlags(tmp_fam.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        tmp_room.setText(selectedRoom);


        //if edit text touched, erase default text
        EditText tmptfd = (EditText)findViewById(R.id.content_tfd);
        tmptfd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(!tfderased) {
                            EditText idInput = (EditText) findViewById(R.id.content_tfd);
                            idInput.setText("");
                            idInput.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                            idInput.setTextColor(Color.BLACK);
                            tfderased = true;
                            break;
                        }
                }
                return false;
            }
        });

        photobtn = (Button) findViewById(R.id.photo_btn);
        btn11 = (Button) findViewById(R.id.btn11);
        btn12 = (Button) findViewById(R.id.btn12);
        btn13 = (Button) findViewById(R.id.btn13);
        btn21 = (Button) findViewById(R.id.btn21);
        btn22 = (Button) findViewById(R.id.btn22);
        btn23 = (Button) findViewById(R.id.btn23);
        btn31 = (Button) findViewById(R.id.btn31);
        btn32 = (Button) findViewById(R.id.btn32);
        btn33 = (Button) findViewById(R.id.btn33);
        tfd = (EditText) findViewById(R.id.content_tfd);

        //iv = (ImageView) findViewById(R.id.imgView);
        //mThumbIds = new ArrayList<>();
        //grid view setting
        gridView = (GridView) findViewById(R.id.img_grid_view);

        mAdapter = new ImageAdapter(this);
        gridView.setAdapter(mAdapter);
        len = gridView.getCount();

    }

    public void onClick_cancelbtn(View v){
        CustomDialog dialog = new CustomDialog(this);
        dialog.show();
    }
    public void onClick_objectlayout(View v){
        CustomDialog dialog = new CustomDialog(this);
        dialog.show();
    }
    public void onClick_photobtn(View v){
        //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        //startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
        Intent intent = new Intent(WriteObservReport2Activity.this, ImageListActivity.class);
        intent.putExtra("mode", "observe");
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
                photobtn.setBackgroundResource(R.color.colorMagenta);
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

                holder.imageview.setLayoutParams(new FrameLayout.LayoutParams(240,240));
                holder.imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.selectedimg.setLayoutParams(new FrameLayout.LayoutParams(220,220));
                holder.selectedimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.selectedimg.setVisibility(View.VISIBLE);
                holder.selectedtxt.setVisibility(View.VISIBLE);
                holder.selectedtxt.setBackgroundResource(R.drawable.shape_oval_photo_delete);
                holder.selectedtxt.setTextColor(getResources().getColor(R.color.colorGray4));
                holder.selectedtxt.setText("X");
                holder.selectedimg.bringToFront();
                holder.selectedtxt.bringToFront();

            }else{
                System.out.println("Here I am!!!!!!!!!!!!!!!!!!");

                holder = (ViewHolder) convertView.getTag();
                holder.selectedimg.setVisibility(View.VISIBLE);
                holder.selectedtxt.setVisibility(View.VISIBLE);
                holder.selectedtxt.setBackgroundResource(R.drawable.shape_oval_photo_delete);
                holder.selectedtxt.setTextColor(getResources().getColor(R.color.colorGray4));
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

    public void onClick_donebtn(View v){
        setDBdata();
        CustomDialog dialog = new CustomDialog(this);
        dialog.setBtn1Text("뒤로");
        dialog.setBtn2Text("등록");
        dialog.setMode(1);
        dialog.setObservReportData(objectName, selectedRoom, writername, writerroom, mood, activity, sleep, day, dayorder, tfdContent, totalphotoUrl);
        dialog.setDialogMsg("관찰일지를 \n등록하시겠습니까?");
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

        Calendar oCalendar = Calendar.getInstance( );  // 현재 날짜/시간 등의 각종 정보 얻기
        int year =  oCalendar.get(Calendar.YEAR);
        int month = oCalendar.get(Calendar.MONTH) + 1;

        day = year + "/" + month + "/" + selected_date;

        tfdContent = tfd.getText().toString();

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

        dayorder = handler.getDayOrder(day, "OBSERVE") + 1;

    }

    public void onClick_11btn(View v){
        if(btn12.isSelected()){
            btn12.setSelected(false);
            btn12.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn13.isSelected()){
            btn13.setSelected(false);
            btn13.setTextColor(getResources().getColor(R.color.colorGray2));
        }
        btn11.setTextColor(getResources().getColor(R.color.colorCyanMain));
        btn11.setSelected(true);
        mood = 1;
    }
    public void onClick_12btn(View v){
        if(btn11.isSelected()){
            btn11.setSelected(false);
            btn11.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn13.isSelected()){
            btn13.setSelected(false);
            btn13.setTextColor(getResources().getColor(R.color.colorGray2));
        }

        btn12.setTextColor(getResources().getColor(R.color.colorBlueGray1));
        btn12.setSelected(true);
        mood = 2;
    }
    public void onClick_13btn(View v){
        if(btn11.isSelected()){
            btn11.setSelected(false);
            btn11.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn12.isSelected()){
            btn12.setSelected(false);
            btn12.setTextColor(getResources().getColor(R.color.colorGray2));
        }
        btn13.setTextColor(getResources().getColor(R.color.colorMagenta));
        btn13.setSelected(true);
        mood = 3;
    }
    public void onClick_21btn(View v){
        if(btn22.isSelected()){
            btn22.setSelected(false);
            btn22.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn23.isSelected()){
            btn23.setSelected(false);
            btn23.setTextColor(getResources().getColor(R.color.colorGray2));
        }
        btn21.setTextColor(getResources().getColor(R.color.colorCyanMain));
        btn21.setSelected(true);
        activity = 1;
    }
    public void onClick_22btn(View v){
        if(btn21.isSelected()){
            btn21.setSelected(false);
            btn21.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn23.isSelected()){
            btn23.setSelected(false);
            btn23.setTextColor(getResources().getColor(R.color.colorGray2));
        }

        btn22.setTextColor(getResources().getColor(R.color.colorBlueGray1));
        btn22.setSelected(true);
        activity = 2;
    }
    public void onClick_23btn(View v){
        if(btn21.isSelected()){
            btn21.setSelected(false);
            btn21.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn22.isSelected()){
            btn22.setSelected(false);
            btn22.setTextColor(getResources().getColor(R.color.colorGray2));
        }
        btn23.setTextColor(getResources().getColor(R.color.colorMagenta));
        btn23.setSelected(true);
        activity = 3;
    }
    public void onClick_31btn(View v){
        if(btn32.isSelected()){
            btn32.setSelected(false);
            btn32.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn33.isSelected()){
            btn33.setSelected(false);
            btn33.setTextColor(getResources().getColor(R.color.colorGray2));
        }
        btn31.setTextColor(getResources().getColor(R.color.colorCyanMain));
        btn31.setSelected(true);
        sleep = 1;
    }
    public void onClick_32btn(View v){
        if(btn31.isSelected()){
            btn31.setSelected(false);
            btn31.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn33.isSelected()){
            btn33.setSelected(false);
            btn33.setTextColor(getResources().getColor(R.color.colorGray2));
        }
        btn32.setTextColor(getResources().getColor(R.color.colorBlueGray1));
        btn32.setSelected(true);
        sleep = 2;
    }
    public void onClick_33btn(View v){
        if(btn31.isSelected()){
            btn31.setSelected(false);
            btn31.setTextColor(getResources().getColor(R.color.colorGray2));
        }else if(btn32.isSelected()){
            btn32.setSelected(false);
            btn32.setTextColor(getResources().getColor(R.color.colorGray2));
        }
        btn33.setTextColor(getResources().getColor(R.color.colorMagenta));
        btn33.setSelected(true);
        sleep = 3;
    }

}
