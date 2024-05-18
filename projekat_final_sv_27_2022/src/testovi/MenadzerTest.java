package testovi;

import static org.junit.Assert.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.Test;

import entity.*;
import funkcije.*;
import main.Main;

public class MenadzerTest {

	@Test
    public void testNadjiMenadzeraPoID() {
        Menadzer m1 = new Menadzer(1001, "Marko", "Markovic", "M", "123456789", "Adresa", "username", "sifra", 1, 2, 500, 2000);
        ManagerMenadzer.svimenadzeri.add(m1);
        Menadzer m2 = new Menadzer(1002, "Nikola", "Nikolic", "M", "123456789", "Adresa", "username", "sifra", 1, 2, 500, 2000);
        ManagerMenadzer.svimenadzeri.add(m2);

        Menadzer dobijeni = ManagerMenadzer.nadjiMenadzeraPoID(1001);

        assertEquals(m1, dobijeni);
        
        ManagerMenadzer.svimenadzeri.remove(ManagerMenadzer.svimenadzeri.size()-1);
        ManagerMenadzer.svimenadzeri.remove(ManagerMenadzer.svimenadzeri.size()-1);
    }
	
	@Test
    public void testPostaviCenuTretmana() {

		uslugaTretman tretman = new uslugaTretman(27, "Tretman A", new tipTretmana("masaza"), 45, 1000.0);

        ArrayList<uslugaTretman> usluge = uslugaTretman.sveusluge;
        usluge.add(new uslugaTretman(27, "Tretman A", new tipTretmana("masaza"), 45, 1000.0));
        usluge.add(new uslugaTretman(28, "Tretman B", new tipTretmana("pedikir"), 55, 1200.0));
        usluge.add(new uslugaTretman(29, "Tretman C", new tipTretmana("manikir"), 35, 1400.0));
        
        ManagerMenadzer.postaviCenuTretmana(tretman, 2000.0);
        
        for (uslugaTretman u : usluge) {
            if (u.getImeUsluge().equals(tretman.getImeUsluge())) {
                assertEquals(2000.0, u.getCena(), 0.001);
            }
        }
        
    }
	
	@Test
	public void testPostaviVrednostZaKL() {

        Main.salon = new Salon("Salon", LocalTime.parse("06:00", DateTimeFormatter.ofPattern("HH:mm")), LocalTime.parse("22:00", DateTimeFormatter.ofPattern("HH:mm")),0,0);
        Main.salon.setVrednostZaKL(0.0);
        ManagerMenadzer.postaviVrednostZaKL(2500.0);
        assertEquals(2500.0, Main.salon.getVrednostZaKL(), 0.001);
    }
	
	@Test
    public void testIzracunajPlatuZaposlenog() {
        Kozmeticar k = ManagerKozmeticar.svikozmeticari.get(0);
        Menadzer m = ManagerMenadzer.svimenadzeri.get(0);
        Recepcioner r = ManagerRecepcioner.svirecepcioneri.get(0);
        
        double ocekivanaPlataK = (1 + k.getStrucnaSprema() * k.getStaz()) * k.getPlata() + k.getBonus(); 
        double dobijenaPlataK = ManagerMenadzer.izracunajPlatuZaposlenog(k.getID());
        
        double ocekivanaPlataM = (1 + m.getStrucnaSprema() * m.getStaz()) * m.getPlata() + m.getBonus(); 
        double dobijenaPlataM = ManagerMenadzer.izracunajPlatuZaposlenog(m.getID());
        
        double ocekivanaPlataR = (1 + r.getStrucnaSprema() * r.getStaz()) * r.getPlata() + r.getBonus(); 
        double dobijenaPlataR = ManagerMenadzer.izracunajPlatuZaposlenog(r.getID());
        
        assertEquals(ocekivanaPlataK, dobijenaPlataK, 0.001);
        assertEquals(ocekivanaPlataM, dobijenaPlataM, 0.001);
        assertEquals(ocekivanaPlataR, dobijenaPlataR, 0.001);
	}
	
	@Test
    public void testIzbrisiZaposlenog() {
        Kozmeticar k = new Kozmeticar();
        k.setID(100001);
        Menadzer m = new Menadzer();
        m.setID(100002);
        Recepcioner r = new Recepcioner();
        r.setID(100003);

        ManagerKozmeticar.svikozmeticari.add(k);
        ManagerMenadzer.svimenadzeri.add(m);
        ManagerRecepcioner.svirecepcioneri.add(r);

        ManagerMenadzer.izbrisiZaposlenog(k.getID());
        ManagerMenadzer.izbrisiZaposlenog(m.getID());
        ManagerMenadzer.izbrisiZaposlenog(r.getID());
        
        assertFalse(ManagerKozmeticar.svikozmeticari.contains(k));
        assertFalse(ManagerMenadzer.svimenadzeri.contains(m));
        assertFalse(ManagerRecepcioner.svirecepcioneri.contains(r));
    }
	
	@Test
    public void testDodajZaposlenog() {

        String username = "Aleksa961";
        String lozinka = "lozinka123";
        String ime = "Aleksa";
        String prezime = "Cup";
        String pol = "M";
        String telefon = "123456789";
        String adresa = "Cara Dusana";
        double strucnaSprema = 0.3;
        double staz = 5;
        double bonus = 2000;
        double plata = 50000;
        ArrayList<tipTretmana> usluge = new ArrayList<>();
        usluge.add(tipTretmana.svitipovi.get(0));
        usluge.add(tipTretmana.svitipovi.get(1));
        String tipZaposlenog = "Kozmeticar";
        
        ManagerMenadzer.dodajZaposlenog(username, lozinka, ime, prezime, pol, telefon, adresa, strucnaSprema, staz, bonus, plata, usluge, tipZaposlenog);
        Kozmeticar dodatKozmeticar = ManagerKozmeticar.svikozmeticari.get(ManagerKozmeticar.svikozmeticari.size() - 1);
        ManagerKozmeticar.svikozmeticari.remove(ManagerKozmeticar.svikozmeticari.size() - 1);
        Kozmeticar.sacuvajKozmeticare(ManagerKozmeticar.svikozmeticari, "data/kozmeticari.csv");
        assertEquals(ime, dodatKozmeticar.getIme());
        assertEquals(prezime, dodatKozmeticar.getPrezime());
        assertEquals(pol, dodatKozmeticar.getPol());
        assertEquals(telefon, dodatKozmeticar.getTelefon());
        assertEquals(adresa, dodatKozmeticar.getAdresa());
        assertEquals(username, dodatKozmeticar.getKorisnickoIme());
        assertEquals(lozinka, dodatKozmeticar.getLozinka());
        assertEquals(strucnaSprema, dodatKozmeticar.getStrucnaSprema(), 0.0001);
        assertEquals(staz, dodatKozmeticar.getStaz(), 0.0001);
        assertEquals(bonus, dodatKozmeticar.getBonus(), 0.0001);
        assertEquals(plata, dodatKozmeticar.getPlata(), 0.0001);
        assertEquals(usluge, dodatKozmeticar.getObucenZa());
        
    }

}
