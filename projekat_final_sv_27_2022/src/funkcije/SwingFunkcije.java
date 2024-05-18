package funkcije;


import java.time.*;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

import entity.*;
import enums.Stanje;
import main.Main;

public class SwingFunkcije {
	
	public static void populateJListUsluge(JList<String> list, ArrayList<uslugaTretman> usluge) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        
        for (uslugaTretman usluga : usluge) {
        	String uslugaCustom = usluga.getImeUsluge() + ", " + usluga.getDuzinaTrajanja() + "min, " + usluga.getCena() + "RSD";
            listModel.addElement(uslugaCustom);
        }

        list.setModel(listModel);
    }
	
	public static void populateJListUslugeFilter(JList<String> list, String tip, int trajanje, double cena) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        
        ArrayList<uslugaTretman> filtriraneUsluge = new ArrayList<>();
        
        for (uslugaTretman usluga : uslugaTretman.sveusluge) {
            if ((tip == null || usluga.getTip().getImeTretmana().equals(tip)) &&
                    (trajanje == 0 || usluga.getDuzinaTrajanja() == trajanje) &&
                    (cena == 0 || usluga.getCena() == cena)) {
                filtriraneUsluge.add(usluga);
            }
        }

        
        for (uslugaTretman usluga : filtriraneUsluge) {
        	String uslugaCustom = usluga.getImeUsluge() + ", " + usluga.getDuzinaTrajanja() + "min, " + usluga.getCena() + "RSD";
            listModel.addElement(uslugaCustom);
        }

        list.setModel(listModel);
    }
	
	public static void populateJListUslugeFilterRecepcioner(JList<String> list, uslugaTretman uslugaP, String tip, double cena) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        
        ArrayList<uslugaTretman> filtriraneUsluge = new ArrayList<>();
        
        for (uslugaTretman usluga : uslugaTretman.sveusluge) {
            if ((uslugaP == null || usluga.getIDUsluge() == uslugaP.getIDUsluge()) && (tip == null || usluga.getTip().getImeTretmana().equals(tip)) && (cena == 0 || usluga.getCena() == cena)) {
                filtriraneUsluge.add(usluga);
            }
        }

        
        for (uslugaTretman usluga : filtriraneUsluge) {
        	String uslugaCustom = usluga.getImeUsluge() + ", " + usluga.getTip().getImeTretmana() + ", " + usluga.getDuzinaTrajanja() + "min, " + usluga.getCena() + "RSD";
            listModel.addElement(uslugaCustom);
        }

        list.setModel(listModel);
    }
	
	
	public static void populateJListKozmeticar(JList<String> list, ArrayList<Kozmeticar> kozmeticari) {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (Kozmeticar koz : kozmeticari) {
        	
            listModel.addElement(koz.getKorisnickoIme());
        }

        list.setModel(listModel);
    }
	
	public static void populateJListTipovi(JList<tipTretmana> list, ArrayList<tipTretmana> svitipovi) {
        DefaultListModel<tipTretmana> listModel = new DefaultListModel<>();

        for (tipTretmana tip : svitipovi) {
        	
            listModel.addElement(tip);
        }

        list.setModel(listModel);
    }
	
	
	
	public static void populateJListTermini(JList<LocalTime> list, ArrayList<LocalDateTime> termini) {
        DefaultListModel<LocalTime> listModel = new DefaultListModel<>();

        for (LocalDateTime termin : termini) {
        	
            listModel.addElement(termin.toLocalTime());
        }

        list.setModel(listModel);
    }
	
	public static void populateJListTerminiBezKozmeticara(JList<LocalTime> list, ArrayList<LocalTime> termini) {
        DefaultListModel<LocalTime> listModel = new DefaultListModel<>();

        for (LocalTime termin : termini) {
        	
            listModel.addElement(termin);
        }

        list.setModel(listModel);
    }
	
	public static DefaultTableModel tableModelZakazaniCenovnik() {
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("ID");
	    tableModel.addColumn("Usluga");
	    tableModel.addColumn("Tip");
	    tableModel.addColumn("Trajanje");
        tableModel.addColumn("Cena");

       
        for (uslugaTretman usluga : uslugaTretman.sveusluge) {
            Object[] rowData = {
                    usluga.getIDUsluge(),
                    usluga.getImeUsluge(),
                    usluga.getTip().getImeTretmana(),
                    usluga.getDuzinaTrajanja(),
                    usluga.getCena()
            };
            tableModel.addRow(rowData);
        }
        
        return tableModel;
	}
	
	public static DefaultTableModel tableModelKlijentoviTretmani(Klijent klijent) {
		DefaultTableModel tableModel = new DefaultTableModel();
	    tableModel.addColumn("Usluga");
        tableModel.addColumn("Kozmeticar");
        tableModel.addColumn("Termin");
        tableModel.addColumn("Stanje");
        tableModel.addColumn("Klijent");
        tableModel.addColumn("Potroseno");

       
        for (zakazanTretman tretman : ManagerKlijent.pregledTretmana(klijent)) {
            Object[] rowData = {
                    tretman.getUslugu().getImeUsluge().toString(),
                    tretman.getIzabraniKozmeticar().getKorisnickoIme().toString(),
                    tretman.getTermin().toLocalDate().getDayOfMonth() + "." + tretman.getTermin().toLocalDate().getMonthValue() + "." + tretman.getTermin().toLocalDate().getYear() + ". " + tretman.getTermin().toLocalTime(),
                    tretman.getStanjeTretmana().toString(),
                    tretman.getKlijent().getKorisnickoIme().toString(),
                    tretman.getNaplaceno()
            };
            tableModel.addRow(rowData);
        }
        
        return tableModel;
	}
	
	public static DefaultTableModel tableModelZakazaniTretmani() {
		DefaultTableModel tableModel = new DefaultTableModel();
	    tableModel.addColumn("Usluga");
        tableModel.addColumn("Kozmeticar");
        tableModel.addColumn("Termin");
        tableModel.addColumn("Stanje");
        tableModel.addColumn("Klijent");

       
        for (zakazanTretman tretman : ManagerZakazanTretman.svizakazani) {
            Object[] rowData = {
                    tretman.getUslugu().getImeUsluge().toString(),
                    tretman.getIzabraniKozmeticar().getKorisnickoIme().toString(),
                    tretman.getTermin().toString(),
                    tretman.getStanjeTretmana().toString(),
                    tretman.getKlijent().getKorisnickoIme().toString()
            };
            tableModel.addRow(rowData);
        }
        
        return tableModel;
	}
	
	public static DefaultTableModel tableModelZakazaniTretmaniKozmeticar(Kozmeticar k) {
		DefaultTableModel tableModel = new DefaultTableModel();
	    tableModel.addColumn("Usluga");
        tableModel.addColumn("Kozmeticar");
        tableModel.addColumn("Termin");
        tableModel.addColumn("Stanje");
        tableModel.addColumn("Klijent");

        ArrayList<zakazanTretman> dodeljeni = ManagerKozmeticar.dodeljeniTretmani(k);
        
        if(dodeljeni != null) {
        	
        	for (zakazanTretman tretman : dodeljeni) {
                Object[] rowData = {
                        tretman.getUslugu().getImeUsluge().toString(),
                        tretman.getIzabraniKozmeticar().getKorisnickoIme().toString(),
                        tretman.getTermin().toLocalDate().getDayOfMonth() + "." + tretman.getTermin().toLocalDate().getMonthValue() + "." + tretman.getTermin().toLocalDate().getYear() + ". " + tretman.getTermin().toLocalTime(),
                        tretman.getStanjeTretmana().toString(),
                        tretman.getKlijent().getKorisnickoIme().toString()
                };
                tableModel.addRow(rowData);
            }
        	
        }

        return tableModel;
	}
	
	public static DefaultTableModel tableModelRaspored(Kozmeticar k, LocalDate datum) {
		DefaultTableModel tableModel = new DefaultTableModel();
	    tableModel.addColumn("Usluga");
        tableModel.addColumn("Kozmeticar");
        tableModel.addColumn("Termin");
        tableModel.addColumn("Stanje");
        tableModel.addColumn("Klijent");

        ArrayList<zakazanTretman> dodeljeni = ManagerKozmeticar.raspored(k, datum);
        
        if(dodeljeni != null) {
        	
        	for (zakazanTretman tretman : dodeljeni) {
                Object[] rowData = {
                        tretman.getUslugu().getImeUsluge().toString(),
                        tretman.getIzabraniKozmeticar().getKorisnickoIme().toString(),
                        tretman.getTermin().toLocalDate().getDayOfMonth() + "." + tretman.getTermin().toLocalDate().getMonthValue() + "." + tretman.getTermin().toLocalDate().getYear() + ". " + tretman.getTermin().toLocalTime(),
                        tretman.getStanjeTretmana().toString(),
                        tretman.getKlijent().getKorisnickoIme().toString()
                };
                tableModel.addRow(rowData);
            }
        	
        }

        return tableModel;
	}
	
	
	public static <T> void populateComboBoxModel(JComboBox<T> comboBox, ArrayList<T> elements) {
	    DefaultComboBoxModel<T> model = new DefaultComboBoxModel<>();
	    
	    for (T element : elements) {
	        model.addElement(element);
	    }
	    
	    comboBox.setModel(model);
	}
	
	public static void populateComboBoxModelUsluga(JComboBox<String> comboBox, ArrayList<uslugaTretman> usluge) {
	    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
	    
	    for (uslugaTretman usluga : usluge) {
	    	String uslugaCustom = usluga.getImeUsluge();
	        model.addElement(uslugaCustom);
	    }
	    
	    comboBox.setModel(model);
	    
	}
	
	public static void populateComboBoxModelKozmeticar(JComboBox<String> comboBox, ArrayList<Kozmeticar> kozmeticari) {
	    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
	    
	    for (Kozmeticar kozmeticar : kozmeticari) {
	    	String kozmeticarCustom = kozmeticar.getKorisnickoIme();
	        model.addElement(kozmeticarCustom);
	    }
	    
	    comboBox.setModel(model);
	    
	}
	
	public static void populateComboBoxModelKlijent(JComboBox<String> comboBox, ArrayList<Klijent> klijenti) {
	    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
	    
	    for (Klijent klijent : klijenti) {
	    	String klijentCustom = klijent.getKorisnickoIme();
	        model.addElement(klijentCustom);
	    }
	    
	    comboBox.setModel(model);
	    
	}
	
	public static void populateComboBoxModelVreme(JComboBox<String> comboBox, ArrayList<LocalDateTime> termini) {
	    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
	    
	    for (LocalDateTime termin : termini) {
	    	String terminCustom = termin.toLocalTime().toString();
	        model.addElement(terminCustom);
	    }
	    
	    comboBox.setModel(model);
	    
	}
	
	public static void populateComboBoxModelVreme1(JComboBox<LocalTime> comboBox) {
	    DefaultComboBoxModel<LocalTime> model = new DefaultComboBoxModel<>();
	    
	    for (LocalTime termin : ManagerZakazanTretman.generisiTermine(Main.salon.getPocetakradnogvremena(), Main.salon.getKrajradnogvremena())) {

	        model.addElement(termin);
	    }
	    
	    comboBox.setModel(model);
	    
	}
	
	
	public static DefaultTableModel tableModelIzvestajiKozmeticari(LocalDate pocetak, LocalDate kraj) {
		DefaultTableModel tableModel = new DefaultTableModel();
	    tableModel.addColumn("Kozmeticar");
        tableModel.addColumn("Broj izvrsenih");
        tableModel.addColumn("Prihodovao");
        
        for (Kozmeticar k : ManagerKozmeticar.svikozmeticari) {
        
            Object[] rowData = {
                    k.getKorisnickoIme(),
                    ManagerKozmeticar.brojIzvrsenih(k, pocetak, kraj),
                    ManagerKozmeticar.ukupnoPrihodovao(k, pocetak, kraj)
            };
            tableModel.addRow(rowData);
        }
        
        return tableModel;
	}
	
	public static DefaultTableModel tableModelIzvestajiZakazaniOtkazani(LocalDate pocetak, LocalDate kraj) {
		DefaultTableModel tableModel = new DefaultTableModel();
	    tableModel.addColumn("Stanje");
        tableModel.addColumn("Broj");

       
        
        for(Stanje s : Stanje.values()) {
        	 Object[] red = {
                     s.toString(),
                     ManagerZakazanTretman.brojPoStanju(s, ManagerZakazanTretman.tretmaniZaOdredjeniPeriod(pocetak, kraj))
             };
        	 tableModel.addRow(red);
        }
        
        return tableModel;
	}
	
	public static DefaultTableModel tableModelIzvestajiUsluga(LocalDate pocetak, LocalDate kraj) {
		DefaultTableModel tableModel = new DefaultTableModel();
	    tableModel.addColumn("Ime usluge");
        tableModel.addColumn("Tip");
        tableModel.addColumn("Trajanje");
        tableModel.addColumn("Cena");
        tableModel.addColumn("Broj zakazanih tretmana");
        tableModel.addColumn("Prihod");

       
        
        for(uslugaTretman u : uslugaTretman.sveusluge) {
        	 Object[] red = {
                     u.getImeUsluge(),
                     u.getTip().getImeTretmana(),
                     u.getDuzinaTrajanja(),
                     u.getCena(),
                     ManagerZakazanTretman.brojPoUsluzi(u, ManagerZakazanTretman.tretmaniZaOdredjeniPeriod(pocetak, kraj)),
                     ManagerZakazanTretman.prihodPoUsluzi(u, ManagerZakazanTretman.tretmaniZaOdredjeniPeriod(pocetak, kraj))
             };
        	 tableModel.addRow(red);
        }
        
        return tableModel;
	}
	
	public static DefaultTableModel tableModelUslovZaKL(ArrayList<Klijent> kojiImaju) {
		DefaultTableModel tableModel = new DefaultTableModel();
	    tableModel.addColumn("Ime");
        tableModel.addColumn("Prezime");
        tableModel.addColumn("Korisnicko ime");
        tableModel.addColumn("Kartica lojalnosti");
        tableModel.addColumn("Potrosnja");

        for(Klijent kl : kojiImaju) {
        	 Object[] red = {
                     kl.getIme(),
                     kl.getPrezime(),
                     kl.getKorisnickoIme(),
                     kl.getKl(),
                     kl.getPotrosnja()
             };
        	 tableModel.addRow(red);
        }
        
        return tableModel;
	}
	
	public static DefaultTableModel tableModelRecepcioner() {
		DefaultTableModel tableModel = new DefaultTableModel();
	    tableModel.addColumn("ID");
        tableModel.addColumn("Ime");
        tableModel.addColumn("Prezime");
        tableModel.addColumn("Pol");
        tableModel.addColumn("Telefon");
        tableModel.addColumn("Adresa");
        tableModel.addColumn("Korisnicko ime");
        tableModel.addColumn("Lozinka");
        tableModel.addColumn("Strucna sprema");
        tableModel.addColumn("Staz");
        tableModel.addColumn("Bonus");
        tableModel.addColumn("Plata");

       
        
        for(Recepcioner r : ManagerRecepcioner.svirecepcioneri) {
        	 Object[] red = {
                     r.getID(),
                     r.getIme(),
                     r.getPrezime(),
                     r.getPol(),
                     r.getTelefon(),
                     r.getAdresa(),
                     r.getKorisnickoIme(),
                     r.getLozinka(),
                     r.getStrucnaSprema(),
                     r.getStaz(),
                     r.getBonus(),
                     r.getPlata()
        	 };
        	 tableModel.addRow(red);
        }
        
        return tableModel;
	}
	
	public static DefaultTableModel tableModelKozmeticar() {
		//String[] imenaKolona = {"ID ", "Ime", "Prezime", "Pol", "Telefon", "Adresa", "Korisnicko ime", "Lozinka", "Strucna sprema", "Staz", "Bonus", "Plata", "Obucen za"};
		DefaultTableModel tableModel = new DefaultTableModel();
	    tableModel.addColumn("ID");
        tableModel.addColumn("Ime");
        tableModel.addColumn("Prezime");
        tableModel.addColumn("Pol");
        tableModel.addColumn("Telefon");
        tableModel.addColumn("Adresa");
        tableModel.addColumn("Korisnicko ime");
        tableModel.addColumn("Lozinka");
        tableModel.addColumn("Strucna sprema");
        tableModel.addColumn("Staz");
        tableModel.addColumn("Bonus");
        tableModel.addColumn("Plata");
        tableModel.addColumn("Obucen za");

       
        
        for(Kozmeticar k : ManagerKozmeticar.svikozmeticari) {
        	 Object[] red = {
                     k.getID(),
                     k.getIme(),
                     k.getPrezime(),
                     k.getPol(),
                     k.getTelefon(),
                     k.getAdresa(),
                     k.getKorisnickoIme(),
                     k.getLozinka(),
                     k.getStrucnaSprema(),
                     k.getStaz(),
                     k.getBonus(),
                     k.getPlata(),
                     k.getObucenZa()
        	 };
        	 tableModel.addRow(red);
        }
        
        return tableModel;
	}
	
	public static DefaultTableModel tableModelMenadzer() {
		DefaultTableModel tableModel = new DefaultTableModel();
	    tableModel.addColumn("ID");
        tableModel.addColumn("Ime");
        tableModel.addColumn("Prezime");
        tableModel.addColumn("Pol");
        tableModel.addColumn("Telefon");
        tableModel.addColumn("Adresa");
        tableModel.addColumn("Korisnicko ime");
        tableModel.addColumn("Lozinka");
        tableModel.addColumn("Strucna sprema");
        tableModel.addColumn("Staz");
        tableModel.addColumn("Bonus");
        tableModel.addColumn("Plata");

       
        
        for(Menadzer m : ManagerMenadzer.svimenadzeri) {
        	 Object[] red = {
                     m.getID(),
                     m.getIme(),
                     m.getPrezime(),
                     m.getPol(),
                     m.getTelefon(),
                     m.getAdresa(),
                     m.getKorisnickoIme(),
                     m.getLozinka(),
                     m.getStrucnaSprema(),
                     m.getStaz(),
                     m.getBonus(),
                     m.getPlata()

        	 };
        	 tableModel.addRow(red);
        }
        
        return tableModel;
	}
	
	public static void isprazniPolja(JTextField ime, JTextField prezime, JComboBox<String> pol, JTextField telefon, JTextField adresa, JTextField username, JTextField lozinka, JTextField strucna, JTextField staz, JTextField bonus, JTextField plata) {
		ime.setText(null);
		prezime.setText(null);
		pol.setSelectedItem("M");
		telefon.setText(null);
		adresa.setText(null);
		username.setText(null);
		lozinka.setText(null);
		strucna.setText(null);
		staz.setText(null);
		bonus.setText(null);
		plata.setText(null);
	}
	
	public static double chartUdeoStanja(Stanje s) {
		double procenat;
		int broj = 0;
		LocalDate danas = LocalDate.now();
        LocalDate preMesecDana = danas.minusDays(30);
        
        ArrayList<zakazanTretman> tretmani = ManagerZakazanTretman.tretmaniZaOdredjeniPeriod(preMesecDana, danas);
        
        for(zakazanTretman t : tretmani) {
        	if(t.getStanjeTretmana().equals(s)) {
        		broj++;
        	}
        }
        
        procenat = ((double) broj/tretmani.size())*100;
        
        return procenat;
	}
	
	
	public static double chartUdeoKozmeticara(Kozmeticar k) {
		double procenat;
		int broj = 0;
		LocalDate danas = LocalDate.now();
        LocalDate preMesecDana = danas.minusDays(30);
        
        ArrayList<zakazanTretman> tretmani = ManagerZakazanTretman.tretmaniZaOdredjeniPeriod(preMesecDana, danas);
        
        for(zakazanTretman t : tretmani) {
        	if(t.getIzabraniKozmeticar().equals(k) && t.getStanjeTretmana().equals(Stanje.IZVRSEN)) {
        		broj++;
        	}
        }
        
        procenat = ((double) broj/tretmani.size())*100;
        
        return procenat;
	}
	
	public static double[] prihodiChart(tipTretmana tip) {
	    double[] meseci = new double[12];
	    for(int i = 0; i < meseci.length; i++) {
	    	meseci[i] = izracunajPrihodeZaMesec(i+1, tip);
	    }
	    return meseci;
	}
	
	public static double izracunajPrihodeZaMesec(int i, tipTretmana tip) {
		double prihodi = 0;
		LocalDate prviUMesecu = LocalDate.of(LocalDate.now().getYear(), i, 1);
		LocalDate poslednjiUMesecu = prviUMesecu.withDayOfMonth(prviUMesecu.lengthOfMonth());
		ArrayList<zakazanTretman> tretmani = ManagerZakazanTretman.tretmaniZaOdredjeniPeriod(prviUMesecu, poslednjiUMesecu);
		for(zakazanTretman z : tretmani) {
			if(z.getStanjeTretmana().equals(Stanje.IZVRSEN) || z.getStanjeTretmana().equals(Stanje.ZAKAZAN) && z.getUslugu().getTip().getImeTretmana().equals(tip.getImeTretmana())) {
				prihodi += z.getUslugu().getCena();
			}
		}
		return prihodi;
	}
	

    public static double[] getIndices(int length) {
        double[] indices = new double[length];
        for (int i = 0; i < length; i++) {
            indices[i] = i;
        }
        return indices;
    }
    
    
    public static void populateComboBoxTipFilter(JComboBox<String> comboBox, ArrayList<tipTretmana> tipovi) {
	    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
	    model.addElement("Filtriraj po tipu");
	    for (tipTretmana tip : tipovi) {
	        model.addElement(tip.getImeTretmana());
	    }
	    
	    comboBox.setModel(model);
	    
	}
    
    public static void populateComboBoxTrajanjeFilter(JComboBox<String> comboBox, ArrayList<uslugaTretman> tretmani) {
	    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
	    ArrayList<Integer> trajanja = new ArrayList<>();
	    model.addElement("Filtriraj po trajanju");
	    for (uslugaTretman t : tretmani) {
	    	if(!trajanja.contains(t.getDuzinaTrajanja())) {
	    		trajanja.add(t.getDuzinaTrajanja());
	    	}
	    }
	    Collections.sort(trajanja);
	    for (Integer t : trajanja) {

	        model.addElement(Integer.toString(t) + " minuta");
	    }
	    
	    comboBox.setModel(model);
	    
	}
    
    public static void populateComboBoxCenaFilter(JComboBox<String> comboBox, ArrayList<uslugaTretman> tretmani) {
	    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
	    ArrayList<Double> cene = new ArrayList<>();
	    model.addElement("Filtriraj po ceni");
	    for (uslugaTretman t : tretmani) {
	    	if(!cene.contains(t.getCena())) {
	    		cene.add(t.getCena());
	    	}
	    }
	    Collections.sort(cene);
	    for (Double c : cene) {

	        model.addElement(Double.toString(c) + " RSD");
	    }
	    
	    comboBox.setModel(model);
	    
	}
    
    public static void populateComboBoxUslugaFilter(JComboBox<String> comboBox, ArrayList<uslugaTretman> usluge) {
	    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
	    model.addElement("Filtriraj po usluzi");
	    for (uslugaTretman usluga : usluge) {
	        model.addElement(usluga.getImeUsluge());
	    }
	    
	    comboBox.setModel(model);
	    
	}
    
    public static boolean isDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
