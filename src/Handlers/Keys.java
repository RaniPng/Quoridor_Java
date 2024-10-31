package Handlers;

import java.awt.event.KeyEvent;

// this class contains a boolean array of current/previus game keys statues 
// for the keys that are used for this game.
// R button key is press when keyState[k] is true and false when released or havent pressed etc.
public class Keys {

	//my game buttons enum
	private static int _counter = 0;
	public static int UP = _counter++;
	public static int LEFT = _counter++;
	public static int DOWN = _counter++;
	public static int RIGHT = _counter++;
	public static int RB = _counter++;
	public static int BB = _counter++;
	public static int ENTER = _counter++;
	public static int ESCAPE = _counter++;
	//the number of keys in my game
	public static final int NUM_KEYS = _counter;

	public static boolean keyState[] = new boolean[NUM_KEYS];
	public static boolean prevKeyState[] = new boolean[NUM_KEYS];

	public static void keySet(int i, boolean b) {
		// vk=hash code of the key after _
		if (i == KeyEvent.VK_UP)
			keyState[UP] = b;
		else if (i == KeyEvent.VK_LEFT)
			keyState[LEFT] = b;
		else if (i == KeyEvent.VK_DOWN)
			keyState[DOWN] = b;
		else if (i == KeyEvent.VK_RIGHT)
			keyState[RIGHT] = b;
		else if (i == KeyEvent.VK_R)
			keyState[RB] = b;
		else if (i == KeyEvent.VK_B)
			keyState[BB] = b;
		else if (i == KeyEvent.VK_ENTER)
			keyState[ENTER] = b;
		else if (i == KeyEvent.VK_ESCAPE)
			keyState[ESCAPE] = b;
	}

	public static void update() {
		//update the previous pressed button
		for (int i = 0; i < NUM_KEYS; i++) {
			prevKeyState[i] = keyState[i];
		}
	}

	public static boolean isPressed(int i) {
		//input- enum key
		//output- was it pressed
		return keyState[i] && !prevKeyState[i];
	}

}
