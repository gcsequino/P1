// MazeSolver.java - solution for recursive maze assignment
// Author: Greyson Sequino
// Date:   2/12/2020
// Class:  CS165
// Email:  greyson.sequino@colostate.edu

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class MazeSolver {

    /**
     * Exception thrown when path is complete
     */
    public static class MazeSolvedException extends Exception {
        private static final long serialVersionUID = 1L;

        MazeSolvedException() {
            super("Found the pot of gold!");
        }
    }

    /**
     * Stores the user interface
     */
    private static UserInterface gui;

    /**
     * Data structures for maze
     */
    private static char[][] maze;

    /**
     * This is the starting point for your program.
     * This method calls the loadMaze method to read the maze file. <p>
     * <ol>
     *   <li> Implement the {@link MazeSolver#loadMaze(String)} method first,
     *        if correctly implemented the user interface should display it correctly.
     *   <li> After the <b>//YOUR CODE HERE! comment</b> write code to find the row and column of the leprechaun,
     *        which is then used for the initial call to {@link MazeSolver#findPath(int, int)}.
     *   <li> Since code has been provided to CALL the method {@link MazeSolver#findPath(int, int)},
     *        no other code in the main method is needed.
     * </ol>
     * @param args set run configurations to either choose or change the maze you are using
     * @throws FileNotFoundException exception thrown when program unable to recognize file name
     */
    public static void main(String[] args) throws FileNotFoundException {

        // Load maze from file
        loadMaze(args[0]);

        // Find leprechaun in maze
        int currentRow = -1;
        int currentCol = -1;

        for (int i = 0; i < maze.length; i++) {
        	for (int j = 0; j < maze[i].length; j++)
        		if (maze[i][j] == 'L') { 
        			currentRow = i;
        			currentCol = j;
        		}
        }
        // Debug: System.out.printf("(Row,Col): (%d,%d)",currentRow,currentCol);

        // Instantiate graphical user interface
        gui = new UserInterface(maze);

        try {
            // Solve maze, using recursion
            findPath(currentRow, currentCol);

            // Impossible maze, notify user interface
            gui.sendStatus(CallbackInterface.SearchStatus.PATH_IMPOSSIBLE, -1, -1); // Cannot solve

        } catch (MazeSolvedException e) {

            // Maze solved, exit normally
        }

    }



    /**
     * Reads a maze file into the 2-dimensional array declared as a class variable.
     *
     * @param filename the name of the maze file.
     * @throws FileNotFoundException exception thrown when program unable to recognize file name
     * 
     * Author: Greyson Sequino
     */
    static void loadMaze(String filename) throws FileNotFoundException {
        
    	// Initialize scanner for filename
    	Scanner s = new Scanner(new File(filename));
    	
    	// Get length and width of maze form file
    	int width = s.nextInt();
    	int length = s.nextInt();
    	s.nextLine();
    	//Debug: System.out.println("length =" + length + " Width =" + width);
    	maze = new char[length][width];
    	
    	// Transfer maze from file to array
    	for (int i = 0; i < length; i++) {
    		String line = s.nextLine();
    		//Debug: System.out.println(line);
    		for (int j = 0; j < line.length(); j++) {
    			maze[i][j] = line.charAt(j);
    		}
    	}

    }

    /**
     * This method calls itself recursively to find a path from the leprechaun to the pot of gold. <p>
     * It also notifies the user interface about the path it is searching. <p>
     * Here is a list of the actions taken in findPath, based on the square it is searching:
     * <p>
     * <ul>
     *   <li> If the row or column is not on the board, notify the user interface calling
     *        sendStatus with PATH_ILLEGAL, and return false.
     *   <li> If maze[row][col] == 'G', then you have found a path to the pot of gold, notify the user
     *        interface by calling sendStatus with PATH_COMPLETE and throw the MazeSolvedException to terminate execution.
     *   <li> If maze[row][col] == 'S', then the current square is already part of a valid path,
     *        do not notify the user interface, just return false.
     *   <li> If maze[row][col] == 'W', then the current square is already part of an invalid path,
     *        not notify the user interface, just return false.
     *   <li> If maze[row][col] == '#', then the current square contains a blocking tree,
     *        do not notify the user interface, just return false.
     *   <li> If current square contains a space or 'L', notify the user interface by calling sendStatus with PATH_VALID,
     *        then recursively call findPath with the row and column of the surrounding squares,
     *        in order: UP, RIGHT, DOWN, and LEFT.
     *   <li> If any of the recursive calls return true, return true from the current invocation of findPath,
     *        otherwise notify the user interface by calling sendStatus with PATH_INVALID, and return false.
     * </ul>
     *
     * @param row the current row of the leprechaun
     * @param col the current column of the leprechaun
     * @return true for a valid path, false otherwise
     * @throws MazeSolvedException exception thrown when maze is complete
     * 
     * Author: Greyson Sequino
     */
    public static boolean findPath(int row, int col) throws MazeSolvedException {
    	
    	// Check row and col on board
    	if (row<0||row>maze.length-1||col<0||col>maze[0].length-1) {
    		gui.sendStatus(CallbackInterface.SearchStatus.PATH_ILLEGAL, row, col);
    		return false;
    	}
    	// Check for G
    	else if (maze[row][col] == 'G') {
    		gui.sendStatus(CallbackInterface.SearchStatus.PATH_COMPLETE, row, col);
    		throw new MazeSolver.MazeSolvedException();
    	}
    	// Check for S
    	else if (maze[row][col] == 'S') {
    		return false;
    	}
    	// Check for W
    	else if (maze[row][col] == 'W') {
    		return false;
    	}
    	// Check for #
    	else if (maze[row][col] == '#') {
    		return false;
    	}
    	// Check for _ or L
    	else if (maze[row][col] == ' ' || maze[row][col] == 'L') {
    		gui.sendStatus(CallbackInterface.SearchStatus.PATH_VALID, row, col);
    		// Recursive call up
    		if (findPath(row-1,col))
    			return true;
    		// Recursive call right
    		if (findPath(row,col+1))
    			return true;
    		// Recursive call down
    		if (findPath(row+1,col))
    			return true;
    		// Recursive call left
    		if (findPath(row,col-1))
    			return true;
    		
    		gui.sendStatus(CallbackInterface.SearchStatus.PATH_INVALID, row, col);
    		return false;
    	}
    	return false;
    }

}

	