package Handlers;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.*;
import Graphics.Sprites;

public class ColorPicker extends JFrame {
	//class of a jframe to choose a color
	private  Color c;

	public ColorPicker() {
		//init the jframe and a random color
		this.setLayout(new FlowLayout());
		this.pack();
		c = Sprites.randomColor();
	}

	public Color ColorChoose() {
		//choose a color and return it
		 c = JColorChooser.showDialog(null, "Pick a color...I guess", c);
		return c;
	}

	public Color getColor() {
		//output- the last choosed color
		return c;
	}
}
