import java.awt.*;


public class Shape {

    private int startX = 4, startY = 0;
    public int normalSpeed = 500;
    public int fastSpeed = 50;
    public int movementDelayTime = normalSpeed;
    private long beginTime;
    private int deltaX = 0;
    private boolean collision = false;
    private int[][] currentCoords;
    private final int[][] nextCoords;
    private final Board board;
    private final Color shapeColor;
    private final String name;

    public Shape(int[][] coords, Board board, Color color, String name) {
        this.currentCoords = coords;
        this.nextCoords = coords;
        this.board = board;
        this.shapeColor = color;
        this.name = name;
    }

    public int getStartX() {
        return startX;
    }
    public int getStartY() {
        return startY;
    }

    public int[][] getCoords(){
        return currentCoords;
    }
    public String getName() {
        return name;
    }

    public void reset(){
        this.startX = 4;
        this.startY = 0;
        collision = false;
    }

    public void update() {
        // check collision, stop if collided and reset shape

        if(collision) {
            //iterate through board and fill shapeColor for shapes (arrays with 1s)
            for(int row=0; row < currentCoords.length; row++) {
                for(int col=0; col < currentCoords[0].length; col++) {
                    if(currentCoords[row][col] != 0) {
                        board.getBoard()[startY + row][startX + col] = shapeColor;
                    }
                }
            }
            checkIfLineFilled();
            board.setCurrentShape();
            return;
        }

        // check horizontal moving and not out of bounds before moving to wished direction
        boolean moveX = true;
        if(!(startX + deltaX + currentCoords[0].length > Board.BOARD_WIDTH) && !(startX + deltaX < 0)) {
            for (int row = 0; row < currentCoords.length; row++) {
                for (int col = 0; col < currentCoords[row].length; col++) {
                    if((currentCoords[row][col] != 0) && (board.getBoard()[startY + row][startX + deltaX + col] != null)){
                        moveX = false;
                    }
                }
            }
            if(moveX){
                startX += deltaX;
            }
        }
        deltaX = 0; //reset deltaX to stop infinite movement

        // check vertical moving
        if(System.currentTimeMillis() - beginTime > movementDelayTime) {
            if(!(startY + 1 + currentCoords.length > Board.BOARD_HEIGHT) && board.getBoard()[startY +1][startX] == null) {
                for(int row=0; row < currentCoords.length; row++) {
                    for(int col=0; col < currentCoords[row].length; col++) {
                        if(currentCoords[row][col] != 0) {
                            if(board.getBoard()[startY + 1 + row][startX + deltaX + col] != null) {
                                collision = true;
                            }
                        }
                    }
                }
                if(!collision){
                    startY++;
                }
            }
            else {
                collision = true;
            }
            beginTime = System.currentTimeMillis();
        }
    }

    private void checkIfLineFilled() {
        int bottomLine = board.getBoard().length - 1;
        int points=0;
        for(int topLine = board.getBoard().length - 1; topLine > 0; topLine--) {
            int count = 0;
            for(int col = 0; col < board.getBoard()[0].length; col++){
                if(board.getBoard()[topLine][col] != null) {
                    count++;
                }
                board.getBoard()[bottomLine][col] = board.getBoard()[topLine][col];
            }
            if(count < board.getBoard()[0].length) {
                bottomLine--;
                points++;
            }
        }
       if (19-points == 1){
           board.addScore(1000);
       }
        else if (19-points == 2){
           board.addScore(2500);
       }
        else if (19-points == 3){
           board.addScore(5000);
       }
        else if (19-points == 4){
           board.addScore(10000);
       }
        else if (19-points == 5){
           board.addScore(20000);
       }
        else {
            board.addScore(100);
        }
    }

    public void rotate() {
        if (Board.STATE_PLAYING == board.getState()) {
            int[][] temp = new int[currentCoords[0].length][currentCoords.length];
            for(int row=0; row < currentCoords.length; row++) {
                for(int col=0; col < currentCoords[row].length; col++) {
                    temp[col][currentCoords.length - row - 1] = currentCoords[row][col];
                }
            }

            //check if it colides with right border
            if(startX + temp[0].length > Board.BOARD_WIDTH || startY + temp.length > Board.BOARD_HEIGHT) {
                return;
            }

            //check if it colides with other shapes
            for(int row=0; row < temp.length; row++) {
                for(int col=0; col < temp[row].length; col++) {
                    if(temp[row][col] != 0 && board.getBoard()[startY + row][startX + col] != null) {
                        return;
                    }
                }
            }
            currentCoords = temp;
        }
    }

    public void renderCurrent(Graphics g) {
        // Draw Shape
        for(int row=0; row < currentCoords.length; row++) {
            for(int col=0; col < currentCoords[0].length; col++) {
                if(currentCoords[row][col] != 0){
                    g.setColor(shapeColor);
                    g.fillRect(col*Board.BLOCK_SIZE + startX * Board.BLOCK_SIZE, row*Board.BLOCK_SIZE + startY *Board.BLOCK_SIZE, Board.BLOCK_SIZE, Board.BLOCK_SIZE);
                }
            }
        }
    }

    public void renderNext(Graphics g) {
        // Draw Shape
        for(int row=0; row < nextCoords.length; row++) {
            for(int col=0; col < nextCoords[0].length; col++) {
                if(nextCoords[row][col] != 0){
                    g.setColor(shapeColor);
                    int nextX = 11;
                    int nextY = 1;
                    g.fillRect(col*Board.BLOCK_SIZE + nextX * Board.BLOCK_SIZE, row*Board.BLOCK_SIZE + nextY *Board.BLOCK_SIZE, Board.BLOCK_SIZE, Board.BLOCK_SIZE);
                }
            }
        }
    }


    public void speedUp() {
        if(movementDelayTime == normalSpeed) {
            movementDelayTime = fastSpeed;
        }
    }
    public void speedDown() {
        if(movementDelayTime == fastSpeed) {
            movementDelayTime = normalSpeed;
        }
    }
    public void moveLeft() {
        deltaX = -1;
    }
    public void moveRight() {
        deltaX = 1;
    }
}
