import common.konstante
from common import konstante
import csv

"""
Funkcija koja kreira novi rečnik koji predstavlja korisnika sa prosleđenim vrednostima. Kao rezultat vraća kolekciju
svih korisnika proširenu novim korisnikom. Može se ponašati kao dodavanje ili ažuriranje, u zavisnosti od vrednosti
parametra azuriraj:
- azuriraj == False: kreira se novi korisnik. staro_korisnicko_ime ne mora biti prosleđeno.
Vraća grešku ako korisničko ime već postoji.
- azuriraj == True: ažurira se postojeći korisnik. Staro korisnicko ime mora biti prosleđeno. 
Vraća grešku ako korisničko ime ne postoji.

Ova funkcija proverava i validnost podataka o korisniku, koji su tipa string.

CHECKPOINT 1: Vraća string sa greškom ako podaci nisu validni.
    Hint: Postoji string funkcija koja proverava da li je string broj bez bacanja grešaka. Probajte da je pronađete.
ODBRANA: Baca grešku sa porukom ako podaci nisu validni.
"""
def kreiraj_korisnika(svi_korisnici: dict, azuriraj: bool, uloga: str, staro_korisnicko_ime: str, 
                      korisnicko_ime: str, lozinka: str, ime: str, prezime: str, email: str = "",
                      pasos: str = "", drzavljanstvo: str = "",
                      telefon: str = "", pol: str = "") -> dict:
    if not azuriraj and korisnicko_ime in svi_korisnici:
        raise Exception("Greška. Korisničko ime već postoji!")
    if azuriraj and staro_korisnicko_ime not in svi_korisnici:
        raise Exception("Greška. Staro korisničko ime ne postoji!")
    if azuriraj and korisnicko_ime in svi_korisnici and korisnicko_ime != staro_korisnicko_ime:
        raise Exception("Greška!")

    korisnik = [uloga, korisnicko_ime, lozinka, ime, prezime, email, pasos, drzavljanstvo, telefon, pol]
    if None in korisnik or "" in korisnik:
        raise Exception("Greška!")

    # uloga
    if uloga not in [konstante.ULOGA_PRODAVAC, konstante.ULOGA_KORISNIK, konstante.ULOGA_ADMIN]:
        raise Exception("Greška! Nepostojeća uloga")
    # korisnicko_ime
    if not korisnicko_ime.isalpha():
        raise Exception("Greška! Korisničko ime mora biti string!")
    # lozinka
    if not lozinka.isalpha():
        raise Exception("Greška! Lozinka mora biti string!")
    # ime
    if not ime.isalpha():
        raise Exception("Greška! Ime mora biti string!")
    # prezime
    if not prezime.isalpha():
        raise Exception("Greška! Prezime mora biti string!")
    # pasos
    if len(pasos) != 9 or not pasos.isnumeric():  # !!!!
        raise Exception("Greška! Pasoš se mora sastojati od 9 cifara!")
    # email:
    if email.count("@") != 1:
        raise Exception("Greška! Email mora da sadrži tačno jedno @!")
    else:
        domen = email.split("@")[1]
        if domen.count(".") >= 2:
            raise Exception("Greška! Domen posle @ mora da sadrži tačno jednu tačku!")

    # drzavljanstvo
    if not drzavljanstvo.isalpha():
        raise Exception("Greška! Državljanstvo mora biti string!")
    # telefon
    if not telefon.isnumeric():
        raise Exception("Greška! Telefon mora biti broj!")
    # pol
    if not pol.isalpha():
        raise Exception("Greška! Pol mora biti string!")

    if not azuriraj:
        if korisnicko_ime not in svi_korisnici:
            svi_korisnici[korisnicko_ime] = {"uloga": uloga, "korisnicko_ime": korisnicko_ime, "lozinka": lozinka,
                                             "ime": ime, "prezime": prezime, "email": email, "pasos": pasos,
                                             "drzavljanstvo": drzavljanstvo, "telefon": telefon, "pol": pol}
            return svi_korisnici
        raise Exception("Greška! Kreiranje novog korisnika nije uspelo!")

    if azuriraj:
        if staro_korisnicko_ime in svi_korisnici or staro_korisnicko_ime == korisnicko_ime:
            svi_korisnici[staro_korisnicko_ime] = {"uloga": uloga, "korisnicko_ime": korisnicko_ime, "lozinka": lozinka,
                                                   "ime": ime, "prezime": prezime, "email": email, "pasos": pasos,
                                                   "drzavljanstvo": drzavljanstvo, "telefon": telefon, "pol": pol}
            return svi_korisnici
        raise Exception("Greška! Ažuriranje korisnika nije uspelo!")

"""
Funkcija koja čuva podatke o svim korisnicima u fajl na zadatoj putanji sa zadatim separatorom.
"""
def sacuvaj_korisnike(putanja: str, separator: str, svi_korisnici: dict):
    with open(putanja, 'w') as datoteka:
        polja=['uloga', 'korisnicko_ime', 'lozinka', 'ime', 'prezime', 'email', 'pasos', 'drzavljanstvo', 'telefon', 'pol']
        korisnik = csv.DictWriter(datoteka, fieldnames=polja, delimiter=separator)
        for element in svi_korisnici:
            korisnik.writerow(svi_korisnici[element])


"""
Funkcija koja učitava sve korisnika iz fajla na putanji sa zadatim separatorom. Kao rezultat vraća učitane korisnike.
"""
def ucitaj_korisnike_iz_fajla(putanja: str, separator: str) -> dict:
    with open(putanja, 'r') as datoteka:
        korisnici = {}

        polja = ['uloga', 'korisnicko_ime', 'lozinka', 'ime', 'prezime', 'email', 'pasos', 'drzavljanstvo', 'telefon', 'pol']

        korisnik = csv.DictReader(datoteka, fieldnames=polja, delimiter=separator)
        for red in korisnik:
            korisnici[red["korisnicko_ime"]] = red

        return korisnici


"""
Funkcija koja vraća korisnika sa zadatim korisničkim imenom i šifrom.
CHECKPOINT 1: Vraća string sa greškom ako korisnik nije pronađen.
ODBRANA: Baca grešku sa porukom ako korisnik nije pronađen.
"""
def login(svi_korisnici, korisnicko_ime, lozinka) -> dict:
    korisnik = svi_korisnici[korisnicko_ime]
    if korisnik:
        if lozinka == korisnik["lozinka"]:
            return korisnik
    raise Exception("Greška. Neuspešan login!")

"""
Funkcija koja vrsi log out
*
"""
def logout(korisnicko_ime: str):
    return (f"Odjavljeni ste {korisnicko_ime}")

