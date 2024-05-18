package entity;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import enums.Stanje;

import funkcije.*;

public class zakazanTretman {
	private uslugaTretman usluga;
	private Kozmeticar izabraniKozmeticar;
	private LocalDateTime termin;
	private Stanje stanjeTretmana;
	private Klijent klijent;
	private double naplaceno;
	
	public zakazanTretman(){
		this.setUslugu(null);
		this.setIzabraniKozmeticar(null);
		this.setTermin(null);
		this.setStanjeTretmana(null);
		this.setKlijent(null);
		this.setNaplaceno(0);
	}
	
	public zakazanTretman(uslugaTretman usluga, Kozmeticar izabraniKozmeticar, LocalDateTime termin, Stanje stanjeTretmana, Klijent klijent, Double naplaceno){
		this.setUslugu(usluga);
		this.setIzabraniKozmeticar(izabraniKozmeticar);
		this.setTermin(termin);
		this.setStanjeTretmana(stanjeTretmana);
		this.setKlijent(klijent);
		this.setNaplaceno(naplaceno);
	}

	public zakazanTretman(uslugaTretman usluga, Kozmeticar izabraniKozmeticar, Stanje stanjeTretmana, Klijent klijent, Double naplaceno) {
		this.setUslugu(usluga);
		this.setIzabraniKozmeticar(izabraniKozmeticar);
		this.setTermin(null);
		this.setStanjeTretmana(stanjeTretmana);
		this.setKlijent(klijent);
		this.setNaplaceno(naplaceno);
	}



	public Klijent getKlijent() {
		return klijent;
	}



	public void setKlijent(Klijent klijent) {
		this.klijent = klijent;
	}



	public uslugaTretman getUslugu() {
		return usluga;
	}

	public void setUslugu(uslugaTretman usluga) {
		this.usluga = usluga;
	}

	public Kozmeticar getIzabraniKozmeticar() {
		return izabraniKozmeticar;
	}

	public void setIzabraniKozmeticar(Kozmeticar izabraniKozmeticar) {
		this.izabraniKozmeticar = izabraniKozmeticar;
	}

	public LocalDateTime getTermin() {
		return termin;
	}

	public void setTermin(LocalDateTime termin) {
		this.termin = termin;
	}

	public Stanje getStanjeTretmana() {
		return stanjeTretmana;
	}

	public void setStanjeTretmana(Stanje stanjeTretmana) {
		this.stanjeTretmana = stanjeTretmana;
	}
	
	public double getNaplaceno() {
		return naplaceno;
	}

	public void setNaplaceno(double naplaceno) {
		this.naplaceno = naplaceno;
	}
	
	
	
	//CITANJE IZ FAJLA
			public static ArrayList<zakazanTretman> ucitajZakazaneTretmane(String path) {
					
			
					String line = "";
					ArrayList<zakazanTretman> zakazanitretmani = new ArrayList<zakazanTretman>();
			
					try {
						BufferedReader br = new BufferedReader(new FileReader(path));
						
						while((line = br.readLine()) != null ) {
							String[] values = line.split(",");
							zakazanTretman zakazantretman = new zakazanTretman();
							zakazantretman.setUslugu(uslugaTretman.nadjiUsluguPoID(Integer.parseInt(values[0])));
							zakazantretman.setIzabraniKozmeticar(Kozmeticar.nadjiKozmeticaraPoID(Integer.parseInt(values[1])));
							zakazantretman.setTermin(Funkcionalnosti.stringToDate(values[2]));
							zakazantretman.setStanjeTretmana(Stanje.valueOf(values[3]));
							zakazantretman.setKlijent(Klijent.nadjiKlijentaPoID(Integer.parseInt(values[4])));
							zakazantretman.setNaplaceno(Double.parseDouble(values[5]));
							zakazanitretmani.add(zakazantretman);		
						}
						br.close();
					}
					catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
					
					return zakazanitretmani;
				}
			
			//UPIS U FAJL
			public static void sacuvajZakazaneTretmane(ArrayList<zakazanTretman> zakazanitretmani, String path) {
			    try {
			        BufferedWriter bw = new BufferedWriter(new FileWriter(path));

			        for (zakazanTretman zakazan : zakazanitretmani) {
			            
			            bw.write(String.format("%d,%d,%s,%s,%d,%f\n", 
			            		zakazan.getUslugu().getIDUsluge(), zakazan.getIzabraniKozmeticar().getID(), Funkcionalnosti.dateToString(zakazan.getTermin()), zakazan.getStanjeTretmana(), zakazan.getKlijent().getID(), zakazan.getNaplaceno()));
			        }

			        bw.close();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			    
			}
	
	

	@Override
	public String toString() {
		return usluga + ", " + izabraniKozmeticar + ", " + termin + ", " + stanjeTretmana + ", " + klijent;
	}
	
	
		
	
	
	
	
	
	
	
	
	
}
