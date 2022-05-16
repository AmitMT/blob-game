package com.example.libgdx_try.camera;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.example.libgdx_try.utils.Utils;

public class Camera {

	PointF transformVector;
	PointF middlePosition;
	float scaleFactor = 4f;
	boolean smoothFollow = true;
	float followRoughness = 0.05f; // 0 - 1

	PointF lastFramePosition = new PointF();
	PointF lastPlayerPosition = new PointF();

	public Camera(PointF middlePosition, PointF transformVector) {
		this.middlePosition = new PointF(middlePosition.x, middlePosition.y);
		this.transformVector = new PointF(transformVector.x, transformVector.y);
	}

	public Camera(PointF middlePosition, PointF transformVector, Options options) {
		this.middlePosition = new PointF(middlePosition.x, middlePosition.y);
		this.transformVector = new PointF(transformVector.x, transformVector.y);

		smoothFollow = options.smoothFollow;
		followRoughness = options.followRoughness;
	}

	public void transformCanvas(Canvas canvas, PointF playerPosition) {
		lastFramePosition.set(transformVector);
		lastPlayerPosition.set(playerPosition);
		canvas.translate(lastFramePosition.x, lastFramePosition.y);
		canvas.scale(scaleFactor, scaleFactor, lastPlayerPosition.x, lastPlayerPosition.y);
	}

	public void revertCanvas(Canvas canvas) {
		float scaleFactor = 1f / this.scaleFactor;
		canvas.scale(scaleFactor, scaleFactor, lastPlayerPosition.x, lastPlayerPosition.y);
		canvas.translate(-lastFramePosition.x, -lastFramePosition.y);
	}

	public PointF getTransformVector() {
		return transformVector;
	}

	public PointF getMiddlePosition() {
		return middlePosition;
	}

	public void setMiddlePosition(Canvas canvas, PointF middlePosition) {
		if (smoothFollow)
			this.middlePosition = Utils.lerp(this.middlePosition, middlePosition, followRoughness);
		else
			this.middlePosition = new PointF(middlePosition.x, middlePosition.y);

		transformVector = new PointF(canvas.getWidth() / 2f - this.middlePosition.x, canvas.getHeight() / 2f - this.middlePosition.y);
	}

	public float getScaleFactor() {
		return scaleFactor;
	}

	public void setScaleFactor(float scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public static class Options {

		boolean smoothFollow = true;
		float followRoughness = 0.05f;

		public Options() {
		}

		public boolean isSmoothFollow() {
			return smoothFollow;
		}

		public Options setSmoothFollow(boolean smoothFollow) {
			this.smoothFollow = smoothFollow;
			return this;
		}

		public float getFollowRoughness() {
			return followRoughness;
		}

		public Options setFollowRoughness(float followRoughness) {
			this.followRoughness = followRoughness;
			return this;
		}
	}
}
