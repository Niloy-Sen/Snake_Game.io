package snake;

public class mainGame {
	public static void main(String args[])
	{
		//new Display("SNAKE",600,600);
		snakelogic snake=new snakelogic("snakes",500,600);
		snake.start();
	}

}
