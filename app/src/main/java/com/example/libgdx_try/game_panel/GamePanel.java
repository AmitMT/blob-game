package com.example.libgdx_try.game_panel;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;

public abstract class GamePanel {
    protected PointF position;

    public GamePanel(PointF position) {
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

    public boolean useTouch(MotionEvent event, int action, int index) {
        return false;
    }
}
