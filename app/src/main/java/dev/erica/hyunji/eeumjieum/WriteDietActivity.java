package dev.erica.hyunji.eeumjieum;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

public class WriteDietActivity extends FragmentActivity {

    RelativeLayout temp_view;
    EditText et1, et2, et3;
    ImageView iv1, iv2, iv3;
    Button pickdatebtn;
    String mode;

    String savedID;
    InputMethodManager imm;

    boolean et1_clicked = false;
    boolean et2_clicked = false;
    boolean et3_clicked = false;

    int photo1 = -1;
    int photo2 = -1;
    int photo3 = -1;
    String morning, afternoon, night;

    int selected_date;
    String string_selected_day;
    int articleKey;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diet);


        //user mode and ID setting
        Intent intent = getIntent();
        savedID = intent.getExtras().getString("userID");
        articleKey = intent.getExtras().getInt("articleKey");
        mode = intent.getExtras().getString("mode");

        temp_view = (RelativeLayout) findViewById(R.id.detail_view_mainview);
        et1 = (EditText) findViewById(R.id.morning_et);
        et2 = (EditText) findViewById(R.id.afternoon_et);
        et3 = (EditText) findViewById(R.id.night_et);
        iv1 = (ImageView) findViewById(R.id.morning_iv);
        iv2 = (ImageView) findViewById(R.id.afternoon_iv);
        iv3 = (ImageView) findViewById(R.id.night_iv);

        pickdatebtn = (Button) findViewById(R.id.day_btn);

        temp_view.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        break;
                }
                return false;
            }
        });

        if(mode.equals("write")) {
            //if edit text touched, erase default text

            et1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if(!et1_clicked){
                                et1_clicked=true;
                                et1.setText("");
                                et1.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                            }
                            break;
                    }
                    return false;
                }
            });

            //if edit text touched, erase default text

            et2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if(!et2_clicked) {
                                et2_clicked=true;
                                et2.setText("");
                                et2.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                            }
                            break;

                    }
                    return false;
                }
            });
            //if edit text touched, erase default text

            et3.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if(!et3_clicked){
                                et3_clicked=true;
                                et3.setText("");
                                et3.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                            }
                            break;
                    }
                    return false;
                }
            });

            initDate();

        }else{      //detailview

            TextView titletv = (TextView) findViewById(R.id.title_tv);
            titletv.setText("식단표");
            Button del = (Button) findViewById(R.id.donebtn);
            del.setVisibility(View.GONE);
            del = (Button) findViewById(R.id.backbtn);
            del.setVisibility(View.VISIBLE);
            iv1.setClickable(false);
            iv2.setClickable(false);
            iv3.setClickable(false);
            et1.setFocusable(false);
            et2.setFocusable(false);
            et3.setFocusable(false);
            et1.setBackgroundColor(Color.TRANSPARENT);
            et2.setBackgroundColor(Color.TRANSPARENT);
            et3.setBackgroundColor(Color.TRANSPARENT);
            pickdatebtn.setClickable(false);
            //DB data read and set it
            setArticle();
        }


        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);


    }

    public void setArticle(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        DietListItem tmp = handler.getDietbyID(articleKey);
        iv1.setImageResource(tmp.getPhoto_morning());
        iv2.setImageResource(tmp.getPhoto_afternoon());
        iv3.setImageResource(tmp.getPhoto_night());
        et1.setText(tmp.getStr_morning());
        et2.setText(tmp.getStr_afternoon());
        et3.setText(tmp.getStr_night());

        String str = tmp.getDay();
        String[] arr = str.split("/");

        int year = Integer.parseInt(arr[0]);
        int month = Integer.parseInt(arr[1]);
        int intday = Integer.parseInt(arr[2]);


        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, intday);
        int dayofweek  = cal.get(Calendar.DAY_OF_WEEK);
        String str_day = month + "월 " + intday + "일 ";


        switch (dayofweek){
            case 1:
                str_day = str_day + "(일)";
                break;
            case 2:
                str_day = str_day + "(월)";
                break;
            case 3:
                str_day = str_day + "(화)";
                break;
            case 4:
                str_day = str_day + "(수)";
                break;
            case 5:
                str_day = str_day + "(목)";
                break;
            case 6:
                str_day = str_day + "(금)";
                break;
            case 7:
                str_day = str_day + "(토)";
                break;
        }

        pickdatebtn.setText(str_day);

    }

    public void onClick_cancelbtn(View v){
        CustomDialog dialog = new CustomDialog(this);
        dialog.show();
    }

    public void onClick_donebtn(View v){
        if(mode.equals("write")) {
            setDBdata();
            CustomDialog dialog = new CustomDialog(this);
            dialog.setBtn1Text("뒤로");
            dialog.setBtn2Text("등록");
            dialog.setMode(7);      //mode 7 for diet
            dialog.setDietData(string_selected_day, morning, afternoon, night, photo1, photo2, photo3);
            dialog.setDialogMsg("현재 식단을 \n등록하시겠습니까?");
            dialog.show();

        }
    }

    public void setDBdata(){
        morning = et1.getText().toString();
        afternoon = et2.getText().toString();
        night = et3.getText().toString();
    }

    public void onClick_photo(View v){
        int imageviewid = v.getId();
        if(mode.equals("write")){
            Intent intent = new Intent(this, ImageListActivity.class);
            intent.putExtra("mode", "diet");
            if(imageviewid == R.id.morning_iv) {
                intent.putExtra("imgfor", 1);
            }else if(imageviewid == R.id.afternoon_iv) {
                intent.putExtra("imgfor", 2);
            }else if(imageviewid == R.id.night_iv) {
                intent.putExtra("imgfor", 3);
            }
            startActivityForResult(intent, 1);

        }else{
        }
    }

    //custom gallery
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1 && data != null) {
            ArrayList<Integer> tmp = data.getIntegerArrayListExtra("result");
            int imgfor = data.getIntExtra("imgfor", 0);

            Toast.makeText(getBaseContext(), ""+tmp.size(), Toast.LENGTH_SHORT).show();
            Iterator iterator = tmp.iterator();

            while (iterator.hasNext()) {
                Integer element = (Integer) iterator.next();
                if (imgfor == 1) {
                    photo1 = element;
                    iv1.setImageResource(photo1);
                } else if (imgfor == 2) {
                    photo2 = element;
                    iv2.setImageResource(photo2);
                } else if (imgfor == 3) {
                    photo3 = element;
                    iv3.setImageResource(photo3);
                }
                System.out.println("result array" + element.toString());
            }
        }
        else{
        }

    }


    public void initDate(){

        Calendar cal = Calendar.getInstance(Locale.KOREA);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int intdate = cal.get(Calendar.DATE);
        string_selected_day = year + "/" + month + "/" + intdate;

        selected_date = intdate;

        String strmon, strday;
        strmon = String.valueOf(month);
        strday = String.valueOf(intdate);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);

        String str_day = strmon + "월 " + strday + "일 ";


        switch (dayofweek){
            case 1:
                str_day = str_day + "(일)";
                break;
            case 2:
                str_day = str_day + "(월)";
                break;
            case 3:
                str_day = str_day + "(화)";
                break;
            case 4:
                str_day = str_day + "(수)";
                break;
            case 5:
                str_day = str_day + "(목)";
                break;
            case 6:
                str_day = str_day + "(금)";
                break;
            case 7:
                str_day = str_day + "(토)";
                break;
        }

        pickdatebtn.setText(str_day);

    }

    public void onClick_pickdatebtn(View v){
        String arr[] = string_selected_day.split("/");
        int year = Integer.parseInt(arr[0]);
        int month = Integer.parseInt(arr[1]) - 1;
        int day = Integer.parseInt(arr[2]);
        DatePickerDialog dialog = new DatePickerDialog(this, datelistener, year, month, day);
        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener datelistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            int month = monthOfYear + 1;
            String strmon, strday;

            strmon = String.valueOf(month);
            strday = String.valueOf(dayOfMonth);

            Calendar cal = Calendar.getInstance(Locale.KOREA);
            cal.set(year, monthOfYear, dayOfMonth);
            int dayofweek = cal.get(Calendar.DAY_OF_WEEK);


            string_selected_day = year + "/" + month + "/" + dayOfMonth;
            selected_date = dayOfMonth;
            String str_day = strmon + "월 " + strday + "일 ";


            switch (dayofweek){
                case 1:
                    str_day = str_day + "(일)";
                    break;
                case 2:
                    str_day = str_day + "(월)";
                    break;
                case 3:
                    str_day = str_day + "(화)";
                    break;
                case 4:
                    str_day = str_day + "(수)";
                    break;
                case 5:
                    str_day = str_day + "(목)";
                    break;
                case 6:
                    str_day = str_day + "(금)";
                    break;
                case 7:
                    str_day = str_day + "(토)";
                    break;
            }
            pickdatebtn.setText(str_day);
            // Toast.makeText(getApplicationContext(), year + "/"+ monthOfYear + "/"+dayOfMonth, Toast.LENGTH_SHORT).show();
        }
    };




}
