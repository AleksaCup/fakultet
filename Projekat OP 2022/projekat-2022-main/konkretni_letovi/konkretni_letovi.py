from datetime import datetime, timedelta
import csv

"""
Funkcija koja za zadati konkretni let kreira sve konkretne letove u opsegu operativnosti.
Kao rezultat vraća rečnik svih konkretnih letova koji sadrži nove konkretne letove.
"""
def kreiranje_konkretnog_leta(svi_konkretni_letovi: dict, let: dict) -> dict:
    max_sifra = max(svi_konkretni_letovi.keys(), default=0)
    vreme_p = let['vreme_poletanja'].split(":")
    vreme_s = let['vreme_sletanja'].split(":")
    sat_poletanja = int(vreme_p[0])
    minut_poletanja = int(vreme_p[1])
    sat_sletanja = int(vreme_s[0])
    minut_sletanja = int(vreme_s[1])
    trenutni_dan = let["datum_pocetka_operativnosti"]
    kraj = let["datum_kraja_operativnosti"]
    while trenutni_dan < kraj:
        weekday = trenutni_dan.weekday()
        if weekday in let["dani"]:
            konkretan_let = {
                "sifra": max_sifra + 1,
                "broj_leta": let["broj_leta"],
                "datum_i_vreme_polaska": trenutni_dan.replace(hour=sat_poletanja, minute=minut_poletanja),
                "datum_i_vreme_dolaska": trenutni_dan.replace(hour=sat_sletanja, minute=minut_sletanja),
                "zauzetost": []
            }
            max_sifra += 1
            svi_konkretni_letovi[konkretan_let["sifra"]] = konkretan_let
        trenutni_dan += timedelta(days=1)
    return svi_konkretni_letovi


"""
Funkcija čuva konkretne letove u fajl na zadatoj putanji sa zadatim separatorom. 
"""
def sacuvaj_kokretan_let(putanja: str, separator: str, svi_konkretni_letovi: dict):
    with open(putanja, 'w', newline='') as datoteka:
        polja = ['sifra', 'broj_leta', 'datum_i_vreme_polaska', 'datum_i_vreme_dolaska', 'zauzetost']
        konkretni_let = csv.DictWriter(datoteka, fieldnames=polja, delimiter=separator)
        for let in svi_konkretni_letovi:
            konkretni_let.writerow(svi_konkretni_letovi[let])


"""
Funkcija učitava konkretne letove iz fajla na zadatoj putanji sa zadatim separatorom.
"""
def ucitaj_konkretan_let(putanja: str, separator: str) -> dict:
    with open(putanja, 'r', newline='') as datoteka:
        svi_konkretni_letovi = {}

        polja = ['sifra', 'broj_leta', 'datum_i_vreme_polaska', 'datum_i_vreme_dolaska', 'zauzetost']

        let = csv.DictReader(datoteka, fieldnames=polja, delimiter=separator)
        for red in let:
            if isinstance(red['sifra'], str):
                red['sifra'] = int(red['sifra'])
            red['datum_i_vreme_polaska'] = datetime.strptime(red['datum_i_vreme_polaska'], "%Y-%m-%d %H:%M:%S")
            red['datum_i_vreme_dolaska'] = datetime.strptime(red['datum_i_vreme_dolaska'], "%Y-%m-%d %H:%M:%S")
            if red['zauzetost'] == 'False' or red['zauzetost'] == 'false':
                red['zauzetost'] = False
            elif red['zauzetost'] == 'True' or red['zauzetost'] == 'true':
                red['zauzetost'] = True
            else:
                red['zauzetost'] = eval(red['zauzetost'])

            svi_konkretni_letovi[red['sifra']] = red
        return svi_konkretni_letovi
