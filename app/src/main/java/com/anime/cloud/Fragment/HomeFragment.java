package com.anime.cloud.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anime.cloud.Adapters.InCorsoAdapter;
import com.anime.cloud.Adapters.OnClickInterface;
import com.anime.cloud.Adapters.UltimiAggiuntiAdapter;
import com.anime.cloud.Model.PojoAnime;
import com.anime.cloud.R;
import com.anime.cloud.Utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements OnClickInterface {
    private RecyclerView mRecyclerViewInCorso;
    private RecyclerView mRecyclerViewUltimi;
    private Context mContext;
    private ArrayList<PojoAnime> mAnimeInCorso;
    private ArrayList<PojoAnime> mAnimeUltimi;
    private InCorsoAdapter mAdapterInCorso;
    private UltimiAggiuntiAdapter mAdapterUltimi;
    private ProgressBar mProgressMain;

    public HomeFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setupComponentUI(view);
        httpConnection();

        return view;
    }

    private void setupComponentUI(final View view) {

        mRecyclerViewInCorso = view.findViewById(R.id.recycler_view_main);
        mRecyclerViewUltimi = view.findViewById(R.id.recycler_view_ultimi);
        mProgressMain = view.findViewById(R.id.progress_bar_main);

        mRecyclerViewInCorso.setHasFixedSize(true);
        mRecyclerViewInCorso.setItemViewCacheSize(20);
        mRecyclerViewInCorso.setDrawingCacheEnabled(true);
        mRecyclerViewInCorso.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerViewUltimi.setHasFixedSize(true);
        mRecyclerViewUltimi.setItemViewCacheSize(20);
        mRecyclerViewUltimi.setDrawingCacheEnabled(true);
        mRecyclerViewUltimi.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


        mProgressMain.setVisibility(View.VISIBLE);

    }

    private void httpConnection() {

        RequestQueue requestQueueFirst = Volley.newRequestQueue(getContext());
        mAnimeInCorso = new ArrayList<>();
        mAnimeInCorso.clear();

        mAnimeUltimi = new ArrayList<>();
        mAnimeInCorso.clear();
        StringRequest request = new StringRequest(Request.Method.GET, Utils.BASE_ENDPOINT_ONAIR,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //ScrapData
                        Document document = Jsoup.parse(response);

                        Elements titles = document.select(".col-md-7.col-sm-7.archive-col>.card-block>.card-title>b");
                        Elements imgs = document.select(".card-img-top.archive-card-img>a>img");
                        Elements urls = document.select(".card-img-top.archive-card-img>a");

                        Elements titles_u = document.select("a.text-white.mb-0.mt-0.mb-0.mt-0");
                        Elements imgs_u = document.select(".card-img-top.archive-card-img>a>img");
                        Elements urls_u = document.select(".card-img-top.archive-card-img>a");

                        for (int i = 0, t = 0, p = 0; i < titles.size() && t < imgs.size() && p < urls.size(); i++, t++, p++) {
                            mAnimeInCorso.add(new PojoAnime(
                                    titles.get(i).text()
                                    , imgs.get(t).attr("abs:src")
                                    , urls.get(p).attr("href")));
                        }

                        for (int i = 0, t = 0, p = 0; i < titles.size() && t < imgs.size() && p < urls.size(); i++, t++, p++) {
                            mAnimeUltimi.add(new PojoAnime(
                                    titles.get(i).text()
                                    , imgs.get(t).attr("abs:src")
                                    , urls.get(p).attr("href")));
                        }

                        updateAdapter();
                        mProgressMain.setVisibility(View.GONE);

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Risorsa non disponibile", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueueFirst.add(request);
    }

    private void updateAdapter() {
        mAdapterInCorso = new InCorsoAdapter(getContext(), mAnimeInCorso);
        mRecyclerViewInCorso.setAdapter(mAdapterInCorso);

        mAdapterUltimi = new UltimiAggiuntiAdapter(getContext(), mAnimeUltimi);
        mRecyclerViewUltimi.setAdapter(mAdapterUltimi);

        mAdapterInCorso.notifyDataSetChanged();
        mAdapterUltimi.notifyDataSetChanged();
        mAdapterInCorso.setOnClickInterface(this);
        mAdapterUltimi.setOnClickInterface(this);
    }


    protected void hideSystemUI() {

        getActivity().getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                );
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUI();
    }

    @Override
    public void onStart() {
        super.onStart();
        hideSystemUI();
    }


    @Override
    public void onClickItem(View view, int pos) {

        FragmentDetail fragmentDetail = new FragmentDetail();

        Bundle bundle = new Bundle();
        bundle.putSerializable("DetailAnime", mAnimeInCorso.get(pos));
        fragmentDetail.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragmentDetail)
                .addToBackStack(null)
                .commit();
    }

}
