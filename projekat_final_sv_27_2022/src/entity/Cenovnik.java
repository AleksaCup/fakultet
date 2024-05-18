package entity;

public class Cenovnik {
	
	
	public uslugaTretman tretman;
	public double cena;
	
	public Cenovnik(uslugaTretman tretman, double cena){
		this.tretman = tretman;
		this.cena = cena;
	}

	public uslugaTretman getTretman() {
		return tretman;
	}

	public void setTretman(uslugaTretman tretman) {
		this.tretman = tretman;
	}

	public double getCena() {
		return cena;
	}

	public void setCena(double cena) {
		this.cena = cena;
	}

	

	@Override
	public String toString() {
		return "Ime usluge: " + getTretman().getImeUsluge() + ", cena: " + getCena() + " RSD";
	}
	
	

}
