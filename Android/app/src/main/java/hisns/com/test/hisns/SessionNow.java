package hisns.com.test.hisns;

import android.content.Context;
import android.content.SharedPreferences;

//sharedPreferences 사용
public class SessionNow {

    //token값 넣기
    public static void setSession(Context con, String key, String val){
        //token이라는 파일명으로 xml파일 생성해서 저장한다, MODE_PRIVATE는 자기앱에서만 실행됨
        SharedPreferences pref = con.getSharedPreferences("token", con.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key,val);
        editor.commit(); //변경사항을 저장합니다
    }
    //token값 불러오기
    public static String getSession(Context con, String key){
        SharedPreferences pref = con.getSharedPreferences("token", con.MODE_PRIVATE);
        return pref.getString(key,"");//문자는 "" 숫자는0을 넣는다
    }
    //token값 삭제하기
    public static void delSession(Context con, String key){
        SharedPreferences pref = con.getSharedPreferences("token", con.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key); //token1에 해당하는 value값 삭제
        editor.clear(); //모든 값을 지워줍니다.
        editor.commit(); //값 저장(저장,삭제후 꼭 해줘야 적용된다)
    }
}
