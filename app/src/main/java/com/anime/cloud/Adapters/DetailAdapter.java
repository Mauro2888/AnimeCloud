package com.anime.cloud.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anime.cloud.Model.PojoEpisodes;
import com.anime.cloud.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolderMain> {

    private Context context;
    private List<PojoEpisodes> episodesList;
    private OnClickInterface onClickInterface;

    public void setOnClickInterface(OnClickInterface onClickInterface) {
        this.onClickInterface = onClickInterface;
    }

    public DetailAdapter(Context context, List<PojoEpisodes> episodesList) {
        this.context = context;
        this.episodesList = episodesList;
    }

    @NonNull
    @Override
    public ViewHolderMain onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_episodes_detail, parent, false);
        ViewHolderMain holderMain = new ViewHolderMain(root);
        return holderMain;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMain holder, int position) {

        PojoEpisodes pojoEpisodes = episodesList.get(position);
        holder.episodeNumber.setText(pojoEpisodes.getEpisode());

    }

    @Override
    public int getItemCount() {
        return episodesList.size();
    }

    public class ViewHolderMain extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView episodeNumber;

        private ViewHolderMain(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            episodeNumber = itemView.findViewById(R.id.episode_number);
        }

        @Override
        public void onClick(View v) {
            if (v != null){
                onClickInterface.onClickItem(v,getAdapterPosition());
            }
        }
    }
}
