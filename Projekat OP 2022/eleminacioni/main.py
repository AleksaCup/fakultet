from datetime import datetime
import csv
import re

def ucitaj_ispite(putanja: str, separator: str):
    with open(putanja, 'r', newline='') as datoteka:
        svi_ispiti = {}

        polja = ['broj_indeksa', 'predmet', 'ime', 'prezime', 'datum_polaganja', 'broj_bodova']

        ispiti = csv.DictReader(datoteka, fieldnames = polja, delimiter=separator)
        for red in ispiti:
            
            svi_ispiti[red['broj_indeksa']] = red
        return svi_ispiti
    
def sacuvaj_ispit(putanja: str, separator: str, svi_ispiti: dict):
    with open(putanja, 'w', newline='') as datoteka:
        polja = ['broj_indeksa', 'predmet', 'ime', 'prezime', 'datum_polaganja', 'broj_bodova']
        let = csv.DictWriter(datoteka, fieldnames=polja, delimiter=separator)
        for i in svi_ispiti:
            let.writerow(svi_ispiti[i])


if __name__ == "__main__":
    program = True
    while program:
        print("\nIzbor:")
        print("x ili X: IZLAZAK IZ APLIKACIJE")
        print("1: DODAVANJE NOVOG ISPITA")
        print("2: ČITANJE SVIH ISPITA")

        opcija = input("Unesite opciju: ")

        if opcija == "x" or opcija == "X":
            print("Uspešno ste izašli iz aplikacije!")
            exit()
        elif opcija == "1":
            svi_ispiti = ucitaj_ispite("ispiti.csv", "|")
            broj_indeksa = ""
            while broj_indeksa == "":
                broj_indeksa = input("Unesite broj indeksa: ") 
            predmet = ""
            while predmet == "":
             predmet = input("Unesite naziv predmeta: ")
            ime = ""
            while ime == "":
                ime = input("Unesite ime: ")
            prezime = ""
            while prezime == "":
                prezime = input("Unesite prezime: ")
            datum_polaganja = ""
            format_datuma = r'\d{2}\.\d{2}\.\d{4}'           
            while datum_polaganja == "" or not re.match(format_datuma, datum_polaganja):
                datum_polaganja = input("Unesite datum polaganja: ")
            broj_bodova = -2
            while broj_bodova < 0 or broj_bodova > 100:
                broj_bodova = input("Unesite broj bodova: ")
                if broj_bodova == "":
                    broj_bodova = -2
                else:
                    broj_bodova =int(broj_bodova)                

            svi_ispiti[broj_indeksa] = {
                'broj_indeksa': broj_indeksa, 
                'predmet': predmet, 
                'ime': ime, 
                'prezime': prezime, 
                'datum_polaganja': datum_polaganja, 
                'broj_bodova': broj_bodova
            }
            sacuvaj_ispit("ispiti.csv", "|", svi_ispiti)
            print("\nUspešno ste upisali novi ispit u fajl!")
        elif opcija == "2":
            ispiti = ucitaj_ispite("ispiti.csv", "|")
            print(f"Broj indeksa".ljust(20), "Predmet".ljust(20), "Ime".ljust(20), "Prezime".ljust(20), "Datum polaganja".ljust(20), "Broj bodova".ljust(20))
            for ispit in ispiti.values():
                print(ispit['broj_indeksa'].ljust(20), ispit['predmet'].ljust(20), ispit['ime'].ljust(20), ispit['prezime'].ljust(20), ispit['datum_polaganja'].ljust(20), ispit['broj_bodova'].ljust(20))

        else:
            print("Loš unos!")
