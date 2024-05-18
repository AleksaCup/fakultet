package entity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Recepcioner extends Zaposleni {

	public Recepcioner(int ID, String ime, String prezime, String pol, String telefon, String adresa, String korisnickoIme,
			String lozinka, double strucnaSprema, double staz, double bonus, double plata) {
		super(ID, ime, prezime, pol, telefon, adresa, korisnickoIme, lozinka, strucnaSprema, staz, bonus, plata);
		
	}

	
	public Recepcioner(int ID, String ime, String prezime) {
		super(ID, ime, prezime, null, null, null, null, null, 0, 0, 0, 0);
		
	}
	
	public Recepcioner() {
		super(0, null, null, null, null, null, null, null, 0, 0, 0, 0);
	}
	
	//ISPIS IZ FAJLA
	public static ArrayList<Recepcioner> ucitajRecepcionere(String path) {
			
	
			String line = "";
			ArrayList<Recepcioner> recepcioneri = new ArrayList<Recepcioner>();
	
			try {
				BufferedReader br = new BufferedReader(new FileReader(path));
				
				while((line = br.readLine()) != null ) {
					String[] values = line.split(",");
					Recepcioner recepcioner = new Recepcioner();
					recepcioner.setID(Integer.parseInt(values[0]));
					recepcioner.setIme(values[1]);
					recepcioner.setPrezime(values[2]);
					recepcioner.setPol(values[3]);
					recepcioner.setTelefon(values[4]);
					recepcioner.setAdresa(values[5]);
					recepcioner.setKorisnickoIme(values[6]);
					recepcioner.setLozinka(values[7]);
					recepcioner.setStrucnaSprema(Double.parseDouble(values[8]));
					recepcioner.setStaz(Double.parseDouble(values[9]));
					recepcioner.setBonus(Double.parseDouble(values[10]));
					recepcioner.setPlata(Double.parseDouble(values[11]));
					recepcioneri.add(recepcioner);		
				}
				br.close();
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			return recepcioneri;
		}
	
	//UPIS U FAJL
	public static void sacuvajRecepcionere(ArrayList<Recepcioner> recepcioneri, String path) {
	    try {
	        BufferedWriter bw = new BufferedWriter(new FileWriter(path));

	        for (Recepcioner recepcioner : recepcioneri) {
	            

	            
	            bw.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%f,%f,%f,%f\n", 
	            		recepcioner.getID(), recepcioner.getIme(), recepcioner.getPrezime(), recepcioner.getPol(), recepcioner.getTelefon(),
	            		recepcioner.getAdresa(), recepcioner.getKorisnickoIme(), recepcioner.getLozinka(), recepcioner.getStrucnaSprema(), recepcioner.getStaz(), recepcioner.getBonus(), recepcioner.getPlata()));
	        }

	        bw.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	}

	
	
	
	public static void ispisiRecepcionere(ArrayList<Recepcioner> recepcioneri) {
	    for (Recepcioner r : recepcioneri) {
	        System.out.println(r);
	    }
	}
	
	@Override
	public String toString() {
		return "Recepcioner[" + getID() + ", " + getIme() + ", " + getPrezime() + ", " + getPol() + ", " + getTelefon() + ", " + getAdresa() + ", " + getKorisnickoIme() + ", "
				+ getLozinka() + ", " + getStrucnaSprema() + ", " + getStaz() + ", " + getBonus() + ", " + getPlata() + "]";
	}
}
