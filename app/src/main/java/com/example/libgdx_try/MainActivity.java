package com.example.libgdx_try;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	{
		ContextProvider.getInstance(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn).setOnClickListener(view -> {
			Intent intent = new Intent(this, GameActivity.class);
			startActivity(intent);
		});
	}
}