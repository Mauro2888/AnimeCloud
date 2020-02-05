package com.anime.cloud;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anime.cloud.Utils.Utils;

import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashActivity extends AppCompatActivity {

    private HashMap<String, String> archive = new HashMap<>();
    private static final String LOG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen splashScreen = new EasySplashScreen(SplashActivity.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(5000)
                .withBackgroundColor(Color.parseColor("#000000"))
                .withLogo(R.drawable.icon_anime);

        View view = splashScreen.create();
        setContentView(view);
        downloadArchive();
        hideSystemUI();
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

    private void downloadArchive() {
        final HashMap<String, String> mapTitles_Urls = new HashMap<>();
       final ArrayList<String>arrayList_urls = new ArrayList<>();
       final ArrayList<String>arrayList_titles = new ArrayList<>();
        RequestQueue requestQueueFirst = Volley.newRequestQueue(this);
        final StringRequest request = new StringRequest(Request.Method.GET, Utils.BASE_ENDPOINT_ARCHIVE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String body = response;
                Document document = Jsoup.parse(body);

                Elements urls = document.select("a");
                Elements titles = document.select("h6.card-title");
                for (Element title : titles) {
                    arrayList_titles.add(title.text());
                 }

                for (Element url_titles : urls) {
                    String allUrls_final = url_titles.attr("href");
                    if (allUrls_final.contains("anime.php?id")){
                        arrayList_urls.add(allUrls_final);
                    }

                }

                for(int i = 0; i < arrayList_titles.size(); i++){
                    mapTitles_Urls.put(arrayList_titles.get(i), arrayList_urls.get(i));
                }

                for (Map.Entry<String,String> m:mapTitles_Urls.entrySet()) {
                    Log.d(LOG,m.getKey() + " " + m.getValue());
                }
                Log.d(LOG,String.valueOf(mapTitles_Urls.size()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueueFirst.add(request);
    }
}
