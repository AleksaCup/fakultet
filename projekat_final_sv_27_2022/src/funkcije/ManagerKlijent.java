package funkcije;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import entity.*;
import enums.Stanje;
import enums.KarticaLojalnosti;
import main.Main;

public class ManagerKlijent {
	
	public static ArrayList<Klijent> sviklijenti = Klijent.ucitajKlijente("data/klijenti.csv");
	
	public static String pathZakazaniTretmani = "data/zakazanitretmani.csv";
	
	
	
	public static Klijent nadjiKlijentapoID(int id) {
		for(Klijent k : sviklijenti) {
			if(k.getID() == id) {
				return k;
			}
		}
	    return null;
	}
	
	public static Klijent nadjiKlijentapoKorisnickomImenu(String username) {
		for(Klijent k : sviklijenti) {
			if(k.getKorisnickoIme().equals(username)) {
				return k;
			}
		}
	    return null;
	}
	
	public static void obrisiKlijentapoID(int id) {
		for(Klijent k : sviklijenti) {
			if(k.getID() == id) {
				sviklijenti.remove(k);
				return;
			}
		}
		
		JOptionPane.showMessageDialog(null, "Nije pronadjen klijent sa zadatim Id-jem");
	    

	}
	
	public static void proveriUslovZaKL(Klijent k) {
		double vrednost = Main.salon.getVrednostZaKL();
		if(k.getPotrosnja() >= vrednost) {
			k.setKl(KarticaLojalnosti.IMA);
			for(int i = 0; i < sviklijenti.size(); i++) {
				if(sviklijenti.get(i).equals(k)) {
					sviklijenti.set(i, k);
					Klijent.sacuvajKlijente(sviklijenti, "data/klijenti.csv");	
				}
				
			}
		}
		else {
			k.setKl(KarticaLojalnosti.NEMA);
			for(int i = 0; i < sviklijenti.size(); i++) {
				if(sviklijenti.get(i).equals(k)) {
					sviklijenti.set(i, k);
					Klijent.sacuvajKlijente(sviklijenti, "data/klijenti.csv");	
				}
				
			}
		}
	}
	
	public static boolean proveriUslovZaKarticu(Klijent k) {
		double vrednost = Main.salon.getVrednostZaKL();
		if(k.getPotrosnja() >= vrednost) {
			k.setKl(KarticaLojalnosti.IMA);
			for(int i = 0; i < sviklijenti.size(); i++) {
				if(sviklijenti.get(i).equals(k)) {
					sviklijenti.set(i, k);
					Klijent.sacuvajKlijente(sviklijenti, "data/klijenti.csv");	
					return true;
				}
				
			}
		}
		else {
			k.setKl(KarticaLojalnosti.NEMA);
			for(int i = 0; i < sviklijenti.size(); i++) {
				if(sviklijenti.get(i).equals(k)) {
					sviklijenti.set(i, k);
					Klijent.sacuvajKlijente(sviklijenti, "data/klijenti.csv");	
				}
				
			}
		}
		return false;
	}
	
	
	public static zakazanTretman zakaziTretman(uslugaTretman usluga, Kozmeticar izabraniKozmeticar, LocalDateTime termin, Klijent klijent) {
		
		ArrayList<zakazanTretman> tretmani = zakazanTretman.ucitajZakazaneTretmane(pathZakazaniTretmani);
		int bonusMin = Funkcionalnosti.ucitajBonus("data/bonus.csv");
		ArrayList<LocalDateTime> slobodnitermini = ManagerKozmeticar.nadjiSlobodneTermine(izabraniKozmeticar, termin.toLocalDate(), Main.salon.getPocetakradnogvremena(), Main.salon.getKrajradnogvremena());
		
		int brojtretmana = ManagerKozmeticar.dodeljeniTretmani(izabraniKozmeticar).size();
		
		boolean slobodan = false;
		for (LocalDateTime slobodantermin : slobodnitermini) {
		    if (slobodantermin.equals(termin)) {
		        slobodan = true;
		        break;
		    }
		}

		if (!slobodan) {
			return null;
		}
		
		ArrayList<tipTretmana> svitipovi = izabraniKozmeticar.getObucenZa();
		
		proveriUslovZaKL(klijent);
		
		for (tipTretmana tip : svitipovi) {
	        if (tip.getImeTretmana().equals(usluga.getTip().getImeTretmana())) {
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
					klijent.setPotrosnja(klijent.getPotrosnja() + usluga.getCena());  //KLIJENTU SE U POTROSNJU UBACUJE CENA ZAKAZANOG TRETAMANA
				}
				
				Klijent.updateKlijenta(klijent);
				return tretmani.get(tretmani.size()-1);
	        }
		}
		
		return null;
		
	}

	public static zakazanTretman zakaziTretmanBezKozmeticara(uslugaTretman usluga, LocalDateTime termin, Klijent klijent) {
		
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
		
		Random random = new Random();
		int i = random.nextInt(ispravniKozmeticari.size());
		
		Kozmeticar generisanKozmeticar = ispravniKozmeticari.get(i);
		
		proveriUslovZaKL(klijent);
		
		int brojtretmana = ManagerKozmeticar.dodeljeniTretmani(generisanKozmeticar).size();
		
		if(klijent.getKl().equals(KarticaLojalnosti.IMA)) {
    		zakazanTretman t = new zakazanTretman(usluga, generisanKozmeticar, termin, Stanje.ZAKAZAN, klijent, 0.9*usluga.getCena());
    		tretmani.add(t);
    	}
    	else {
    		zakazanTretman t = new zakazanTretman(usluga, generisanKozmeticar, termin, Stanje.ZAKAZAN, klijent, usluga.getCena());
    		tretmani.add(t);
    	}
		zakazanTretman.sacuvajZakazaneTretmane(tretmani, pathZakazaniTretmani);
		
		brojtretmana +=1;
		
		if(brojtretmana >= bonusMin) {
			for(int m = 0; m < ManagerKozmeticar.svikozmeticari.size(); m++) {
				if(ManagerKozmeticar.svikozmeticari.get(m).equals(generisanKozmeticar)) {
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
				if(ManagerKozmeticar.svikozmeticari.get(m).equals(generisanKozmeticar)) {
					Kozmeticar k = ManagerKozmeticar.svikozmeticari.get(m);
					k.setBonus(0);
					ManagerKozmeticar.svikozmeticari.set(m, k);
					Kozmeticar.sacuvajKozmeticare(ManagerKozmeticar.svikozmeticari, "data/kozmeticari.csv");
				}
			}
		}
		
		zakazanTretman zak = tretmani.get(tretmani.size()-1);
		

		klijent.setPotrosnja(klijent.getPotrosnja() + zak.getNaplaceno());

		
		Klijent.updateKlijenta(klijent);
		return zak;


		
	}
	
	public static ArrayList<zakazanTretman> pregledTretmana(Klijent kl){
		
		ArrayList<zakazanTretman> klijentoviTretmani = new ArrayList<>();
		
		//ArrayList<zakazanTretman> zakazani = new ArrayList<>();
		
		
		for(zakazanTretman zt : ManagerZakazanTretman.svizakazani) {
			if(zt.getKlijent().equals(kl)) {
				klijentoviTretmani.add(zt);
			}
		}
		
		return klijentoviTretmani;
	}
	
	public static void otkaziTretman(zakazanTretman tretman, Klijent klijent) {
		
		for(int i=0; i<ManagerZakazanTretman.svizakazani.size(); i++) {
			if(ManagerZakazanTretman.svizakazani.get(i).equals(tretman) && tretman.getKlijent().equals(klijent)) {
				ManagerZakazanTretman.svizakazani.get(i).setStanjeTretmana(Stanje.OTKAZAO_KLIJENT);
				klijent.setPotrosnja(klijent.getPotrosnja() - tretman.getUslugu().getCena());
				Klijent.updateKlijenta(klijent);
				ManagerZakazanTretman.azurirajTretman(ManagerZakazanTretman.svizakazani.get(i), i);
			}
		}
		
	}
	


	
	
	

}
