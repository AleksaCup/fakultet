package entity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class uslugaTretman {
	public static ArrayList<uslugaTretman> sveusluge = ucitajUsluge("data/usluge.csv");
	
	public int IDUsluge;
	public String ImeUsluge;
	public tipTretmana tip;
	public int duzinaTrajanja;
	public double cena;
	
	
	public uslugaTretman(int IDUsluge, String imeUsluge, tipTretmana tip, int duzinaTrajanja, double cena) {
		this.IDUsluge = IDUsluge;
		this.ImeUsluge = imeUsluge;
		this.tip = tip;
		this.duzinaTrajanja = duzinaTrajanja;
		this.cena = cena;
	}
	
	public uslugaTretman() {
		this.IDUsluge = 0;
		this.ImeUsluge = null;
		this.tip = null;
		this.duzinaTrajanja = 0;
		this.cena = 0;
	}
	
	public int getIDUsluge() {
		return IDUsluge;
	}


	public void setIDUsluge(int IDUsluge) {
		this.IDUsluge = IDUsluge;
	}


	public String getImeUsluge() {
		return ImeUsluge;
	}


	public void setImeUsluge(String imeUsluge) {
		this.ImeUsluge = imeUsluge;
	}


	public tipTretmana getTip() {
		return tip;
	}


	public void setTip(tipTretmana tip) {
		this.tip = tip;
	}


	public int getDuzinaTrajanja() {
		return duzinaTrajanja;
	}


	public void setDuzinaTrajanja(int duzinaTrajanja) {
		this.duzinaTrajanja = duzinaTrajanja;
	}


	public double getCena() {
		return cena;
	}


	public void setCena(double cena) {
		this.cena = cena;
	}
	
	public static void ispisiUsluge(ArrayList<uslugaTretman> usluga) {
	    for (uslugaTretman u : usluga) {
	        System.out.println(u);
	    }
	}
	
	public static uslugaTretman uslugaPoImenu(String nazivUsluge) {
		ArrayList<uslugaTretman> sveusluge = ucitajUsluge("data/usluge.csv");
		for(uslugaTretman u : sveusluge) {
			if(u.getImeUsluge().equals(nazivUsluge)) {
				return u;
			}
		}
		return null;
	}
	
	
	
	public static ArrayList<uslugaTretman> ucitajUsluge(String path) {
		
		

		String line = "";
		ArrayList<uslugaTretman> usluge = new ArrayList<uslugaTretman>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			
			while((line = br.readLine()) != null ) {
				String[] values = line.split(",");
				uslugaTretman usluga = new uslugaTretman();
				usluga.setIDUsluge(Integer.parseInt(values[0]));
				usluga.setImeUsluge(values[1]);
				usluga.setTip(new tipTretmana(values[2]));
				usluga.setDuzinaTrajanja(Integer.parseInt(values[3]));
				usluga.setCena(Double.parseDouble(values[4]));

				usluge.add(usluga);		
			}
			br.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return usluge;
	}
	
	public static void sacuvajUsluge(ArrayList<uslugaTretman> usluge, String path) {
	    try {
	        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
	        
	        
	        for (uslugaTretman usluga : usluge) {
	        	bw.write(usluga.getIDUsluge() + ",");
	        	bw.write(usluga.getImeUsluge() + ",");
	            bw.write(usluga.getTip().getImeTretmana() + ",");
	            bw.write(usluga.getDuzinaTrajanja() + ",");
	            bw.write(usluga.getCena() + "");
	            bw.newLine();
	        }
	        
	        bw.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static uslugaTretman nadjiUsluguPoID(int id) {
		ArrayList<uslugaTretman> usluge = uslugaTretman.ucitajUsluge("data/usluge.csv");
		for(uslugaTretman u : usluge) {
			if(u.getIDUsluge() == id) {
				return u;
			}
		}
		return null;
	}
	


	@Override
	public String toString() {
		return IDUsluge + ", " + ImeUsluge + ", " + tip + ", " + duzinaTrajanja + ", " + cena;
	}
	
	
	
	
	
	

}
