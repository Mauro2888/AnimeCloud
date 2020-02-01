package com.anime.cloud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anime.cloud.Adapters.MainAdapter;
import com.anime.cloud.Adapters.OnClickInterface;
import com.anime.cloud.Utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnClickInterface {

    private RecyclerView mRecyclerView;
    private ArrayList<PojoAnime> mcollectionOfAnime;
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideSystemUI();
        setupComponentUI();
    }

    private void setupComponentUI() {

        mRecyclerView = findViewById(R.id.recycler_view_main);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, Utils.calculateNoOfColumns(getApplicationContext()));
        mRecyclerView.setLayoutManager(mLayoutManager);

        RequestQueue requestQueueFirst = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, "https://animeunity.it/index.php/",
                new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mcollectionOfAnime = new ArrayList<>();

                //ScrapData
                String body = response;
                Document document = Jsoup.parse(body);
                Elements titles = document.select("div.carousel-item");
                for (Element title : titles) {
                    mcollectionOfAnime.add(new PojoAnime(
                            title.text(),
                            title.select("img").attr("abs:src"),
                            title.select("div.carousel-item>a").attr("href")));
                }

                mAdapter = new MainAdapter(MainActivity.this, mcollectionOfAnime);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnClickInterface(MainActivity.this);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueueFirst.add(request);

        /**
         * HashMap<String,String> map = new HashMap<>();
         *                     map.put("title",title.text());
         *                     map.put("img",title.select("img").attr("abs:src"));
         *                     map.put("urlPage",title.select("div.carousel-item>a").attr("href"));
         *
         *                     Gson gson = new Gson();
         *                     String jsonData = gson.toJson(map);
         *                     Log.d("ARRAY",jsonData);
         */
    }

    protected void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onClickItem(View view, int pos) {
        Intent startDetail = new Intent(MainActivity.this,DetailAnime.class);
        startDetail.putExtra("DetailAnime",mcollectionOfAnime.get(pos));
        startActivity(startDetail);
    }
}
