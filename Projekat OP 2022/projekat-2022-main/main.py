import aerodromi, izvestaji, karte, konkretni_letovi, korisnici, letovi, model_aviona
from aerodromi import aerodromi
from izvestaji import izvestaji
from karte import karte
from konkretni_letovi import konkretni_letovi
from korisnici import korisnici
from letovi import letovi
from model_aviona import model_aviona
from common import konstante
from datetime import datetime, date, timedelta
import time



def checkin():
    try:
        sve_karte = karte.ucitaj_karte_iz_fajla("karte.csv", "|")
        svi_letovi = letovi.ucitaj_letove_iz_fajla("letovi.csv", "|")
        svi_konkretni_letovi = konkretni_letovi.ucitaj_konkretan_let("konkretni_letovi.csv", "|")
        svi_korisnici = korisnici.ucitaj_korisnike_iz_fajla("korisnici.csv", "|")

        broj_karte = int(input("Unesite broj karte: "))
        for karta in sve_karte.values():
            if karta['broj_karte'] == broj_karte:
                trazena_karta = karta
        
        for let in svi_konkretni_letovi.values():
            if let['sifra'] == trazena_karta['sifra_konkretnog_leta']:
                konkretni_let = let

        if not logovanje['pasos']:
            pasos = input(f"Unesite pasoš putnika {logovanje['ime']} {logovanje['prezime']} :")
            svi_korisnici[logovanje['korisnicko_ime']]['pasos'] = pasos
            sve_karte[broj_karte]['kupac']['pasos'] = pasos
        for putnik in sve_karte[broj_karte]['putnici']:
            if not putnik['pasos']:
                if putnik['korisnicko_ime'] == logovanje['korisnicko_ime']:
                    putnik['pasos'] = pasos
                else:
                    pasos1 = input(f"Unesite državljanstvo putnika {putnik['ime']} {putnik['prezime']}: ")
                    putnik['pasos'] = pasos1



        if not logovanje['drzavljanstvo']:
            drzavljanstvo = input(f"Unesite državljanstvo putnika {logovanje['ime']} {logovanje['prezime']}: ")
            svi_korisnici[logovanje['korisnicko_ime']]['drzavljanstvo'] = drzavljanstvo
            sve_karte[broj_karte]['kupac']['drzavljanstvo'] = drzavljanstvo
        for putnik in sve_karte[broj_karte]['putnici']:
            if not putnik['drzavljanstvo']:
                if putnik['korisnicko_ime'] == logovanje['korisnicko_ime']:
                    putnik['drzavljanstvo'] = drzavljanstvo
                else:
                    drzavljanstvo1 = input(f"Unesite državljanstvo putnika {putnik['ime']} {putnik['prezime']}: ")
                    putnik['drzavljanstvo'] = drzavljanstvo1


        if not logovanje['pol']:
            pol = input(f"Unesite pol putnika {logovanje['ime']} {logovanje['prezime']}: ")
            svi_korisnici[logovanje['korisnicko_ime']]['pol'] = pol
            sve_karte[broj_karte]['kupac']['pol'] = pol
        for putnik in sve_karte[broj_karte]['putnici']:
            if not putnik['pol']:
                if putnik['korisnicko_ime'] == logovanje['korisnicko_ime']:
                    putnik['pol'] = pol
                else:
                    pol1 = input(f"Unesite državljanstvo putnika {putnik['ime']} {putnik['prezime']}: ")
                    putnik['pol'] = pol1



        korisnici.sacuvaj_korisnike("korisnici.csv", "|", svi_korisnici)
        karte.sacuvaj_karte(sve_karte, "karte.csv", "|")
    

        
        konkretni_let = svi_konkretni_letovi[sve_karte[broj_karte]['sifra_konkretnog_leta']]

        matrica = letovi.matrica_zauzetosti(konkretni_let)
        

        n = 1
        pozicije = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z']
        for red in matrica:
            print(f"Red {n}: ", end=' ')
            for p, sediste in enumerate(red):
                if sediste == True:
                    print("X", end=' ')
                else:
                    print(pozicije[p], end=' ')
            print()
            n += 1

        red_aviona = int(input("\nUnesite broj reda: "))
        pozicija = input("Unesite poziciju: ")

        konkretan_let, karta = letovi.checkin(trazena_karta, svi_letovi, konkretni_let, red_aviona, pozicija)


        sve_karte[karta['broj_karte']] = karta
        svi_konkretni_letovi[konkretan_let['sifra']] = konkretan_let

        karte.sacuvaj_karte(sve_karte, "karte.csv", "|")
        konkretni_letovi.sacuvaj_kokretan_let("konkretni_letovi.csv", "|", svi_konkretni_letovi)

        print(f"\nUspešan CHECK-IN!")

        checkin_povezani = input("\nDa li želite da izvršite CHECK-IN za povezani let? (DA/NE): ")
        if checkin_povezani == 'DA':
            pov_letovi = letovi.povezani_letovi(svi_letovi, svi_konkretni_letovi, konkretni_let)
            

            for let in pov_letovi:
                for karta in sve_karte:
                    if sve_karte[karta]['sifra_konkretnog_leta'] == let['sifra']:
                        karta_povezanog = sve_karte[karta]
                        povezani_kon_let = let 

            matrica_pov = letovi.matrica_zauzetosti(povezani_kon_let)
            
            n = 1
            pozicije = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z']
            for red in matrica_pov:
                print(f"Red {n}: ", end=' ')
                for p, sediste in enumerate(red):
                    if sediste == True:
                        print("X", end=' ')
                    else:
                        print(pozicije[p], end=' ')
                print()
                n += 1

            red_aviona = int(input("\nUnesite broj reda: "))
            pozicija = input("Unesite poziciju: ")

            konkretan_let, karta = letovi.checkin(karta_povezanog, svi_letovi, povezani_kon_let, red_aviona, pozicija)

            print(f"\nUspešan CHECK-IN za povezani let!")

            sve_karte[karta['broj_karte']] = karta
            svi_konkretni_letovi[konkretan_let['sifra']] = konkretan_let

            karte.sacuvaj_karte(sve_karte, "karte.csv", "|")
            konkretni_letovi.sacuvaj_kokretan_let("konkretni_letovi.csv", "|", svi_konkretni_letovi)

        
    except Exception as e:
        print("-->:", e)


def pretraga():
    kriterijumi()
    option = input("\nVaš izbor: ")

    if option == "1":
        polaziste = input("Unesite polazište: ")
        trazeni_letovi = letovi.pretraga_letova(svi_letovi, svi_konkretni_letovi, polaziste, "", None, None, "", "", "")
        print("\n\nTRAŽENI LETOVI SA ZADATIM KRITERIJUMOM PRETRAGE SU:\n")
        print("Šifra".ljust(10),"Broj leta".ljust(20), "Polazak".ljust(30), "Dolazak".ljust(30))
        for let1 in trazeni_letovi:
            print(str(let1['sifra']).ljust(10), str(let1['broj_leta']).ljust(20), str(let1['datum_i_vreme_polaska']).ljust(30), str(let1['datum_i_vreme_dolaska']).ljust(30))
        print("\n")
    elif option == "2":
        odrediste = input("Unesite odredište: ")
        trazeni_letovi = letovi.pretraga_letova(svi_letovi, svi_konkretni_letovi, "", odrediste, None, None, "", "", "")
        print("\n\nTRAŽENI LETOVI SA ZADATIM KRITERIJUMOM PRETRAGE SU:\n")
        print("Šifra".ljust(10),"Broj leta".ljust(20), "Polazak".ljust(30), "Dolazak".ljust(30))
        for let1 in trazeni_letovi:
            print(str(let1['sifra']).ljust(10), str(let1['broj_leta']).ljust(20), str(let1['datum_i_vreme_polaska']).ljust(30), str(let1['datum_i_vreme_dolaska']).ljust(30))
        print("\n")
    elif option == "3":
        datum_polaska = datetime.strptime(input("Unesite datum polaska: "), "%Y-%m-%d")   #RADI
        trazeni_letovi = letovi.pretraga_letova(svi_letovi, svi_konkretni_letovi, "", "", datum_polaska, None, "", "", "")
        print("\n\nTRAŽENI LETOVI SA ZADATIM KRITERIJUMOM PRETRAGE SU:\n")
        print("Šifra".ljust(10),"Broj leta".ljust(20), "Polazak".ljust(30), "Dolazak".ljust(30))
        for let1 in trazeni_letovi:
            print(str(let1['sifra']).ljust(10), str(let1['broj_leta']).ljust(20), str(let1['datum_i_vreme_polaska']).ljust(30), str(let1['datum_i_vreme_dolaska']).ljust(30))
        print("\n")
    elif option == "4":
        datum_dolaska = datetime.strptime(input("Unesite datum dolaska: "), "%Y-%m-%d")
        trazeni_letovi = letovi.pretraga_letova(svi_letovi, svi_konkretni_letovi, "", "", None, datum_dolaska, "", "", "")
        print("\n\nTRAŽENI LETOVI SA ZADATIM KRITERIJUMOM PRETRAGE SU:\n")
        print("Šifra".ljust(10),"Broj leta".ljust(20), "Polazak".ljust(30), "Dolazak".ljust(30))
        for let1 in trazeni_letovi:
            print(str(let1['sifra']).ljust(10), str(let1['broj_leta']).ljust(20), str(let1['datum_i_vreme_polaska']).ljust(30), str(let1['datum_i_vreme_dolaska']).ljust(30))
        print("\n")
    elif option == "5":
        vreme_poletanja = input("Unesite vreme poletanja: ")
        trazeni_letovi = letovi.pretraga_letova(svi_letovi, svi_konkretni_letovi, "", "", None, None, vreme_poletanja, "", "")
        print("\n\nTRAŽENI LETOVI SA ZADATIM KRITERIJUMOM PRETRAGE SU:\n")
        print("Šifra".ljust(10),"Broj leta".ljust(20), "Polazak".ljust(30), "Dolazak".ljust(30))
        for let1 in trazeni_letovi:
            print(str(let1['sifra']).ljust(10), str(let1['broj_leta']).ljust(20), str(let1['datum_i_vreme_polaska']).ljust(30), str(let1['datum_i_vreme_dolaska']).ljust(30))
        print("\n")
    elif option == "6":
        vreme_sletanja = input("Unesite vreme sletanja: ")
        trazeni_letovi = letovi.pretraga_letova(svi_letovi, svi_konkretni_letovi, "", "", None, None, "", vreme_sletanja, "")
        print("\n\nTRAŽENI LETOVI SA ZADATIM KRITERIJUMOM PRETRAGE SU:\n")
        print("Šifra".ljust(10),"Broj leta".ljust(20), "Polazak".ljust(30), "Dolazak".ljust(30))
        for let1 in trazeni_letovi:
            print(str(let1['sifra']).ljust(10), str(let1['broj_leta']).ljust(20), str(let1['datum_i_vreme_polaska']).ljust(30), str(let1['datum_i_vreme_dolaska']).ljust(30))
        print("\n")
    elif option == "7":
        prevoznik = input("Unesite prevoznika: ")
        trazeni_letovi = letovi.pretraga_letova(svi_letovi, svi_konkretni_letovi, "", "", None, None, "", "", prevoznik)
        print("\n\nTRAŽENI LETOVI SA ZADATIM KRITERIJUMOM PRETRAGE SU:\n")
        print("Šifra".ljust(10),"Broj leta".ljust(20), "Polazak".ljust(30), "Dolazak".ljust(30))
        for let1 in trazeni_letovi:
            print(str(let1['sifra']).ljust(10), str(let1['broj_leta']).ljust(20), str(let1['datum_i_vreme_polaska']).ljust(30), str(let1['datum_i_vreme_dolaska']).ljust(30))
        print("\n")
    else:
        print("Loš unos!")

def visekriterijumska_pretraga():
    try:
        print("\nVIŠEKRITERIJUMSKA PRETRAGA\n")
        print("\nNAPOMENA: Ukoliko ne želite da unesete određeni kriterijum pritisnite ENTER da preskočite!\n")

        polaziste_v = input("Unesite polazište: ")
        odrediste_v = input("Unesite odredište: ")
        datum_polaska_v = input("Unesite datum polaska: ")
        datum_dolaska_v = input("Unesite datum dolaska: ")
        vreme_poletanja_v = input("Unesite vreme poletanja: ")
        vreme_sletanja_v = input("Unesite vreme sletanja: ")
        prevoznik_v = input("Unesite prevoznika: ")

        if datum_polaska_v:
            datum_polaska_v = datetime.strptime(datum_polaska_v, "%Y-%m-%d")
        else:
            datum_polaska_v = None
        if datum_dolaska_v:
            datum_dolaska_v = datetime.strptime(datum_dolaska_v, "%Y-%m-%d")
        else:
            datum_dolaska_v = None

        trazeni_letovi_v = letovi.pretraga_letova(svi_letovi, svi_konkretni_letovi, polaziste_v, odrediste_v, datum_polaska_v, datum_dolaska_v, vreme_poletanja_v, vreme_sletanja_v, prevoznik_v)
        print("Traženi letovi sa zadatim kriterijumima pretrage su:\n")
        print("Šifra".ljust(7),"Broj leta".ljust(10), "Polazak".ljust(20), "Dolazak".ljust(20))
        for let1 in trazeni_letovi_v:
            print(str(let1['sifra']).ljust(7), str(let1['broj_leta']).ljust(10), datetime.strftime(let1['datum_i_vreme_polaska'], "%d.%m.%Y. %H:%M").ljust(20), datetime.strftime(let1['datum_i_vreme_dolaska'], "%d.%m.%Y. %H:%M").ljust(20))
    except Exception as e:
        print("-->:", e)

def top_10():
    try:
        polaziste_t10 = input("Unesite polazište: ")
        odrediste_t10 = input("Unesite odredište: ")
        najjeftiniji = letovi.trazenje_10_najjeftinijih_letova(svi_letovi, polaziste_t10, odrediste_t10)
        n=len(najjeftiniji)
        print("\n****************************************************************************************\n")
        print("Redni br.".ljust(15),"Broj leta".ljust(15), "Polazište".ljust(15), "Odredište".ljust(15), "Polazak".ljust(15), "Dolazak".ljust(15), "Cena".ljust(15))
        
        for let in najjeftiniji:
            
            
            print(str(n).ljust(15), let['broj_leta'].ljust(15), let['sifra_polazisnog_aerodroma'].ljust(15), let['sifra_odredisnog_aerodorma'].ljust(15), let['vreme_poletanja'].ljust(15), let['vreme_sletanja'].ljust(15), str(let['cena']).ljust(15))

            n-=1
        print("\n****************************************************************************************\n")
    except Exception as e:
        print("-->:", e)

def fleksibilni_polasci():
    try:
        polaziste_f = input("Unesite polazište: ")
        odrediste_f = input("Unesite odredište: ")
        datum_polaska_f = datetime.strptime(input("Unesite datum polaska: "), "%Y-%m-%d").date()
        #datum_dolaska_f = datetime.strptime(input("Unesite datum dolaska: "), "%Y-%m-%d").date()
        broj_fleksibilnih_dana = int(input("Unesite broj fleksibilnih dana: "))
        fleksibilni_polasci = letovi.fleksibilni_polasci(svi_letovi, svi_konkretni_letovi, polaziste_f, odrediste_f, datum_polaska_f, broj_fleksibilnih_dana, None)
        print("Fleksibilni polasci za unete kriterijume su:\n")

        print("Broj leta".ljust(15), "Polazište".ljust(15), "Odredište".ljust(15), "Polazak".ljust(15), "Dolazak".ljust(15), "Cena".ljust(15))
        for let in fleksibilni_polasci:     
            print(let['broj_leta'].ljust(15), let['sifra_polazisnog_aerodroma'].ljust(15), let['sifra_odredisnog_aerodorma'].ljust(15), let['vreme_poletanja'].ljust(15), let['vreme_sletanja'].ljust(15), str(let['cena']).ljust(15))

    except Exception as e:
        print("-->:", e)

def izvestaji_30_dana(
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
            if karta["prodavac"]["korisnicko_ime"] in izvestaj:
                izvestaj[karta["prodavac"]["korisnicko_ime"]][0] += 1
                izvestaj[karta["prodavac"]["korisnicko_ime"]][1] += cena
            else:
                izvestaj[karta["prodavac"]["korisnicko_ime"]] = [1, cena, karta["prodavac"]["korisnicko_ime"]]
    return izvestaj

def izvestaji_opcije():
    print("\n\nIzaberite vrstu izveštaja:\n")
    print("a. Lista prodatih karata za izabrani dan prodaje")
    print("b. Lista prodatih karata za izabrani dan polaska")
    print("c. Lista prodatih karata za izabrani dan prodaje i izabranog prodavca")
    print("d. Ukupan broj i cena prodatih karata za izabrani dan prodaje")
    print("e. Ukupan broj i cena prodatih karata za izabrani dan polaska")
    print("f. Ukupan broj i cena prodatih karata za izabrani dan prodaje i izabranog prodavca")
    print("g. Ukupan broj i cena prodatih karata u poslednjih 30 dana, po prodavcima")


def kriterijumi():
    print("\nIzaberite kriterijum pretrage:")
    print("\n1. Polazište")
    print("2. Odredište")
    print("3. Datum polaska")
    print("4. Datum dolaska")
    print("5. Vreme poletanja")
    print("6. Vreme sletanja")
    print("7. Prevoznik\n")


def meni_registrovani_korisnik():
    print("\n1. Odjava sa sistema")
    print("2. Izlazak iz aplikacije")
    print("3. Pregled nerealizovanih letova")
    print("4. Pretraga letova")
    print("5. Višekriterijumska pretraga letova")
    print("6. Prikaz 10 najjeftinijih letova")
    print("7. Fleksibilni polasci")
    print("8. Kupovina karata")
    print("9. Pregled nerealizovanih karata")
    print("10. Prijava na let (CHECK-IN)\n")


def meni_registrovani_prodavac():
    print("\n1. Odjava sa sistema")
    print("2. Izlazak iz aplikacije")
    print("3. Pregled nerealizovanih letova")
    print("4. Pretraga letova")
    print("5. Višekriterijumska pretraga letova")
    print("6. Prikaz 10 najjeftinijih letova")
    print("7. Fleksibilni polasci")
    print("8. Prodaja karata")
    print("9. Prijava na let (CHECK-IN)")
    print("10. Izmena karte")
    print("11. Brisanje karte")
    print("12. Pretraga prodatih karata\n")


def meni_registrovani_admin():
    print("\n1. Odjava sa sistema")
    print("2. Izlazak iz aplikacije")
    print("3. Pregled nerealizovanih letova")
    print("4. Pretraga letova")
    print("5. Višekriterijumska pretraga letova")
    print("6. Prikaz 10 najjeftinijih letova")
    print("7. Fleksibilni polasci")
    print("8. Pretraga prodatih karata")
    print("9. Registracija novih prodavaca")
    print("10. Kreiranje letova")
    print("11. Izmena letova")
    print("12. Brisanje karata")
    print("13. Izveštavanje\n")


def meni_neregistrovani():
    print("\n1. Prijava na sistem")
    print("2. Izlazak iz aplikacije")
    print("3. Pregled nerealizovanih letova")
    print("4. Pretraga letova")
    print("5. Višekriterijumska pretraga letova")
    print("6. Prikaz 10 najjeftinijih letova")
    print("7. Fleksibilni polasci")
    print("8. Registracija\n")

def o_autoru():
    danasnji_datum = datetime.strftime(datetime.now(), "%d. %m. %Y.")
    print(f"***********************************************************************")
    print(f"*                                                                     *")
    print(f"*   Projekat razvio: Aleksa Ćup                                       *")
    print(f"*   Broj indeksa: SV 27/2022                                          *")
    print(f"*   Verzija: 1.0                                                      *")
    print(f"*   Kontakt: aleksa@markephing.com, cup.sv27.2022@uns.ac.rs           *")
    print(f"*                                                                     *")
    print(f"*   Današnji datum: {danasnji_datum}                                     *")
    print(f"*                                                                     *")
    print(f"***********************************************************************")


if __name__ == '__main__':
    print("\n")
    print("***********************************************************************")
    print("*         DOBRODOŠLI U APLIKACIJU ZA PRODAJU AVIONSKIH KARATA         *")
    print("***********************************************************************")
    print("\n")
    time.sleep(1)
    o_autoru()
    svi_letovi = letovi.ucitaj_letove_iz_fajla("letovi.csv", "|")
    for let in svi_letovi:
        svi_letovi[let]["dani"] = eval(svi_letovi[let]["dani"])
        svi_letovi[let]["datum_pocetka_operativnosti"] = datetime.strptime(svi_letovi[let]["datum_pocetka_operativnosti"], "%Y-%m-%d")
        svi_letovi[let]["datum_kraja_operativnosti"] = datetime.strptime(svi_letovi[let]["datum_kraja_operativnosti"], "%Y-%m-%d")

    svi_konkretni_letovi = {}
    for let in svi_letovi:
        svi_konkretni_letovi = konkretni_letovi.kreiranje_konkretnog_leta(svi_konkretni_letovi, svi_letovi[let])
    
    for sifra, konkretni_let in svi_konkretni_letovi.items():
        konkretni_let = letovi.podesi_matricu_zauzetosti(svi_letovi, konkretni_let)
    podesi_petlja = True
    while podesi_petlja:
        podesi = input("\nDa li želite da resetujete matricu zauzetosti za sve letove? Sve karte u datoteci biće trajno obrisane. (DA/NE): ")
        #ZAUZETI ODMA MESTA OD PPOSTOJEĆIH KARATA IZ FAJLA
        if podesi == 'NE' or podesi == 'ne' or podesi == 'Ne':
            karte_za_matricu = karte.ucitaj_karte_iz_fajla("karte.csv", "|")
            for karta in karte_za_matricu.values():
                if karta['sediste']:
                    pozicija_s = ord(karta['sediste'][0]) - 65
                    red_s = int(karta['sediste'][1]) - 1
                    svi_konkretni_letovi[karta['sifra_konkretnog_leta']]['zauzetost'][red_s][pozicija_s] = True
        
            konkretni_letovi.sacuvaj_kokretan_let("konkretni_letovi.csv", "|", svi_konkretni_letovi)
            podesi_petlja = False
        elif podesi == 'DA' or podesi == 'da' or podesi == 'Da':
            konkretni_letovi.sacuvaj_kokretan_let("konkretni_letovi.csv", "|", svi_konkretni_letovi)
            karte.sacuvaj_karte({}, "karte.csv", "|")
            podesi_petlja = False
        else:
            print("\nNepostojeća opcija. Pokušajte ponovo!")

    #konkretni_letovi.sacuvaj_kokretan_let("konkretni_letovi.csv", "|", svi_konkretni_letovi)
    

    print("\nProgram se pokreće za: ", end="")
    for i in range(3, 0, -1):
        time.sleep(1)
        print(i, end=" ", flush = True)
        
        #print("\r", end="", flush=True)
    time.sleep(1)
    print("\n")

    program = True
    uloge = True

    while program:
        meni_neregistrovani()
        
        option = input("\nVaš izbor: ")

        if option == "1":
            uloge = True
            while uloge:
                try:
                    korisnci_fajl = korisnici.ucitaj_korisnike_iz_fajla("korisnici.csv", "|")
                    korisnicko_ime = input("Unesite korisničko ime: ")
                    lozinka = input("Unesite lozinku: ")
                    logovanje = korisnici.login(korisnci_fajl, korisnicko_ime, lozinka)
                    if logovanje:

                        print(f"\nUloga: {logovanje['uloga']}\nKorisničko ime: {logovanje['korisnicko_ime']}\n\n\n")

                        if logovanje["uloga"] == konstante.ULOGA_KORISNIK:
                            korisnik_program = True
                            while korisnik_program:
                                meni_registrovani_korisnik()
                                option1 = input("Vaš izbor: ")

                                if option1 == "1":
                                    korisnici.logout(logovanje["korisnicko_ime"])
                                    korisnik_program = False
                                    uloge = False
                                elif option1 == "2":
                                    print("Izašli ste iz aplikacije. Prijatan dan.")
                                    program = False
                                    quit()
                                elif option1 == "3":
                                    print("\nPREGLED NEREALIZOVANIH LETOVA:\n")
                                    print("****************************************************************************************\n")
                                    ner_letovi = letovi.pregled_nerealizovanih_letova(svi_letovi)
                                    print("Broj leta".ljust(15), "Polazište".ljust(15), "Odredište".ljust(15), "Polazak".ljust(15), "Dolazak".ljust(15), "Cena".ljust(15))
                                    for let in ner_letovi:
                                        
                                        print(let['broj_leta'].ljust(15), let['sifra_polazisnog_aerodroma'].ljust(15), let['sifra_odredisnog_aerodorma'].ljust(15), let['vreme_poletanja'].ljust(15), let['vreme_sletanja'].ljust(15), str(let['cena']).ljust(15))
                                    print("\n****************************************************************************************\n")
                                                        
                                elif option1 == "4":
                                    pretraga()
                                elif option1 == "5":
                                    visekriterijumska_pretraga()
                                elif option1 == "6":
                                    top_10()
                                elif option1 == "7":
                                    fleksibilni_polasci()
                                elif option1 == "8":   #KUPOVINA KARTE   KORISNIK
                                    try:
                                        svi_letovi = letovi.ucitaj_letove_iz_fajla("letovi.csv", "|")
                                        sve_karte = karte.ucitaj_karte_iz_fajla("karte.csv", "|")
                                        svi_konkretni_letovi = konkretni_letovi.ucitaj_konkretan_let("konkretni_letovi.csv", "|")
                                        svi_korisnici = korisnici.ucitaj_korisnike_iz_fajla("korisnici.csv", "|")
                                        sifra_konkretnog_leta = int(input("Unesite šifru konkretnog leta: "))
                                        putnici=[]
                                        logovanje['pasos'] = int(logovanje['pasos'])
                                        logovanje['telefon'] = int(logovanje['telefon'])
                                        
                                        odabir = True
                                        while odabir:
                                            za_drugog = input("Da li kartu uzimate za sebe ili za drugu osobu? (1 ZA SEBE - 2 ZA DRUGOG): ")
                                            if za_drugog == '1':
                                                putnici.append(logovanje)
                                                odabir = False
                                            elif za_drugog == '2':
                                                korisnicko_ime = input("Unesite korisničko ime: ")
                                                lozinka = input("Unesite lozinku: ")
                                                telefon = input("Unesite telefon: ")
                                                email = input("Unesite email: ")
                                                ime = input("Unesite ime: ")
                                                prezime = input("Unesite prezime: ")
                                                pasos = input("Unesite pasoš: ")
                                                drzavljanstvo = input("Unesite državljanstvo: ")
                                                pol = input("Unesite pol: ")
                                                sledeci_putnik1 = korisnici.kreiraj_korisnika({}, False, konstante.ULOGA_KORISNIK, "", korisnicko_ime, lozinka, ime, prezime, email, pasos, drzavljanstvo, telefon, pol)
                                                sledeci_putnik1[korisnicko_ime]['pasos'] = int(sledeci_putnik1[korisnicko_ime]['pasos'])
                                                sledeci_putnik1[korisnicko_ime]['telefon'] = int(sledeci_putnik1[korisnicko_ime]['telefon'])
                                                putnici.append(sledeci_putnik1[korisnicko_ime])
                                                odabir = False
                                            else:
                                                print("Nepostojeća opcija")
                                        sledeci_putnik = True
                                        while sledeci_putnik:
                                            sledeci = input("Da li želite da unesete saputnika? (DA/NE): ")
                                            if sledeci == "NE" or sledeci == "ne" or sledeci == "Ne":
                                                sledeci_putnik = False
                                            else: 
                                                print("\nKreiranje saputnika\n")
                                                korisnicko_ime = input("Unesite korisničko ime: ")
                                                lozinka = input("Unesite lozinku: ")
                                                telefon = input("Unesite telefon: ")
                                                email = input("Unesite email: ")
                                                ime = input("Unesite ime: ")
                                                prezime = input("Unesite prezime: ")
                                                pasos = input("Unesite pasoš: ")
                                                drzavljanstvo = input("Unesite državljanstvo: ")
                                                pol = input("Unesite pol: ")
                                                sledeci_putnik1 = korisnici.kreiraj_korisnika({}, False, konstante.ULOGA_KORISNIK, "", korisnicko_ime, lozinka, ime, prezime, email, pasos, drzavljanstvo, telefon, pol)
                                                sledeci_putnik1[korisnicko_ime]['pasos'] = int(sledeci_putnik1[korisnicko_ime]['pasos'])
                                                sledeci_putnik1[korisnicko_ime]['telefon'] = int(sledeci_putnik1[korisnicko_ime]['telefon'])
                                                putnici.append(sledeci_putnik1[korisnicko_ime])
                                        

                                        slobodna_mesta = svi_konkretni_letovi[sifra_konkretnog_leta]['zauzetost']

                                        #DOSTUPNI PRODAVCI
                                        print("\nDostupni prodavci:")
                                        p = 1
                                        for korisnik in svi_korisnici.values():
                                            if korisnik['uloga'] == konstante.ULOGA_PRODAVAC:
                                                print(f"{p}. {korisnik['korisnicko_ime']}")
                                                p += 1

                                        kor_prodavac = input("\nUnesite korisničko ime prodavca: ")

                                        if kor_prodavac not in svi_korisnici:
                                            izabrani_prodavac = {}
                                        else:
                                            izabrani_prodavac = svi_korisnici[kor_prodavac]
                                            izabrani_prodavac['pasos'] = int(izabrani_prodavac['pasos'])
                                            izabrani_prodavac['telefon'] = int(izabrani_prodavac['telefon'])

                                        karta, sve_karte = karte.kupovina_karte(sve_karte, svi_konkretni_letovi, sifra_konkretnog_leta, putnici, slobodna_mesta, logovanje, prodavac = izabrani_prodavac, datum_prodaje = datetime.strftime(datetime.now(),"%d.%m.%Y."))
                                        karte.sacuvaj_karte(sve_karte ,"karte.csv", "|")
                                        print(f"\nUspešno ste kupili kartu! Broj Vaše karte je {karta['broj_karte']}\n")

                                        povezani_da = input("Da li želite da kupite povezani let (DA/NE): ")
                                        if povezani_da == "DA" or povezani_da == "Da" or povezani_da == "da":
                                            konkretan = svi_konkretni_letovi[sifra_konkretnog_leta]
                                            povezani = letovi.povezani_letovi(svi_letovi, svi_konkretni_letovi, konkretan)
                                            if not povezani:
                                                print("Za uneti let ne postoji povezani let!")
                                            else:
                                                print("Šifra".ljust(7),"Broj leta".ljust(10), "Polazak".ljust(20), "Dolazak".ljust(20))
                                                for let1 in povezani:
                                                    print(str(let1['sifra']).ljust(7), str(let1['broj_leta']).ljust(10), datetime.strftime(let1['datum_i_vreme_polaska'], "%d.%m.%Y. %H:%M").ljust(20), datetime.strftime(let1['datum_i_vreme_dolaska'], "%d.%m.%Y. %H:%M").ljust(20))


                                                sifra_konkretnog_leta1 = int(input("\nUnesite šifru konkretnog leta: "))

                                                if svi_konkretni_letovi[sifra_konkretnog_leta1] in povezani:

                                                    # putnici=[]
                                                    # putnici.append(logovanje)
                                                    # sledeci_putnik = True
                                                    # while sledeci_putnik:
                                                    #     sledeci = input("Da li želite da unesete saputnika? (DA/NE): ")
                                                    #     if sledeci == "NE":
                                                    #         sledeci_putnik = False
                                                    #     else: 
                                                    #         print("\nKreiranje saputnika\n")
                                                    #         korisnicko_ime = input("Unesite korisničko ime: ")
                                                    #         lozinka = input("Unesite lozinku: ")
                                                    #         telefon = input("Unesite telefon: ")
                                                    #         email = input("Unesite email: ")
                                                    #         ime = input("Unesite ime: ")
                                                    #         prezime = input("Unesite prezime: ")
                                                    #         pasos = input("Unesite pasoš: ")
                                                    #         drzavljanstvo = input("Unesite državljanstvo: ")
                                                    #         pol = input("Unesite pol: ")
                                                    #         sledeci_putnik1 = korisnici.kreiraj_korisnika({}, False, konstante.ULOGA_KORISNIK, "", korisnicko_ime, lozinka, ime, prezime, email, pasos, drzavljanstvo, telefon, pol)
                                                    #         sledeci_putnik1[korisnicko_ime]['pasos'] = int(sledeci_putnik1[korisnicko_ime]['pasos'])
                                                    #         sledeci_putnik1[korisnicko_ime]['telefon'] = int(sledeci_putnik1[korisnicko_ime]['telefon'])
                                                    #         putnici.append(sledeci_putnik1[korisnicko_ime])


                                                    slobodna_mesta = svi_konkretni_letovi[sifra_konkretnog_leta1]['zauzetost']
 
                                                    

                                                    
                                                    karta, sve_karte = karte.kupovina_karte(sve_karte, svi_konkretni_letovi, sifra_konkretnog_leta1, putnici, slobodna_mesta, logovanje, prodavac = izabrani_prodavac, datum_prodaje = datetime.strftime(datetime.now(),"%d.%m.%Y."))
                                                    karte.sacuvaj_karte(sve_karte ,"karte.csv", "|")
                                                    print(f"\nUspešno ste kupili kartu za povezani let! Broj Vaše karte je {karta['broj_karte']}\n")
                                    except Exception as e:
                                        print("-->:", e)      
                                elif option1 == "9":
                                    try:
                                        sve_karte = karte.ucitaj_karte_iz_fajla("karte.csv", "|")
                                        korisnikove_karte=[]
                                        logovanje['pasos'] = int(logovanje['pasos'])
                                        logovanje['telefon'] = int(logovanje['telefon'])
                                        for karta in sve_karte:
                                            if sve_karte[karta]['kupac'] == logovanje:
                                                korisnikove_karte.append(sve_karte[karta])
                                        nerealizovane_karte = karte.pregled_nerealizovanaih_karata(logovanje, korisnikove_karte)
                                        print("Broj".ljust(10), "Šifra".ljust(10), "Datum prodaje".ljust(20), "Kupac".ljust(20), "Prodavac".ljust(20), "Sedište".ljust(20), "Obrisana".ljust(20), "Putnici".ljust(15))
                                        for karta in nerealizovane_karte:
                                            putnici_u_karti = []
                                            for putnik in karta['putnici']:
                                                putnici_u_karti.append(putnik['korisnicko_ime'])
                                            print(str(karta['broj_karte']).ljust(10), str(karta['sifra_konkretnog_leta']).ljust(10), str(karta['datum_prodaje']).ljust(20), str(karta['kupac']['korisnicko_ime']).ljust(20), str(karta['prodavac']['korisnicko_ime']).ljust(20), str(karta['sediste']).ljust(20), str(karta['obrisana']).ljust(20), " ".join(str(x) for x in putnici_u_karti))
                                    except Exception as e:
                                        print("-->:", e)
                                    
                                elif option1 == "10": #CHECK-IN KORISNIK   RADI
                                    checkin()
                                else:
                                    print("Nepostojeća opcija!")

                        elif logovanje["uloga"] == konstante.ULOGA_PRODAVAC:
                            prodavac_program = True
                            while prodavac_program:
                                meni_registrovani_prodavac()
                                option = input("Vaš izbor: ")

                                if option == "1":
                                    korisnici.logout(logovanje["korisnicko_ime"])
                                    prodavac_program = False
                                    uloge = False
                                elif option == "2":
                                    print("Izašli ste iz aplikacije. Prijatan dan.")
                                    program = False
                                    exit()
                                elif option == "3":
                                    print("\nPREGLED NEREALIZOVANIH LETOVA:\n")
                                    print("****************************************************************************************\n")
                                    ner_letovi = letovi.pregled_nerealizovanih_letova(svi_letovi)
                                    print("Broj leta".ljust(15), "Polazište".ljust(15), "Odredište".ljust(15), "Polazak".ljust(15), "Dolazak".ljust(15), "Cena".ljust(15))
                                    for let in ner_letovi:
                                        
                                        print(let['broj_leta'].ljust(15), let['sifra_polazisnog_aerodroma'].ljust(15), let['sifra_odredisnog_aerodorma'].ljust(15), let['vreme_poletanja'].ljust(15), let['vreme_sletanja'].ljust(15), str(let['cena']).ljust(15))
                                    print("\n****************************************************************************************\n")
                                elif option == "4":
                                    pretraga()
                                elif option == "5":
                                    visekriterijumska_pretraga()
                                elif option == "6":
                                    top_10()
                                elif option == "7":
                                    fleksibilni_polasci()
                                elif option == "8":  #PRODAJA KARATA U IZRADI
                                    try:
                                        svi_letovi = letovi.ucitaj_letove_iz_fajla("letovi.csv", "|")
                                        sve_karte = karte.ucitaj_karte_iz_fajla("karte.csv", "|")
                                        svi_konkretni_letovi = konkretni_letovi.ucitaj_konkretan_let("konkretni_letovi.csv", "|")
                                        svi_korisnici = korisnici.ucitaj_korisnike_iz_fajla("korisnici.csv", "|")
                                        logovanje['pasos'] = int(logovanje['pasos'])
                                        logovanje['telefon'] = int(logovanje['telefon'])
                                        sifra_konkretnog_leta = int(input("Unesite šifru konkretnog leta: "))
                                        putnici=[]

                                        putnik = input("Da li želite da unesete registrovanog ili neregistrovanog kupca? (1 za registrovanog - 2 za neregistrovanog): ")
                                        if putnik == '1':
                                            kor_ime = input("Unesite korisničko ime kupca: ")
                                            svi_korisnici[kor_ime]['pasos'] = int(svi_korisnici[kor_ime]['pasos'])
                                            svi_korisnici[kor_ime]['telefon'] = int(svi_korisnici[kor_ime]['telefon'])
                                            putnici.append(svi_korisnici[kor_ime])
                                            kupac = putnici[0]
                                        elif putnik == '2':
                                            ime = input("Unesite ime kupca: ")
                                            prezime = input("Unesite prezime kupca: ")
                                            ner_korisnik = korisnici.kreiraj_korisnika({}, False, konstante.ULOGA_KORISNIK, '', 'neregistrovani', 'lozinka', ime, prezime, 'email@email.com', '', '', '', '')
                                            ner_korisnik['neregistrovani']['pasos'] = int(ner_korisnik['neregistrovani']['pasos'])
                                            ner_korisnik['neregistrovani']['telefon'] = int(ner_korisnik['neregistrovani']['telefon'])
                                            putnici.append(ner_korisnik['neregistrovani'])
                                            kupac = putnici[0]
                                        else:
                                            print("\nNepostojeća opcija! Prodaja karata se obustavlja!")
                                            break

                                        sledeci_putnik = True
                                        while sledeci_putnik:
                                            sledeci = input("\nDa li želite da unesete saputnika? (DA/NE): ")
                                            if sledeci == "NE" or sledeci == "ne" or sledeci == "Ne":
                                                sledeci_putnik = False
                                            else:
                                                print("\nKreiranje saputnika\n") 
                                                korisnicko_ime = input("Unesite korisničko ime: ")
                                                lozinka = input("Unesite lozinku: ")
                                                telefon = input("Unesite telefon: ")
                                                email = input("Unesite email: ")
                                                ime = input("Unesite ime: ")
                                                prezime = input("Unesite prezime: ")
                                                pasos = input("Unesite pasoš: ")
                                                drzavljanstvo = input("Unesite državljanstvo: ")
                                                pol = input("Unesite pol: ")
                                                sledeci_putnik1 = korisnici.kreiraj_korisnika({}, False, konstante.ULOGA_KORISNIK, "", korisnicko_ime, lozinka, ime, prezime, email, pasos, drzavljanstvo, telefon, pol)
                                                sledeci_putnik1[korisnicko_ime]['pasos'] = int(sledeci_putnik1[korisnicko_ime]['pasos'])
                                                sledeci_putnik1[korisnicko_ime]['telefon'] = int(sledeci_putnik1[korisnicko_ime]['telefon'])
                                                putnici.append(sledeci_putnik1[korisnicko_ime])
                                        

                                        slobodna_mesta = svi_konkretni_letovi[sifra_konkretnog_leta]['zauzetost']

                                        
                                        karta, sve_karte = karte.kupovina_karte(sve_karte, svi_konkretni_letovi, sifra_konkretnog_leta, putnici, slobodna_mesta, kupac, prodavac = logovanje, datum_prodaje = datetime.strftime(datetime.now(),"%d.%m.%Y."))
                                        karte.sacuvaj_karte(sve_karte ,"karte.csv", "|")
                                        print(f"\nUspešno ste kupili kartu! Broj Vaše karte je {karta['broj_karte']}\n")

                                        povezani_da = input("Da li želite da kupite povezani let (DA/NE): ")
                                        if povezani_da == "DA" or povezani_da == "Da" or povezani_da == "da":
                                            konkretan = svi_konkretni_letovi[sifra_konkretnog_leta]
                                            povezani = letovi.povezani_letovi(svi_letovi, svi_konkretni_letovi, konkretan)
                                            if not povezani:
                                                print("Za uneti let ne postoji povezani let!")
                                            else:

                                                print("Šifra".ljust(7),"Broj leta".ljust(10), "Polazak".ljust(20), "Dolazak".ljust(20))
                                                for let1 in povezani:
                                                    print(str(let1['sifra']).ljust(7), str(let1['broj_leta']).ljust(10), datetime.strftime(let1['datum_i_vreme_polaska'], "%d.%m.%Y. %H:%M").ljust(20), datetime.strftime(let1['datum_i_vreme_dolaska'], "%d.%m.%Y. %H:%M").ljust(20))


                                                sifra_konkretnog_leta1 = int(input("\nUnesite šifru konkretnog leta: "))

                                                if svi_konkretni_letovi[sifra_konkretnog_leta1] in povezani:

                                                    # putnici = kupac
                                                    # sledeci_putnik = True
                                                    # while sledeci_putnik:
                                                    #     sledeci = input("Da li želite da unesete saputnika? (DA/NE): ")
                                                    #     if sledeci == "NE":
                                                    #         sledeci_putnik = False
                                                    #     else: 
                                                    #         print("\nKreiranje saputnika\n")
                                                    #         korisnicko_ime = input("Unesite korisničko ime: ")
                                                    #         lozinka = input("Unesite lozinku: ")
                                                    #         telefon = input("Unesite telefon: ")
                                                    #         email = input("Unesite email: ")
                                                    #         ime = input("Unesite ime: ")
                                                    #         prezime = input("Unesite prezime: ")
                                                    #         pasos = input("Unesite pasoš: ")
                                                    #         drzavljanstvo = input("Unesite državljanstvo: ")
                                                    #         pol = input("Unesite pol: ")
                                                    #         sledeci_putnik1 = korisnici.kreiraj_korisnika({}, False, konstante.ULOGA_KORISNIK, "", korisnicko_ime, lozinka, ime, prezime, email, pasos, drzavljanstvo, telefon, pol)
                                                    #         sledeci_putnik1[korisnicko_ime]['pasos'] = int(sledeci_putnik1[korisnicko_ime]['pasos'])
                                                    #         sledeci_putnik1[korisnicko_ime]['telefon'] = int(sledeci_putnik1[korisnicko_ime]['telefon'])
                                                    #         putnici.append(sledeci_putnik1[korisnicko_ime])
                                                    
                                                    
                                                    slobodna_mesta = svi_konkretni_letovi[sifra_konkretnog_leta1]['zauzetost']

                                                    
                                                    karta, sve_karte = karte.kupovina_karte(sve_karte, svi_konkretni_letovi, sifra_konkretnog_leta1, putnici, slobodna_mesta, kupac, prodavac = logovanje, datum_prodaje = datetime.strftime(datetime.now(),"%d.%m.%Y."))
                                                    karte.sacuvaj_karte(sve_karte ,"karte.csv", "|")
                                                    print(f"Uspešno ste kupili kartu za povezani let! Broj Vaše karte je {karta['broj_karte']}\n")
                                    except Exception as e:
                                        print("-->:", e)
                                elif option == "9":
                                    checkin()
                                    break
                                elif option == "10":     #IZMENA KARTE
                                    try:
                                        sve_karte = karte.ucitaj_karte_iz_fajla("karte.csv", "|")
                                        svi_konkretni_letovi = konkretni_letovi.ucitaj_konkretan_let("konkretni_letovi.csv", "|")
                                        broj_karte = int(input("Unesite broj karte za izmenu: "))
                                        nova_sifra_konkretnog_leta = int(input("Unesite novu šifru konkretnog leta: "))
                                        nov_datum_polaska = datetime.strptime(input("Unesite nov datum prodaje karte: "), "%Y-%m-%d")
                                        sediste = input("Unesite novu šifru sedišta: ")
                                        izmenjene_karte = karte.izmena_karte(sve_karte, svi_konkretni_letovi, broj_karte, nova_sifra_konkretnog_leta, nov_datum_polaska, sediste)
                                        print("Broj".ljust(10), "Šifra".ljust(10), "Datum prodaje".ljust(20), "Kupac".ljust(20), "Prodavac".ljust(20), "Sedište".ljust(20), "Obrisana".ljust(20), "Putnici".ljust(15))
                                        for karta in izmenjene_karte.values():
                                            putnici_u_karti = []
                                            for putnik in karta['putnici']:
                                                putnici_u_karti.append(putnik['korisnicko_ime'])
                                            if isinstance(karta['datum_prodaje'], datetime):
                                                karta['datum_prodaje'] = datetime.strftime(karta['datum_prodaje'], "%d.%m.%Y.")
                                            print(str(karta['broj_karte']).ljust(10), str(karta['sifra_konkretnog_leta']).ljust(10), str(karta['datum_prodaje']).ljust(20), str(karta['kupac']['korisnicko_ime']).ljust(20), str(karta['prodavac']['korisnicko_ime']).ljust(20), str(karta['sediste']).ljust(20), str(karta['obrisana']).ljust(20), " ".join(str(x) for x in putnici_u_karti))
                                    except Exception as e:
                                        print("-->:", e)
                                elif option == "11":   #BRISANJE KARTE STAVLJA 'obrisana' NA TRUE   #RADI
                                    try:
                                        sve_karte = karte.ucitaj_karte_iz_fajla("karte.csv", "|")
                                        print("BRISANJE KARATA\n\nSVE KARTE\n")
                                        print("Broj".ljust(10), "Šifra".ljust(10), "Datum prodaje".ljust(20), "Kupac".ljust(20), "Prodavac".ljust(20), "Sedište".ljust(20), "Obrisana".ljust(20), "Putnici".ljust(15))
                                        for karta in sve_karte.values():
                                            putnici_u_karti = []
                                            for putnik in karta['putnici']:
                                                putnici_u_karti.append(putnik['korisnicko_ime'])
                                            print(str(karta['broj_karte']).ljust(10), str(karta['sifra_konkretnog_leta']).ljust(10), str(karta['datum_prodaje']).ljust(20), str(karta['kupac']['korisnicko_ime']).ljust(20), str(karta['prodavac']['korisnicko_ime']).ljust(20), str(karta['sediste']).ljust(20), str(karta['obrisana']).ljust(20), " ".join(str(x) for x in putnici_u_karti))

                                        broj_karte = int(input("\nUnesite broj karte: "))
                                        trazene_karte = karte.brisanje_karte(logovanje, sve_karte, broj_karte)
                                        print("\n")
                                        print("Broj".ljust(10), "Šifra".ljust(10), "Datum prodaje".ljust(20), "Kupac".ljust(20), "Prodavac".ljust(20), "Sedište".ljust(20), "Obrisana".ljust(20), "Putnici".ljust(15))
                                        for karta in trazene_karte.values():
                                            putnici_u_karti = []
                                            for putnik in karta['putnici']:
                                                putnici_u_karti.append(putnik['korisnicko_ime'])
                                            print(str(karta['broj_karte']).ljust(10), str(karta['sifra_konkretnog_leta']).ljust(10), str(karta['datum_prodaje']).ljust(20), str(karta['kupac']['korisnicko_ime']).ljust(20), str(karta['prodavac']['korisnicko_ime']).ljust(20), str(karta['sediste']).ljust(20), str(karta['obrisana']).ljust(20), " ".join(str(x) for x in putnici_u_karti))
                                        karte.sacuvaj_karte(trazene_karte, "karte.csv", "|")
                                    except Exception as e:
                                        print("-->:", e)
                                elif option == "12": #PRETRAGA PRODATIH KARATA
                                    try:
                                        sve_karte = karte.ucitaj_karte_iz_fajla("karte.csv", "|")
                                        svi_letovi = letovi.ucitaj_letove_iz_fajla("letovi.csv", "|")
                                        svi_konkretni_letovi = konkretni_letovi.ucitaj_konkretan_let("konkretni_letovi.csv", "|")
                                        
                                        polaziste = input("Unesite polazište: ")
                                        odrediste = input("Unesite odredište: ")
                                        datum_polaska = input("Unesite datum polaska: ")
                                        datum_dolaska = input("Unesite datum dolaska: ")
                                        

                                        if datum_polaska:
                                            datum_polaska = datetime.strptime(datum_polaska, "%Y-%m-%d")
                                        else:
                                            datum_polaska = None
                                        if datum_dolaska:
                                            datum_dolaska = datetime.strptime(datum_dolaska, "%Y-%m-%d")
                                        else:
                                            datum_dolaska = None

                                        korisnicko_ime_putnika = input("Unesite korisničko ime putnika: ")

                                        trazene_karte = karte.pretraga_prodatih_karata(sve_karte, svi_letovi, svi_konkretni_letovi, polaziste, odrediste, datum_polaska, datum_dolaska, korisnicko_ime_putnika)
                                        print("\nTražene karte sa zadatim kriterijumima pretrage su:\n")
                                        print("Broj".ljust(10), "Šifra".ljust(10), "Datum prodaje".ljust(20), "Kupac".ljust(20), "Prodavac".ljust(20), "Sedište".ljust(20), "Obrisana".ljust(20), "Putnici".ljust(15))
                                        for karta in trazene_karte:
                                            putnici_u_karti = []
                                            for putnik in karta['putnici']:
                                                putnici_u_karti.append(putnik['korisnicko_ime'])
                                            print(str(karta['broj_karte']).ljust(10), str(karta['sifra_konkretnog_leta']).ljust(10), str(karta['datum_prodaje']).ljust(20), str(karta['kupac']['korisnicko_ime']).ljust(20), str(karta['prodavac']['korisnicko_ime']).ljust(20), str(karta['sediste']).ljust(20), str(karta['obrisana']).ljust(20), " ".join(str(x) for x in putnici_u_karti))
                                    except Exception as e:
                                        print("-->:", e)
                                else:
                                    print("\nNepostojeća opcija za ulogu PRODAVAC!")
                                
                        elif logovanje["uloga"] == konstante.ULOGA_ADMIN:
                            admin_program = True
                            while admin_program:
                                meni_registrovani_admin()
                                option = input("Vaš izbor: ")

                                if option == "1":
                                    korisnici.logout(logovanje["korisnicko_ime"])
                                    admin_program = False
                                    uloge = False
                                elif option == "2":
                                    print("Izašli ste iz aplikacije. Prijatan dan.")
                                    program = False
                                    exit()
                                    
                                elif option == "3":
                                    print("\nPREGLED NEREALIZOVANIH LETOVA:\n")
                                    print("****************************************************************************************\n")
                                    ner_letovi = letovi.pregled_nerealizovanih_letova(svi_letovi)
                                    print("Broj leta".ljust(15), "Polazište".ljust(15), "Odredište".ljust(15), "Polazak".ljust(15), "Dolazak".ljust(15), "Cena".ljust(15))
                                    for let in ner_letovi:
                                        
                                        print(let['broj_leta'].ljust(15), let['sifra_polazisnog_aerodroma'].ljust(15), let['sifra_odredisnog_aerodorma'].ljust(15), let['vreme_poletanja'].ljust(15), let['vreme_sletanja'].ljust(15), str(let['cena']).ljust(15))
                                    print("\n****************************************************************************************\n")
                                elif option == "4":
                                    pretraga()
                                elif option == "5":
                                    visekriterijumska_pretraga()
                                elif option == "6":
                                    top_10()
                                elif option == "7":
                                    fleksibilni_polasci()
                                elif option == "8":  #PRETRAGA PRODATIH KARATA
                                    try:
                                        sve_karte = karte.ucitaj_karte_iz_fajla("karte.csv", "|")
                                        svi_letovi = letovi.ucitaj_letove_iz_fajla("letovi.csv", "|")
                                        svi_konkretni_letovi = konkretni_letovi.ucitaj_konkretan_let("konkretni_letovi.csv", "|")
                                        print("\nPRETRAGA PRODATIH KARATA\n")
                                        print("\nNAPOMENA: Ukoliko ne želite da unesete određeni kriterijum pritisnite ENTER da preskočite!\n")
                                        polaziste = input("Unesite polazište: ")
                                        odrediste = input("Unesite odredište: ")
                                        datum_polaska = input("Unesite datum i vreme polaska: ")
                                        datum_dolaska = input("Unesite datum i vreme dolaska: ")
                                        putnik = input("Unesite korisničko ime putnika: ")

                                        if datum_polaska:
                                            datetime.strptime(datum_polaska, "%Y-%m-%d %H:%M:%S")
                                        if datum_dolaska:
                                            datetime.strptime(datum_dolaska, "%Y-%m-%d %H:%M:%S")

                                        trazene_karte = karte.pretraga_prodatih_karata(sve_karte, svi_letovi, svi_konkretni_letovi, polaziste, odrediste, datum_polaska, datum_dolaska, putnik)
                                        print("Tražene karte sa zadatim kriterijumima pretrage su:\n")
                                        # OVO TREBA KAD ISPRAVE TEST ZA KONKRETNE LETOVE print("Šifra leta".ljust(10),"Datum prodaje".ljust(10), "Kupac".ljust(20), "Prodavac".ljust(20), "Sedište".ljust(20), "Status".ljust(20), "Obrisana".ljust(20), "Putnici".ljust(15))
                                        print("Šifra".ljust(10),"Datum prodaje".ljust(20), "Kupac".ljust(20), "Prodavac".ljust(20), "Sedište".ljust(20), "Obrisana".ljust(20), "Putnici".ljust(15))
                                        for karta in trazene_karte:
                                            putnici_u_karti = []
                                            for putnik in karta['putnici']:
                                                putnici_u_karti.append(putnik['korisnicko_ime'])
                                            print(str(karta['sifra_konkretnog_leta']).ljust(10), str(karta['datum_prodaje']).ljust(20), str(karta['kupac']['korisnicko_ime']).ljust(20), str(karta['prodavac']['korisnicko_ime']).ljust(20), str(karta['sediste']).ljust(20), str(karta['obrisana']).ljust(20), " ".join(str(x) for x in putnici_u_karti))
                                        
                                    except Exception as e:
                                        print("-->:", e)
                                elif option == "9":   #REGISTRACIJA NOVOG PRODAVCA  RADI
                                    try:
                                        svi_korisnici_fajl = korisnici.ucitaj_korisnike_iz_fajla("korisnici.csv", "|")
                                        svi_korisnici_upis = {}

                                        korisnicko_ime = input("Unesite korisničko ime: ")
                                        lozinka = input("Unesite lozinku: ")
                                        telefon = input("Unesite telefon: ")
                                        email = input("Unesite email: ")
                                        ime = input("Unesite ime: ")
                                        prezime = input("Unesite prezime: ")
                                        pasos = input("Unesite pasoš (opciono): ")
                                        drzavljanstvo = input("Unesite državljanstvo (opciono): ")
                                        pol = input("Unesite pol (opciono): ")

                                        svi_korisnici_upis = korisnici.kreiraj_korisnika(svi_korisnici_upis, False, konstante.ULOGA_PRODAVAC, "", korisnicko_ime, lozinka, ime, prezime, email, pasos, drzavljanstvo, telefon, pol)
                                        svi_korisnici_upis[korisnicko_ime]['pasos'] = int(svi_korisnici_upis[korisnicko_ime]['pasos'])
                                        svi_korisnici_upis[korisnicko_ime]['telefon'] = int(svi_korisnici_upis[korisnicko_ime]['telefon'])
                                        svi_korisnici_fajl[korisnicko_ime] = svi_korisnici_upis[korisnicko_ime]
                                        korisnici.sacuvaj_korisnike("korisnici.csv", "|", svi_korisnici_fajl)
                                        print(f"\nUspešno ste registrovali prodavca: {svi_korisnici_upis[korisnicko_ime]['korisnicko_ime']}")

                                        print("\nPREGLED DODATOG PRODAVCA:\n")
                                        
                                        print(f"Uloga: {konstante.ULOGA_PRODAVAC}")
                                        print(f"Korisničko ime: {korisnicko_ime}")
                                        print(f"Lozinka: {lozinka}")
                                        print(f"Telefon: {telefon}")
                                        print(f"Email: {email}")
                                        print(f"Ime: {ime}")
                                        print(f"Prezime: {prezime}")

                                        if not pasos:
                                            print("Pasoš: Nije unet broj pasoša!")
                                        else:
                                            print(f"Pasoš: {pasos}")

                                        if not drzavljanstvo:
                                            print("Državljanstvo: Nije uneto državljanstvo!")
                                        else:
                                            print(f"Državljanstvo: {drzavljanstvo}")

                                        if not pol:
                                            print("Pol: Nije unet pol!\n")
                                        else:
                                            print(f"Pol: {pol}\n")

                                    except Exception as e:
                                        print("-->:", e)
                                elif option == "10":   #KREIRANJE LETOVA  RADI
                                    try:
                                        svi_letovi_test = letovi.ucitaj_letove_iz_fajla("letovi.csv", "|")

                                        broj_leta = input("Unesite broj leta: ")
                                        sifra_polazisnog_aerodroma = input("Unesite šifru polazišnog aerodroma: ")
                                        sifra_odredisnog_aerodorma = input("Unesite šifru odredišnog aerodroma: ")
                                        vreme_poletanja = input("Unesite vreme poletanja: ")
                                        vreme_sletanja = input("Unesite vreme sletanja: ")
                                        sletanje_sutra = input("Sletanje sutra (da/ne): ")
                                        if sletanje_sutra == "da" or sletanje_sutra == "DA" or sletanje_sutra == "Da":
                                            sletanje_sutra = True
                                        if sletanje_sutra == "ne" or sletanje_sutra == "NE" or sletanje_sutra == "Ne":
                                            sletanje_sutra = False

                                        prevoznik = input("Unesite prevoznika: ")
                                        dani = input("Unesite dane: ")

                                        dani_lista = dani.split(", ")
                                        dani_lista = [int(x) for x in dani_lista]
                                        
                                        svi_modeli_aviona = model_aviona.ucitaj_modele_aviona("model.csv", "|")

                                        print("\n\nIZABERITE MODEL AVIONA:\n")
                                        for model_izbor in svi_modeli_aviona.values():
                                            print(f"{model_izbor['id']}. {model_izbor['naziv']}") 

                                        model = int(input("\nUnesite model: "))
                                        model_f= svi_modeli_aviona[model]

                                        cena = float(input("Unesite cenu: "))
                                        datum_pocetka_operativnosti = datetime.strptime(input("Unesite datum početka operativnosti: "), "%Y-%m-%d").date()
                                        datum_kraja_operativnosti = datetime.strptime(input("Unesite datum kraja operativnosti: "), "%Y-%m-%d").date()
    
                                        svi_letovi_novilet = letovi.kreiranje_letova(svi_letovi_test, broj_leta, sifra_polazisnog_aerodroma, sifra_odredisnog_aerodorma, vreme_poletanja, vreme_sletanja, sletanje_sutra, prevoznik, dani_lista, model_f, cena, datum_pocetka_operativnosti, datum_kraja_operativnosti)
                                        letovi.sacuvaj_letove("letovi.csv", "|", svi_letovi_novilet)
                                        print("\nUspešno ste kreirali novi let!\n")
                                    except Exception as e:
                                        print("-->:", e)
                                elif option == "11":     #IZMENA LETOVA PROVERITI ZA SLETANJE SUTRA   PITANJE
                                    try:
                                        svi_letovi_file = letovi.ucitaj_letove_iz_fajla("letovi.csv", "|")
                                        broj_leta = input("Unesite broj leta koji želite da izmenite: ")
                                        sifra_polazisnog_aerodroma = input("Unesite novu šifru polazišnog aerodroma: ")
                                        sifra_odredisnog_aerodorma = input("Unesite novu šifru odredišnog aerodroma: ")
                                        vreme_poletanja = input("Unesite novo vreme poletanja: ")
                                        vreme_sletanja = input("Unesite novo vreme sletanja: ")
                                        sletanje_sutra1 = input("Sletanje sutra (da/ne): ")
                                        if sletanje_sutra1 == "da" or sletanje_sutra1 == "DA" or sletanje_sutra1 == "Da":
                                            sletanje_sutra1 = True
                                        if sletanje_sutra1 == "ne" or sletanje_sutra1 == "NE" or sletanje_sutra1 == "Ne":
                                            sletanje_sutra1 = True
                                        
                                        prevoznik = input("Unesite novog prevoznika: ")
                                        dani = input("Unesite nove dane: ")

                                        dani_lista = dani.split(", ")
                                        dani_lista = [int(x) for x in dani_lista]

                                        svi_modeli_aviona = model_aviona.ucitaj_modele_aviona("model.csv", "|")

                                        print("\n\nIZABERITE MODEL AVIONA:\n")
                                        for model_izbor in svi_modeli_aviona.values():
                                            print(f"{model_izbor['id']}. {model_izbor['naziv']}") 

                                        model = int(input("\nUnesite model: "))
                                        model_f= svi_modeli_aviona[model]

                                        cena = float(input("Unesite cenu: "))
                                        datum_pocetka_operativnosti = datetime.strptime(input("Unesite datum početka operativnosti: "), "%Y-%m-%d").date()
                                        datum_kraja_operativnosti = datetime.strptime(input("Unesite datum kraja operativnosti: "), "%Y-%m-%d").date()

                                        svi_letovi_izmena = letovi.izmena_letova(svi_letovi_file, broj_leta, sifra_polazisnog_aerodroma, sifra_odredisnog_aerodorma, vreme_poletanja, vreme_sletanja, sletanje_sutra1, prevoznik, dani_lista, model_f, cena, datum_pocetka_operativnosti, datum_kraja_operativnosti)
                                        letovi.sacuvaj_letove("letovi.csv", "|", svi_letovi_izmena)
                                    except Exception as e:
                                        print("-->:", e)
                                elif option == "12":  #BRISANJE KARTE ADMIN  #RADI
                                    try:
                                        sve_karte = karte.ucitaj_karte_iz_fajla("karte.csv", "|")
                                        print("BRISANJE KARATA\n\nSVE KARTE\n")
                                        print("Broj".ljust(10), "Šifra".ljust(10), "Datum prodaje".ljust(20), "Kupac".ljust(20), "Prodavac".ljust(20), "Sedište".ljust(20), "Obrisana".ljust(20), "Putnici".ljust(15))
                                        for karta in sve_karte.values():
                                            putnici_u_karti = []
                                            for putnik in karta['putnici']:
                                                putnici_u_karti.append(putnik['korisnicko_ime'])
                                            print(str(karta['broj_karte']).ljust(10), str(karta['sifra_konkretnog_leta']).ljust(10), str(karta['datum_prodaje']).ljust(20), str(karta['kupac']['korisnicko_ime']).ljust(20), str(karta['prodavac']['korisnicko_ime']).ljust(20), str(karta['sediste']).ljust(20), str(karta['obrisana']).ljust(20), " ".join(str(x) for x in putnici_u_karti))

                                        broj_karte = int(input("\nUnesite broj karte: "))
                                        trazene_karte = karte.brisanje_karte(logovanje, sve_karte, broj_karte)
                                        print("\n")
                                        print("Broj".ljust(10), "Šifra".ljust(10), "Datum prodaje".ljust(20), "Kupac".ljust(20), "Prodavac".ljust(20), "Sedište".ljust(20), "Obrisana".ljust(20), "Putnici".ljust(15))
                                        for karta in trazene_karte.values():
                                            putnici_u_karti = []
                                            for putnik in karta['putnici']:
                                                putnici_u_karti.append(putnik['korisnicko_ime'])
                                            print(str(karta['broj_karte']).ljust(10), str(karta['sifra_konkretnog_leta']).ljust(10), str(karta['datum_prodaje']).ljust(20), str(karta['kupac']['korisnicko_ime']).ljust(20), str(karta['prodavac']['korisnicko_ime']).ljust(20), str(karta['sediste']).ljust(20), str(karta['obrisana']).ljust(20), " ".join(str(x) for x in putnici_u_karti))
                                        karte.sacuvaj_karte(trazene_karte, "karte.csv", "|")
                                    except Exception as e:
                                        print("-->:", e)
                                elif option == "13":
                                    izvestaji_petlja = True
                                    while izvestaji_petlja:
                                        try:
                                            sve_karte = karte.ucitaj_karte_iz_fajla("karte.csv", "|")
                                            for karta in sve_karte.values():
                                                karta['datum_prodaje'] = datetime.strptime(karta['datum_prodaje'], "%d.%m.%Y." ).date()

                                            izvestaji_opcije()
                                            option = input("\nVaš izbor: ")

                                            if option == "a":  #RADI
                                                dan = datetime.strptime(input("Unesite datum prodaje (primer ispravnog formata datuma: 2022-11-11): "), "%Y-%m-%d").date()
                                                prodate_karte = izvestaji.izvestaj_prodatih_karata_za_dan_prodaje(sve_karte, dan)
                                                if prodate_karte:
                                                    print("Šifra".ljust(10),"Datum prodaje".ljust(20), "Kupac".ljust(20), "Prodavac".ljust(20), "Sedište".ljust(20), "Obrisana".ljust(20), "Putnici".ljust(15))
                                                    for karta in prodate_karte:
                                                        putnici_u_karti = []
                                                        for putnik in karta['putnici']:
                                                            putnici_u_karti.append(putnik['korisnicko_ime'])
                                                        print(str(karta['sifra_konkretnog_leta']).ljust(10), str(karta['datum_prodaje']).ljust(20), str(karta['kupac']['korisnicko_ime']).ljust(20), str(karta['prodavac']['korisnicko_ime']).ljust(20), str(karta['sediste']).ljust(20), str(karta['obrisana']).ljust(20), " ".join(str(x) for x in putnici_u_karti))
                                                else:
                                                    print(f"Prazna lista!")
                                                break
                                            elif option == "b":  #RADI
                                                svi_konkretni_letovi = konkretni_letovi.ucitaj_konkretan_let("konkretni_letovi.csv", "|")
                                                dan = datetime.strptime(input("Unesite datum polaska (primer ispravnog formata datuma: 2022-11-11): "), "%Y-%m-%d").date()
                                                prodate_karte = izvestaji.izvestaj_prodatih_karata_za_dan_polaska(sve_karte, svi_konkretni_letovi, dan)

                                                if prodate_karte:
                                                    print("Šifra".ljust(10),"Datum prodaje".ljust(20), "Kupac".ljust(20), "Prodavac".ljust(20), "Sedište".ljust(20), "Obrisana".ljust(20), "Putnici".ljust(15))
                                                    for karta in prodate_karte:
                                                        putnici_u_karti = []
                                                        for putnik in karta['putnici']:
                                                            putnici_u_karti.append(putnik['korisnicko_ime'])
                                                        print(str(karta['sifra_konkretnog_leta']).ljust(10), str(karta['datum_prodaje']).ljust(20), str(karta['kupac']['korisnicko_ime']).ljust(20), str(karta['prodavac']['korisnicko_ime']).ljust(20), str(karta['sediste']).ljust(20), str(karta['obrisana']).ljust(20), " ".join(str(x) for x in putnici_u_karti))
                                                else:
                                                    print(f"Prazna lista!")
                                                break
                                            elif option == "c":  #RADI
                                                dan = datetime.strptime(input("Unesite datum prodaje (primer ispravnog formata datuma: 2022-11-11): "), "%Y-%m-%d").date()
                                                prodavac = input("Unesite korisničko ime prodavca: ")
                                                print("\n")
                                                for karta in sve_karte.values():
                                                    if prodavac == karta['prodavac']['korisnicko_ime']:
                                                        prodavac = karta['prodavac']

                                                prodate_karte = izvestaji.izvestaj_prodatih_karata_za_dan_prodaje_i_prodavca(sve_karte, dan, prodavac)
                                                if prodate_karte:
                                                    print("Šifra".ljust(10),"Datum prodaje".ljust(20), "Kupac".ljust(20), "Prodavac".ljust(20), "Sedište".ljust(20), "Obrisana".ljust(20), "Putnici".ljust(15))
                                                    for karta in prodate_karte:
                                                        putnici_u_karti = []
                                                        for putnik in karta['putnici']:
                                                            putnici_u_karti.append(putnik['korisnicko_ime'])
                                                        print(str(karta['sifra_konkretnog_leta']).ljust(10), str(karta['datum_prodaje']).ljust(20), str(karta['kupac']['korisnicko_ime']).ljust(20), str(karta['prodavac']['korisnicko_ime']).ljust(20), str(karta['sediste']).ljust(20), str(karta['obrisana']).ljust(20), " ".join(str(x) for x in putnici_u_karti))
                                                else:
                                                    print(f"Prazna lista!")
                                                break
                                            elif option == "d":  #RADI
                                                
                                                svi_konkretni_letovi = konkretni_letovi.ucitaj_konkretan_let("konkretni_letovi.csv", "|")
                                                svi_letovi = letovi.ucitaj_letove_iz_fajla("letovi.csv", "|")
                                                dan = datetime.strptime(input("Unesite datum prodaje (primer ispravnog formata datuma: 2022-11-11): "), "%Y-%m-%d").date()
                                                ukupan_broj, cena = izvestaji.izvestaj_ubc_prodatih_karata_za_dan_prodaje(sve_karte, svi_konkretni_letovi, svi_letovi, dan)

                                                print(f"\nUkupan broj prodatih karata na dan {datetime.strftime(dan, '%d. %m. %Y.')} -----> {ukupan_broj}")
                                                print(f"\nUkupna cena prodatih karata na dan {datetime.strftime(dan, '%d. %m. %Y.')} -----> {cena}")

                                                break
                                            elif option == "e":  #RADI
                                                svi_konkretni_letovi = konkretni_letovi.ucitaj_konkretan_let("konkretni_letovi.csv", "|")
                                                svi_letovi = letovi.ucitaj_letove_iz_fajla("letovi.csv", "|")
                                                dan = datetime.strptime(input("Unesite datum polaska (primer ispravnog formata datuma: 2022-11-11): "), "%Y-%m-%d").date()
                                                ukupan_broj, cena = izvestaji.izvestaj_ubc_prodatih_karata_za_dan_polaska(sve_karte, svi_konkretni_letovi, svi_letovi, dan)

                                                print(f"\nUkupan broj prodatih karata sa danom polaska {datetime.strftime(dan, '%d. %m. %Y.')} -----> {ukupan_broj}")
                                                print(f"\nUkupna cena prodatih karata sa danom polaska {datetime.strftime(dan, '%d. %m. %Y.')} -----> {cena}")

                                                break
                                            elif option == "f":  #RADI
                                                svi_konkretni_letovi = konkretni_letovi.ucitaj_konkretan_let("konkretni_letovi.csv", "|")
                                                svi_letovi = letovi.ucitaj_letove_iz_fajla("letovi.csv", "|")
                                                dan = datetime.strptime(input("Unesite datum prodaje (primer ispravnog formata datuma: 2022-11-11): "), "%Y-%m-%d").date()
                                                prodavac = input("Unesite korisničko ime prodavca: ")
                                                
                                                for karta in sve_karte.values():
                                                    if prodavac == karta['prodavac']['korisnicko_ime']:
                                                        prodavac = karta['prodavac']
                                                    
                                                ukupan_broj, cena = izvestaji.izvestaj_ubc_prodatih_karata_za_dan_prodaje_i_prodavca(sve_karte, svi_konkretni_letovi, svi_letovi, dan, prodavac)
                                            
                                                print(f"\nUkupan broj prodatih karata na dan prodaje {datetime.strftime(dan, '%d. %m. %Y.')} i prodavca {prodavac['korisnicko_ime']} -----> {ukupan_broj}")
                                                print(f"\nUkupna cena prodatih karata na dan prodaje {datetime.strftime(dan, '%d. %m. %Y.')} i prodavca {prodavac['korisnicko_ime']} -----> {cena}\n")

                                                break
                                            elif option == "g":
                                                sve_karte = karte.ucitaj_karte_iz_fajla("karte.csv", "|")
                                                svi_konkretni_letovi = konkretni_letovi.ucitaj_konkretan_let("konkretni_letovi.csv", "|")
                                                svi_letovi = letovi.ucitaj_letove_iz_fajla("letovi.csv", "|")
                                                izvestaj = izvestaji_30_dana(sve_karte, svi_konkretni_letovi, svi_letovi)
                                                print("\n")
                                                print(f"Prodavac".ljust(20), "Br. prodatih karata".ljust(30), "Ukupna cena prodatih karata".ljust(30))
                                                for prodavac in izvestaj.values():

                                                    print(prodavac[2].ljust(20), str(prodavac[0]).ljust(30), str(prodavac[1]).ljust(30))
                                                break
                                            else:
                                                print(f"Nevalidan unos! Pokušajte ponovo.\n")
                                        except Exception as e:
                                            print("-->:", e)
                                else:
                                    print("\nNepostojeća opcija za ulogu ADMIN!")

                        
                    else:
                        print("Pogrešno korisničko ime ili lozinka! Pokušajte ponovo")

                except Exception as e:
                    print("-->:", e)
                    
        elif option == "2":  # OPCIJA 2 IZLAZAK IZ APLIKACIJE  #RADI
            print("Uspešno ste izašli iz aplikacije.")
            exit()
        elif option == "3":  # OPCIJA 3 PREGLED SVIH NEREALIZOVANIH LETOVA   #RADI
            print("\nPREGLED NEREALIZOVANIH LETOVA:")
            print("\n****************************************************************************************\n")
            ner_letovi = letovi.pregled_nerealizovanih_letova(svi_letovi)
            print("Broj leta".ljust(15), "Polazište".ljust(15), "Odredište".ljust(15), "Polazak".ljust(15), "Dolazak".ljust(15), "Cena".ljust(15))
            for let in ner_letovi:
                
                print(let['broj_leta'].ljust(15), let['sifra_polazisnog_aerodroma'].ljust(15), let['sifra_odredisnog_aerodorma'].ljust(15), let['vreme_poletanja'].ljust(15), let['vreme_sletanja'].ljust(15), str(let['cena']).ljust(15))
            print("\n****************************************************************************************\n")

        elif option == "4":  # OPCIJA 4 PRETRAGA PO JEDNOM KRITERIJUMU
            pretraga()
        elif option == "5":  # OPCIJA 5 VIŠEKRITERIJUMSKA PRETRAGA
            visekriterijumska_pretraga()
        elif option == "6":  # OPCIJA 6 TOP 10 NAJJEFTINIJIH LETOVA U OPADAJUĆEM REDOSLEDU
            top_10()
        elif option == "7":  # OPCIJA 7 FLEKSIBILNI POLASCI
            fleksibilni_polasci()
        elif option == "8":  # RADI
            try:
                svi_korisnici_fajl = korisnici.ucitaj_korisnike_iz_fajla("korisnici.csv", "|")
                svi_korisnici_upis = {}

                korisnicko_ime = input("Unesite korisničko ime: ")
                lozinka = input("Unesite lozinku: ")
                telefon = input("Unesite telefon: ")
                email = input("Unesite email: ")
                ime = input("Unesite ime: ")
                prezime = input("Unesite prezime: ")
                pasos = input("Unesite pasoš (opciono): ")
                drzavljanstvo = input("Unesite državljanstvo (opciono): ")
                pol = input("Unesite pol (opciono): ")

                svi_korisnici_upis = korisnici.kreiraj_korisnika(svi_korisnici_upis, False, konstante.ULOGA_KORISNIK, "", korisnicko_ime, lozinka, ime, prezime, email, pasos, drzavljanstvo, telefon, pol)
                svi_korisnici_upis[korisnicko_ime]['pasos'] = int(svi_korisnici_upis[korisnicko_ime]['pasos'])
                svi_korisnici_upis[korisnicko_ime]['telefon'] = int(svi_korisnici_upis[korisnicko_ime]['telefon'])
                svi_korisnici_fajl[korisnicko_ime] = svi_korisnici_upis[korisnicko_ime]
                korisnici.sacuvaj_korisnike("korisnici.csv", "|", svi_korisnici_fajl)
                print(f"\nUspešno ste registrovali korisnika: {svi_korisnici_upis[korisnicko_ime]['korisnicko_ime']}")
            except Exception as e:
                print("-->:", e)

        else:
            print("Loš unos! Pokušajte ponovo!\n")


