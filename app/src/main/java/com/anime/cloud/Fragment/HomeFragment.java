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
import com.anime.cloud.Utils.FireBaseSing;
import com.anime.cloud.Utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private FirebaseDatabase mDataBase;
    private DatabaseReference mRef;

    public HomeFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        FireBaseSing.getDatabase();
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

        mDataBase = FirebaseDatabase.getInstance();
        mRef = mDataBase.getReference();
        DatabaseReference anime_ref = mRef.child("Anime");
        anime_ref.keepSynced(true);
        mAnimeInCorso = new ArrayList<>();
        mAnimeUltimi = new ArrayList<>();
        anime_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String titoli = postSnapshot.child("Titolo").getValue(String.class);
                    String url_anime = postSnapshot.child("Url").getValue(String.class);
                    String imgs = postSnapshot.child("Img_url").getValue(String.class);
                    String generi = postSnapshot.child("Genere").getValue(String.class);
                    String mangakas = postSnapshot.child("Mangaka").getValue(String.class);
                    String plots = postSnapshot.child("Trama").getValue(String.class);

                    mAnimeInCorso.add(new PojoAnime(titoli,imgs,url_anime,generi,plots));
                    mAnimeUltimi.add(new PojoAnime(titoli,imgs,url_anime,generi,plots));
                    updateAdapter();
                    mProgressMain.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
