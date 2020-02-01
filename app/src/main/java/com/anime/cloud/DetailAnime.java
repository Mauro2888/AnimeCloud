package com.anime.cloud;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class DetailAnime extends AppCompatActivity {

    private PojoAnime pojoAnime;
    private ImageView mImageDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_anime);

        Intent intentMain = getIntent();
        pojoAnime = (PojoAnime) intentMain.getSerializableExtra("DetailAnime");
        String title = pojoAnime.getTitleAnime();
        final String url = pojoAnime.getUrlAnimePage();
        String image = pojoAnime.getUrlImg();

        mImageDetail = findViewById(R.id.anime_image_detail);
        Glide.with(DetailAnime.this).asBitmap().centerCrop().load(image).into(mImageDetail);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest =
                new StringRequest(Request.Method.GET, "https://animeunity.it/" + url + "/",
                        new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //ScrapData
                        String body = response;
                        Document document = Jsoup.parse(body);
                        Elements titles = document.select("a");
                        for (Element title : titles) {
                            //mcollectionOfAnime.add(new PojoAnime(
                            String all = title.attr("href");
                            if (all.contains("&ep")){
                                Log.d("DATA",title.text() + " " + title.attr("href"));
                            }
                            
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue.add(stringRequest);
    }
}
