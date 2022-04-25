package com.example.libgdx_try.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import androidx.annotation.NonNull;

public class Sprite implements Cloneable {

	Bitmap bitmap;
	Rect rect;

	public Sprite(Bitmap bitmap, Rect rect) {
		this.bitmap = bitmap;
		this.rect = rect;
	}

	public void draw(Canvas canvas, Rect rect) {
		canvas.drawBitmap(bitmap, this.rect, rect, null);
	}

	public int getWidth() {
		return rect.width();
	}

	public int getHeight() {
		return rect.height();
	}

	@NonNull
	@Override
	public Object clone() throws CloneNotSupportedException {
		Sprite cloned = (Sprite) super.clone();
		if (cloned.bitmap != null)
			cloned.bitmap = cloned.bitmap.copy(cloned.bitmap.getConfig(), true);
		if (cloned.rect != null) cloned.rect = new Rect(cloned.rect);
		return cloned;
	}
}
