package dev.erica.hyunji.eeumjieum;

/**
 * Created by Erica on 2016-09-19.
 */
public class DietListItem {
    String day;
    String str_morning;
    String str_afternoon;
    String str_night;
    int articlekey;

    int photo_morning, photo_afternoon, photo_night;

    DietListItem(int articlekey, String day, String str_morning, String str_afternoon, String str_night, int photo_morning, int photo_afternoon, int photo_night){
        this.articlekey = articlekey;
        this.day = day;
        this.str_morning = str_morning;
        this.str_afternoon = str_afternoon;
        this.str_night = str_night;
        this.photo_morning = photo_morning;
        this.photo_afternoon = photo_afternoon;
        this.photo_night = photo_night;
    }

    public int getArticlekey() {
        return articlekey;
    }

    public String getDay() {
        return day;
    }

    public int getPhoto_afternoon() {
        return photo_afternoon;
    }

    public int getPhoto_morning() {
        return photo_morning;
    }

    public int getPhoto_night() {
        return photo_night;
    }

    public String getStr_afternoon() {
        return str_afternoon;
    }

    public String getStr_morning() {
        return str_morning;
    }

    public String getStr_night() {
        return str_night;
    }

}
