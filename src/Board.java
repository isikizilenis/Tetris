import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Board extends JPanel implements KeyListener, MouseListener, MouseMotionListener {

    // State Flags
    public static int STATE_PLAYING = 0;
    public static int STATE_GAMEOVER = 1;
    public static int STATE_PAUSED = 2;
    public static int LEADERBOARD = 3;

    private int state = STATE_PLAYING;
    private boolean nameRequested = false;

    // Game Board Size
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 20;
    public static final int BLOCK_SIZE = 30;
    
    private final Color[][] board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
    public static Color[] colors = new Color[] {
            Color.RED,
            Color.ORANGE,
            Color.GREEN,
            Color.BLUE,
            Color.MAGENTA,
            Color.CYAN,
            Color.YELLOW
    };
    private Shape currentShape;
    private Shape nextShape;
    private final Shape[] shapeTypes;

    public int score;

    public Board() {
        shapeTypes = new Shape[7];
        shapeTypes[0] = new Shape(new int[][]{
                {1, 1, 1}
        }, this, colors[0], "Line-shaped Tetromino");

        shapeTypes[1] = new Shape(new int[][]{
                {1, 1, 1},
                {0, 1, 0}
        }, this, colors[1], "T-shaped Tetromino");

        shapeTypes[2] = new Shape(new int[][]{
                {1, 1, 1},
                {1, 0, 0}
        }, this, colors[2], "Clockwise L-shaped Tetromino");

        shapeTypes[3] = new Shape(new int[][]{
                {1, 1, 1},
                {0, 0, 1}
        }, this, colors[3], "Counter-clockwise L-shaped Tetromino");

        shapeTypes[4] = new Shape(new int[][]{
                {0, 1, 1},
                {1, 1, 0}
        }, this, colors[4], "Counter-clockwise Z-shaped Tetromino");

        shapeTypes[5] = new Shape(new int[][]{
                {1, 1, 0},
                {0, 1, 1}
        }, this, colors[5], "Z-shaped Tetromino");

        shapeTypes[6] = new Shape(new int[][]{
                {1, 1},
                {1, 1}
        }, this, colors[6], "Square-shaped Tetromino");

        currentShape = getRandomShape(shapeTypes);
        nextShape = getRandomShape(shapeTypes);

        // updates and repaints the board FOR EVERY TICK depending on players pressed keys
        Timer looper = new Timer(1000/60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();
            }
        });
        looper.start();
    }

    private void update() {
        if(state == STATE_PLAYING) {
            currentShape.update();
        }
    }

    public int getState() {
        return state;
    }

    public void setCurrentShape(){
        currentShape = nextShape;
        nextShape = getRandomShape(shapeTypes);
        currentShape.movementDelayTime = currentShape.normalSpeed;
        currentShape.reset();
        checkGameOver();
    }

    private void checkGameOver(){
        int[][] coords = currentShape.getCoords();
        for(int row=0; row < coords.length; row++) {
            for(int col=0; col < coords[0].length; col++) {
                if(coords[row][col] != 0 && board[currentShape.getStartY() + row][currentShape.getStartX() + col] != null) {
                    state = STATE_GAMEOVER;
                    if(!nameRequested) {
                        nameRequested = true;
                        String playerName = JOptionPane.showInputDialog(null, "Enter your name:", "Game Over", JOptionPane.PLAIN_MESSAGE);
                        if (playerName == null || playerName.toString().trim().isEmpty()) {
                            playerName = "Unknown"; // Standardname, falls der Benutzer nichts eingibt
                        }
                        else{
                            playerName = playerName.trim();
                            if(playerName.length() > 10) {
                                playerName = playerName.substring(0, 10);
                            }
                        }
                        Leaderboard.updateLeaderboard(playerName.toString(), getCurrentDateTime(), score);
                        System.out.println(Leaderboard.readLeaderboard());
                    }
                    System.out.println("State: " + state);
                }
            }
        }
    }

    public static String getCurrentDateTime() {
        // Aktuelles Datum und Uhrzeit abrufen
        LocalDateTime now = LocalDateTime.now();

        // Format für Datum und Uhrzeit definieren
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        // Formatieren und als String zurückgeben
        return now.format(formatter);
    }


    private static Shape getRandomShape(Shape[] shapes) {
        return shapes[(int) (Math.random() * shapes.length)];
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw Background
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        currentShape.renderCurrent(g);
        nextShape.renderNext(g);

        for(int row = 0; row < board.length; row++){
            for (int col = 0; col < board[row].length; col++){
                if(board[row][col] != null){
                    g.setColor(board[row][col]);
                    g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }

        // Draw Grid
        g.setColor(Color.BLACK);
        for (int row=0; row<BOARD_HEIGHT+1; row++) {
            if(row == 4 || row == 15){
                g.drawLine(0, row*BLOCK_SIZE, BOARD_HEIGHT*Gui.WIDTH, BLOCK_SIZE*row);
            }
            g.drawLine(0, row*BLOCK_SIZE, BOARD_WIDTH*BLOCK_SIZE, row*BLOCK_SIZE);
        }
        for (int col=0; col<BOARD_WIDTH+1; col++) {
            g.drawLine(col*BLOCK_SIZE, 0, col*BLOCK_SIZE, BOARD_HEIGHT*BLOCK_SIZE);
        }

        g.setFont(new Font("Arial", Font.BOLD, 13));
        g.drawString("Next Shape:", 330, 20);

        g.setFont(new Font("Arial", Font.BOLD, 17));
        g.drawString("Score:", 320, 475);

        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.drawString(String.valueOf(score), 320, 500);

        if(state == STATE_PAUSED) {
            g.setColor(Color.blue);
            g.fillRect(104, 275, 91, 35);

            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("PAUSED", 109, 300);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(104, 275, 91, 35);
        }
        if(state == LEADERBOARD){
            // background
            g.setColor(Color.black);
            g.fillRect(0, 190, 300, 170);

            // frame
            g.setColor(Color.orange);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(0, 190, 300, 170);
            g2d.drawRect(0, 190, 300, 30);

            // title
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Leaderboard", 100, 210);

            // leaderboard entries
            g.setColor(Color.white);
            String leaderboard = Leaderboard.readLeaderboard();
            String[] lines = leaderboard.split("\n");
            int y = 245;
            for (int i = 0; i < Math.min(lines.length, 5); i++) {
                g.drawString(lines[i], 15, y);
                y += 20; // Abstand zwischen Einträgen
            }

            // press space to continue
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.PLAIN, 10));
            g.drawString("Press SPACE to continue", 96, 350);
        }
        if(state == STATE_GAMEOVER) {
            g.setColor(Color.black);
            g.fillRect(85, 230, 131, 139);

            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("GAME OVER", 90, 263);

            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 15));
            g.drawString("Your Score:", 110, 305);

            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 15));
            if(score<1000){
                g.drawString(Integer.toString(score), 140, 330);
            }
            if(score>=1000 && score<10000){
                g.drawString(Integer.toString(score), 135, 330);
            }
            else if (score >= 10000){
                g.drawString(Integer.toString(score), 130, 330);
            }

            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.PLAIN, 10));
            g.drawString("Press SPACE to continue", 92, 360);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(85, 230, 131, 139);
            g2d.drawRect(85, 230, 131, 50);
        }
    }

    public Color[][] getBoard() {
        return board;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            currentShape.speedDown();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            currentShape.speedUp();
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            currentShape.moveLeft();
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            currentShape.moveRight();
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP) {
            currentShape.rotate();
        }
        else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            if(state == STATE_GAMEOVER) {
                state = LEADERBOARD;
                System.out.println("state: " + state);
                nameRequested = false;
            }
            else if (state == STATE_PLAYING) {
                state = STATE_PAUSED;
                System.out.println("State: " + state);
            }
            else if (state == STATE_PAUSED) {
                state = STATE_PLAYING;
                System.out.println("State: " + state);
            }
            else if (state == LEADERBOARD) {
                for(int row=0; row < BOARD_HEIGHT; row++) {
                    for(int col=0; col < BOARD_WIDTH; col++) {
                        board[row][col] = null;
                        setScore(0);
                    }
                }
                setCurrentShape();
                currentShape.movementDelayTime = currentShape.normalSpeed;
                state = STATE_PLAYING;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
    
    public void addScore(int score) {
        this.score += score;
    }
    public void setScore(int score) {
        this.score = score;
    }
}
