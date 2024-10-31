package GameState;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import Graphics.Background;
import Graphics.Img;
import Main.GamePanel;

public class HelpState extends GameState {
	//buttons
	private Img next;
	private Img prev;
	private Img _exitButton;
	
	//the backgrounds are used as a pages of information that the knowledge the player have is drawn/ written in the actual picture
	private Background pages[];
	private int currentPage;//used to swap between the background- like swapping pages
	
	
	public HelpState(GameStateManager gsm) {
		this._gsm = gsm;
		init();
	}

	@Override
	public void init() {
		//init the backgrounds, there are 4 images and their names are 0 to 3 with the same ending -jpg
		pages=new Background[4];
		for (int i = 0; i < pages.length; i++) {
			pages[i]=new Background("/Backgrounds/Help/"+(i)+".jpg");
		}
		
		//init buttons
		next=new Img("/buttons/help/next.png", GamePanel.WIDTH - 100, GamePanel.HEIGHT - 90, 90, 13);
		prev=new Img("/buttons/help/prev.png", 10, GamePanel.HEIGHT - 90, 90, 13);
		_exitButton = new Img("/buttons/settings/exit.png", 10, 30, 90, 13);
		//init to the first page
		currentPage=0;
	}

	@Override
	public void update() {

	}

	@Override
	public void draw(Graphics2D g) {
		//draw all the buttons and the background
		pages[currentPage].draw(g);
		g.drawString(String.valueOf(currentPage), GamePanel.WIDTH/2, 10);
		next.drawImg(g);
		prev.drawImg(g);
		_exitButton.drawImg(g);
	}

	@Override
	public void handleInput() {

	}

	@Override
	public void MouseClicked(MouseEvent e) {
		//if the next button was pressed then switch to the next page,if the previous button was pressed switch to the previous
		if(next.isClicked(e)&&currentPage<pages.length-1) 
			currentPage++;
		if(prev.isClicked(e)&&currentPage>0) 
			currentPage--;
		if (_exitButton.isClicked(e))
			_gsm.setState(_gsm.MENUSTATE);
		
	}

}
