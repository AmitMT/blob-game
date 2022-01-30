package com.example.libgdx_try;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.libgdx_try.game_object.Blob;
import com.example.libgdx_try.game_object.Tank;
import com.example.libgdx_try.game_panel.Joystick;
import com.example.libgdx_try.graphics.CoronaSpriteSheet;

import java.util.Random;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    GameLoop gameLoop;
    Tank player;
    Blob[] blobs = new Blob[100];
    Joystick movingJoystick;
    Joystick lookingJoystick;
    Paint UPSPaint;
    float scaleFactor = 4;

    public Game(Context context) {
        this(context, null);
    }

    public Game(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        CoronaSpriteSheet coronaSpriteSheet = new CoronaSpriteSheet(context);

        Paint playerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        playerPaint.setColor(ContextCompat.getColor(context, R.color.player));
        player = new Tank(
                new PointF(0, 0),
                30,
                playerPaint,
                coronaSpriteSheet.getSpriteByIndex(0, 0),
                new Point(50, 20),
                100
        );

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        UPSPaint = new Paint();
        UPSPaint.setColor(ContextCompat.getColor(context, R.color.FPS_meter));
        UPSPaint.setTextSize(30);

        Random random = new Random();
        Paint enemyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        enemyPaint.setColor(ContextCompat.getColor(context, R.color.enemy));
        for (int i = 0; i < blobs.length; i++) {
            blobs[i] = new Blob(new PointF(
                    random.nextInt(1000) - 500,
                    random.nextInt(2000) - 1000),
                    random.nextInt(50) + 20,
                    enemyPaint,
                    coronaSpriteSheet.getSpriteByIndex(0, 3)
            );
        }

        setFocusable(true);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.background));
        canvas.translate(getWidth() / 2f - player.getPosition().x, getHeight() / 2f - player.getPosition().y);
        canvas.scale(scaleFactor, scaleFactor, player.getPosition().x, player.getPosition().y);
        drawRelationalToPlayer(canvas);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor, player.getPosition().x, player.getPosition().y);
        canvas.translate(player.getPosition().x - getWidth() / 2f, player.getPosition().y - getHeight() / 2f);

        movingJoystick.draw(canvas);
        lookingJoystick.draw(canvas);

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
        movingJoystick.update();
        lookingJoystick.update();

        player.setAcceleration(new PointF(movingJoystick.getActuator().x * Blob.MAX_ACCELERATION, movingJoystick.getActuator().y * Blob.MAX_ACCELERATION));
        if (lookingJoystick.getActuator().x != 0 && lookingJoystick.getActuator().y != 0)
            player.lerpToAngle((float) (Math.atan2(lookingJoystick.getActuator().y, lookingJoystick.getActuator().x) * 180 / Math.PI), 0.15f);
        if (lookingJoystick.isVisible()) player.shoot();
        player.update();
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);

        if (event.getX(index) < getWidth() / 2f) {
            if (movingJoystick.useTouch(event, action, index)) return true;
        } else {
            if (lookingJoystick.useTouch(event, action, index)) return true;
        }

        return super.onTouchEvent(event);
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
        movingJoystick = new Joystick(100, 50);
        lookingJoystick = new Joystick(100, 50);

        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {
            gameLoop = new GameLoop(this, holder);
        }
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    public void pause() {
        gameLoop.stopLoop();
    }
}
