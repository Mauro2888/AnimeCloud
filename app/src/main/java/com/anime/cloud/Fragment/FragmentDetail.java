package com.anime.cloud.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anime.cloud.Adapters.DetailAdapter;
import com.anime.cloud.Adapters.OnClickInterface;
import com.anime.cloud.Adapters.PojoEpisodes;
import com.anime.cloud.PlayActivity;
import com.anime.cloud.PojoAnime;
import com.anime.cloud.R;
import com.anime.cloud.Utils.Utils;
import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class FragmentDetail extends Fragment implements OnClickInterface {

    private Context mContext;
    private PojoAnime pojoAnime;
    private ImageView mImageDetail;
    private TextView mTitle,mGenere,mTrama,mStato;
    private DetailAdapter mDetailAdapter;
    private ProgressBar mProgressDetail;
    private RecyclerView mRecyclerDetailEpisodes;
    private ArrayList<PojoEpisodes> mEpisodesArrayList = new ArrayList<>();

    public FragmentDetail(){}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail,container,false);
        hideSystemUI();

        Bundle intentMain = getArguments();
        pojoAnime = (PojoAnime) intentMain.getSerializable("DetailAnime");

        mProgressDetail = view.findViewById(R.id.progress_bar_detail);
        mProgressDetail.setVisibility(View.VISIBLE);

        mTitle = view.findViewById(R.id.title_detail);
        mGenere = view.findViewById(R.id.genere_detail);
        mStato = view.findViewById(R.id.stato_detail);
        mTrama = view.findViewById(R.id.trama_detail);

        //get data from intent
        String title = pojoAnime.getTitleAnime();
        final String url = pojoAnime.getUrlAnimePage();
        String image = pojoAnime.getUrlImg();

        mTitle.setText(title);
        mRecyclerDetailEpisodes = view.findViewById(R.id.recycler_view_detail);
        mRecyclerDetailEpisodes.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerDetailEpisodes.setHasFixedSize(true);
        setupAdapter();

        mImageDetail = view.findViewById(R.id.anime_image_detail);
        Glide.with(getContext()).asBitmap().centerCrop().load(image).into(mImageDetail);

        if (url != null){
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest =
                    new StringRequest(Request.Method.GET, Utils.BASE_ENDPOINT_DETAIL + url,
                            new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    scrapingSite(response);
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "Risorsa non disponibile", Toast.LENGTH_SHORT).show();
                        }
                    });

            requestQueue.add(stringRequest);
        }

        return view;
    }

    protected void scrapingSite(String response) {
        Document document = Jsoup.parse(response);

        Elements infoAnime = document.select(".card-body.bg-light-gray");

        Element trama = infoAnime.select("p:contains(TRAMA)").first();
        Element stato = infoAnime.select("p:contains(STATO)").first();
        Element genere = infoAnime.select("p:contains(GENERI)").first();

        Elements titles = document.select("a");
        for (Element title : titles) {
            String all = title.attr("href");
            if (all.contains("&ep") && all.contains("anime.php")) {
                mEpisodesArrayList.add(new PojoEpisodes(title.text(),all));
            }

        }

        setupAdapter();

        //setupComponents
        mGenere.setText(genere.text());
        mStato.setText(stato.text());
        mTrama.setText(trama.text());

    }

    private void setupAdapter() {
        mDetailAdapter = new DetailAdapter(getContext(), mEpisodesArrayList);
        mRecyclerDetailEpisodes.setAdapter(mDetailAdapter);
        mDetailAdapter.notifyDataSetChanged();
        mDetailAdapter.setOnClickInterface(this);
        mProgressDetail.setVisibility(View.GONE);
    }

    @Override
    public void onClickItem(View view, int pos) {
        RequestQueue requestQueueUrl = Volley.newRequestQueue(getContext());
        StringRequest requestVideo = new StringRequest(Request.Method.GET,
                Utils.BASE_ENDPOINT_DETAIL + mEpisodesArrayList.get(pos).getUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Document document = Jsoup.parse(response);
                        Elements basic = document.select("source");
                        for (Element i: basic) {
                            Intent sendUrl = new Intent(getContext(), PlayActivity.class);
                            sendUrl.putExtra("url",i.attr("src"));
                            startActivity(sendUrl);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueueUrl.add(requestVideo);
    }

    protected void hideSystemUI() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            getActivity().getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    );
        }
    }
}
