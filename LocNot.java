import java.text.DecimalFormat;

public class LocNot {
	private double lat, lng; // Latitude and longitude coordinates in degrees
	private int maxNbRepeats; // Maximum number of times the associated task should be repeated.
	private int nbRepeats; // Actual number of times the task has been repeated.
	private String text; // Text of the note

	public double getLng() {
		return lng;
	}

	public double getLat() {
		return lat;
	}

	public int getMaxNbRepeats() {
		return maxNbRepeats;
	}

	public int getNbRepeats() {
		return nbRepeats;
	}

	public String getText() {
		return text;
	}

	public LocNot(String text, double lat, double lng, int maxNbRepeats, int nbRepeats) {
		this.text = text;
		this.lat = lat;
		this.lng = lng;
		this.maxNbRepeats = maxNbRepeats;
		this.nbRepeats = nbRepeats;
	}

	// Check if the notification is active.
	public boolean isActive() {
		return maxNbRepeats == 0 || nbRepeats < maxNbRepeats;
	}

	// Perform the task. This has effect only if the notification is active, in
	// which case the present method increases the number of repeats and returns
	// true. If the notification is inactive, the method has no effect and returns
	// false.
	public boolean perform() {
		if (maxNbRepeats == 0 || nbRepeats < maxNbRepeats) {
			nbRepeats++;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		DecimalFormat formatter = new DecimalFormat("#.00000"); // We want to display 5 decimals only
		return formatter.format(lat) + "\t" + formatter.format(lng) + "\t" + maxNbRepeats + "\t" + nbRepeats + "\t"
				+ text;
	}
}
