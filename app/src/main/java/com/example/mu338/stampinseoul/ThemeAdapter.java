package com.example.mu338.stampinseoul;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import xyz.hanks.library.bang.SmallBangView;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.MyViewHolder> {

    int i=0;
    final static String TAG = "ThemeActivity";

    Context context;
    ArrayList<ThemeData> list;

    int layout;
    // Hashtable<Integer, ThemeData> item;

    View view;
    View viewDialog;

    static final String KEY = "OEZDFxQGYkA8crUzSlj51nwQQb9Jh78Y5UWvaW5gXccZ5t2ttRXNjcdXjJJ8FsHlriUWu%2B%2FVhFfuI32FbuMhTA%3D%3D";
    static final String appName = "Zella";

    ThemeData detailThemeData = new ThemeData();

    RequestQueue queue;

    Bitmap bitmap;

    public ThemeAdapter(int layout, Context context, ArrayList<ThemeData> list) {
        super();
        this.layout = layout;
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txtView.setText(list.get(position).getTitle());

        Glide.with(context).load(list.get(position).getFirstImage()).into(holder.imgView);


       /* Thread thread = new Thread(){

            @Override
            public void run(){

                try{

                    URL url = new URL(list.get(position).getFirstImage());

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

            holder.imgView.setImageBitmap(bitmap);

        }catch (InterruptedException e){

        }*/


        // loadImageInBackground(list.get(position).getFirstImage(), context);
        // Log.d(TAG, list.get(position).getTitle()+"   "+list.get(position).getFirstImage()+"   "+(i++));
        holder.itemView.setTag(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    int position = (int) v.getTag();

                    ThemeAdapter.AsyncTaskClassSub asyncSub = new ThemeAdapter.AsyncTaskClassSub();
                    asyncSub.execute(position);

            }
        });

        /*holder.imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(list.get(position).isHart()){

                    holder.imagebtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);

                    list.get(position).setHart(false);

                }else{

                    holder.imagebtn.setImageResource(R.drawable.ic_favorite_black_24dp);

                    list.get(position).setHart(true);

                }

            }
        });*/

        holder.Like_heart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (holder.Like_heart.isSelected()) {

                    holder.Like_heart.setSelected(false);

                } else {

                    holder.Like_heart.setSelected(true);

                    holder.Like_heart.likeAnimation(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                        }

                    });
                }

            }
        });

    }

    @Override
    public int getItemCount() {
         return list == null ? 0: list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

      //  public ImageView imgView;
        public TextView txtView;
        public ImageView imgView;
        public ImageView imagebtn;

        public SmallBangView Like_heart;
        public ImageView imageHeart;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtView = itemView.findViewById(R.id.txtView);
            imgView = itemView.findViewById(R.id.imgView);
            //imagebtn = itemView.findViewById(R.id.imagebtn);

            Like_heart = itemView.findViewById(R.id.like_heart);
            imageHeart = itemView.findViewById(R.id.imageHeart);

        }
    }

    public ThemeData TourData(int position){

        return list != null ? list.get(position) : null;
    }

    /*public void loadImageInBackground(String str, Context context) {


        Target target = new Target() {

            @Override
            public void onPrepareLoad(Drawable arg0) {


        }

            @Override
            public void onBitmapLoaded(Bitmap arg0, Picasso.LoadedFrom arg1) {

                imgView.setImageBitmap(arg0);
            }

            @Override
            public void onBitmapFailed(Drawable arg0) {
                // TODO Auto-generated method stub
            }
        };

        Picasso.with(context)
                .load(str)
                .into(target);
    }*/

    class AsyncTaskClassSub extends android.os.AsyncTask<Integer, ThemeData, ThemeData> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ThemeData doInBackground(Integer... integers) {

            int position = integers[0];

            //Log.d(TAG, "TwoFrag의 asynckTask에서 받는 포지션 : "+position);

            // ThemeData themeData = getData(contentIdList.get(position));

            ThemeData myThemeData1 = list.get(position);

            ThemeData themeData = getData(myThemeData1.getContentsID());

            // publishProgress(themeData);

            return themeData;
        }

        @Override
        protected void onProgressUpdate(ThemeData... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ThemeData themeData) {
            super.onPostExecute(themeData);

            viewDialog = View.inflate(context, R.layout.dialog_info, null);

            TextView txt_Detail_title = viewDialog.findViewById(R.id.txt_Detail_title);
            TextView txt_Detail_addr = viewDialog.findViewById(R.id.txt_Detail_addr);
            TextView txt_Detail_info = viewDialog.findViewById(R.id.txt_Detail_info);

            Button btnExit = viewDialog.findViewById(R.id.btnExit);

            ImageView img_Datail_info = viewDialog.findViewById(R.id.img_Datail_info);

            txt_Detail_title.setText(themeData.getTitle());
            txt_Detail_addr.setText(themeData.getAddr());
            txt_Detail_info.setText(themeData.getOverView());

            Glide.with(context).load(themeData.getFirstImage()).override(500, 300).into(img_Datail_info);

            final Dialog dialog = new Dialog(viewDialog.getContext());

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.setContentView(viewDialog); // 이미지가 들어감
            dialog.show();

            btnExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


        }
    } // end of AsyncTaskClass

    private ThemeData getData(int contentID) {

        queue = Volley.newRequestQueue(context);

        String url = "http://api.visitkorea.or.kr/openapi/service/"
                + "rest/KorService/detailCommon?ServiceKey=" + KEY
                + "&contentId=" + contentID
                + "&firstImageYN=Y&mapinfoYN=Y&addrinfoYN=Y&defaultYN=Y&overviewYN=Y"
                + "&pageNo=1&MobileOS=AND&MobileApp="
                + appName + "&_type=json";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject parse_response = (JSONObject) response.get("response");
                            JSONObject parse_body = (JSONObject) parse_response.get("body");
                            JSONObject parse_items = (JSONObject) parse_body.get("items");
                            JSONObject parse_itemlist = (JSONObject) parse_items.get("item");

                            //list.removeAll(list);

                            // detailThemeData = null;
                            detailThemeData.setFirstImage(parse_itemlist.getString("firstimage"));
                            detailThemeData.setTitle(parse_itemlist.getString("title"));
                            detailThemeData.setAddr(parse_itemlist.getString("addr1"));
                            detailThemeData.setOverView(parse_itemlist.getString("overview"));

                            //Toast.makeText(getActivity(), "봐야됨 "+ parse_itemlist.getString("addr1"), Toast.LENGTH_SHORT).show();

                            //list.add(detailThemeData);

                            // Log.d(TAG, " Two frg : "+detailThemeData.getTitle());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,
                                error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(jsObjRequest);
        // Log.d(TAG, "getDATA에서 : "+detailThemeData);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return detailThemeData;
    }

}


