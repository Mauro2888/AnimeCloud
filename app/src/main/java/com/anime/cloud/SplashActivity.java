package com.anime.cloud;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashActivity extends AppCompatActivity {

    private HashMap<String, String> archive = new HashMap<>();
    private FirebaseFirestore mFireStore;
    private FirebaseDatabase reference = FirebaseDatabase.getInstance();
    private DatabaseReference mData = reference.getReference();
    private static final String LOG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen splashScreen = new EasySplashScreen(SplashActivity.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(10000)
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


        final ArrayList<String> arrayList_urls = new ArrayList<>();
        final ArrayList<String> arrayList_titles = new ArrayList<>();
        final ArrayList<String> arrayList_plots = new ArrayList<>();
        final ArrayList<String> arrayList_generes = new ArrayList<>();
        final ArrayList<String> arrayList_mangakas = new ArrayList<>();
        //mFireStore = FirebaseFirestore.getInstance();

        RequestQueue requestQueueFirst = Volley.newRequestQueue(this);
        final StringRequest request = new StringRequest(Request.Method.GET, Utils.BASE_ENDPOINT_ARCHIVE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String body = response;
                Document document = Jsoup.parse(body);

                Elements urls = document.select("a");
                Elements titles = document.select("h6.card-title");
                Elements mangakas = document.select("p.card-text.text-secondary");
                Elements plots = document.select("p.card-text.archive-plot");
                Elements generes = document.select("div.card-footer.archive-card-footer>badge.btn-archive-genres");

                for (Element title : titles) {
                    arrayList_titles.add(title.text());
                }

                for (Element mangaka : mangakas) {
                    arrayList_mangakas.add(mangaka.text());
                }

                for (Element plot : plots) {
                    arrayList_plots.add(plot.text());
                }

                for (Element genere : generes) {
                    arrayList_generes.add(genere.text());
                }

                for (Element url_titles : urls) {
                    String allUrls_final = url_titles.attr("href");
                    if (allUrls_final.contains("anime.php?id")) {
                        arrayList_urls.add(allUrls_final);
                    }

                }
                JsonObject object = new JsonObject();
                JsonArray jsonArray = new JsonArray();
                for (int i = 0; i < arrayList_titles.size(); i++) {

                    JsonObject object1 = new JsonObject();
                        object1.addProperty("Titolo",arrayList_titles.get(i));
                        object1.addProperty("Url",arrayList_urls.get(i));
                        object1.addProperty("Trama",arrayList_plots.get(i));
                        object1.addProperty("Mangaka",arrayList_mangakas.get(i));
                        jsonArray.add(object1);

                }
                object.add("Anime",jsonArray);

                //Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Gson gson = new Gson();
                String prettyJson = gson.toJson(object);
                mData.setValue(prettyJson);

                Log.d(LOG,prettyJson);

                Log.d(LOG, String.valueOf(mapTitles_Urls.size()
                + " " + arrayList_plots.size() + " " + arrayList_urls.size() + " " + arrayList_mangakas.size()));


               /* Log.d(LOG, (
                        arrayList_titles.get(i)
                                + arrayList_plots.get(i)
                                + arrayList_mangakas.get(i)
                                + arrayList_urls.get(i)));*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueueFirst.add(request);
    }
}
