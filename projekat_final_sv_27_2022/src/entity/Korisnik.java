package entity;

import java.util.ArrayList;
import java.util.Objects;

public class Korisnik {
	
	private int ID;
	private String ime;
	private String prezime;
	private String pol;
	private String telefon;
	private String adresa;
	private String korisnickoIme;
	private String lozinka;
	
	Korisnik(int ID, String ime, String prezime, String pol, String telefon, String adresa, String korisnickoIme, String lozinka){
		this.setID(ID);
		this.setIme(ime);	
		this.setPrezime(prezime);
		this.setPol(pol);
		this.setTelefon(telefon);
		this.setAdresa(adresa);
		this.setKorisnickoIme(korisnickoIme);
		this.setLozinka(lozinka);
		
	}
	
	Korisnik(String ime){
		this.setIme(ime);	
	}
	
	Korisnik(String ime, String prezime){
		this.setIme(ime);	
		this.setPrezime(prezime);
	}
	
	
	
	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getPol() {
		return pol;
	}

	public void setPol(String pol) {
		this.pol = pol;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}
	
	
	public static void ispisiKorisnike(ArrayList<Korisnik> korisnici) {
	    for (Korisnik k : korisnici) {
	        System.out.println(k);
	    }
	}

	@Override
	public String toString() {
		return ID + ", " + ime + ", " + prezime + ", " + pol + ", " + telefon + ", " + adresa + ", " + korisnickoIme + ", " + lozinka;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ID, adresa, ime, korisnickoIme, lozinka, pol, prezime, telefon);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Korisnik other = (Korisnik) obj;
		return ID == other.ID && Objects.equals(adresa, other.adresa) && Objects.equals(ime, other.ime)
				&& Objects.equals(korisnickoIme, other.korisnickoIme) && Objects.equals(lozinka, other.lozinka)
				&& Objects.equals(pol, other.pol) && Objects.equals(prezime, other.prezime)
				&& Objects.equals(telefon, other.telefon);
	}

	
	
	

}
