package funkcije;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import entity.*;

import enums.Stanje;

public class ManagerZakazanTretman {
	
	public static String pathZakazani = "data/zakazanitretmani.csv";
	
	public static ArrayList<zakazanTretman> svizakazani = zakazanTretman.ucitajZakazaneTretmane(pathZakazani);
	
	public static void dodajUListu(zakazanTretman tretman) {
		if(tretman != null) {
			ManagerZakazanTretman.svizakazani.add(tretman);
			zakazanTretman.sacuvajZakazaneTretmane(svizakazani, pathZakazani);
		}
		else {
			System.out.println("Neuspesno zakazivanje! Tretman vec postoji ili je izabrani kozmeticar zauzet u zadatom terminu!");
		}
	}
	
	public static void azurirajTretman(zakazanTretman tretman, int index) {
		if(index != -1) {
				svizakazani.set(index, tretman);
				zakazanTretman.sacuvajZakazaneTretmane(svizakazani, pathZakazani);
		}
	}
	
	public static void azurirajTretmanKlijent(zakazanTretman tretman, int index, Klijent k) {
		if(index != -1) {
				ArrayList<zakazanTretman> zakazaniKlijent = ManagerKlijent.pregledTretmana(k);
				zakazaniKlijent.set(index, tretman);
				zakazanTretman existingTretman = zakazaniKlijent.get(index);
	            int svizakazaniIndex = svizakazani.indexOf(existingTretman);

	            if (svizakazaniIndex != -1) {

	                svizakazani.set(svizakazaniIndex, tretman);
	                zakazanTretman.sacuvajZakazaneTretmane(svizakazani, pathZakazani);
	            }
				
				
		}
	}
	
	public static ArrayList<zakazanTretman> tretmaniZaOdredjeniPeriod(LocalDate pocetak, LocalDate kraj) {
	    ArrayList<zakazanTretman> trazenitretmani = new ArrayList<>();

	    for(zakazanTretman tretman : svizakazani) {
	        if((tretman.getTermin().toLocalDate().isAfter(pocetak) || tretman.getTermin().toLocalDate().isEqual(pocetak)) && (tretman.getTermin().toLocalDate().isBefore(kraj) || tretman.getTermin().toLocalDate().isEqual(kraj))) {
	        	trazenitretmani.add(tretman);
	        }
	    }

	    return trazenitretmani;
	}
	
	public static double prihodiZaOdredjeniPeriod(LocalDateTime pocetak, LocalDateTime kraj) {
	    //ArrayList<zakazanTretman> trazenitretmani = new ArrayList<>();
	    double prihodi = 0;
	    

	    for(zakazanTretman tretman : svizakazani) {
	        if((tretman.getTermin().isAfter(pocetak) || tretman.getTermin().isEqual(pocetak)) && (tretman.getTermin().isBefore(kraj) || tretman.getTermin().isEqual(kraj))) {
	        	if(tretman.getStanjeTretmana() == Stanje.ZAKAZAN || tretman.getStanjeTretmana() == Stanje.IZVRSEN || tretman.getStanjeTretmana() == Stanje.KLIJENT_SE_NIJE_POJAVIO) {
	        		prihodi += uslugaTretman.uslugaPoImenu(tretman.getUslugu().getImeUsluge()).getCena();
	        	}
	        	
	        	if(tretman.getStanjeTretmana() == Stanje.OTKAZAO_KLIJENT) {
	        		prihodi += 0.1*uslugaTretman.uslugaPoImenu(tretman.getUslugu().getImeUsluge()).getCena();
	        	}
	        }
	    }

	    return prihodi;
	}
	
	public static double rashodiZaOdredjeniPeriod(LocalDateTime pocetak, LocalDateTime kraj) {
	    ArrayList<zakazanTretman> trazenitretmani = new ArrayList<>();
	    double rashodi = 0;
	    
	    

	    for(zakazanTretman tretman : svizakazani) {
	        if((tretman.getTermin().isAfter(pocetak) || tretman.getTermin().isEqual(pocetak)) && (tretman.getTermin().isBefore(kraj) || tretman.getTermin().isEqual(kraj))) {
	        	if(tretman.getStanjeTretmana().equals(Stanje.OTKAZAO_KLIJENT)) {
	        		rashodi += 0.9*uslugaTretman.uslugaPoImenu(tretman.getUslugu().getImeUsluge()).getCena();
	        	}
	        	
	        	if(tretman.getStanjeTretmana().equals(Stanje.OTKAZAO_SALON)) {
	        		rashodi += uslugaTretman.uslugaPoImenu(tretman.getUslugu().getImeUsluge()).getCena();
	        	}
	        	
	        	
	        	
	        }
	    }
	    
	    if (pocetak.withDayOfMonth(1).isEqual(pocetak) || pocetak.withDayOfMonth(1).isAfter(pocetak) && pocetak.withDayOfMonth(1).isBefore(kraj)) {

	    	for(Recepcioner rec : ManagerRecepcioner.svirecepcioneri) {
		    	rashodi += ManagerMenadzer.izracunajPlatuZaposlenog(rec.getID());
		    }
		    
		    for(Kozmeticar koz : ManagerKozmeticar.svikozmeticari) {
		    	rashodi += ManagerMenadzer.izracunajPlatuZaposlenog(koz.getID());
		    }
		    
		    for(Menadzer men : ManagerMenadzer.svimenadzeri) {
		    	rashodi += ManagerMenadzer.izracunajPlatuZaposlenog(men.getID());
		    }
        }
	    
	    
	    

	    return rashodi;
	}
	
	public static ArrayList<LocalTime> generisiTermine(LocalTime pocetak, LocalTime kraj) {
	        ArrayList<LocalTime> appointments = new ArrayList<>();

	        // Start from the first full hour after the start time
	        LocalTime currentAppointment = pocetak.withMinute(0);

	        while (currentAppointment.isBefore(kraj)) {
	            appointments.add(currentAppointment);
	            currentAppointment = currentAppointment.plusHours(1);
	        }

	        return appointments;
	    }
	   
	public static int brojPoStanju(Stanje s, ArrayList<zakazanTretman> tretmani) {
		  int broj = 0;
		  for(zakazanTretman z : tretmani) {
			  if(z.getStanjeTretmana().equals(s)) {
				  broj++;
			  }
		  }
		  return broj;
	  }
	  
	public static int brojPoUsluzi(uslugaTretman u, ArrayList<zakazanTretman> tretmani) {
		  int broj = 0;
		  for(zakazanTretman z : tretmani) {
			  if(z.getUslugu().getIDUsluge() == u.getIDUsluge()) {
				  broj++;
			  }
		  }
		  return broj;
	  }
	
	public static double prihodPoUsluzi(uslugaTretman u, ArrayList<zakazanTretman> tretmani) {
		  double prihod = 0;
		  for(zakazanTretman z : tretmani) {
			  if(z.getUslugu().getIDUsluge() == u.getIDUsluge()) {
				  prihod += u.getCena();
			  }
		  }
		  return prihod;
	  }
	

}
