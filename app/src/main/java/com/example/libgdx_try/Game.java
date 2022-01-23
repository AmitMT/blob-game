package com.example.libgdx_try;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;

import java.util.Random;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    GameLoop gameLoop;
    Blob player;
    Blob[] blobs = new Blob[100];
    Joystick joystick;
    Paint UPSPaint;

    public Game(Context context) {
        this(context, null);
    }

    public Game(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        player = new Blob(new PointF(0, 0), 30, ContextCompat.getColor(context, R.color.player));

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        UPSPaint = new Paint();
        UPSPaint.setColor(ContextCompat.getColor(context, R.color.FPS_meter));
        UPSPaint.setTextSize(30);

        Random random = new Random();
        for (int i = 0; i < blobs.length; i++) {
            blobs[i] = new Blob(new PointF(
                    random.nextInt(1000) - 500,
                    random.nextInt(2000) - 1000),
                    random.nextInt(100),
                    ContextCompat.getColor(context, R.color.enemy)
            );
        }

        setFocusable(true);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.background));

        canvas.translate(getWidth() / 2f - player.getPosition().x, getHeight() / 2f - player.getPosition().y);
        drawRelationalToPlayer(canvas);
        canvas.translate(player.getPosition().x - getWidth() / 2f, player.getPosition().y - getHeight() / 2f);

        joystick.draw(canvas);

        drawUPS(canvas);
        drawFPS(canvas);
    }

    public void drawRelationalToPlayer(Canvas canvas) {
        for (Blob blob : blobs) {
            blob.draw(canvas);
        }

        player.draw(canvas);
    }

    public void update() {
        joystick.update();
        player.setAcceleration(new PointF(joystick.getActuator().x * Blob.MAX_ACCELERATION, joystick.getActuator().y * Blob.MAX_ACCELERATION));
        player.update();
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
//        if (joystick.useTouch(event)) return true;

        switch (event.getAction()) {
            case MotionEvent.ACTION_POINTER_DOWN:
                joystick.setCenter(new Point((int) event.getX(), (int) event.getY()));
                joystick.setActuator(new PointF(event.getX(), event.getY()));
                joystick.setVisible(true);
                return true;
            case MotionEvent.ACTION_MOVE:
                joystick.setActuator(new PointF(event.getX(), event.getY()));
                joystick.setVisible(true);
                return true;
            case MotionEvent.ACTION_UP:
                joystick.resetActuator();
                joystick.setVisible(false);
                return true;
        }

        int action = event.getAction();
        // Get the index of the pointer associated with the action.
//        int index = event.getActionIndex();
        int xPos = -1;
        int yPos = -1;

        Log.d("hii", "The action is " + event.getActionIndex() + actionToString(action) + action + " " + new Random().nextInt());

        return super.onTouchEvent(event);
    }

    public static String actionToString(int action) {
        switch (action) {

            case MotionEvent.ACTION_DOWN:
                return "Down";
            case MotionEvent.ACTION_MOVE:
                return "Move";
            case MotionEvent.ACTION_POINTER_DOWN:
                return "Pointer Down";
            case MotionEvent.ACTION_UP:
                return "Up";
            case MotionEvent.ACTION_POINTER_UP:
                return "Pointer Up";
            case MotionEvent.ACTION_OUTSIDE:
                return "Outside";
            case MotionEvent.ACTION_CANCEL:
                return "Cancel";
        }
        return "";
    }


    public void drawUPS(Canvas canvas) {
        String avgUPS = Double.toString(gameLoop.getAvgUPS());

        canvas.drawText("UPS: " + avgUPS, 15, 40, UPSPaint);
    }

    public void drawFPS(Canvas canvas) {
        String avgFPS = Double.toString(gameLoop.getAvgFPS());

        canvas.drawText("FPS: " + avgFPS, 15, 75, UPSPaint);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        joystick = new com.example.libgdx_try.Joystick(new Point(150, getHeight() - 150), 100, 50);
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}
