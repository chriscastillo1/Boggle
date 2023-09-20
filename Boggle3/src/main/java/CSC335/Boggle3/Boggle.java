package CSC335.Boggle3;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

/*
 * Author: Chris Castillo
 * Purpose: Write a Boggle Game that can find all the words the user has entered,
 * keep track of incorrect words, and find all possible words in the board.
 * Function of this class should allow for easy console output or GUI output
 */
public class Boggle {

	private static DiceTray diceTray;
	private HashSet<String> correctUserWords, incorrectUserWords;
	private static HashSet<String> allWordsInBoard, dictionary;
	
	// Constructor that initializes DiceTray with random dice
	public Boggle() {
		diceTray = new DiceTray();
		dictionary = new HashSet<>();
		allWordsInBoard = new HashSet<>();
		correctUserWords = new HashSet<>();
		incorrectUserWords = new HashSet<>();

		findAllWordsInBoard();
	}

	// Constructor that allows custom boggle boards to be inputed
	public Boggle(char[][] board) {
		// Initalizes a new DiceTray
		diceTray = new DiceTray(board);
		dictionary = new HashSet<>();
		allWordsInBoard = new HashSet<>();
		correctUserWords = new HashSet<>();
		incorrectUserWords = new HashSet<>();

		findAllWordsInBoard();
	}

	// Checks whether the word is in the board and dictionary
	public boolean findWord(String attempt) {
		// CASE-inSensitive
		attempt = attempt.toUpperCase();

		if (dictionary.contains(attempt) && diceTray.found(attempt)) {
			correctUserWords.add(attempt);
			return true;
		}

		// If its NOT in dictionary OR diceTray then it is an incorrect word
		incorrectUserWords.add(attempt);
		return false;
	}

	// Returns HashSet of all the correct words the user entered
	public HashSet<String> getCorrectUserWords() {
		return correctUserWords;
	}

	// Returns a HashSet of all the incorrect words the user entered
	public HashSet<String> getIncorrectUserWords() {
		return incorrectUserWords;
	}

	// Returns a HashSet of all words in the board
	public HashSet<String> getAllWordsInBoard() {
		return allWordsInBoard;
	}

	// Finds ALL words in the current Boggle Board
	private static void findAllWordsInBoard() {
		try {
			// Opens BoggleWords.txt
			File wordList = new File("BoggleWords.txt");

			// Initializes Scanner and goes through ENTIRE dictionary
			Scanner reader = new Scanner(wordList);
			while (reader.hasNextLine()) {
				String word = reader.nextLine().toUpperCase();
				
				dictionary.add(word);
				if (diceTray.found(word)) {
					allWordsInBoard.add(word);
				}
			}

			reader.close();

		} catch (FileNotFoundException fileNotFound) {
			fileNotFound.printStackTrace();
		}
	}

	// Calculates Boggle Score of all correct words found by user
	public int calculateScore(HashSet<String> correctWords) {
		int score = 0;
		for (String word : correctWords) {
			if (word.length() == 3 || word.length() == 4) {
				score += 1;
			} else if (word.length() == 5) {
				score += 2;
			} else if (word.length() == 6) {
				score += 3;
			} else if (word.length() == 7) {
				score += 5;
			} else {
				score += 11;
			}
		}
		return score;
	}

	// Prints out Boggle Dice Tray
	@Override
	public String toString() {
		return diceTray.toString();
	}
}