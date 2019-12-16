package com.example.mu338.stampinseoul;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class tutorial_fragment1 extends Fragment {

    private TextView textView;
    private View view;
    private Button btnStart;

    // 뷰페이저를 통해서 슬라이딩이나 탭키를 눌러서 뷰페이저 프래그먼트가 변경이 되려면 현재 프래그먼트 상태를 저장하는 변수 필요.
    public static tutorial_fragment1 newInstance(){

        tutorial_fragment1 fragment1 = new tutorial_fragment1();

        return fragment1;
    }

    // onCreate()와 같은 기능.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tutorial_frag1, container, false); // 메인 액티비티의 setContentView와 같음.

        textView = view.findViewById(R.id.textView);
        btnStart = view.findViewById(R.id.btnStart);

        btnStart.setVisibility(View.INVISIBLE);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ThemeActivity.class);

                startActivity(intent);

            }
        });


        return view;
    }
}
