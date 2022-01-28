package com.example.libgdx_try.game_panel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.MotionEvent;

public class Joystick {

    Point outerCircleCenter = new Point(0, 0);
    float outerCircleRadius;
    Point innerCircleCenter = new Point(0, 0);
    float innerCircleRadius;
    Paint outerCirclePaintFill;
    Paint outerCirclePaintStroke;
    Paint innerCirclePaint;
    PointF actuator = new PointF(0, 0);
    boolean visible = false;

    public Joystick(float outerCircleRadius, float innerCircleRadius) {
        this.outerCircleRadius = outerCircleRadius;
        this.innerCircleRadius = innerCircleRadius;

        outerCirclePaintFill = new Paint();
        outerCirclePaintFill.setColor(Color.parseColor("#66666666"));
        outerCirclePaintFill.setStyle(Paint.Style.FILL);

        outerCirclePaintStroke = new Paint();
        outerCirclePaintStroke.setColor(Color.parseColor("#88888888"));
        outerCirclePaintStroke.setStyle(Paint.Style.STROKE);
        outerCirclePaintStroke.setStrokeWidth(8);

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(Color.parseColor("#88000000"));
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
        outerCircleCenter = new Point(center.x, center.y);
        innerCircleCenter = new Point(center.x, center.y);
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
            case MotionEvent.ACTION_DOWN:
                setCenter(new Point((int) event.getX(), (int) event.getY()));
                setActuator(new PointF(event.getX(), event.getY()));
                setVisible(true);
                return true;
            case MotionEvent.ACTION_MOVE:
                setActuator(new PointF(event.getX(), event.getY()));
                return true;
            case MotionEvent.ACTION_UP:
                resetActuator();
                setVisible(false);
                return true;
        }
        return false;
    }
}
