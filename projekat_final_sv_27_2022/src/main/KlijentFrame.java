package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;


import entity.Klijent;
import entity.Kozmeticar;
import entity.tipTretmana;
import entity.uslugaTretman;
import entity.zakazanTretman;
import enums.Stanje;
import funkcije.Funkcionalnosti;
import funkcije.ManagerKlijent;
import funkcije.ManagerKozmeticar;
import funkcije.ManagerZakazanTretman;
import funkcije.SwingFunkcije;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JList;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import com.toedter.calendar.JDateChooser;
import java.beans.PropertyChangeListener;
import java.time.*;
import java.beans.PropertyChangeEvent;
import javax.swing.JSeparator;
import javax.swing.border.MatteBorder;
import java.awt.Color;

public class KlijentFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTable tabelaPregled;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KlijentFrame frame = new KlijentFrame();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public KlijentFrame() {
		int KorisnikID = Funkcionalnosti.ucitajID("data/id.csv");    //OVO VRATI KADA ISTESTIRAS
		//int KorisnikID = 113;
		setTitle("PhiAcademy - Klijent");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBounds(100, 100, 1163, 823);
	    getContentPane().setLayout(null);
	    
	    
	    //setExtendedState(JFrame.MAXIMIZED_BOTH);
	    
	    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	    tabbedPane.setBounds(10, 74, 1128, 695);
	    getContentPane().add(tabbedPane);
	    
	    JPanel panel = new JPanel();
	    tabbedPane.addTab("Zakazi tretman", null, panel, null);
	    panel.setLayout(null);
	    
	    JPanel panel_2 = new JPanel();
	    panel_2.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
	    panel_2.setBounds(10, 85, 189, 286);
	    panel.add(panel_2);
	    panel_2.setLayout(null);
	    
	    JLabel kozmeticarLbl = new JLabel("Kozmeticar:");
	    kozmeticarLbl.setHorizontalAlignment(SwingConstants.CENTER);
	    kozmeticarLbl.setBounds(551, 42, 165, 20);
	    kozmeticarLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    panel.add(kozmeticarLbl);
	    
	    JLabel tretmanLbl = new JLabel("Tretman:");
	    tretmanLbl.setHorizontalAlignment(SwingConstants.CENTER);
	    tretmanLbl.setBounds(263, 42, 228, 20);
	    tretmanLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    panel.add(tretmanLbl);
	    
	    JLabel terminLbl = new JLabel("Termin:");
	    terminLbl.setHorizontalAlignment(SwingConstants.CENTER);
	    terminLbl.setBounds(777, 148, 165, 20);
	    terminLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    panel.add(terminLbl);
	    
	    JButton zakaziKlijent = new JButton("ZAKAZI");
	    zakaziKlijent.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    
	    zakaziKlijent.setBounds(551, 330, 391, 41);
	    panel.add(zakaziKlijent);
	    
	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setBounds(263, 85, 228, 286);
	    panel.add(scrollPane);
	    
	    
	    JComboBox tipFilter = new JComboBox();
	    tipFilter.setBounds(10, 42, 169, 22);
	    panel_2.add(tipFilter);
	    
	    SwingFunkcije.populateComboBoxTipFilter(tipFilter, tipTretmana.svitipovi);
	    
	    JComboBox trajanjeFilter = new JComboBox();
	    trajanjeFilter.setBounds(10, 106, 169, 22);
	    panel_2.add(trajanjeFilter);
	    
	    SwingFunkcije.populateComboBoxTrajanjeFilter(trajanjeFilter, uslugaTretman.sveusluge);
	    
	    JComboBox cenaFilter = new JComboBox();
	    cenaFilter.setBounds(10, 170, 169, 22);
	    panel_2.add(cenaFilter);
	    
	    SwingFunkcije.populateComboBoxCenaFilter(cenaFilter, uslugaTretman.sveusluge);
	    
	    ArrayList<Kozmeticar> kozmeticari = new ArrayList<>();
	    JList list = new JList();
	    
	    if(tipFilter.getSelectedIndex() == 0 && trajanjeFilter.getSelectedIndex() == 0 && cenaFilter.getSelectedIndex() == 0) {
	    	SwingFunkcije.populateJListUsluge(list, uslugaTretman.sveusluge);
	    }
	    
	    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    scrollPane.setViewportView(list);
	    
	    
	    JScrollPane scrollPane_1 = new JScrollPane();
	    scrollPane_1.setBounds(551, 85, 165, 191);
	    panel.add(scrollPane_1);
	    
	    JList listKozmeticar = new JList();
	    scrollPane_1.setViewportView(listKozmeticar);
	    listKozmeticar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    
	    JScrollPane scrollPane_2 = new JScrollPane();
	    scrollPane_2.setBounds(777, 191, 165, 85);
	    panel.add(scrollPane_2);
	    
	    JList terminList = new JList();
	    terminList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    scrollPane_2.setViewportView(terminList);
	    
	    JDateChooser dateChooser = new JDateChooser();
	    
	    
	    
	    list.addListSelectionListener(new ListSelectionListener() {
	    	public void valueChanged(ListSelectionEvent e) {
	    		int selektovano = list.getSelectedIndex();
	    		
	    		if(selektovano != -1) {
	    			
	    		
	    		uslugaTretman usluga = uslugaTretman.sveusluge.get(selektovano);
	    		ArrayList<Kozmeticar> kozmeticari = ManagerKozmeticar.kvalifikovaniKozmericari(usluga);
	    		SwingFunkcije.populateJListKozmeticar(listKozmeticar, kozmeticari);
	    		
	    		
	    		dateChooser.addPropertyChangeListener(new PropertyChangeListener() {
	    	    	public void propertyChange(PropertyChangeEvent evt) {
	    	    		
	    	    		if(listKozmeticar.getSelectedIndex() != -1) {
	    	    			
	    	    			Kozmeticar selektovanKozmeticar = ManagerKozmeticar.nadjiKozmeticaraPoKorisnickomImenu(listKozmeticar.getSelectedValue().toString());
		    	    		if (evt.getNewValue() instanceof Date) {
		    	    			
		                        Date selektovanDatum = (Date) evt.getNewValue();
		                        LocalDate selektovanDatumLocal = selektovanDatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		                        ArrayList<LocalDateTime> slobodniterminizadan = ManagerKozmeticar.nadjiSlobodneTermine(selektovanKozmeticar, selektovanDatumLocal, Main.salon.getPocetakradnogvremena(), Main.salon.getKrajradnogvremena());
		                        SwingFunkcije.populateJListTermini(terminList, slobodniterminizadan);
		    	    			}
	    	    			
	    	    		}
	    	    		else {
	    	    			
	    	    			if (evt.getNewValue() instanceof Date) {
		                        Date selektovanDatum = (Date) evt.getNewValue(); 
		                        LocalDate selektovanDatumLocal = selektovanDatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		                        SwingFunkcije.populateJListTerminiBezKozmeticara(terminList, ManagerZakazanTretman.generisiTermine(Main.salon.getPocetakradnogvremena(), Main.salon.getKrajradnogvremena()));
		                        
		    	    		}
	    	    			
	    	    		}
	    	    		
	    	    	}
	    	    }); //datChooser
	    		
	    		}
	    	}

	    });
	    
	    
	    
	    
	    
	    
	    JLabel datumLbl = new JLabel("Datum:");
	    datumLbl.setHorizontalAlignment(SwingConstants.CENTER);
	    datumLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    datumLbl.setBounds(777, 42, 165, 20);
	    panel.add(datumLbl);
	    
	    
	    
	    dateChooser.setBounds(777, 85, 165, 33);
	    panel.add(dateChooser);
	    
	    JSeparator separator = new JSeparator();
	    separator.setBounds(10, 382, 949, 20);
	    panel.add(separator);
	    
	    
	    
	    
	    
	    JLabel tipLbl = new JLabel("Tip:");
	    tipLbl.setBounds(10, 11, 169, 20);
	    panel_2.add(tipLbl);
	    tipLbl.setHorizontalAlignment(SwingConstants.CENTER);
	    tipLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    
	    
	    
	    JLabel trajanjeLbl = new JLabel("Trajanje:");
	    trajanjeLbl.setBounds(10, 75, 169, 20);
	    panel_2.add(trajanjeLbl);
	    trajanjeLbl.setHorizontalAlignment(SwingConstants.CENTER);
	    trajanjeLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    
	    
	    
	    
	    
	    JLabel cenaLbl = new JLabel("Cena:");
	    cenaLbl.setBounds(10, 139, 169, 20);
	    panel_2.add(cenaLbl);
	    cenaLbl.setHorizontalAlignment(SwingConstants.CENTER);
	    cenaLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    
	    JButton ocistiFiltere = new JButton("OCISTI FILTERE");
	    ocistiFiltere.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    ocistiFiltere.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		tipFilter.setSelectedIndex(0);
	    		trajanjeFilter.setSelectedIndex(0);
	    		cenaFilter.setSelectedIndex(0);
	    		SwingFunkcije.populateJListUsluge(list, uslugaTretman.sveusluge);
	    	}
	    });
	    ocistiFiltere.setBounds(10, 237, 169, 38);
	    panel_2.add(ocistiFiltere);
	    
	    JLabel filterLbl = new JLabel("Filteri:");
	    filterLbl.setHorizontalAlignment(SwingConstants.CENTER);
	    filterLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    filterLbl.setBounds(10, 42, 189, 20);
	    panel.add(filterLbl);
	    
	    cenaFilter.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if(tipFilter.getSelectedIndex() == 0 && trajanjeFilter.getSelectedIndex() == 0 && cenaFilter.getSelectedIndex() == 0) {
	    			SwingFunkcije.populateJListUsluge(list, uslugaTretman.sveusluge);
	    			listKozmeticar.setModel(new DefaultListModel<>());
	    		}
	    		else {
	    			String tip = null;
	    		    if (tipFilter.getSelectedIndex() != 0) {
	    		        tip = tipFilter.getSelectedItem().toString();
	    		    }

	    		    int trajanje = 0;
	    		    if (trajanjeFilter.getSelectedIndex() != 0) {
	    		        trajanje = Integer.parseInt(trajanjeFilter.getSelectedItem().toString().split(" ")[0]);
	    		    }

	    		    double cena = 0;
	    		    if (cenaFilter.getSelectedIndex() != 0) {
	    		    	cena = Double.parseDouble(cenaFilter.getSelectedItem().toString().split(" ")[0]);
	    		    }
	    		    
	    		    SwingFunkcije.populateJListUslugeFilter(list, tip, trajanje, cena);
	    		    listKozmeticar.setModel(new DefaultListModel<>());
	    		}
	    	}
	    });
	    
	    //TRAJANJE FILTER
	    trajanjeFilter.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if(tipFilter.getSelectedIndex() == 0 && trajanjeFilter.getSelectedIndex() == 0 && cenaFilter.getSelectedIndex() == 0) {
	    			SwingFunkcije.populateJListUsluge(list, uslugaTretman.sveusluge);
	    			listKozmeticar.setModel(new DefaultListModel<>());
	    		}
	    		else {
	    			String tip = null;
	    		    if (tipFilter.getSelectedIndex() != 0) {
	    		        tip = tipFilter.getSelectedItem().toString();
	    		    }

	    		    int trajanje = 0;
	    		    if (trajanjeFilter.getSelectedIndex() != 0) {
	    		    	trajanje = Integer.parseInt(trajanjeFilter.getSelectedItem().toString().split(" ")[0]);
	    		    }

	    		    double cena = 0;
	    		    if (cenaFilter.getSelectedIndex() != 0) {
	    		        cena = Double.parseDouble(cenaFilter.getSelectedItem().toString().split(" ")[0]);
	    		    }
	    		    
	    		    SwingFunkcije.populateJListUslugeFilter(list, tip, trajanje, cena);
	    		    listKozmeticar.setModel(new DefaultListModel<>());
	    		}
	    	}
	    });
	    //TIP FILTER
	    tipFilter.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if(tipFilter.getSelectedIndex() == 0 && trajanjeFilter.getSelectedIndex() == 0 && cenaFilter.getSelectedIndex() == 0) {
	    			SwingFunkcije.populateJListUsluge(list, uslugaTretman.sveusluge);
	    			listKozmeticar.setModel(new DefaultListModel<>());
	    		}
	    		else {
	    			String tip = null;
	    		    if (tipFilter.getSelectedIndex() != 0) {
	    		        tip = tipFilter.getSelectedItem().toString();
	    		    }

	    		    int trajanje = 0;
	    		    if (trajanjeFilter.getSelectedIndex() != 0) {
	    		    	trajanje = Integer.parseInt(trajanjeFilter.getSelectedItem().toString().split(" ")[0]);
	    		    }

	    		    double cena = 0;
	    		    if (cenaFilter.getSelectedIndex() != 0) {
	    		    	cena = Double.parseDouble(cenaFilter.getSelectedItem().toString().split(" ")[0]);
	    		    }
	    		    
	    		    SwingFunkcije.populateJListUslugeFilter(list, tip, trajanje, cena);
	    		    listKozmeticar.setModel(new DefaultListModel<String>());
	    		}
	    	}
	    });
	    
	    JPanel panel_1 = new JPanel();
	    tabbedPane.addTab("Pregledaj svoje tretmane", null, panel_1, null);
	    panel_1.setLayout(null);
	    
	    
	    JScrollPane scrollPane_3 = new JScrollPane();
	    scrollPane_3.setBounds(10, 45, 949, 353);
	    panel_1.add(scrollPane_3);
	    
	    tabelaPregled = new JTable(SwingFunkcije.tableModelKlijentoviTretmani(ManagerKlijent.nadjiKlijentapoID(KorisnikID)));
	    scrollPane_3.setViewportView(tabelaPregled);
	    
	    
        
	    
//	    tabelaPregled = new JTable(SwingFunkcije.tableModelKlijentoviTretmani(ManagerKlijent.nadjiKlijentapoID(KorisnikID)));
//	    scrollPane_3.setViewportView(tabelaPregled);
	    
	    JLabel labelKLStanje = new JLabel("Stanje na kartici lojalnosti: " + ManagerKlijent.nadjiKlijentapoID(KorisnikID).getPotrosnja() + "RSD");
	    labelKLStanje.setFont(new Font("Tahoma", Font.PLAIN, 20));
	    labelKLStanje.setBounds(10, 471, 423, 32);
	    panel_1.add(labelKLStanje);
	    
	    JButton otkaziTretmanKlijent = new JButton("OTKAZI TRETMAN");
	    otkaziTretmanKlijent.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    
	    otkaziTretmanKlijent.setBounds(759, 471, 200, 32);
	    panel_1.add(otkaziTretmanKlijent);
	    
	    JLabel lblNewLabel = new JLabel("Pozdrav, " + ManagerKlijent.nadjiKlijentapoID(KorisnikID).getIme() + "!");
	    lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
	    lblNewLabel.setBounds(10, 11, 974, 33);
	    getContentPane().add(lblNewLabel);
	    
	    
	    
	    
	    zakaziKlijent.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		int redniBroj = terminList.getSelectedIndex();
	    		
	    		if(listKozmeticar.getSelectedIndex() != -1) {   //IZABRAN KOZMETICAR
	    		
	    		if(redniBroj != -1) {
		    		uslugaTretman usluga = uslugaTretman.sveusluge.get(list.getSelectedIndex());
	    			Klijent k = ManagerKlijent.nadjiKlijentapoID(KorisnikID);
	    			Kozmeticar selektovanKozmeticar = ManagerKozmeticar.nadjiKozmeticaraPoKorisnickomImenu(listKozmeticar.getSelectedValue().toString());
	    			Date selektovanDatum = dateChooser.getDate();
                    LocalDate selektovanDatumLocal = selektovanDatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    ArrayList<LocalDateTime> slobodniterminizadan = ManagerKozmeticar.nadjiSlobodneTermine(selektovanKozmeticar, selektovanDatumLocal, Main.salon.getPocetakradnogvremena(), Main.salon.getKrajradnogvremena());
	    			LocalDateTime selektovanTermin = slobodniterminizadan.get(redniBroj);
    	    		
    	    		if(list.getSelectedIndex() == -1 || listKozmeticar.getSelectedIndex() == -1 || terminList.getSelectedIndex() == -1 || dateChooser.getDate() == null) {
    	    			JOptionPane.showMessageDialog(null, "Nisu izabrana sva polja.");
    	    		}
    	    		else {
    	    			zakazanTretman z = ManagerKlijent.zakaziTretman(usluga, selektovanKozmeticar, selektovanTermin, k);
    	    			ManagerZakazanTretman.svizakazani.add(z);
    	    			tabelaPregled.setModel(SwingFunkcije.tableModelKlijentoviTretmani(k));
    	    			labelKLStanje.setText("Stanje na kartici lojalnosti: " + ManagerKlijent.nadjiKlijentapoID(KorisnikID).getPotrosnja() + "RSD");
    	    			JOptionPane.showMessageDialog(null, "Uspesno zakazan tretman.");
    	    			tipFilter.setSelectedIndex(0);
    		    		trajanjeFilter.setSelectedIndex(0);
    		    		cenaFilter.setSelectedIndex(0);
    		    		list.setSelectedIndex(-1);
    		    		listKozmeticar.setSelectedIndex(-1);
    		    		dateChooser.setDate(null);
    		    		terminList.setModel(new DefaultListModel());

    	    		}
	    			
	    		}
	    		else {
	    			JOptionPane.showMessageDialog(null, "Nije izabran termin.");

	    		}
	    		

	    		}
	    		else {
	    			
	    			if(redniBroj != -1) {
    	    			LocalTime vrednost = (LocalTime) terminList.getSelectedValue();
    	    			Date selektovanDatum = dateChooser.getDate();
    	    			uslugaTretman usluga = uslugaTretman.sveusluge.get(list.getSelectedIndex());
                        
                        LocalDate selektovanDatumLocal = selektovanDatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    	    			
        	    		if(list.getSelectedIndex() == -1 || terminList.getSelectedIndex() == -1 || dateChooser.getDate() == null) {
        	    			JOptionPane.showMessageDialog(null, "Nisu izabrana sva polja.");
        	    		}
        	    		else {
        	    			LocalDateTime selektovanTermin = selektovanDatumLocal.atTime(vrednost);
        	    			Klijent k = ManagerKlijent.nadjiKlijentapoID(KorisnikID);
        	    			zakazanTretman z = ManagerKlijent.zakaziTretmanBezKozmeticara(usluga, selektovanTermin, k);
        	    			
        	    			if(z == null) {
        	    				JOptionPane.showMessageDialog(null, "Nije slobodan termin.");
        	    			}
        	    			else {
        	    				ManagerZakazanTretman.svizakazani.add(z);
        	    				tabelaPregled.setModel(SwingFunkcije.tableModelKlijentoviTretmani(k));
        	    				labelKLStanje.setText("Stanje na kartici lojalnosti: " + ManagerKlijent.nadjiKlijentapoID(KorisnikID).getPotrosnja() + "RSD");
        	    				JOptionPane.showMessageDialog(null, "Uspesno zakazan termin.");
        	    			}
        	    			tipFilter.setSelectedIndex(0);
        		    		trajanjeFilter.setSelectedIndex(0);
        		    		cenaFilter.setSelectedIndex(0);
        		    		list.setSelectedIndex(-1);
        		    		listKozmeticar.setSelectedIndex(-1);
        		    		dateChooser.setDate(null);
        		    		terminList.setModel(new DefaultListModel());
        	    			
        	    		}
    	    			
    	    		}
    	    		else {
    	    			JOptionPane.showMessageDialog(null, "Nije izabran termin.");

    	    		}
	    		}
	    		
	    	}
	    });
	    
	    JButton odjava = new JButton("Odjavi se");
	    
	    odjava.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    odjava.setBounds(1014, 11, 125, 33);
	    getContentPane().add(odjava);
	    
	    odjava.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		Funkcionalnosti.openFrame("Start");
	    		dispose();
	    	}
	    });
	    
	    otkaziTretmanKlijent.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		int selektovano = tabelaPregled.getSelectedRow();
	    		int columnCount = tabelaPregled.getColumnCount();
	    		
                if (selektovano != -1) {
                	Object[] rowData = new Object[columnCount];

                    for (int i = 0; i < columnCount; i++) {
                        rowData[i] = tabelaPregled.getModel().getValueAt(selektovano, i);
                    }

                    
					String klijent = (String) rowData[4];
					zakazanTretman konkretanTretman = ManagerKlijent.pregledTretmana(ManagerKlijent.nadjiKlijentapoKorisnickomImenu(klijent)).get(selektovano);
                    
					if(konkretanTretman.getStanjeTretmana().equals(Stanje.ZAKAZAN)) {
						ManagerKlijent.otkaziTretman(konkretanTretman, ManagerKlijent.nadjiKlijentapoKorisnickomImenu(klijent));
						DefaultTableModel model = SwingFunkcije.tableModelKlijentoviTretmani(ManagerKlijent.nadjiKlijentapoID(KorisnikID));
	                    model.setValueAt(Stanje.OTKAZAO_KLIJENT, selektovano, 3);
	                    labelKLStanje.setText("Stanje na kartici lojalnosti: " + ManagerKlijent.nadjiKlijentapoID(KorisnikID).getPotrosnja() + "RSD");

	                    Klijent k = ManagerKlijent.nadjiKlijentapoID(KorisnikID);
	                    Funkcionalnosti.otkazaoKlijent(konkretanTretman, selektovano, k, Main.salon);
	                    model.setValueAt(konkretanTretman.getNaplaceno(), selektovano, 5);
	                    tabelaPregled.setModel(model);
	                    labelKLStanje.setText("Stanje na kartici lojalnosti: " + Funkcionalnosti.potrosenaSuma(ManagerKlijent.pregledTretmana(k)) + "RSD");
					}
					else if(konkretanTretman.getStanjeTretmana().equals(Stanje.OTKAZAO_KLIJENT) || konkretanTretman.getStanjeTretmana().equals(Stanje.OTKAZAO_SALON)) {
						JOptionPane.showMessageDialog(null, "Ne mozete da otkazete vec otkazani tretman.");
					}
					else {
						JOptionPane.showMessageDialog(null, "Ne mozete da otkazete izabrani tretman.");
					}
				}
                else {
                	JOptionPane.showMessageDialog(null, "Morate selektovati tretman koji zelite da otkazete.");
                }
	    	}
	    });

	    
	}
}
