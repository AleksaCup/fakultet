package entity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import enums.KarticaLojalnosti;
import funkcije.ManagerKlijent;

public class Klijent extends Korisnik {
	
	KarticaLojalnosti kl;
	double potrosnja;


	

	public Klijent(int ID, String ime, String prezime, String pol, String telefon, String adresa, String korisnickoIme,
			String lozinka, KarticaLojalnosti kl, double potrosnja) {
		super(ID, ime, prezime, pol, telefon, adresa, korisnickoIme, lozinka);
		this.setKl(kl);
		this.setPotrosnja(potrosnja);
		
	}
	
	

	@Override
	public String toString() {
		return  "Klijent[" + getID()  + ", " + getIme() + ", "  + getPrezime()
				+ ", " + getPol() + ", " + getTelefon() + ", " + getAdresa()
				+ ", " + getKorisnickoIme() + ", " + getLozinka() + ", " + getKl() + ", " + getPotrosnja() + "]";
	}



	public Klijent(String ime, String prezime){
		super(ime, prezime);
	}
	
	public Klijent(int ID, String ime, String prezime, String username, String sifra) {
		super(ID, null, null, null, null, null, username, sifra);
		this.setKl(null);
		this.setPotrosnja(0);
	}
	
	public Klijent(int ID, String ime, String prezime){
		super(ID, ime, prezime, null, null, null, null, null);
	}
	
	
	public Klijent() {
		super(0, null, null, null, null, null, null, null);
		this.setKl(null);
		this.setPotrosnja(0);
		
	}
	
	public KarticaLojalnosti getKl() {
		return kl;
	}

	public void setKl(KarticaLojalnosti kl) {
		this.kl = kl;
	}

	
	public double getPotrosnja() {
		return potrosnja;
	}

	public void setPotrosnja(double potrosnja) {
		this.potrosnja = potrosnja;
	}
	
	
	
	
	
	public static ArrayList<Klijent> ucitajKlijente(String path) {
		

		String line = "";
		ArrayList<Klijent> klijenti = new ArrayList<Klijent>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			
			while((line = br.readLine()) != null ) {
				String[] values = line.split(",");
				Klijent klijent = new Klijent();
				klijent.setID(Integer.parseInt(values[0]));
		        klijent.setIme(values[1]);
		        klijent.setPrezime(values[2]);
		        klijent.setPol(values[3]);
		        klijent.setTelefon(values[4]);
		        klijent.setAdresa(values[5]);
		        klijent.setKorisnickoIme(values[6]);
		        klijent.setLozinka(values[7]);

	            
	            if(values[8] == "IMA") {
	            	 klijent.setKl(KarticaLojalnosti.IMA);
	            }
	            else if(values[8] == "NEMA") {
	            	klijent.setKl(KarticaLojalnosti.NEMA);
	            }
	            else {
	            	klijent.setKl(null);
	            }
	            
	            klijent.setPotrosnja(Double.parseDouble(values[9]));
	            
	            

		        klijenti.add(klijent);		
			}
			br.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return klijenti;
	}
	
	public static void sacuvajKlijente(ArrayList<Klijent> klijenti, String path) {
	    try {
	        BufferedWriter bw = new BufferedWriter(new FileWriter(path));

	        for (Klijent klijent : klijenti) {

	        	KarticaLojalnosti karticaEnum = klijent.getKl();
	        	String kartica = new String();
	        	if(karticaEnum != null && karticaEnum.equals(KarticaLojalnosti.IMA)) {
	        		kartica = "IMA";
	        	}
	        	else {
	        		kartica = "NEMA";
	        	}
	            
	            bw.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s,%f\n", 
	            		klijent.getID(), klijent.getIme(), klijent.getPrezime(), klijent.getPol(), klijent.getTelefon(),
	                klijent.getAdresa(), klijent.getKorisnickoIme(), klijent.getLozinka(), kartica, klijent.getPotrosnja()));
	        }

	        bw.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	}

	
	
	public static void obrisiKlijentaPoID(int id) {
		
		String path = "data/klijenti.csv";
		
		ArrayList<Klijent> sviklijenti = ucitajKlijente(path);
	    for (int i = 0; i < sviklijenti.size(); i++) {
	        if (sviklijenti.get(i).getID() == id) {
	            sviklijenti.remove(i);
	            sacuvajKlijente(sviklijenti, path);
	            System.out.println("Klijent sa ID-jem " + id + " uspesno obrisan.");
	            return;
	        }
	    }
	    System.out.println("Klijent sa ID-jem " + id + " nije pronadjen.");
	}
	
	
	public static void ispisiKlijente(ArrayList<Klijent> klijenti) {
	    for (Klijent k : klijenti) {
	        System.out.println(k);
	    }
	}
	
	public static void updateKlijenta(Klijent kl) {
		for (int i = 0; i < ManagerKlijent.sviklijenti.size(); i++) {
	        Klijent k = ManagerKlijent.sviklijenti.get(i);
	        if (k.getID() == kl.getID()) {
	            ManagerKlijent.sviklijenti.set(i, kl);
	            Klijent.sacuvajKlijente(ManagerKlijent.sviklijenti, "data/klijenti.csv");
	            break;
	        }
	    }
	}
	
	public static Klijent nadjiKlijentaPoID(int id) {
		for(Klijent kl : ManagerKlijent.sviklijenti) {
			if(kl.getID() == id) {
				return kl;
			}
		}
		return null;
	}
	
	


}
