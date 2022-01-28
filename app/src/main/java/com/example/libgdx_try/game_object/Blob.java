package com.example.libgdx_try.game_object;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.example.libgdx_try.GameLoop;
import com.example.libgdx_try.graphics.Sprite;

public class Blob extends GameObject {
    protected static final float FRICTION = .05f;
    protected static final float MAX_ACCELERATION_IN_SECONDS = 25;
    public static final float MAX_ACCELERATION = MAX_ACCELERATION_IN_SECONDS / GameLoop.MAX_UPS;
    // The blob's max velocity is ( (MAX_ACCELERATION_IN_SECONDS / FRICTION) + MAX_ACCELERATION_IN_SECONDS )

    protected float radius;
    protected Paint paint;
    protected Sprite sprite;

    public Blob(PointF position, float radius, int color, Sprite sprite) {
        super(position);

        this.radius = radius;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);

        this.sprite = sprite;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(position.x, position.y, radius, paint);
    }

    @Override
    public void update() {
        velocity.offset(acceleration.x, acceleration.y);
        position.offset(velocity.x, velocity.y);
        velocity.set(velocity.x * (1 - FRICTION), velocity.y * (1 - FRICTION));
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getColor() {
        return paint.getColor();
    }

    public void setColor(int color) {
        paint.setColor(color);
    }
}
