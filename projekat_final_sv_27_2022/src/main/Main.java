package main;

import entity.*;
import funkcije.*;



public class Main {
	
	public static Salon salon = Salon.ucitajSalon("data/salon.csv");
	
	public static void main(String[] args) {
		
		Salon.pocetniBilans();
		Funkcionalnosti.openFrame("Start");

	}

}
