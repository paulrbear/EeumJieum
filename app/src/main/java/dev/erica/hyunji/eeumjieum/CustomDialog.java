package dev.erica.hyunji.eeumjieum;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Erica on 2016-09-08.
 */
public class CustomDialog extends Dialog implements View.OnClickListener {
    Button btn1;
    Button btn2;
    Context context;
    String dbName;
    String objectname;
    String objectroom;
    String writer;
    String writerroom;
    String day;
    String tfdcontent;
    String photo;
    String location;
    String time;
    int mood;
    int activity;
    int sleep;
    int dayorder;
    String title;
    int mode = 1;   //1 :observe report, 2: program report, 3: work report
    String normalperson;
    String outperson;
    String hosplitalperson;
    String etcperson;
    String programtxt;
    String[] objectNamelist;
    int[] objectStatuslist;
    String[] objectContentlist;
    int[] meal1;
    int[] meal2;
    int[] meal3;
    int[] objectImg;

    String morning, afternoon, night;
    int photo1, photo2, photo3;


    ArrayList<PersonalWorkReportItem> personalreport = new ArrayList<>();

    public CustomDialog(Context context){
        super(context);

        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    public void onClick(View view){
        if(view == btn1){
            dismiss();
        }else if(view == btn2){
            if(btn2.getText().equals("취소")) {
                if (context instanceof Activity) {
                    ((Activity) context).setResult(-1);
                    ((Activity) context).finish();
                }
            }else if(btn2.getText().equals("등록") && mode == 1) {
                if (context instanceof Activity) {
                    MyDBHandler handler = MyDBHandler.open(context);
                    handler.insertObserve(objectname, objectroom, writer, writerroom, mood, activity, sleep, day, dayorder, tfdcontent, photo);
                    ((Activity) context).setResult(1);
                    ((Activity) context).finish();
                }
            }else if(btn2.getText().equals("등록") && mode == 2) {
                if (context instanceof Activity) {
                    MyDBHandler handler = MyDBHandler.open(context);
                    handler.insertProgram(objectname, writer, writerroom, day, dayorder, title, tfdcontent, photo);
                    ((Activity) context).setResult(2);
                    ((Activity) context).finish();
                }
            }else if(btn2.getText().equals("등록") && mode == 3) {
                if (context instanceof Activity) {
                    MyDBHandler handler = MyDBHandler.open(context);
                    handler.insertWorkReport(objectroom, day, normalperson, outperson, hosplitalperson, etcperson, programtxt);

                    WorkReportArticleItem tmp = handler.getWorkReportbyRoomDay(objectroom, day);
                    int articlekey = tmp.getArticleid();

                    for(int i = 0 ; i < objectNamelist.length; i++){
                        handler.insertPersonalWorkReport(articlekey, objectNamelist[i], objectroom, objectImg[i], objectStatuslist[i], objectContentlist[i], meal1[i], meal2[i], meal3[i]);
                    }
                    ((Activity) context).setResult(3);
                    ((Activity) context).finish();
                }
            }else if(btn2.getText().equals("등록") && mode == 4) {
                if (context instanceof Activity) {
                    MyDBHandler handler = MyDBHandler.open(context);
                    handler.insertSchedule(objectroom, writer, writerroom, day, dayorder, time, title, location, tfdcontent, photo);
                    ((Activity) context).setResult(4);
                    ((Activity) context).finish();
                }
            }else if(btn2.getText().equals("등록") && mode == 5) {
                if (context instanceof Activity) {
                    MyDBHandler handler = MyDBHandler.open(context);
                    handler.insertNotice(objectroom, writer, writerroom, day, dayorder, title, tfdcontent, photo);
                    ((Activity) context).setResult(5);
                    ((Activity) context).finish();
                }
            }else if(btn2.getText().equals("등록") && mode == 6) {
                if (context instanceof Activity) {
                    MyDBHandler handler = MyDBHandler.open(context);
                    handler.insertAlbum(objectname, writer, writerroom, day, dayorder, title, tfdcontent, photo);
                    ((Activity) context).setResult(6);
                    ((Activity) context).finish();
                }
            }else if(btn2.getText().equals("등록") && mode == 7) {
                if (context instanceof Activity) {
                    MyDBHandler handler = MyDBHandler.open(context);
                    handler.insertDiet(day, morning, afternoon, night, photo1, photo2, photo3);
                    ((Activity) context).setResult(7);
                    ((Activity) context).finish();
                }
            }

        }
    }

    public void setDialogMsg(String text){
        TextView tfd = (TextView) findViewById(R.id.dialog_tfd);
        tfd.setText(text);
    }

    public void setBtn1Text(String text){
        btn1.setText(text);
    }

    public void setBtn2Text(String text){
        btn2.setText(text);
    }

    public void setMode(int mode){
        this.mode = mode;
    }

    public void setObservReportData(String objectname, String objectroom, String writer, String writerroom, int mood, int activity, int sleep, String day, int dayorder, String tfdcontent, String photo){
        this.dbName = "OBSERVE";
        this.objectname = objectname;
        this.objectroom = objectroom;
        this.writer = writer;
        this.writerroom = writerroom;
        this.mood = mood;
        this.activity = activity;
        this.sleep = sleep;
        this.day = day;
        this.dayorder = dayorder;
        this.tfdcontent = tfdcontent;
        this.photo = photo;

    }

    public void setProgramReportData(String objectname, String writer, String writerroom, String day, int dayorder, String title, String tfdcontent, String photo){
        this.dbName = "PROGRAM";
        this.objectname = objectname;
        this.writer = writer;
        this.writerroom = writerroom;
        this.day = day;
        this.dayorder = dayorder;
        this.title = title;
        this.tfdcontent = tfdcontent;
        this.photo = photo;
    }

    public void setWorkReportData(String objectroom, String day, String normalperson, String outperson, String hosplitalperson, String etcperson, String programtxt, String[] objectName, int[] objectStatus, String[] objectContent, int[] meal1, int[] meal2, int[] meal3, int[] objectImg){
        this.objectroom = objectroom;
        this.day = day;
        this.normalperson = normalperson;
        this.outperson = outperson;
        this.hosplitalperson = hosplitalperson;
        this.etcperson = etcperson;
        this.programtxt = programtxt;
        this.objectNamelist = objectName.clone();
        this.objectStatuslist = objectStatus.clone();
        this.objectContentlist = objectContent.clone();
        this.meal1 = meal1.clone();
        this.meal2 = meal2.clone();
        this.meal3 = meal3.clone();
        this.objectImg = objectImg.clone();
    }

    public void setScheduleData(String objectroom, String writer, String writerroom, String day, int dayorder, String time, String title, String location, String tfdcontent, String photo){
        this.dbName = "SCHEDULE";
        this.objectroom = objectroom;
        this.writer = writer;
        this.writerroom = writerroom;
        this.title = title;
        this.location = location;
        this.day = day;
        this.time = time;
        this.dayorder = dayorder;
        this.tfdcontent = tfdcontent;
        this.photo = photo;

    }

    public void setNoticeData(String objectroom, String writer, String writerroom, String day, int dayorder, String title, String tfdcontent, String photo){
        this.dbName = "NOTICE";
        this.objectroom = objectroom;
        this.writer = writer;
        this.writerroom = writerroom;
        this.title = title;
        this.day = day;
        this.dayorder = dayorder;
        this.tfdcontent = tfdcontent;
        this.photo = photo;
    }

    public void setAlbumData(String objectname, String writer, String writerroom, String day, int dayorder, String title, String tfdcontent, String photo){
        this.dbName = "ALBUM";
        this.objectname = objectname;
        this.writer = writer;
        this.writerroom = writerroom;
        this.day = day;
        this.dayorder = dayorder;
        this.title = title;
        this.tfdcontent = tfdcontent;
        this.photo = photo;
    }

    public void setDietData(String day, String morning, String afternoon, String night, int photo1, int photo2, int photo3){
        this.day = day;
        this.morning = morning;
        this.afternoon = afternoon;
        this.night =night;
        this.photo1 = photo1;
        this.photo2 = photo2;
        this.photo3 = photo3;
    }


}
