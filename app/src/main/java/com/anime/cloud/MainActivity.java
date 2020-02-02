package com.anime.cloud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anime.cloud.Adapters.MainAdapter;
import com.anime.cloud.Adapters.OnClickInterface;
import com.anime.cloud.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import okhttp3.internal.Util;

public class MainActivity extends AppCompatActivity implements OnClickInterface {

    private RecyclerView mRecyclerView;
    private ArrayList<PojoAnime> mCollectionsAnimeHome;
    private MainAdapter mAdapter;
    private ProgressBar mProgressMain;

    private String testEnd = "https://api.jikan.moe/v3/search/anime?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideSystemUI();
        setupComponentUI();
    }

    private void setupComponentUI() {

        mRecyclerView = findViewById(R.id.recycler_view_main);
        mProgressMain = findViewById(R.id.progress_bar_main);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, Utils.calculateNoOfColumns(getApplicationContext()));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mProgressMain.setVisibility(View.VISIBLE);

        RequestQueue requestQueueFirst = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, Utils.BASE_ENDPOINT,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mCollectionsAnimeHome = new ArrayList<>();

                        //ScrapData
                        String body = response;
                        Document document = Jsoup.parse(body);
                        Elements titles = document.select("div.carousel-item");
                        for (Element title : titles) {
                            mCollectionsAnimeHome.add(new PojoAnime(
                                    title.text(),
                                    title.select("img").attr("abs:src"),
                                    title.select("div.carousel-item>a").attr("href")));

                        }
                        mAdapter = new MainAdapter(MainActivity.this, mCollectionsAnimeHome);
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        mProgressMain.setVisibility(View.GONE);
                        mAdapter.setOnClickInterface(MainActivity.this);

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Risorsa non disponibile", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueueFirst.add(request);
    }

    protected void hideSystemUI() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    );
        }
    }

    @Override
    public void onClickItem(View view, int pos) {
        Intent startDetail = new Intent(MainActivity.this, DetailAnime.class);
        startDetail.putExtra("DetailAnime", mCollectionsAnimeHome.get(pos));
        startActivity(startDetail);
    }
}
