package com.example.mu338.stampinseoul;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;

public class AlbumActivity extends Fragment implements View.OnClickListener, View.OnTouchListener {

    /*private Integer[] reviewImage = { R.drawable.review_test1, R.drawable.review_test2, R.drawable.review_test3};

    private String[] reviewID = { "계정 테스트1", "계정 테스트2" , "계정 테스트3"};

    private String[] reviewTitle = { "테스트1", "테스트2", "테스트3"};

    private String[] reviewContent = { "내용 테스트1", "내용 테스트2", "내용 테스트3"};
    */

    private ArrayList<CameraData> cameraList = new ArrayList<>();
    private ArrayList<CameraData> cameraList2 = new ArrayList<>();

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private AlbumAdapter albumAdapter;

    private View view;


    // == 플로팅 버튼, 드로어

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;

    private FloatingActionButton fab, fab1, fab2;
    private DrawerLayout drawerLayout;
    private ConstraintLayout drawer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_album, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(linearLayoutManager);

        //Log.d("dd", );

        /*if ( cameraList.isEmpty()){
            cameraList = (ArrayList<CameraData>) getArguments().getSerializable("list");
        }*/

        albumAdapter = new AlbumAdapter(R.layout.album_item, cameraList);

        recyclerView.setAdapter(albumAdapter);

        // == 플로팅 버튼 , 드로어

        fab = view.findViewById(R.id.fab);
        fab1 = view.findViewById(R.id.fab1);
        fab2 = view.findViewById(R.id.fab2);

        fab_open = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_close);

        drawerLayout = view.findViewById(R.id.drawerLayout);
        drawer = view.findViewById(R.id.drawer);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

        drawer.setOnTouchListener(this);
        drawerLayout.setDrawerListener(listener);

        // == 카메라

        // cameraList = (ArrayList<CameraData>)getActivity().getIntent().getSerializableExtra("list");

        return view;

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.fab :

                anim();

                break;

            case R.id.fab1 :

                anim();
                drawerLayout.openDrawer(drawer);

                Bundle bundle = new Bundle();

                /*Log.d("dd", cameraList.toString());

                cameraList = (ArrayList<CameraData>) bundle.getSerializable("list");

                Log.d("dd", cameraList.toString());

                albumAdapter.notifyDataSetChanged(); */

                break;


            case R.id.fab2 :

                anim();

                Intent intent2 = new Intent(getActivity(), CameraActivity.class);

                startActivity(intent2);

                break;

            default: break;

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
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;

        } else {

            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;

        }
    }

}
