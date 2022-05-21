package com.example.libgdx_try.utils;

import com.example.libgdx_try.game_object.CircleObject;

public class Collision {

	public static boolean circleCircle(CircleObject circle1, CircleObject circle2) {
		return Math.pow(circle1.getPosition().x - circle2.getPosition().x, 2) +
			Math.pow(circle1.getPosition().y - circle2.getPosition().y, 2)
			<
			Math.pow(circle1.getRadius() + circle2.getRadius(), 2);
	}


}
