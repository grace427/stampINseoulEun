package com.example.mu338.stampinseoul;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MoreActivity extends Fragment {

    private String[] txtContent = { "내 정보", "고객 센터", "이용 약관", "App 정보"};

    private ArrayList<String> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private MoreAdapter moreAdapter;

    private View view;

    private Button btnLogout;
    private ImageView imgKakao;
    private TextView txtKakaoName;

    String strNickname, strProfile;
    Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_more, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(linearLayoutManager);

        list.removeAll(list);

        for(int i = 0 ; i < txtContent.length ; i++){

            list.add(txtContent[i]);
        }


        moreAdapter = new MoreAdapter(R.layout.more_item, list);

        recyclerView.setAdapter(moreAdapter);

        btnLogout = view.findViewById(R.id.btnLogout);

        /*
        Bundle extra = this.getArguments();

        if(extra != null ){

            extra = getArguments();

            strNickname = extra.getString("name");
            strProfile = extra.getString("profile");

            txtKakaoName.setText(strNickname);

            Thread thread = new Thread(){

                @Override
                public void run(){

                    try{

                        URL url = new URL(strProfile);

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                        conn.setDoInput(true);

                        conn.connect();

                        InputStream is = conn.getInputStream();

                        bitmap = BitmapFactory.decodeStream(is);


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

            };

            thread.start();

            try {

                thread.join();

                imgKakao.setImageBitmap(bitmap);

            }catch (InterruptedException e){

            }

        }
        */


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogout();
                Toast.makeText(view.getContext(), "로그아웃 합니다.", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    /*private void onClickLogout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }*/
    private void onClickLogout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {

                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);

            }
        });
    }
}
