package com.example.libgdx_try.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.libgdx_try.R;

public class CoronaSpriteSheet {

	protected static final int spriteWidth = 64;
	protected static final int spriteHeight = 64;
	protected Bitmap bitmap;

	public CoronaSpriteSheet(Context context) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;
		bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_sheet, options);
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public Sprite getSpriteByIndex(int rowIndex, int columnIndex) {
		return new Sprite(getBitmap(), new Rect(
			columnIndex * spriteWidth,
			rowIndex * spriteHeight,
			(columnIndex + 1) * spriteWidth,
			(rowIndex + 1) * spriteHeight
		));
	}

}
