package dev.erica.hyunji.eeumjieum;

/**
 * Created by Erica on 2016-09-16.
 */
public class ProgramArticleItem {
    int articleid, dayorder;
    String objectname, writer, writerroom, day, title, tfdcontent, photo;

    public ProgramArticleItem(int articleid, String objectname, String writer, String writerroom, String day, int dayorder, String title, String tfdcontent, String photo){
        this.articleid = articleid;
        this.objectname = objectname;
        this.writer = writer;
        this.writerroom = writerroom;
        this.day = day;
        this.dayorder = dayorder;
        this.title = title;
        this.tfdcontent = tfdcontent;
        this.photo = photo;
    }

    public String getObjectname(){return objectname;}
    public String getWriter(){return writer;}
    public String getWriterroom(){return writerroom;}
    public String getDay(){return day;}
    public String getTfdcontent(){return tfdcontent;}
    public String getPhoto(){return photo;}
    public int getArticleid(){return articleid;}
    public String getTitle(){return title;}
    public int getDayorder(){return dayorder;}




}
