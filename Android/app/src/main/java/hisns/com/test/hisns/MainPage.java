package hisns.com.test.hisns;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainPage extends AppCompatActivity{

    private TextView tv;//액션바 상단 텍스트

    private TabLayout tabLayout;//TabLayout
    private ViewPager viewPager;//tab버튼 클릭시 페이지 넘김


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        //탭버튼 넣기
        tobgo();
        //액션바 원하는 옵션 추가
        actiontitle();
        //저장된 토큰값 출력
        showtoken();
    }

    //탭버튼 넣기
    private void tobgo(){
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        /*
        tabLayout.addTab(tabLayout.newTab().setText("Tab One"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab Two"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab Three"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        */

        // Initializing ViewPager
        viewPager = (ViewPager) findViewById(R.id.aaaa);

        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void actiontitle(){
        //내가 원하는 글씨넣기
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom);
        //액션바 색상
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.colorLogo));
        //getSupportActionBar().setElevation(0);

        tv=(TextView) findViewById(R.id.page1text);
    }
    //액션바 메뉴넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }
    //액션바 메뉴 선택시
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //옵션1 선택시
            case R.id.menucon1:
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                break;
            //옵션2 선택시
            case R.id.menucon2:
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    //저장된 토큰값 출력
    private void showtoken(){
        String aa=SessionNow.getSession(this,"token1");

        //json값에서 닉네임값만 추출한다
        try {
            JSONObject jobj=new JSONObject(aa.toString());
            tv.setText(jobj.getString("nicname"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //저장된 토큰값 삭제
    private void deltoken(){
        SessionNow.delSession(this,"token1");
    }
/*
    //뒤로가기 버튼 클릭시
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);//홈버튼 처럼 앱 종료안되게 함
    }
    */
}
