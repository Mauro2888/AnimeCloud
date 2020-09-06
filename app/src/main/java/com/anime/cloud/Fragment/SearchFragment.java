package com.anime.cloud.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anime.cloud.R;
import com.anime.cloud.Utils.FireBaseSing;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private AutoCompleteTextView mSearch;
    private Context mContext;
    private ArrayAdapter mArrayAdapterTitles;
    private ArrayList<String> mTitles;

    public SearchFragment() {
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mTitles = new ArrayList<>();
        mArrayAdapterTitles = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, mTitles);
        mSearch = view.findViewById(R.id.search_anime);
        FirebaseDatabase mDataBase = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mDataBase.getReference();
        DatabaseReference anime_ref = mRef.child("Anime");
        anime_ref.keepSynced(true);
        anime_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String titoli = postSnapshot.child("titleAnime").getValue(String.class);
                    String url_anime = postSnapshot.child("Url").getValue(String.class);
                    String imgs = postSnapshot.child("Img_url").getValue(String.class);
                    String generi = postSnapshot.child("Genere").getValue(String.class);
                    String mangakas = postSnapshot.child("Mangaka").getValue(String.class);
                    String plots = postSnapshot.child("Trama").getValue(String.class);
                    mTitles.add(titoli);
                    mArrayAdapterTitles.notifyDataSetChanged();
                    mSearch.setAdapter(mArrayAdapterTitles);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext, "Errore", Toast.LENGTH_SHORT).show();
            }
        });

        mSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String data = (String) adapterView.getItemAtPosition(i);
                Toast.makeText(mContext, "" + data, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
