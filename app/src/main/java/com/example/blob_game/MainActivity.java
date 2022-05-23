package com.example.blob_game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blob_game.ui.Button;


public class MainActivity extends AppCompatActivity {

	{
		ContextProvider.getInstance(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

		SharedPreferences sharedPreferences = getSharedPreferences("Blob Game SP", MODE_PRIVATE);
		String prevName = sharedPreferences.getString("name", "");

		setContentView(R.layout.activity_main);
		
		((EditText) findViewById(R.id.name_text)).setText(prevName);

		findViewById(R.id.start_button).setOnClickListener(view -> {
			String name = ((EditText) findViewById(R.id.name_text)).getText().toString();
			name = !name.equals("") ? name : null;
			((Button) findViewById(R.id.start_button)).setText("");
			((ProgressBar) findViewById(R.id.progress_bar)).setVisibility(ProgressBar.VISIBLE);

			SharedPreferences.Editor myEdit = sharedPreferences.edit();
			myEdit.putString("name", name);
			myEdit.apply();

			Intent intent = new Intent(this, GameActivity.class);
			intent.putExtra("name", name);
			startActivity(intent);
			overridePendingTransition(R.anim.no_transition, R.anim.scale_fade_out);
		});
	}
}