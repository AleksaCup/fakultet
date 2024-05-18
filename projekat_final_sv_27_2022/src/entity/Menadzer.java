package entity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Menadzer extends Zaposleni {

	public Menadzer(int ID, String ime, String prezime, String pol, String telefon, String adresa, String korisnickoIme,
			String lozinka, double strucnaSprema, double staz, double bonus, double plata) {
		super(ID, ime, prezime, pol, telefon, adresa, korisnickoIme, lozinka, strucnaSprema, staz, bonus, plata);

	}
	
	
	public Menadzer(int ID, String ime, String prezime) {
		super(ID, ime, prezime, null, null, null, null, null, 0, 0, 0, 0);
		
	}
	
	public Menadzer(){
		super(0, null, null, null, null, null, null, null, 0, 0, 0, 0);
	}
	
	//ISPIS IZ FAJLA
		public static ArrayList<Menadzer> ucitajMenadzere(String path) {
				
		
				String line = "";
				ArrayList<Menadzer> menadzeri = new ArrayList<Menadzer>();
		
				try {
					BufferedReader br = new BufferedReader(new FileReader(path));
					
					while((line = br.readLine()) != null ) {
						String[] values = line.split(",");
						Menadzer menadzer = new Menadzer();
						menadzer.setID(Integer.parseInt(values[0]));
						menadzer.setIme(values[1]);
						menadzer.setPrezime(values[2]);
						menadzer.setPol(values[3]);
						menadzer.setTelefon(values[4]);
						menadzer.setAdresa(values[5]);
						menadzer.setKorisnickoIme(values[6]);
						menadzer.setLozinka(values[7]);
						menadzer.setStrucnaSprema(Double.parseDouble(values[8]));
						menadzer.setStaz(Double.parseDouble(values[9]));
						menadzer.setBonus(Double.parseDouble(values[10]));
						menadzer.setPlata(Double.parseDouble(values[11]));
						menadzeri.add(menadzer);		
					}
					br.close();
				}
				catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				
				return menadzeri;
			}
		
		//UPIS U FAJL
		public static void sacuvajMenadzere(ArrayList<Menadzer> menadzeri, String path) {
		    try {
		        BufferedWriter bw = new BufferedWriter(new FileWriter(path));

		        for (Menadzer menadzer : menadzeri) {
		            

		            
		            bw.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%f,%f,%f,%f\n", 
		            		menadzer.getID(), menadzer.getIme(), menadzer.getPrezime(), menadzer.getPol(), menadzer.getTelefon(),
		            		menadzer.getAdresa(), menadzer.getKorisnickoIme(), menadzer.getLozinka(), menadzer.getStrucnaSprema(), menadzer.getStaz(), menadzer.getBonus(), menadzer.getPlata()));
		        }

		        bw.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    
		}

	

	public static void ispisiMenadzere(ArrayList<Menadzer> menadzeri) {
	    for (Menadzer m : menadzeri) {
	        System.out.println(m);
	    }
	}


	@Override
	public String toString() {
		return "Menadzer[" + getID() + ", " + getIme() + ", " + getPrezime() + ", " + getPol() + ", " + getTelefon() + ", " + getAdresa() + ", " + getKorisnickoIme() + ", "
				+ getLozinka() + ", " + getStrucnaSprema() + ", " + getStaz() + ", " + getBonus() + ", " + getPlata() + "]";
	}


	
	
	
	
}

