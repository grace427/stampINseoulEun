package com.example.mu338.stampinseoul;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Random;

public class GpsActivity extends Fragment implements View.OnClickListener, View.OnTouchListener {

    boolean win = false;

    private static final String TAG = "MainActivity";

    // Log.d(TAG,"");

    LocationManager locManager;
    AlertReceiver receiver;
    TextView locationText;
    PendingIntent proximityIntent;

    boolean isPermitted = false;
    boolean isLocRequested = false;
    boolean isAlertRegistered = false;

    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    Button alert, alert_release;

    double lastlat = 0.0;
    double lastlng = 0.0;

    GoogleMap gMap;
    GroundOverlayOptions cctvMark;
    ArrayList<MarkerOptions> cctvList = new ArrayList<MarkerOptions>();
    boolean chcked = false;
    private float min = 200.0f;

    // == 리사이클러뷰
    private ArrayList<ThemeData> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private GpsAdapter gpsAdapter;

    private View view;

    // 플로팅 버튼, 드로어

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;

    private FloatingActionButton fab, fab1;
    private DrawerLayout dl;
    private ConstraintLayout drawer;

    private GpsAnimationDialog gpsAnimationDialog;

    int[] img = {R.drawable.gps_back1, R.drawable.gps_back2, R.drawable.gps_back3, R.drawable.gps_back4 };

    LottieAnimationView animationView = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_gps, container, false);

        MainActivity.db = MainActivity.dbHelper.getWritableDatabase();
        Cursor cursor;
        cursor = MainActivity.db.rawQuery("SELECT * FROM STAMP_"+LoginActivity.userId+";", null);
        if(cursor != null){
            while(cursor.moveToNext()){
                list.add(new ThemeData(cursor.getString(1), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4)));
            }
        }
        // == 로딩 애니메이션

        gpsAnimationDialog = new GpsAnimationDialog(view.getContext());
        gpsAnimationDialog.show();

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gpsAnimationDialog.dismiss();

            }
        }, 2800);


        // == 리사이클러뷰

        recyclerView = view.findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(linearLayoutManager);

        gpsAdapter = new GpsAdapter(R.layout.gps_item, list);

        recyclerView.setAdapter(gpsAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {
                lastlng=list.get(position).getMapX();
                lastlat=list.get(position).getMapY();
                Log.d("TAG", "위도 경도ㅓ: "+lastlat+"  "+lastlng+" 타이틀  : "+list.get(position).getTitle());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // == GPS

        locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationText = view.findViewById(R.id.location);

        alert = view.findViewById(R.id.alert);
        alert_release = view.findViewById(R.id.alert_release);

        requestRuntimePermission();

        try {

            if (isPermitted) {

                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, gpsLocationListener);
                isLocRequested = true;

            } else {
                Toast.makeText(getActivity(), "Permission이 없습니다..", Toast.LENGTH_LONG).show();
            }

        } catch (SecurityException e) {

            e.printStackTrace();

        }

        // ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

        alert.setOnClickListener(this);
        alert_release.setOnClickListener(this);


        // == 플로팅 버튼, 드로어

        fab = view.findViewById(R.id.fab);
        fab1 = view.findViewById(R.id.fab1);

        fab_open = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_close);

        dl = view.findViewById(R.id.dl);
        drawer = view.findViewById(R.id.drawer);
        ConstraintLayout gps_back = view.findViewById(R.id.gps_back);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);

        drawer.setOnTouchListener(this);
        dl.setDrawerListener(listener);


        Random ram = new Random();

        int num = ram.nextInt(img.length);
        gps_back.setBackgroundResource(img[num]);

        Animation ai = AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_in);
        gps_back.startAnimation(ai);

        locationText.setText("등록 버튼을 눌러주세요.");

        String[] s = {"red_wave.json", "blue_wave.json", "yellow_wave.json", "green_wave.json", "black_wave.json"};

        animationView = view.findViewById(R.id.animation_view);

        /*for ( int i = 0 ; i < s.length ; i++){

            if(animationView.isAnimating() == true){
                animationView.cancelAnimation();
                animationView.setAnimation(s[i]);
                animationView.loop(true);
                animationView.playAnimation();
            }else {
                animationView.setAnimation(s[i]);
                animationView.loop(true);
                animationView.playAnimation();
            }

        }*/

            animationView.cancelAnimation();
            animationView.setAnimation("black_wave.json");
            animationView.loop(true);
            animationView.playAnimation();


        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.alert:

                receiver = new AlertReceiver();

                IntentFilter filter = new IntentFilter("com.example.mu338.stampinseoul");

                v.getContext().registerReceiver(receiver, filter);

                Intent intent = new Intent("com.example.mu338.stampinseoul");

                proximityIntent = PendingIntent.getBroadcast(v.getContext(), 0, intent, 0);

                try {

                    if(lastlat !=0.0 && lastlng !=0.0) {
                        win = true;

                        locManager.addProximityAlert(lastlat, lastlng, min, -1, proximityIntent);

                        Toast.makeText(getActivity(), "GPS기능을 시작합니다.", Toast.LENGTH_SHORT).show();

                        animationView = view.findViewById(R.id.animation_view);

                        if (animationView.isAnimating()) {
                            animationView.cancelAnimation();
                        }

                        locationText.setText("GPS기능을 시작합니다.");
                    }else{
                        Toast.makeText(view.getContext(), "목적지를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }

                } catch (SecurityException e) {

                    e.printStackTrace();

                }

                isAlertRegistered = true;

                break;

            case R.id.alert_release:

                min=200.0f;

                if(animationView.isAnimating()){
                    animationView.cancelAnimation();
                }

                animationView = view.findViewById(R.id.animation_view);

                animationView.setAnimation("black_wave.json");
                animationView.loop(true);
                // animationView.setRe
                animationView.playAnimation();

                try {

                    if (isAlertRegistered) {

                        locManager.removeProximityAlert(proximityIntent);
                        getActivity().unregisterReceiver(receiver);
                        isAlertRegistered = false;

                    }

                    win = false;
                    Toast.makeText(getActivity(), "GPS기능을 해제합니다.", Toast.LENGTH_SHORT).show();

                    locationText.setText("등록버튼을 눌러주세요.");

                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.fab :

                anim();

                break;

            case R.id.fab1 :

                anim();
                dl.openDrawer(drawer);

                break;
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        try {

            if (isLocRequested) {
                locManager.removeUpdates(gpsLocationListener);
                isLocRequested = false;
            }

            if (isAlertRegistered) {
                locManager.removeProximityAlert(proximityIntent);
                getActivity().unregisterReceiver(receiver);
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    final LocationListener gpsLocationListener = new LocationListener() {

        //단점은 움직여서 값이 변동이 되야 한다 그래야 작동한다.

        public void onLocationChanged(Location location) {

            if (win) {

                try {
                    locManager.addProximityAlert(lastlat, lastlng, min, -1, proximityIntent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }

//            chcked = true;
//            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(MainActivity.this);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }

    };

    private void requestRuntimePermission() {

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            }

        } else {

            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener);

            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, gpsLocationListener);

            isPermitted = true;

        }

    }

    @Override

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isPermitted = true;
                } else {
                    isPermitted = false;
                }
                return;
            }

        }

    }


    // gps 값 받는 브로드캐스트 리시버 내부클래스

    public class AlertReceiver extends BroadcastReceiver {

        @Override

        public void onReceive(Context context, Intent intent) {

            boolean isEntering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);

            boolean test = false;

            animationView = view.findViewById(R.id.animation_view);

            Log.d(TAG, "AlertReceiver" + isEntering);

            if (isEntering) {

                int mm = (int) min;

                switch (mm) {

                    case 20:

                        if(animationView.isAnimating() == true){
                            animationView.cancelAnimation();
                        }

                        Toast.makeText(context, "목표에 도착했습니다.", Toast.LENGTH_LONG).show();

                        // dl.setBackgroundColor(Color.YELLOW);

                        animationView.setAnimation("green_wave.json");
                        animationView.loop(true);
                        animationView.playAnimation();

                        locationText.setText("목표 지점에 도착했어요.");

                        //Log.d(TAG,"4번");

                        break;

                    case 50:

                        if(animationView.isAnimating() == true){
                            animationView.cancelAnimation();
                        }

                        Toast.makeText(context, "목표반경 50미터 안에 들어왔습니다.", Toast.LENGTH_LONG).show();
                        // dl.setBackgroundColor(Color.BLUE);

                        animationView.setAnimation("yellow_wave.json");
                        animationView.loop(true);
                        animationView.playAnimation();

                        locationText.setText("목표반경 50미터 내에 들어왔어요.");

                        min = 20.0f;
                        //Log.d(TAG,"3번");



                        break;

                    case 100:

                        if(animationView.isAnimating() == true){

                            animationView.cancelAnimation();

                        }

                        Toast.makeText(context, "목표반경 100미터 안에 들어왔습니다.", Toast.LENGTH_LONG).show();
                        // dl.setBackgroundColor(Color.RED);

                        animationView.setAnimation("blue_wave.json");
                        animationView.loop(true);
                        animationView.playAnimation();

                        locationText.setText("목표반경 100미터 내에 들어왔어요.");

                        min = 50.0f;
                        //Log.d(TAG,"2번");



                        break;

                    case 200:

                        if(animationView.isAnimating() == true){
                            animationView.cancelAnimation();
                        }

                        Toast.makeText(context, "목표반경 200미터 안에 들어왔습니다.", Toast.LENGTH_LONG).show();
                        //dl.setBackgroundColor(Color.GREEN);

                        animationView.setAnimation("red_wave.json");
                        animationView.loop(true);
                        animationView.playAnimation();

                        locationText.setText("목표반경 200미터 내에 들어왔어요.");

                        //Log.d(TAG,"1번");
                        min = 100.0f;


                        break;

                }

            } else {

                int mm = (int) min;

                switch (mm) {

                    case 20:

                        if(animationView.isAnimating() == true){
                            animationView.cancelAnimation();
                        }

                        Toast.makeText(context, "목표반경 20미터 멀어졌습니다.", Toast.LENGTH_LONG).show();

                        animationView.setAnimation("yellow_wave.json");
                        animationView.loop(true);
                        animationView.playAnimation();

                        locationText.setText("목표반경 20미터 내에서 벗어났어요.");

                        min = 50.0f;

                        break;

                    case 50:

                        if(animationView.isAnimating() == true){
                            animationView.cancelAnimation();
                        }

                        Toast.makeText(context, "목표반경 50미터 멀어졌습니다.", Toast.LENGTH_LONG).show();

                        animationView.setAnimation("blue_wave.json");
                        animationView.loop(true);
                        animationView.playAnimation();

                        locationText.setText("목표반경 50미터 내에서 벗어났어요.");

                        min = 100.0f;

                        break;

                    case 100:

                        if(animationView.isAnimating() == true){
                            animationView.cancelAnimation();
                        }

                        Toast.makeText(context, "목표반경 100미터 멀어졌습니다.", Toast.LENGTH_LONG).show();

                        animationView.setAnimation("red_wave.json");
                        animationView.loop(true);
                        // animationView.setRe
                        animationView.playAnimation();

                        locationText.setText("목표반경 100미터 내에서 벗어났어요.");

                        min = 200.0f;
                        break;

                    case 200:

                        if(animationView.isAnimating() == true){
                            animationView.cancelAnimation();
                        }

                        Toast.makeText(context, "목표에서 200미터 멀어졌습니다", Toast.LENGTH_LONG).show();

                        animationView.setAnimation("black_wave.json");
                        animationView.loop(true);
                        // animationView.setRe
                        animationView.playAnimation();

                        locationText.setText("목표반경 200미터 내에서 벗어났어요.");
                        break;

                }
            }
        }
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {

        // 슬라이딩을 시작 했을때 이벤트 발생
        @Override
        public void onDrawerSlide(@NonNull View view, float v) {

        }

        // 메뉴가 열었을때 이벤트 발생
        @Override
        public void onDrawerOpened(@NonNull View view) {

        }

        // 메뉴를 닫았을때 이벤트 발생
        @Override
        public void onDrawerClosed(@NonNull View view) {

        }

        // 메뉴 바가 상태가 바뀌었을때 이벤트 발생
        @Override
        public void onDrawerStateChanged(int i) {

        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public void anim() {

        if (isFabOpen) {

            fab1.startAnimation(fab_close);
            fab1.setClickable(false);
            isFabOpen = false;

        } else {

            fab1.startAnimation(fab_open);
            fab1.setClickable(true);
            isFabOpen = true;

        }
    }

}
