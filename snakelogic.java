package snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;
import java.util.Random;

public class snakelogic implements Runnable,KeyListener{
	public Thread thread;
	private Display display;
	private String title;
	private int width,height;
	private BufferStrategy buffer;
	public Graphics g;
	private LinkedList<Point> snake;
	private int direction=Direction.no_direction;
	
	//rectangle setup
	public static final int WIDTH=15;
	public static final int HEIGHT=15;
	public static final int BOX_WIDTH=31;
	public static final int BOX_HEIGHT=31;
	private Point fruit;
	private int score;
	public snakelogic(String title,int width,int height)
	{
		this.title=title;
		this.height=height;
		this.width=width;
		
	}
	 
	public void init()
	{
		display=new Display(title,width,height);
		display.getFrame().addKeyListener(this);
		direction=Direction.right; 
		 
		snake=new LinkedList<Point>();
		RandomFruit();
		restart();
	} 
	public void Draw(Graphics g)
	{
		DrawRect(g);
		DrawSnake(g);
		DrawFruit(g);
		drawscore(g);
	}
	private void drawscore(Graphics g)
	{
		g.setColor(Color.black);
		g.setFont(new Font("arial",Font.BOLD,26));
		g.drawString("SCORE : "+score, 22, 500);
	}
	public void DrawRect(Graphics g)
	{
		
		g.setColor(Color.cyan);
		g.fillRect(0, 0, WIDTH*BOX_WIDTH, HEIGHT*BOX_HEIGHT);
		g.setColor(Color.darkGray);
		
	}
	public void DrawSnake(Graphics g)
	{
		
	 for(Point p:snake)
	 {
		 g.fillRect(p.x*BOX_WIDTH, p.y*BOX_HEIGHT, BOX_WIDTH, BOX_HEIGHT);
	 }
		 
	}
	public void restart()
	{
		direction=Direction.right;
		snake.clear();
		score =0;
		snake.add(new Point(9,7));
		snake.add(new Point(8,7));
		snake.add(new Point(7,7));
		snake.add(new Point(6,7));
		
	}
	public void DrawFruit(Graphics g)
	{
		g.setColor(Color.red);
		g.fillRect(fruit.x*BOX_WIDTH, fruit.y*BOX_HEIGHT, BOX_WIDTH, BOX_HEIGHT);
	}
	public void RandomFruit()
	{
		Random ran=new Random();
		int ranX=ran.nextInt(WIDTH);
		int ranY=ran.nextInt(HEIGHT);
		fruit =new Point(ranX,ranY);
		while(snake.contains(fruit))
		{
			fruit =new Point(ranX,ranY);
		}
	}
	public void move()
	{
		Point head=snake.peekFirst();
		Point newPoint =head;
		switch(direction)
		{
		case Direction.up:
			newPoint =new Point(head.x,head.y-1);
			break;
		case Direction.down:
			newPoint =new Point(head.x,head.y+1);
			break;
		case Direction.right:
			newPoint=new Point(head.x+1,head.y);
			break;
		case Direction.left:
			newPoint=new Point(head.x-1,head.y);
			break;
		}
		if(snake.contains(newPoint))
		{
			restart();
			return;
		}
		
		if(newPoint.x>=WIDTH)
		{
			newPoint= new Point(newPoint.x=0,newPoint.y);
		}
		else if(newPoint.x<0)
		{
			newPoint =new Point(newPoint.x=WIDTH-1,newPoint.y);
		}
		else if(head.y>=HEIGHT) {
			newPoint=new Point(newPoint.x,newPoint.y=0);
		}
		else if(head.y<0)
		{
			newPoint=new Point(newPoint.x,newPoint.y=HEIGHT-1);
		}
		snake.remove(snake.peekLast());
		if(newPoint.equals(fruit))
		{
		Point add=(Point)newPoint.clone();
		score +=5;
		snake.push(add);
		
		RandomFruit();
		}
		snake.push(newPoint);
		
	}
	public void render()
	{
		 buffer=display.getCanvas().getBufferStrategy();
		 if(buffer==null)
		 {
			 display.getCanvas().createBufferStrategy(3);
			 return;
		 }
		 g=buffer.getDrawGraphics();
		 g.clearRect(0, 0, width, height);
		 //start drawing
		// g.drawRect(0, 0, 300, 300);
		 Draw(g);
		 
		 //drawing stops
		 buffer.show();
		 g.dispose();
	}  
	
	public synchronized void start()
	{
		if(thread==null)
		{
		thread=new Thread(this);
		thread.start();
	}
	}
	public synchronized void stop()
	{
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  	
	}
	public void run()
	{
		init();
		//direction= Direction.up;
			while(true)
			{
				move();
				render();
				Thread.currentThread();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
	}

	public void keyTyped(KeyEvent e) {
		
		
	}

	
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_UP:
			if(direction!=Direction.down)
			direction= Direction.up;
			break;
		case KeyEvent.VK_DOWN:
			if(direction!=Direction.up)
			direction= Direction.down;
			break;
		case KeyEvent.VK_RIGHT:
			if(direction!=Direction.left)
			direction= Direction.right;
			break;
		case KeyEvent.VK_LEFT:
			if(direction!=Direction.right)
			direction= Direction.left;
			break;
		
		}
			
	}
	public void keyReleased(KeyEvent e) {
		
		
	}
	

}
