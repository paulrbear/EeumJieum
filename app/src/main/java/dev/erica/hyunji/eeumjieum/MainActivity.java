package dev.erica.hyunji.eeumjieum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;


public class MainActivity extends FragmentActivity {

    EditText temp_email, temp_pwd;
    RelativeLayout temp_view;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= 21){
            startActivity(new Intent(this, SplashActivity.class));  //start splash page
            getWindow().setStatusBarColor(Color.parseColor("#F7F7F7"));
        }

        setContentView(R.layout.activity_main);

        initDB();

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        String savedID = pref.getString("id","");
        Integer savedMode = pref.getInt("mode",0);
        Boolean login_enable = pref.getBoolean("login",false);

        if(login_enable){
                CharSequence text = "Hello " + savedID + " ! ";
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("userID", savedID);
                intent.putExtra("userMode",savedMode);
                startActivity(intent);  //start home activity
                overridePendingTransition(0,0);
                finish();
        }


        //if main view touched, softkeyboard hiding
        temp_view = (RelativeLayout)findViewById(R.id.mainactivity_mainview);
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

        //if edit text touched, erase default text
        temp_email = (EditText)findViewById(R.id.emailtfd);
        temp_email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        EditText idInput = (EditText) findViewById(R.id.emailtfd);
                        idInput.setText("");
                        idInput.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        break;
                }
                return false;
            }
        });

        temp_pwd = (EditText)findViewById(R.id.pwdtfd);
        temp_pwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        EditText pwdInput = (EditText) findViewById(R.id.pwdtfd);
                        pwdInput.setText("");
                        pwdInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        pwdInput.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                        break;
                }
                return false;
            }
        });

        EditText pwdInput = (EditText)findViewById(R.id.pwdtfd);
        pwdInput.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    actual_login();
                    return true;
                }
                return false;
            }
        });

    }

    public void actual_login(){
        EditText idInput, pwdInput;

        idInput = (EditText) findViewById(R.id.emailtfd);
        pwdInput = (EditText) findViewById(R.id.pwdtfd);

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String id = idInput.getText().toString();
        String pwd = pwdInput.getText().toString();
        int validationMode = loginValidation(id, pwd);

        if (validationMode == 1) {  //parent login
            editor.putString("id", id);
            editor.putInt("mode", validationMode);
            editor.putBoolean("login", true);


            editor.commit();

            Toast.makeText(MainActivity.this, "Parent Login Success", Toast.LENGTH_LONG).show();

            //clear activity stack and start home activity
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.putExtra("userID", id);
            intent.putExtra("userMode", 1);

            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1){
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            }else{

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            startActivity(intent);


        } else if(validationMode == 2) {    //worker login

            editor.putString("id", id);
            editor.putInt("mode", validationMode);
            editor.putBoolean("login", true);



            editor.commit();

            Toast.makeText(MainActivity.this, "Worker Login Success", Toast.LENGTH_LONG).show();

            //clear activity stack and start home activity
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.putExtra("userID", id);
            intent.putExtra("userMode", 2);

            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1){
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            }else{

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            startActivity(intent);
            //startActivity(new Intent(this, HomeActivity.class));  //start home page

        }else {
            Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

  /*
  public void onClick_emailtfd(View v){
        EditText idInput = (EditText) findViewById(R.id.emailtfd);
        idInput.setText("");
        idInput.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
    }

    public void onClick_pwdtfd(View v){
        EditText pwdInput = (EditText) findViewById(R.id.pwdtfd);
        pwdInput.setText("");
        pwdInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pwdInput.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
    }*/

    public void initDB(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        if( handler.isRoomDBEmpty()){
            handler.insertRoomUser("한지은", "기쁨방", R.drawable.uimg_circle_1);
            handler.insertRoomUser("백설호", "기쁨방", R.drawable.uimg_circle_1);
            handler.insertRoomUser("최미선", "기쁨방", R.drawable.uimg_circle_1);
            handler.insertRoomUser("박예은", "기쁨방", R.drawable.uimg_circle_1);

            handler.insertRoomUser("최민수", "믿음방", R.drawable.uimg_circle_1);

            handler.insertRoomUser("김종현", "은혜방", R.drawable.uimg_circle_1);
            handler.insertRoomUser("김태리", "은혜방", R.drawable.uimg_circle_1);
        }
        if(handler.isUserDBEmpty()){
            handler.insertUser("9999", "p", "p", 1);
            handler.insertUser("9999", "w", "w", 2);
        }
        if(handler.isWorkerDBEmpty()){
            handler.insertWorker("w", "이선웅", "은혜방", R.drawable.uimg_circle_1);
        }
        if(handler.isParentDBEmpty()){
            handler.insertParent("p", "한지은", "기쁨방", R.drawable.uimg_circle_1);
        }
    }

    public void onClick_login(View v){
       actual_login();
    }

    public int loginValidation(String id, String pwd){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());

       /* if(id.equals("w") && pwd.equals("w")){
            return 2;
        }else if(id.equals("p") && pwd.equals("p")){
            return 1;
        }else {*/
            Integer i = handler.ValidLogin(id, pwd);
           return i ;

        //}
    }


    public void onClick_join(View v){
        Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
        startActivity(intent);
    }

}
