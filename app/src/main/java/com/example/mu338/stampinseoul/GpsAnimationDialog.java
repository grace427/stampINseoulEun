package com.example.mu338.stampinseoul;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class GpsAnimationDialog extends ProgressDialog {

    private Context c;
    // private ImageView imgGps;


    public GpsAnimationDialog(Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);

        c = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_roading_animation);

        /*imgGps = findViewById(R.id.gps_image);

        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(imgGps);
        Glide.with(c).load(R.drawable.markeranimaition).into(gifImage);
        */

        LottieAnimationView animationView = findViewById(R.id.animation_view);
        animationView.setAnimation("location_search.json");
        animationView.loop(true);
        animationView.playAnimation();

    }

    @Override
    public void show() {
        super.show();

    }

    @Override
    public void dismiss() {
        super.dismiss();

    }
}
