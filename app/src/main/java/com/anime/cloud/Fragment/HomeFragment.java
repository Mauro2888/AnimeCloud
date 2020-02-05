package com.anime.cloud.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anime.cloud.Adapters.MainAdapter;
import com.anime.cloud.Adapters.OnClickInterface;
import com.anime.cloud.PojoAnime;
import com.anime.cloud.R;
import com.anime.cloud.Utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements OnClickInterface {
    private RecyclerView mRecyclerView;
    private Context mContext;
    private ArrayList<PojoAnime> mCollectionsAnimeHome = new ArrayList<>();
    private MainAdapter mAdapter;
    private ProgressBar mProgressMain;
    RecyclerView.LayoutManager mLayoutManager;

    public HomeFragment(){}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        hideSystemUI();
        setupComponentUI(view);
        httpConnection();

        return view;
    }

    private void setupComponentUI(final View view) {

        mRecyclerView = view.findViewById(R.id.recycler_view_main);
        mProgressMain = view.findViewById(R.id.progress_bar_main);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mLayoutManager = new GridLayoutManager(getContext(), Utils.calculateNoOfColumns(getContext()));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mProgressMain.setVisibility(View.VISIBLE);

    }

    private void httpConnection() {
        RequestQueue requestQueueFirst = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, Utils.BASE_ENDPOINT,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
        mAdapter = new MainAdapter(getContext(), mCollectionsAnimeHome);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnClickInterface(this);
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

    @Override
    public void onClickItem(View view, int pos) {

        FragmentDetail fragmentDetail = new FragmentDetail();

        Bundle bundle = new Bundle();
        bundle.putSerializable("DetailAnime", mCollectionsAnimeHome.get(pos));
        fragmentDetail.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,fragmentDetail)
                .addToBackStack(null)
                .commit();
    }
}
