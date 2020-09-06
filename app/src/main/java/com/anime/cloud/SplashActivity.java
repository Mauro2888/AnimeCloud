package com.anime.cloud;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashActivity extends AppCompatActivity {

    private FirebaseDatabase reference = FirebaseDatabase.getInstance();
    private DatabaseReference mData;

    private static final String LOG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen splashScreen = new EasySplashScreen(SplashActivity.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(1000)
                .withBackgroundColor(Color.parseColor("#000000"))
                .withLogo(R.drawable.icon_anime);

        View view = splashScreen.create();
        setContentView(view);
        //downloadArchive();
        hideSystemUI();
    }

    protected void hideSystemUI() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    );
        }
    }

   /* private void downloadArchive() {
        //mData = reference.getReference();
        DatabaseReference uidRef = mData.child("Anime");
        uidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String titoli = postSnapshot.child("Titolo").getValue(String.class);
                    String url_anime = postSnapshot.child("Url").getValue(String.class);
                    String imgs = postSnapshot.child("Img_url").getValue(String.class);
                    String generi = postSnapshot.child("Genere").getValue(String.class);
                    String mangakas = postSnapshot.child("Mangaka").getValue(String.class);
                    String plots = postSnapshot.child("Trama").getValue(String.class);
                    Log.d(LOG,titoli +" " + url_anime);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }*/
}
