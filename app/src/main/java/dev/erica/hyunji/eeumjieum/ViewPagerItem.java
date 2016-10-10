package dev.erica.hyunji.eeumjieum;

/**
 * Created by Erica on 2016-09-20.
 */
public class ViewPagerItem {
    int articleType;        //1 :notice     2:schedule      3:observe report        4:programreport
    String subtitle;
    String title;
    String content;
    int articlekey;
    int main_photo;
    int bgcolor;

    ViewPagerItem(int articleType, String subtitle, String title, String content, int articlekey, int main_photo, int bgcolor){
        this.articleType = articleType;
        this.subtitle = subtitle;
        this.title = title;
        this.content = content;
        this.articlekey = articlekey;
        this.main_photo = main_photo;
        this.bgcolor = bgcolor;
    }

    public int getArticleType() {
        return articleType;
    }

    public int getArticlekey() {
        return articlekey;
    }

    public String getContent() {
        return content;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getTitle() {
        return title;
    }

    public int getMain_photo() {
        return main_photo;
    }

    public int getBgcolor() { return bgcolor; }
}