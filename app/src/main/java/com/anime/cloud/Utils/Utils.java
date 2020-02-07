package com.anime.cloud.Utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class Utils {

    public static final String BASE_ENDPOINT = "https://animeunity.it/index.php/";
    public static final String BASE_ENDPOINT_DETAIL = "https://animeunity.it/";
    public static final String BASE_ENDPOINT_ONAIR = "https://animeunity.it/anime.php?c=onair";
    public static final String BASE_ENDPOINT_ARCHIVE = "https://animeunity.it/anime.php?c=archive&page=*";
    public static final String CSS_TITLE_URL_ARCHIVE = "a,h6.card-title";
    public static final String CSS_IMG_URL_ARCHIVE = "card-img.archive-card-img";
    //public static final String CSS_TITLE_URL_ARCHIVE = "div.card-img-top.archive-card-img";

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns >= 2 ? noOfColumns : 2;
    }
}
