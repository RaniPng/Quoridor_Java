package Graphics;

import Main.GamePanel;
import java.awt.*;

public class Background {
//image of the background
	private Img _image;


	public Background(String s) {
		
		try {
			//set the image to the size of the screen
			_image=new Img(s, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void draw(Graphics2D g) {
		//draw it
		_image.drawImg(g);
	}

}
