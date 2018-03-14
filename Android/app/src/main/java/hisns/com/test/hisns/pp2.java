package hisns.com.test.hisns;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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

public class pp2 extends Fragment {

    RadioButton rb1;
    RadioButton rb2;
    RadioGroup rg;
    Button bt1;
    ListView lv1;
    String link1="http://192.168.0.121:8090/SnsGo/myBoard.do";
    String link2="http://192.168.0.121:8090/SnsGo/hiddenBoard.do";
    String unum="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View setView = inflater.inflate(R.layout.pp2, container, false);
        rg = (RadioGroup)setView.findViewById(R.id.rg);
        rb1 = (RadioButton)setView.findViewById(R.id.rb1);
        rb2 = (RadioButton)setView.findViewById(R.id.rb2);
        lv1 = (ListView)setView.findViewById(R.id.lv1);
        bt1 = (Button)setView.findViewById(R.id.bt1);
        showtoken();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                Log.i("abc","3");
                if(checkedId==R.id.rb1){
//                    showtoken();
                    Log.i("token.. email1:",unum);
                    insertGo();
                }else if (checkedId==R.id.rb2){
//                    showtoken();
                    Log.i("token.. email2:",unum);
                    insertGo2();
                }

            }
        });

        hidboardwrite();

        return setView;
    }

    private void hidboardwrite(){
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),HidBoardwrite.class);
                startActivity(intent);
            }
        });
    }

    private void showtoken(){
        String aa=SessionNow.getSession(getContext(),"token1");

        //json값에서 이메일값만 추출한다
        try {
            JSONObject jobj=new JSONObject(aa.toString());
            Log.i("test", jobj.getString("email"));
            unum= jobj.getString("email");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //서버에 값 보내기
    private void insertGo(){
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            protected void onPreExecute(){
                loading = ProgressDialog.show(getContext(), "검색중....", null, true, true);
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    String link2 = params[0];//접속 주소
                    String nic2 = params[1];

                    String data = "unum=" + URLEncoder.encode(nic2, "UTF-8");

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
                if(result!=null){
                    showInfo(result);
                }else{
                }loading.dismiss();//다이얼로그 종료
            }
        }//SendLogData
        InsertData task = new InsertData();
        task.execute(link1,unum);
    }//sendInfo

    private void insertGo2(){
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            protected void onPreExecute(){
                loading = ProgressDialog.show(getContext(), "검색중....", null, true, true);
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    String link2 = params[0];//접속 주소
                    String nic2 = params[1];

                    String data = "unum=" + URLEncoder.encode(nic2, "UTF-8");

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
                if(result!=null){
                    showInfo2(result);
                }else{
                }loading.dismiss();//다이얼로그 종료
            }
        }//SendLogData
        InsertData task = new InsertData();
        task.execute(link2,unum);
    }//sendInfo


    private void showInfo(String result){
        try {
            JSONArray ja = new JSONArray(result);
            List<String> list = new ArrayList<String>();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject order = ja.getJSONObject(i);
                list.add("제목:"+order.getString("btitle") +"작성날짜:"+order.getString("wdate"));
            }

            lv1.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showInfo2(String result) {
        try {
            JSONArray ja = new JSONArray(result);
            List<String> list1 = new ArrayList<String>();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject order = ja.getJSONObject(i);
                list1.add("제목:" + order.getString("btitle") + "작성날짜:" + order.getString("wdate"));
            }

            lv1.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

