package com.anime.cloud.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.anime.cloud.Adapters.DetailAdapter;
import com.anime.cloud.Adapters.OnClickInterface;
import com.anime.cloud.Model.Anime;
import com.anime.cloud.Model.Episodi;
import com.anime.cloud.PlayActivity;
import com.anime.cloud.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FragmentDetail extends Fragment implements OnClickInterface, View.OnClickListener {

    private static final String LOG = FragmentDetail.class.getSimpleName();
    private Context mContext;
    private Anime animePojo;
    private ImageView mImageDetail;
    private TextView mTitle,mGenere,mTrama,mStato;
    private DetailAdapter mDetailAdapter;
    private ProgressBar mProgressDetail;
    private RecyclerView mRecyclerDetailEpisodes;
    private ArrayList<String> mEpisodesArrayList = new ArrayList<>();
    private boolean isClickedTextView = false;
    int id;
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
        animePojo = (Anime) intentMain.getSerializable("DetailAnime_inCorso");


        setupUIComponents(view);
        getArgumentsFromFragment(animePojo,view);
        mRecyclerDetailEpisodes = view.findViewById(R.id.recycler_view_detail);
        mRecyclerDetailEpisodes.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerDetailEpisodes.setHasFixedSize(true);
        setupAdapter();

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

    private void getArgumentsFromFragment(final Anime animePojo, View view){
        id = animePojo.getId();
        String title = animePojo.getTitleAnime();
        String image = animePojo.getUrlImage();
        String genere = animePojo.getMangaka();
        String trama = animePojo.getPlotAnime();
        List<Episodi> episodi = animePojo.getUrlVideo();
        for (Episodi i : episodi){
            if (id == i.getIdAnime()){
                mEpisodesArrayList.add(i.getLink());
            }

        }

        mTitle.setText(title);
        mGenere.setText(genere);
        mTrama.setText(trama);

        mImageDetail = view.findViewById(R.id.anime_image_detail);
        Glide.with(getContext()).asBitmap().fitCenter().load(image).into(mImageDetail);
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
        Intent sendUrl = new Intent(getContext(), PlayActivity.class);
        sendUrl.putExtra("url",mEpisodesArrayList.get(pos));
        startActivity(sendUrl);
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
