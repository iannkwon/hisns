package hisns.com.test.hisns;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class pp1 extends Fragment implements OnMapReadyCallback{

    View setView;

    //db연동 주소
    String link11="http://192.168.0.111:8090/SnsGo/BoardMapView.do";

    List<Integer> boardnum=new ArrayList();//게시글 번호
    List<String> btitle=new ArrayList();//제목
    List<String> bcontent=new ArrayList();//내용
    List<Double> lat1=new ArrayList();//위도
    List<Double> lon1=new ArrayList();//경도


    //지도정보 관련 변수
    private Double lat=null; //내위치 위도
    private Double lon=null; //내위치 경도
    private LocationManager Lm;
    private LocationListener lo;
    private GoogleMap gm;
    Geocoder geocoder; //좌표를 주소값으로 변환
    ImageView iv1;
    ImageView iv2;
    ImageView iv3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setView=inflater.inflate(R.layout.pp1, container, false);

        //게시글 작성 페이지 이동
        BoardPage();

        //구글맵정보 가져오기
        MapView mapview=(MapView)setView.findViewById(R.id.mapView);
        mapview.onCreate(savedInstanceState);
        mapview.onResume();
        mapview.getMapAsync(this);

        return setView;
    }

    //서버에 게시판 검색 정보 보내기
    public void nomalBoardInfoSend(){
        class SendLogData extends AsyncTask<String, Void, String> {
            protected void onPreExecute(){
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    String link2 = params[0];//접속 주소
                    String blocation = params[1];
                    String blocation2 = params[2];

                    String data1="blocation="+ URLEncoder.encode(blocation, "UTF-8");
                    data1+="&blocation2="+URLEncoder.encode(blocation2,"UTF-8");

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
                if(result!=null){
                    showInfo(result);
                }else{
                }
            }
        }//SendLogData
        SendLogData aa= new SendLogData();
        aa.execute(link11,String.valueOf(lat),String.valueOf(lon));
    }//sendInfo
    //값 정리
    private void showInfo(String result){
        try {
            String tot ="";
            JSONArray ja = new JSONArray(result);

            for (int i = 0; i < ja.length(); i++) {
                JSONObject order = ja.getJSONObject(i);

                boardnum.add(i,order.getInt("bnum"));
                btitle.add(i,order.getString("btitle"));
                //bcontent.add(i,order.getString("bcontent"));
                lat1.add(i,order.getDouble("blocation"));
                lon1.add(i,order.getDouble("blocation2"));

                //Log.i("dd:",order.getString("btitle"));
            }

            //게시글 마커 추가
            ccc1();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //게시글 작성 페이지 이동
    private void BoardPage(){
        ImageView boardwriteBtn=(ImageView)  setView.findViewById(R.id.boardwriteBtn);
        boardwriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Boardwrite.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gm=googleMap;
        Lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        gpsReady();//gps설정 안했을시 설정하면

        ccc();//위치정보를 가져온다.
    }

    //마커 추가 위치(게시글)
    public void ccc1() {
        Marker boardMarker=null;
        for(int a=0; a<boardnum.size(); a++){
            LatLng latLng1 = new LatLng(lat1.get(a), lon1.get(a));

            boardMarker=gm.addMarker(new MarkerOptions()
                    //마커 아이콘
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.boardloc))
                    .zIndex(boardnum.get(a)) //게시글 번호 넣어주기(double로 값이 저장된다/내위치는 0.0번값)
                    .title(btitle.get(a)+" <☜상세보기>")  //제목
                    //.snippet(bcontent.get(a)) //내용
                    .position(latLng1)); //위치
        }
        //boardMarker.showInfoWindow();//타이틀창 떠있는상태로 (마지막1개만 적용됨ㅠㅠ)

        //정보창 클릭 이벤트
        gm.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                geocoder = new Geocoder(getActivity());
                List<Address> list = null;
                try {
                    list = geocoder.getFromLocation(marker.getPosition().latitude,marker.getPosition().longitude, 10); // 얻어올 값의 개수
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent=new Intent();
                intent.setClass(getActivity(), BoardRead.class);

                intent.putExtra("boardnum",String.valueOf(marker.getZIndex())); //게시글번호(double값임)
                intent.putExtra("boardaddress",list.get(0).getAddressLine(0).toString());  //주소
                //intent.putExtra("boardloc1",String.valueOf(marker.getPosition().latitude));   //위도
                //intent.putExtra("boardloc2",String.valueOf(marker.getPosition().longitude));  //경도

                startActivity(intent);
            }
        });

        //마커 클릭이벤트
        gm.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });


    }

        //모든 설정 및 예외 처리 생각해서 위치 찿기(내위치)
    public void ccc() {
        Lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        lo = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng lo1;
                //최초접속시 줌18정도로 맞춰줌
                if(lat==null){
                    lat = location.getLatitude();//위도
                    lon = location.getLongitude();//경도

                    lo1= new LatLng(lat, lon);
                    gm.moveCamera(CameraUpdateFactory.newLatLngZoom(lo1,18));//줌18
                    //이후 사용자가 줌을 조정했을 가능성이 있기에, 사용자가 고정한 줌으로 위치값을 계속 불러와주기 위해
                }else{
                    gm.clear();//기존에 있던마커삭제

                    lat = location.getLatitude();//위도
                    lon = location.getLongitude();//경도

                    lo1 = new LatLng(lat, lon);
                    gm.moveCamera(CameraUpdateFactory.newLatLngZoom(lo1,gm.getCameraPosition().zoom));//현재 줌단계 고정
                }

                //서버에 게시판 검색 정보 보내기
                nomalBoardInfoSend();


                //내위치 마커 추가
                gm.addMarker(new MarkerOptions()
                        //마커 아이콘
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        //.title("내위치")
                        //.snippet("내용1")
                        .position(lo1));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
//        Lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, lo);
//        Lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, lo);
    }
    public void gpsReady(){
        boolean gpsuse = false;//gps 사용가능여부
        boolean netuse = false;//네트워크 사용가능여부
        // GPS 정보 가져오기
        gpsuse = Lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 현재 네트워크 상태 값 알아오기
        netuse = Lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        //gps, 네트워크 둘다 사용불가일 때
        if (!gpsuse && !netuse) {
            if (!gpsuse) {
                showSettingsAlert();
            }
        } else {//사용가능 일 때
            //Toast.makeText(getApplicationContext(),"딤1",Toast.LENGTH_LONG).show();

        }
    }
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle("GPS 사용유무셋팅");
        alertDialog.setMessage("GPS 셋팅이 되지 않았을수도 있습니다. 설정창으로 가시겠습니까?");

        // 네 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

                //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            }
        });
        // 아니오 하면 종료 합니다.
        alertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
