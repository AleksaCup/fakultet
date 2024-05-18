package funkcije;

import entity.*;
import enums.KarticaLojalnosti;
import enums.Stanje;
import main.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public class Funkcionalnosti {
	
	public static String pathKozmeticari = "data/kozmeticari.csv";
	public static String pathMenadzeri = "data/menadzeri.csv";
	public static String pathRecepcioneri = "data/recepcioneri.csv";
	public static String pathID = "data/id.csv";
	
	 
	
	public static String dateToString(LocalDateTime dateTime) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	    return dateTime.format(formatter);
	}

	public static LocalDateTime stringToDate(String string) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	    return LocalDateTime.parse(string, formatter);
	}
	
	public static String datumUString(LocalDate date) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    return date.format(formatter);
	}

	public static LocalDate stringUDatum(String string) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    return LocalDate.parse(string, formatter);
	}
	
	public static int ucitajID(String path) {
		int ID = -1;
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			ID = Integer.parseInt(br.readLine());
			br.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return ID;
	}
	
	public static void sacuvajID(int ID, String path) {
	    try {
	        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
	            bw.write(String.format("%d\n", ID));
	        bw.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	}
	
	
	public static int ucitajBonus(String path) {
		int bonus = -1;
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			bonus = Integer.parseInt(br.readLine());
			br.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return bonus;
	}
	
	public static void sacuvajBonus(int bonus, String path) {
	    try {
	        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
	            bw.write(String.format("%d\n", bonus));
	        bw.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	}
	

    public static int login(String username, String password) {
    	
    	for (Klijent kl : ManagerKlijent.sviklijenti) {
    	    if (kl.getKorisnickoIme().equals(username)) {
    	        if (kl.getLozinka().equals(password)) {
    	        	Funkcionalnosti.sacuvajID(kl.getID(), pathID);
    	            return kl.getID();
    	        } 
    	        else {
    	            JOptionPane.showMessageDialog(null, "Pogresna lozinka za uneto korisnicko ime.");
    	            return -1;
    	        }
    	    }
    	      
        }
    	
    	for (Kozmeticar koz : ManagerKozmeticar.svikozmeticari) {
            if (koz.getKorisnickoIme().equals(username)) {
            	if (koz.getLozinka().equals(password)) {
            		Funkcionalnosti.sacuvajID(koz.getID(), pathID);
    	            return koz.getID();
    	        } 
    	        else {
    	            JOptionPane.showMessageDialog(null, "Pogresna lozinka za uneto korisnicko ime.");
    	            return -1;
    	        }
            }
        }
    	
    	for (Recepcioner rec : ManagerRecepcioner.svirecepcioneri) {
            if (rec.getKorisnickoIme().equals(username)) {
            	if (rec.getLozinka().equals(password)) {
            		Funkcionalnosti.sacuvajID(rec.getID(), pathID);
    	            return rec.getID();
    	        } 
    	        else {
    	            JOptionPane.showMessageDialog(null, "Pogresna lozinka za uneto korisnicko ime.");
    	            return -1;
    	        }
            }
        }
    	
    	for (Menadzer men : ManagerMenadzer.svimenadzeri) {
            if (men.getKorisnickoIme().equals(username)) {
            	if (men.getLozinka().equals(password)) {
            		Funkcionalnosti.sacuvajID(men.getID(), pathID);
    	            return men.getID();
    	        } 
    	        else {
    	            JOptionPane.showMessageDialog(null, "Pogresna lozinka za uneto korisnicko ime.");
    	            return -1;
    	        }
            }
        }
    	
    	JOptionPane.showMessageDialog(null, "Korisnik sa korisnickim imenom " + username + " ne postoji.");
    	return -1; 
        
    }
    
    public static void openFrame(String frameType) {
        JFrame frame = null;

        if (frameType.equals("Recepcioner")) {
            frame = new RecepcionerFrame();
        } else if (frameType.equals("Menadzer")) {
            frame = new MenadzerFrame();
        } else if (frameType.equals("Kozmeticar")) {
            frame = new KozmeticarFrame();
        } else if (frameType.equals("Klijent")) {
            frame = new KlijentFrame();
        } else if (frameType.equals("Registracija")) {
            frame = new RegistracijaFrame();
        } else if (frameType.equals("Start")) {
            frame = new Start();
        }
        
        

        if (frame != null) {
            frame.setVisible(true);
        } 
        
    }
    
    public static Zaposleni nadjiZaposlenogPoID(int ID) {
    	for(Kozmeticar koz : ManagerKozmeticar.svikozmeticari) {
    		if(ID == koz.getID()) {
    			return koz;
    			
    		}
    	}
    	

    	for(Recepcioner rec : ManagerRecepcioner.svirecepcioneri) {
    		if(ID == rec.getID()) {
    			return rec;
    		}
    	}
    	
    	for(Menadzer men : ManagerMenadzer.svimenadzeri) {
    		if(ID == men.getID()) {
    			return men;
    		}
    	}
    	
    	return null;
    	
    }
    
    
    public static void otkazaoKlijent(zakazanTretman tretman, int index, Klijent k, Salon salon) {
    	
    	if(tretman != null) {
    		tretman.setStanjeTretmana(Stanje.OTKAZAO_KLIJENT);
        	tretman.getKlijent().setPotrosnja(tretman.getKlijent().getPotrosnja() - 0.9*tretman.getNaplaceno());
        	salon.setBilans(salon.getBilans() - 0.9*tretman.getNaplaceno());
    		tretman.setNaplaceno(0.1*tretman.getNaplaceno());
    		for(int i = 0; i<ManagerKlijent.sviklijenti.size(); i++) {
    			if(ManagerKlijent.sviklijenti.get(i).getID() == tretman.getKlijent().getID()) {
    				ManagerKlijent.sviklijenti.set(i, tretman.getKlijent());
    			}
    		}
    		Klijent.sacuvajKlijente(ManagerKlijent.sviklijenti, "data/klijenti.csv");
        	ManagerZakazanTretman.azurirajTretmanKlijent(tretman, index, k); //OVDE IZMENI TREBA DA FUNKCIJA PROLAZI SAMO KROZ KLIJENTOVE TRETMANE
    	}
    	
    	
    }
    
    public static double potrosenaSuma(ArrayList<zakazanTretman> klijentoviTretmani) {
    	double suma = 0;
    	for(zakazanTretman z : klijentoviTretmani) {
    		suma += z.getNaplaceno();
    	}
    	//OVDE SAMO AZURIRAJ KLIJENTA, ODNOSNO POTROSNJU, SAMO STAVI SUMU I TO JE TO
    	Klijent k = klijentoviTretmani.get(0).getKlijent();
    	k.setPotrosnja(suma);
    	for(int i=0; i<ManagerKlijent.sviklijenti.size(); i++) {
    		if(ManagerKlijent.sviklijenti.get(i).equals(k)) {
    			ManagerKlijent.sviklijenti.set(i, k);
    			Klijent.sacuvajKlijente(ManagerKlijent.sviklijenti, "data/klijenti.csv");
    		}
    	}

    	return suma;
    }
    
    public static void otkazaoSalon(zakazanTretman tretman, int index, Salon salon) {
    	
    	if(tretman != null) {
    		tretman.setStanjeTretmana(Stanje.OTKAZAO_SALON);
        	tretman.getKlijent().setPotrosnja(tretman.getKlijent().getPotrosnja() - tretman.getNaplaceno());
        	salon.setBilans(salon.getBilans() - tretman.getNaplaceno());
        	tretman.setNaplaceno(0);
        	ManagerZakazanTretman.azurirajTretman(tretman, index);
    	}
    	
    }
    
    public static void Izvrsen(zakazanTretman tretman, int index, Salon salon) {
    	if(tretman != null) {
    		tretman.setStanjeTretmana(Stanje.IZVRSEN);
        	ManagerZakazanTretman.azurirajTretman(tretman, index);
        }
    	}
    	
    public static void nijeSePojavio(zakazanTretman tretman, int index, Salon salon) {
    	if(tretman != null) {
    		tretman.setStanjeTretmana(Stanje.KLIJENT_SE_NIJE_POJAVIO);
        	ManagerZakazanTretman.azurirajTretman(tretman, index);
    	}
    	
    }
    
    public static void setujStanjaPrilikomPokretanjaAplikacije() {
    	if(ManagerZakazanTretman.svizakazani.size() != 0) {
    		for(zakazanTretman z : ManagerZakazanTretman.svizakazani) {
        		if(z.getTermin().isBefore(LocalDateTime.now()) && z.getStanjeTretmana().equals(Stanje.ZAKAZAN)) {
        			z.setStanjeTretmana(Stanje.IZVRSEN);
        		}
        	}
    		zakazanTretman.sacuvajZakazaneTretmane(ManagerZakazanTretman.svizakazani, "data/zakazanitretmani.csv");
    	}
    	
    }
    
    public static int generisiID() {
    	ArrayList<Integer> postojeci = new ArrayList<Integer>();
    	int generisani;
    	
    	for(Klijent k : ManagerKlijent.sviklijenti)
    		postojeci.add(k.getID());
    	for(Kozmeticar k : ManagerKozmeticar.svikozmeticari)
    		postojeci.add(k.getID());
    	for(Recepcioner k : ManagerRecepcioner.svirecepcioneri)
    		postojeci.add(k.getID());
    	for(Menadzer k : ManagerMenadzer.svimenadzeri)
    		postojeci.add(k.getID());
    	
    	generisani = generisiRandomBroj(1000, postojeci);
    	
    	
    	return generisani;
    }
    
    public static int generisiRandomBroj(int gornjaGranica, ArrayList<Integer> postojeci) {
        Random random = new Random();
        int randomInt;

        do {
            randomInt = random.nextInt(gornjaGranica);
        } while (postojeci.contains(randomInt));

        return randomInt;
    }
    
    public static boolean slobodanUsername(String username) {
    	for(Kozmeticar koz : ManagerKozmeticar.svikozmeticari) {
    		if(username.equals(koz.getKorisnickoIme())) {
    			return false;
    		}
    	}

    	for(Recepcioner rec : ManagerRecepcioner.svirecepcioneri) {
    		if(username.equals(rec.getKorisnickoIme())) {
    			return false;
    		}
    	}
    	
    	for(Menadzer men : ManagerMenadzer.svimenadzeri) {
    		if(username.equals(men.getKorisnickoIme())) {
    			return false;
    		}
    	}
    	
    	for(Klijent kl : ManagerKlijent.sviklijenti) {
    		if(username.equals(kl.getKorisnickoIme())) {
    			return false;
    		}
    	}
    	return true;
    }
    
    
    public static double izracunajPotrosnju(zakazanTretman tretman) {
    	double potrosnja = 0;
    		if(ManagerKlijent.proveriUslovZaKarticu(tretman.getKlijent()) && (tretman.getStanjeTretmana().equals(Stanje.IZVRSEN) || tretman.getStanjeTretmana().equals(Stanje.ZAKAZAN) || tretman.getStanjeTretmana().equals(Stanje.KLIJENT_SE_NIJE_POJAVIO))) {
    			potrosnja = 0.9*tretman.getUslugu().getCena();
    		}
    		else if(tretman.getStanjeTretmana().equals(Stanje.IZVRSEN) || tretman.getStanjeTretmana().equals(Stanje.ZAKAZAN) || tretman.getStanjeTretmana().equals(Stanje.KLIJENT_SE_NIJE_POJAVIO)) {
    			potrosnja = tretman.getUslugu().getCena();
    		}
    		else if(tretman.getStanjeTretmana().equals(Stanje.OTKAZAO_KLIJENT)){
    			potrosnja = 0.1*tretman.getUslugu().getCena();
    		}
    		else if(tretman.getStanjeTretmana().equals(Stanje.OTKAZAO_SALON))  {
    			potrosnja = 0;
    		}
    		
    		return potrosnja;
    }


}
