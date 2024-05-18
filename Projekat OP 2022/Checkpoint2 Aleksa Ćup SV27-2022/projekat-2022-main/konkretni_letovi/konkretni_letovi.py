from datetime import datetime, timedelta
from datetime import datetime
import csv

sifra_konkretnog_leta = 0

def kreiranje_konkretnog_leta(svi_konkretni_letovi: dict, let: dict):
    global sifra_konkretnog_leta

    
    vreme_poletanja = datetime.strptime(let['vreme_poletanja'], '%H:%M').time()
    vreme_sletanja = datetime.strptime(let['vreme_sletanja'], '%H:%M').time()

    # Ovde se prolazi kroz sve dane u određenom intervalu između početka i kraja operativnosti
    for dan in range((let['datum_kraja_operativnosti'] - let['datum_pocetka_operativnosti']).days + 1):
        # Ovde pravim trenutni datum u koji smeštam svaki datum u zadatom intervalu tako što se na početni datum doda onoliko dana(pomoću funkcije timedelta) za koliko se taj datum razlikuje od početnog 

        trenutni_datum = let['datum_pocetka_operativnosti'] + timedelta(days=dan)

        # Let kreiramo samo ako se dan u nedelji trenutnog datuma poklapa sa danom kojim se taj let realizuje
        if trenutni_datum.weekday() in let['dani']:
            sifra_konkretnog_leta += 1
            konkretan_let = {
                "sifra": sifra_konkretnog_leta,
                "broj_leta": let['broj_leta'],
                "datum_i_vreme_polaska": datetime.combine(trenutni_datum, vreme_poletanja),
                "datum_i_vreme_dolaska": datetime.combine(trenutni_datum, vreme_sletanja),
            }
            svi_konkretni_letovi[sifra_konkretnog_leta] = konkretan_let
            
    return svi_konkretni_letovi
def sacuvaj_kokretan_let(putanja: str, separator: str, svi_konkretni_letovi: dict):
    with open(putanja, 'w') as datoteka:
        polja = ['sifra', 'broj_leta', 'datum_i_vreme_polaska', 'datum_i_vreme_dolaska']
        konkretni_let = csv.DictWriter(datoteka, fieldnames=polja, delimiter=separator)
        for let in svi_konkretni_letovi:
            konkretni_let.writerow(svi_konkretni_letovi[let])

def ucitaj_konkretan_let(putanja: str, separator: str) -> dict:
    with open(putanja, 'r') as datoteka:
        svi_konkretni_letovi = {}

        polja = ['sifra', 'broj_leta', 'datum_i_vreme_polaska', 'datum_i_vreme_dolaska']

        let = csv.DictReader(datoteka, fieldnames=polja, delimiter=separator)
        for red in let:
            if isinstance(red['sifra'], str):
                red['sifra'] = int(red['sifra'])
            red['datum_i_vreme_polaska']= datetime.strptime(red['datum_i_vreme_polaska'], "%Y-%m-%d %H:%M:%S")
            red['datum_i_vreme_dolaska']= datetime.strptime(red['datum_i_vreme_dolaska'], "%Y-%m-%d %H:%M:%S")
            
            svi_konkretni_letovi[red['sifra']] = red
        return svi_konkretni_letovi
