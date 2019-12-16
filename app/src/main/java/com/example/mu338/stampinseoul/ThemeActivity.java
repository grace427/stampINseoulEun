package com.example.mu338.stampinseoul;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class ThemeActivity extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener, ViewPager.OnPageChangeListener, View.OnClickListener {

    private FragmentStatePagerAdapter fragmentStatePagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    boolean isDragged;

    private long backButtonTime = 0;

    private ArrayList<ThemeFavoritesData> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Theme_favorites_adapter theme_favorites_adapter;

    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;

    private EditText edtSearch;
    private ImageButton btnSearch;

    String strNickname, strProfile;
    Long strId;
    Long ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        //뷰페이저 설정
        viewPager=findViewById(R.id.viewPager);
        tabLayout=findViewById(R.id.tabLayout);

        fragmentStatePagerAdapter = new ThemeViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(fragmentStatePagerAdapter);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                tabLayout.setTabsFromPagerAdapter(fragmentStatePagerAdapter);
                tabLayout.addOnTabSelectedListener(ThemeActivity.this);
            }
        });

        viewPager.addOnPageChangeListener(this);

        // == 검색

        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(this);


        // == 플로팅 버튼, 드로어

        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

        Intent intent = getIntent();

        strNickname = intent.getStringExtra("name");
        strProfile = intent.getStringExtra("profile");
        Long strId = intent.getLongExtra("id", 0L);

        Log.d("dd", strId.toString());

        Toast.makeText(getApplicationContext(), strId+" 님, 환영합니다!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fragmentStatePagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        /*if( !isDragged ){
            viewPager.setCurrentItem(tab.getPosition());
        }
        isDragged = false;*/
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {
        if( i == ViewPager.SCROLL_STATE_DRAGGING)
            isDragged = true;
    }

    @Override
    public void onBackPressed() {

        long currentTime = System.currentTimeMillis();
        long gapTime = currentTime - backButtonTime;

        if( gapTime >= 0 && gapTime <= 2000){
            super.onBackPressed();
        }else {
            backButtonTime = currentTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.fab :

                anim();

                break;

            case R.id.fab1 :

                anim();

                Intent intent = new Intent(ThemeActivity.this, BottomMenuActivity.class);

                startActivity(intent);

                break;

            case R.id.fab2 :

                anim();

                final View viewDialog = v.inflate(v.getContext(), R.layout.dialog_favorites, null);

                recyclerView = viewDialog.findViewById(R.id.recyclerView);

                linearLayoutManager = new LinearLayoutManager(viewDialog.getContext());

                recyclerView.setLayoutManager(linearLayoutManager);

                theme_favorites_adapter = new Theme_favorites_adapter(R.layout.dialog_favorites_item, list);

                recyclerView.setAdapter(theme_favorites_adapter);

                Button btnSave = viewDialog.findViewById(R.id.btnSave);
                Button btnExit = viewDialog.findViewById(R.id.btnExit);

                final Dialog dialog = new Dialog(viewDialog.getContext());

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.setContentView(viewDialog); // 이미지가 들어감
                dialog.show();

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                break;

            case R.id.btnSearch :

                String word = edtSearch.getText().toString().trim();

                Intent intent2 = new Intent(ThemeActivity.this, SearchActivity.class);

                if(word.length() > 1){

                    intent2.putExtra("word",word);

                    startActivity(intent2);

                }else{
                    Toast.makeText(getApplicationContext(), "두 글자 이상 입력해 주세요", Toast.LENGTH_LONG).show();
                }

            default:

                break;

        }
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
