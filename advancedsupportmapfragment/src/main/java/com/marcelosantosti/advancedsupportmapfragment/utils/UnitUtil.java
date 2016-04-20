package com.marcelosantosti.advancedsupportmapfragment.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by mlsantos on 20/04/2016.
 */
public class UnitUtil {

    public static int convertToDp(Context context, int value) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }
}
