package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import CSC335.Boggle3.Boggle;

/**
 * Grader tests for Boggle. Just tests for functionality: Boggle Initalization,
 * Custom DiceTray, GetCorrectWords, GetIncorrectWords Calculate Score, Print,
 * and FindAllWords Author: Chris Castillo
 */
class BoggleTest {
	// Random chars for custom boggleBoard
	private char[][] randChars = { { 'R', 'E', 'D', 'M' }, { 'B', 'A', 'N', 'O' }, { 'T', 'M', 'D', 'Q' },
			{ 'L', 'O', 'E', 'V' } };

	Boggle boardScoreAndDupes = new Boggle(randChars);

	@Test
	void testBoggleCreate() {
		Boggle game = new Boggle();

		game.findWord("notpossibleword");
		game.findWord("1234");
		game.findWord("cows123");

		HashSet<String> incorrect = game.getIncorrectUserWords();
		assertEquals(3, incorrect.size());
	}

	@Test
	void testCalculateScoreAndDuplicateAndAllWords() {
		assertTrue(boardScoreAndDupes.findWord("red"));
		assertTrue(boardScoreAndDupes.findWord("ban"));
		assertFalse(boardScoreAndDupes.findWord("ON"));
		assertTrue(boardScoreAndDupes.findWord("red"));
		assertEquals(2, boardScoreAndDupes.calculateScore(boardScoreAndDupes.getCorrectUserWords()));
		assertEquals(124, boardScoreAndDupes.getAllWordsInBoard().size());
	}

	@Test
	void testWordInBoardNotDictionary() {
		Boggle setBoard = new Boggle(randChars);

		assertFalse(setBoard.findWord("MOQU"));
		assertFalse(setBoard.findWord("RADE"));
		assertEquals(0, setBoard.calculateScore(setBoard.getCorrectUserWords()));
	}

	@Test
	void testWordInDictionaryNotInBoard() {
		Boggle setBoard = new Boggle(randChars);

		setBoard.findWord("disqualified");
		assertEquals(0, setBoard.calculateScore(setBoard.getCorrectUserWords()));
	}

	@Test
	void testNoWordsInBoardOrDictionary() {
		Boggle setBoard = new Boggle(randChars);

		setBoard.findWord("123");
		setBoard.findWord("nothereboy");
		assertEquals(0, setBoard.calculateScore(setBoard.getCorrectUserWords()));
	}

	@Test
	void testScoreWithBiggerWords() {
		char[][] randChars = { { 'M', 'A', 'N', 'I' }, { 'E', 'E', 'T', 'L' }, { 'D', 'S', 'P', 'E' },
				{ 'I', 'R', 'E', 'D' } };
		Boggle setBoard = new Boggle(randChars);

		// Total Points 34
		assertTrue(setBoard.findWord("MAN")); // Length 3 - 1pt
		assertTrue(setBoard.findWord("amen")); // length 4 - 1pt
		assertTrue(setBoard.findWord("EdEmA")); // length 5 - 2pt
		assertTrue(setBoard.findWord("deePEn")); // length 6 - 3pt
		assertTrue(setBoard.findWord("STEAMED")); // length 7 - 5pt
		assertTrue(setBoard.findWord("pristine")); // length 8 - 11pt
		assertTrue(setBoard.findWord("presented")); // length 9 - 11pt
		assertEquals(34, setBoard.calculateScore(setBoard.getCorrectUserWords()));
	}

	@Test
	void testWordsWithQ() {
		char[][] randChars = { { 'Q', 'E', 'S', 'M' }, { 'E', 'E', 'T', 'L' }, { 'D', 'S', 'P', 'E' },
				{ 'I', 'R', 'E', 'D' } };
		Boggle setBoard = new Boggle(randChars);
		assertFalse(setBoard.findWord("que"));
		assertFalse(setBoard.findWord("QUE"));
	}

	@Test
	void testBoardPrints() {
		Boggle setBoard = new Boggle(randChars);
		String board = setBoard.toString();
		String result = "R   E   D   M\nB   A   N   O\nT   M   D   Qu\nL   O   E   V";
		assertTrue(result.equals(board));
	}
}
