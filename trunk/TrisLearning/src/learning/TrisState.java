package learning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class TrisState implements Serializable {

	private static final double GREEDY_PROBABILITY = 8 / 9;
	private static final boolean ROULETTE = false;
	private static final double K = 2;
	private static final double WIN_REINFORCEMENT = 1000;
	private static final double LOSE_REINFORCEMENT = -1000; // set to 0 if you
															// use roulette

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

	private double reinforcement;

	/**
	 * 
	 * @param state
	 *            The integer state is a representation of the state of the
	 *            tic-tac-toe game. The integer is computed from the grid
	 *            representation taking the sum of the product between the value
	 *            and 3^n, where n represent the position of the value on the
	 *            grid.
	 */
	public TrisState(int state) {
		id = state;
		grid = convertStateToGrid(state);
		initializeTurn();
		reinforcement = calculateReinforcement();
		if (reinforcement != 0) {
			value = reinforcement;
		}
	}

	/**
	 * 
	 * @param state
	 *            The string state is a representation of the state of the
	 *            tic-tac-toe game. The string is composed of 9 characters, each
	 *            of these can be either 'x', 'o' or '-'.
	 */
	public TrisState(String state) {
		grid = convertStateToGrid(state);
		id = convertStateToInt(grid);
		initializeTurn();
		reinforcement = calculateReinforcement();
		if (reinforcement != 0) {
			value = reinforcement;
		}
	}

	private double calculateReinforcement() {
		for (int i = 0; i < 3; i++) {
			if (grid[i][0] == grid[i][1] && grid[i][1] == grid[i][2]
					&& grid[i][0] != 0) {
				return grid[i][0] == 1 ? WIN_REINFORCEMENT : LOSE_REINFORCEMENT;
			}
			if (grid[0][i] == grid[1][i] && grid[1][i] == grid[2][i]
					&& grid[0][i] != 0) {
				return grid[0][i] == 1 ? WIN_REINFORCEMENT : LOSE_REINFORCEMENT;
			}
		}
		if (grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2]
				&& grid[0][0] != 0) {
			return grid[0][0] == 1 ? WIN_REINFORCEMENT : LOSE_REINFORCEMENT;
		}
		if (grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0]
				&& grid[0][2] != 0) {
			return grid[0][2] == 1 ? WIN_REINFORCEMENT : LOSE_REINFORCEMENT;
		}
		return 0;
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

	public double getEligibility() {
		return eligibility;
	}

	public int getId() {
		return id;
	}

	public double getReinforcement() {
		return reinforcement;
	}

	public double getValue() {
		return value;
	}

	private void initializeTurn() {
		int[] count = countSymbols();
		turn = count[0] - count[1] + 1;
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

	private int[][] rotate(int[][] grid) {
		int[][] fgrid = new int[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				fgrid[2 - j][i] = grid[i][j];
			}
		}
		return fgrid;
	}

	private char[][] rotate(char[][] grid) {
		char[][] fgrid = new char[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				fgrid[2 - j][i] = grid[i][j];
			}
		}
		return fgrid;
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

	private char[][] flip(char[][] grid) {
		char[][] fgrid = new char[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				fgrid[i][2 - j] = grid[i][j];
			}
		}
		return fgrid;
	}

	public TrisState getNextBestState(HashMap<Integer, TrisState> map,
			boolean learning) {
		ArrayList<TrisState> states = new ArrayList<TrisState>();
		TrisState bestMax = null;
		TrisState bestMin = null;
		double maxValue = Double.NEGATIVE_INFINITY;
		double minValue = Double.POSITIVE_INFINITY;
		double sum = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (grid[i][j] == 0) {
					int[][] nextGrid = copyArray(grid);
					nextGrid[i][j] = turn;
					TrisState ns = null;
					boolean contains = false;
					for (int h = 0; h < 2; h++) {
						for (int k = 0; k < 4; k++) {
							int id = convertStateToInt(nextGrid);
							if (map.containsKey(id)) {
								ns = map.get(id);
								contains = true;
								break;
							}
							nextGrid = rotate(nextGrid);
						}
						if (contains) {
							break;
						}
						nextGrid = flip(nextGrid);
					}
					if (!contains) {
						int id = convertStateToInt(nextGrid);
						ns = new TrisState(id);
						map.put(id, ns);
					}
					if (ns.value > maxValue) {
						maxValue = ns.value;
						bestMax = ns;
					}
					if (ns.value < minValue) {
						minValue = ns.value;
						bestMin = ns;
					}
					if (!states.contains(ns)) {
						states.add(ns);
						if (turn == 1) {
							sum += ns.getValue();
						} else {
							sum += 2 * WIN_REINFORCEMENT - ns.getValue();
						}
					}
				}
			}
		}
		// for (TrisState trisState : states) {
		// System.out.println(trisState);
		// }
		// System.out.println("---------------");
		if (!ROULETTE || !learning) {
			if (Math.random() < GREEDY_PROBABILITY || !learning) {
				if (turn == 1) {
					return bestMax;
				} else {
					return bestMin;
				}
			}
			return states.get((int) (Math.random() * states.size()));
		} else {
			double[] values = new double[states.size()];
			sum = sum + K * (states.size());
			if (turn == 1) {
				values[0] = (states.get(0).getValue() + K) / sum;
				for (int i = 1; i < values.length; i++) {
					values[i] = ((states.get(i).getValue() + K) / sum)
							+ values[i - 1];
				}
			} else {
				values[0] = (2 * WIN_REINFORCEMENT - states.get(0).getValue() + K)
						/ sum;
				for (int i = 1; i < values.length; i++) {
					values[i] = ((2 * WIN_REINFORCEMENT
							- states.get(i).getValue() + K) / sum)
							+ values[i - 1];
				}
			}
			double rand = Math.random();
			for (int i = 0; i < values.length; i++) {
				if (rand < values[i]) {
					return states.get(i);
				}
			}
			return states.get(states.size() - 1);
		}
	}

	public TrisState makeMove(HashMap<Integer, TrisState> map, int i, int j,
			char[][] currentGrid) {
		int[][] grid = copyArray(this.grid);

		boolean aligned = false;
		char[][] cgrid = getGrid();
		for (int h = 0; h < 2; h++) {
			for (int k = 0; k < 4; k++) {
				if (gridDiff(cgrid, currentGrid) < 1) {
					aligned = true;
					break;
				}
				cgrid = rotate(cgrid);
				int t = i;
				i = 2 - j;
				j = t;
			}
			if (aligned) {
				break;
			}
			cgrid = flip(cgrid);
			j = 2 - j;
		}
		grid[i][j] = turn;

		for (int h = 0; h < 2; h++) {
			for (int k = 0; k < 4; k++) {
				int id = convertStateToInt(grid);
				if (map.containsKey(id)) {
					TrisState ns = map.get(id);
					return ns;
				}
				grid = rotate(grid);
			}
			grid = flip(grid);
		}
		int id = convertStateToInt(grid);
		TrisState ns = new TrisState(id);
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

	public char[][] getAlignedGrid(char[][] currentGrid) {
		char[][] cgrid = getGrid();
		for (int h = 0; h < 2; h++) {
			for (int k = 0; k < 4; k++) {
				if (gridDiff(cgrid, currentGrid) < 2) {
					return cgrid;
				}
				cgrid = rotate(cgrid);
			}
			cgrid = flip(cgrid);
		}
		return cgrid;
	}

	private int gridDiff(char[][] grid1, char[][] grid2) {
		int count = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (grid1[i][j] != grid2[i][j]) {
					count++;
				}
			}

		}
		return count;
	}
}
