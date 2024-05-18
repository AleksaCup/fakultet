package entity;

import java.util.ArrayList;
import funkcije.ManagerKozmeticar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Kozmeticar extends Zaposleni {
	
	private ArrayList<tipTretmana> obucenZa = new ArrayList<>();

	public Kozmeticar(int ID, String ime, String prezime, String pol, String telefon, String adresa, String korisnickoIme,
			String lozinka, double strucnaSprema, double staz, double bonus, double plata, ArrayList<tipTretmana> obucenZa) {
		
		super(ID, ime, prezime, pol, telefon, adresa, korisnickoIme, lozinka, strucnaSprema, staz, bonus, plata);
		this.obucenZa = obucenZa;
		
	}
	
	public Kozmeticar(String ime, ArrayList<tipTretmana> obucenZa){
		super(ime);
		this.obucenZa = obucenZa;
		
	}
	

	public Kozmeticar() {
	        super(0, null, null, null, null, null, null, null, 0, 0, 0, 0);
	        this.obucenZa = null;
	}
	
	public Kozmeticar(String ime) {
		super(0, ime, null, null, null, null, null, null, 0, 0, 0, 0);
        this.obucenZa = null;
	}
	
	public Kozmeticar(int ID, String ime, String prezime) {
		super(ID, ime, prezime, null, null, null, null, null, 0, 0, 0, 0);
        this.obucenZa = null;
	}
	
	public static void ukloniKozmeticara(ArrayList<Kozmeticar> kozmeticari, Kozmeticar kozmeticar) {
	    kozmeticari.removeIf(k -> k.equals(kozmeticar));
	}
	
	public static void ukloniKozmeticaraById(ArrayList<Kozmeticar> kozmeticari, int id) {
	    kozmeticari.removeIf(k -> k.getID() == id);
	}
	
	public static void updateKozmeticarByID(int ID, String ime, String prezime, String pol, String telefon, String adresa,
	        String korisnickoIme, String lozinka, int strucnaSprema, int staz, int bonus, int plata,
	        ArrayList<tipTretmana> obucenZa) {
		ArrayList<Kozmeticar>kozmeticariList = ucitajKozmeticare("data/kozmeticari.csv");
	    for (Kozmeticar k : kozmeticariList) {
	        if (k.getID() == ID) {
	            k.setIme(ime);
	            k.setPrezime(prezime);
	            k.setPol(pol);
	            k.setTelefon(telefon);
	            k.setAdresa(adresa);
	            k.setKorisnickoIme(korisnickoIme);
	            k.setLozinka(lozinka);
	            k.setStrucnaSprema(strucnaSprema);
	            k.setStaz(staz);
	            k.setBonus(bonus);
	            k.setPlata(plata);
	            k.setObucenZa(obucenZa);
	            return;
	        }
	    }
	    throw new IllegalArgumentException("No Kozmeticar with ID " + ID + " found.");
	}
		
	
	public static ArrayList<Kozmeticar> ucitajKozmeticare(String path) {
		

		String line = "";
		ArrayList<Kozmeticar> kozmeticari = new ArrayList<Kozmeticar>();
		

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			
			while((line = br.readLine()) != null ) {
				String[] values = line.split(",");
				Kozmeticar kozmeticar = new Kozmeticar();
				kozmeticar.setID(Integer.parseInt(values[0]));
		        kozmeticar.setIme(values[1]);
		        kozmeticar.setPrezime(values[2]);
		        kozmeticar.setPol(values[3]);
		        kozmeticar.setTelefon(values[4]);
		        kozmeticar.setAdresa(values[5]);
		        kozmeticar.setKorisnickoIme(values[6]);
		        kozmeticar.setLozinka(values[7]);
		        kozmeticar.setStrucnaSprema(Double.parseDouble(values[8]));
		        kozmeticar.setStaz(Double.parseDouble(values[9]));
		        kozmeticar.setBonus(Double.parseDouble(values[10]));
		        kozmeticar.setPlata(Double.parseDouble(values[11]));
		        
		        String[] obucenZa = values[12].split("-");
		        ArrayList<tipTretmana> tipovi = new ArrayList<>();
		        for (String tip : obucenZa) {
		        	tipTretmana type = new tipTretmana(tip);
		            tipovi.add(type);
		        }
		        kozmeticar.setObucenZa(tipovi);

		        kozmeticari.add(kozmeticar);		
			}
			br.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return kozmeticari;
	}
	
	public static void sacuvajKozmeticare(ArrayList<Kozmeticar> kozmeticari, String path) {
	    try {
	        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
	        
	        
	        
	        
	        for (Kozmeticar kozmeticar : kozmeticari) {
	        	bw.write(kozmeticar.getID() + ",");
	            bw.write(kozmeticar.getIme() + ",");
	            bw.write(kozmeticar.getPrezime() + ",");
	            bw.write(kozmeticar.getPol() + ",");
	            bw.write(kozmeticar.getTelefon() + ",");
	            bw.write(kozmeticar.getAdresa() + ",");
	            bw.write(kozmeticar.getKorisnickoIme() + ",");
	            bw.write(kozmeticar.getLozinka() + ",");
	            bw.write(kozmeticar.getStrucnaSprema() + ",");
	            bw.write(kozmeticar.getStaz() + ",");
	            bw.write(kozmeticar.getBonus() + ",");
	            bw.write(kozmeticar.getPlata() + ",");
	            ArrayList<tipTretmana> obucenZa = kozmeticar.getObucenZa();
		        StringBuilder sb = new StringBuilder();

		        for (int i = 0; i < obucenZa.size(); i++) {
		            sb.append(obucenZa.get(i).getImeTretmana());

		            if (i < obucenZa.size() - 1) {
		                sb.append("-");
		            }
		        }
		        
	            bw.write(sb.toString() + "");
	            bw.newLine();
	        }
	        
	        bw.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void ispisiKozmeticare(ArrayList<Kozmeticar> kozmeticari) {
	    for (Kozmeticar k : kozmeticari) {
	        System.out.println(k);
	    }
	}
	
	public static Kozmeticar nadjiKozmeticaraPoID(int id) {
		for(Kozmeticar koz : ManagerKozmeticar.svikozmeticari) {
			if(koz.getID() == id) {
				return koz;
			}
		}
		return null;
	}

	


	public ArrayList<tipTretmana> getObucenZa() {
		return obucenZa;
	}

	public void setObucenZa(ArrayList<tipTretmana> obucenZa) {
		this.obucenZa = obucenZa;
	}
	
	

	@Override
	public String toString() {
		return "Kozmeticar[" + getID() + ", " + getIme() + ", " + getPrezime() + ", " + getPol() + ", " + getTelefon() + ", " + getAdresa() + ", " + getKorisnickoIme() + ", " + getLozinka() + ", " + getObucenZa() + "]";
	}
	
	
	
	
	

}
