package hisns.com.test.hisns;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Date;

public class JoinPage extends AppCompatActivity {

    EditText emailtext11;
    EditText pwtext11;
    EditText pwctext11;
    EditText nicnametext11;
    String email1;//이메일
    String pw1;//비번
    String pwc1;//비번확인
    String nicname1;//닉네임
    String joindate;//가입날짜
    String link1="http://192.168.0.121:8090/SnsGo/joinGo.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_page);

        //액션바 원하는 옵션 추가
        actiontitle();

        //가입하기버튼 클릭시
        joinGo();
    }
    private void actiontitle(){
        //내가 원하는 글씨넣기
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom);
        //뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //액션바 색상
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.colorLogo));

        TextView tv=(TextView) findViewById(R.id.page1text);
        tv.setText("회원가입");
    }
    //액션바 메뉴넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }
    //액션바 메뉴 선택시
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //뒤로가기 버튼클릭시
            case android.R.id.home:
                Intent in=new Intent(JoinPage.this, LoginPage.class);
                startActivity(in);
                finish();
                break;
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
    //가입하기 버튼 클릭시
    private void joinGo(){
        Button btn=(Button)findViewById(R.id.joinGoBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo();//회원가입정보 가져오기

                //내용이 빈값일 때 저장못하게 하기
                if(email1.equals("") || pw1.equals("") || pwc1.equals("") || nicname1.equals("")){
                    Toast.makeText(getApplicationContext(),"내용을 모두 넣어주세요",Toast.LENGTH_LONG).show();
                }else if(!pw1.equals(pwc1)){
                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다",Toast.LENGTH_LONG).show();
                }else{
                    insertGo();//서버에 가입정보 보내기
                }
            }
        });
    }
    //회원가입정보 가져오기
    private void getInfo(){
        emailtext11=(EditText) findViewById(R.id.emailText);
        pwtext11=(EditText) findViewById(R.id.pwText);
        pwctext11=(EditText) findViewById(R.id.pwcText);
        nicnametext11=(EditText) findViewById(R.id.nicnameText);
        Date sd = new Date(System.currentTimeMillis());//날짜 가져오기

        email1=emailtext11.getText().toString();//이메일
        pw1=pwtext11.getText().toString();//비번
        pwc1=pwctext11.getText().toString();//비번확인
        nicname1=nicnametext11.getText().toString();//닉네임
        joindate=String.valueOf(sd);//날짜
    }

    //서버에 값 보내기
    private void insertGo(){
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            protected void onPreExecute(){
                loading = ProgressDialog.show(JoinPage.this, "가입중....", null, true, true);
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    String link2 = params[0];//접속 주소
                    String email2 = params[1];
                    String password2 = params[2];
                    String nicname2 = params[3];
                    String joindate2 = params[4];


                    String data = "email=" + URLEncoder.encode(email2, "UTF-8");
                    data += "&password=" + URLEncoder.encode(password2, "UTF-8");
                    data += "&nicname=" + URLEncoder.encode(nicname2, "UTF-8");
                    data += "&joindate=" + URLEncoder.encode(joindate2, "UTF-8");

                    URL url = new URL(link2);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                    wr.write( data );
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
        InsertData task = new InsertData();
        task.execute(link1,email1,pw1,nicname1,joindate);
    }//sendInfo

    //가입후 결과
    private void showgo1(String re1){
        if(re1.equals("emfail")){
            Toast.makeText(getApplicationContext(),"이메일 중복",Toast.LENGTH_LONG).show();
        }if(re1.equals("nicfail")){
            Toast.makeText(getApplicationContext(),"닉네임 중복",Toast.LENGTH_LONG).show();
        }if(re1.equals("joinfail")){
            Toast.makeText(getApplicationContext(),"가입 실패",Toast.LENGTH_LONG).show();
        }else if(re1.equals("joinsuccess")){
            Toast.makeText(getApplicationContext(),"가입 성공",Toast.LENGTH_LONG).show();
            SessionNow.setSession(this,"token1", re1);
            Intent in=new Intent(JoinPage.this,LoginPage.class);
            startActivity(in);
            finish();
        }
    }

    //뒤로가기 버튼 클릭시
    @Override
    public void onBackPressed() {
        //moveTaskToBack(true);//홈버튼 처럼 앱 종료안되게 함
        Intent in=new Intent(JoinPage.this, LoginPage.class);
        startActivity(in);
        finish();
    }
}
