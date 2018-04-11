package Unterbringung;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class HousingTests {
	Quarters u;
	Lifeguard dummie;

	@Before
	public void setUp() {
		u = new Quarters();
		u.rettungsschwimmerEinlesen();
		dummie = u.allLifeguards.stream().filter(x -> x.getName().contains("Pascalis")).findFirst().get();
		// u.roomSimulator();
		// u.timeLord();
	}

	@Test
	public void findIdiot() {
		assertTrue(dummie.getBday().equals("07.05.1997"));
		dummie.setMitanreisender(u.allLifeguards.get(new Random().nextInt(u.allLifeguards.size())));
	}

	@Test
	public void wasNotHoused() {

	}

	// TODO different kind of lifeguards with different qualities on their search
	// for exceptions
	// String name, String bday, String geschlecht, String addresse, String anreise,
	// String abreise
	@Test
	public void simianArmy() {
		Lifeguard numberOne = new Lifeguard("Hagrid", null, "n", null, "09.06.2017", "29.06.2017");
		System.out.println(numberOne);
		assertTrue(u.findRoomForLifeguard(numberOne));
	}

}
