package com.example.libgdx_try.game_object;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class CircleObject extends GameObject {
    protected float radius;
    protected Paint paint;

    public CircleObject(PointF position) {
        super(position);
    }

    public CircleObject(PointF position, float radius) {
        super(position);

        this.radius = radius;
    }

    public CircleObject(PointF position, float radius, Paint paint) {
        super(position);

        this.paint = paint;
        this.radius = radius;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(position.x, position.y, radius, paint);
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public int getColor() {
        return paint.getColor();
    }

    public void setColor(int color) {
        paint.setColor(color);
    }
}
