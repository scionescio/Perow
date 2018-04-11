package Unterbringung;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * TODO clean up this whole entire getter/setter bullshit and boolean mess
 */
@SuppressWarnings("unused")
public class Lifeguard implements Comparable<Object> {

	static AtomicInteger ai = new AtomicInteger(1);
	private int id;

	private String name;
	private String bday;
	private int age;
	private String geschlecht;
	private String addresse;

	private LocalDate anreise;
	private LocalDate abreise;

	private Lifeguard mitanreisender;
	private String buddy;

	public String getBuddy() {
		return buddy;
	}

	public void setBuddy(String buddy) {
		this.buddy = buddy;
		this.hasBuddy = true;
	}

	private boolean hasBuddy;
	private boolean housed;
	private Room room;

	public enum Gender {

		Male("m"), Female("w", "f"), Neutral("n", "", " ", null);

		List<String> stringRepresentations;

		private Gender(String... stringRepresentations) {
			this.stringRepresentations = Arrays.asList(stringRepresentations);
		}

		public static Gender stringToGender(String stringInput) {
			for (Gender g : Gender.values()) {
				if (g.stringRepresentations.contains(stringInput)) {
					return g;
				}
			}

			return Neutral;

		}

	}

	/*
	 * TODO validierung
	 */
	public Lifeguard(String name, String bday, String geschlecht, String addresse, String anreise, String abreise) {
		super();
		// this.id = ai.getAndIncrement();
		// if (!name.equals("")) {
		// String[] s = name.split(",");
		// StringBuilder sb = new StringBuilder();
		// sb.append(s[1]).delete(0, 1);
		// sb.append(" ").append(s[0].charAt(0));
		// this.name = sb.toString();
		// } else {
		// this.name = "Max Mustermann";
		// }
		this.name = name;
		this.bday = bday;
		if (bday != null && !bday.equals("")) {
			LocalDate ageHelper = LocalDate.parse(bday, DateTimeFormatter.ofPattern("dd.MM.uuuu"));
			LocalDate now = LocalDate.now();
			this.age = Period.between(ageHelper, now).getYears();
		} else {
			age = 100;
		}
		this.geschlecht = geschlecht;
		this.addresse = addresse;
		this.anreise = LocalDate.parse(anreise, DateTimeFormatter.ofPattern("dd.MM.uuuu"));
		this.abreise = LocalDate.parse(abreise, DateTimeFormatter.ofPattern("dd.MM.uuuu"));
	}

	public Lifeguard(String name, String bday, String geschlecht, String addresse, LocalDate anreise,
			LocalDate abreise) {
		super();
		this.name = name;
		this.bday = bday;
		if (bday != null && !bday.equals("")) {
			LocalDate ageHelper = LocalDate.parse(bday, DateTimeFormatter.ofPattern("dd.MM.uuuu"));
			LocalDate now = LocalDate.now();
			this.age = Period.between(ageHelper, now).getYears();
		} else {
			age = 100;
		}
		this.geschlecht = geschlecht;
		this.addresse = addresse;
		this.anreise = anreise;
		this.abreise = abreise;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();

		s.append("[" + this.getName() + " | " + this.age + " | " + " has Buddy: ");
		if (this.hasBuddy()) {
			s.append(this.mitanreisender.getName());
		} else {
			s.append("false");
		}
		s.append(" |  has Room: " + hasRoom() + " | " + this.anreise + " | " + this.abreise + "]");

		return s.toString();
		// return "[" + this.name + " | " + this.age + " | " + this.geschlecht + " | " +
		// " has Buddy: " + hasBuddy()
		// + " | " + " has Room: " + hasRoom() + " | " + this.anreise + " | " +
		// this.abreise + "]";
	}

	public boolean hasRoom() {
		if (getRoom() != null) {
			return true;
		}
		return false;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	@Override
	/*
	 * TODO Comparator
	 */
	public int compareTo(Object o) {
		Lifeguard d = (Lifeguard) o;
		if (d.hasBuddy() && this.hasBuddy()) {
			return 0;
		} else if (d.hasBuddy()) {
			return 1;
		} else if (this.hasBuddy()) {
			return -1;
		} else if (this.getGeschlecht().equals(d.getGeschlecht())) {
			if (this.getAge() <= d.getAge()) {
				return -1;
			}
		} else if (d.geschlecht.equals("w") && this.geschlecht.equals("w")) {
			return 0;
		} else if (d.geschlecht.equals("w")) {
			return 1;
		} else if (this.geschlecht.equals("w")) {
			return -1;
		}
		return 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {

		return name;
	}

	public String printName() {
		StringBuilder shortName = new StringBuilder();
		String[] a = this.name.split(",");
		if (a.length > 1) {
			shortName.append(a[1]);
			shortName.delete(0, 1);
			shortName.append(" ");
			shortName.append(a[0].charAt(0));
		} else {
			shortName.append(this.name);
		}
		return shortName.toString();

	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBday() {
		return bday;
	}

	public void setBday(String bday) {
		this.bday = bday;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGeschlecht() {
		return geschlecht;
	}

	public void setGeschlecht(String geschlecht) {
		this.geschlecht = geschlecht;
	}

	public String getAddresse() {
		return addresse;
	}

	public void setAddresse(String addresse) {
		this.addresse = addresse;
	}

	public LocalDate getAnreise() {
		return anreise;
	}

	public void setAnreise(LocalDate anreise) {
		this.anreise = anreise;
	}

	public LocalDate getAbreise() {
		return abreise;
	}

	public void setAbreise(LocalDate abreise) {
		this.abreise = abreise;
	}

	public Lifeguard getMitanreisender() {
		return mitanreisender;
	}

	public void setMitanreisender(Lifeguard mitanreisender) {
		this.mitanreisender = mitanreisender;
		this.hasBuddy = true;
	}

	public boolean hasBuddy() {
		return hasBuddy;
	}

	public void setHatMitanreisenden(boolean hatMitanreisenden) {
		this.hasBuddy = hatMitanreisenden;
	}

	public boolean isHoused() {
		return housed;
	}

	public void setUntergebracht(boolean untergebracht) {
		this.housed = untergebracht;
	}

}