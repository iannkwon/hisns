package hisns.com.test.hisns;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class BoardRead extends AppCompatActivity {

    private TextView tv;//액션바 상단 텍스트

    private String boardnum1;//게시글 번호
    private String link11="http://192.168.0.121:8090/SnsGo/BoardSearch.do";

    private String address;//주소
    private String btitle;//작성제목
    private String nicname;//작성자 닉네임
    private String wdate;//작성일
    private String bcontent;//작성내용


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boardread);

        //액션바 원하는 옵션 추가
        actiontitle();

        //intent putExtra로 보낸값 받기
        boardGetInfo();

        //서버에 게시글 번호보내서 값 가져오기
        sendInfo();
    }

    //액션바 원하는 옵션 추가
    private void actiontitle(){
        //내가 원하는 글씨넣기
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom);
        //액션바 색상
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.colorLogo));
        //getSupportActionBar().setElevation(0);
        tv=(TextView) findViewById(R.id.page1text);
    }

    //intent putExtra로 보낸값 받기
    private void boardGetInfo(){
        Intent intent=this.getIntent();
        String bnum=intent.getStringExtra("boardnum");
        address=intent.getStringExtra("boardaddress");
        //String loc1=intent.getStringExtra("boardloc1");
       // String loc2=intent.getStringExtra("boardloc2");

        String []bnumCast=bnum.split("\\.");//게시글 번호값이 double로 와서 쪼갯음(casting하면 에러..)

        boardnum1=bnumCast[0];

        /*
        Log.i("get1",String.valueOf(bnum));
        Log.i("get2",String.valueOf(address));
        Log.i("get3",String.valueOf(loc1));
        Log.i("get4",String.valueOf(loc2));
        Log.i("get5",bnumCast[0]);
        */
    }

    //서버에 게시글 번호보내서 값 가져오기
    private void sendInfo(){
        class SendLogData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            protected void onPreExecute(){
                loading = ProgressDialog.show(BoardRead.this, "로딩중....", null, true, true);
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    String link2 = params[0];//접속 주소
                    String boardnum2 = params[1];//아이디 값

                    String data1="bnum="+ URLEncoder.encode(boardnum2, "UTF-8");

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
        aa.execute(link11,boardnum1);
    }//sendInfo

    //로그인 확인후 결과
    private void showgo1(String re1){
        //Log.i("datago",re1);
        try {
            JSONObject jobj=new JSONObject(re1);
            btitle=jobj.getString("btitle");
            nicname=jobj.getString("nicname");
            wdate=jobj.getString("wdate");
            bcontent=jobj.getString("bcontent");

            showgo2();//받은값 layout에 출력

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //받은값 layout에 출력
    private void showgo2(){
        tv.setText(btitle);//제목 액션바 상위에 놓음
        TextView address1=(TextView)findViewById(R.id.boardread_address);
        TextView nicname1=(TextView)findViewById(R.id.boardread_nicname);
        TextView wdate1=(TextView)findViewById(R.id.boardread_date);
        TextView bcontent1=(TextView)findViewById(R.id.boardread_content);

        address1.setText(address);
        nicname1.setText(nicname);
        wdate1.setText(wdate);
        bcontent1.setText(bcontent);
    }
}
