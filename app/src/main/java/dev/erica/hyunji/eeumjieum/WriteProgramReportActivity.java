package dev.erica.hyunji.eeumjieum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_program_report);

        Intent intent = getIntent();
        savedID = intent.getExtras().getString("userID");
        savedMode = intent.getExtras().getInt("userMode");
        mode = intent.getExtras().getString("mode");

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
                totalphotoUrl = totalphotoUrl + element.toString() + "/";
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
            TextView tv = (TextView) findViewById(R.id.person_count_tv);
            tv.setVisibility(View.VISIBLE);
            tv.setText(""+tmp.size());
        }

    }





}
