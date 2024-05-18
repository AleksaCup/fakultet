from datetime import datetime, date, timedelta

"""
Funkcija kao rezultat vraća listu karata prodatih na zadati dan.
"""
def izvestaj_prodatih_karata_za_dan_prodaje(sve_karte: dict, dan: date) -> list:
    prodate_karte = []
    for karta in sve_karte:
        if sve_karte[karta]["datum_prodaje"] == dan:
            prodate_karte.append(sve_karte[karta])
    return prodate_karte


"""
Funkcija kao rezultat vraća listu svih karata čiji je dan polaska leta na zadati dan.
"""
def izvestaj_prodatih_karata_za_dan_polaska(sve_karte: dict, svi_konkretni_letovi: dict, dan: date) -> list:
    prodate_karte = []
    for karta in sve_karte.values():
        konkretni_let = svi_konkretni_letovi[karta["sifra_konkretnog_leta"]]
        if konkretni_let and konkretni_let["datum_i_vreme_polaska"].date() == dan:
            prodate_karte.append(karta)
    return prodate_karte

"""
Funkcija kao rezultat vraća listu karata koje je na zadati dan prodao zadati prodavac.
"""
def izvestaj_prodatih_karata_za_dan_prodaje_i_prodavca(sve_karte: dict, dan: date, prodavac: str) -> list:
    prodate_karte = []
    for karta in sve_karte:
        if sve_karte[karta]["datum_prodaje"] == dan and sve_karte[karta]["prodavac"] == prodavac:
            prodate_karte.append(sve_karte[karta])
    return prodate_karte

"""
Funkcija kao rezultat vraća dve vrednosti: broj karata prodatih na zadati dan i njihovu ukupnu cenu.
Rezultat se vraća kao torka. Npr. return broj, suma
"""
def izvestaj_ubc_prodatih_karata_za_dan_prodaje(
    sve_karte: dict,
    svi_konkretni_letovi: dict,
    svi_letovi,
    dan: date
) -> tuple:
    if not dan:
        raise Exception("Greška!")

    broj_karata = 0
    ukupna_cena = 0
    for karta in sve_karte.values():
        if karta["datum_prodaje"] == dan:
            konkretni_let = svi_konkretni_letovi[karta["sifra_konkretnog_leta"]]
            let = svi_letovi[konkretni_let["broj_leta"]]
            broj_karata += 1
            ukupna_cena += let["cena"]
    return broj_karata, ukupna_cena


"""
Funkcija kao rezultat vraća dve vrednosti: broj karata čiji je dan polaska leta na zadati dan i njihovu ukupnu cenu.
Rezultat se vraća kao torka. Npr. return broj, suma
"""
def izvestaj_ubc_prodatih_karata_za_dan_polaska(
    sve_karte: dict,
    svi_konkretni_letovi: dict,
    svi_letovi: dict,
    dan: date
) -> tuple:
    if not dan:
        raise Exception("Greška!")

    

    broj_karata = 0
    ukupna_cena = 0

    if isinstance(dan, datetime):
            dan = dan.date()

    for karta in sve_karte.values():
        konkretni_let = svi_konkretni_letovi[karta["sifra_konkretnog_leta"]]
        datum = konkretni_let["datum_i_vreme_polaska"]
        if isinstance(datum, datetime):
            datum = datum.date()
        
        if konkretni_let and datum == dan:
            konkretni_let = svi_konkretni_letovi[karta["sifra_konkretnog_leta"]]
            let = svi_letovi[konkretni_let["broj_leta"]]
            broj_karata += 1
            ukupna_cena += let["cena"]
    return broj_karata, ukupna_cena


"""
Funkcija kao rezultat vraća dve vrednosti: broj karata koje je zadati prodavac prodao na zadati dan i njihovu 
ukupnu cenu. Rezultat se vraća kao torka. Npr. return broj, suma
"""
def izvestaj_ubc_prodatih_karata_za_dan_prodaje_i_prodavca(
    sve_karte: dict,
    konkretni_letovi: dict,
    svi_letovi: dict,
    dan: date,
    prodavac: str
) -> tuple:
    broj_karata = 0
    ukupna_cena = 0
    for karta in sve_karte.values():
        if karta["datum_prodaje"] == dan and karta["prodavac"] == prodavac:
            konkretni_let = konkretni_letovi.get(karta["sifra_konkretnog_leta"])
            let = svi_letovi.get(konkretni_let["broj_leta"])
            broj_karata += 1
            ukupna_cena += let["cena"]
    return broj_karata, ukupna_cena


"""
Funkcija kao rezultat vraća rečnik koji za ključ ima dan prodaje, a za vrednost broj karata prodatih na taj dan.
Npr: {"2023-01-01": 20}
"""
def izvestaj_ubc_prodatih_karata_30_dana_po_prodavcima(
    sve_karte: dict,
    svi_konkretni_letovi: dict,
    svi_letovi: dict
) -> dict: 
    
    danas = date.today()
    pre_30_dana = danas - timedelta(days=30)
    izvestaj = {}
    for karta in sve_karte.values():
        if karta["obrisana"] == True:
            continue
        konkretan_let = svi_konkretni_letovi[karta["sifra_konkretnog_leta"]]
        let = svi_letovi[konkretan_let["broj_leta"]]
        cena = let["cena"]
        datum_prodaje = datetime.strptime(karta["datum_prodaje"], "%d.%m.%Y.")
        if datum_prodaje.date() >= pre_30_dana and datum_prodaje.date() <= danas:
            if karta["prodavac"] in izvestaj:
                izvestaj[karta["prodavac"]][0] += 1
                izvestaj[karta["prodavac"]][1] += cena
            else:
                izvestaj[karta["prodavac"]] = [1, cena, karta["prodavac"]]
    return izvestaj


