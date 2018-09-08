/*
Christopher Sordan
*/

package snake; 

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

public class SnakeGame extends javax.swing.JPanel implements java.awt.event.KeyListener {

	
	int[][] taken = new int[30][30]; 

	//occupancy constants
	public final int WALL = 1, SNAKE = 2, FRUIT = 3, COLLISION = 4, NORTH = 5, SOUTH = 6, WEST = 7, EAST = 8; //directions, collision, snake, etc

	// grid of the game
	public static final int ROW_COUNT = 30, COLUMN_COUNT = 30;

	// variables of level total and score to be acted upon
	int level = 1;

	int total = 0;
	
	int score = 0;

	String highScore = "";

	int increment = 2;

	boolean gameOver = false;

	// where the snake will start
	int[][] snakeInitial = { { 10, 11 }, { 10, 12 }, { 10, 13 }, { 10, 14 }, { 10, 15 } };

	int[] xSnake = new int[900]; // 30 x 30

	int[] ySnake = new int[900]; 

	int nSnake = 5; // length of the snake

	javax.swing.JLabel titleGame = new javax.swing.JLabel("My First Java Game"); //all the instructions, labels etc 

	javax.swing.JLabel playerhighScore = new javax.swing.JLabel("Snake Hall of Fame: ");

	javax.swing.JLabel playerScore = new javax.swing.JLabel("CURRENT SCORE : 0");
	
	javax.swing.JLabel playerTotal = new javax.swing.JLabel("TOTAL SCORE : 0");
	
	

	javax.swing.JLabel playerLevel = new javax.swing.JLabel("LEVEL : 1");

	javax.swing.JLabel playerInstructions = new javax.swing.JLabel(
			"INSTRUCTIONS: Grow the snake by eating randomly generated fruits.");

	javax.swing.JLabel playerInstructions2 = new javax.swing.JLabel("Avoid the walls and yourself! Be careful the speed increases with the levels!");

	javax.swing.JLabel playerControls = new javax.swing.JLabel(
			"CONTROLS: Use either the WASD keys or the arrow to control the snake.");

	javax.swing.JLabel playerControls2 = new javax.swing.JLabel(
			"Use the shift or space key to restart the game when prompted and to change the level.");

	javax.swing.JLabel moveText = new javax.swing.JLabel("Press shift or space to go to the next level");

	javax.swing.JLabel gameOverLabel = new javax.swing.JLabel("GAME OVER, PRESS shift/aspace");

	java.util.Random random = new java.util.Random();

	public void init() {

		this.setPreferredSize(new java.awt.Dimension(1024, 500));

		this.setBackground(java.awt.Color.cyan);

		// layout of the level and starting snake position
		int[][] layout = getlevelObstacles(level);
		for (int y = 0; y < ROW_COUNT; y++)  
		{
			for (int x = 0; x < COLUMN_COUNT; x++) 
			{
				taken[y][x] = layout[y][x];
			}
		}

		
		for (int i = 0; i < nSnake; i++) {
			xSnake[i] = snakeInitial[i][0];
			ySnake[i] = snakeInitial[i][1];
		}
		drawSnake();

		titleGame.setBounds(635, 20, 300, 30);

		playerScore.setBounds(650, 100, 300, 30);
		
		playerTotal.setBounds(650, 125, 300, 30);

		playerhighScore.setBounds(650, 150, 300, 30);

		playerLevel.setBounds(650, 200, 100, 30);

		playerControls.setBounds(490, 300, 500, 30);

		playerControls2.setBounds(490, 310, 500, 30);

		playerInstructions.setBounds(490, 350, 600, 30);

		playerInstructions2.setBounds(490, 360, 600, 30);

		moveText.setBounds(180, 200, 200, 30);

		moveText.setForeground(java.awt.Color.WHITE);

		gameOverLabel.setBounds(130, 200, 200, 30);

		gameOverLabel.setForeground(java.awt.Color.WHITE);

		gameOverLabel.setVisible(false);

		this.setLayout(null);

		this.add(titleGame);

		this.add(playerScore);

		this.add(playerhighScore);

		this.add(playerLevel);

		this.add(playerControls);

		this.add(playerControls2);

		this.add(playerInstructions);

		this.add(playerInstructions2);

		this.add(moveText);

		this.add(gameOverLabel);
		
		this.add(playerTotal);

	}

	int[][] getlevelObstacles(int level) {

		while (level > 10)
			level -= 10;
		// levels to be used that are set up later in code
		String[] allLevel = { null, level1, level2, level3, level4, level5, level6, level7, level8, level9, level10 };

		int H = ROW_COUNT;

		int W = COLUMN_COUNT;

		int[][] levelObstacles = new int[H][W];

		int i = 0;
		String s = allLevel[level];
		for (int y = 0; y < H; y++)
			for (int x = 0; x < W; x++) {
				levelObstacles[y][x] = s.charAt(i++) - '0';
			}
		return levelObstacles;
	}
	// 
	public void drawSnake() {
		for (int i = 0; i < nSnake; i++) {
			int x = xSnake[i], y = ySnake[i];

			taken[y][x] = SNAKE; // snake head

			if (i == 0)
				taken[y][x] = NORTH; // snake body
		}
	}

	
	public void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g); 

		// setting up the stage with color layout
		java.awt.Color WALL_COLOR = java.awt.Color.ORANGE;
		java.awt.Color FRUIT_COLOR = java.awt.Color.YELLOW;
		java.awt.Color SNAKE_COLOR = java.awt.Color.GREEN;
		java.awt.Color COLLISION_COLOR = java.awt.Color.GRAY;

		for (int y = 0; y < ROW_COUNT; y++) 
		{
			for (int x = 0; x < COLUMN_COUNT; x++) 
			{
				java.awt.Color color = java.awt.Color.BLACK;

				if (taken[y][x] == WALL)
					color = WALL_COLOR;

				else if (taken[y][x] == SNAKE)
					color = SNAKE_COLOR;

				else if (taken[y][x] == FRUIT)
					color = FRUIT_COLOR;

				else if (taken[y][x] == COLLISION)
					color = COLLISION_COLOR;

				g.setColor(color);
				g.fillRect(x * 16, y * 16, 16, 16);  

				if (taken[y][x] == FRUIT)
					drawFRUIT(g, x, y);

				if (taken[y][x] == NORTH)
					snakeHead(g, x, y, northHeadImage);

				if (taken[y][x] == SOUTH)
					snakeHead(g, x, y, southHeadImage);

				if (taken[y][x] == WEST)
					snakeHead(g, x, y, westHeadImage);

				if (taken[y][x] == EAST)
					snakeHead(g, x, y, eastHeadImage);

				if (taken[y][x] == SNAKE)
					snakeBody(g, x, y);
			}
		}

	}

	// randomly input fruit into open space
	void showFRUIT() {
		int W = taken[0].length; 
		int H = taken.length; 

		
		int x = 0, y = 0;
		while (true) {
			x = random.nextInt(W);
			y = random.nextInt(H);

			if (taken[y][x] == 0)
				break;
		}

		
		taken[y][x] = FRUIT;
	}
	// when user has reached the next level after completing the previous level
	void nextLevel() {
		CheckScore();
		repaint();

		try {
			Thread.sleep(1000);
		} catch (Exception ignore) {
		}

		level++;
		playerLevel.setText("LEVEL : " + level);
		int[][] layout = getlevelObstacles(level);

		int H = taken.length, W = taken[0].length;
		for (int y = 0; y < H; y++) 
		{
			for (int x = 0; x < W; x++)
			{
				taken[y][x] = layout[y][x];
			}
		}

		nSnake = 5;
		for (int i = 0; i < nSnake; i++) {
			xSnake[i] = snakeInitial[i][0];
			ySnake[i] = snakeInitial[i][1];
		}
		drawSnake();

		repaint();

		// clear
		shiftPressed = leftPressed = rightPressed = upPressed = rightPressed = tabPressed = false;
	}

	void sleep(int millisecond) {
		try {
			Thread.sleep(millisecond);
		} catch (Exception ignore) {
		}
	}

	void waitshift() {

		moveText.setText("Level " + level + ", press shift or space");

		moveText.setVisible(true);

		repaint();

		while (!shiftPressed) {
			sleep(10);
		}
		;

		moveText.setVisible(false);
	}

	
	// this method is called every time until game is over or next level
	void gameLoop() {

		waitshift();

		int grow = 0; 

		int xv = 0, yv = -1; 

		showFRUIT();

		int frame = 0;
		int speed = 10; 
		
		
		while (true) {
			repaint();

			int delay = 50 * speed / level ;

			sleep(delay); 

			// head coordinate
			int x = xSnake[0], y = ySnake[0];
			int x0 = x, y0 = y;

			// tail coordinate
			int xTail = xSnake[nSnake - 1], yTail = ySnake[nSnake - 1];

			if (leftPressed && xv == 0) { // move left
				xv = -1;
				yv = 0;
			} 
			if (rightPressed && xv == 0) { // move right
				xv = +1;
				yv = 0;
			} 
			if (upPressed && yv == 0) { //  move up
				xv = 0;
				yv = -1;
			} 
			if (downPressed && yv == 0) { // move down
				xv = 0;
				yv = +1;
			
			
			} 

			x += xv;
			y += yv; 

			int head = 0; 
			if (yv < 0)
				head = NORTH;
			if (yv > 0)
				head = SOUTH;
			if (xv < 0)
				head = WEST;
			if (xv > 0)
				head = EAST;

			if (taken[y][x] == WALL) 
			{
				taken[y][x] = COLLISION; // hit wall game over
				repaint();
				gameOver = true;
				break;
			}
			if (taken[y][x] == SNAKE) 
			{
				taken[y][x] = COLLISION; // hit itself game over
				repaint();
				gameOver = true;
				break;
			}
			if (taken[y][x] == FRUIT)  // eat fruit score changes and snake grows
			{
				score = score + increment;
				total = total + increment;
				highScore = " HighScore = " + level;
				playerScore.setText("CURRENT SCORE : " + score);
				playerTotal.setText("TOTAL SCORE : " + total);
				grow = 5;
				if (score % 10 == 0) {
					taken[y][x] = head; 
											
					taken[y0][x0] = SNAKE; 
					nextLevel();
					break;
				}

				if (highScore.equals("")) {

					highScore = this.GetHighScoreValue();

				}
				showFRUIT();
			}

			if (grow > 0) { // if snake grows update the head and tail of the snake and clear the old head and tail
				nSnake++;
			} 

			shiftRight(); 

			xSnake[0] = x;
			ySnake[0] = y; 

			taken[y][x] = head;
			taken[y0][x0] = SNAKE; 

			if (grow == 0)
				taken[yTail][xTail] = 0; 
			if (grow > 0)
				grow--;

		}

	}

	void showGameOver() {
		gameOverLabel.setVisible(true);

		this.repaint();
	}
	// snake position ={(10,11),(10,12),(10,13),(10,14),(10,15)}
	// shifted position ={(unused),(10,11),(10,12),(10,13),(10,14)} -> shift out (10,15)
	// the unused cell is reserved for the new head position
	// the original tail is shifted out and will be erased from screen.
	
	void shiftRight() {
		for (int i = nSnake - 1; i >= 1; i--) {
			xSnake[i] = xSnake[i - 1];
			ySnake[i] = ySnake[i - 1];
		}
	}

	// keyboard controls
	boolean leftPressed = false;
	
	boolean rightPressed = false;
	
	boolean upPressed = false;
	
	boolean downPressed = false;
	
	boolean shiftPressed = false;

	boolean tabPressed = false;
	public void keyPressed(java.awt.event.KeyEvent event) {

		leftPressed = false;

		rightPressed = false;

		upPressed = false;

		downPressed = false;

		shiftPressed = false;
		
		tabPressed= false;
		
		if (event.getKeyCode() == 37) 
		{
			leftPressed = true;
		}
		if (event.getKeyCode() == 65)
		{
			leftPressed = true;
		}
		if (event.getKeyCode() == 39)
		{
			rightPressed = true;
		}
		if (event.getKeyCode() == 68) 
		{
			rightPressed = true;
		}
		if (event.getKeyCode() == 40) 
		{
			downPressed = true;
		}
		if (event.getKeyCode() == 83) 
		{
			downPressed = true;
		}
		if (event.getKeyCode() == 38) 
		{
			upPressed = true;
		}
		if (event.getKeyCode() == 87)
		{
			upPressed = true;
		}
		if (event.getKeyCode() == 16)
		{
			shiftPressed = true;
		}
		if (event.getKeyCode() == 32)
		{
			shiftPressed = true;
		}
		if (event.getKeyCode() == 9)
		{
			tabPressed = true;
		}

	}

	
	public void keyReleased(java.awt.event.KeyEvent event) {
		
	}

	
	public void keyTyped(java.awt.event.KeyEvent event) {
		
	}

	public void drawFRUIT(java.awt.Graphics g, int x, int y) {
		java.awt.Color[] colorTable = { java.awt.Color.BLACK, new java.awt.Color(0x80, 0x00, 0x80),
				new java.awt.Color(0x00, 0xff, 0x00), new java.awt.Color(0xff, 0x00, 0x00),
				new java.awt.Color(0xff, 0xff, 0x00), new java.awt.Color(0xc0, 0xc0, 0xc0),
				new java.awt.Color(0xff, 0xff, 0xff), new java.awt.Color(0x80, 0x80, 0x80) };

		x *= 16; 
		y *= 16; 

		for (int row = 0, i = 0; row < 16; row++) {
			for (int column = 0; column < 16; column++, i++) {
				java.awt.Color color = colorTable[FRUITImage1[i]];
				g.setColor(color);
				g.drawLine(x + column, y + row, x + column, y + row);
			}
		}
	}

	public void snakeHead(java.awt.Graphics g, int x, int y, int[] image) {
		java.awt.Color[] colorTable = { java.awt.Color.BLACK, new java.awt.Color(0x00, 0x80, 0x00),
				new java.awt.Color(0x00, 0xff, 0x00), new java.awt.Color(0xff, 0x00, 0x00),
				new java.awt.Color(0x00, 0x00, 0x80), };

		x *= 16; 
		y *= 16; 

		for (int row = 0, i = 0; row < 16; row++) {
			for (int column = 0; column < 16; column++, i++) {
				java.awt.Color color = colorTable[image[i]];
				g.setColor(color);
				g.drawLine(x + column, y + row, x + column, y + row);
			}
		}
	}

	void snakeBody(java.awt.Graphics g, int x, int y) {
		g.setColor(new java.awt.Color(0x00, 0x80, 0x00));
		g.drawLine(x * 16, y * 16, x * 16 + 1, y * 16);
		g.drawLine(x * 16, y * 16, x * 16, y * 16 + 1);
		g.drawLine(x * 16 + 14, y * 16, x * 16 + 15, y * 16);
		g.drawLine(x * 16 + 15, y * 16, x * 16 + 15, y * 16 + 1);
		g.drawLine(x * 16, y * 16 + 14, x * 16, y * 16 + 15);
		g.drawLine(x * 16, y * 16 + 14, x * 16 + 1, y * 16 + 15);
		g.drawLine(x * 16 + 15, y * 16 + 14, x * 16 + 15, y * 16 + 15);
		g.drawLine(x * 16 + 14, y * 16 + 15, x * 16 + 15, y * 16 + 15);
	}

	void restart() {
		shiftPressed = false;

		while (!shiftPressed) { //pauses threads 
			sleep(10);
		}
		;

		gameOverLabel.setVisible(false);

		this.level = 0;

		this.score = 0;

		playerScore.setText("SCORE : 0");

		nextLevel();

	}

	int[] upsideDown(int[] image) {
		int[] target = new int[image.length];

		int W = 16, H = 16;
		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) {
				target[(H - 1 - y) * W + x] = image[y * W + x];
			}
		}
		return target;
	}

	int[] rotate(int[] image) {
		
		int[] target = new int[image.length];

		int W = 16, H = 16;
		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) {
				target[x * H + y] = image[y * W + x]; // row major => column
														// major
			}
		}
		return target;
	}

	int[] flip(int[] image) {
		int[] target = new int[image.length];

		int W = 16, H = 16;
		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) {
				target[y * W + W - x - 1] = image[y * W + x];
			}
		}
		return target;
	}

	public static void main(String[] args) throws Exception {

		SnakeGame snake = new SnakeGame();

		snake.init();

		javax.swing.JFrame window = new javax.swing.JFrame("Christopher Sordan Snake Game");

		window.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

		window.add(snake);

		window.pack();

		window.setVisible(true);

		window.addKeyListener(snake);

		while (true) {
			while (!snake.gameOver) {
				
				snake.gameLoop();
			}

			snake.showGameOver();

			snake.gameOver = false;

			snake.restart();
		}
	}

	// level layouts
	String level1 =

	"111111111111111111111111111111" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "111111111111111111111111111111";

	String level2 = "111111111111111111111111111111" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "111111110000000000001111111111" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "111111111111111111111111111111";

	String level3 = "111111111111111111111111111111" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000001100000000000001" + "100000000000001100000000000001"
			+ "100000000000001100000000000001" + "100000000000001100000000000001" + "100000000000001100000000000001"
			+ "100000000000001100000000000001" + "100000000000001100000000000001" + "100000000000001100000000000001"
			+ "100000000000001100000000000001" + "100000000000001100000000000001" + "100000000000001100000000000001"
			+ "100000000000001100000000000001" + "100000000000001100000000000001" + "100000000000001100000000000001"
			+ "100000000000001100000000000001" + "100000000000001100000000000001" + "100000000000001100000000000001"
			+ "100000000000001100000000000001" + "100000000000001100000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "111111111111111111111111111111";

	String level4 = "111111111111111111111111111111" + "100000000000001100000000000001"
			+ "100000000000001100000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000001100000000000001" + "100000000000001100000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000001100000000000001" + "100000000000001100000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "111100000000000111111111111111" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000001100000000000001"
			+ "100000000000001100000000000001" + "100000000000000000000000000001" + "100000000000001100000000000001"
			+ "100000000000001100000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000001100000000000001" + "100000000000001100000000000001" + "100000000000001100000000000001"
			+ "111111111111111111111111111111";

	String level5 =

	"111111111111111111111111111111" +

	"100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "111111100000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000001111111111111111111" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "111100000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000001111111111111111111" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "111111110000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000001111111111111111111"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "111111111111111111111111111111";

	String level6 =

	"111111111111111111111111111111" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000100001" + "100000010000000000000000100001"
			+ "100000010000000000000000100001" + "100000010000000000000000100001" + "100000010000000000000000100001"
			+ "100000010001111111111000100001" + "100000010001000000000000100001" + "100000010001000000000000100001"
			+ "100000010001000000001000100001" + "100000010001000110001000100001" + "100000010001000110001000100001"
			+ "100000010001000110001000100001" + "100000010001000110001000100001" + "100000010001000110001000100001"
			+ "100000010001000000001000100001" + "100000010000000000001000100001" + "100000010000000000001000100001"
			+ "100000010001111111111000100001" + "100000010000000000000000100001" + "100000010000000000000000100001"
			+ "100000010000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "111111111111111111111111111111";

	String level7 =

	"111111111111111111111111111111" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "111111000111111111010001111111" + "100000000100000000010000000001" + "100000000100000000010000000001"
			+ "100000000100000000010000000001" + "100001111100000000011111000001" + "100000000100000000010000000001"
			+ "100000000100000000010000000001" + "100001111100000000011111000001" + "100000000100000000010000000001"
			+ "100000000100000000010000000001" + "100000000100000000010000000001" + "111111000101111111110001111111"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "111111111111111111111111111111";

	String level8 =

	"111111111111111111111111111111" + "100010000000000000000000000001" + "100010000000000000000000010001"
			+ "100010000000000000000000010001" + "100010000000000000000000010001" + "100010000000000000000000010001"
			+ "100010000000000000000000010001" + "100010000000000000000000010001" + "100010000111111111110000010001"
			+ "100010000100000000010000010001" + "100010000100000000010000010001" + "100010000100000000010000010001"
			+ "100010000100000000010000010001" + "100010000100000000010000010001" + "100010000100000000010000010001"
			+ "100010000100000000010000010001" + "100010000100000000010000010001" + "100010000100000000010000010001"
			+ "100010000100000000010000010001" + "100010000100000000010000010001" + "100010000100000000010000010001"
			+ "100010000101111111010000010001" + "100010000000000000000000010001" + "100010000000000000000000010001"
			+ "100010000000000000000000010001" + "100010000000000000000000010001" + "100010000000000000000000010001"
			+ "100010000000000000000000010001" + "100000000000000000000000010001" + "111111111111111111111111111111";

	String level9 = "111111111111111111111111111111" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100111111111111111111111111001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000011111110111111100000001" + "100000010000000000000100000001"
			+ "101010010000000000000100100101" + "101010010000000000000100010101" + "100000010000000000000100000001"
			+ "100000010000000000000100000001" + "101010010000000000000100010101" + "100000010000000000000100000001"
			+ "100000010000000000000100000001" + "101010010000000000000100010101" + "100000010000000000000100000001"
			+ "100101011111111111111101010101" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "100000000000000000000000000001" + "100000000000000000000000000001" + "100000000000000000000000000001"
			+ "111111111111111111111111111111";

	String level10 = 
					"111111111111111111111111111111"+
					"100000000000000000000000000001"+
					"100000000000000000000000000001"+
					"100000000000000000000000000001"+
					"100000000000000000000000000001"+
					"100000000000000000000000000001"+
					"100000000000000000000000000001"+
					"100000000000000000000000000011"+
					"100000000000000000000000000101"+
					"100000000000000000000000001001"+
					"100000000000000000000000010001"+
					"100000000000000000000000100001"+
					"100000000000000000000001000001"+
					"100000111111111111111111000001"+
					"100000000000000000000001000001"+
					"100000111111111111111101000001"+
					"100000100011000000000101000001"+
					"100000101000010100010101000001"+
					"111111101101000100010101000001"+
					"100000101000010001010101000001"+
					"100000101010000011010101000001"+
					"100000101001010000010001000001"+
					"100000101111111111111111000001"+
					"100000000000000000000000000001"+
					"100000000000000000000000000001"+
					"100000000000000000000000000001"+
					"100000000000000000000000000001"+
					"100000000000000000000000000001"+
					"100000000000000000000000000001"+
					"111111111111111111111111111111";

	int[] FRUITImage = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0,
			0, 0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 1, 2, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 1, 1, 2, 1, 0, 0, 0, 0, 1,
			5, 5, 5, 5, 5, 1, 1, 1, 2, 1, 0, 0, 0, 0, 1, 5, 5, 6, 6, 5, 1, 2, 2, 2, 1, 0, 0, 0, 0, 0, 1, 5, 6, 6, 5, 1,
			2, 2, 2, 2, 2, 1, 0, 0, 0, 1, 5, 5, 5, 5, 5, 1, 1, 1, 2, 2, 1, 2, 1, 0, 0, 1, 5, 5, 5, 5, 5, 5, 7, 7, 1, 1,
			1, 2, 1, 0, 0, 1, 7, 5, 5, 5, 7, 7, 7, 5, 3, 3, 1, 2, 1, 0, 1, 5, 7, 7, 7, 7, 5, 7, 7, 3, 3, 4, 1, 1, 0, 0,
			1, 3, 7, 5, 7, 7, 7, 3, 4, 3, 3, 3, 3, 1, 0, 0, 1, 3, 3, 3, 3, 4, 3, 3, 3, 3, 4, 3, 1, 0, 0, 0, 1, 3, 4, 3,
			3, 3, 3, 4, 3, 3, 1, 1, 0, 0, 0, 0, 0, 1, 3, 3, 4, 3, 3, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0,
			0, 0, 0, 0, 0, 0, 0, 0 };
	
	int[] FRUITImage1 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0,
			0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1,
			1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 5, 5, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 5, 5, 1, 1,
			1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
			1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0,
			1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1,
			1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 
			1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0,
			1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1,
			1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1,1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0,
			0, 0, 0, 0, 0, 0, 0, 0 };









	int[] northHeadImage = { 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 1, 1, 1, 2, 2, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 2, 2, 1, 1, 0, 0, 0, 0, 0, 0,
			0, 1, 1, 2, 2, 2, 2, 2, 2, 1, 1, 0, 0, 0, 0, 0, 3, 3, 3, 2, 2, 2, 2, 2, 2, 3, 3, 3, 0, 0, 0, 3, 3, 3, 3, 3,
			2, 2, 2, 2, 3, 3, 3, 3, 3, 0, 0, 3, 3, 3, 3, 3, 2, 2, 2, 2, 3, 3, 3, 3, 3, 0, 0, 3, 3, 3, 3, 3, 2, 2, 2, 2,
			3, 3, 3, 3, 3, 0, 0, 1, 3, 3, 3, 2, 2, 2, 2, 2, 2, 3, 3, 3, 1, 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1,
			1, 0, 0, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 0, 1, 1,
			1, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2,
			2, 2, 2, 2, 2, 2, 1, 1, 1, 1 };

	int[] southHeadImage = upsideDown(northHeadImage);

	int[] westHeadImage = rotate(northHeadImage);

	int[] eastHeadImage = flip(westHeadImage);

	public void CheckScore() { //checks score if you reached up to or past level nine records name in dat file 
		

		if (level >= 9) {

			String user = JOptionPane.showInputDialog("You entered the National Snake Hall Of Fame. Enter a name:");
			highScore = user + level;
			File scoreFile = new File("highscore.dat");
			if (!scoreFile.exists()) {
				try {
					scoreFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			FileWriter writeFile = null;
			BufferedWriter writer = null;
			try {
				writeFile = new FileWriter(scoreFile);
				writer = new BufferedWriter(writeFile);
				writer.write(this.highScore);
			} catch (Exception e) {

			} finally {
				try {
					if (writer != null)

						writer.close();
				} catch (Exception e) {
				}

			}
		}
	}

	public String GetHighScoreValue() {

		FileReader readFile = null;
		BufferedReader reader = null;

		try {
			readFile = new FileReader("highscore.dat");
			reader = new BufferedReader(readFile);
			return reader.readLine();
		}

		catch (Exception e) {
			return "Nobody:0";
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}



