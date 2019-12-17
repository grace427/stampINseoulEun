package com.example.mu338.stampinseoul;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.example.mu338.stampinseoul.LoginActivity.userId;


public class ThemeActivity extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener, ViewPager.OnPageChangeListener, View.OnClickListener {

    private FragmentStatePagerAdapter fragmentStatePagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    boolean isDragged;

    private long backButtonTime = 0;

    // 찜 데이터베이스와 연동되는 리스트
    private ArrayList<ThemeData> list = new ArrayList<>();
    // 찜 목록에서 선택한 것만 담는 리스트
    private ArrayList<ThemeData> checkedList = new ArrayList<>();
    // 찜 목록에 뿌려주기 위해 만든 리스트
    private ArrayList<String> titleList = new ArrayList<>();

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Theme_favorites_adapter theme_favorites_adapter;

    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;

    private EditText edtSearch;
    private ImageButton btnSearch;

    public static String strNickname, strProfile;
    public static Long strId;
    private ListView listView;

    public static DBHelper dbHelper;
    public static SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        //뷰페이저 설정
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

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
        strId = intent.getLongExtra("id", 0);

        Toast.makeText(getApplicationContext(), strId + " 님, 환영합니다!", Toast.LENGTH_SHORT).show();

        // db helper 객체 생성
        dbHelper = new DBHelper(this);
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
        if (i == ViewPager.SCROLL_STATE_DRAGGING)
            isDragged = true;
    }

    @Override
    public void onBackPressed() {

        long currentTime = System.currentTimeMillis();
        long gapTime = currentTime - backButtonTime;

        if (gapTime >= 0 && gapTime <= 2000) {
            super.onBackPressed();
        } else {
            backButtonTime = currentTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fab:

                anim();

                break;

            case R.id.fab1:

                anim();

                Intent intent = new Intent(ThemeActivity.this, BottomMenuActivity.class);

                startActivity(intent);

                break;

            case R.id.fab2:
                list.removeAll(list);
                checkedList.removeAll(checkedList);
                titleList.removeAll(titleList);
                anim();

                final View viewDialog = v.inflate(v.getContext(), R.layout.dialog_favorites, null);

                listView = viewDialog.findViewById(R.id.listView);

                // 여기서 DB ZZIM 테이블에 들어있는거 리스트에 넣어서 뿌려주기
                db = dbHelper.getWritableDatabase();
                final Cursor cursor;

                cursor = db.rawQuery("SELECT * FROM ZZIM_" + strId + ";", null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        list.add(new ThemeData(cursor.getString(0), cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3)));
                        titleList.add(cursor.getString(0));
                    }
                }

                final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_check_box_color, titleList);

                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                Button btnSave = viewDialog.findViewById(R.id.btnSave);
                Button btnExit = viewDialog.findViewById(R.id.btnExit);


                final Dialog dialog = new Dialog(viewDialog.getContext());

                // Check
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        SparseBooleanArray booleans = listView.getCheckedItemPositions();

                        // 스탬프 리스트에 이미 있는 항목을 선택한 경우 checkedlist에 들어가지 못함
                        if (booleans.get(position)) {
                            checkedList.add(list.get(position));

                            Log.d("TAG", " 처음 체크했을때 체크리스트 : " + checkedList);

                            MainActivity.db = MainActivity.dbHelper.getWritableDatabase();
                            Cursor cursor;
                            cursor = MainActivity.db.rawQuery("SELECT title FROM STAMP_" + userId + ";", null);

                            if (checkedList != null) {
                                while (cursor.moveToNext()) {
                                    if (cursor.getString(0).equals(list.get(position).getTitle())) {
                                        Toast.makeText(getApplicationContext(), list.get(position).getTitle() + " 이미 스탬프 리스트에 들어 있습니다.", Toast.LENGTH_LONG).show();
                                        checkedList.remove(list.get(position));
                                    }
                                }
                                cursor.moveToFirst();
                            }

                        } else {
                            checkedList.remove(list.get(position));
                        }
                        cursor.close();
                        Log.d("TAG", "최종 체크리스트 : " + checkedList);
                    }
                });

                // Delete
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                        Log.d("TAG", "롱클릭 들어옴 " + list.get(position).getTitle());
                        String zzimDelete = "DELETE FROM ZZIM_" + userId + " WHERE title='" + list.get(position).getTitle() + "';";
                        db.execSQL(zzimDelete);
                        list.remove(list);
                        adapter.notifyDataSetChanged();
                        return true;
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.setContentView(viewDialog);
                dialog.show();

                // Insert
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.db = MainActivity.dbHelper.getWritableDatabase();
                        Cursor cursor;
                        cursor = MainActivity.db.rawQuery("SELECT * FROM STAMP_" + userId + ";", null);
                        cursor.moveToFirst();
                        Log.d("TAG", "스탬프 테이블 카운트 : " + String.valueOf(cursor.getCount()));

                        Log.d("TAG", "체크리스트 사이즈 : " + String.valueOf(checkedList.size()));

                        if (checkedList.size()+cursor.getCount() > 8) {
                            Toast.makeText(getApplicationContext(), "스탬프 리스트에 8개 이상 담을 수 없습니닷!!!\n현재 스탬프 리스트에는 "
                                    + cursor.getCount()+"개 들어있습니다.", Toast.LENGTH_LONG).show();

//                        } else if (cursor.getCount() > 8) {
//                            Toast.makeText(getApplicationContext(), "현재 스탬프 리스트에는 " + cursor.getCount() + "개 들어있습니다. 더이상 추가할 수 없습니다.", Toast.LENGTH_LONG).show();
//                            for (ThemeData themeData : checkedList) {
//                                String zzimDelete = "DELETE FROM STAMP_" + userId + " WHERE title='" + themeData.getTitle() + "';";
//                                db.execSQL(zzimDelete);
//                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "스탬프 리스트에 잘 담았습니다.", Toast.LENGTH_LONG).show();
                            for (ThemeData themeData : checkedList) {
                                String stampInsert = "INSERT INTO STAMP_" + userId + "(title, addr, mapX, mapY)" + " VALUES('" + themeData.getTitle() + "', '"
                                        + themeData.getAddr() + "', '"
                                        + themeData.getMapX() + "', '"
                                        + themeData.getMapY() + "');";

                                db.execSQL(stampInsert);
                            }
                        }
                        cursor.close();
                    }
                });

                btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                cursor.close();

            case R.id.btnSearch:

                String word = edtSearch.getText().toString().trim();

                Intent intent2 = new Intent(ThemeActivity.this, SearchActivity.class);

                if (word.length() > 1) {

                    intent2.putExtra("word", word);

                    startActivity(intent2);

                } else {
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


