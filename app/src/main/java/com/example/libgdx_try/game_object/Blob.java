package com.example.libgdx_try.game_object;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.example.libgdx_try.GameLoop;
import com.example.libgdx_try.graphics.Sprite;

public class Blob extends CircleObject {

    public static final float MAX_ACCELERATION_IN_SECONDS = 25;
    public static final float MAX_ACCELERATION = MAX_ACCELERATION_IN_SECONDS / GameLoop.MAX_UPS;
    public static final float FRICTION = .05f;
    // The blob's max velocity is ( (MAX_ACCELERATION_IN_SECONDS / FRICTION) + MAX_ACCELERATION_IN_SECONDS )

    protected Sprite sprite;

    public Blob(PointF position, float radius, Paint paint, Sprite sprite) {
        super(position, radius, paint);

        this.sprite = sprite;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(position.x, position.y, radius, paint);
    }

    @Override
    public void update() {
        if (!active) return;
        super.update();

        velocity.set(velocity.x * (1 - FRICTION), velocity.y * (1 - FRICTION));
    }
}
