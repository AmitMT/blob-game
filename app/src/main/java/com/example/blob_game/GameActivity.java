package com.example.blob_game;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blob_game.network.Socket;

public class GameActivity extends AppCompatActivity {

	public static String name;
	final PermissionManager permissionManager = new PermissionManager(this);
	final String[] PERMISSIONS = { Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE };
	public Game game;
	// public UpgradesPanel upgrades_panel;

	{
		ContextProvider.getInstance(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		name = getIntent().getExtras().getString("name");

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;


		setContentView(R.layout.activity_game);

		// upgrades_panel = findViewById(R.id.upgrades_panel);
		game = findViewById(R.id.game);

		permissionManager.setPermissions(PERMISSIONS);
		permissionManager.requestAllPermissions();

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
