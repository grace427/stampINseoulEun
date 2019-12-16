package com.example.mu338.stampinseoul;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class tutorial_fragment3 extends Fragment {

    private TextView textView;
    private View view;

    // 뷰페이저를 통해서 슬라이딩이나 탭키를 눌러서 뷰페이저 프래그먼트가 변경이 되려면 현재 프래그먼트 상태를 저장하는 변수 필요.
    public static tutorial_fragment3 newInstance(){

        tutorial_fragment3 fragment3 = new tutorial_fragment3();

        return fragment3;
    }

    // onCreate()와 같은 기능.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tutorial_frag3, container, false); // 메인 액티비티의 setContentView와 같음.

        textView = view.findViewById(R.id.textView);

        return view;
    }
}
