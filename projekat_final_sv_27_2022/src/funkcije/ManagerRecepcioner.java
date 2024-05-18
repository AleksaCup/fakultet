package funkcije;

import entity.Recepcioner;
import entity.tipTretmana;
import entity.Klijent;
import entity.Kozmeticar;
import entity.zakazanTretman;
import entity.uslugaTretman;
import enums.KarticaLojalnosti;
import enums.Stanje;
import main.Main;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ManagerRecepcioner {
	
	public static ArrayList<Recepcioner> svirecepcioneri = Recepcioner.ucitajRecepcionere("data/recepcioneri.csv");
	
	public static String pathZakazaniTretmani = "data/zakazanitretmani.csv";
	
	public static Recepcioner nadjiRecepcioneraPoID(int ID) {
		for(Recepcioner r : svirecepcioneri) {
			if(r.getID() == ID) {
				return r;
			}
		}
		return null;
	}
	
	public static zakazanTretman zakaziTretman(uslugaTretman usluga, Kozmeticar izabraniKozmeticar, LocalDateTime termin, Klijent klijent) {
		ArrayList<zakazanTretman> tretmani = zakazanTretman.ucitajZakazaneTretmane(pathZakazaniTretmani);
		int bonusMin = Funkcionalnosti.ucitajBonus("data/bonus.csv");

		ArrayList<Kozmeticar> ispravniKozmeticari = new ArrayList<>();
		
		for(Kozmeticar k : ManagerKozmeticar.svikozmeticari) {
			ArrayList<LocalDateTime> slobodnitermini = ManagerKozmeticar.nadjiSlobodneTermine(k, termin.toLocalDate(), Main.salon.getPocetakradnogvremena(), Main.salon.getKrajradnogvremena());
			ArrayList<tipTretmana> svitipovi = k.getObucenZa();
			ArrayList<String> sviTipoviImena = new ArrayList<>();
			for(tipTretmana t : svitipovi) {
				sviTipoviImena.add(t.getImeTretmana());
			}
			if(slobodnitermini.contains(termin) && sviTipoviImena.contains(usluga.getTip().getImeTretmana())) {
				ispravniKozmeticari.add(k);
			}
		}
		
		if(ispravniKozmeticari.size() == 0) {
			return null;
		}
		
		
		ManagerKlijent.proveriUslovZaKL(klijent);
		
		
        if (ispravniKozmeticari.contains(izabraniKozmeticar)) {
        	
        	int brojtretmana = ManagerKozmeticar.dodeljeniTretmani(izabraniKozmeticar).size();
        	
        	if(klijent.getKl().equals(KarticaLojalnosti.IMA)) {
        		zakazanTretman t = new zakazanTretman(usluga, izabraniKozmeticar, termin, Stanje.ZAKAZAN, klijent, 0.9*usluga.getCena());
        		tretmani.add(t);
        	}
        	else {
        		zakazanTretman t = new zakazanTretman(usluga, izabraniKozmeticar, termin, Stanje.ZAKAZAN, klijent, usluga.getCena());
        		tretmani.add(t);
        	}
			zakazanTretman.sacuvajZakazaneTretmane(tretmani, pathZakazaniTretmani);
			
			brojtretmana +=1;
			
			if(brojtretmana >= bonusMin) {
				for(int m = 0; m < ManagerKozmeticar.svikozmeticari.size(); m++) {
					if(ManagerKozmeticar.svikozmeticari.get(m).equals(izabraniKozmeticar)) {
						Kozmeticar k = ManagerKozmeticar.svikozmeticari.get(m);
						if(k.getBonus() == 0) {
							k.setBonus(10000);
						}
						ManagerKozmeticar.svikozmeticari.set(m, k);
						Kozmeticar.sacuvajKozmeticare(ManagerKozmeticar.svikozmeticari, "data/kozmeticari.csv");
					}
				}
			}
			else {
				for(int m = 0; m < ManagerKozmeticar.svikozmeticari.size(); m++) {
					if(ManagerKozmeticar.svikozmeticari.get(m).equals(izabraniKozmeticar)) {
						Kozmeticar k = ManagerKozmeticar.svikozmeticari.get(m);
						k.setBonus(0);
						ManagerKozmeticar.svikozmeticari.set(m, k);
						Kozmeticar.sacuvajKozmeticare(ManagerKozmeticar.svikozmeticari, "data/kozmeticari.csv");
					}
				}
			}
			
			if(klijent.getKl().equals(KarticaLojalnosti.IMA)) {
				klijent.setPotrosnja(klijent.getPotrosnja() + 0.9*usluga.getCena());
			}
			else {
				klijent.setPotrosnja(klijent.getPotrosnja() + usluga.getCena());
			}
			
			Klijent.updateKlijenta(klijent);
			return tretmani.get(tretmani.size()-1);
        }
	
	
        return null;
	}
	
	public static void otkaziTretman(zakazanTretman tretman) {
		
		for(int i=0; i<ManagerZakazanTretman.svizakazani.size(); i++) {
			if(ManagerZakazanTretman.svizakazani.get(i).equals(tretman)) {
				ManagerZakazanTretman.svizakazani.get(i).setStanjeTretmana(Stanje.OTKAZAO_SALON);
				ManagerZakazanTretman.azurirajTretman(ManagerZakazanTretman.svizakazani.get(i), i);
				break;
			}
		}
		
		
	}
	
	public static void izmeniTretman(zakazanTretman tretman, uslugaTretman usluga, Kozmeticar k, LocalDateTime termin, Stanje stanje, Klijent kl) {
		for(zakazanTretman t : ManagerZakazanTretman.svizakazani) {
			if(t.equals(tretman)) {
				t.setUslugu(usluga);
				t.setIzabraniKozmeticar(k);
				t.setTermin(termin);
				t.setStanjeTretmana(stanje);
				t.setKlijent(kl);
			}
		}
		
	}

}
