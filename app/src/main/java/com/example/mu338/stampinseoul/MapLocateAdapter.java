package com.example.mu338.stampinseoul;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MapLocateAdapter extends RecyclerView.Adapter<MapLocateAdapter.CustomViewHolder> {

    private int layout;
    private ArrayList<ThemeData> list;

    static int number = 0;

    public MapLocateAdapter(int layout, ArrayList<ThemeData> list) {
        this.layout = layout;
        this.list = list;
    }


    @NonNull
    @Override
    public MapLocateAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final MapLocateAdapter.CustomViewHolder customViewHolder, final int position) {

        customViewHolder.txtName.setText(list.get(position).getTitle());
        customViewHolder.txtContent.setText(list.get(position).getAddr());

        customViewHolder.itemView.setTag(position);

        customViewHolder.imaProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String str = list.get(position).getTitle();

                Uri uri = Uri.parse("https://www.google.com/search?q="+str+"&oq="+str+"&aqs=chrome");

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                intent.setPackage("com.android.chrome");

                v.getContext().startActivity(intent);

            }
        });
        customViewHolder.imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CameraActivity.class);
                intent.putExtra("title", list.get(position).getTitle());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override // 리스트의 사이즈를 준다.
    public int getItemCount() {
        return (list != null) ? (list.size()) : (0); // 리스트에 값이 들어있으면 ~
    }


    // =========== 내부 클래스

    // 상속을 받아야됨. // 생성자를 만들어줄것
    // 홀더뷰가 객체화 되면 파인드뷰아이디를 여기서 찾아줌.
    // getView를 분업화. 인플레이터, 바인딩, 파인드 뷰 아이디.
    // 매치는 onBindViewHolder에서 해줌.
    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public ImageView imaProfile;
        public TextView txtName;
        public TextView txtContent;
        public ImageView imageCamera;

        // 아이템뷰에는 뷰 홀더가 객체가 된 레이아웃 주소가 전달이 됨.
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCamera = itemView.findViewById(R.id.imageCamera);
            imaProfile = itemView.findViewById(R.id.fImgProfile);
            txtName = itemView.findViewById(R.id.fTxtName);
            txtContent = itemView.findViewById(R.id.txtContent);

        }
    }
}
