package com.example.libgdx_try.game_panel;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class DebugText extends GamePanel {

    String text;
    Paint paint;

    public DebugText(PointF position, float fontSize, int color) {
        super(position);

        paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(fontSize);
    }

    public DebugText(PointF position, Paint paint) {
        super(position);

        this.paint = paint;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(text, position.x, position.y, paint);
    }

    @Override
    public void update() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}
