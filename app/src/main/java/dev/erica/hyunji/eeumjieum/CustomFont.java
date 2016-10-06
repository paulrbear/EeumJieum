package dev.erica.hyunji.eeumjieum;

import android.app.Application;
import com.tsengvn.typekit.Typekit;
/**
 * Created by Erica on 2016-09-21.
 */
public class CustomFont extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "SpoqaHanSans-Regular.ttf"))
                .addBold(Typekit.createFromAsset(this, "SpoqaHanSans-Bold.ttf"))
                .addItalic(Typekit.createFromAsset(this, "SpoqaHanSans-Thin.ttf"));// "fonts/폰트.ttf"
    }
}
