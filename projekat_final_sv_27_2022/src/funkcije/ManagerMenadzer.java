package funkcije;

import java.util.ArrayList;

import entity.*;
import main.Main;

public class ManagerMenadzer {
	
	public static ArrayList<Menadzer> svimenadzeri = Menadzer.ucitajMenadzere("data/menadzeri.csv");
	
	public static String pathKozmeticari = "data/kozmeticari.csv";
	public static String pathMenadzeri = "data/menadzeri.csv";
	public static String pathRecepcioneri = "data/recepcioneri.csv";
	
	
	public static void dodajZaposlenog(String korisnickoIme, String lozinka, String ime, String prezime, String pol, String telefon, String adresa, double strucnaSprema, double staz, double bonus, double plata, ArrayList<tipTretmana> usluge,  String tipZaposlenog) {
		
		switch(tipZaposlenog) {
			case "Kozmeticar":
				ArrayList<Kozmeticar> kozmeticari = ManagerKozmeticar.svikozmeticari;
				Kozmeticar kozmeticar = new Kozmeticar(Funkcionalnosti.generisiID(), ime, prezime, pol, telefon, adresa, korisnickoIme, lozinka, strucnaSprema, staz, bonus, plata, usluge);
				kozmeticari.add(kozmeticar);
				Kozmeticar.sacuvajKozmeticare(kozmeticari, pathKozmeticari);
				break;
			
			case "Menadzer":
				Menadzer menadzer = new Menadzer(Funkcionalnosti.generisiID(), ime, prezime, pol, telefon, adresa, korisnickoIme, lozinka, strucnaSprema, staz, bonus, plata);
				svimenadzeri.add(menadzer);
				Menadzer.sacuvajMenadzere(svimenadzeri, pathMenadzeri);
				break;
				
			case "Recepcioner":
				ArrayList<Recepcioner> recepcioneri = ManagerRecepcioner.svirecepcioneri;
				Recepcioner recepcioner = new Recepcioner(Funkcionalnosti.generisiID(), ime, prezime, pol, telefon, adresa, korisnickoIme, lozinka, strucnaSprema, staz, bonus, plata);
				recepcioneri.add(recepcioner);
				Recepcioner.sacuvajRecepcionere(recepcioneri, pathRecepcioneri);
				break;
				
				
		}
	
	}

    public static void izbrisiZaposlenog(int ID) {
    	for(Kozmeticar koz : ManagerKozmeticar.svikozmeticari) {
    		if(ID == koz.getID()) {
    			ManagerKozmeticar.svikozmeticari.remove(koz);
    			Kozmeticar.sacuvajKozmeticare(ManagerKozmeticar.svikozmeticari, pathKozmeticari);
    			return;
    			
    		}
    	}
    	

    	for(Recepcioner rec : ManagerRecepcioner.svirecepcioneri) {
    		if(ID == rec.getID()) {
    			ManagerRecepcioner.svirecepcioneri.remove(rec);
    			Recepcioner.sacuvajRecepcionere(ManagerRecepcioner.svirecepcioneri, pathRecepcioneri);
    			return;
    		}
    	}
    	
    	for(Menadzer men : ManagerMenadzer.svimenadzeri) {
    		if(ID == men.getID()) {
    			ManagerMenadzer.svimenadzeri.remove(men);
    			Menadzer.sacuvajMenadzere(ManagerMenadzer.svimenadzeri, pathMenadzeri);
    			return;
    		}
    	}


    	
    }

    public static double izracunajPlatuZaposlenog(int ID) {
    	
    	Zaposleni zap = Funkcionalnosti.nadjiZaposlenogPoID(ID);
    	double strucnasprema = zap.getStrucnaSprema();
    	double staz = zap.getStaz();
    	double bonus = zap.getBonus();
    	double osnovnaplata = zap.getPlata();
    	
    	double plata = (1 + strucnasprema*staz)*osnovnaplata + bonus; 
    	
    	return plata;
    }
    
    public static void postaviCenuTretmana(uslugaTretman tretman, double cena) {
    	ArrayList<uslugaTretman> usluge = uslugaTretman.sveusluge;
    	for (int i = 0; i < usluge.size(); i++) {
    		uslugaTretman u = usluge.get(i);
	        if (u.getImeUsluge() == tretman.getImeUsluge()) {
	        	u.setCena(cena);
	            usluge.set(i, u);
	        }
    	}
    }
    
    public static void postaviVrednostZaKL(double cena) {
    	Main.salon.setVrednostZaKL(cena);
    	Salon.sacuvajSalon(Main.salon, "data/salon.csv");
    }

    public static Menadzer nadjiMenadzeraPoID(int ID) {
    	for(Menadzer m : svimenadzeri) {
    		if(m.getID() == ID) {
    			return m;
    		}
    	}
    	return null;
    }


}
