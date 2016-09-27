package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

public class JoinParentActivity extends FragmentActivity {
    EditText temp_compNum, temp_email, temp_pwd, temp_repwd;
    RelativeLayout temp_view;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21){
            getWindow().setStatusBarColor(Color.parseColor("#F7F7F7"));
        }
        setContentView(R.layout.activity_join_parent);

        if(Build.VERSION.SDK_INT >= 21){
            getWindow().setStatusBarColor(Color.parseColor("#F7F7F7"));
        }


        EditText repwdInput = (EditText)findViewById(R.id.repwdtfd);
        repwdInput.setOnEditorActionListener(new OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    actual_join();
                    return true;
                }
                return false;
            }
        });

        //if main view touched, softkeyboard hiding
        temp_view = (RelativeLayout)findViewById(R.id.join_parent_mainview);
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
        temp_compNum = (EditText)findViewById(R.id.compNumtfd);
        temp_compNum.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        EditText compInput = (EditText) findViewById(R.id.compNumtfd);
                        compInput.setText("");
                        compInput.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        break;
                }
                return false;
            }
        });

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

        temp_repwd = (EditText)findViewById(R.id.repwdtfd);
        temp_repwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        EditText repwdInput = (EditText) findViewById(R.id.repwdtfd);
                        repwdInput.setText("");
                        repwdInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        repwdInput.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                        break;
                }
                return false;
            }
        });
    }


    public void onClick_backbtn(View v){
        finish();
    }

    public void onClick_joinbtn(View v){
        actual_join();
    }

    public void actual_join(){

        MyDBHandler handler = MyDBHandler.open(getApplicationContext());


        EditText compInput = (EditText) findViewById(R.id.compNumtfd);
        EditText idInput = (EditText) findViewById(R.id.emailtfd);
        EditText pwdInput = (EditText) findViewById(R.id.pwdtfd);
        EditText repwdInput = (EditText) findViewById(R.id.repwdtfd);

        String comp = compInput.getText().toString();
        String id = idInput.getText().toString();
        String pwd = pwdInput.getText().toString();
        String repwd = repwdInput.getText().toString();

        if(handler.ValidEmail(id)){
            if(pwd.equals(repwd)){
                handler.insertUser(comp, id, pwd, 1);
                Toast.makeText(JoinParentActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show();
                //finish();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1){
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                }else{
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(intent);

            }else{
                Toast.makeText(JoinParentActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(JoinParentActivity.this, "이미 사용중인 이메일입니다.", Toast.LENGTH_LONG).show();
        }

    }
}


 /*
    public void onClick_compNumtfd(View v){
        EditText compInput = (EditText) findViewById(R.id.compNumtfd);
        compInput.setText("");
        compInput.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
    }
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
    }
    public void onClick_repwdtfd(View v){
        EditText repwdInput = (EditText) findViewById(R.id.repwdtfd);
        repwdInput.setText("");
        repwdInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        repwdInput.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
    }
*/