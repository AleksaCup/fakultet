import csv
sledeci_id = 0
"""
Funkcija kreira novi rečnik za model aviona i dodaje ga u rečnik svih modela aviona.
Kao rezultat vraća rečnik svih modela aviona sa novim modelom.
"""
def kreiranje_modela_aviona(
    svi_modeli_aviona: dict,
    naziv: str ="",
    broj_redova: str = "",
    pozicije_sedista: list = []
) -> dict:
    global sledeci_id
    max_sifra = max(svi_modeli_aviona.keys(), default=0)
    if not naziv:
        raise Exception("Greška! Niste uneli naziv.")
    if not broj_redova:
        raise Exception("Greška! Niste uneli broj redova.")
    if not pozicije_sedista:
        raise Exception("Greška! Niste uneli pozicije sedišta.")

    svi_modeli_aviona[max_sifra] = {
        'id': max_sifra,
        'naziv': naziv,
        'broj_redova': broj_redova,
        'pozicije_sedista': pozicije_sedista
    }
    max_sifra += 1
    return svi_modeli_aviona


"""
Funkcija čuva sve modele aviona u fajl na zadatoj putanji sa zadatim operatorom.
"""
def sacuvaj_modele_aviona(putanja: str, separator: str, svi_aerodromi: dict):
    with open(putanja, 'w') as datoteka:
        polja = ['id', 'naziv', 'broj_redova', 'pozicije_sedista']
        aerodromi = csv.DictWriter(datoteka, fieldnames=polja, delimiter=separator)
        for aerodrom in svi_aerodromi:
            aerodromi.writerow(svi_aerodromi[aerodrom])


"""
Funkcija učitava sve modele aviona iz fajla na zadatoj putanji sa zadatim operatorom.
"""
def ucitaj_modele_aviona(putanja: str, separator: str) -> dict:
    with open(putanja, 'r') as datoteka:
        svi_aerodromi = {}

        polja = ['id', 'naziv', 'broj_redova', 'pozicije_sedista']
        aerodromi = csv.DictReader(datoteka, fieldnames=polja, delimiter=separator)
        for red in aerodromi:
            red['id'] = int(red['id'])
            red['broj_redova'] = int(red['broj_redova'])
            red['pozicije_sedista'] = eval(red['pozicije_sedista'])


            svi_aerodromi[red['id']] = red
        return svi_aerodromi
