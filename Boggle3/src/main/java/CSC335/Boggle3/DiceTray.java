package CSC335.Boggle3;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Model the tray of dice in the game Boggle. A DiceTray can be constructed with
 * a 4x4 array of characters for testing.
 *
 * A 2nd constructor with no arguments can be added later to simulate the
 * shaking of 16 dice and selection of one side.
 *
 * @author CHRIS CASTILLO
 */
public class DiceTray {

	private char[][] theBoard;
	private boolean[][] visited;
	private HashSet<String> wordBank;

	/*
	 * Construct a DiceTray object using a hard-coded 2D array of chars. One is
	 * provided in the given unit test. You can create others.
	 */
	public DiceTray(char[][] newBoard) {
		theBoard = newBoard;
		visited = new boolean[4][4];
		wordBank = new HashSet<String>();
	}

	/*
	 * 2nd Constructor that builds a Boggle DiceTray of 16 random die
	 */
	public DiceTray() {
		char[] buildBoard = rollDiceRandomize();
		theBoard = buildBoggleBoard(buildBoard);
		visited = new boolean[4][4];
		wordBank = new HashSet<String>();
	}

	/*
	 * Takes in an char[] that has all 16 dice roll results Then it iterates through
	 * the 4x4 array and fills each spot
	 */
	private char[][] buildBoggleBoard(char[] rolls) {
		char[][] theBoard = new char[4][4];

		// Starts at 0 and goes to 15.
		int rollIndex = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				theBoard[i][j] = rolls[rollIndex];
				rollIndex++;
			}
		}
		return theBoard;
	}

	/*
	 * It rolls all 16 boggle dice and it takes the roll of each dice and adds it to
	 * a character array to build the BoggleBoard
	 */
	private char[] rollDiceRandomize() {
		// Stores all random dice roll results
		char[] result = new char[16];

		// Checks if the dice has been used. If not, roll and get char.
		boolean[] diceUsed = new boolean[16];
		String[] diceCombos = { "LRYTTE", "ANAEEG", "AFPKFS", "YLDEVR", "VTHRWE", "IDSYTT", "XLDERI", "ZNRNHL",
				"EGHWNE", "OATTOW", "HCPOAS", "NMIHUQ", "SEOTIS", "MTOICU", "ENSIEU", "OBBAOJ" };

		int resultIndex = 0;

		// If any dice have NOT BEEN USED, keep looping through until used
		while (!diceAllUsed(diceUsed)) {
			// Generates random index
			int getRandIndex = ThreadLocalRandom.current().nextInt(0, 16);

			// If the dice chosen has not been used, roll it and get a char.
			if (!diceUsed[getRandIndex]) {
				diceUsed[getRandIndex] = true;
				result[resultIndex] = getRandomChar(diceCombos[getRandIndex]);
				resultIndex++;
			}
		}
		return result;
	}

	// Checks if all the dice have been "rolled"
	private boolean diceAllUsed(boolean[] used) {
		for (boolean diceUsed : used) {
			if (!diceUsed) {
				return false;
			}
		}
		return true;
	}

	// Takes in a Dice (represented as String) and "rolls" dice and gets
	// Random character returned
	private char getRandomChar(String dice) {
		int getRandomIndex = ThreadLocalRandom.current().nextInt(0, dice.length());
		return dice.charAt(getRandomIndex);
	}

	// Method to see if the User entered word shows up in the Boggle Board
	private void isWordInBoard(char[][] board, boolean[][] visited, int i, int j, String attempt, String nextLetter,
			String word) {
		// Marks starting point as Visited
		visited[i][j] = true;
		word = word + board[i][j];

		// If the user word equals the word that we have been recursively building, then
		// add it to wordBank (All UpperCase)
		if (attempt.equals(word.toUpperCase())) {
			wordBank.add(word.toUpperCase());
		}

		// If the nextLetter (IE there are more letters to search through), then
		// continue recursing
		if (nextLetter.length() >= 1) {

			// Goes through ROW and COL (minus 1) to ROW and COL (plus 1) to find all
			// adjacent possible letters
			for (int row = i - 1; row <= i + 1 && row < 4; row++) {
				for (int col = j - 1; col <= j + 1 && col < 4; col++) {

					// If the ROW and COL (non-negative) and its a space that HASNT been visited
					// If the Letter at Board[Row][Col] is equal to the nextLetter we are looking
					// Recurse into the next letter, and add it to our word
					if (row >= 0 && col >= 0 && !visited[row][col]
							&& Character.toUpperCase(board[row][col]) == nextLetter.charAt(0))
						isWordInBoard(board, visited, row, col, attempt, nextLetter.substring(1), word);
				}
			}
		}
		// If length is 0, back out each layer and reset visited to false
		word = "" + word.charAt(word.length() - 1);
		visited[i][j] = false;
	}

	/**
	 * Return true if attempt can found on the board following the rules of Boggle
	 * like the same letter can only be used once.
	 *
	 * @param attempt A word that may be in the board by connecting consecutive
	 *                letters
	 *
	 * @return True if search is found
	 */
	public boolean found(String attempt) {
		// Makes String ALL CAPS for case-insensitive
		attempt = attempt.toUpperCase();

		// Checks for valid word length
		if (attempt.length() < 3 || attempt.length() > 16) {
			return false;
		}

		// Filters out any words like "QIET" or "DISQALIFIED" Special Q vs QU exception
		if (attempt.contains("Q") && !attempt.contains("QU")) {
			return false;
		}

		// If attempt contains a "QU" like "QUIET".
		// Modifies attempt to have JUST Q because Board has only Q spot, not QU.
		// "QUIET" --> "QIET"
		attempt = handleQUBoggleWords(attempt);

		// Beginning of word to pass through recursion
		String wordBeg = "";
		// Loops through ROWS and COLS of the BOARD
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				// If the Letter at ROW and COL == Start of the Letter of Attempt.
				// Find all possible paths for the word
				if (Character.toUpperCase(theBoard[i][j]) == attempt.charAt(0)) {
					isWordInBoard(theBoard, visited, i, j, attempt, attempt.substring(1), wordBeg);
				}
			}
		}
		// Finally checks if the attempt is contained in the wordBank.
		return wordBank.contains(attempt);
	}

	/*
	 * If attempt contains a "QU" like "QUIET". Modifies attempt to have JUST Q
	 * because Board has only Q spot, not QU. "QUIET" --> "QIET"
	 */
	private String handleQUBoggleWords(String attempt) {
		if (attempt.contains("QU")) {
			int start = attempt.indexOf("QU");
			return attempt.substring(0, start) + "Q" + attempt.substring(start + 2);
		}
		return attempt;
	}

	// Overrides string method and allows for DiceTray to be printed in a 4x4 grid
	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (j == 3) {
					if (Character.toUpperCase(theBoard[i][j]) == 'Q') {
						result += "Qu";
					} else {
						result += Character.toUpperCase(theBoard[i][j]);
					}
				} else {
					if (Character.toUpperCase(theBoard[i][j]) == 'Q') {
						result += "Qu  ";
					} else {
						result += Character.toUpperCase(theBoard[i][j]) + "   ";
					}
				}
			}
			if (i != 3) {
				result += "\n";
			}
			result += "\n";
		}
		return result;
	}
}