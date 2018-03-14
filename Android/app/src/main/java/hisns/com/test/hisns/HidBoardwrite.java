package hisns.com.test.hisns;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class HidBoardwrite extends AppCompatActivity {

    TextView tv1; //location 현재위치
    EditText et1; //title값
    EditText et2; //content값
    ImageView iv1;
    String location;  //위치
    String location2;  //위치
    String title; //제목
    String content; //내용
    String image; //파일이름
    String wdate;
    String unum;
    String link1="http://192.168.0.121:8090/SnsGo/HidBoardwrite.do";

    String email;

    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hidboardwrite);
        Log.i("tt","aa");


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);
        }
        //send클릭시
        send();

        //cancel 클릭시
        cancel();

        //select photo클릭시(사진올리기)
        selectphoto();

    }

    private void showtoken(){
        String aa=SessionNow.getSession(this,"token1");

        //json값에서 이메일값만 추출한다
        try {
            JSONObject jobj=new JSONObject(aa.toString());
            Log.i("token",jobj.getString("email"));
            unum= jobj.getString("email");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //사진선택시
    private void selectphoto(){
        Button bt1 = (Button)findViewById(R.id.bt_1);
        iv1 = (ImageView)findViewById(R.id.iv_1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, 1);
            }
        });
    }


    //글작성 완료시
    private void send(){
        Button bt2 = (Button)findViewById(R.id.bt_2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //**location now 설정해줘야됨!! tv1 작업대기
                getInfo();
                Log.i("title","title:"+title);
                Log.i("content","content:"+content);
                Log.i("loc","loc:"+location);
                Log.i("loc2","loc2:"+location2);
                Log.i("image","image:"+image);
                Log.i("wdate","wdate:"+wdate);
                Log.i("unum","unum:"+unum);

//                new Thread(){ //사진파일 서버에 저장
//                    @Override
//                    public void run() {
//                        doProcess();
//                    }
//                };
                //내용이 빈값일 때 저장못하게 하기
                if(title.equals("") || content.equals("")){
                    Toast.makeText(getApplicationContext(),"제목과 내용을 입력해주세요",Toast.LENGTH_LONG).show();
                }else{
                    insertGo();//서버에 작성정보 보내기
                }

            }
        });
    }
//    private Handler mHandler = new Handler(); //사진파일 서버에 저장
//    private String insert_result;
//
//    private void doProcess() {
//        Log.i("Main Log", "doProcess()..getAbsolutePath.."+ Environment.getExternalStorageDirectory().getAbsolutePath());
//        Log.i("Main Log", "doProcess()...getAbsoluteFile.."+ Environment.getExternalStorageDirectory().getAbsoluteFile());
//
//        String charset = "UTF-8";
////        File uploadFile1 = new File(Environment.getExternalStorageDirectory(),"DCIM/Camera/IMG_20150331_053729.jpg");
////        File uploadFile1 = new File("/storage/emulated/0/Android/data/com.android.browser/files/Download/"+filePath);
//        File uploadFile1 = new File(filePath);
//        String requestURL = "http://192.168.0.121:8090/web16fileupload/uploadOk.jsp";
//
//
//        Log.i("Main Log", "doProcess()...uploadFile1.exists():"+uploadFile1.exists());
//
//        try {
//            MultipartUtility multipart = null;
//            try {
//                multipart = new MultipartUtility(requestURL, charset);
//                Log.i("Main Log", "doProcess()...multipart:"+multipart);
//
//                for (File f: ContextCompat.getExternalFilesDirs(this,fileName)) {
//                    Log.i("Main Log", "doProcess()...getExternalFilesDirs:");
//                    Log.i("Main Log", f.toString() );
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.i("Main Log", "doProcess()...IOException:" + e.toString());
//            }
//
//
//
//            multipart.addHeaderField("User-Agent", "CodeJava");
//            multipart.addHeaderField("Test-Header", "Header-Value");
//
////            multipart.addFormField("tel", "010-0000-0000");
//            multipart.addFilePart("multipartFile", uploadFile1);
//            multipart.finish();
//
//            List<String> response = multipart.finish();
//
//            Log.i("Main Log", "SERVER REPLIED:");
//
//            for (String line : response) {
//                Log.i("Main Log", line);
//            }
//        } catch (IOException ex) {
//            Log.i("Main Log", ex.toString());
//        }//사진파일 서버에 저장
//    }

    //글작성 내용 가져오기
    private void getInfo(){
//        Intent intent = getIntent(); //location가져오기
//        final String nickname = intent.getExtras().getString("nickname");
//        Log.i("Board Nic",nickname);
        showtoken();

        et1 = (EditText)findViewById(R.id.et_1);//제목
        et2 = (EditText)findViewById(R.id.et_2);//내용
        tv1 = (TextView)findViewById(R.id.tv_1);//장소
        Date sd = new Date(System.currentTimeMillis());//날짜 가져오기
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String a ="ab";
        tv1.setText(a); //location 셋팅
        String aa ="bb";


        Log.i("unum:",unum);
        title = et1.getText().toString();  //제목
        content = et2.getText().toString(); //내용
//        unum = String.valueOf(42);//닉네임
        wdate=String.valueOf(sd);//날짜
        location = tv1.getText().toString();  //장소
        location2 = aa;
        image = fileName;               //파일명
        if(image==null){
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            image=String.valueOf(df2.format(sd));
        }

    }
    //서버에 값 보내기
    private void insertGo(){
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            protected void onPreExecute(){
                loading = ProgressDialog.show(HidBoardwrite.this, "작성중....", null, true, true);
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    String link2 = params[0];//접속 주소
                    String title2 = params[1];
                    String content2 = params[2];
                    String nic2 = params[3];
                    String wdate2 = params[4];
                    String loc = params[5];
                    String loc2 = params[6];
                    String image2 = params[7];


                    String data = "btitle=" + URLEncoder.encode(title2, "UTF-8");
                    data += "&bcontent=" + URLEncoder.encode(content2, "UTF-8");
                    data += "&unum=" + URLEncoder.encode(nic2, "UTF-8");
                    data += "&wdate=" + URLEncoder.encode(wdate2, "UTF-8");
                    data += "&blocation=" + URLEncoder.encode(loc, "UTF-8");
                    data += "&blocation2=" + URLEncoder.encode(loc2, "UTF-8");
                    data += "&bfilenum=" + URLEncoder.encode(image2, "UTF-8");

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
        task.execute(link1,title,content,unum,wdate,location,location2,image);
    }//sendInfo


    private void showgo1(String re1){
        if(re1.equals("fcfail")){
            Toast.makeText(getApplicationContext(),"파일 이름 중복",Toast.LENGTH_LONG).show();
        }if(re1.equals("writefail")){
            Toast.makeText(getApplicationContext(),"글작성 실패",Toast.LENGTH_LONG).show();
        }else if(re1.equals("writeok")){
            Toast.makeText(getApplicationContext(),"글작성 성공",Toast.LENGTH_LONG).show();
//            SessionNow.setSession(this,"token1", re1);
            Intent in=new Intent(HidBoardwrite.this,SearchList.class);
            startActivity(in);
            finish();
        }
    }

    //글작성 취소시
    private void cancel(){
        Button bt3 = (Button)findViewById(R.id.bt_3);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(HidBoardwrite.this, MainPage.class);
                startActivity(in);
                finish();
            }
        });
    }


    //image 선택시 셋팅
    Uri curImgURI;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        curImgURI = data.getData();
        setImage(curImgURI);
    }

    String filePath;
    String fileName;

    private void setImage(Uri curImgURI) {
        Cursor cursor = getContentResolver().query(curImgURI, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            filePath = cursor.getString(cursor.getColumnIndex("_data"));
//            fileName = cursor.getString(cursor.getColumnIndex("bucket_display_name"));
            fileName = "/"+cursor.getString(cursor.getColumnIndex("_display_name"));
            cursor.moveToNext();
        }
        cursor.close();

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(curImgURI), null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        iv1.setImageBitmap(bitmap);
    }

}

