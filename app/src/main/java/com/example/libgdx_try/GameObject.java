package com.example.libgdx_try;

import android.graphics.Canvas;
import android.graphics.PointF;

public abstract class GameObject {
    protected PointF position;
    protected PointF velocity = new PointF(0, 0);
    protected PointF acceleration = new PointF(0, 0);

    public GameObject(PointF position) {
        this.position = position;
    }

    public abstract void draw(Canvas canvas);

    public abstract void update();

    public PointF getPosition() {
        return position;
    }

    public void setPosition(PointF center) {
        this.position = center;
    }

    public PointF getVelocity() {
        return velocity;
    }

    public void setVelocity(PointF velocity) {
        this.velocity = velocity;
    }

    public PointF getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(PointF acceleration) {
        this.acceleration = acceleration;
    }
}
