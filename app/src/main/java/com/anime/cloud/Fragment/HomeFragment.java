package com.anime.cloud.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.anime.cloud.Adapters.InCorsoAdapter;
import com.anime.cloud.Adapters.OnClickInterface;
import com.anime.cloud.Model.Anime;
import com.anime.cloud.Model.Episodi;
import com.anime.cloud.R;
import com.anime.cloud.Utils.FireBaseSing;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnClickInterface {
    private RecyclerView mRecyclerViewInCorso;
    private Context mContext;
    private ArrayList<Anime> mAnimeInCorso;
    private InCorsoAdapter mAdapterInCorso;
    private ProgressBar mProgressMain;
    private FirebaseDatabase mDataBase;
    private DatabaseReference mRef;
    private List<Episodi>mEpisodi;

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

        mRecyclerViewInCorso = view.findViewById(R.id.recycler_view_home);
        mProgressMain = view.findViewById(R.id.progress_bar_main);

        mRecyclerViewInCorso.setHasFixedSize(true);
        mRecyclerViewInCorso.setItemViewCacheSize(20);
        mRecyclerViewInCorso.setDrawingCacheEnabled(true);
        mRecyclerViewInCorso.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mProgressMain.setVisibility(View.VISIBLE);

    }

    private void httpConnection() {

        mDataBase = FirebaseDatabase.getInstance();
        mRef = mDataBase.getReference().child("Anime");
        mRef.keepSynced(true);
        mAnimeInCorso = new ArrayList<>();
        mEpisodi = new ArrayList<>();

        mRef.limitToFirst(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    int id = postSnapshot.child("id").getValue(Integer.class);
                    String titoli = postSnapshot.child("titleAnime").getValue(String.class);
                    String imgs = postSnapshot.child("urlImage").getValue(String.class);
                    String mangaka = postSnapshot.child("mangaka").getValue(String.class);
                    String plots = postSnapshot.child("plotAnime").getValue(String.class);
                        for (DataSnapshot snapshot : postSnapshot.child("urlVideo").getChildren()){
                            long ids = snapshot.child("anime_id").getValue(long.class);
                            String url = snapshot.child("link").getValue(String.class);
                            String episodeNumber = snapshot.child("number").getValue(String.class);
                            mEpisodi.add(new Episodi(ids,url,episodeNumber));
                    }


                    mAnimeInCorso.add(new Anime(id,titoli,imgs,mangaka,plots,mEpisodi));
                    updateAdapter();
                    mProgressMain.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext, "Errore", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateAdapter() {
        mAdapterInCorso = new InCorsoAdapter(getContext(), mAnimeInCorso);
        mRecyclerViewInCorso.setAdapter(mAdapterInCorso);
        mAdapterInCorso.notifyDataSetChanged();
        mAdapterInCorso.setOnClickInterface(this);

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
        bundle.putSerializable("DetailAnime_inCorso", mAnimeInCorso.get(pos));
        fragmentDetail.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragmentDetail)
                .addToBackStack(null)
                .commit();
    }
}
