package game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import learning.TrisState;

public class Game extends JFrame {

	private static final String KNOWLEDGE_FILE = "tris.dat";
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new Game();
	}

	private JButton b00 = new JButton("(0,0)");
	private JButton b01 = new JButton("(0,1)");
	private JButton b02 = new JButton("(0,2)");
	private JButton b10 = new JButton("(1,0)");
	private JButton b11 = new JButton("(1,1)");
	private JButton b12 = new JButton("(1,2)");
	private JButton b20 = new JButton("(2,0)");
	private JButton b21 = new JButton("(2,1)");
	private JButton b22 = new JButton("(2,2)");

	private JButton[][] grid = { { b00, b01, b02 }, { b10, b11, b12 },
			{ b20, b21, b22 } };

	private HashMap<Integer, TrisState> map = new HashMap<Integer, TrisState>();

	private TrisState state;
	private char[][] currentGrid;

	private int turn;

	public Game() {
		setTitle("Tris");
		setSize(200, 200);
		Dimension dim = getToolkit().getScreenSize();
		setLocation(dim.width / 2 - getWidth() / 2, dim.height / 2
				- getHeight() / 2);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		setLayout(new BorderLayout());

		JPanel grid = new JPanel();
		add(grid, BorderLayout.CENTER);

		grid.setLayout(new GridLayout(3, 3));

		b00.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				makeMove(0, 0);
			}
		});
		grid.add(b00);

		b01.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				makeMove(0, 1);
			}
		});
		grid.add(b01);

		b02.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				makeMove(0, 2);
			}
		});
		grid.add(b02);

		b10.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				makeMove(1, 0);
			}
		});
		grid.add(b10);

		b11.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				makeMove(1, 1);
			}
		});
		grid.add(b11);

		b12.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				makeMove(1, 2);
			}
		});
		grid.add(b12);

		b20.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				makeMove(2, 0);
			}
		});
		grid.add(b20);

		b21.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				makeMove(2, 1);
			}
		});
		grid.add(b21);

		b22.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				makeMove(2, 2);
			}
		});
		grid.add(b22);

		JButton button = new JButton("Nuova partita");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				nextGame();
			}
		});
		add(button, BorderLayout.SOUTH);

		loadKnowledge(KNOWLEDGE_FILE);
		turn = 1;
		nextGame();

		setVisible(true);
	}

	private void loadKnowledge(String fileName) {

		try {
			FileInputStream saveFile = new FileInputStream(fileName);
			DataInputStream save = new DataInputStream(saveFile);
			int id = save.readInt();
			while (id != -1) {
				TrisState s = new TrisState(id);
				s.setValue(save.readDouble());
				s.setEligibility(save.readDouble());
				map.put(id, s);
				id = save.readInt();
			}
			save.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void makeMove(int i, int j) {
		if (grid[i][j].getText().equals("-")) {
			if (!state.isTerminal()) {
				state = state.makeMove(map, i, j, currentGrid);
				currentGrid = state.getAlignedGrid(currentGrid);
				if (!state.isTerminal()) {
					state = state.getNextBestState(map, false);
					currentGrid = state.getAlignedGrid(currentGrid);
					if (state.isTerminal()) {
						if (state.getReinforcement() != 0) {
							JOptionPane.showMessageDialog(this, "Hai perso!!");
						} else {
							JOptionPane.showMessageDialog(this, "Pareggio!!");
						}
					}
				} else {
					if (state.getReinforcement() != 0) {
						JOptionPane.showMessageDialog(this, "Hai vinto!!");
					} else {
						JOptionPane.showMessageDialog(this, "Pareggio!!");
					}
				}
				updateGrid();
			}
		}
	}

	private void nextGame() {
		turn = 1 - turn;
		state = map.get(0);
		currentGrid = state.getGrid();
		if (turn == 1) {
			state = state.getNextBestState(map, false);
			currentGrid = state.getAlignedGrid(currentGrid);
		}
		updateGrid();
	}

	private void updateGrid() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				grid[i][j].setText(String.valueOf(currentGrid[i][j]));
			}
		}
	}
}
