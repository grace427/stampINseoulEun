package com.example.mu338.stampinseoul;

import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mu338.stampinseoul.MainActivity;
import com.example.mu338.stampinseoul.R;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.ApiErrorCode;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin, btnGuestLogin;
    private LoginButton btn_kakao_login;

    private SessionCallback sessionCallback;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);

        btnGuestLogin = findViewById(R.id.btnGuestLogin);
        btn_kakao_login = findViewById(R.id.btn_kakao_login);

        // == 카카오

        sessionCallback = new SessionCallback(); // SessionCallback 초기화
        Session.getCurrentSession().addCallback(sessionCallback); // 현재 세션에 콜백 붙임.

        Session.getCurrentSession().checkAndImplicitOpen(); // 자동 로그인

        // ==

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_kakao_login.performClick();

            }
        });

        btnGuestLogin.setEnabled(false);

        btnGuestLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnGuestLogin :

                Intent intent = new Intent(LoginActivity.this, ThemeActivity.class);

                startActivity(intent);

                finish();

                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 현재 액티비티 제거시 콜백도 같이 제거
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    private class SessionCallback implements ISessionCallback {

        // 로그인 세션이 열렸을때.
        @Override
        public void onSessionOpened() {

            // 유저 정보를 받아오는 함수 이름 me
            UserManagement.getInstance().me(new MeV2ResponseCallback() {

                // 로그인에 실패했을 때
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                // 로그인 도중 세션이 비정상적인 이유로 닫혔을 때.
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                }

                // 로그인에 성공했을 때. MeV2Response 객체 넘어오는데, 로그인한 유저의 정보를 담고 있는 중요객체.
                @Override
                public void onSuccess(MeV2Response result) {

                    Intent intent = new Intent(getApplicationContext(), ThemeActivity.class);

                    intent.putExtra("name", result.getNickname()); // 유저 닉네임
                    intent.putExtra("profile", result.getProfileImagePath()); // 카카오톡 프로필 이미지
                    intent.putExtra("id", result.getId());


                    Log.d("dd", String.valueOf(result.getId()));

                    startActivity(intent);

                    finish();

                }
            });
        }

        // 로그인 세션이 정상적으로 열리지 않았을 때.
        @Override
        public void onSessionOpenFailed(KakaoException e) {

            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();

        }
    }

}
