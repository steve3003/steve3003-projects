package learning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class TrisStateSymmetry implements Comparable<TrisStateSymmetry>,
		Serializable {

	// previous version which support tris' symmetry, it needs an update respect
	// to the new code inserted in TDLambda.java

	private static final double GREEDY_PROBABILITY = 0.2;

	/**
	 * 
	 */
	private static final long serialVersionUID = 7335449388882580059L;

	private static final char SYMBOLS[] = { '-', 'x', 'o' };

	private int[][] grid;
	private int id;
	private int turn;
	private double value = 0;
	private double eligibility = 0;

	private int reinforcement;

	/**
	 * 
	 * @param state
	 *            The integer state is a representation of the state of the
	 *            tic-tac-toe game. The integer is computed from the grid
	 *            representation taking the sum of the product between the value
	 *            and 3^n, where n represent the position of the value on the
	 *            grid.
	 */
	public TrisStateSymmetry(int state) {
		id = state;
		grid = convertStateToGrid(state);
		initializeTurn();
		reinforcement = calculateReinforcement();
	}

	/**
	 * 
	 * @param state
	 *            The string state is a representation of the state of the
	 *            tic-tac-toe game. The string is composed of 9 characters, each
	 *            of these can be either 'x', 'o' or '-'.
	 */
	public TrisStateSymmetry(String state) {
		grid = convertStateToGrid(state);
		id = convertStateToInt(grid);
		initializeTurn();
		reinforcement = calculateReinforcement();
	}

	private int calculateReinforcement() {
		for (int i = 0; i < 3; i++) {
			if (grid[i][0] == grid[i][1] && grid[i][1] == grid[i][2]
					&& grid[i][0] != 0) {
				return grid[i][0] != turn ? 1000 : 0;
			}
			if (grid[0][i] == grid[1][i] && grid[1][i] == grid[2][i]
					&& grid[0][i] != 0) {
				return grid[0][i] != turn ? 1000 : 0;
			}
		}
		if (grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2]
				&& grid[0][0] != 0) {
			return grid[0][0] != turn ? 1000 : 0;
		}
		if (grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0]
				&& grid[0][2] != 0) {
			return grid[0][2] != turn ? 1000 : 0;
		}
		int[] count = countSymbols();
		if (count[0] + count[1] == 9)
			return 100;
		return 0;
	}

	@Override
	public int compareTo(TrisStateSymmetry state) {
		int[][] grid = copyArray(state.grid);
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 4; j++) {
				if (id == convertStateToInt(grid)) {
					return 0;
				}
				grid = rotate(grid);
			}
			grid = flip(grid);
		}
		return id - state.id;
	}

	private int[][] convertStateToGrid(int state) {
		int[][] grid = new int[3][3];
		for (int i = 0; i < 9; i++) {
			grid[i / 3][i % 3] = state % 3;
			state /= 3;
		}
		return grid;
	}

	private int[][] convertStateToGrid(String state) {
		int[][] grid = new int[3][3];
		for (int i = 0; i < 9; i++) {
			char c = state.charAt(i);
			if (c == SYMBOLS[2]) {
				grid[i / 3][i % 3] = 2;
			} else if (c == SYMBOLS[1]) {
				grid[i / 3][i % 3] = 1;
			} else {
				grid[i / 3][i % 3] = 0;
			}
		}
		return grid;
	}

	private int convertStateToInt(int[][] grid) {
		int state = 0;
		int[] coefficients = { 1, 3, 9, 27, 81, 243, 729, 2187, 6561 };
		for (int i = 0; i < 9; i++) {
			state += grid[i / 3][i % 3] * coefficients[i];
		}
		return state;
	}

	private String convertStateToString(int[][] grid) {
		String s = "";
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				s += SYMBOLS[grid[i][j]];
			}
		}
		return s;
	}

	private int[] countSymbols() {
		int[] count = { 0, 0 };
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (grid[i][j] != 0)
					count[grid[i][j] - 1]++;
			}
		}
		return count;
	}

	private int[][] flip(int[][] grid) {
		int[][] fgrid = new int[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				fgrid[i][2 - j] = grid[i][j];
			}
		}
		return fgrid;
	}

	public double getEligibility() {
		return eligibility;
	}

	public int getId() {
		return id;
	}

	public int getReinforcement() {
		return reinforcement;
	}

	public double getValue() {
		return value;
	}

	private void initializeTurn() {
		int[] count = countSymbols();
		turn = count[0] - count[1] + 1;
	}

	private int[][] rotate(int[][] grid) {
		int[][] fgrid = new int[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				fgrid[2 - j][i] = grid[i][j];
			}
		}
		return fgrid;
	}

	public void setEligibility(double eligibility) {
		this.eligibility = eligibility;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		String cs = convertStateToString(grid);
		String s = "";
		for (int i = 0; i < cs.length(); i++) {
			s += cs.charAt(i) + (i % 3 == 2 ? "\n" : "");
		}
		return s;
	}

	public boolean isTerminal() {
		for (int i = 0; i < 3; i++) {
			if (grid[i][0] == grid[i][1] && grid[i][1] == grid[i][2]
					&& grid[i][0] != 0) {
				return true;
			}
			if (grid[0][i] == grid[1][i] && grid[1][i] == grid[2][i]
					&& grid[0][i] != 0) {
				return true;
			}
		}
		if (grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2]
				&& grid[0][0] != 0) {
			return true;
		}
		if (grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0]
				&& grid[0][2] != 0) {
			return true;
		}
		int[] count = countSymbols();
		if (count[0] + count[1] == 9)
			return true;
		return false;
	}

	public TrisStateSymmetry getNextBestState(
			HashMap<Integer, TrisStateSymmetry> map, boolean learning) {
		ArrayList<TrisStateSymmetry> states = new ArrayList<TrisStateSymmetry>();
		TrisStateSymmetry best = null;
		double maxValue = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (grid[i][j] == 0) {
					grid[i][j] = turn;
					TrisStateSymmetry ns = null;
					boolean contains = false;
					for (int h = 0; h < 2; h++) {
						for (int k = 0; k < 4; k++) {
							if (!contains) {
								int id = convertStateToInt(grid);
								if (map.containsKey(id)) {
									ns = map.get(id);
									contains = true;
								}
							}
							grid = rotate(grid);
						}
						grid = flip(grid);
					}
					if (!contains) {
						int id = convertStateToInt(grid);
						ns = new TrisStateSymmetry(id);
						map.put(id, ns);
					}
					if (ns.value > maxValue) {
						maxValue = ns.value;
						best = ns;
					}
					grid[i][j] = 0;
					if (!states.contains(ns)) {
						states.add(ns);
					}
				}
			}
		}
		// for (TrisState trisState : states) {
		// System.out.println(trisState);
		// }
		// System.out.println("---------------");
		if (Math.random() < GREEDY_PROBABILITY || !learning) {
			return best;
		}
		return states.get((int) (Math.random() * states.size()));
	}

	public TrisStateSymmetry makeMove(HashMap<Integer, TrisStateSymmetry> map,
			int i, int j) {
		int[][] grid = copyArray(this.grid);

		grid[i][j] = turn;
		TrisStateSymmetry ns = null;
		for (int h = 0; h < 2; h++) {
			for (int k = 0; k < 4; k++) {
				int id = convertStateToInt(grid);
				if (map.containsKey(id)) {
					ns = map.get(id);
					return ns;
				}
				grid = rotate(grid);
			}
			grid = flip(grid);
		}
		int id = convertStateToInt(grid);
		ns = new TrisStateSymmetry(id);
		map.put(id, ns);
		return ns;
	}

	private int[][] copyArray(int[][] grid2) {
		int[][] grid = new int[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				grid[i][j] = grid2[i][j];
			}
		}
		return grid;
	}

	public char[][] getGrid() {
		char[][] cgrid = new char[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				cgrid[i][j] = SYMBOLS[grid[i][j]];
			}
		}
		return cgrid;
	}
}
