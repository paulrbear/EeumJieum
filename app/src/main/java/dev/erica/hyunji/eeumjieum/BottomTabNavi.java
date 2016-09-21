package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * Created by Erica on 2016-08-26.
 */
public class BottomTabNavi extends LinearLayout{
Context context;

    public BottomTabNavi(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.bottom_tab, this, true);

    }

}
