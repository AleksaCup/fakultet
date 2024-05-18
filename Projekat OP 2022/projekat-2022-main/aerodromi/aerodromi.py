import csv

"""
Funkcija kreira rečnik za novi aerodrom i dodaje ga u rečnik svih aerodroma.
Kao rezultat vraća rečnik svih aerodroma sa novim aerodromom.
"""
def kreiranje_aerodroma(
    svi_aerodromi: dict,
    skracenica: str ="",
    pun_naziv: str ="",
    grad: str ="",
    drzava: str =""
) -> dict:
    if not skracenica:
        raise Exception("Greška!")
    if not pun_naziv:
        raise Exception("Greška!")
    if not grad:
        raise Exception("Greška!")
    if not drzava:
        raise Exception("Greška!")

    svi_aerodromi[skracenica] = {
        "skracenica": skracenica,
        "pun_naziv": pun_naziv,
        "grad": grad,
        "drzava": drzava
    }
    return svi_aerodromi

"""
Funkcija koja čuva aerodrome u fajl.
"""
def sacuvaj_aerodrome(putanja: str, separator: str, svi_aerodromi: dict):
    with open(putanja, 'w') as datoteka:
        polja = ['skracenica', 'pun_naziv', 'grad', 'drzava']
        aerodromi = csv.DictWriter(datoteka, fieldnames=polja, delimiter=separator)
        for aerodrom in svi_aerodromi:
            aerodromi.writerow(svi_aerodromi[aerodrom])

"""
Funkcija koja učitava aerodrome iz fajla.
"""
def ucitaj_aerodrom(putanja: str, separator: str) -> dict:
    with open(putanja, 'r') as datoteka:
        svi_aerodromi = {}

        polja = ['skracenica', 'pun_naziv', 'grad', 'drzava']
        aerodromi = csv.DictReader(datoteka, fieldnames=polja, delimiter=separator)
        for red in aerodromi:


            svi_aerodromi[red['skracenica']] = red
        return svi_aerodromi