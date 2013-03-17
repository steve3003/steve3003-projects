package learning;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class TDLambda {

	private static final int EPISODES = 200000;
	private static final double ALPHA = 0.2;
	private static final double GAMMA = 0.7;
	private static final double LAMBDA = 0;
	private static final boolean LOAD_KNOWLEDGE = false;
	private static final boolean BEST = false;

	public static void main(String[] args) {
		HashMap<Integer, TrisState> map = new HashMap<Integer, TrisState>();
		if (LOAD_KNOWLEDGE) {
			loadKnowledge("tris.dat", map);
		} else {
			map.put(0, new TrisState(0));
		}
		int perc = 0, perc2 = 0;
		for (int i = 0; i < EPISODES; i++) {
			perc2 = 100 * (i + 1) / EPISODES;
			if (perc2 > perc) {
				System.out.println(perc);
				perc = perc2;
			}
			TrisState s = map.get(0);
			do {
				TrisState sp = s.getNextBestState(map, !BEST);
				// System.out.println(sp);
				double delta = sp.getReinforcement() + GAMMA * sp.getValue()
						- s.getValue();
				s.setEligibility(s.getEligibility() + 1);
				for (Integer key : map.keySet()) {
					s = map.get(key);
					s.setValue(s.getValue() + ALPHA * delta
							* s.getEligibility());
					s.setEligibility(GAMMA * LAMBDA * s.getEligibility());
				}
				s = sp;
			} while (!s.isTerminal());
			// for (TrisState state : map.values()) {
			// if (state.getValue() < 0)
			// System.out.println(state.toString() + state.getValue()
			// + "\n");
			// }
		}
		System.out.println("100 done!!");

		// for (TrisState state : map.values()) {
		// System.out.println(state.toString() + state.getValue() + "\n");
		// }

		try {
			FileOutputStream saveFile = new FileOutputStream("tris.dat");

			DataOutputStream save = new DataOutputStream(saveFile);

			for (Integer key : map.keySet()) {
				TrisState s = map.get(key);
				save.writeInt(s.getId());
				save.writeDouble(s.getValue());
				save.writeDouble(s.getEligibility());
			}

			save.writeInt(-1);

			save.flush();
			save.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void loadKnowledge(String fileName,
			HashMap<Integer, TrisState> map) {

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
}
