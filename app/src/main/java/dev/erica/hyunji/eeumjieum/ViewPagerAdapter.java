package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Erica on 2016-09-20.
 */
public class ViewPagerAdapter extends PagerAdapter {
    LayoutInflater inflater;
    private ArrayList<ViewPagerItem> mListData;

    public ViewPagerAdapter(Context context, ArrayList<ViewPagerItem> data) {
        // TODO Auto-generated constructor stub
       inflater = LayoutInflater.from(context);
        this.mListData = data;
        System.out.println("current data length" + mListData.size() + "**********" +
                "*********************************************************");
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mListData.get(position).getTitle();
    }

    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }

    //PagerAdapter가 가지고 잇는 View의 개수를 리턴
    //보통 보여줘야하는 이미지 배열 데이터의 길이를 리턴
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mListData.size();
    }

    //ViewPager가 현재 보여질 Item(View객체)를 생성할 필요가 있는 때 자동으로 호출
    //쉽게 말해, 스크롤을 통해 현재 보여져야 하는 View를 만들어냄.
    //첫번째 파라미터 : ViewPager
    //두번째 파라미터 : ViewPager가 보여줄 View의 위치(가장 처음부터 0,1,2,3...)

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.view_pager_item, null);

        System.out.println("i'm here******************************************" +
                "*********************************************************");

        FrameLayout img = (FrameLayout) view.findViewById(R.id.fullscreen_layout);
        TextView subtitle = (TextView) view.findViewById(R.id.sub_title_tv);
        TextView title = (TextView) view.findViewById(R.id.title_tv);
        TextView content = (TextView) view.findViewById(R.id.content_tv);
        TextView articlekey = (TextView) view.findViewById(R.id.invisible_content_key);
        Button detailview = (Button) view.findViewById(R.id.detailview_btn);


        ViewPagerItem tmp = mListData.get(position);

        if(mListData.size() > 0) {
            img.setBackgroundResource(tmp.getMain_photo());
            subtitle.setText(tmp.getSubtitle());
            title.setText(tmp.getTitle());
            content.setText(tmp.getContent());
            articlekey.setText("" + tmp.getArticlekey());

            detailview.setOnClickListener(HomeActivity.detailViewListener);
        }else{
            img.setBackgroundResource(R.color.colorSkyBlue);
            subtitle.setText("없음");
            title.setText("오늘 등록된 글이 없습니다");
            content.setText("");
            articlekey.setText("");
        }

        container.addView(view);

        return view;
    }


    //화면에 보이지 않은 View는파쾨를 해서 메모리를 관리함.
    //첫번째 파라미터 : ViewPager
    //두번째 파라미터 : 파괴될 View의 인덱스(가장 처음부터 0,1,2,3...)
    //세번째 파라미터 : 파괴될 객체(더 이상 보이지 않은 View 객체)
    @Override

    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        //ViewPager에서 보이지 않는 View는 제거
        //세번째 파라미터가 View 객체 이지만 데이터 타입이 Object여서 형변환 실시
        container.removeView((View)object);
    }

    //instantiateItem() 메소드에서 리턴된 Ojbect가 View가  맞는지 확인하는 메소드

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        // TODO Auto-generated method stub
        return v==obj;
    }

    public int getCurrentArticleType(int position){
        return mListData.get(position).getArticleType();
    }

    public int getCurrentArticlekey(int position){
        return mListData.get(position).getArticlekey();
    }

    public String getCurrentTotalPhoto(int position){
        return mListData.get(position).getTotal_photo();
    }

    public String getCurrentArticleTitle(int position){
        return mListData.get(position).getTitle();
    }
    public String getCurrentArticleSubtitle(int position){
        return mListData.get(position).getSubtitle();
    }

}
