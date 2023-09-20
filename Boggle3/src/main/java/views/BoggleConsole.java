package views;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

import CSC335.Boggle3.Boggle;

/*
 * Author: Chris Castillo
 * Purpose: A console version of the Boggle Game that takes System.in using a scanner
 * It initializes a Boggle Board, prints it out so the users can see the board.
 * Users can enter word guesses as many times as they want. Once they are satisfied with guessing
 * enter 'ZZ' to quite the boggle and see your Score, Words you got right, words you got wrong,
 * and words you could have found
 */
public class BoggleConsole {
	private static Boggle boggleGame;
	private static HashSet<String> systemInUserWords;

	// Initializes the Console Game
	public static void main(String[] args) {
		setupStartOutput();
		gatherUserInput();
		userWordsInputToBoard();
		calculateScore();
		userWordsCorrectAndIncorrect(boggleGame.getCorrectUserWords(), true, false);
		userWordsCorrectAndIncorrect(boggleGame.getIncorrectUserWords(), false, false);
		allWordsYouCouldHaveFound();
	}

	// Prints out start of the game and BoggleBoard
	private static void setupStartOutput() {
		boggleGame = new Boggle();
		systemInUserWords = new HashSet<>();
		System.out.println("Welcome to Boggle! Here is your Boggle Board:\n");
		System.out.println(boggleGame);
		System.out.print("\n");
		System.out.println("Enter words and type ZZ to quit and calculate score:\n");
	}

	// Users a scanner and takes in all user command line input
	private static void gatherUserInput() {
		Scanner boggleInput = new Scanner(System.in);
		while (true) {
			String word = boggleInput.next();

			// If user enters Sentinel Value ZZ, it quites taking user input
			if (word.equals("ZZ")) {
				System.out.print("\n");
				break;
			}
			systemInUserWords.add(word);
		}
		boggleInput.close();
	}

	// Takes user input from command line and checks if words entered are in the
	// boggleBoard & Dictionary
	private static void userWordsInputToBoard() {
		for (String attempt : systemInUserWords) {
			boggleGame.findWord(attempt);
		}
	}
	
	// Calculates and prints user score based off Boggle score Rules
	private static void calculateScore() {
		int score = boggleGame.calculateScore(boggleGame.getCorrectUserWords());
		System.out.println("Your Score: " + score);
	}
	
	/*
	 * Prints out all words that user entered correctly.
	 * Prints out all words that user entered incorrectly.S
	 * Prints out all possible words in BoggleBoard 
	 */
	private static void userWordsCorrectAndIncorrect(HashSet<String> userWords, boolean correct,
			boolean computerWords) {
		if (!computerWords) {
			if (correct) {
				System.out.println("\nWords You Found:");
				System.out.println("================");
			} else {
				System.out.println("Incorrect Words");
				System.out.println("===============");
			}
		}
		
		// Converts to ArrayList to alphabetize words
		ArrayList<String> words = new ArrayList<>();
		words.addAll(userWords);
		Collections.sort(words);
		
		// Loops through and prints words out. 10 on each line
		int lineCount = 0;
		for (String word : words) {
			if (lineCount == 10) {
				System.out.print(word);
				System.out.print("\n");
				lineCount = 0;
			} else {
				System.out.print(word + " ");
				lineCount++;
			}
		}
		if (!computerWords) {
			System.out.println("\n");
		}
	}
	
	// Prints out all possible words that are in the BoggleBoard
	private static void allWordsYouCouldHaveFound() {
		HashSet<String> words = boggleGame.getAllWordsInBoard();
		words.removeAll(boggleGame.getCorrectUserWords());

		System.out.println("You Could Have Found " + words.size() + " more words.");
		System.out.println("The Computer Found All Your Words Plus These:");
		System.out.println("=============================================\n");

		userWordsCorrectAndIncorrect(words, true, true);
	}
}