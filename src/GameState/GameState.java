package GameState;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public abstract class GameState {
	//abstract class ,simulate a game page
	
	protected GameStateManager _gsm;//the page manger
	public abstract void init();//init page
	public abstract void update();//update page
	public abstract void draw(Graphics2D g);//draw page
	public abstract void handleInput();//listen to input in page
	public abstract void MouseClicked(MouseEvent e);//listen to mouse clicks in page
}
