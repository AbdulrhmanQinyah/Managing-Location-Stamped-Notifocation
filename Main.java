public class Main {

	public static void main(String[] args) {
		Map<Double, Map<Double, LocNot>> nots = new BST<Double, Map<Double, LocNot>>();

		// Add some notifications
		LocNotManager.addNot(nots, new LocNot("Buy water", 24.75365, 46.62900, 0, 0));
		LocNotManager.addNot(nots, new LocNot("Submit PA male", 24.72294, 46.61978, 5, 0));
		LocNotManager.addNot(nots, new LocNot("Submit HW male", 24.72294, 46.61838, 5, 0));
		LocNotManager.addNot(nots, new LocNot("Submit PA female", 24.72328, 46.63640, 5, 4));
		LocNotManager.addNot(nots, new LocNot("Submit HW female", 24.72328, 46.63540, 5, 4));
		LocNotManager.addNot(nots, new LocNot("Ask for room reparations", 24.72062, 46.62248, 1, 0));
		LocNotManager.addNot(nots, new LocNot("Buy notebook", 24.75213, 46.62443, 1, 0));
		LocNotManager.addNot(nots, new LocNot("Buy milk", 24.74203, 46.65809, 0, 0));
		LocNotManager.addNot(nots, new LocNot("Make doctor appointment", 24.71144, 46.62124, 1, 0));

		// Map<Double, Map<Double, LocNot>> nots = LocNotManager.load("input.txt"); //
		// Reading from file

		LocNotManager.save("output.txt", nots); // Write to file

		// Some queries
		LocNotManager.print(LocNotManager.getAllNots(nots)); // Print all notifications

		LocNotManager.print(LocNotManager.getActiveNotsAt(nots, 24.72328, 46.63640, 100000)); // This should print all tasks

		LocNotManager.print(LocNotManager.getActiveNotsAt(nots, 24.72328, 46.63640, 1000)); // This should print the task: Submit PA female and Submit HW female

		LocNotManager.print(LocNotManager.getActiveNotsAt(nots, 24.74203 + GPS.angle(50), 46.65809 - GPS.angle(50), 110)); // This should print the task: Buy milk

		LocNotManager.print(LocNotManager.getActiveNotsAt(nots, 24.72328 + GPS.angle(50), 46.63640, 200)); // This should print the task: Submit PA female

		LocNotManager.perform(nots, 24.72328 + GPS.angle(300), 46.63640, 100);

		LocNotManager.print(LocNotManager.getActiveNotsAt(nots, 24.72328 + GPS.angle(300), 46.63640, 100));// This should be empty

		LocNotManager.print(LocNotManager.getActiveNotsAt(nots, 24.72062, 46.62248, 100)); // This should print the task: Ask for room reparations

		LocNotManager.perform(nots, 24.72062, 46.62248, 100); // This performs the task: Ask for room reparations

		LocNotManager.print(LocNotManager.getActiveNotsAt(nots, 24.72062, 46.62248, 100)); // This should be empty (this task can only be repeated once)

		LocNotManager.print(LocNotManager.index(nots)); // Print index

		LocNotManager.delNots(nots, "Submit");

		LocNotManager.print(LocNotManager.getAllNots(nots)); // Print all notifications (no "Submit" here)

		System.out.println("nbKeyComp: " + nots.nbKeyComp(24.71144)); // Prints: 4

		System.out.println("nbKeyComp (range): " + nots.nbKeyComp(24.71144, 24.75365)); // Prints: 5

		System.out.println("nbKeyComp (range): " + nots.nbKeyComp(24.75365, 24.71144)); // Prints: 1

		System.out.println("nbKeyComp (range): " + nots.nbKeyComp(24.70000, 24.81111)); // Prints: 5

	}
}

