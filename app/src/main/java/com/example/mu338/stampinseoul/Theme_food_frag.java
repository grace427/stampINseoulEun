package com.example.mu338.stampinseoul;


import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.mu338.stampinseoul.LoginActivity.userId;

// 맛집 코드 : 39
public class Theme_food_frag extends Fragment {

    private View view;
    ProgressDialog pDialog;

    RecyclerView recyclerView;
    ThemeAdapter adapter;
    LinearLayoutManager layoutManager;

    RequestQueue queue;

    AlertDialog.Builder dialog;

    // 메인 화면 출력용
    ArrayList<ThemeData> list = new ArrayList<>();

    LottieAnimationView animationView3 = null;

    public static DBHelper dbHelper;
    public static SQLiteDatabase db;



    final static String TAG = "ThemeActivity";
    static final String KEY = "GN2mE8m8pbEpOyKZDhiRdDOZjg%2FR%2FUEIgo7z26k3HEefz8M0DvSZZwn0ekpLJmg%2F42jihzBbKf57CP79m12CrA%3D%3D";
    static final String appName = "Zella";

    public Theme_food_frag() {
        // Required empty public constructor
    }

    //뷰페이저로 프레그먼트가 변화되는 상태를 저장하는 변수가 필요하다.
    public static Theme_food_frag newInstance(){
        Theme_food_frag food_frag = new Theme_food_frag();
        return food_frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.frag_theme,container,false);

        recyclerView = view.findViewById(R.id.grid_recyclerview);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        animationView3 = view.findViewById(R.id.animation_view3);

        animationView3.setAnimation("red_wave.json");
        animationView3.loop(true);
        animationView3.playAnimation();
        animationView3.setVisibility(View.INVISIBLE);

        // db helper 객체 생성
        dbHelper = new DBHelper(view.getContext());

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            Theme_food_frag.AsyncTaskClassMain async = new Theme_food_frag.AsyncTaskClassMain();
            async.execute();

            adapter = new ThemeAdapter(R.layout.item_theme, getActivity(), list);
        }
    } // end of setUserVisibleHint

    class AsyncTaskClassMain extends android.os.AsyncTask<Integer, Long, String> {

        // 일반쓰레드 돌리기 전 메인쓰레드에서 보여줄 화면처리
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            displayLoader();
        }

        // 일반쓰레드에서 돌릴 네트워크 작업
        @Override
        protected String doInBackground(Integer... integers) {
            getAreaBasedList();
            // publishProgress()를 호출하면 onProgressUpdate가 실행되고 메인쓰레드에서 UI 처리를 한다
            //publishProgress();
            return "작업 종료";
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            // 일반 쓰레드가 도는 도중에 메인 쓰레드에서 처리할 UI작업
            super.onProgressUpdate(values);
        }

        // doInBackground 메서드가 완료되면 메인 쓰레드가 얘를 호출한다(doInBackground가 반환한 값을 매개변수로 받음)
        @Override
        protected void onPostExecute(String s) {
            // Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            super.onPostExecute(s);
        }
    } // end of AsyncTaskClassMain


    // contentid를 위한 함수(contentId는 detailCommon에서 쓰기 위해 구한다)
    private void getAreaBasedList() {
        queue = Volley.newRequestQueue(getActivity());

        // 맛집 코드 39
        String url = "http://api.visitkorea.or.kr/openapi/service/"
                + "rest/KorService/areaBasedList?ServiceKey=" + KEY
                + "&areaCode=1&contentTypeId=39&listYN=Y&arrange=P"
                + "&numOfRows=20&pageNo=1&MobileOS=AND&MobileApp="
                + appName + "&_type=json";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        db = dbHelper.getWritableDatabase();
                        Cursor cursor;

                        cursor = db.rawQuery("SELECT title FROM ZZIM_"+userId+";", null);
                        try {
                            JSONObject parse_response = (JSONObject) response.get("response");
                            JSONObject parse_body = (JSONObject) parse_response.get("body");
                            JSONObject parse_items = (JSONObject) parse_body.get("items");
                            JSONArray parse_itemlist = (JSONArray) parse_items.get("item");

                            for (int i = 0; i < parse_itemlist.length(); i++) {
                                JSONObject imsi = (JSONObject) parse_itemlist.get(i);

                                ThemeData themeData = new ThemeData();
                                themeData.setFirstImage(imsi.getString("firstimage"));
                                themeData.setTitle(imsi.getString("title"));
                                themeData.setAddr(imsi.getString("addr1"));
                                themeData.setMapX(imsi.getDouble("mapx"));
                                themeData.setMapY(imsi.getDouble("mapy"));
                                themeData.setContentsID(Integer.valueOf(imsi.getString("contentid")));


                                while(cursor.moveToNext()){
                                        if(cursor.getString(0).equals(themeData.getTitle())){
                                            themeData.setHart(true);
                                        }
                                    Log.d("TAG", i+"  테마데이터 : "+themeData.getTitle());
                                    Log.d("TAG", i+"  커서 : "+cursor.getString(cursor.getColumnIndex("title")));

                                }
                                cursor.moveToFirst();
                                list.add(themeData);

                            }

                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(getActivity(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, error.getMessage() + "에러");
                    }
                });
        queue.add(jsObjRequest);
    } // end of getAreaBasedList

    private void displayLoader() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("잠시만 기다려 주세요..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

}
