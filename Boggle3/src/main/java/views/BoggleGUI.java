package views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import CSC335.Boggle3.Boggle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/*
 * Author: Chris Castillo
 * Purpose: Creates a GUI version of the Game Boggle
 */
public class BoggleGUI extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	// Sets the fonts for the game and board
	Font font = new Font("Lucida Console", 18);
	Font BoggleBoardFont = new Font("Lucida Console", 40);

	// Declares necessary private labels for GUI
	private Boggle game;
	private Button newGameButton = new Button("New Game");
	private Button endGameButton = new Button("End Game");
	private Label attemptLabel = new Label("Enter Attempts Below: ");
	private Label resultLabel = new Label("Game Results: ");
	private Label boggleBoardView = new Label();
	private TextArea attemptBox = new TextArea();
	private TextArea resultOutputView = new TextArea();
	private GridPane boggleWindow;

	@Override
	public void start(@SuppressWarnings("exports") Stage stage) {
		stage.setTitle("Boggle Game");
		game = new Boggle();

		windowLayout();
		registerHandlers();

		Scene scene = new Scene(boggleWindow, 830, 870);

		// Sets the stage to not be resizable
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}

	// Arranges the layout of the Boggle Game GUI
	private void windowLayout() {
		boggleWindow = new GridPane();
		boggleWindow.setHgap(10);
		boggleWindow.setVgap(10);
		boggleWindow.setMinSize(400, 400);
		boggleWindow.setAlignment(Pos.TOP_LEFT);

		// Adds the Buttons New Game / End Game
		boggleWindow.add(newGameButton, 1, 1);
		boggleWindow.add(endGameButton, 2, 1);

		setFonts();

		// Adds the Boggle Board Display
		setupBoggleView();

		// Adds the Attempt Entry View
		setupAttemptView();

		// Adds Results
		setupResultView();
	}

	// Sets the font of the game
	private void setFonts() {
		newGameButton.setFont(font);
		endGameButton.setFont(font);
		attemptLabel.setFont(font);
		resultLabel.setFont(font);
		boggleBoardView.setFont(BoggleBoardFont);
		attemptBox.setFont(font);
		resultOutputView.setFont(font);
	}

	// Sets the font and size of the Boggle Game printout
	private void setupBoggleView() {
		boggleBoardView.setMinSize(400, 400);
		boggleBoardView.setMaxSize(400, 400);
		boggleBoardView.setMouseTransparent(true);

		boggleBoardView.setStyle("-fx-border-style: solid;" + "-fx-border-width: 0.5;" + "-fx-border-color: grey;"
				+ "-fx-background-color: white;");
		boggleBoardView.setAlignment(Pos.CENTER);

		boggleBoardView.setText(game.toString());
		boggleWindow.add(boggleBoardView, 1, 2, 2, 1);
	}

	// Declares the size of the box for users to enter their word attempts
	private void setupAttemptView() {
		boggleWindow.add(attemptLabel, 3, 1, 2, 1);

		attemptBox.setStyle("-fx-border-style: solid;" + "-fx-border-width: 1;" + "-fx-border-color: red;"
				+ "-fx-focus-color: -fx-control-inner-background;"
				+ "-fx-faint-focus-color: -fx-control-inner-background;");
		attemptBox.setWrapText(true);
		attemptBox.setMinSize(400, 400);
		attemptBox.setMaxSize(400, 400);
		boggleWindow.add(attemptBox, 3, 2);
	}

	// Setups the GUI view for the results of the boggle game
	private void setupResultView() {
		resultLabel.setStyle("-fx-padding: 4;");

		boggleWindow.add(resultLabel, 1, 3, 2, 1);

		resultOutputView.setWrapText(true);
		resultOutputView.setEditable(false);

		resultOutputView.setMinSize(810, 350);
		resultOutputView.setMaxSize(810, 350);
		boggleWindow.add(resultOutputView, 1, 4, 4, 1);
	}

	// Handles the action events that New Game and End Game button has
	private void registerHandlers() {
		newGameButton.setOnAction(new startNewGame());
		endGameButton.setOnAction(new endGameButtonResults());
	}

	/*
	 * EventHandler to reset the Boggle Game to a new Fresh State
	 */
	private class startNewGame implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			endGameButton.setDisable(false);
			setNewGame();

			resultLabel.setText("Game Results: ");

			resultOutputView.setText("");
			resultOutputView.setStyle("");

			attemptBox.setText("");
			attemptBox.setEditable(true);
			attemptBox.setMouseTransparent(false);
			attemptBox.setStyle("-fx-border-style: solid;" + "-fx-border-width: 1;" + "-fx-border-color: red;"
					+ "-fx-focus-color: -fx-control-inner-background;"
					+ "-fx-faint-focus-color: -fx-control-inner-background;");
		}

		// Initializes a new boggle game
		private void setNewGame() {
			game = new Boggle();
			boggleBoardView.setText(game.toString());
		}
	}

	/*
	 * EventHandler to calculate score, words missed, words got and words could have
	 * gotten in the Boggle Game.
	 */
	private class endGameButtonResults implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			endGameButton.setDisable(true);
			setAttemptBoxState();
			getUserInput();
			calculateScore();
			resultOutput();
		}
		
		// Gets user input from attempt box and enters it into Boggle Game
		private void getUserInput() {
			String[] userInput = attemptBox.getText().split("[ ,\n] ?");

			for (String word : userInput) {
				game.findWord(word);
			}
		}
		
		// Makes it where attemptbox cannot be edited after game has ended
		private void setAttemptBoxState() {
			attemptBox.setMouseTransparent(true);
			attemptBox.setEditable(false);
			attemptBox.setStyle("-fx-border-style: solid;" + "-fx-border-width: 1.5;" + "-fx-border-color: #028A0F;");
		}
		
		// Calculates user score based off Boggle Game rules
		private void calculateScore() {
			int score = game.calculateScore(game.getCorrectUserWords());
			resultLabel.setText("Game Results: Score of " + score);
		}

		// Sets the output view and shows user entered words, incorrect words, and all
		// words coudlve found and score
		private void resultOutput() {
			String wordsFoundLabel = "Words You Found:\n" + "----------------\n";
			String wrongWordsLabel = "Incorrect Words:\n" + "----------------\n";

			HashSet<String> possibleWords = allPossibleWords();
			String possibleWordsLabel = "You Could Have Found " + possibleWords.size() + " More Words:\n";
			String underlineWord = "-".repeat(possibleWordsLabel.length() - 1);
			possibleWordsLabel += underlineWord + "\n";

			String found = wordsFoundLabel + wordOutputFormat(game.getCorrectUserWords());
			String incorrect = wrongWordsLabel + wordOutputFormat(game.getIncorrectUserWords());
			String possible = possibleWordsLabel + wordOutputFormat(possibleWords);

			resultOutputView
					.setStyle("-fx-border-style: solid;" + "-fx-border-width: 1.5;" + "-fx-border-color: #028A0F;");
			resultOutputView.setText(found + "\n\n" + incorrect + "\n\n" + possible);
		}

		// Calculates and returns all words that the user did not find that were in
		// board
		private HashSet<String> allPossibleWords() {
			HashSet<String> words = game.getAllWordsInBoard();
			words.removeAll(game.getCorrectUserWords());
			return words;
		}

		// Creates a string output format for a hashset of words
		private String wordOutputFormat(HashSet<String> wordList) {
			ArrayList<String> words = new ArrayList<>();
			words.addAll(wordList);
			Collections.sort(words);

			String result = "";

			for (String w : words) {
				result += w + " ";
			}

			return result;
		}
	}
}