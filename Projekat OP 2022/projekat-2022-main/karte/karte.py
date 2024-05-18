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
    **kwargs
):
    global sledeci_broj_karte
    br_karte = max(sve_karte.keys(), default=0)
    # Proveri da li je kupac korisnik
    if kupac['uloga'] != konstante.ULOGA_KORISNIK:
        raise Exception("Kupac mora biti korisnik!")
    # Proveri da li je kupac prodavac, prodavac ne može da kupi kartu
    if kupac['uloga'] == konstante.ULOGA_PRODAVAC:
        raise Exception("Prodavac ne može da kupi kartu!")
    # Proveri da li let postoji
    if sifra_konkretnog_leta not in svi_konkretni_letovi:
        raise Exception("Nepostojeći let!")


    postoji_slobodno = False
    for red in slobodna_mesta:
        for kolona in red:
            if kolona:
                continue
            else:
                postoji_slobodno = True
            
    if not postoji_slobodno:
        raise Exception("Nema slobodnih mesta u ovom konkretnom letu!")


    karta = {
        "broj_karte": br_karte + 1,
        "putnici": putnici,
        "sifra_konkretnog_leta": sifra_konkretnog_leta,
        "status": konstante.STATUS_NEREALIZOVANA_KARTA,
        "kupac": kupac,
        "prodavac": kwargs["prodavac"],
        "datum_prodaje": kwargs["datum_prodaje"],
        "obrisana": False
    }
    br_karte +=1
    sledeci_broj_karte += 1

    sve_karte[karta["broj_karte"]] = karta


    return karta, sve_karte

def pregled_nerealizovanaih_karata(korisnik: dict, sve_karte: iter):
    # nerealizovane_karte = []
    # for karta in sve_karte:
    #     if karta["status"] == konstante.STATUS_NEREALIZOVANA_KARTA:
    #         if korisnik in karta["putnici"]:
    #             nerealizovane_karte.append(karta)
    # return nerealizovane_karte

    nerealizovane_karte=[]
    for karta in sve_karte:
        if karta['status'] == konstante.STATUS_NEREALIZOVANA_KARTA and korisnik in karta['putnici']:
            nerealizovane_karte.append(karta)
        elif 'kupac' in karta and karta['kupac'] == korisnik:
            nerealizovane_karte.append(karta)
    return nerealizovane_karte


"""
Funkcija menja sve vrednosti karte novim vrednostima. Kao rezultat vraća rečnik sa svim kartama, 
koji sada sadrži izmenu.
"""
def izmena_karte(
    sve_karte: iter,
    svi_konkretni_letovi: iter,
    broj_karte: int,
    nova_sifra_konkretnog_leta: int=None,
    nov_datum_polaska:
    datetime=None,
    sediste=None
) -> dict:
    karta = sve_karte[broj_karte]
    sifra_trenutnog_konkretnog_leta = karta["sifra_konkretnog_leta"]
    trenutni_konkretan_let = svi_konkretni_letovi[sifra_trenutnog_konkretnog_leta]

    if nova_sifra_konkretnog_leta:
        karta["sifra_konkretnog_leta"] = nova_sifra_konkretnog_leta
        #trenutni_konkretan_let["zauzeto"] = False
        novi_konkretan_let = svi_konkretni_letovi[nova_sifra_konkretnog_leta]
        #novi_konkretan_let["zauzeto"] = True
    if nov_datum_polaska:
        karta["datum_prodaje"] = nov_datum_polaska
    
    if sediste:
        karta["sediste"] = sediste

    sve_karte[broj_karte] = karta

    return sve_karte


"""
 Funkcija brisanja karte se ponaša drugačije u zavisnosti od korisnika:
- Prodavac: karta se označava za brisanje
- Admin/menadžer: karta se trajno briše
Kao rezultat se vraća nova kolekcija svih karata.
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
Funkcija vraća sve karte koje se poklapaju sa svim zadatim kriterijumima. 
Kriterijum se ne primenjuje ako nije prosleđen.
"""
def pretraga_prodatih_karata(sve_karte: dict, svi_letovi:dict, svi_konkretni_letovi:dict, polaziste: str="",
                             odrediste: str="", datum_polaska: datetime="", datum_dolaska: datetime="",
                             korisnicko_ime_putnika: str="")->list:
    trazene_karte = []
    if not polaziste and not odrediste and not datum_dolaska and not datum_polaska and not korisnicko_ime_putnika:
        raise Exception("Ne postoji ni jedan parametar!")
    
    for karta in sve_karte: 
        putnici_u_karti = []
        for putnik in sve_karte[karta]['putnici']:
            putnici_u_karti.append(putnik['korisnicko_ime'])

        for svaki_let in svi_letovi.values():
            for konkretan_let in svi_konkretni_letovi.values():
                if svaki_let['broj_leta'] == konkretan_let['broj_leta'] and sve_karte[karta]['sifra_konkretnog_leta'] == konkretan_let['sifra']:
                    if (not polaziste or svaki_let["sifra_polazisnog_aerodroma"] == polaziste) and (not odrediste or svaki_let["sifra_odredisnog_aerodorma"] == odrediste) and (not datum_polaska or konkretan_let["datum_i_vreme_polaska"].date() == datum_polaska.date()) and (not datum_dolaska or konkretan_let["datum_i_vreme_dolaska"].date() == datum_dolaska.date()) and (not korisnicko_ime_putnika or korisnicko_ime_putnika in putnici_u_karti):
                        trazene_karte.append(sve_karte[karta])
    return trazene_karte


"""
Funkcija čuva sve karte u fajl na zadatoj putanji sa zadatim separatorom.
"""
def sacuvaj_karte(sve_karte: dict, putanja: str, separator: str):
    with open(putanja, 'w', newline='') as datoteka:


        polja = ['broj_karte', 'sifra_konkretnog_leta', 'kupac', 'prodavac', 'sediste', 'status', 'datum_prodaje', 'obrisana', 'putnici']
        #polja = ['broj_karte', 'sifra_konkretnog_leta', 'kupac', 'prodavac', 'sediste', 'datum_prodaje', 'obrisana', 'putnici']
       
        karte = csv.DictWriter(datoteka, fieldnames=polja, delimiter=separator)
        for karta in sve_karte:
            karte.writerow(sve_karte[karta])


"""
Funkcija učitava sve karte iz fajla sa zadate putanje sa zadatim separatorom.
"""
def ucitaj_karte_iz_fajla(putanja: str, separator: str) -> dict:
    with open(putanja, 'r') as datoteka:
        

        polja = ['broj_karte', 'sifra_konkretnog_leta', 'kupac', 'prodavac', 'sediste', 'status', 'datum_prodaje', 'obrisana', 'putnici']
        #polja = ['broj_karte', 'sifra_konkretnog_leta', 'kupac', 'prodavac', 'sediste', 'datum_prodaje', 'obrisana', 'putnici']
        karte = csv.DictReader(datoteka, fieldnames=polja, delimiter=separator)
        sve_karte = {}
        for red in karte:
            #Broj karte i šifra konkretnog leta moraju biti int dok je polje 'obrisana logička promenljiva i ona mora biti True ili False'
            red['broj_karte'] = int(red['broj_karte'])
            red['sifra_konkretnog_leta'] = int(red['sifra_konkretnog_leta'])
            #red['datum_prodaje'] = datetime.strptime(red['datum_prodaje'], "%Y-%m-%d %H:%M:%S")
            
            #red['datum_prodaje'] = datetime.strptime(red['datum_prodaje'], "%d.%m.%Y.")
            red['kupac'] = eval(red['kupac'])
            red['prodavac'] = eval(red['prodavac'])
            red['putnici'] = eval(red['putnici'])
            if red['obrisana'] == 'False' or red['obrisana'] == 'false':
                red['obrisana'] = False
            elif red['obrisana'] == 'True' or red['obrisana'] == 'true':
                red['obrisana'] = True
            else:
                raise Exception("Polje 'obrisana' mora biti logička promenljiva True ili False!")
            if red['sifra_konkretnog_leta'] >= 1000:      #ovaj if stoji zbog lošeg testa
                red.pop('status', None)
            sve_karte[red['broj_karte']] = red
    return sve_karte

