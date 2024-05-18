from datetime import datetime, date, timedelta
import csv
import re
import copy

"""
Funkcija koja omogucuje korisniku da pregleda informacije o letovima
Ova funkcija sluzi samo za prikaz
"""
def pregled_nerealizovanih_letova(svi_letovi: dict):
    letovi=[]

    
    trenutno_vreme = datetime.now()

    for let in svi_letovi:
        if svi_letovi[let]['datum_pocetka_operativnosti'] > trenutno_vreme:
            letovi.append(svi_letovi[let])
    
    return letovi
"""
Funkcija koja omogucava pretragu leta po yadatim kriterijumima. Korisnik moze da zada jedan ili vise kriterijuma.
Povratna vrednost je lista konkretnih letova.
vreme_poletanja i vreme_sletanja su u formatu hh:mm
"""
def pretraga_letova(svi_letovi: dict, konkretni_letovi:dict, polaziste: str = "", odrediste: str = "",
                    datum_polaska: datetime = None, datum_dolaska: datetime = None,
                    vreme_poletanja: str = "", vreme_sletanja: str = "", prevoznik: str = "") -> list:
    
    trazeni_letovi = []
    if not polaziste and not odrediste and not datum_dolaska and not datum_polaska and not vreme_poletanja and not vreme_sletanja and not prevoznik:
        raise Exception("Ne postoji ni jedan parametar!")

    for sifra, svaki_let in svi_letovi.items():
        for konkretan_let in konkretni_letovi.values():
            if svaki_let['broj_leta'] == konkretan_let['broj_leta']:
                if (not polaziste or svaki_let["sifra_polazisnog_aerodroma"] == polaziste) and (not odrediste or svaki_let["sifra_odredisnog_aerodorma"] == odrediste) and (not datum_polaska or konkretan_let["datum_i_vreme_polaska"].date() == datum_polaska.date()) and (not datum_dolaska or konkretan_let["datum_i_vreme_dolaska"].date() == datum_dolaska.date()) and (not vreme_poletanja or svaki_let["vreme_poletanja"] == vreme_poletanja) and (not vreme_sletanja or svaki_let["vreme_sletanja"] == vreme_sletanja) and (not prevoznik or svaki_let["prevoznik"] == prevoznik):
                    trazeni_letovi.append(konkretan_let)
    return trazeni_letovi

"""
Funkcija koja kreira novi rečnik koji predstavlja let sa prosleđenim vrednostima. Kao rezultat vraća kolekciju
svih letova proširenu novim letom. 
Ova funkcija proverava i validnost podataka o letu. Paziti da kada se kreira let, da se kreiraju i njegovi konkretni letovi.
vreme_poletanja i vreme_sletanja su u formatu hh:mm
CHECKPOINT2: Baca grešku sa porukom ako podaci nisu validni.
"""
def kreiranje_letova(svi_letovi : dict, broj_leta: str, sifra_polazisnog_aerodroma: str,
                     sifra_odredisnog_aerodorma: str,
                     vreme_poletanja: str, vreme_sletanja: str, sletanje_sutra: bool, prevoznik: str,
                     dani: list, model: dict, cena: float,  datum_pocetka_operativnosti: datetime = None ,
                    datum_kraja_operativnosti: datetime = None):
    format_broj_leta = r'^[a-zA-Z][a-zA-Z]\d\d$'
    if not re.match(format_broj_leta, broj_leta):
        raise Exception("Nevalidan broj leta")

    if not datetime.strptime(vreme_poletanja, '%H:%M'):
        raise Exception("Nevalidano vreme poletanja!")

    if not datetime.strptime(vreme_sletanja, '%H:%M'):
        raise Exception("Nevalidano vreme sletanja!")

    if cena < 0:
        raise Exception("Nevalidna cena!")

    if not dani:
        raise Exception("Nevalidni dani!")

    if not model:
        raise Exception("Nevalidan model!")

    if broj_leta not in svi_letovi:
        svi_letovi[broj_leta] = {
            "broj_leta": broj_leta,
            "sifra_polazisnog_aerodroma": sifra_polazisnog_aerodroma,
            "sifra_odredisnog_aerodorma": sifra_odredisnog_aerodorma,
            "vreme_poletanja": vreme_poletanja,
            "vreme_sletanja": vreme_sletanja,
            "sletanje_sutra": sletanje_sutra,
            "prevoznik": prevoznik,
            "dani": dani,
            "model": model,
            "cena": cena,
            "datum_pocetka_operativnosti": datum_pocetka_operativnosti,
            "datum_kraja_operativnosti": datum_kraja_operativnosti
        }
        return svi_letovi
    raise Exception("Neuspešno kreiranje leta!")
"""
Funkcija koja menja let sa prosleđenim vrednostima. Kao rezultat vraća kolekciju
svih letova sa promenjenim letom. 
Ova funkcija proverava i validnost podataka o letu.
vreme_poletanja i vreme_sletanja su u formatu hh:mm
CHECKPOINT2: Baca grešku sa porukom ako podaci nisu validni.
"""
def izmena_letova(
    svi_letovi : dict,
    broj_leta: str,
    sifra_polazisnog_aerodroma: str,
    sifra_odredisnog_aerodorma: str,
    vreme_poletanja: str,
    vreme_sletanja: str,
    sletanje_sutra: bool,
    prevoznik: str,
    dani: list,
    model: dict,
    cena: float,
    datum_pocetka_operativnosti: datetime,
    datum_kraja_operativnosti: datetime
) -> dict:
    if broj_leta not in svi_letovi:
        raise Exception("Let sa navedenim brojem ne postoji!")
    if not sifra_polazisnog_aerodroma:
        raise Exception("Greška sa šifrom polazišnog aerodroma!")
    if not sifra_odredisnog_aerodorma:
        raise Exception("Greška sa šifrom odredišnog aerodroma!")
    if not vreme_poletanja:
        raise Exception("Greška sa vremenom poletanja!")
    if not vreme_sletanja:
        raise Exception("Greška sa vremenom sletanja!")
    if not sletanje_sutra:
        raise Exception("Greška sa sletanjem sutra!")
    if not prevoznik:
        raise Exception("Greška sa prevoznikom!")
    if not dani:
        raise Exception("Greška sa danima!")
    if not model:
        raise Exception("Greška sa modelom aviona!")
    if not datum_pocetka_operativnosti:
        raise Exception("Greška sa datumom početka operativnosti!")
    if not datum_kraja_operativnosti:
        raise Exception("Greška sa datumom kraja operativnosti!")
    if datum_kraja_operativnosti < datum_pocetka_operativnosti:
        raise Exception("Greška! Datum početka operativnosti ne sme biti posle datuma početka operativnosti!")

    svi_letovi[broj_leta] = {
        "broj_leta": broj_leta,
        "sifra_polazisnog_aerodroma": sifra_polazisnog_aerodroma,
        "sifra_odredisnog_aerodorma": sifra_odredisnog_aerodorma,
        "vreme_poletanja": vreme_poletanja,
        "vreme_sletanja": vreme_sletanja,
        "sletanje_sutra": sletanje_sutra,
        "prevoznik": prevoznik,
        "dani": dani,
        "model": model,
        "cena": cena,
        "datum_pocetka_operativnosti": datum_pocetka_operativnosti,
        "datum_kraja_operativnosti": datum_kraja_operativnosti
    }
    if svi_letovi[broj_leta]["datum_pocetka_operativnosti"] >= svi_letovi[broj_leta]["datum_kraja_operativnosti"]:
        raise Exception("Greška!")

    return svi_letovi
"""
Funkcija koja cuva sve letove na zadatoj putanji
"""
def sacuvaj_letove(putanja: str, separator: str, svi_letovi: dict):
    with open(putanja, 'w', newline='') as datoteka:
        polja = ['broj_leta', 'sifra_polazisnog_aerodroma', 'sifra_odredisnog_aerodorma', 'vreme_poletanja', 'vreme_sletanja', 'sletanje_sutra', 'prevoznik', 'dani', 'model',
        'cena', 'datum_pocetka_operativnosti', 'datum_kraja_operativnosti']
        let = csv.DictWriter(datoteka, fieldnames=polja, delimiter=separator)
        for i in svi_letovi:
            let.writerow(svi_letovi[i])

"""
Funkcija koja učitava sve letove iz fajla i vraća ih u rečniku.
"""
def ucitaj_letove_iz_fajla(putanja: str, separator: str) -> dict:
    with open(putanja, 'r', newline='') as datoteka:
        svi_letovi = {}

        polja = ['broj_leta', 'sifra_polazisnog_aerodroma', 'sifra_odredisnog_aerodorma', 'vreme_poletanja', 'vreme_sletanja', 'sletanje_sutra', 'prevoznik', 'dani', 'model',
        'cena', 'datum_pocetka_operativnosti', 'datum_kraja_operativnosti']

        let = csv.DictReader(datoteka, fieldnames=polja, delimiter=separator)
        for red in let:
            if red['sletanje_sutra'] == 'False' or red['sletanje_sutra'] == 'false':
                red['sletanje_sutra'] = False
            if red['sletanje_sutra'] == 'True' or red['sletanje_sutra'] == 'true':
                red['sletanje_sutra'] = True
            if isinstance(red['cena'], str):
                red['cena'] = float(red['cena'])
            
            svi_letovi[red['broj_leta']] = red
        return svi_letovi

"""
Pomoćna funkcija koja podešava matricu zauzetosti leta tako da sva mesta budu slobodna.
Prolazi kroz sve redove i sve poziciej sedišta i postavlja ih na "nezauzeto".
"""
def podesi_matricu_zauzetosti(svi_letovi: dict, konkretni_let: dict):
    model = svi_letovi[konkretni_let['broj_leta']]['model']
    if isinstance(model, str):
        model = eval(svi_letovi[konkretni_let['broj_leta']]['model'])

    konkretni_let['zauzetost'] = [[False for polje in model['pozicije_sedista']] for polje in range(model['broj_redova'])]  
    return konkretni_let   

"""
Funkcija koja vraća matricu zauzetosti sedišta. Svaka stavka sadrži oznaku pozicije i oznaku reda.
Primer: [[True, False], [False, True]] -> A1 i B2 su zauzeti, A2 i B1 su slobodni
"""
def matrica_zauzetosti(konkretni_let: dict) -> list:
    return konkretni_let["zauzetost"]   


"""
Funkcija koja zauzima sedište na datoj poziciji u redu, najkasnije 48h pre poletanja. Redovi počinju od 1. 
Vraća grešku ako se sedište ne može zauzeti iz bilo kog razloga.
"""
def checkin(karta, svi_letovi: dict, konkretni_let: dict, red: int, pozicija: str):
    if konkretni_let['datum_i_vreme_polaska'] - timedelta(hours=48) < datetime.now():
        raise Exception("Prošao checkin!")
    if red < 1:
        raise Exception("Nevalidan broj reda!")
    model = eval(svi_letovi[konkretni_let['broj_leta']]['model'])
    if red > model['broj_redova']:
        raise Exception("Nevalidan broj redova!")
    if pozicija not in model['pozicije_sedista']:
        raise Exception("Nevalidna pozicija sedišta")

    pozicija_index = model["pozicije_sedista"].index(pozicija)
    if konkretni_let['zauzetost'][red - 1][pozicija_index]:
        raise Exception("Sedište je već zauzeto!")
    konkretni_let['zauzetost'][red - 1][pozicija_index] = True
    karta['sediste'] = f'{pozicija}{red}'
    return konkretni_let, karta


"""
Funkcija koja vraća listu konkretni letova koji zadovoljavaju sledeće uslove:
1. Polazište im je jednako odredištu prosleđenog konkretnog leta
2. Vreme i mesto poletanja im je najviše 120 minuta nakon sletanja konkretnog leta
"""
def povezani_letovi(svi_letovi: dict, svi_konkretni_letovi: dict, konkretni_let: dict) -> list:
    povezani_letovi = []

    for let in svi_konkretni_letovi:
        svaki_let = svi_letovi[svi_konkretni_letovi[let]["broj_leta"]]
        prosledjen_let = svi_letovi[konkretni_let["broj_leta"]]
        vreme_poletanja = konkretni_let["datum_i_vreme_dolaska"] + timedelta(hours=2)
        if svaki_let["sifra_polazisnog_aerodroma"] == prosledjen_let["sifra_odredisnog_aerodorma"] and svi_konkretni_letovi[let]["datum_i_vreme_polaska"].date() == vreme_poletanja.date() and svi_konkretni_letovi[let]["datum_i_vreme_polaska"].time() <= vreme_poletanja.time():
            povezani_letovi.append(svi_konkretni_letovi[let])
    return povezani_letovi


"""
Funkcija koja vraća sve konkretne letove čije je vreme polaska u zadatom opsegu, +/- zadati broj fleksibilnih dana
"""
def fleksibilni_polasci(svi_letovi: dict, konkretni_letovi: dict, polaziste: str, odrediste: str,
                        datum_polaska: date, broj_fleksibilnih_dana: int, datum_dolaska: date) -> list:
    if not polaziste:
        raise Exception("Greška! Polazište nije navedeno")
    if not odrediste:
        raise Exception("Greška! Odredište nije navedeno")
    if not datum_polaska:
        raise Exception("Greška! Datum polaska nije naveden")
    #if not datum_dolaska:
        #raise Exception("Greška! Datum dolaska nije naveden")
    if not broj_fleksibilnih_dana:
        raise Exception("Greška! Broj fleksibilnih dana nije naveden")

   
    letovi = []
    #OVO JE ZA POLAZAK
    datum_pocetka = datum_polaska - timedelta(days=broj_fleksibilnih_dana)
    datum_kraja = datum_polaska + timedelta(days=broj_fleksibilnih_dana)

    #OVO JE ZA DOLAZAK
    #datum_pocetka_dolaska = datum_dolaska - timedelta(days=broj_fleksibilnih_dana)
    #datum_kraja_dolaska = datum_dolaska + timedelta(days=broj_fleksibilnih_dana)
    
    for let in svi_letovi.values():
        if let["sifra_polazisnog_aerodroma"] == polaziste and let["sifra_odredisnog_aerodorma"] == odrediste:
            for konkretan_let in konkretni_letovi.values():
                if konkretan_let["broj_leta"] == let["broj_leta"] and datum_pocetka <= konkretan_let["datum_i_vreme_polaska"].date() <= datum_kraja: # and datum_pocetka_dolaska <= konkretan_let["datum_i_vreme_dolaska"].date() <= datum_kraja_dolaska: #and konkretan_let["zauzetost"] == False:
                    letovi.append(let)
    
    letovi.sort(key=lambda x: x["cena"], reverse=True)
    return letovi

def trazenje_10_najjeftinijih_letova(svi_letovi: dict, polaziste: str, odrediste: str) -> list:
    if not isinstance(polaziste, str):
        raise Exception("Pogresno uneto polaziste!")
    if not isinstance(odrediste, str):
        raise Exception("Pogresno uneto odrediste!")
    lista = []
    for let in svi_letovi:
        if polaziste != "" and svi_letovi[let]["sifra_polazisnog_aerodroma"] != polaziste:
            continue
        if odrediste != "" and svi_letovi[let]["sifra_odredisnog_aerodorma"] != odrediste:
            continue
        lista.append(svi_letovi[let])
    lista = sorted(lista, key = lambda x: x["cena"])
    top10 = []
    for i in range(len(lista)):
        if i > 9:
            break
        top10.append(lista[i])
    top10 = sorted(top10, key=lambda x: x["cena"], reverse=True)
    return top10
