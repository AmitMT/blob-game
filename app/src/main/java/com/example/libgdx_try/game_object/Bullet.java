package com.example.libgdx_try.game_object;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Bullet extends CircleObject {

    long timeSinceCreation;
    int ttl;
    boolean dying;
    float disintegrationSpeed = 0.1f; // slow - 0 < x < 1 - fast

    public Bullet(PointF position, PointF velocity, float radius, int ttl, Paint paint) {
        super(position, radius, paint);

        timeSinceCreation = System.currentTimeMillis();
        this.velocity = velocity;
        this.ttl = ttl;
    }

    @Override
    public void update() {
        if (!active) return;
        super.update();

        if (System.currentTimeMillis() > timeSinceCreation + ttl) dying = true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (dying) {
            if (paint.getAlpha() <= 255 * disintegrationSpeed) active = false;
            else paint.setAlpha(paint.getAlpha() - (int)(255 * disintegrationSpeed));
        }

        super.draw(canvas);
    }
}