package com.anime.cloud.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anime.cloud.Model.PojoAnime;
import com.anime.cloud.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InCorsoAdapter extends RecyclerView.Adapter<InCorsoAdapter.ViewHolderMain> {

    private Context context;
    private List<PojoAnime> animeList;
    private OnClickInterface onClickInterface;
    private int mLimit = 15;

    public void setOnClickInterface(OnClickInterface onClickInterface) {
        this.onClickInterface = onClickInterface;
    }

    public InCorsoAdapter(Context context, List<PojoAnime> animeList) {
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

        PojoAnime pojoAnime = animeList.get(position);
        holder.title.setText(pojoAnime.getTitleAnime());

        RequestOptions glideOp = new RequestOptions()
                .fitCenter();

        Glide.with(context)
                .asBitmap()
                .apply(glideOp)
                .load(pojoAnime.getUrlImg())
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
