package dev.erica.hyunji.eeumjieum;

/**
 * Created by Erica on 2016-09-12.
 */
public class ObservArticleItem {
        int articleid, mood, activity, sleep, dayorder;
        String objectname, objectroom, writer, writerroom, day, tfdcontent, photo;

        public ObservArticleItem(int articleid, String objectname, String objectroom, String writer, String writerroom, int mood, int activity, int sleep, String day, int dayorder, String tfdcontent, String photo){
            this.articleid = articleid;
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

        public String getObjectname(){return objectname;}
        public String getObjectroom(){return objectroom;}
        public String getWriter(){return writer;}
        public String getWriterroom(){return writerroom;}
        public String getDay(){return day;}
        public String getTfdcontent(){return tfdcontent;}
        public String getPhoto(){return photo;}
        public int getArticleid(){return articleid;}
        public int getMood(){return mood;}
        public int getActivity(){return activity;}
        public int getSleep(){return sleep;}
        public int getDayorder(){return dayorder;}

}


