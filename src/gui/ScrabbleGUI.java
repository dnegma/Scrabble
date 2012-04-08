package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import player.Player;
import board.Board;
import board.Square;

public class ScrabbleGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int SQUARE_SIZE = 10;

	Board board;
	Player player1;
	Player player2;

	JPanel scorePanel = new JPanel();
	JLabel score1Label = new JLabel();
	JLabel score2Label = new JLabel();

	JPanel gridPanel = new JPanel();
	JLabel[][] grid = new JLabel[Board.BOARD_SIZE][Board.BOARD_SIZE];
	JPanel[][] squares = new JPanel[Board.BOARD_SIZE][Board.BOARD_SIZE];

	public ScrabbleGUI(Board board, Player player1, Player player2) {
		setLayout(new BorderLayout());
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation(screenSize.width / 4, screenSize.height / 4);

		this.board = board;
		this.player1 = player1;
		this.player2 = player2;
		initGUI();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}

	private void initGUI() {
		initGrid();
		initScorePanel();
		add(gridPanel);
		add(scorePanel, BorderLayout.SOUTH);
	}

	private void initScorePanel() {
		scorePanel.setLayout(new GridLayout(1, 2));
		score1Label.setText(player1.getClass().getName());
		score2Label.setText(player2.getClass().getName());
		scorePanel.add(score1Label);
		scorePanel.add(score2Label);
	}

	private void initGrid() {
		gridPanel.setLayout(new GridLayout(Board.BOARD_SIZE, Board.BOARD_SIZE));
		for (int row = 0; row < Board.BOARD_SIZE; row++) {
			for (int column = 0; column < Board.BOARD_SIZE; column++) {
				Square square = board.getSquare(row, column);
				JPanel panel = new JPanel();
				JLabel label = new JLabel(getContent(square));
				label.setBackground(getSquareColor(square));
				panel.setBackground(getSquareColor(square));
				panel.setSize(50, 50);
				panel.setBorder(BorderFactory.createEtchedBorder());
				panel.add(label);
				squares[row][column] = panel;
				grid[row][column] = label;
				gridPanel.add(panel);
			}
		}
	}

	public Color getSquareColor(Square square) {
		char content = square.getContent();
		switch (content) {
		case Square.THREE_WORD_BONUS:
			return Color.RED;
		case Square.TWO_WORD_BONUS:
			return new Color(255, 228, 150);
		case Square.THREE_LETTER_BONUS:
			return Color.CYAN;
		case Square.TWO_LETTER_BONUS:
			return new Color(100, 149, 237);
		}
		return new Color(211, 211, 211);
	}

	private String getContent(Square square) {
		char content = square.getContent();
		if (square.containsLetter()) {
			return ""+content;
		} else {
			switch(content) {
			case Square.THREE_WORD_BONUS : 
				return "3W";
			case Square.TWO_WORD_BONUS :
				return "2W";
			case Square.THREE_LETTER_BONUS :
				return "3L";
			case Square.TWO_LETTER_BONUS :
				return "2L";
			}
		}
		return "";
	}

	public void updateScores(Player player, int turn) {
		String text = (turn < 0) ? player1.getClass().getSimpleName() : player2
				.getClass().getSimpleName();
		text = text + ": " + player.getScore() + " points!";
		if (turn < 0)
			score1Label.setText(text);
		else
			score2Label.setText(text);
	}
	public void updateBoard() {
		for (int row = 0; row < Board.BOARD_SIZE; row++) {
			for (int column = 0; column < Board.BOARD_SIZE; column++) {
				JLabel label = grid[row][column];
				Square square = board.getSquare(row, column);
				if (square.containsLetter())
					squares[row][column].setBackground(Color.WHITE);
				label.setText(getContent(square));
			}
		}
	}

}
