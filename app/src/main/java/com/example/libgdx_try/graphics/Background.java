package com.example.libgdx_try.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.libgdx_try.camera.Camera;

public class Background {

	Camera camera;
	Paint paint;
	int spaceBetweenLines = 50;

	{
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(2);
	}

	public Background(Camera camera) {
		this.camera = camera;
	}

	public Background(Camera camera, Options options) {
		this.camera = camera;

		paint = options.paint;
		spaceBetweenLines = options.spaceBetweenLines;
	}

	public void draw(Canvas canvas, Camera camera) {
		for (int i = -1000; i < canvas.getWidth() / camera.getScaleFactor() + spaceBetweenLines + 1000; i += spaceBetweenLines) {
			canvas.drawLine(
				camera.getMiddlePosition().x - canvas.getWidth() / camera.getScaleFactor() / 2f - camera.getMiddlePosition().x % spaceBetweenLines + i,
				camera.getMiddlePosition().y - canvas.getHeight() / camera.getScaleFactor() / 2f - 1000,
				camera.getMiddlePosition().x - canvas.getWidth() / camera.getScaleFactor() / 2f - camera.getMiddlePosition().x % spaceBetweenLines + i,
				camera.getMiddlePosition().y + canvas.getHeight() / camera.getScaleFactor() / 2f + 1000,
				paint
			);
		}

		for (int i = -1000; i < canvas.getHeight() / camera.getScaleFactor() + spaceBetweenLines + 1000; i += spaceBetweenLines) {
			canvas.drawLine(
				camera.getMiddlePosition().x - canvas.getWidth() / camera.getScaleFactor() / 2f - 1000,
				camera.getMiddlePosition().y - canvas.getHeight() / camera.getScaleFactor() / 2f - camera.getMiddlePosition().y % spaceBetweenLines + i,
				camera.getMiddlePosition().x + canvas.getWidth() / camera.getScaleFactor() / 2f + 1000,
				camera.getMiddlePosition().y - canvas.getHeight() / camera.getScaleFactor() / 2f - camera.getMiddlePosition().y % spaceBetweenLines + i,
				paint
			);
		}
	}

	public static class Options {

		Paint paint;
		int spaceBetweenLines = 50;

		{
			paint = new Paint();
			paint.setColor(Color.WHITE);
			paint.setStrokeWidth(2);
		}

		public Paint getPaint() {
			return paint;
		}

		public Options setPaint(Paint paint) {
			this.paint = paint;
			return this;
		}

		public int getSpaceBetweenLines() {
			return spaceBetweenLines;
		}

		public Options setSpaceBetweenLines(int spaceBetweenLines) {
			this.spaceBetweenLines = spaceBetweenLines;
			return this;
		}
	}
}
