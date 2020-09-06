package com.anime.cloud.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anime.cloud.R;

import java.util.ArrayList;
import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolderMain> {

    private Context context;
    private List<String> episodesList;
    private OnClickInterface onClickInterface;

    public void setOnClickInterface(OnClickInterface onClickInterface) {
        this.onClickInterface = onClickInterface;
    }

    public DetailAdapter(Context context, ArrayList<String> episodesList) {
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

        String pojoEpisodes = episodesList.get(position);
        holder.episodeNumber.setText(pojoEpisodes);

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
