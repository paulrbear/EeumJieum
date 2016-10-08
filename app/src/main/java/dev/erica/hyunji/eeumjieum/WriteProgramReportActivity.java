package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class WriteProgramReportActivity extends FragmentActivity {
    private String savedID;
    private int savedMode;
    private String objectName, writername, writerroom, day, title, tfdContent, totalphotoUrl;
    private int dayorder;
    private String mode;

    ArrayList<Integer> mThumbIds= new ArrayList<>();
    GridView gridView;
    int len;
    private ImageAdapter mAdapter;

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
        setContentView(R.layout.activity_write_program_report);

        Intent intent = getIntent();
        savedID = intent.getExtras().getString("userID");
        savedMode = intent.getExtras().getInt("userMode");
        mode = intent.getExtras().getString("mode");


        //grid view setting
        gridView = (GridView) findViewById(R.id.img_grid_view);

        if(mode.equals("album")){
            TextView title_tv = (TextView) findViewById(R.id.title_tv);
            title_tv.setText("앨범작성");
            EditText content_et = (EditText) findViewById(R.id.content_et);
            content_et.setText("소중한 순간을 올려주세요");
            content_et.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            content_et.setPadding(0,400,0,0);
            content_et.setTextColor(getResources().getColor(R.color.colorGray2));
            content_et.setFocusable(false);

        }



        mAdapter = new ImageAdapter(this);
        gridView.setAdapter(mAdapter);
        len = gridView.getCount();


    }

    public void onClick_cancelbtn(View v){
        CustomDialog dialog = new CustomDialog(this);
        dialog.show();
    }

    public void onClick_donebtn(View v){
        if(mode.equals("program")) {
            setDBdata();
            CustomDialog dialog = new CustomDialog(this);
            dialog.setBtn1Text("뒤로");
            dialog.setBtn2Text("등록");
            dialog.setMode(2);      //mode 2 for program report registeration
            dialog.setProgramReportData(objectName, writername, writerroom, day, dayorder, title, tfdContent, totalphotoUrl);
            dialog.setDialogMsg("프로그램 일지를 \n등록하시겠습니까?");
            dialog.show();
        }else {
            if(mThumbIds.size()==0){
                Toast.makeText(getApplicationContext(), "사진을 선택하세요:)", Toast.LENGTH_SHORT).show();
                return;
            }
            setDBdata();
            CustomDialog dialog = new CustomDialog(this);
            dialog.setBtn1Text("뒤로");
            dialog.setBtn2Text("등록");
            dialog.setMode(6);      //mode 2 for program report registeration
            dialog.setAlbumData(objectName, writername, writerroom, day, dayorder, title, tfdContent, totalphotoUrl);
            dialog.setDialogMsg("현재 내용을 \n등록하시겠습니까?");
            dialog.show();
        }
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
        int day_of_month = oCalendar.get(Calendar.DAY_OF_MONTH);
        day = year + "/" + month + "/" + day_of_month;

        EditText tfd = (EditText) findViewById(R.id.content_et);
        tfdContent = tfd.getText().toString();
        tfd = (EditText) findViewById(R.id.title_et);
        title = tfd.getText().toString();

        if(mode.equals("program")) {
            dayorder = handler.getDayOrder(day, "PROGRAM") + 1;
        }else{
            dayorder = handler.getDayOrder(day, "ALBUM") + 1;
        }

    }

    public void onClick_photobtn(View v){
        Intent intent = new Intent(this, ImageListActivity.class);
        intent.putExtra("mode", "program");
        startActivityForResult(intent, 1);
    }

    public void onClick_personbtn(View v){
        Intent intent = new Intent(this, SelectMultiRoomUserActivity.class);
        intent.putExtra("mode", "program");
        startActivityForResult(intent, 1);
    }


    //tag person and select photo activity finished
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1 && data != null) {               //photoselected
            ArrayList<Integer> tmp = data.getIntegerArrayListExtra("result");

            Iterator iterator = tmp.iterator();
            totalphotoUrl = tmp.size() + "/";
            while (iterator.hasNext()){
                Integer element = (Integer) iterator.next();
                mThumbIds.add(element);
                totalphotoUrl = totalphotoUrl + element.toString() + "/";
            }if(mThumbIds.size() > 0) {

                if(mode.equals("album")){
                    EditText content_et = (EditText) findViewById(R.id.content_et);
                    content_et.setVisibility(View.GONE);

                    gridView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    gridView.setBackgroundColor(getResources().getColor(R.color.colorGray1));
                }

                gridView.setVisibility(View.VISIBLE);
                Button photobtn = (Button) findViewById(R.id.photo_btn);
                photobtn.setBackgroundResource(R.drawable.addphoto_active);
                mAdapter.notifyDataSetChanged();
            }

            Toast.makeText(getBaseContext(), ""+tmp.size(), Toast.LENGTH_SHORT).show();
            TextView tv = (TextView) findViewById(R.id.photo_count_tv);
            tv.setVisibility(View.VISIBLE);
            tv.setText(""+tmp.size());


        }
        else if(resultCode == 2 & data != null){                //personselected
            ArrayList<String> tmp = data.getStringArrayListExtra("result");

            Iterator iterator = tmp.iterator();
            objectName = tmp.size() + "/";
            while (iterator.hasNext()){
                String element = (String) iterator.next();
                objectName = objectName + element + "/";
            }

            Toast.makeText(getBaseContext(), ""+tmp.size(), Toast.LENGTH_SHORT).show();
            if(tmp.size() > 0) {
                Button person = (Button) findViewById(R.id.tag_person_btn);
                person.setBackgroundResource(R.drawable.tag_active);

                TextView tv = (TextView) findViewById(R.id.person_count_tv);
                tv.setVisibility(View.VISIBLE);
                tv.setText("" + tmp.size());
            }
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
                holder.selectedtxt.setTextColor(getResources().getColor(R.color.colorGray3));
                holder.selectedtxt.setText("X");
                holder.selectedimg.bringToFront();
                holder.selectedtxt.bringToFront();

            }else{

                holder = (ViewHolder) convertView.getTag();
                holder.selectedimg.setVisibility(View.VISIBLE);
                holder.selectedtxt.setVisibility(View.VISIBLE);
                holder.selectedtxt.setBackgroundResource(R.drawable.shape_oval_photo_delete);
                holder.selectedtxt.setTextColor(getResources().getColor(R.color.colorGray3));
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
                        TextView photo_count_tv = (TextView) findViewById(R.id.photo_count_tv);
                        photo_count_tv.setText(""+mThumbIds.size());

                    }else if(mThumbIds.size() == 1){
                        System.out.println("this is the last item");
                        gridView.setVisibility(View.GONE);
                        Button photobtn = (Button) findViewById(R.id.photo_btn);
                        photobtn.setBackgroundResource(R.drawable.addphoto);
                        mThumbIds.remove(id);
                        TextView photo_count_tv = (TextView) findViewById(R.id.photo_count_tv);
                        photo_count_tv.setVisibility(View.GONE);

                        EditText content_et = (EditText) findViewById(R.id.content_et);
                        content_et.setVisibility(View.VISIBLE);



                    }else{

                    }

                }
            });

            holder.imageview.setImageResource(mThumbIds.get(position));
            holder.id = position;
            return convertView;

        }

    }




}
