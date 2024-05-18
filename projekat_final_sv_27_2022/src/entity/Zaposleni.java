package entity;

public class Zaposleni extends Korisnik {
	
	private double strucnaSprema, staz, bonus, plata;

	public Zaposleni(int ID, String ime, String prezime, String pol, String telefon, String adresa, String korisnickoIme,
			String lozinka, double strucnaSprema, double staz, double bonus, double plata) {
		super(ID, ime, prezime, pol, telefon, adresa, korisnickoIme, lozinka);
		this.setStrucnaSprema(strucnaSprema);
		this.setStaz(staz);
		this.setBonus(bonus);
		this.setPlata(plata);
		
	}
	
	public Zaposleni(double strucnaSprema, double staz, double bonus, double plata) {
		super(1234, null, null, null, null, null, null, null);
		this.setStrucnaSprema(strucnaSprema);
		this.setStaz(staz);
		this.setBonus(bonus);
		this.setPlata(plata);
		
	}
	
	public Zaposleni(String ime){
		super(ime);
	}

	public double getStrucnaSprema() {
		return strucnaSprema;
	}

	public void setStrucnaSprema(double strucnaSprema) {
		this.strucnaSprema = strucnaSprema;
	}

	public double getStaz() {
		return staz;
	}

	public void setStaz(double staz) {
		this.staz = staz;
	}

	public double getBonus() {
		return bonus;
	}

	public void setBonus(double bonus) {
		this.bonus = bonus;
	}

	public double getPlata() {
		return plata;
	}

	public void setPlata(double plata) {
		this.plata = plata;
	}
	
	
	
	
	

}
