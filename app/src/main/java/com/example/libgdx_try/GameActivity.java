package com.example.libgdx_try;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.libgdx_try.network.Socket;

public class GameActivity extends AppCompatActivity {

	final PermissionManager permissionManager = new PermissionManager(this);
	final String[] PERMISSIONS = { Manifest.permission.INTERNET };
	public Game game;
	boolean gameActive = true;

	{
		ContextProvider.getInstance(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;


		setContentView(R.layout.activity_game);

		game = findViewById(R.id.game);
		game.resume();

		permissionManager.setPermissions(PERMISSIONS);
		permissionManager.requestAllPermissions();

		findViewById(R.id.btn).setOnClickListener(view -> {
			if (gameActive) {
				game.pause();
				gameActive = false;
			} else {
				game.resume();
				gameActive = true;
			}
		});

		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(new ScreenReceiver(), filter);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		permissionManager.setPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		Socket.getInstance().startSocket();
		super.onResume();
	}

	@Override
	protected void onPause() {
		game.pause();
		Socket.getInstance().stopSocket();
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
