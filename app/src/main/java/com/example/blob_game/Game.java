package com.example.blob_game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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

import com.example.blob_game.camera.Camera;
import com.example.blob_game.camera.CameraShake;
import com.example.blob_game.game_object.Blob;
import com.example.blob_game.game_object.Tank;
import com.example.blob_game.game_panel.DebugText;
import com.example.blob_game.game_panel.Joystick;
import com.example.blob_game.graphics.Background;
import com.example.blob_game.graphics.CoronaSpriteSheet;
import com.example.blob_game.network.Socket;
import com.example.blob_game.network.TanksHandler;

import java.util.Random;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

	public static CameraShake cameraShake = new CameraShake();
	GameLoop gameLoop;
	Camera camera;
	Tank player;
	Joystick movingJoystick;
	Joystick lookingJoystick;
	Background background;

	DebugText positionText;

	DebugText fpsDebugText;
	DebugText upsDebugText;

	PointF cameraPos = new PointF();

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

		Paint backgroundPaint = new Paint();
		backgroundPaint.setColor(ContextCompat.getColor(context, R.color.background_lines));
		backgroundPaint.setStrokeWidth(5);
		Background.Options backgroundOptions = new Background.Options().setPaint(backgroundPaint);
		background = new Background(camera, backgroundOptions);

		Paint playerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		playerPaint.setColor(ContextCompat.getColor(context, R.color.player));
		Paint playerBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		playerBorderPaint.setStyle(Paint.Style.STROKE);
		playerBorderPaint.setColor(ContextCompat.getColor(context, R.color.player_border));
		playerBorderPaint.setStrokeWidth(3.5f);
		Paint barrelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		barrelPaint.setColor(ContextCompat.getColor(context, R.color.barrel));
		Paint barrelBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		barrelBorderPaint.setStyle(Paint.Style.STROKE);
		barrelBorderPaint.setColor(ContextCompat.getColor(context, R.color.barrel_border));
		barrelBorderPaint.setStrokeWidth(3.5f);

		Tank.Options playerOptions = (Tank.Options) new Tank.Options()
			.setBarrelOptions(new Tank.Barrel.Options()
				.setLength(18)
				.setThickness(22)
				.setPaint(barrelPaint)
			)
			.setName(GameActivity.name)
			.setSprite(coronaSpriteSheet.getSpriteByIndex(0, 0))
			.setPaint(playerPaint)
			.setBorderPaint(playerBorderPaint);

		Random random = new Random();
		player = new Tank(
			new PointF(random.nextInt(10000) - 5000, random.nextInt(10000) - 5000),
			30,
			playerOptions
		);
		TanksHandler.player = player;
		Socket.getInstance().setDataSending(true);

		SurfaceHolder surfaceHolder = getHolder();
		Log.i("hi", "a" + surfaceHolder);
		surfaceHolder.addCallback(this);


		gameLoop = new GameLoop(this, surfaceHolder);

		fpsDebugText = new DebugText(new PointF(20, 50), 48, ContextCompat.getColor(context, R.color.FPS_meter));
		upsDebugText = new DebugText(new PointF(20, 100), 48, ContextCompat.getColor(context, R.color.FPS_meter));

		setFocusable(true);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		canvas.drawColor(ContextCompat.getColor(getContext(), R.color.background));

		camera.setMiddlePosition(canvas, player.getPosition());

		camera.transformCanvas(canvas, new PointF(player.getPosition().x, player.getPosition().y));
		cameraShake.transformCanvas(canvas, player.getPosition());
		drawRelationalToPlayer(canvas);
		cameraShake.revertCanvas(canvas, player.getPosition());
		camera.revertCanvas(canvas);

		movingJoystick.draw(canvas);
		lookingJoystick.draw(canvas);

		positionText.draw(canvas);

		fpsDebugText.setText("FPS: " + Math.round(gameLoop.getAvgFPS() * 100.0) / 100.0);
		fpsDebugText.draw(canvas);
		upsDebugText.draw(canvas);
	}

	public void drawRelationalToPlayer(Canvas canvas) {
		background.draw(canvas, camera);

		for (Tank enemy : TanksHandler.getEnemies())
			enemy.draw(canvas);

		player.draw(canvas);
	}

	public void update() {
		positionText.setText("X: " + ((int) player.getPosition().x) / 10 + " Y: " + ((int) player.getPosition().y) / 10);
		upsDebugText.setText("UPS: " + Math.round(gameLoop.getAvgUPS() * 100.0) / 100.0);
		cameraShake.update();
		movingJoystick.update();
		lookingJoystick.update();

		player.setAcceleration(new PointF(movingJoystick.getActuator().x * Blob.MAX_ACCELERATION, movingJoystick.getActuator().y * Blob.MAX_ACCELERATION));
		if (lookingJoystick.getActuator().x != 0 && lookingJoystick.getActuator().y != 0)
			player.lerpToAngle((float) (Math.atan2(lookingJoystick.getActuator().y, lookingJoystick.getActuator().x) * 180 / Math.PI), 0.15f);
		if (lookingJoystick.isVisible()) player.shoot();
		player.update();

		for (Tank enemy : TanksHandler.getEnemies())
			enemy.update();
	}

	@Override
	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouchEvent(MotionEvent event) {
		int index = event.getActionIndex();
		int action = event.getActionMasked();

		if (action == MotionEvent.ACTION_MOVE) {
			int count = event.getPointerCount();

			boolean usedTouch = false;
			for (int i = 0; i < count; i++) {
				if (event.getX(i) < getWidth() / 2f) {
					if (movingJoystick.useTouch(event, action, i)) usedTouch = true;
				} else {
					if (lookingJoystick.useTouch(event, action, i)) usedTouch = true;
				}
			}
			if (usedTouch) return true;
		}

		if (event.getX(index) < getWidth() / 2f) {
			if (movingJoystick.useTouch(event, action, index)) return true;
		} else {
			if (lookingJoystick.useTouch(event, action, index)) return true;
		}

		return super.onTouchEvent(event);
	}

	@Override
	public void surfaceCreated(@NonNull SurfaceHolder holder) {
	}

	@Override
	public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
		camera = new Camera(
			player.getPosition(),
			new PointF(
				getWidth() / 2f - player.getPosition().x,
				getHeight() / 2f - player.getPosition().y
			)
		);
		movingJoystick = new Joystick(100, 50);
		lookingJoystick = new Joystick(100, 50);
		cameraPos.set(getWidth() / 2f, getHeight() / 2f);

		positionText = new DebugText(
			new PointF(getWidth() - 400, getHeight() - 30),
			48,
			ContextCompat.getColor(ContextProvider.getInstance().context, R.color.text)
		);

		if (gameLoop.getState().equals(Thread.State.TERMINATED))
			gameLoop = new GameLoop(this, holder);

		gameLoop.startLoop();

		new android.os.Handler().postDelayed(() -> {
			pause();
			resume();
		}, 200);
	}

	@Override
	public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
	}

	public void resume() {
		SurfaceHolder surfaceHolder = getHolder();
		gameLoop = new GameLoop(this, surfaceHolder);
		gameLoop.startLoop();
	}

	public void pause() {
		gameLoop.stopLoop();
	}
}
