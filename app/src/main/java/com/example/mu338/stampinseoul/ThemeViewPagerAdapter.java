package com.example.mu338.stampinseoul;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class ThemeViewPagerAdapter extends FragmentStatePagerAdapter {

    public ThemeViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //프래그먼트 교체를 보여주는 역할
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return Theme_festival_frag.newInstance();
            case 1: return Theme_shpping_frag.newInstance();
            case 2: return Theme_food_frag.newInstance();
            case 3: return Theme_Acti_frag.newInstance();
            case 4: return Theme_culture_frag.newInstance();
            default: return null;
        }

    }

    @Override
    public int getCount() {
        return 5;
    }

    //상단의 탭 레이아웃 인디케이터에 텍스트를 선언해주는것
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){

            case 0: return "축제";
            case 1: return "쇼핑";
            case 2: return "맛집";
            case 3: return "액티비티";
            case 4: return "문화";
            default: return null;
        }
    }

}
