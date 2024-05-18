from datetime import datetime
import csv
import re

"""
Funkcija koja omogucuje korisniku da pregleda informacije o letovima
Ova funkcija sluzi samo za prikaz
"""
def pregled_nerealizovanih_letova(svi_letovi: dict):
    for let in svi_letovi:
        if let['datum_pocetka_operativnosti'] > datetime.now():
            print(let)
            print("\n")
"""
Funkcija koja omogucava pretragu leta po yadatim kriterijumima. Korisnik moze da zada jedan ili vise kriterijuma.
Povratna vrednost je lista konkretnih letova.
vreme_poletanja i vreme_sletanja su u formatu hh:mm
"""
def pretraga_letova(svi_letovi: dict, konkretni_letovi:dict, polaziste: str = "", odrediste: str = "", datum_polaska: datetime = None, datum_dolaska: datetime = None,
                    vreme_poletanja: str = "", vreme_sletanja: str = "", prevoznik: str = "")->list:
    trazeni_letovi = []
    if not polaziste and not odrediste and not datum_dolaska and not datum_polaska and not vreme_poletanja and not vreme_sletanja and not prevoznik:
        raise Exception("Ne postoji ni jedan parametar!")

    for svaki_let in svi_letovi.values():
        for konkretan_let in konkretni_letovi.values():
            if not polaziste or polaziste in svaki_let["sifra_polazisnog_aerodroma"]:
                if not odrediste or odrediste in svaki_let["sifra_odredisnog_aerodorma"]:
                    if not datum_polaska or datum_polaska == konkretan_let["datum_i_vreme_polaska"]:
                        if not datum_dolaska or datum_dolaska == konkretan_let["datum_i_vreme_dolaska"]:
                            if not vreme_poletanja or vreme_poletanja in svaki_let["vreme_poletanja"]:
                                if not vreme_sletanja or vreme_sletanja in svaki_let["vreme_sletanja"]:
                                    if not prevoznik or prevoznik in svaki_let["prevoznik"]:
                                        trazeni_letovi.append(konkretan_let)
                                    else:
                                        raise Exception("Greška sa prevoznikom!")
                                else:
                                    raise Exception("Greška sa vremenom sletanja!")
                            else:
                                raise Exception("Greška sa vremenom poletanja!")
                        else:
                            raise Exception("Greška sa datumom dolaska!")
                    else:
                        raise Exception("Greška sa datumom polaska!")
                else:
                    raise Exception("Greška sa odredištem!")
            else:
                raise Exception("Greška sa polazištem!")
    return trazeni_letovi

"""
Funkcija koja kreira novi rečnik koji predstavlja let sa prosleđenim vrednostima. Kao rezultat vraća kolekciju
svih letova proširenu novim letom. 
Ova funkcija proverava i validnost podataka o letu. Paziti da kada se kreira let, da se kreiraju i njegovi konkretni letovi.
vreme_poletanja i vreme_sletanja su u formatu hh:mm
CHECKPOINT2: Baca grešku sa porukom ako podaci nisu validni.
"""
def kreiranje_letova(svi_letovi : dict, broj_leta: str, sifra_polazisnog_aerodroma: str, sifra_odredisnog_aerodorma: str,
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
"""
Funkcija koja cuva sve letove na zadatoj putanji
"""
def sacuvaj_letove(putanja: str, separator: str, svi_letovi: dict):
    with open(putanja, 'w') as datoteka:
        polja = ['broj_leta', 'sifra_polazisnog_aerodroma', 'sifra_odredisnog_aerodorma', 'vreme_poletanja', 'vreme_sletanja', 'sletanje_sutra', 'prevoznik', 'dani', 'model',
        'cena', 'datum_pocetka_operativnosti', 'datum_kraja_operativnosti']
        let = csv.DictWriter(datoteka, fieldnames=polja, delimiter=separator)
        for i in svi_letovi:
            let.writerow(svi_letovi[i])

"""
Funkcija koja učitava sve letove iz fajla i vraća ih u rečniku.
"""
def ucitaj_letove_iz_fajla(putanja: str, separator: str) -> dict:
    with open(putanja, 'r') as datoteka:
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
    pass


"""
Funkcija koja vraća matricu zauzetosti sedišta. Svaka stavka sadrži oznaku pozicije i oznaku reda.
Primer: [[True, False], [False, True]] -> A1 i B2 su zauzeti, A2 i B1 su slobodni
"""
def matrica_zauzetosti(konkretni_let: dict) -> list:
    pass