package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erica on 2016-08-07.
 */
public class MyDBHandler {
    DBHelper helper;
    SQLiteDatabase db;

    public MyDBHandler(Context context){
        helper = new DBHelper(context, "AppTotalDB.sqlite", null, 1);
    }

    public static MyDBHandler open(Context context){
        return new MyDBHandler(context);
    }

    public void close(){
        db.close();
    }

    public void insertUser(String compNum, String email, String pwd, int mode){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO USER VALUES(null, '"+ compNum + "', '" + email + "', '" + pwd + "', " + mode + ");");
        db.close();
    }
    public void insertRoomUser(String userName, String roomType, int img){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO ROOM VALUES(null, '"+ userName + "', '" + roomType + "', " + img + ");");
        db.close();
    }
    public void insertWorker(String email, String name, String room, int img){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO WORKER VALUES(null, '"+ email + "', '" + name + "', '" + room + "', " + img + ");");
        db.close();
    }
    public void insertParent(String email, String name, String room, int img){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO PARENT VALUES(null, '"+ email + "', '" + name + "', '" + room + "', " + img + ");");
        db.close();
    }
    public void insertObserve(String objectname, String objectroom, String writer, String writerroom, int mood, int activity, int sleep, String day, int dayorder, String tfdcontent, String photo){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO OBSERVE VALUES(null, '"+ objectname + "', '" + objectroom + "', '" + writer + "', '" + writerroom + "', " + mood + ", " + activity + ", " + sleep + ", '"
                + day + "', " + dayorder + ", '" + tfdcontent + "', '" + photo + "');");
        db.close();
    }
    public void insertComment(String dbname, int observekey, String day, String time, int comment_order, String writer, String content_txt){
        SQLiteDatabase db = helper.getWritableDatabase();

            String sql = "INSERT INTO " + dbname + " VALUES(null, "+ observekey + ", '" + day + "', '" + time + "', "
                    + comment_order + ", '" + writer + "', '" + content_txt + "');";
            db.execSQL(sql);
            db.close();

    }
    public void insertProgram(String objectname, String writer, String writerroom, String day, int dayorder, String title, String tfdcontent, String photo){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO PROGRAM VALUES(null, '"+ objectname + "', '" + writer + "', '" + writerroom + "', '"
                + day + "', " + dayorder + ", '" + title + "', '" + tfdcontent + "', '" + photo + "');");
        db.close();
    }
    public void insertWorkReport(String objectroom, String day, String normalperson, String outperson, String hospitalperson, String etcperson, String programtxt){

        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO WORKREPORT VALUES(null, '"+ objectroom + "', '" + day + "', '" + normalperson + "', '" + outperson
                + "', '" + hospitalperson + "', '" + etcperson + "', '" + programtxt + "');");
        db.close();

    }
    public void insertPersonalWorkReport(int reportkey, String objectname, String objectroom, int uimage, int status, String content, int meal, int meal2, int meal3){


        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO PERSONALWORKREPORT VALUES(null, "+ reportkey + ", '" + objectname + "', '" + objectroom + "', " + uimage + ", "  + status
                + ", '" + content + "', " + meal + ", " + meal2 + ", " + meal3 + ");");
        db.close();
    }
    public void insertSchedule(String objectroom, String writer, String writerroom, String day, int dayorder, String time, String title, String location, String tfdcontent, String photo){

        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO SCHEDULE VALUES(null, '" + objectroom + "', '" + writer + "', '" + writerroom + "', '" + day + "', " + dayorder + ", '" + time + "', '"
                + title + "', '" + location + "', '" + tfdcontent + "', '" + photo + "');");
        db.close();

    }
    public void insertNotice(String objectroom, String writer, String writerroom, String day, int dayorder,String title, String tfdcontent, String photo){

        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO NOTICE VALUES(null, '" + objectroom + "', '" + writer + "', '" + writerroom + "', '" + day + "', " + dayorder + ", '"
                + title + "', '" + tfdcontent + "', '" + photo + "');");
        db.close();

    }

    public void insertAlbum(String objectname, String writer, String writerroom, String day, int dayorder, String title, String tfdcontent, String photo){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO ALBUM VALUES(null, '"+ objectname + "', '" + writer + "', '" + writerroom + "', '"
                + day + "', " + dayorder + ", '" + title + "', '" + tfdcontent + "', '" + photo + "');");
        db.close();
    }

    public void insertDiet(String day, String morning, String afternoon, String night, int photo1, int photo2, int photo3){
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("INSERT INTO DIET VALUES(null, '"+ day + "', '" + morning + "', '" + afternoon + "', '"
                    + night + "', " + photo1 + ", " + photo2 + ", " + photo3 + ");");
            db.close();
    }

    public ArrayList<DietListItem> getDietbyDay(String day){
        ArrayList<DietListItem> tmpArticle = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM DIET WHERE day LIKE '" + day + "%';";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            DietListItem tmp = new DietListItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7));
            tmpArticle.add(tmp);
            cursor.moveToNext();
        }

        cursor.close();

        return tmpArticle;
    }
    public DietListItem getDietbyID(int key){
        DietListItem tmpArticle;
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM DIET WHERE _id=" + key + ";";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        if(!cursor.isAfterLast()){
            tmpArticle = new DietListItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7));
            cursor.moveToNext();
        }else{
            return null;
        }

        cursor.close();

        return tmpArticle;
    }


    public ArrayList<ScheduleListItem> getSchedulebyDay(String day){
        ArrayList<ScheduleListItem> tmpArticle = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM SCHEDULE WHERE day LIKE '" + day + "%';";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            ScheduleListItem tmp = new ScheduleListItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                    cursor.getString(9), cursor.getString(10));
            tmpArticle.add(tmp);
            cursor.moveToNext();
        }

        cursor.close();

        return tmpArticle;
    }
    public ScheduleListItem getSchedulebyID(int key){
        ScheduleListItem tmpArticle = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM SCHEDULE WHERE _id=" + key + ";";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            tmpArticle = new ScheduleListItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                    cursor.getString(9), cursor.getString(10));
            cursor.moveToNext();
        }
        cursor.close();

        return tmpArticle;
    }
    public NoticeListItem getNoticebyID(int key){
        NoticeListItem tmpArticle;
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM NOTICE WHERE _id=" + key + ";";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        if(!cursor.isAfterLast()){
            tmpArticle = new NoticeListItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6),cursor.getString(7),
                    cursor.getString(8));

            cursor.moveToNext();
        }else{
            return null;
        }

        cursor.close();

        return tmpArticle;
    }
    public ArrayList<NoticeListItem> getNoticebyDay(String day){
        ArrayList<NoticeListItem> tmpArticle = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM NOTICE WHERE day LIKE '" + day + "%';";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            NoticeListItem tmp = new NoticeListItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6),cursor.getString(7),
                    cursor.getString(8));
            tmpArticle.add(tmp);
            cursor.moveToNext();
        }

        cursor.close();

        return tmpArticle;
    }
    public List<WorkReportArticleItem> getWorkReportbyDay(String day){
        List<WorkReportArticleItem> tmpArticle = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM WORKREPORT WHERE day='" + day + "';";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            WorkReportArticleItem tmp = new WorkReportArticleItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
            tmpArticle.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();

        return tmpArticle;
    }
    public WorkReportArticleItem getWorkReportbyRoomDay(String room, String day){
        WorkReportArticleItem tmpArticle = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM WORKREPORT WHERE objectroom='" + room + "' AND day='" + day + "';";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            tmpArticle = new WorkReportArticleItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
            cursor.moveToNext();
        }
        cursor.close();

        return tmpArticle;
    }
    public WorkReportArticleItem getWorkReportbyID(int key){
        WorkReportArticleItem tmpArticle = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM WORKREPORT WHERE _id=" + key + ";";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            tmpArticle = new WorkReportArticleItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
            cursor.moveToNext();
        }
        cursor.close();

        return tmpArticle;
    }
    public List<PersonalWorkReportItem> getPersonalWorkReportbyID(int reportkey){
        List<PersonalWorkReportItem> resultlist = new ArrayList<>();

        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM PERSONALWORKREPORT WHERE reportkey=" + reportkey + ";";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            PersonalWorkReportItem tmp = new PersonalWorkReportItem(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4),
                    cursor.getInt(5), cursor.getString(6), cursor.getInt(7), cursor.getInt(8), cursor.getInt(9));
            resultlist.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();

        return resultlist;

    }
    public List<String> getObservArticleWriter(int articleKey){
        SQLiteDatabase db = helper.getReadableDatabase();
        List<String> resultlist = new ArrayList<String>();
        String writername="";

            Cursor cursor = db.rawQuery("SELECT writer FROM OBSERVE WHERE _id=" + articleKey + ";", null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                writername = cursor.getString(0);
                cursor.moveToNext();
            }
            cursor.close();

        cursor = db.rawQuery("SELECT * FROM WORKER WHERE name='" + writername + "';", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            resultlist.add(writername);
            resultlist.add(cursor.getString(3));        //room
            resultlist.add(""+cursor.getInt(4));        //img
            cursor.moveToNext();
        }
        cursor.close();

        return resultlist;
    }
    public List<String> getProgramArticleWriter(int articleKey){
        SQLiteDatabase db = helper.getReadableDatabase();
        List<String> resultlist = new ArrayList<String>();
        String writername="";

        Cursor cursor = db.rawQuery("SELECT writer FROM PROGRAM WHERE _id=" + articleKey + ";", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            writername = cursor.getString(0);
            cursor.moveToNext();
        }
        cursor.close();

        cursor = db.rawQuery("SELECT * FROM WORKER WHERE name='" + writername + "';", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            resultlist.add(writername);
            resultlist.add(cursor.getString(3));        //room
            resultlist.add(""+cursor.getInt(4));        //img
            cursor.moveToNext();
        }
        cursor.close();

        return resultlist;
    }
    public List<String> getScheduleWriter(int articleKey){
        SQLiteDatabase db = helper.getReadableDatabase();
        List<String> resultlist = new ArrayList<String>();
        String writername="";

        Cursor cursor = db.rawQuery("SELECT writer FROM SCHEDULE WHERE _id=" + articleKey + ";", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            writername = cursor.getString(0);
            cursor.moveToNext();
        }
        cursor.close();

        cursor = db.rawQuery("SELECT * FROM WORKER WHERE name='" + writername + "';", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            resultlist.add(writername);
            resultlist.add(cursor.getString(3));        //room
            resultlist.add(""+cursor.getInt(4));        //img
            cursor.moveToNext();
        }
        cursor.close();

        return resultlist;
    }
    public List<String> getNoticeWriter(int articleKey){
        SQLiteDatabase db = helper.getReadableDatabase();
        List<String> resultlist = new ArrayList<String>();
        String writername="";

        Cursor cursor = db.rawQuery("SELECT writer FROM NOTICE WHERE _id=" + articleKey + ";", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            writername = cursor.getString(0);
            cursor.moveToNext();
        }
        cursor.close();

        cursor = db.rawQuery("SELECT * FROM WORKER WHERE name='" + writername + "';", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            resultlist.add(writername);
            resultlist.add(cursor.getString(3));        //room
            resultlist.add(""+cursor.getInt(4));        //img
            cursor.moveToNext();
        }
        cursor.close();

        return resultlist;
    }



    public List<RoomUserItem> getRoomUser(String roomType){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ROOM WHERE roomType='" + roomType + "';", null);
        List<RoomUserItem> resultlist = new ArrayList<>();

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            RoomUserItem tmp = new RoomUserItem(cursor.getString(1), cursor.getInt(3));
            resultlist.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();
        return resultlist;
    }
    public RoomUserItem getRoomUserImage(String userName, String roomType){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ROOM WHERE userName='" + userName + "' AND roomType='" + roomType+ "';", null);
        RoomUserItem tmp = null;

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            tmp = new RoomUserItem(cursor.getString(1), cursor.getInt(3));
            cursor.moveToNext();
        }
        cursor.close();
        return tmp;
    }
    public List<String> getLoginUserData(String id, int mode){
        SQLiteDatabase db = helper.getReadableDatabase();
        List<String> resultlist = new ArrayList<String>();
        if(mode == 1){  //login user is parent
            Cursor cursor = db.rawQuery("SELECT * FROM PARENT WHERE email='" + id + "';", null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                resultlist.add(cursor.getString(2));
                resultlist.add(cursor.getString(3));
                resultlist.add(""+cursor.getInt(4));
                cursor.moveToNext();
            }
            cursor.close();

        }else if(mode == 2){    //login user is worker
            Cursor cursor = db.rawQuery("SELECT * FROM WORKER WHERE email='" + id + "';", null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                resultlist.add(cursor.getString(2));        //name
                resultlist.add(cursor.getString(3));        //room
                resultlist.add(""+cursor.getInt(4));        //img
                cursor.moveToNext();
            }
            cursor.close();
        }

        return resultlist;
    }
    public List<String> getLoginUserData(String id){
        SQLiteDatabase db = helper.getReadableDatabase();
        List<String> resultlist = new ArrayList<String>();
        int mode;

        Cursor cursor = db.rawQuery("SELECT mode FROM USER WHERE email='" + id + "';", null);
        if(cursor.moveToFirst()){
            mode = cursor.getInt(0);
        }else{
            mode = 0;
        }

        if(mode == 1){  //login user is parent
            cursor = db.rawQuery("SELECT * FROM PARENT WHERE email='" + id + "';", null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                resultlist.add(cursor.getString(2));
                resultlist.add(cursor.getString(3));
                resultlist.add(""+cursor.getInt(4));
                cursor.moveToNext();
            }
            cursor.close();

        }else if(mode == 2){    //login user is worker
            cursor = db.rawQuery("SELECT * FROM WORKER WHERE email='" + id + "';", null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                resultlist.add(cursor.getString(2));        //name
                resultlist.add(cursor.getString(3));        //room
                resultlist.add(""+cursor.getInt(4));        //img
                cursor.moveToNext();
            }
            cursor.close();
        }

        return resultlist;
    }
    public int getDayOrder(String day, String dbname){
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT dayorder FROM " + dbname + " WHERE day='" + day + "';";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            int order = cursor.getInt(0);
            return order;
        }else{
            return 0;
        }
    }

    public ArrayList<ObservArticleItem> getObservArticlebyRoomDay(String room, String day){
        ArrayList<ObservArticleItem> tmpArticle = new ArrayList<ObservArticleItem>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM OBSERVE WHERE objectroom='" + room + "' AND day='" + day + "';";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            ObservArticleItem tmp = new ObservArticleItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6),cursor.getInt(7),
                    cursor.getString(8), cursor.getInt(9), cursor.getString(10), cursor.getString(11));
            tmpArticle.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();

        return tmpArticle;
    }
    public ObservArticleItem getObservArticlebyID(int key){
        ObservArticleItem tmpArticle;
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM OBSERVE WHERE _id=" + key + ";";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        if(!cursor.isAfterLast()){
            tmpArticle = new ObservArticleItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6),cursor.getInt(7),
                    cursor.getString(8), cursor.getInt(9), cursor.getString(10), cursor.getString(11));

            cursor.moveToNext();
        }else{
            return null;
        }

        cursor.close();

        return tmpArticle;
    }
    public ArrayList<ObservArticleItem> getObservArticlebyObjectRoom(String object, String room){
        ArrayList<ObservArticleItem> tmpArticle = new ArrayList<ObservArticleItem>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM OBSERVE WHERE objectroom='" + room + "' AND objectname='" + object + "';";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            ObservArticleItem tmp = new ObservArticleItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6),cursor.getInt(7),
                    cursor.getString(8), cursor.getInt(9), cursor.getString(10), cursor.getString(11));
            tmpArticle.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();

        return tmpArticle;
    }

    public ArrayList<ObservArticleItem> getObserveArticlebyDay(String day){
        ArrayList<ObservArticleItem> tmpArticle = new ArrayList<ObservArticleItem>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM OBSERVE WHERE day='" + day + "';";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            ObservArticleItem tmp = new ObservArticleItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6),cursor.getInt(7),
                    cursor.getString(8), cursor.getInt(9), cursor.getString(10), cursor.getString(11));
            tmpArticle.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();

        return tmpArticle;
    }

    public ArrayList<ObservArticleItem> getObserveArticlebyUserIDDay(String id, String day){
        ArrayList<ObservArticleItem> tmpArticle = new ArrayList<ObservArticleItem>();
        SQLiteDatabase db = helper.getReadableDatabase();

        int mode;
        String name = "";

        Cursor cursor = db.rawQuery("SELECT mode FROM USER WHERE email='" + id + "';", null);
        if(cursor.moveToFirst()){
            mode = cursor.getInt(0);
        }else{
            mode = 0;
        }

        if(mode == 1){  //login user is parent
            cursor = db.rawQuery("SELECT name FROM PARENT WHERE email='" + id + "';", null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                name = cursor.getString(0);
                cursor.moveToNext();
            }
            cursor.close();

        }else if(mode == 2){    //login user is worker
            cursor = db.rawQuery("SELECT name FROM WORKER WHERE email='" + id + "';", null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                name = cursor.getString(0);
                cursor.moveToNext();
            }
            cursor.close();
        }

        String sql = "SELECT * FROM OBSERVE WHERE objectname='" + name + "' AND day='" + day + "';";
        cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            ObservArticleItem tmp = new ObservArticleItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6),cursor.getInt(7),
                    cursor.getString(8), cursor.getInt(9), cursor.getString(10), cursor.getString(11));
            tmpArticle.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();

        return tmpArticle;
    }

    public ArrayList<ArticleCommentItem> getArticleCommentList(String dbname, int articlekey){
        ArrayList<ArticleCommentItem> tmpComment = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + dbname + " WHERE articlekey=" + articlekey + ";";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            ArticleCommentItem tmp = new ArticleCommentItem(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(5), cursor.getString(6) );
            tmpComment.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();

        return tmpComment;
    }

    public ProgramArticleItem getProgramArticlebyID(int key){
        ProgramArticleItem tmpArticle;
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM PROGRAM WHERE _id=" + key + ";";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        if(!cursor.isAfterLast()){
            tmpArticle = new ProgramArticleItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
            cursor.moveToNext();
        }else{
            return null;
        }
        cursor.close();
        return tmpArticle;
    }
    public ArrayList<ProgramArticleItem> getProgramArticlebyDay(String day){
        ArrayList<ProgramArticleItem> tmpArticle = new ArrayList<ProgramArticleItem>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM PROGRAM WHERE day LIKE '" + day + "%';";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            ProgramArticleItem tmp = new ProgramArticleItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
            tmpArticle.add(tmp);
            cursor.moveToNext();
        }


        cursor.close();

        return tmpArticle;
    }

    public ArrayList<ProgramArticleItem> getAlbumArticlebyDay(String day){
        ArrayList<ProgramArticleItem> tmpArticle = new ArrayList<ProgramArticleItem>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM ALBUM WHERE day LIKE '" + day + "%';";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            ProgramArticleItem tmp = new ProgramArticleItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
            tmpArticle.add(tmp);
            cursor.moveToNext();
        }


        cursor.close();

        return tmpArticle;
    }

    public boolean isRoomDBEmpty(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ROOM;",null);
        if(cursor.getCount()== 0){
            return true;
        }else{
            return false;
        }
    }
    public boolean isUserDBEmpty(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USER;",null);
        if(cursor.getCount()== 0){
            return true;
        }else{
            return false;
        }
    }
    public boolean isWorkerDBEmpty(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM WORKER;",null);
        if(cursor.getCount()== 0){
            return true;
        }else{
            return false;
        }
    }
    public boolean isParentDBEmpty(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PARENT;",null);
        if(cursor.getCount()== 0){
            return true;
        }else{
            return false;
        }
    }

    public boolean isScheduleExist(String day){
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM SCHEDULE WHERE day='" + day + "';";
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            int count = cursor.getInt(0);
            if(count == 0){
                return false;
            }else {
                return true;
            }
        }else{
            return false;
        }
    }

    public int getCommentNumber(String dbname, int articleKey){
        int count=0;
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM " + dbname + " WHERE articlekey=" + articleKey + ";";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            count = cursor.getInt(0);
            cursor.moveToNext();
        }
        cursor.close();

        return count;
    }

    public int ValidLogin(String email, String pwd){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USER WHERE email='" + email + "' AND pwd='" + pwd + "';", null);
        /*if (cursor.getCount() == 0){
            return 0;
        }*/
        if(cursor.moveToFirst()){
            int mode = cursor.getInt(4);
            return mode;
        }else {
           return 0;
        }
    }
    public boolean ValidEmail(String email){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USER WHERE email='" + email + "';",null);
        if(cursor.getCount()== 0){
            return true;
        }else{
            return false;
        }
    }


}
