package com.example.mu338.stampinseoul;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GpsAdapter extends RecyclerView.Adapter<GpsAdapter.CustomViewHolder> {
                                    // 리사이클러뷰 어댑터, 메인 어댑터에서 제공되는 내부클래스를 쓰는것, CustomViewHolder는 내가 직접 만드는것.
                                    // 3개의 메소드인데, 리스트뷰 메소드 4개가 포함되어있음.

    // 1. private Context context : onCreateViewHolder에서 ViewGroup으로 제공이 된다.
    private int layout;
    private ArrayList<ThemeData> list;

    private LayoutInflater layoutInflater;

    public GpsAdapter(int layout, ArrayList<ThemeData> list) {
        this.layout = layout;
        this.list = list;
    }


    // 뷰 홀더에 있는 화면을 객체화해서 해당된 viewHolder 리턴한다.
    @NonNull
    @Override // getView와 같음.
    public GpsAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        // 이 view는 밑 내부클래스 생성자인 itemView에 넘겨줌. 레이아웃 인플레이터
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);

        // 해당된 뷰 홀더의 아이디를 찾는다.
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    // customViewHolder : 뷰 홀더의 정보가 들어옴. 값을 넣는다.
    @Override
    public void onBindViewHolder(@NonNull final GpsAdapter.CustomViewHolder customViewHolder, final int position) {

        // customViewHolder.imaProfile.setImageResource(list.get(position).getMissionImgProfile()); // 값을 넣는다.
        customViewHolder.txtName.setText(list.get(position).getTitle());
        customViewHolder.txtContent.setText(list.get(position).getAddr());

        customViewHolder.itemView.setTag(position);



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

        // 아이템뷰에는 뷰 홀더가 객체가 된 레이아웃 주소가 전달이 됨.
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            imaProfile = itemView.findViewById(R.id.fImgProfile);
            txtName = itemView.findViewById(R.id.fTxtName);
            txtContent = itemView.findViewById(R.id.txtContent);

        }
    }
}
