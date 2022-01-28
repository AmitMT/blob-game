package com.example.libgdx_try.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {

    final Bitmap bitmap;
    final Rect rect;

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
}
