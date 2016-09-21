package dev.erica.hyunji.eeumjieum;

/**
 * Created by Erica on 2016-09-18.
 */
public class ScheduleListItem {
    String day;
    String time;
    String title;
    String objectroom;
    String writer;
    String writerroom;
    String location;
    String content;
    String photo;
    int articlekey, dayorder;

    ScheduleListItem(int articlekey, String objectroom, String writer, String writerroom, String day, int dayorder, String time, String title, String location, String content, String photo){
        this.articlekey= articlekey;
        this.objectroom = objectroom;
        this.writer = writer;
        this.writerroom = writerroom;
        this.day = day;
        this.dayorder = dayorder;
        this.time = time;
        this.title = title;
        this.location = location;
        this.content = content;
        this.photo = photo;
    }

    public int getArticlekey() {
        return articlekey;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
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

    public String getLocation() {
        return location;
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
