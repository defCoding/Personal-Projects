package Game;

import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;
import Snake.*;
import Segment.*;
import Grid.*;
import Food.*;
import DrawUtil.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


/**
 * This class models the frame in which the game will be played on.
 * */
public class Game extends JFrame implements KeyListener, ActionListener {
  public static final int FRAMESIZE = 600;
  private static Snake snake;
  private static Grid grid;
  private static boolean isFood;
  private static Food food;
  private static DrawUtil drawer;
  private static Timer timer;
  /**
   * This constructor creates the frame and makes it visible.
   * */
  public Game() {
    final int SNAKESIZE = 10;
    final int SEGSIZE = 10;
    Game.isFood = true;
    Game.grid = new Grid(Game.FRAMESIZE, SEGSIZE);
    
    int gridLength = Game.grid.getGrid().length;
    Random generator = new Random();
    Game.food = new Food(Game.grid.getGrid()[generator.nextInt(gridLength)][generator.nextInt(gridLength)]);
    
    Game.snake = new Snake(SNAKESIZE, SEGSIZE, Game.grid);
    
    Game.drawer = new DrawUtil(Game.snake, Game.food);
    setSize(Game.FRAMESIZE + 15, Game.FRAMESIZE + 40);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    Game.timer = new Timer(100, this);
  }
  /**
   * Changes the velocity depending on keypress.
   * @param e The key event.
   * */
  public void keyPressed(KeyEvent e) {
    Segment segment = Game.snake.getSegments().get(0);
    if (e.getKeyCode() == 38) {
      snake.startMoving();
      snake.changeYVel(-1);
      snake.changeXVel(0);
    }
    else if (e.getKeyCode() == 40) {
      snake.startMoving();
      snake.changeYVel(1);
      snake.changeXVel(0);
    }
    else if (e.getKeyCode() == 37) {
      snake.startMoving();
      snake.changeXVel(-1);
      snake.changeYVel(0);
    }
    else if (e.getKeyCode() == 39) {
      snake.startMoving();
      snake.changeXVel(1);
      snake.changeYVel(0);
    }
    else if (e.getKeyCode() == 80) {
      Game.timer.stop();
    }
    else if (e.getKeyCode() == 79) {
      Game.timer.start();
    }
  }
  public void keyReleased(KeyEvent e) {}
  public void keyTyped(KeyEvent e) {}
  
  /**
   * This moves the snake when an action has occured.
   * @param e The action event.
   * */
  public void actionPerformed(ActionEvent e) {
    if (Game.snake.isMoving()) {
      try {
        Game.snake.move();
      } catch (ArrayIndexOutOfBoundsException exc) {
        Game.drawer.setGameOver(true);
        Game.timer.stop();
      }
    }
    if (Game.snake.getSegments().get(0).getPoint().equals(Game.food.getPoint())) {
      Game.snake.addSegment();
      Game.isFood = false;
    }
    if (!this.isFood()) {
      this.makeFood();
      Game.isFood = true;
    }
    if (!Game.snake.checkSnakeHead()) {
      Game.drawer.setGameOver(true);
      Game.timer.stop();
    }
    this.repaint();
  }
  /**
   * This method creates food on the map.
   * */
  public void makeFood() {
    int gridLength = Game.grid.getGrid().length;
    Random generator = new Random();
    Game.food.move(Game.grid.getGrid()[generator.nextInt(gridLength)][generator.nextInt(gridLength)]);
  }
  /**
   * This method checks if there is already food.
   * @return True if there is food, false otherwise.
   * */
  public boolean isFood() {
    return Game.isFood;
  }
  /**
   * This main method runs the game.
   * @param args Not used.
   * */
  public static void main(String[] args) {
    Game game = new Game();
    game.add(Game.drawer);
    game.addKeyListener(game);
    Game.timer.start();
  }
}
