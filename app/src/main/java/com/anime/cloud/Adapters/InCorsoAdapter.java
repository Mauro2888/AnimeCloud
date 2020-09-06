package com.anime.cloud.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anime.cloud.Model.Anime;
import com.anime.cloud.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class InCorsoAdapter extends RecyclerView.Adapter<InCorsoAdapter.ViewHolderMain> {

    private Context context;
    private List<Anime> animeList;
    private OnClickInterface onClickInterface;
    private int mLimit = 20;

    public void setOnClickInterface(OnClickInterface onClickInterface) {
        this.onClickInterface = onClickInterface;
    }

    public InCorsoAdapter(Context context, List<Anime> animeList) {
        this.context = context;
        this.animeList = animeList;
    }

    @NonNull
    @Override
    public ViewHolderMain onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_in_corso, parent, false);
        ViewHolderMain holderMain = new ViewHolderMain(root);
        return holderMain;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMain holder, int position) {

        Anime animePojo = animeList.get(position);
        holder.title.setText(animePojo.getTitleAnime());

        RequestOptions glideOp = new RequestOptions()
                .fitCenter();

        Glide.with(context)
                .asBitmap()
                .apply(glideOp)
                .load(animePojo.getUrlImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        if (animeList.size() > mLimit){
            return mLimit;
        }else {
            return animeList.size();
        }

    }

    public class ViewHolderMain extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView imageView;

        public ViewHolderMain(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title_anime);
            imageView = itemView.findViewById(R.id.image_anime);
        }

        @Override
        public void onClick(View v) {
            if (v != null){
                onClickInterface.onClickItem(v,getAdapterPosition());
            }
        }
    }
}
