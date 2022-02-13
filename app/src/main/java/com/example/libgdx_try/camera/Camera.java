package com.example.libgdx_try.camera;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.example.libgdx_try.utils.Utils;

public class Camera {

    PointF position;
    float scaleFactor = 4f;
    boolean smoothFollow = true;
    float followRoughness = 0.05f; // 0 - 1

    PointF lastFramePosition = new PointF();

    public Camera(PointF position) {
        this.position = position;
    }

    public Camera(PointF position, Options options) {
        this.position = position;

        smoothFollow = options.smoothFollow;
        followRoughness = options.followRoughness;
    }

    public void transformCanvas(Canvas canvas, PointF playerPosition) {
        lastFramePosition.set(position);
        canvas.translate(lastFramePosition.x, lastFramePosition.y);
        canvas.scale(scaleFactor, scaleFactor, playerPosition.x, playerPosition.y);
    }

    public void revertCanvas(Canvas canvas, PointF playerPosition) {
        float scaleFactor = 1f / this.scaleFactor;
        canvas.scale(scaleFactor, scaleFactor, playerPosition.x, playerPosition.y);
        canvas.translate(-lastFramePosition.x, -lastFramePosition.y);
    }

    public PointF getPosition() {
        return position;
    }

    public void setPosition(PointF position) {
        if (smoothFollow)
            this.position = Utils.lerp(this.position, position, followRoughness);
        else this.position = position;
    }

    public static class Options {

        boolean smoothFollow = true;
        float followRoughness = 0.05f;

        public Options() {
        }

        public boolean isSmoothFollow() {
            return smoothFollow;
        }

        public Options setSmoothFollow(boolean smoothFollow) {
            this.smoothFollow = smoothFollow;
            return this;
        }

        public float getFollowRoughness() {
            return followRoughness;
        }

        public Options setFollowRoughness(float followRoughness) {
            this.followRoughness = followRoughness;
            return this;
        }
    }
}
