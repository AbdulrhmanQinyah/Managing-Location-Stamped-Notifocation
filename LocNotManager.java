import java.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Scanner;

public class LocNotManager {
	private static FileWriter br;

	// Load notifications from file. Assume format is correct. The notifications are
	// indexed by latitude then by longitude.
	public static Map<Double, Map<Double, LocNot>> load(String fileName) throws IOException {

		Map<Double, Map<Double, LocNot>> maap = new BST<Double, Map<Double, LocNot>>();
		double lott, longtt;
		int mxnbrp, nbr;
		int count = 0;
		String txt;
		try {
			Scanner fif = new Scanner(new File(fileName));
			while (fif.hasNext()) {
				lott = fif.nextDouble();
				longtt = fif.nextDouble();
				mxnbrp = fif.nextInt();
				nbr = fif.nextInt();
				txt = fif.nextLine().substring(1);
				LocNot l = new LocNot(txt, lott, longtt, mxnbrp, nbr);
				addNot(maap, l);
			}
			fif.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return maap;
	}

	// Save notifications to file.
	public static void save(String fileName, Map<Double, Map<Double, LocNot>> nots) {
		int count = 0;
		List<LocNot> lol = new LinkedList<LocNot>();
		try {
			br = new FileWriter(fileName);
			if (!lol.empty()) {
				while (!lol.last()) {
					count++;
					lol.findNext();
				}
				count++;
				for (int i = 0; i < count; i++) {
					br.write(lol.retrieve().toString());
					lol.findNext();
				}
				br.write(lol.retrieve().toString());
			}

		} catch (IOException iio) {
			iio.printStackTrace();
		}

	}

	// Return all notifications sorted first by latitude then by longitude.
	public static List<LocNot> getAllNots(Map<Double, Map<Double, LocNot>> nots) {
		List<Pair<Double, Map<Double, LocNot>>> l = new LinkedList<Pair<Double, Map<Double, LocNot>>>();
		List<Pair<Double, LocNot>> l3 = new LinkedList<Pair<Double, LocNot>>();
		List<LocNot> l5 = new LinkedList<LocNot>();

		if (nots.empty()) {
			return l5;
		}
		l = nots.getAll();
		if (l.empty()) {
			return l5;
		}
		l.findFirst();
		while (!l.last()) {
			l3 = l.retrieve().second.getAll();
			l3.findFirst();
			while (!l3.last()) {
				l5.insert(l3.retrieve().second);
				l3.findNext();
			}
			l5.insert(l3.retrieve().second);
			l.findNext();

		}
		l3 = (l.retrieve().second.getAll());
		l3.findFirst();
		while (!l3.last()) {
			l5.insert(l3.retrieve().second);
			l3.findNext();
		}
		l5.insert(l3.retrieve().second);
		return l5;
	}

	// Add a notification. Returns true if insert took place, false otherwise.
	public static boolean addNot(Map<Double, Map<Double, LocNot>> nots, LocNot not) {

		Map<Double, LocNot> map = new BST<Double, LocNot>();
		map.insert(not.getLng(), not);
		if (nots.insert(not.getLat(), map)) {
			return true;
		} else {
			nots.find(not.getLat());
			if (nots.retrieve().insert(not.getLng(), not)) {
				return true;
			} else {
				return false;
			}
		}

	}

	// Delete the notification at (lat, lng). Returns true if delete took place,
	// false otherwise.
	public static boolean delNot(Map<Double, Map<Double, LocNot>> nots, double lat, double lng) {
		Map<Double, LocNot> map = new BST<Double, LocNot>();
		int f = 0;
		if (nots.find(lat)) {
			map = nots.retrieve();
			if (map.remove(lng)) {
				f = 1;
			} else {
				f = 0;
			}
			if (map.empty()) {
				nots.remove(lat);
			}
		}
		if (f == 1) {
			return true;
		} else {
			return false;
		}
	}

	// Return the list of notifications within a square of side dst (in meters)
	// centered at the position (lat, lng) (it does not matter if the notification
	// is active or not). Do not call Map.getAll().
	public static List<LocNot> getNotsAt(Map<Double, Map<Double, LocNot>> nots, double lat, double lng, double dst) {
		double angle = GPS.angle(dst) / 2;

		List<Pair<Double, Map<Double, LocNot>>> ltl = new LinkedList<Pair<Double, Map<Double, LocNot>>>();

		List<LocNot> l5 = new LinkedList<LocNot>();
		ltl = nots.getRange(lat - angle, lng + angle);

		int count = 0;

		if (ltl.empty()) {
			return l5;
		}
		ltl.findFirst();
		while (!ltl.last()) {
			count++;
			ltl.findNext();
		}
		count++;
		ltl.findFirst();
		for (int i = 0; i < count; i++) {
			List<Pair<Double, LocNot>> lngl = new LinkedList<Pair<Double, LocNot>>();
			lngl = ltl.retrieve().second.getRange(lng - angle, lng + angle);
			if (!lngl.empty()) {
				lngl.findFirst();
				while (!lngl.last()) {
					l5.insert(lngl.retrieve().second);
					lngl.findNext();
				}
				l5.insert(lngl.retrieve().second);

			}
			ltl.findNext();
		}

		return l5;
	}

	// Return the list of active notifications within a square of side dst (in
	// meters) centered at the position (lat, lng). Do not call Map.getAll().
	public static List<LocNot> getActiveNotsAt(Map<Double, Map<Double, LocNot>> nots, double lat, double lng,
			double dst) {
		double angle = GPS.angle(dst) / 2;
		List<Pair<Double, Map<Double, LocNot>>> ltl = new LinkedList<Pair<Double, Map<Double, LocNot>>>();
		List<LocNot> l5 = new LinkedList<LocNot>();
		ltl = nots.getRange(lat - angle, lng + angle);
		int count = 0;
		ltl.findFirst();
		while (!ltl.last()) {
			count++;
			ltl.findNext();
		}
		count++;
		ltl.findFirst();
		for (int i = 0; i < count; i++) {
			List<Pair<Double, LocNot>> lngl = new LinkedList<Pair<Double, LocNot>>();
			lngl = ltl.retrieve().second.getRange(lng - angle, lng + angle);
			if (!lngl.empty()) {
				lngl.findFirst();
				int lngf = 1;
				while (lngf == 1) {
					if (lngl.last()) {
						lngf = 0;
					}
					if (lngl.retrieve().second.isActive()) {
						l5.insert(lngl.retrieve().second);
						lngl.findNext();
					} else {
						lngl.findNext();
					}
				}
			}
			ltl.findNext();
		}

		return l5;
	}

	// Perform task of any active notification within a square of side dst (in
	// meters) centered at the position (lat, lng) (call method perform). Do not
	// call Map.getAll().
	public static void perform(Map<Double, Map<Double, LocNot>> nots, double lat, double lng, double dst) {
		List<LocNot> not = new LinkedList<LocNot>();
		not = getNotsAt(nots, lat, lng, dst);
		if (nots.empty()) {
			return;
		}
		int count = 0;
		if (not.empty()) {
			return;
		}
		not.findFirst();
		while (!not.last()) {
			count++;
			not.findNext();
		}
		count++;
		not.findFirst();
		for (int i = 0; i < count; i++) {
			if (not.retrieve().isActive()) {
				not.retrieve().perform();
				not.findNext();
			} else {
				not.findNext();
			}
		}

	}

	// Return a map that maps every word to the list of notifications in which it
	// appears. The list must have no duplicates.
	public static Map<String, List<LocNot>> index(Map<Double, Map<Double, LocNot>> nots) {
		List<LocNot> not = new LinkedList<LocNot>();
		int count = 0;
		if (nots.empty()) {
			return null;
		}
		not = getAllNots(nots);
		Map<String, List<LocNot>> map = new BST<String, List<LocNot>>();
		if (not.empty()) {
			return null;
		} else {
			not.findFirst();
			while (!not.last()) {
				count++;
				not.findNext();
			}
			count++;
			not.findFirst();
			for (int i = 0; i < count; i++) {
				String gl = not.retrieve().getText();
				for (String index : gl.split(" ")) {
					if (map.find(index)) {
						map.retrieve().insert(not.retrieve());
					} else {
						List<LocNot> tr = new LinkedList<LocNot>();
						tr.insert(not.retrieve());
						map.insert(index, tr);
					}
				}
				not.findNext();

			}
		}
		return map;
	}

	// Delete all notifications containing the word w.
	public static void delNots(Map<Double, Map<Double, LocNot>> nots, String w) {
		List<Pair<Double, Map<Double, LocNot>>> l = new LinkedList<Pair<Double, Map<Double, LocNot>>>();
		List<LocNot> ln = new LinkedList<LocNot>();
		Map<String, List<LocNot>> lol = new BST<String, List<LocNot>>();
		lol = index(nots);
		if (!lol.find(w)) {
			return;
		}
		int count = 0;
		ln = lol.retrieve();
		if (ln.empty()) {
			return;
		}
		while (!ln.last()) {
			count++;
			ln.findNext();
		}
		count++;
		ln.findFirst();
		for (int i = 0; i < count; i++) {
			delNot(nots, ln.retrieve().getLat(), ln.retrieve().getLng());
			ln.findNext();
		}

	}

	// Print a list of notifications in the same format used in file.
	public static void print(List<LocNot> l) {
		System.out.println("-------------------------------------------------------------------------------------");
		if (!l.empty()) {
			l.findFirst();
			while (!l.last()) {
				System.out.println(l.retrieve());
				l.findNext();
			}
			System.out.println(l.retrieve());
		} else {
			System.out.println("Empty");
		}
		System.out.println("------------------");
	}

	// Print an index.
	public static void print(Map<String, List<LocNot>> ind) {
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		List<Pair<String, List<LocNot>>> l = ind.getAll();
		if (!l.empty()) {
			l.findFirst();
			while (!l.last()) {
				System.out.println(l.retrieve().first);
				print(l.retrieve().second);
				l.findNext();
			}
			System.out.println(l.retrieve().first);
			print(l.retrieve().second);
		} else {
			System.out.println("Empty");
		}
		System.out.println("++++++++++++++++++");
	}

}