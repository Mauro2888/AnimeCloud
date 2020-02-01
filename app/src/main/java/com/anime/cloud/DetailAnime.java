package com.anime.cloud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anime.cloud.Adapters.DetailAdapter;
import com.anime.cloud.Adapters.OnClickInterface;
import com.anime.cloud.Adapters.PojoEpisodes;
import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class DetailAnime extends AppCompatActivity implements OnClickInterface {

    private PojoAnime pojoAnime;
    private ImageView mImageDetail;
    private DetailAdapter mDetailAdapter;
    private RecyclerView mRecyclerDetailEpisodes;
    private ArrayList<PojoEpisodes> mEpisodesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_anime);
        Intent intentMain = getIntent();
        pojoAnime = (PojoAnime) intentMain.getSerializableExtra("DetailAnime");
        String title = pojoAnime.getTitleAnime();
        final String url = pojoAnime.getUrlAnimePage();
        String image = pojoAnime.getUrlImg();

        mRecyclerDetailEpisodes = findViewById(R.id.recycler_view_detail);
        mRecyclerDetailEpisodes.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerDetailEpisodes.setHasFixedSize(true);

        mImageDetail = findViewById(R.id.anime_image_detail);
        Glide.with(DetailAnime.this).asBitmap().centerCrop().load(image).into(mImageDetail);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest =
                new StringRequest(Request.Method.GET, "https://animeunity.it/" + url + "/",
                        new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mEpisodesArrayList = new ArrayList<>();
                        //ScrapData
                        String body = response;
                        Document document = Jsoup.parse(body);
                        Elements titles = document.select("a");
                        for (Element title : titles) {
                            String all = title.attr("href");
                            if (all.contains("&ep")){
                                mEpisodesArrayList.add(new PojoEpisodes(title.text()));
                            }

                        }
                        mDetailAdapter = new DetailAdapter(DetailAnime.this,mEpisodesArrayList);
                        mRecyclerDetailEpisodes.setAdapter(mDetailAdapter);
                        mDetailAdapter.notifyDataSetChanged();
                        mDetailAdapter.setOnClickInterface(DetailAnime.this);
                        //title.attr("href"));
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue.add(stringRequest);
    }

    @Override
    public void onClickItem(View view, int pos) {
        Toast.makeText(this, "HEY", Toast.LENGTH_SHORT).show();
    }
}
