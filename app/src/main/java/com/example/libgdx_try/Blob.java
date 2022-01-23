package com.example.libgdx_try;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Blob extends GameObject {
    static final float MAX_VELOCITY_PIXELS_PER_SECOND = 500;
    static final float MAX_ACCELERATION_PIXELS_PER_SECOND_SQR = 30;

    static final float MAX_VELOCITY = MAX_VELOCITY_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    static final float MAX_ACCELERATION = MAX_ACCELERATION_PIXELS_PER_SECOND_SQR / GameLoop.MAX_UPS;
    public final double MAX_VELOCITY_SQR = Math.pow(MAX_VELOCITY, 2);
    float radius;
    // DAMPING - Less then both (MAX_VELOCITY_PIXELS_PER_SECOND / MAX_ACCELERATION_PIXELS_PER_SECOND_SQR) and (GameLoop.MAX_UPS)
    static final float DAMPING = 3f;
    static final float FRICTION = DAMPING / GameLoop.MAX_UPS;
    Paint paint;

    public Blob(PointF position, float radius, int color) {
        super(position);

        this.radius = radius;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(position.x, position.y, radius, paint);
    }

    public void update() {
        velocity.offset(acceleration.x, acceleration.y);
        double velocitySqr = Math.pow(velocity.x, 2) + Math.pow(velocity.y, 2);
        if (velocitySqr > MAX_VELOCITY_SQR) {
            float normalizeFactor = MAX_VELOCITY / (float) (Math.sqrt(velocitySqr));
            velocity.set(velocity.x * normalizeFactor, velocity.y * normalizeFactor);
        }
        position.offset(velocity.x, velocity.y);
        velocity = Utils.lerp(velocity, new PointF(0, 0), FRICTION);
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
