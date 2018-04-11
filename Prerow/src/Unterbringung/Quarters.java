package Unterbringung;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.concurrent.PriorityBlockingQueue;

/*
 * @author Chris + Pascalis
 */

public class Quarters {
	ArrayList<Lifeguard> allLifeguards = new ArrayList<>();
	ArrayList<Room> allRooms = new ArrayList<>();
	PriorityBlockingQueue<Lifeguard> lifeguardsOfOneWeek = new PriorityBlockingQueue<>();
	boolean triggerOutput = false;

	public static void main(String[] args) {
		Quarters u = new Quarters();
		u.rettungsschwimmerEinlesen();
		// System.out.println((double) u.allLifeguards.stream().map(x ->
		// x.getAge()).reduce((x, y) -> x + y).get()
		// / (double) u.allLifeguards.stream().count());
		u.roomSimulator();
		u.timeLord();

	}

	public void roomSimulator() {
		this.allRooms.add(new Room(3));
		for (int i = 0; i < 10; i++) {
			this.allRooms.add(new Room(2));
		}
		// this.allRooms.add(new Room(10));
	}

	/*
	 * Output only when different -> done via bool triggerOutput
	 */
	public boolean timeLord() {

		// TODO reference between lifeguard and room to stop the streaming nonsense
		LocalDate dd = LocalDate.parse("19.05.2017", DateTimeFormatter.ofPattern("dd.MM.uuuu"));
		for (int i = 0; i < 150; i++) {
			LocalDate ld = dd.plus(Period.ofDays(i));
			System.out.println("------------------------------------"
					+ ld.format(DateTimeFormatter.ofPattern("dd.MM.uuuu")).toString()
					+ "--------------------------------------");

			this.allLifeguards.stream().filter(x -> x.getAbreise().compareTo(ld) == 0).forEach(x -> {
				// System.out.println(x + " leaves on " + ld);
				triggerOutput = true;
				this.allRooms.stream().filter(y -> y.getInhibitants().contains(x)).forEach(y -> y.ausziehen(x));
			});
			this.allLifeguards.stream().filter(x -> x.getAnreise().compareTo(ld) == 0).forEach(x -> {
				// System.out.println(x + " arrives on " + ld);
				if (!x.isHoused()) { // because housed lg are not deleted from alllifeguards anymore
					triggerOutput = true;
					lifeguardsOfOneWeek.add(x);
				}
			});

			sortieren();
			// lifeguardsOfOneWeek.forEach(x -> System.out.println(x));
			assignLifeguardToARoom(lifeguardsOfOneWeek);

			// allLifeguards.stream().filter(x -> x.getAnreise().compareTo(ld) ==
			// 0).forEach(x -> System.out.println(x));

			if (!allLifeguards.stream().filter(x -> x.getAnreise().compareTo(ld) == 0).allMatch(x -> x.hasRoom())) {
				System.out.println("Hasn't found a room: ");
				allLifeguards.stream().filter(x -> x.getAnreise().compareTo(ld) == 0).filter(x -> !x.hasRoom())
						.forEach(x -> System.out.println(x));
				System.out.println("|||||||||||||||||||||");
			}
			if (triggerOutput) {
				allRooms.stream().forEach(x -> System.out.println(x));
			}
			System.out.println("------------------------------------------------------------------------------------");
			triggerOutput = false;
		}

		return true;

	}

	public boolean sortieren() {

		// System.out.println(lel.isEmpty());

		lifeguardsOfOneWeek.stream().forEach((x) -> {

			if (x.getBuddy() != null) {
				for (Lifeguard l : allLifeguards) {
					if (l.getName().equals(x.getBuddy())) {
						x.setMitanreisender(l);
						l.setMitanreisender(x);
						break;
					}
				}
				if (x.getMitanreisender() == null) {
					Lifeguard g = new Lifeguard(x.getBuddy(), null, x.getGeschlecht(), null, x.getAnreise(),
							x.getAbreise());
					g.setMitanreisender(x);
					this.allLifeguards.add(g);
					x.setMitanreisender(g);
				}

				// x.setMitanreisender(
				// allLifeguards.stream().filter(y ->
				// y.getName().equals(x.getBuddy())).findAny().orElse(x));

			} else {
				lifeguardsOfOneWeek.stream().forEach((y) -> {

					if (!x.hasBuddy() && !y.hasBuddy() && !y.equals(x) && x.getGeschlecht().equals(y.getGeschlecht())) {

						if (Math.abs(x.getAge() - y.getAge()) <= 5) {

							x.setMitanreisender(y);
							y.setMitanreisender(x);
						}
					}
				});
			}
		});

		sortLifeguardsOfOneWeek();

		// lifeguardsOfOneWeek.forEach(x -> System.out.println(x));

		return true;
	}

	public boolean sortLifeguardsOfOneWeek() {
		lifeguardsOfOneWeek.stream().sorted((r, d) -> {
			return r.compareTo(d);
		});

		return true;
	}

	/*
	 * TODO appropriate handling if someone is not housed; e.g. make room
	 * 
	 */
	public boolean assignLifeguardToARoom(PriorityBlockingQueue<Lifeguard> lifeguardsOfOneWeek) {
		while (!lifeguardsOfOneWeek.isEmpty()) {
			Lifeguard r = lifeguardsOfOneWeek.poll();
			boolean roomFound = findRoomForLifeguard(r);
			if (!roomFound) {

			}
		}
		return true;
	}

	public boolean findRoomForLifeguard(Lifeguard r) {
		if (r.hasBuddy()) {
			return goGetARoom(r);
			// if (!goGetARoom(r)) {
			//
			// System.out.println(r + " has to sleep outside");
			// } else {
			// System.out.println(r + " is housed: " + r.isHoused() + " and stays in Room "
			// + r.getRoom().getId());
			// }
		} else {
			return bookRoom(r);
			// if (!bookRoom(r)) {
			// System.out.println(r + " has to sleep outside");
			// } else {
			// System.out.println(r + " is housed: " + r.isHoused() + " and stays in Room "
			// + r.getRoom().getId());
			// }
		}
	}

	/*
	 * DONE componentize putting 2 lifeguards in 1 room -> putTwoPeopleInRoom
	 */
	public boolean goGetARoom(Lifeguard r) {

		for (Room z : allRooms) {

			if (z.getInhibitants().size() > 0 && z.emptyBeds() == 2) {
				if (r.getMitanreisender().getGeschlecht().equals(r.getGeschlecht())
						&& z.getInhibitants().get(0).getGeschlecht().equals(r.getGeschlecht())) {
					putTwoPeopleInRoom(r, z);
					return true;
				}
				continue;
			}

			if (z.emptyBeds() == 2) {
				putTwoPeopleInRoom(r, z);
				return true;
			}

		}
		r.getMitanreisender().setHatMitanreisenden(false);
		r.setHatMitanreisenden(false);

		return bookRoom(r);
	}

	public void putTwoPeopleInRoom(Lifeguard r, Room z) {
		z.belegen(r);
		r.setUntergebracht(true);
		r.setRoom(z);
		z.belegen(r.getMitanreisender());
		r.getMitanreisender().setUntergebracht(true);
		r.getMitanreisender().setRoom(z);
		lifeguardsOfOneWeek.remove(r.getMitanreisender());

		// if (!r.getName().equals(r.getMitanreisender().getName())) {
		// allLifeguards.remove(r.getMitanreisender());
		// }
	}

	/*
	 * possibly omit reduce()
	 */
	public boolean bookRoom(Lifeguard l) {
		try {
			Room r = this.allRooms.stream().filter(x -> x.emptyBeds() >= 1).filter(x -> {
				if (x.getInhibitants().isEmpty()) {
					return true;
				} else if (x.getInhibitants().get(0).getGeschlecht().equals(l.getGeschlecht())) {
					return true;
				} else {
					return false;
				}
			}).reduce((x, y) -> {
				return reducingToOptimalRoom(l, x, y);
			}).get();

			// for (Room r : allRooms) {
			// if (r.emptyBeds() >= 1) {
			r.belegen(l);
			l.setUntergebracht(true);
			l.setRoom(r);
			return true;
			// }
			// }
			// return false;

		} catch (NoSuchElementException nse) {
			// System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||"+
			// nse.getMessage());
			return false;
		}
	}

	public Room reducingToOptimalRoom(Lifeguard l, Room x, Room y) {
		// order to fill up rooms first rather than having all rooms blocked by
		// women
		if (!(x.getInhibitants().size() == 0) && !(y.getInhibitants().size() == 0)
				&& Math.abs(l.getAge() - x.getInhibitants().get(0).getAge()) <= Math
						.abs(l.getAge() - y.getInhibitants().get(0).getAge())) {
			return x;
		} else if (!(x.getInhibitants().size() == 0) && !(y.getInhibitants().size() == 0)
				&& Math.abs(l.getAge() - x.getInhibitants().get(0).getAge()) >= Math
						.abs(l.getAge() - y.getInhibitants().get(0).getAge())) {
			return y;
		} else if (x.getInhibitants().size() == 0 && !(y.getInhibitants().size() == 0)) {
			return y;
		} else if (y.getInhibitants().size() == 0 && !(x.getInhibitants().size() == 0)) {
			return x;
		} else if (y.getInhibitants().size() == 0 && (x.getInhibitants().size() == 0)) {
			return x;
		}
		return x;
	}

	public boolean rettungsschwimmerEinlesen() {
		File databaseFile = new File("/Users/pascalismaschke/Desktop/Wachliste2017PrerowCSV.csv");
		// System.out.println(databaseFile.exists());
		if (databaseFile.exists()) {
			try (BufferedReader r = new BufferedReader(new FileReader(databaseFile))) {
				String line = r.readLine();
				while ((line = r.readLine()) != null) {
					String[] b = line.split(";");
					// System.out.println(b.length);
					if ((b.length == 7) || !b[7].contains("X")) {
						Lifeguard l = new Lifeguard(b[0], b[1], b[2], b[3], b[5], b[6]);
						if (b.length >= 10 && !b[9].equals("")) {
							l.setBuddy(b[9]);
						}
						allLifeguards.add(l);
					}

				}
				r.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

}