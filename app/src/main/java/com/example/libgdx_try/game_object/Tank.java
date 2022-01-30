package com.example.libgdx_try.game_object;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.libgdx_try.Utils;
import com.example.libgdx_try.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

public class Tank extends Blob {

    Point barrelSize = new Point(80, 30);
    float angle = 0; // 0 - 360
    Paint barrelPaint;
    Matrix barrelRotationMatrix = new Matrix(); // For server optimisation. used instead of "canvas.rotate()"
    Path barrelPath = new Path();

    List<Bullet> bullets = new ArrayList<>();
    int bulletRadius = 10;
    float bulletSpeed = 4;
    int reloadMill = 1000;
    long prevShotTime;
    Paint bulletPaint;

    public Tank(PointF position, float radius, Paint paint, Sprite sprite) {
        super(position, radius, paint, sprite);

        barrelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barrelPaint.setColor(Color.parseColor("#cccccc"));
    }

    public Tank(PointF position, float radius, Paint paint, Sprite sprite, Point barrelSize, int reloadMill) {
        this(position, radius, paint, sprite);

        this.barrelSize = barrelSize;
        this.reloadMill = reloadMill;
        bulletPaint = paint;
    }

    @Override
    public void draw(Canvas canvas) {

        barrelRotationMatrix.setRotate(angle);
        // 2d matrix of the barrel
        float[] points = new float[]{
                0, -barrelSize.y / 2f,
                barrelSize.x, -barrelSize.y / 2f,
                barrelSize.x, +barrelSize.y / 2f,
                0, +barrelSize.y / 2f,
        };
        barrelRotationMatrix.mapPoints(points);

        barrelPath.reset();
        barrelPath.moveTo(points[0] + position.x, points[1] + position.y);
        barrelPath.lineTo(points[2] + position.x, points[3] + position.y);
        barrelPath.lineTo(points[4] + position.x, points[5] + position.y);
        barrelPath.lineTo(points[6] + position.x, points[7] + position.y);

        bullets.forEach(bullet -> bullet.draw(canvas));

        canvas.drawPath(barrelPath, barrelPaint);

        super.draw(canvas);
    }

    @Override
    public void update() {
        if (!active) return;
        super.update();

        List<Bullet> needDeletion = new ArrayList<>();
        bullets.forEach(bullet -> {
            if (bullet.active) bullet.update();
            else needDeletion.add(bullet);
        });
        bullets.removeAll(needDeletion);
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void lerpToAngle(float angle, float speed) {
        if (angle - this.angle > 180)
            this.angle = Utils.lerp(this.angle + 360, angle, speed) % 360;
        else if (this.angle - angle > 180) {
            this.angle = Utils.lerp(this.angle, angle + 360, speed) % 360;
        } else {
            this.angle = Utils.lerp(this.angle, angle, speed);
        }
    }

    // TODO: wait what if the user changes the phone's time. I need a bypass.

    public void shoot() {
        long currTime = System.currentTimeMillis();
        if (prevShotTime + reloadMill * 10L < currTime) {
            float[] positionAndVelocity = {
                    barrelSize.x - bulletRadius, 0, // position (end of barrel)
                    bulletSpeed, 0 // velocity
            };
            barrelRotationMatrix.mapPoints(positionAndVelocity);

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.WHITE);
            Bullet bullet = new Bullet(new PointF(
                    position.x + positionAndVelocity[0],
                    position.y + positionAndVelocity[1]),
                    new PointF(velocity.x + positionAndVelocity[2],
                            velocity.y + positionAndVelocity[3]),
                    bulletRadius,
                    500,
                    paint
            );
            bullets.add(bullet);
            prevShotTime = currTime;
        }
    }
}
