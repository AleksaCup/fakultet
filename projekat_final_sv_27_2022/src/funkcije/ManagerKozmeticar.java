package funkcije;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import entity.*;
import enums.Stanje;

public class ManagerKozmeticar {
	
	public static ArrayList<Kozmeticar> svikozmeticari = Kozmeticar.ucitajKozmeticare("data/kozmeticari.csv");
	
	public static ArrayList<zakazanTretman> dodeljeniTretmani(Kozmeticar k){
		
		ArrayList<zakazanTretman> lista = new ArrayList<>();
		
		for(zakazanTretman tretman : ManagerZakazanTretman.svizakazani) {
			if(tretman.getIzabraniKozmeticar().equals(k)) {
				lista.add(tretman);
			}
		}
		return lista;
		
	}
	
	public static ArrayList<zakazanTretman> raspored(Kozmeticar k, LocalDate dan){
		
		ArrayList<zakazanTretman> raspored = new ArrayList<>();
		
		for(zakazanTretman tretman : ManagerZakazanTretman.svizakazani) {
			if(tretman.getIzabraniKozmeticar().equals(k) && tretman.getTermin().toLocalDate().equals(dan)  && tretman.getStanjeTretmana().equals(Stanje.ZAKAZAN)) {
				raspored.add(tretman);
			}
		}
		return raspored;
		
	}
	
	public static ArrayList<LocalDateTime> nadjiSlobodneTermine(Kozmeticar k, LocalDate dan, LocalTime radnoVremePocetak, LocalTime radnoVremeKraj) {
	    ArrayList<LocalDateTime> slobodniTermini = new ArrayList<>();

	    LocalDateTime pocetakRadnogVremena = LocalDateTime.of(dan, radnoVremePocetak);
	    LocalDateTime krajRadnogVremena = LocalDateTime.of(dan, radnoVremeKraj);

	    LocalDateTime trenutniTermin = pocetakRadnogVremena;

	    ArrayList<zakazanTretman> zakazaniTretmani = ManagerZakazanTretman.svizakazani;

	    while (trenutniTermin.isBefore(krajRadnogVremena)) {
	        boolean isAvailable = true;

	        for (zakazanTretman tretman : zakazaniTretmani) {
	            if (tretman.getIzabraniKozmeticar().equals(k) && tretman.getTermin().toLocalDate().equals(dan)) {
	                LocalDateTime termin = tretman.getTermin();
	                uslugaTretman usluga = tretman.getUslugu();

	                int duzinaTretmana = usluga.getDuzinaTrajanja();
	                LocalDateTime zavrsetakTermina = termin.plusMinutes(duzinaTretmana);

	                if (trenutniTermin.equals(termin) || trenutniTermin.isAfter(termin) && trenutniTermin.isBefore(zavrsetakTermina)) {
	                    isAvailable = false;
	                    break;
	                }
	            }
	        }

	        if (isAvailable) {
	            slobodniTermini.add(trenutniTermin);
	        }

	        trenutniTermin = trenutniTermin.plusHours(1);
	    }

	    return slobodniTermini;
	}

	public static ArrayList<Kozmeticar> kvalifikovaniKozmericari(uslugaTretman usluga){
		ArrayList<Kozmeticar> ispravniKozmeticari = new ArrayList<>();
		for(Kozmeticar k : svikozmeticari) {
			ArrayList<tipTretmana> svitipovi = k.getObucenZa();
			ArrayList<String> sviTipoviImena = new ArrayList<>();
			
			for(tipTretmana t : svitipovi) {
				sviTipoviImena.add(t.getImeTretmana());
			}
			if(sviTipoviImena.contains(usluga.getTip().getImeTretmana())) {
				ispravniKozmeticari.add(k);
			}
			
		}
		
		return ispravniKozmeticari;
	}
	
	public static Kozmeticar nadjiKozmeticaraPoKorisnickomImenu(String username){
		for(Kozmeticar k : svikozmeticari) {
			if(k.getKorisnickoIme().equals(username)) {
				return k;
			}	
		}
		return null;
	}
	
	public static Kozmeticar nadjiKozmeticaraPoID(int ID){
		for(Kozmeticar k : svikozmeticari) {
			if(k.getID() == ID) {
				return k;
			}	
		}
		return null;
	}
	
	public static int brojIzvrsenih(Kozmeticar k, LocalDate pocetak, LocalDate kraj) {
		int broj = 0;
		for(zakazanTretman z : ManagerKozmeticar.dodeljeniTretmani(k)) {
			if(z.getStanjeTretmana().equals(Stanje.IZVRSEN) && (z.getTermin().toLocalDate().isAfter(pocetak) || z.getTermin().toLocalDate().isEqual(pocetak)) && (z.getTermin().toLocalDate().isBefore(kraj) || z.getTermin().toLocalDate().isEqual(kraj))) {
				broj++;
			}
		}
		return broj;
	}
	
	public static double ukupnoPrihodovao(Kozmeticar k, LocalDate pocetak, LocalDate kraj) {
		double prihod = 0;
		for(zakazanTretman z : ManagerKozmeticar.dodeljeniTretmani(k)) {
			if(z.getStanjeTretmana().equals(Stanje.IZVRSEN) && (z.getTermin().toLocalDate().isAfter(pocetak) || z.getTermin().toLocalDate().isEqual(pocetak)) && (z.getTermin().toLocalDate().isBefore(kraj) || z.getTermin().toLocalDate().isEqual(kraj))) {
				prihod += z.getUslugu().getCena();
			}
		}
		return prihod;
	}
}
