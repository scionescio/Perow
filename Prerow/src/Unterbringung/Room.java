package Unterbringung;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * TODO age average method
 * 
 */
public class Room {

	private static AtomicInteger ai = new AtomicInteger(1);
	private int id;
	private int numberBeds;
	private ArrayList<Lifeguard> inhibitants = new ArrayList<>();

	public Room(int anzahlBetten) {
		this.id = ai.getAndIncrement();
		this.numberBeds = anzahlBetten;
	}

	public String toStringOld() {
		return "Zimmer [id=" + id + ", anzahlBetten=" + numberBeds + ", bewohner=" + inhibitants + "]";
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Zimmer: " + id + "\t | Betten: " + numberBeds + " |");
		if (inhibitants.size() > 0) {
			sb.append(" Bewohner: ");
			for (Lifeguard l : inhibitants) {
				sb.append(l.printName() + ", " + l.getGeschlecht() + ", " + l.getAge() + " | ");
			}
		}
		return sb.toString();
	}

	public int emptyBeds() {
		return numberBeds - inhibitants.size();
	}

	public void belegen(Lifeguard r) {
		inhibitants.add(r);
	}

	public void ausziehen(Lifeguard r) {
		inhibitants.remove(r);
		if (inhibitants.size() == 0) {
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumberBeds() {
		return numberBeds;
	}

	public void setNumberBeds(int numberBeds) {
		this.numberBeds = numberBeds;
	}

	public ArrayList<Lifeguard> getInhibitants() {
		return inhibitants;
	}

	public void setInhibitants(ArrayList<Lifeguard> inhibitants) {
		this.inhibitants = inhibitants;
	}

}
