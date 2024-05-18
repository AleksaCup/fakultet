from common import konstante
from functools import reduce
from datetime import datetime
import csv

"""
Brojačka promenljiva koja se automatski povećava pri kreiranju nove karte.
"""
sledeci_broj_karte = 1

"""
Kupovina karte proverava da li prosleđeni konkretni let postoji i da li ima slobodnih mesta. U tom slučaju se karta 
dodaje  u kolekciju svih karata. Slobodna mesta se prosleđuju posebno iako su deo konkretnog leta, zbog lakšeg 
testiranja. Baca grešku ako podaci nisu validni.
kwargs moze da prihvati prodavca kao recnik, i datum_prodaje kao datetime
recnik prodavac moze imati id i ulogu
CHECKPOINT 2: kupuje se samo za ulogovanog korisnika i bez povezanih letova.
ODBRANA: moguće je dodati saputnike i odabrati povezane letove. 
"""
def kupovina_karte(
    sve_karte: dict,
    svi_konkretni_letovi: dict,
    sifra_konkretnog_leta: int,
    putnici: list,
    slobodna_mesta: list,
    kupac: dict,
    **kwargs #prodavac i datum prodaje
) -> dict:
    # Proveri da li je kupac korisnik
    if kupac["uloga"] != konstante.ULOGA_KORISNIK:
        raise Exception("Kupac mora biti korisnik!")
    # Proveri da li je kupac prodavac, prodavac ne može da kupi kartu
    if kupac["uloga"] == konstante.ULOGA_PRODAVAC:
        raise Exception("Prodavac ne može da kupi kartu!")
    # Proveri da li let postoji
    if sifra_konkretnog_leta not in svi_konkretni_letovi:
        raise Exception("Nepostojeći let!")

    global sledeci_broj_karte

    karta = {
        "broj_karte": sledeci_broj_karte,
        "putnici": putnici,
        "sifra_konkretnog_leta": sifra_konkretnog_leta,
        "status": konstante.STATUS_NEREALIZOVANA_KARTA,
        "kupac": kupac,
        "prodavac": kwargs["prodavac"],
        "datum_prodaje": kwargs["datum_prodaje"],
        "obrisana": False
    }

    sledeci_broj_karte += 1

    sve_karte[karta["broj_karte"]] = karta

    return karta

"""
Vraća sve nerealizovane karte za korisnika u listi.
"""
def pregled_nerealizovanaih_karata(korisnik: dict, sve_karte: iter) -> list:
    nerealizovane_karte = []
    for karta in sve_karte:
        if karta["status"] == konstante.STATUS_NEREALIZOVANA_KARTA:
            if korisnik in karta["putnici"]:
                nerealizovane_karte.append(karta)
    return nerealizovane_karte


"""
 Funkcija brisanja karte se ponaša drugačije u zavisnosti od korisnika:
- Prodavac: karta se označava za brisanje
- Admin/menadžer: karta se trajno briše
Kao rezultat se vraća nova kolekcija svih karata. Baca grešku ako podaci nisu validni.
"""
def brisanje_karte(korisnik: dict, sve_karte: dict, broj_karte: int) -> dict:
    if korisnik["uloga"] == konstante.ULOGA_KORISNIK:
        raise Exception("Običan korisnik ne može da obriše kartu!")
    elif korisnik["uloga"] == konstante.ULOGA_PRODAVAC:
        if broj_karte not in sve_karte:
            raise Exception("Brisanje nepostojeće karte!")
        else:
            sve_karte[broj_karte]["obrisana"] = True
            return sve_karte
    elif korisnik["uloga"] == konstante.ULOGA_ADMIN:
        if broj_karte not in sve_karte:
            raise Exception("Brisanje nepostojeće karte!")
        else:
            sve_karte.pop(broj_karte)
            return sve_karte


"""
Funkcija koja čuva sve karte u fajl na zadatoj putanji.
"""
def sacuvaj_karte(sve_karte: dict, putanja: str, separator: str):
    with open(putanja, 'w') as datoteka:
        polja = ['broj_karte', 'sifra_konkretnog_leta', 'kupac', 'prodavac', 'sifra_sedista', 'datum_prodaje', 'obrisana']
        karte = csv.DictWriter(datoteka, fieldnames=polja, delimiter=separator)
        for karta in sve_karte:
            karte.writerow(sve_karte[karta])

"""
Funkcija koja učitava sve karte iz fajla i vraća ih u rečniku.
"""
def ucitaj_karte_iz_fajla(putanja: str, separator: str) -> dict:
    with open(putanja, 'r') as datoteka:
        polja = ['broj_karte', 'sifra_konkretnog_leta', 'kupac', 'prodavac', 'sifra_sedista', 'datum_prodaje', 'obrisana']
        karte = csv.DictReader(datoteka, fieldnames=polja, delimiter=separator)
        sve_karte = {}
        for red in karte:
            #Broj karte i šifra konkretnog leta moraju biti int dok je polje 'obrisana logička promenljiva i ona mora biti True ili False'
            red['broj_karte'] = int(red['broj_karte'])
            red['sifra_konkretnog_leta'] = int(red['sifra_konkretnog_leta'])
            if red['obrisana'] == 'False' or red['obrisana'] == 'false':
                red['obrisana'] = False
            elif red['obrisana'] == 'True' or red['obrisana'] == 'true':
                red['obrisana'] = True
            else:
                raise Exception("Polje 'obrisana' mora biti logička promenljiva True ili False!")
            sve_karte[red['broj_karte']] = red
    return sve_karte

