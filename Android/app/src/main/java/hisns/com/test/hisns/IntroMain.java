package hisns.com.test.hisns;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class IntroMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_main);

        //3초뒤 페이지 전환
        Handler hd=new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent in=new Intent(IntroMain.this, LoginPage.class);
                startActivity(in);
                finish();
            }
        },3000);//3초

        //상단 액션바 숨기기
        getSupportActionBar().hide();
    }
}
