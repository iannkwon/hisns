package hisns.com.test.hisns;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginPage extends AppCompatActivity {
    EditText a1;//아이디 id값
    EditText a2;//패스워드 id값
    String idtext11;//아이디
    String pwtext11;//패스워드
    String link11="http://192.168.0.121:8090/SnsGo/loginGo.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        //액션바 숨기기
        getSupportActionBar().hide();

        //로그인 클릭시
        loginGo();

        //회원가입 클릭시
        joinGo();
    }
    //회원가입 클릭시
    private void joinGo(){
        TextView jointext=(TextView) findViewById(R.id.logJoinBtn);

        jointext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(LoginPage.this, JoinPage.class);
                startActivity(in);
                finish();
            }
        });
    }
    //로그인 버튼클릭시
    private void loginGo(){
        Button btn=(Button) findViewById(R.id.logBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent in=new Intent(LoginPage.this, MainPage.class);
                startActivity(in);
                finish();
                */
                getInfo();//로그인 정보가져오기(아이디,비번)

                //내용이 빈값일 때 저장못하게 하기
                if(idtext11.equals("") || pwtext11.equals("")){
                    Toast.makeText(getApplicationContext(),"내용을 모두 넣어주세요",Toast.LENGTH_LONG).show();
                }else{
                    sendInfo();//서버에 로그인 정보 보내기
                }
            }
        });
    }

    //로그인 정보가져오기(아이디,비번)
    private void getInfo(){
        a1=(EditText) findViewById(R.id.logIdText);//아이디 id값
        a2=(EditText) findViewById(R.id.logPwText);//패스워드 id값
        idtext11=a1.getText().toString();
        pwtext11=a2.getText().toString();
    }

    //서버에 로그인 정보 보내기
    private void sendInfo(){
        class SendLogData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            protected void onPreExecute(){
                loading = ProgressDialog.show(LoginPage.this, "로그인중....", null, true, true);
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    String link2 = params[0];//접속 주소
                    String id2 = params[1];//아이디 값
                    String pw2 = params[2];//비번 값

                    String data1="email="+ URLEncoder.encode(id2, "UTF-8");
                    data1+="&password="+URLEncoder.encode(pw2,"UTF-8");
                    URL url = new URL(link2);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                    wr.write( data1 );
                    wr.flush();

                    StringBuilder sb = new StringBuilder();

                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while((json = br.readLine())!= null){
                        sb.append(json);
                    }
                    return sb.toString();

                }catch (Exception e){
                    return null;
                }
            }//doln
            @Override
            protected void onPostExecute(String result){
                loading.dismiss();//다이얼로그 종료
                if(result!=null){
                    showgo1(result);//토큰값 가져온다(현재 이메일)
                }else{
                    Toast.makeText(getApplicationContext(),"에러",Toast.LENGTH_LONG).show();
                }
            }
        }//SendLogData
        SendLogData aa= new SendLogData();
        aa.execute(link11,idtext11,pwtext11);
    }//sendInfo

    //로그인 확인후 결과
    private void showgo1(String re1){
        if(re1.equals("fail")){
            Toast.makeText(getApplicationContext(),"로그인 실패",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),"로그인 성공",Toast.LENGTH_LONG).show();
            SessionNow.setSession(this,"token1", re1);
            Intent in=new Intent(LoginPage.this, MainPage.class);
            startActivity(in);
            finish();
        }
    }
}
