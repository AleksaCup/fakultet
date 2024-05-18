from datetime import datetime, date
from functools import reduce

def izvestaj_prodatih_karata_za_dan_prodaje(sve_karte: dict, dan: datetime)->list:
    prodate_karte = []
    for karta in sve_karte:
        if sve_karte[karta]["datum_prodaje"] == dan:
            prodate_karte.append(sve_karte[karta])
    return prodate_karte

def izvestaj_prodatih_karata_za_dan_polaska(sve_karte: dict, svi_konkretni_letovi: dict, dan: date):
    prodate_karte = []
    for karta in sve_karte:
        for konkretan_let in svi_konkretni_letovi:
            if konkretan_let[karta]["datum_i_vreme_polaska"] == dan:
                prodate_karte.append(sve_karte[karta])
    return prodate_karte

def izvestaj_prodatih_karata_za_dan_prodaje_i_prodavca(sve_karte: dict, dan: date, prodavac: str):
    prodate_karte = []
    for karta in sve_karte:
        if sve_karte[karta]["datum_prodaje"] == dan and sve_karte[karta]["prodavac"] == prodavac:
            prodate_karte.append(sve_karte[karta])
    return prodate_karte

def izvestaj_ubc_prodatih_karata_za_dan_prodaje(
    sve_karte: dict,
    svi_konkretni_letovi: dict,
    svi_letovi,
    dan: date
) -> tuple:
    if not dan:
        raise Exception("Greška!")
    i = 0
    prodate_karte = []
    for karta in sve_karte:
        if sve_karte[karta]["datum_prodaje"] == dan:
            prodate_karte.append(sve_karte[karta])
            i += 1
    for let in svi_letovi:
        if svi_letovi[let]["cena"] in svi_konkretni_letovi:
            cena = svi_letovi[let]["cena"]
        cena = cena * i
    return (i, cena)
    # vraca broj i cenu brojkarata, cena



def izvestaj_ubc_prodatih_karata_za_dan_polaska(sve_karte: dict, svi_konkretni_letovi: dict, svi_letovi: dict, dan: date): #ubc znaci ukupan broj i cena
    ukupan_broj_karata = 0
    ukupna_cena = 0
    karte_za_dan=[]
    
    for karta in sve_karte:
        if datetime.strptime(karta['datum_prodaje'], "%d") ==dan:
           
            konkretni_let = svi_konkretni_letovi[karta['sifra_konkretnog_leta']]
            let = svi_letovi[konkretni_let['broj_leta']]
            ukupna_cena += let['cena']
            ukupan_broj_karata += 1
    
    return (ukupan_broj_karata, ukupna_cena)

def izvestaj_ubc_prodatih_karata_za_dan_prodaje_i_prodavca(sve_karte: dict, konkretni_letovi: dict, svi_letovi: dict, dan: date, prodavac: str): #ubc znaci ukupan broj i cena
    pass

def izvestaj_ubc_prodatih_karata_30_dana_po_prodavcima(sve_karte: dict, svi_konkretni_letovi: dict, svi_letovi: dict)->dict: #ubc znaci ukupan broj i cena
    return {"Ovi testovi su sjajni majke mi"}


