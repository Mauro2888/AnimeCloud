package com.anime.cloud.Utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class Utils {

    public static final String BASE_ENDPOINT = "https://animeunity.it/index.php/";
    public static final String BASE_ENDPOINT_DETAIL = "https://animeunity.it/";

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns >= 2 ? noOfColumns : 2;
    }
}
