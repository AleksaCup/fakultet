package entity;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



import enums.Stanje;
import funkcije.ManagerZakazanTretman;
import main.Main;


public class Salon {
	
	private String nazivSalona;
	private LocalTime pocetakradnogvremena;
	private LocalTime krajradnogvremena;
	private double vrednostZaKL;
	private double bilans;
	
	public Salon() {
		this.setNazivSalona(null);
		this.setPocetakradnogvremena(null);
		this.setKrajradnogvremena(null);
		this.setVrednostZaKL(0);
		this.setBilans(0);
	}
	
	public Salon(String nazivSalona, LocalTime pocetakradnogvremena, LocalTime krajradnogvremena, double vrednostZaKL, double bilans) {
		this.setNazivSalona(nazivSalona);
		this.setPocetakradnogvremena(pocetakradnogvremena);
		this.setKrajradnogvremena(krajradnogvremena);
		this.setVrednostZaKL(vrednostZaKL);
		this.setBilans(bilans);
	}

	public String getNazivSalona() {
		return nazivSalona;
	}

	public void setNazivSalona(String nazivSalona) {
		this.nazivSalona = nazivSalona;
	}

	public LocalTime getPocetakradnogvremena() {
		return pocetakradnogvremena;
	}

	public void setPocetakradnogvremena(LocalTime pocetakradnogvremena) {
		this.pocetakradnogvremena = pocetakradnogvremena;
	}

	public LocalTime getKrajradnogvremena() {
		return krajradnogvremena;
	}

	public void setKrajradnogvremena(LocalTime krajradnogvremena) {
		this.krajradnogvremena = krajradnogvremena;
	}
	
	public double getVrednostZaKL() {
		return vrednostZaKL;
	}

	public void setVrednostZaKL(double VrednostZaKL) {
		this.vrednostZaKL = VrednostZaKL;
	}
	
	public double getBilans() {
		return bilans;
	}

	public void setBilans(double bilans) {
		this.bilans = bilans;
	}
	
	public static void pocetniBilans() {
		double bilans = 0;
		for(zakazanTretman t : ManagerZakazanTretman.svizakazani) {
			if(t.getStanjeTretmana().equals(Stanje.ZAKAZAN) || t.getStanjeTretmana().equals(Stanje.IZVRSEN)) {
				bilans += t.getUslugu().getCena();
			}
		}
		Main.salon.setBilans(bilans);
		
	}
	
	
public static Salon ucitajSalon(String path) {
		

		String line = "";
		Salon salon = new Salon();

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			
			while((line = br.readLine()) != null ) {
				String[] values = line.split(",");

				salon.setNazivSalona(values[0]);
				salon.setPocetakradnogvremena(LocalTime.parse(values[1], DateTimeFormatter.ofPattern("HH:mm")));
		        salon.setKrajradnogvremena(LocalTime.parse(values[2], DateTimeFormatter.ofPattern("HH:mm")));
		        salon.setVrednostZaKL(Double.parseDouble(values[3]));
		        salon.setBilans(Double.parseDouble(values[3]));		
			}
			br.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return salon;
	}
	
	public static void sacuvajSalon(Salon salon, String path) {
	    try {
	        BufferedWriter bw = new BufferedWriter(new FileWriter(path));

	        	String pocetakRadnogVremena = salon.getPocetakradnogvremena().format(DateTimeFormatter.ofPattern("HH:mm"));
	        	String krajRadnogVremena = salon.getKrajradnogvremena().format(DateTimeFormatter.ofPattern("HH:mm"));
	            
	            bw.write(String.format("%s,%s,%s,%f,%f", 
	            		salon.getNazivSalona(), pocetakRadnogVremena, krajRadnogVremena, salon.getVrednostZaKL(), salon.getBilans()));
	        

	        bw.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	}

	
	
	
	
	
	@Override
	public String toString() {
		return "\"" + nazivSalona + "\" radi od: " + pocetakradnogvremena + "h do:" + krajradnogvremena + "h";
	}
	
	

}
