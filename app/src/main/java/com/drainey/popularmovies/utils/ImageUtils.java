package com.drainey.popularmovies.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.drainey.popularmovies.R;

/**
 * Created by david-rainey on 5/21/18.
 */

public class ImageUtils {
    public static Drawable getRedErrorIcon(Context context){
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_error_icon);
        int color = ContextCompat.getColor(context, R.color.dark_red);
        drawable.setColorFilter(color, PorterDuff.Mode.DST);
        return drawable;
    }
}
