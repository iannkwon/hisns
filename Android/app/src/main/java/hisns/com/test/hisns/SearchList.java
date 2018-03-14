package hisns.com.test.hisns;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


//import static hisns.com.test.hisns.R.id.textView3;
//import static hisns.com.test.hisns.R.id.view_offset_helper;

public class SearchList extends AppCompatActivity {
    String link2="http://192.168.0.121:8090/SnsGo/searchGo.do";//접속할 주소
    EditText getsearchtext1;//검색값 edittext
    String getsearch1;//검색값 가져오기
    ListView listView;
    ArrayAdapter<String> Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list);
        Log.i("test","test1");
        listView = (ListView)findViewById(R.id.listview01); //리스트뷰
        homeGo();//메인화면으로
        searchgo();//검색하기

    }
    //메인화면이동
    public void homeGo(){
        Button backbtn11=(Button) findViewById(R.id.backbtn);
        backbtn11.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //검색버튼 클릭
    private void searchgo(){
        Button getsearchbtn11=(Button) findViewById(R.id.getsearchbtn);
        getsearchbtn11.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("test","test2");

                //gogo2()는 이 메소드 안에서 실행되므로 이 메소드 안에서 변수값을 넣어야 제대로 작동한다
                //link2="http://www.templenews.net/andgo/andget2.php";


                getsearchtext1=(EditText) findViewById(R.id.getsearchtext);
                getsearch1=getsearchtext1.getText().toString();//검색값
                gogo2();
                Log.i("ClickOK",getsearch1);

//                Toast.makeText(getApplicationContext(),getsearch1,Toast.LENGTH_LONG).show();



            }
        });
    }
    //검색 내용 보내기
    public void gogo2(){
        class GetData2 extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                try{
                    String link2 = params[0];//접속 주소
                    String getsearch2 = params[1];//검색 값
                    Log.i("linkOK",link2);
                    Log.i("getSearchOK",getsearch2);

                    String data1="searchtot="+ URLEncoder.encode(getsearch2, "UTF-8");
                    URL url = new URL(link2);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    Log.i("searchOK",data1);

                    con.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                    wr.write(data1);
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
            }
            @Override
            protected void onPostExecute(String result){
                showgo2(result);
            }
        }
        GetData2 g=new GetData2();
        g.execute(link2,getsearch1);
    }

    //가져온 검색 출력하기
    public void showgo2(String dbtot){
        try {
            JSONArray ja = new JSONArray(dbtot);
            List<String> list = new ArrayList<String>();

                for (int i = 0; i <ja.length();i++){
                    JSONObject c = ja.getJSONObject(i);
                    String nicname = c.getString("nicname");
                    list.add("닉네임:"+nicname);
                }
            Log.i("nicname",list.toString());
            Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
            listView.setAdapter(Adapter);


        }catch (JSONException e) {
            e.printStackTrace();
        }



    }//

}
