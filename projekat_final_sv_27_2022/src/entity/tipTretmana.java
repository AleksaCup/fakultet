package entity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class tipTretmana {
	
	public static ArrayList<tipTretmana> svitipovi = ucitajTipove("data/tipovi.csv");

	public String imeTretmana;

	
	
	
	public tipTretmana(String imeTretmana){
		this.imeTretmana = imeTretmana;
	}
	
	public tipTretmana(){
		this.imeTretmana = null;
	}
	
	public String getImeTretmana() {
		return imeTretmana;
	}

	public void setImeTretmana(String imeTretmana) {
		this.imeTretmana = imeTretmana;
	}

	public static void ispisiTipove(ArrayList<tipTretmana> tipovi) {
	    for (tipTretmana t : tipovi) {
	        System.out.println(t);
	    }
	}
	
	public static ArrayList<tipTretmana> ucitajTipove(String path) {
		

		String line = "";
		ArrayList<tipTretmana> tipovi = new ArrayList<>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			
			while((line = br.readLine()) != null ) {
				String[] values = line.split(",");
				tipTretmana tip = new tipTretmana();
		        tip.setImeTretmana(values[0]);
		        tipovi.add(tip);		
			}
			br.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return tipovi;
	}
	
	public static void sacuvajTipove(ArrayList<tipTretmana> tipovi, String path) {
	    try {
	        BufferedWriter bw = new BufferedWriter(new FileWriter(path));

	        for (tipTretmana tip : tipovi) {
	            
	            bw.write(String.format("%s\n", tip.getImeTretmana()));
	        }

	        bw.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	}

	public static tipTretmana nadjiTipPoImenu(String ime) {
		for(tipTretmana t : svitipovi) {
			if(t.getImeTretmana().equals(ime)) {
				return t;
			}
		}
		return null;
	}
	
	@Override
 	public String toString() {
		return imeTretmana;
	}
	
	

}
