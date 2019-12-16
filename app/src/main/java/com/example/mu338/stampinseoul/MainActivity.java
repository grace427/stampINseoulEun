package com.example.mu338.stampinseoul;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {

    private long backButtonTime = 0;

    private FragmentPagerAdapter fragmentPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_viewpager);

        viewPager = findViewById(R.id.viewPager);

        // 1. 양식 1번
        fragmentPagerAdapter = new tutorialViewPagerAdapter(getSupportFragmentManager());

        CircleIndicator indicator = findViewById(R.id.indicator);

        viewPager.setAdapter(fragmentPagerAdapter);

        indicator.setViewPager(viewPager);
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
}
