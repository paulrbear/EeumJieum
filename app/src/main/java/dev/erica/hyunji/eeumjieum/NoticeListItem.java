package dev.erica.hyunji.eeumjieum;

/**
 * Created by Erica on 2016-09-18.
 */
public class NoticeListItem {

    String day;
    String title;
    String objectroom;
    String writer;
    String writerroom;
    String content;
    String photo;
    int articlekey, dayorder;

    NoticeListItem(int articlekey, String objectroom, String writer, String writerroom, String day, int dayorder, String title, String content, String photo){
        this.articlekey= articlekey;
        this.objectroom = objectroom;
        this.writer = writer;
        this.writerroom = writerroom;
        this.day = day;
        this.dayorder = dayorder;
        this.title = title;
        this.content = content;
        this.photo = photo;
    }

    public int getArticlekey() {
        return articlekey;
    }

    public String getDay() {
        return day;
    }


    public String getTitle() {
        return title;
    }

    public int getDayorder() {
        return dayorder;
    }

    public String getContent() {
        return content;
    }

    public String getObjectroom() {
        return objectroom;
    }

    public String getPhoto() {
        return photo;
    }

    public String getWriter() {
        return writer;
    }

    public String getWriterroom() {
        return writerroom;
    }







}
