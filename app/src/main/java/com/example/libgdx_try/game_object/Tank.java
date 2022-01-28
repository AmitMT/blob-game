package com.example.libgdx_try.game_object;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.libgdx_try.graphics.Sprite;

public class Tank extends Blob {

    Point barrelSize = new Point(80, 30);
    float angle = 0; // 0 - 360
    int reloadMill = 100;
    Paint barrelPaint;
    Matrix barrelRotationMatrix = new Matrix(); // For server optimisation. used instead of "canvas.rotate()"
    Path barrelPath = new Path();

    public Tank(PointF position, float radius, int color, Sprite sprite) {
        super(position, radius, color, sprite);

        barrelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barrelPaint.setColor(Color.parseColor("#cccccc"));
    }

    public Tank(PointF position, float radius, int color, Sprite sprite, Point barrelSize, int reloadMill) {
        this(position, radius, color, sprite);

        this.barrelSize = barrelSize;
        this.reloadMill = reloadMill;
    }

    @Override
    public void draw(Canvas canvas) {
        barrelRotationMatrix.setRotate(angle);
        // 2d matrix of the barrel
        float[] points = new float[]{
                0, -barrelSize.y / 2f,
                barrelSize.x, -barrelSize.y / 2f,
                barrelSize.x, +barrelSize.y / 2f,
                0, +barrelSize.y / 2f
        };
        barrelRotationMatrix.mapPoints(points);

        barrelPath.reset();
        barrelPath.moveTo(points[0] + position.x, points[1] + position.y);
        barrelPath.lineTo(points[2] + position.x, points[3] + position.y);
        barrelPath.lineTo(points[4] + position.x, points[5] + position.y);
        barrelPath.lineTo(points[6] + position.x, points[7] + position.y);

        canvas.drawPath(barrelPath, barrelPaint);

        super.draw(canvas);
    }

    @Override
    public void update() {
        super.update();
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
