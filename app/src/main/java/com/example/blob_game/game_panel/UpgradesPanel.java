package com.example.blob_game.game_panel;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.blob_game.utils.Utils;

public class UpgradesPanel extends LinearLayout {

	public final static String[] upgrades = { "Health", "Regeneration", "Body Damage", "Speed", "Damage", "Bullet Speed", "Reload" };

	int upgradePoints = 0;

	UpgradeView[] upgradeViews = new UpgradeView[upgrades.length];

	public UpgradesPanel(Context context, AttributeSet attributeSet) {
		this(context);
	}

	public UpgradesPanel(Context context) {
		super(context);

		setOrientation(LinearLayout.VERTICAL);

		for (int i = 0; i < upgrades.length; i++) {
			upgradeViews[i] = new UpgradeView(context, upgrades[i], this);
			addView(upgradeViews[i]);
		}

		setVisibility(View.GONE);
		open();
	}

	public void addUpgradePoint() {
		upgradePoints++;
		if (upgradePoints > 0)
			open();
	}

	public void removeUpgradePoint() {
		upgradePoints--;
		if (upgradePoints <= 0)
			close();
	}

	public void open() {
		for (UpgradeView upgradeView : upgradeViews)
			upgradeView.setTranslationX(-1000);

		final int[] i = { 0 };
		Handler handler = new Handler(Looper.getMainLooper());
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (i[0] < upgrades.length) {
					upgradeViews[i[0]].setTranslationX(0);
					Animation animation = new TranslateAnimation(-1000, 0, 0, 0);
					animation.setDuration(500);
					animation.setInterpolator(new DecelerateInterpolator());
					upgradeViews[i[0]].startAnimation(animation);
					i[0]++;
					handler.postDelayed(this, 30);
				}
			}
		};
		runnable.run();

		setVisibility(View.VISIBLE);
	}

	public void close() {
		final int[] i = { 0 };
		Handler handler = new Handler(Looper.getMainLooper());
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (i[0] < upgrades.length) {
					Animation animation = new TranslateAnimation(0, -1000, 0, 0);
					animation.setDuration(500);
					animation.setInterpolator(new AccelerateInterpolator());
					UpgradeView currentView = upgradeViews[i[0]];
					handler.postDelayed(() -> {
						currentView.setTranslationX(-1000);
					}, 450);
					currentView.startAnimation(animation);
					i[0]++;
					handler.postDelayed(this, 30);
				} else {
					handler.postDelayed(() -> {
						setVisibility(View.GONE);
					}, 500);
				}
			}
		};
		runnable.run();
	}

	static class UpgradeView extends LinearLayout {

		final int MAX = 10;
		int progress = 0;

		LinearLayout progressLayout;

		public UpgradeView(Context context, AttributeSet attributeSet) {
			this(context, "", new UpgradesPanel(context));
		}

		public UpgradeView(Context context, String upgradeName, UpgradesPanel parent) {
			super(context);

			setGravity(Gravity.CENTER_VERTICAL);
			setPadding(0, 0, 0, (int) Utils.convertDpToPixel(2, context));
			setBackgroundColor(Color.parseColor("#80000000"));
			setPadding(
				(int) Utils.convertDpToPixel(5, context),
				(int) Utils.convertDpToPixel(5, context),
				(int) Utils.convertDpToPixel(5, context),
				(int) Utils.convertDpToPixel(5, context)
			);

			progressLayout = new LinearLayout(context);
			updateProgressLayout(progressLayout);
			addView(progressLayout);

			Button plus = new Button(context);
			plus.setText("+");
			plus.setTypeface(Typeface.DEFAULT_BOLD);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				(int) Utils.convertDpToPixel(20, context),
				(int) Utils.convertDpToPixel(20, context)
			);
			params.setMargins((int) Utils.convertDpToPixel(10, context), 0, 0, 0);
			plus.setLayoutParams(params);
			plus.setPadding(0, 0, 0, 0);
			plus.setBackgroundColor(Color.parseColor("#77777777"));
			plus.setOnClickListener(view -> {
				if (parent.upgradePoints > 0) {
					progress++;
					updateProgressLayout(progressLayout);
					parent.removeUpgradePoint();
				} else
					parent.close();
			});
			addView(plus);

			TextView textView = new TextView(context);
			params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins((int) Utils.convertDpToPixel(10, context), 0, 0, 0);
			textView.setLayoutParams(params);
			textView.setText(upgradeName);
			textView.setTypeface(Typeface.DEFAULT_BOLD);
			addView(textView);
		}

		void updateProgressLayout(LinearLayout progressLayout) {
			progressLayout.removeAllViews();
			View[] progressViews = new View[MAX];
			for (int i = 0; i < progressViews.length; i++) {
				progressViews[i] = new View(getContext());
				progressViews[i].setBackgroundColor(i < progress ? Color.parseColor("#00b2e1") : Color.parseColor("#77777777"));
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					(int) Utils.convertDpToPixel(10, getContext()),
					(int) Utils.convertDpToPixel(10, getContext())
				);
				layoutParams.setMargins((int) Utils.convertDpToPixel(1, getContext()), 0, (int) Utils.convertDpToPixel(1, getContext()), 0);
				progressViews[i].setLayoutParams(layoutParams);
				progressLayout.addView(progressViews[i]);
			}
		}
	}
}

