package com.example.libgdx_try.game_object;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;

public abstract class GameObject {
    protected PointF position;
    protected PointF velocity = new PointF(0, 0);
    protected PointF acceleration = new PointF(0, 0);
    protected boolean active = true;

    public GameObject(PointF position) {
        this.position = position;
    }

    public GameObject(PointF position, Options options) {
        this.position = position;
        this.velocity = options.velocity;
        this.acceleration = options.acceleration;
        this.active = options.active;
    }

    public abstract void draw(Canvas canvas);

    public void update() {
        if (!active) return;
        velocity.offset(acceleration.x, acceleration.y);
        position.offset(velocity.x, velocity.y);
    }

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean useTouch(MotionEvent event, int action, int index) {
        return false;
    }

    public static class Options {

        protected PointF velocity = new PointF(0, 0);
        protected PointF acceleration = new PointF(0, 0);
        protected boolean active = true;

        public Options() {
        }

        public PointF getVelocity() {
            return velocity;
        }

        public Options setVelocity(PointF velocity) {
            this.velocity = velocity;
            return this;
        }

        public PointF getAcceleration() {
            return acceleration;
        }

        public Options setAcceleration(PointF acceleration) {
            this.acceleration = acceleration;
            return this;
        }

        public boolean isActive() {
            return active;
        }

        public Options setActive(boolean active) {
            this.active = active;
            return this;
        }
    }
}
