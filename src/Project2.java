import java.lang.Math;   
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.util.Scanner;
import java.util.Stack;
import java.io.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//Eyiara Oladipo
//Project 2, 11/14/2022

public class Project2 extends JFrame{

	
	static Stack usersMoves = new Stack();
	//Declaring the necessary variables
	static String userName = "";
	static String buttonPosition = "";
	static int mazeColumn = 0;
	static int mazeRows = 0;
	static int mazeWallPercentage = 0;
	static JButton[][] mazeButtonsArray ;
	static char[][] maze = new char[mazeRows][mazeColumn]; 
	static int currentPositionColumn;
	static int currentPositionRow;
	static int pathRow = 0;
	static int pathColumn = 0;
	static int endPathRow = 0;
	static int userPositionRow = 0;
	static int userPositionColumn = 0;
	static int currentUserMoves = 0;
	static int computersMoves = 0;
	static JFrame mainGameFrame; 
	static boolean moveColumnAgain = false;
	//0 is none, 1 is up, -1 is down
	static int rowDirection = 0;
	static int columnDirection = 0;
	static boolean moveRowFromTopEdge = false;
	static boolean moveRowFromBottomEdge = false;
		


	//Get the user name, maze width, height, and percentage of walls
	//Name: getGameInformatione
	//Output: None
	//Input: none
	//Purpose: Create a JFrame to receive user name, maze height, width, and percentage of walls
	public static void getGameInformation() {
		//Creating an intro title
		JLabel introLabel = new JLabel("<html>Welcome Human, You find yourself stuck in a maze...can you find your way out?</html>", SwingConstants.CENTER);
		introLabel.setBorder(new EmptyBorder(0, 20, 0, 20));
		JPanel centerPanel = new JPanel();
		GridLayout layout1 = new GridLayout(3,1);
		layout1.setVgap(13);
		centerPanel.setLayout(layout1);
		centerPanel.setBorder(new EmptyBorder(0, 40, 0, 40));

		//Name section
		JPanel userNamePanel = new JPanel();
		userNamePanel.setLayout(new GridLayout(1, 2));
		JLabel userNameLabel = new JLabel("Enter Your Name: ");
		JTextField userNameTextField = new JTextField(15);
		userNamePanel.add(userNameLabel);
		userNamePanel.add(userNameTextField);
		
		//Maze size section
		String[] possibleMazeSizes = {"5", "10", "15", "20", "25", "30", "50"};
		int[] possibleMazeSizesInt = {5,10,15,20,25, 30, 50};
		int[] possibleMazeWallPercentagesInt = {10,15,30,45,60};


		JPanel mazeSizePanel = new JPanel();
		GridLayout layout = new GridLayout(1,4);
		layout.setHgap(65);
		layout.setVgap(25);
		mazeSizePanel.setLayout(layout);
		JLabel mazeSizeLabelHeight = new JLabel("Maze Height: ");
		JLabel mazeSizeLabelWidth = new JLabel("Maze Width: ");
		JComboBox mazeSizeBoxHeight = new JComboBox(possibleMazeSizes);
		JComboBox mazeSizeBoxWidth = new JComboBox(possibleMazeSizes);
		mazeSizePanel.add(mazeSizeLabelWidth);
		mazeSizePanel.add(mazeSizeBoxWidth);
		mazeSizePanel.add(mazeSizeLabelHeight);
		mazeSizePanel.add(mazeSizeBoxHeight);

		//Maze Walls section
		JPanel mazeWallPanel = new JPanel();
		JLabel mazeWallLabel = new JLabel("Percentage of walls: ");
		mazeWallPanel.setLayout(new GridLayout(1, 2));
		String[] possibleMazeWallPercentage = {"10", "15", "30", "45", "60"};
		JComboBox possibleMazeWallPercentageComboBox = new JComboBox(possibleMazeWallPercentage);
		mazeWallPanel.add(mazeWallLabel);
		mazeWallPanel.add(possibleMazeWallPercentageComboBox);
		
		//Initializing the JFrame information
		JFrame informationFrame = new JFrame();
		informationFrame.setTitle("A-maze-ing world of gumball");
		informationFrame.setSize(600, 260);
		informationFrame.setLayout(new BorderLayout(10, 20));
		informationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Start game button
		JButton startGame = new JButton("Start Game");
		startGame.addActionListener(new ActionListener () {		
			public void actionPerformed(ActionEvent e) {	
				userName = userNameTextField.getText();
				mazeColumn = possibleMazeSizesInt[mazeSizeBoxHeight.getSelectedIndex()];
				mazeRows = possibleMazeSizesInt[mazeSizeBoxWidth.getSelectedIndex()];
				mazeWallPercentage = possibleMazeWallPercentagesInt[possibleMazeWallPercentageComboBox.getSelectedIndex()];
				
				//Checking if user put in a name
				if(userName.trim().isEmpty()) {
					userNameLabel.setForeground(Color.RED);
				}else {
					informationFrame.setVisible(false);
					JOptionPane.showMessageDialog(null, "Welcome to the Maze! The Start position is the red cube and the end position is the green cube. Click buttons adjacent to your current position to move.");
					new Project2();
				}
			}
		});
		
		
		//Top panel
		informationFrame.add(introLabel, BorderLayout.NORTH);
		
		//Center Panel
		centerPanel.add(userNamePanel);
		centerPanel.add(mazeSizePanel);
		centerPanel.add(mazeWallPanel);
		//Adding information to be stored
		informationFrame.add(centerPanel, BorderLayout.CENTER);
		informationFrame.add(startGame, BorderLayout.SOUTH);
		informationFrame.setVisible(true);
	}
	
	//Name: Restart game
	//Output: None
	//Input: None
	//Purpose: Restart the game
	public static void restartGame() {
		mainGameFrame.setVisible(false);
		//Resetting variables
		currentPositionColumn = 0;
		currentPositionRow = 0;
		pathRow = pathColumn = endPathRow = computersMoves = currentUserMoves = userPositionRow= userPositionColumn = 0;
		usersMoves.clear();
		new Project2();
	}
	
	//Build the top section of the Maze panel
	public static JPanel buildMazePanelTop() {
		JPanel topMazePanel = new JPanel();
		JLabel userNameLabel = new JLabel("Current Player: "+userName);
		topMazePanel.setLayout(new GridLayout(2,1));
		topMazePanel.add(userNameLabel);
		return topMazePanel;
	}
	
	//Build the center section of the maze panel
	public static JPanel buildMazePanelCenter() {
//		KEY LISTENER
		class ArrowKeyListener implements KeyListener {
		    @Override
		    public void keyTyped(KeyEvent e) {
		    	System.out.println("BAAK");
		        // This method is not commonly used for arrow key input.
		    }

		    @Override
		    public void keyPressed(KeyEvent e) {
		        int keyCode = e.getKeyCode();
		        if (keyCode == KeyEvent.VK_LEFT) {
		        	mazeButtonsArray[userPositionRow][userPositionColumn - 1].doClick();
		        	System.out.println("left clicked");
		        } else if (keyCode == KeyEvent.VK_RIGHT) {
		        	mazeButtonsArray[userPositionRow][userPositionColumn + 1].doClick();
		        } else if (keyCode == KeyEvent.VK_UP) {
		        	mazeButtonsArray[userPositionRow - 1][userPositionColumn].doClick();
		        	System.out.println("up clicked");
		        } else if (keyCode == KeyEvent.VK_DOWN) {
		        	mazeButtonsArray[userPositionRow + 1][userPositionColumn].doClick();
		        	System.out.println("down clicked");
		        }
		    }

		    @Override
		    public void keyReleased(KeyEvent e) {
		        // This method is not commonly used for arrow key input.
		    }
		}
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(mazeRows, mazeColumn));
		mazeButtonsArray = new JButton[mazeRows][mazeColumn];
		for(int i = 0; i < mazeRows; i++) {
			for(int j = 0; j < mazeColumn; j++) {
			mazeButtonsArray[i][j] = new JButton();
			mazeButtonsArray[i][j].setName(""+i+"-"+ j + "-");
			mazeButtonsArray[i][j].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				JButton buttonClicked = (JButton)e.getSource();
//				Icon icon = new ImageIcon("C:\\Users\\oladi\\Downloads\\black-circle-th.png");
//				buttonClicked.setIcon(icon);
				int buttonPositionRow;
				int buttonPositionColumn;
				//If the user clicks the winning box
				buttonPosition = buttonClicked.getName();
				//Setting a delimiter to make get the column and row of the selected buttons based on
				//where the user clicked
				Scanner getCoords = new Scanner(buttonPosition);
				getCoords.useDelimiter("-");
				String buttonRow = getCoords.next();
				String buttonColumn = getCoords.next();
				getCoords.close();
				buttonPositionColumn =  Integer.parseInt(buttonColumn);
				buttonPositionRow =  Integer.parseInt(buttonRow);

				//Making sure that the user can move according to where they are 
				//Checking if the spot being clicked isnt pink
				if(((buttonPositionRow == userPositionRow &&
						buttonPositionColumn == userPositionColumn+1)||
						(buttonPositionRow == userPositionRow &&
						buttonPositionColumn == userPositionColumn - 1) || (buttonPositionRow == userPositionRow + 1 && 
						buttonPositionColumn == userPositionColumn)||
						(buttonPositionRow == userPositionRow - 1 && buttonPositionColumn == userPositionColumn))) {
					if((buttonClicked.getBackground().getRGB() != -16711681) ) {	
						//User has moved
						buttonClicked.setBackground(Color.CYAN);
//						System.out.println(buttonClicked.getBackground().getRGB());
						userPositionRow = buttonPositionRow;
						currentUserMoves++;
						userPositionColumn = buttonPositionColumn;
						usersMoves.push(userPositionRow + "-" + userPositionColumn);
					}
					if(buttonClicked.getText().contains("End")){
						currentUserMoves++;
						JOptionPane.showMessageDialog(null, "You have passed the maze! Congratulations!!!");
						JOptionPane.showMessageDialog(null, "You used " + currentUserMoves + ". Compared to the Computer's " + computersMoves);
					}
				}
				centerPanel.requestFocusInWindow();

							}
		});
			
			
			
//			Listen for keyboard events
			centerPanel.add(mazeButtonsArray[i][j]);
			}
		}
		
		centerPanel.setFocusable(true);
		centerPanel.requestFocusInWindow();
		 ArrowKeyListener arrowKeyListener = new ArrowKeyListener();
		 centerPanel.addKeyListener(arrowKeyListener);
		prepareMazeBoard();
		return centerPanel;
	}
	
	public static JPanel buildMazePanelBottom() {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1, 6));
		JButton undoButton = new JButton("Undo");
		JButton restartButton = new JButton("Restart");
		JButton newGameButton = new JButton("New Game");
		JButton showPathButton = new JButton("Show Path");
		JButton exitButton = new JButton("Exit");

		restartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				restartGame();
			}
		});
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				System.exit(0);
			}
		});
		showPathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				for(int i = 0; i < mazeRows; i++) {
					for(int j = 0; j < mazeColumn; j++) {
						if(mazeButtonsArray[i][j].getName().contains("Path")) {
							mazeButtonsArray[i][j].setBackground(Color.GREEN);
							//End game
						};
					}
				}
//				JOptionPane.showMessageDialog(null, "Since you have seen the solution, The game will be restarting now");
//				restartGame();
			}
		});
		
		undoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				if(usersMoves.size() > 1) {
				String mostRecentMove = (String) usersMoves.peek();
				Scanner getCoords = new Scanner(mostRecentMove);
				getCoords.useDelimiter("-");
				int buttonRow = Integer.parseInt(getCoords.next());
				int buttonColumn = Integer.parseInt(getCoords.next());
				mazeButtonsArray[buttonRow][buttonColumn].setBackground(Color.white);
				usersMoves.pop();
				mostRecentMove = (String) usersMoves.peek();
				getCoords = new Scanner(mostRecentMove);
				getCoords.useDelimiter("-");
				buttonRow = Integer.parseInt(getCoords.next());
				buttonColumn = Integer.parseInt(getCoords.next());
				userPositionColumn = currentPositionColumn = buttonColumn;
				userPositionRow = currentPositionRow = buttonRow;
			}
			}
		});
		
		newGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainGameFrame.setVisible(false);
				getGameInformation();
				currentPositionColumn = 0;
				currentPositionRow = 0;
				pathRow = pathColumn = endPathRow = computersMoves = currentUserMoves = userPositionRow= userPositionColumn = 0;
				usersMoves.clear();
			}
		});
		bottomPanel.add(undoButton);
		bottomPanel.add(restartButton);
		bottomPanel.add(showPathButton);
		bottomPanel.add(newGameButton);
		bottomPanel.add(exitButton);
	
		return bottomPanel;
	}
	

	//Craft a path for the computer to solve the maze
	public static boolean craftPath(int startingRowIndex, int startingColumnIndex, int endingColumnIndex) {
		//If it is 0, move row, if it is 1, move column
		int movePositionChoice = (int)Math.floor(Math.random() * 10);
		//Move row
		 if(moveColumnAgain){
			rowDirection = 0;
			pathColumn++;	
			moveColumnAgain = false;
			computersMoves++;
		}else if(movePositionChoice < 7) {
			if(moveRowFromTopEdge && pathRow < mazeRows - 1) {
				pathRow++;
				moveColumnAgain = true;
				moveRowFromTopEdge= false;
			} else if(moveRowFromBottomEdge && pathRow > 1) {
				pathRow--;
				moveColumnAgain = true;
				moveRowFromBottomEdge=false;			
			}else if(pathRow == 0) {
				pathColumn++;
				moveColumnAgain = true;
				moveRowFromTopEdge = true;
			} else if (pathRow == mazeRows - 1) {
				moveRowFromBottomEdge = true;
				pathColumn++;
			} else {
				if(pathRow != mazeRows && pathRow >= 0)
				if(rowDirection == 1) {
					pathRow--;
				} else if(rowDirection == -1) {
					pathRow++;
				}else if((int)(Math.random() * 10) < 5){
					rowDirection = 1;
					pathRow--;
				} else {
					rowDirection = -1;
					pathRow++;
				}	
			}
			computersMoves++;		
		} else {
			pathColumn++;	
			moveColumnAgain = true;
			rowDirection = 0;
			computersMoves++;
		}
		String placeholder = mazeButtonsArray[pathRow][pathColumn].getName();
		mazeButtonsArray[pathRow][pathColumn].setName(placeholder+"Path");;
		if(pathColumn == endingColumnIndex) {
			endPathRow = pathRow;
			return false;
		}

		return true;
		
	}
	
	
	public static void prepareMazeBoard() {
		//Column is always going to be 0 
		int startPosition = (int) Math.floor(Math.random() * mazeRows);
		usersMoves.push(startPosition + "-" + 0);
		mazeButtonsArray[startPosition][0].setBackground(Color.CYAN);
		mazeButtonsArray[startPosition][0].setText("Start");
		pathRow = startPosition;
		userPositionRow = startPosition;
		pathColumn = 0;
		currentPositionRow = startPosition;
		currentPositionColumn = 0;
		//Get the walls 
		double numberOfWallsCalc = (((double)mazeWallPercentage/100.0) * ( mazeColumn * mazeRows ));
		int numberOfWalls = (int) Math.ceil((int)numberOfWallsCalc);
	   //Get ending value and check if a win is possible
		boolean keepLooping = true;
		
		//Crafting a path
		while(keepLooping) {
			keepLooping = craftPath(startPosition, 0, mazeColumn -1 );
		}
		
		for(int i = 0; i < mazeRows; i++) {
			for(int j = 0; j < mazeColumn; j++) {
				if(mazeButtonsArray[i][j].getText() != "Start" && mazeButtonsArray[i][j].getText() != "End"
						+ "")
				mazeButtonsArray[i][j].setBackground(Color.white);
				if((i == 0 || j==0 || j == mazeRows - 1 || i == mazeRows - 1 ) && mazeButtonsArray[i][j].getText() != "Start" && mazeButtonsArray[i][j].getText() != "End" && !mazeButtonsArray[i][j].getName().contains("Path")){
					mazeButtonsArray[i][j].setBackground(Color.LIGHT_GRAY);
					mazeButtonsArray[i][j].setName("Wall");
					mazeButtonsArray[i][j].setEnabled(false);
				}
			}
		}
		
		
		mazeButtonsArray[endPathRow][mazeColumn - 1].setBackground(Color.GREEN);
		mazeButtonsArray[endPathRow][mazeColumn - 1].setText("End");
		
		for(int i = 0; i < numberOfWalls; i++) {
			double randomHeight = Math.floor(Math.random() * mazeColumn);
			double randomWidth = Math.floor(Math.random() * mazeRows);

			//Checking if a wall is not the start button, end button, or wall
			JButton potentialWall = mazeButtonsArray[(int)randomWidth][(int)randomHeight];
			if(potentialWall.getText() == "Start" || potentialWall.getText() == "End" || potentialWall.getName() == "Wall" || potentialWall.getName().contains("Path")) {
				i--;
			}else {
				mazeButtonsArray[(int)randomWidth][(int)randomHeight].setBackground(Color.LIGHT_GRAY);
				mazeButtonsArray[(int)randomWidth][(int)randomHeight].setName("Wall");
				mazeButtonsArray[(int)randomWidth][(int)randomHeight].setEnabled(false);
			}
		}
	}
	
	//Building a constructor to create the game board
	public Project2(){
		mainGameFrame = new JFrame();
		mainGameFrame.setTitle("A-maze-ing world of gumball");		
//		mainGameFrame.setSize(mazeRows > 10 ? mazeRows*50 : mazeRows*70 , mazeColumn > 10 ? mazeColumn * 50 : mazeColumn * 70 ) ;
//		mainGameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		mainGameFrame.setSize(1100,900) ;

		mainGameFrame.setLayout(new BorderLayout(0, 0));
		mainGameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		JPanel topMazePanel = buildMazePanelTop();
		JPanel centerMazePanel = buildMazePanelCenter();
		JPanel bottomMazePanel = buildMazePanelBottom();
		mainGameFrame.add(topMazePanel, BorderLayout.NORTH);
		mainGameFrame.add(centerMazePanel, BorderLayout.CENTER);
		mainGameFrame.add(bottomMazePanel, BorderLayout.SOUTH);
		mainGameFrame.setVisible(true);
	}

	public static void main(String[] args) {
		getGameInformation();
	}

}
