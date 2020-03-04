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
import com.anime.cloud.Model.PojoEpisodes;
import com.anime.cloud.PlayActivity;
import com.anime.cloud.Model.PojoAnime;
import com.anime.cloud.R;
import com.anime.cloud.Utils.Utils;
import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class FragmentDetail extends Fragment implements OnClickInterface, View.OnClickListener {

    private static final String LOG = FragmentDetail.class.getSimpleName();
    private Context mContext;
    private PojoAnime pojoAnime;
    private ImageView mImageDetail;
    private TextView mTitle,mGenere,mTrama,mStato;
    private DetailAdapter mDetailAdapter;
    private ProgressBar mProgressDetail;
    private RecyclerView mRecyclerDetailEpisodes;
    private ArrayList<PojoEpisodes> mEpisodesArrayList = new ArrayList<>();
    private boolean isClickedTextView = false;

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
        if (intentMain != null && intentMain.containsKey("DetailAnime_all")){
            pojoAnime = (PojoAnime) intentMain.getSerializable("DetailAnime_all");
        }else {
            pojoAnime = (PojoAnime) intentMain.getSerializable("DetailAnime_inCorso");
        }

        setupUIComponents(view);
        getArgumentsFromFragment(pojoAnime,view);

        mRecyclerDetailEpisodes = view.findViewById(R.id.recycler_view_detail);
        mRecyclerDetailEpisodes.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerDetailEpisodes.setHasFixedSize(true);

        return view;
    }

    private void setupUIComponents(View view){
        mProgressDetail = view.findViewById(R.id.progress_bar_detail);
        mProgressDetail.setVisibility(View.VISIBLE);
        mTitle = view.findViewById(R.id.title_detail);
        mGenere = view.findViewById(R.id.genere_detail);
        mStato = view.findViewById(R.id.stato_detail);
        mTrama = view.findViewById(R.id.trama_detail);
        mTrama.setOnClickListener(this);
    }

    private void getArgumentsFromFragment(PojoAnime pojoAnime, View view){
        String title = pojoAnime.getTitleAnime();
        final String url = pojoAnime.getUrlAnimePage();
        String image = pojoAnime.getUrlImg();
        String genere = pojoAnime.getGeneri();
        String trama = pojoAnime.getTrama();

        if (url != null){
            fetchEpisodesFromUrl(url);
        }


        mTitle.setText(title);
        mGenere.setText(genere);
        mTrama.setText(trama);

        mImageDetail = view.findViewById(R.id.anime_image_detail);
        Glide.with(getContext()).asBitmap().fitCenter().load(image).into(mImageDetail);
    }

    protected void fetchEpisodesFromUrl(String response) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, Utils.BASE_ENDPOINT_DETAIL + response, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                Elements titles = document.select("a");
                for (Element title : titles) {
                    String all = title.attr("href");
                    if (all.contains("&ep") && all.contains("anime.php")) {
                        Log.d("URL",title.text() + " " + all);
                        mEpisodesArrayList.add(new PojoEpisodes(title.text(),all));
                    }
                    setupAdapter();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(request);
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

    private void hideSystemUI() {

        getActivity().getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                );
    }

    @Override
    public void onClick(View v) {
        if (v == mTrama && isClickedTextView){
            mTrama.setMaxLines(4);
            isClickedTextView = false;
        }else {
            mTrama.setMaxLines(Integer.MAX_VALUE);
            isClickedTextView = true;
        }
    }
}
