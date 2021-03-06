package com.example.blob_game.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.DisplayMetrics;

public class Utils {

	public static float convertDpToPixel(float dp, Context context) {
		return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
	}

	public static PointF lerp(PointF p1, PointF p2, float t) {
		return new PointF(p1.x + (p2.x - p1.x) * t, p1.y + (p2.y - p1.y) * t);
	}

	public static float lerp(float num1, float num2, float t) {
		return num1 + (num2 - num1) * t;
	}
}
