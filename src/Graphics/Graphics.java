package Graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import Board.Block;
import Board.Board;
import Entity.Barrier;
import Entity.Player;
import Handlers.Players;
import Handlers.Turns;
import Main.GamePanel;

public class Graphics {
	// image of a block
	public static final Img _block = new Img("/Board/block.png", 1, 1, Block.size, Block.size);
	// players sprites
	public static BufferedImage player[];
	// the size of player
	private static int _playSize = Block.size;
	public static String numbers[];// path to numbers imges
	// players colors
	public static Color _playerColor[];

	public static void setPlayers() {
		// set the arrays according to the number of players
		player = new BufferedImage[Players._players.length];
		_playerColor = new Color[Players._players.length];
		numbers = new String[4];
		for (int i = 0; i < numbers.length; i++)
			numbers[i] = "/buttons/Win/" + (i + 1) + ".png";
	}

	public static void drawCurrPlayerTurn(Graphics2D g) {
		// drawing who is the current player on the screen
		int num = Turns._currentTurn + 1;
		String str = "Player " + num + " Turn";
		g.setColor(Color.black);
		g.fillRect(GamePanel.WIDTH - 90, 10, 75, 15);
		g.setColor(_playerColor[num - 1]);
		g.drawString(str, GamePanel.WIDTH - 90, 20);

	}

	public static void drawCurrPlayerUI(Graphics2D g) {
		// draw above the current player/their barrier his number
		Player p = Turns.getCurrTurnPlayer();
		if (p != null) {
			int x = p.get_x(), y = p.get_y() - 10;
			if (p.is_settingABarrier()) {
				x = p.get_toPut().get_x();
				y = p.get_toPut().get_y() - 10;
			}
			int num = Turns._currentTurn + 1;
			String str = "Player " + num;
			g.setColor(Color.black);
			g.fillRect(x, y - 10, 45, 15);
			g.setColor(_playerColor[num - 1]);
			g.drawString(str, x, y);
		}

	}

	public static void drawPlayer(int x, int y, int i, Graphics2D g) {
		// draw given player in given position
		if (player[i] != null)
			g.drawImage(player[i], x, y, _playSize, _playSize, null);
	}

	public static void drawPlayerBarriers(Graphics2D g, int count, Point p, int dx, int dy, int width, int height,
			int playerNum) {
		// draw player's barrier station
		int x = p.x, y = p.y;
		for (int i = 0; i < count; i++) {
			drawBarierr(g, x, y, width, height, playerNum);
			x += dx;
			y += dy;
		}
	}

	public static void drawBlock(Graphics2D g, int x, int y) {
		// draw a block
		_block.drawImg(g, x, y);
	}

	public static void drawBarierr(Graphics2D g, int x, int y, int width, int height, int num) {
		// draw a barrier
		// (can be player's and board's
		g.setColor(_playerColor[num - 1]);
		g.fillRect(x, y, width, height);
	}

	public static void drawBarriers(ArrayList<Barrier> obstacles, Graphics2D g) {
		// draw the barriers from the array list
		for (int i = 0; i < obstacles.size(); i++) {
			int x = obstacles.get(i).get_x();
			int y = obstacles.get(i).get_y();
			int width = obstacles.get(i).getWidth();
			int height = obstacles.get(i).getHeight();
			drawBarierr(g, x, y, width, height, obstacles.get(i).get_playerNum());
		}
	}

	public static void drawBoard(Graphics2D g, int col, int row) {
		// draw all the blocks of the board
		g.setColor(Color.black);
		int x = Board._boardXOffset, y;
		for (int i = 0; i < col; i++) {
			y = Board._boardYOffset;
			for (int j = 0; j < row; j++) {
				drawBlock(g, x, y);
				y += Barrier.thickness + Block.size;
			}
			x += Barrier.thickness + Block.size;
		}
	}

	// set Charecter Colors
	public static void setSprite(BufferedImage img, int i) {
		player[i - 1] = img;

	}

	// set color of player in i
	public static void setColor(Color c, int i) {
		if (i <= _playerColor.length)
			_playerColor[i - 1] = c;
	}

	public static boolean addOrRemovePlayer(int amount) {
		//change the amount of players and their graphics data
		if (amount > 4 || amount <= 1)
			return false;
		int len = player.length;
		if (amount < len)
			len = amount;
		Color c[] = new Color[amount];
		BufferedImage p[] = new BufferedImage[amount];
		for (int i = 0; i <= len - 1; i++) {
			p[i] = player[i];
			c[i] = _playerColor[i];
		}
		player = p;
		_playerColor = c;
		return true;
	}

}
