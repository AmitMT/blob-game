package com.example.libgdx_try;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.libgdx_try.ui.TextDialog;

public class MainActivity extends AppCompatActivity {

	Game game;
	boolean gameActive = true;

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

		findViewById(R.id.btn).setOnClickListener(view -> {
			if (gameActive) {
				game.pause();
				gameActive = false;
			} else {
				game.resume();
				gameActive = true;
			}
		});

		TextDialog.Options options = new TextDialog.Options()
			.setTitle("Payment successful")
			.setText("Your payment has been successfully submitted. We’ve sent you an email with all of the details of your order.");
		TextDialog textDialog = new TextDialog(this, options);
		textDialog.show();
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