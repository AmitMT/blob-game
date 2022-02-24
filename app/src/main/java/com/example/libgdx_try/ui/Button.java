package com.example.libgdx_try.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.example.libgdx_try.R;
import com.example.libgdx_try.utils.Utils;

public class Button extends androidx.appcompat.widget.AppCompatButton {

	public Button(Context context) {
		super(context, null);
	}

	public Button(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);

		setBackground(ContextCompat.getDrawable(context, R.drawable.button_background));
		setAllCaps(false);
		setTextColor(ContextCompat.getColor(context, R.color.title));
		setPadding(
			(int) Utils.convertDpToPixel(15, context),
			(int) Utils.convertDpToPixel(15, context),
			(int) Utils.convertDpToPixel(15, context),
			(int) Utils.convertDpToPixel(15, context)
		);
	}
}
