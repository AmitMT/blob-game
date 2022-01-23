package com.example.libgdx_try;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;

public class Joystick {
    Point outerCircleCenter;
    float outerCircleRadius;
    Point innerCircleCenter;
    float innerCircleRadius;
    Paint outerCirclePaintFill;
    Paint outerCirclePaintStroke;
    Paint innerCirclePaint;
    double centerToTouchDistanceSqr;
    PointF actuator = new PointF(0, 0);
    boolean visible = true;

    public Joystick(Point center, float outerCircleRadius, float innerCircleRadius) {
        outerCircleCenter = new Point(center.x, center.y);
        innerCircleCenter = new Point(center.x, center.y);
        this.outerCircleRadius = outerCircleRadius;
        this.innerCircleRadius = innerCircleRadius;

        outerCirclePaintFill = new Paint();
        outerCirclePaintFill.setColor(Color.parseColor("#88888888"));
        outerCirclePaintFill.setStyle(Paint.Style.FILL);

        outerCirclePaintStroke = new Paint();
        outerCirclePaintStroke.setColor(Color.parseColor("#88aaaaaa"));
        outerCirclePaintStroke.setStyle(Paint.Style.STROKE);
        outerCirclePaintStroke.setStrokeWidth(8);

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(Color.parseColor("#88000000"));
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void draw(Canvas canvas) {
        if (visible) {
            canvas.drawCircle(outerCircleCenter.x, outerCircleCenter.y, outerCircleRadius, outerCirclePaintFill);
            canvas.drawCircle(outerCircleCenter.x, outerCircleCenter.y, outerCircleRadius, outerCirclePaintStroke);

            canvas.drawCircle(innerCircleCenter.x, innerCircleCenter.y, innerCircleRadius, innerCirclePaint);
        }
    }

    public void update() {
        innerCircleCenter.set((int) (outerCircleCenter.x + actuator.x * outerCircleRadius), (int) (outerCircleCenter.y + actuator.y * outerCircleRadius));
    }

    public PointF getActuator() {
        return actuator;
    }

    public void setActuator(PointF touchPosition) {
        double deltaX = touchPosition.x - outerCircleCenter.x;
        double deltaY = touchPosition.y - outerCircleCenter.y;
        double deltaDistanceSqr = Math.pow(deltaX, 2) + Math.pow(deltaY, 2);

        if (deltaDistanceSqr < Math.pow(outerCircleRadius, 2)) {
            actuator.set((float) (deltaX / outerCircleRadius), (float) (deltaY / outerCircleRadius));
        } else {
            double deltaDistance = Math.sqrt(deltaDistanceSqr);
            actuator.set((float) (deltaX / deltaDistance), (float) (deltaY / deltaDistance));
        }
    }

    public void resetActuator() {
        actuator.set(0, 0);
    }

    public void setCenter(Point center) {
        outerCircleCenter = center;
        innerCircleCenter = center;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean useTouch(MotionEvent event) {
        // Return true if used the touch event
        switch (event.getAction()) {
            case MotionEvent.ACTION_POINTER_DOWN:
                setCenter(new Point((int) event.getX(), (int) event.getY()));
                setActuator(new PointF(event.getX(), event.getY()));
                setVisible(true);
                return true;
            case MotionEvent.ACTION_MOVE:
                setActuator(new PointF(event.getX(), event.getY()));
                setVisible(true);
                return true;
            case MotionEvent.ACTION_UP:
                resetActuator();
                setVisible(false);
                return true;
        }
        return false;
    }
}
