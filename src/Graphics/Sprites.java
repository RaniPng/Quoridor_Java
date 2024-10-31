package Graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.imageio.ImageIO;

public class Sprites {
	// set Charecter Colors
	public static BufferedImage setImgColor(Color c, BufferedImage player) {
		// input- image and a color the change it to
		// output- the image when its color set to the given color
		// the color set according to shades of white from the original picture
		int w = player.getWidth();
		int h = player.getHeight();
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				// go throh each pixel of the image and change its shade according to the color
				// and the original picture
				int cur = player.getRGB(i, j);
				if (!isTransparent(i, j, player) && !isColor(i, j, player, Color.BLACK)) {
					player.setRGB(i, j, setColor(cur, c));
				}
			}
		}
		return player;
	}

	private static int setColor(int cur, Color c) {
		// input- the current RGB of the pixel and a color to change to a shade of
		// output- the RGB of the mixed shade
		if (c != null) {
			if (cur == -1118482)
				cur = c.getRGB();
			else if (cur == -1)
				cur = c.brighter().getRGB();
			else if (cur == 16777215)
				cur = c.darker().getRGB();
			else if (cur == -2368549)
				cur = c.brighter().darker().getRGB();
			else if (cur == -3552823)
				cur = c.darker().getRGB();
			else if (cur == -7039852)
				cur = c.brighter().brighter().getRGB();
			else if (cur == -5390378)
				cur = c.brighter().brighter().getRGB();
		}
		return cur;
	}

	public static Color randomColor() {
		// output- random color
		Random rand = new Random();
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		return new Color(r, g, b);
	}

	public static boolean isTransparent(int x, int y, BufferedImage img) {
		// input- position of pixel in the image img
		// output- is the pixel is transparent
		boolean flag = false;
		int pixel = img.getRGB(x, y);
		if ((pixel >> 24) == 0x00) {
			flag = true;
		}
		return flag;
	}

	public static boolean isColor(int x, int y, BufferedImage img, Color c) {
		// input- position in the img and a color
		// output- is the pixel color is the given color
		int pixel = img.getRGB(x, y);
		return (pixel == c.getRGB());

	}

	public static Img[][] load(String s, int w, int h, int x, int y) {
		// input- path to an image size of each selection in the image and position
		// where the selection start from
		// output- image matrix of all the selections in the image
		Img[][] ret;
		int tempY = y;
		try {
			BufferedImage spritesheet = ImageIO.read(Sprites.class.getResourceAsStream(s));
			int width = spritesheet.getWidth() / w;
			int height = spritesheet.getHeight() / h;
			ret = new Img[height][width];

			for (int j = 0; j < width; j++) {
				y = tempY;
				for (int i = 0; i < height; i++) {
					ret[i][j] = new Img(spritesheet.getSubimage(j * w, i * h, w, h), x, y, w, h);
					y += 50;
				}
				x += 50;
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}

	public static BufferedImage cutBufferedImage(BufferedImage img) {
		// input- an image
		// output- the same img but sliced in a way that the image size is in the size
		// of the figure in the image
		// cut off all the sides transparent of the figure
		int w = img.getWidth();
		int h = img.getHeight();
		int upH = -1;
		int downH = h;
		// height
		for (int i = 0; i < w; i++) {
			int numhu = 0;
			int numhd = h;
			boolean swich = false;
			for (int j = 0; j < h; j++) {
				boolean isT = isTransparent(i, j, img);
				if (isT) {
					if (!swich)
						numhu++;
					else
						numhd--;
				} else
					swich = true;
			}

			if (upH < 0)
				upH = numhu;
			else
				upH = Math.min(upH, numhu);

			if (downH >= h)
				downH = numhd;
			else
				downH = Math.max(downH, numhd);
		}

		img = img.getSubimage(0, upH, w, downH - upH);
		h = downH - upH;

		// width
		int leftW = -1, rightW = -1;
		for (int j = 0; j < h; j++) {

			int numhL = 0;
			int numhR = w;
			boolean swich = false;
			for (int i = 0; i < w; i++) {
				boolean isT = isTransparent(i, j, img);
				if (isT) {
					if (!swich)
						numhL++;
					else
						numhR--;
				} else
					swich = true;
			}

			if (leftW < 0)
				leftW = numhL;
			else
				leftW = Math.min(leftW, numhL);

			if (rightW >= h)
				rightW = numhR;
			else
				rightW = Math.max(rightW, numhR);
		}
		img = img.getSubimage(leftW, 0, rightW - leftW, h);
		return img;
	}

}
