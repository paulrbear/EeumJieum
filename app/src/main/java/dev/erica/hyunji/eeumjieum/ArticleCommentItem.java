package dev.erica.hyunji.eeumjieum;

/**
 * Created by Erica on 2016-09-14.
 */
public class ArticleCommentItem {
    int articlekey,type;
    String day, time, writer, comment_content;

    public ArticleCommentItem(int articlekey, String day, String time, String writer, String comment_content, int type){
        this.articlekey = articlekey;
        this.day = day;
        this.time = time;
        this.writer = writer;
        this.comment_content = comment_content;
        this.type = type;
    }

    public ArticleCommentItem(int articlekey, String day, String time, String writer, String comment_content){
        this.articlekey = articlekey;
        this.day = day;
        this.time = time;
        this.writer = writer;
        this.comment_content = comment_content;
        this.type = 0;
    }



    public int getArticleKey(){return articlekey;}
    public String getDay(){return day;}
    public String getTime(){return time;}
    public String getWriter(){return writer;}
    public String getComment_content(){return comment_content;}
    public int getType(){return type;}

}
