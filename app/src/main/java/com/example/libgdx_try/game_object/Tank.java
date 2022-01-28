package com.example.libgdx_try.game_object;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.libgdx_try.graphics.Sprite;

public class Tank extends Blob {

    Point barrelSize = new Point(80, 30);
    float angle = 30; // 0 - 360
    Paint barrelPaint;

    public Tank(PointF position, float radius, int color, Sprite sprite) {
        super(position, radius, color, sprite);

        barrelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barrelPaint.setColor(Color.parseColor("#cccccc"));
    }

    public Tank(PointF position, float radius, int color, Sprite sprite, Point barrelSize) {
        this(position, radius, color, sprite);

        this.barrelSize = barrelSize;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.rotate(angle, position.x, position.y);
        canvas.drawRect(position.x, position.y - (float) barrelSize.y / 2, position.x + barrelSize.x, position.y + (float) barrelSize.y / 2, barrelPaint);
        canvas.rotate(-angle, position.x, position.y);

        super.draw(canvas);
    }

    @Override
    public void update() {
        super.update();

        angle++;
    }
}
