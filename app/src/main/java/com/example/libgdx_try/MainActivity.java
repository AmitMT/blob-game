package com.example.libgdx_try;

import android.Manifest;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	final PermissionManager permissionManager = new PermissionManager(this);
	final String[] PERMISSIONS = { Manifest.permission.INTERNET };
	Game game;
	boolean gameActive = true;

	{
		ContextProvider.getInstance(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		Window window = getWindow();
		window.setFlags(
			WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN
		);

		setContentView(R.layout.activity_main);

		game = findViewById(R.id.game);

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
		super.onResume();
	}

	@Override
	protected void onPause() {
		game.pause();
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