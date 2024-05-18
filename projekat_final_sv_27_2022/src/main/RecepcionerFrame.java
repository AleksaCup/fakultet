package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import entity.Klijent;
import entity.Kozmeticar;
import entity.tipTretmana;
import entity.uslugaTretman;
import entity.zakazanTretman;

import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import enums.Stanje;
import funkcije.Funkcionalnosti;
import funkcije.ManagerKlijent;
import funkcije.ManagerKozmeticar;
import funkcije.ManagerRecepcioner;
import funkcije.ManagerZakazanTretman;
import funkcije.SwingFunkcije;

import javax.swing.JButton;
import com.toedter.calendar.JDateChooser;
import java.beans.PropertyChangeListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.beans.PropertyChangeEvent;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.border.MatteBorder;
import java.awt.Color;

public class RecepcionerFrame extends JFrame {


	private static final long serialVersionUID = -6181263101146506697L;
	private JPanel contentPane;
	private JTable table;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RecepcionerFrame frame = new RecepcionerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public RecepcionerFrame() {
		setTitle("PhiAcademy - Recepcioner");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1171, 823);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		int KorisnikID = Funkcionalnosti.ucitajID("data/id.csv");
		
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 85, 1137, 701);
		contentPane.add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Zakazi tretman", null, panel, null);
		panel.setLayout(null);
		
		JLabel tretmanLbl = new JLabel("Tretman:");
		tretmanLbl.setHorizontalAlignment(SwingConstants.CENTER);
		tretmanLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
		tretmanLbl.setBounds(294, 68, 343, 20);
		panel.add(tretmanLbl);
		
		JLabel kozmeticarLbl = new JLabel("Kozmeticar:");
		kozmeticarLbl.setHorizontalAlignment(SwingConstants.CENTER);
		kozmeticarLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
		kozmeticarLbl.setBounds(690, 68, 165, 20);
		panel.add(kozmeticarLbl);
		
		JLabel terminLbl = new JLabel("Termin:");
		terminLbl.setHorizontalAlignment(SwingConstants.CENTER);
		terminLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
		terminLbl.setBounds(899, 228, 137, 20);
		panel.add(terminLbl);
		
		JButton zakaziRecepcioner = new JButton("ZAKAZI");
		zakaziRecepcioner.setFont(new Font("Tahoma", Font.PLAIN, 18));
		zakaziRecepcioner.setBounds(64, 408, 791, 53);
		panel.add(zakaziRecepcioner);
		
		JLabel datumLbl = new JLabel("Datum:");
		datumLbl.setHorizontalAlignment(SwingConstants.CENTER);
		datumLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
		datumLbl.setBounds(899, 68, 137, 20);
		panel.add(datumLbl);
		
		
		JDateChooser dateChooserZakazi = new JDateChooser();
		dateChooserZakazi.setBounds(899, 111, 137, 31);
		panel.add(dateChooserZakazi);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(294, 111, 343, 286);
		panel.add(scrollPane);
		
		JList<String> listTretman = new JList<String>();
		scrollPane.setViewportView(listTretman);
		SwingFunkcije.populateJListUsluge(listTretman, uslugaTretman.sveusluge);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(690, 111, 165, 286);
		panel.add(scrollPane_1);
		
		JList<String> listKozmeticar = new JList<String>();
		scrollPane_1.setViewportView(listKozmeticar);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(899, 259, 137, 202);
		panel.add(scrollPane_2);
		
		JList<LocalTime> listTermin = new JList<LocalTime>();
		scrollPane_2.setViewportView(listTermin);
		
		JComboBox<String> comboBoxKlijent = new JComboBox<String>();
		comboBoxKlijent.setBounds(899, 186, 137, 31);
		SwingFunkcije.populateComboBoxModelKlijent(comboBoxKlijent, ManagerKlijent.sviklijenti);
		panel.add(comboBoxKlijent);
		
		JLabel KlijentLbl = new JLabel("Klijent:");
		KlijentLbl.setHorizontalAlignment(SwingConstants.CENTER);
		KlijentLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
		KlijentLbl.setBounds(899, 155, 137, 20);
		panel.add(KlijentLbl);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel_2.setBounds(64, 111, 189, 286);
		panel.add(panel_2);
		
		JComboBox<String> uslugaFilter = new JComboBox<String>();
		uslugaFilter.setBounds(10, 42, 169, 22);
		panel_2.add(uslugaFilter);
		
		SwingFunkcije.populateComboBoxUslugaFilter(uslugaFilter, uslugaTretman.sveusluge);
		
		JComboBox<String> tipFilter = new JComboBox<String>();
		tipFilter.setBounds(10, 106, 169, 22);
		panel_2.add(tipFilter);
		
		SwingFunkcije.populateComboBoxTipFilter(tipFilter, tipTretmana.svitipovi);
		
		JComboBox<String> cenaFilter = new JComboBox<String>();
		cenaFilter.setBounds(10, 170, 169, 22);
		panel_2.add(cenaFilter);
		
		SwingFunkcije.populateComboBoxCenaFilter(cenaFilter, uslugaTretman.sveusluge);
		
		JLabel uslugaLbl = new JLabel("Usluga:");
		uslugaLbl.setHorizontalAlignment(SwingConstants.CENTER);
		uslugaLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
		uslugaLbl.setBounds(10, 11, 169, 20);
		panel_2.add(uslugaLbl);
		
		JLabel tipLbl = new JLabel("Tip:");
		tipLbl.setHorizontalAlignment(SwingConstants.CENTER);
		tipLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
		tipLbl.setBounds(10, 75, 169, 20);
		panel_2.add(tipLbl);
		
		JLabel cenaLbl = new JLabel("Cena:");
		cenaLbl.setHorizontalAlignment(SwingConstants.CENTER);
		cenaLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
		cenaLbl.setBounds(10, 139, 169, 20);
		panel_2.add(cenaLbl);
		
		JButton ocistiFiltere = new JButton("OCISTI FILTERE");
		ocistiFiltere.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		ocistiFiltere.setBounds(10, 237, 169, 38);
		panel_2.add(ocistiFiltere);
		
		JLabel lblNewLabel_1_1_4_1_2 = new JLabel("Filteri:");
		lblNewLabel_1_1_4_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_4_1_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1_4_1_2.setBounds(64, 68, 189, 20);
		panel.add(lblNewLabel_1_1_4_1_2);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Zakazani tretmani", null, panel_1, null);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(10, 11, 754, 647);
		panel_1.add(scrollPane_3);
		
		table = new JTable(SwingFunkcije.tableModelZakazaniTretmani());
		scrollPane_3.setViewportView(table);
		
		
		ListSelectionModel selectionModel = table.getSelectionModel();
		
		JComboBox<String> comboBox = new JComboBox<String>();
		
		
		SwingFunkcije.populateComboBoxModelUsluga(comboBox, uslugaTretman.sveusluge);
		comboBox.setBounds(850, 57, 209, 32);
		panel_1.add(comboBox);
		
		JLabel lblUsluga = new JLabel("Usluga");
		lblUsluga.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsluga.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblUsluga.setBounds(850, 11, 209, 35);
		panel_1.add(lblUsluga);
		
		JComboBox<String> comboBox_1 = new JComboBox<String>();

		
		comboBox_1.setBounds(850, 146, 209, 32);
		comboBox_1.setEnabled(false);
		panel_1.add(comboBox_1);
		
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboBox_1.setEnabled(true );
				if(comboBox.getSelectedIndex() == -1) {

					SwingFunkcije.populateComboBoxModelKozmeticar(comboBox_1, ManagerKozmeticar.svikozmeticari);

				}
				else {
					SwingFunkcije.populateComboBoxModelKozmeticar(comboBox_1, ManagerKozmeticar.kvalifikovaniKozmericari(uslugaTretman.sveusluge.get(comboBox.getSelectedIndex())));
				}
			}
		});
		
		JLabel lblKozmeticar = new JLabel("Kozmeticar");
		lblKozmeticar.setHorizontalAlignment(SwingConstants.CENTER);
		lblKozmeticar.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblKozmeticar.setBounds(850, 100, 209, 35);
		panel_1.add(lblKozmeticar);
		
		JComboBox<LocalTime> comboBox_termin = new JComboBox<LocalTime>();
		comboBox_termin.setBounds(850, 324, 209, 32);
		panel_1.add(comboBox_termin);
		
		JLabel lblDan = new JLabel("Dan");
		lblDan.setHorizontalAlignment(SwingConstants.CENTER);
		lblDan.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblDan.setBounds(850, 189, 209, 35);
		panel_1.add(lblDan);
		
		
		JLabel lblTermin = new JLabel("Termin");
		lblTermin.setHorizontalAlignment(SwingConstants.CENTER);
		lblTermin.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTermin.setBounds(850, 278, 209, 35);
		panel_1.add(lblTermin);
		
		
		JComboBox<Object> comboBox_3 = new JComboBox<Object>();
		comboBox_3.setModel(new DefaultComboBoxModel<Object>(Stanje.values()));
		comboBox_3.setBounds(850, 412, 209, 32);
		panel_1.add(comboBox_3);
		
		JLabel lblStanje = new JLabel("Stanje");
		lblStanje.setHorizontalAlignment(SwingConstants.CENTER);
		lblStanje.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblStanje.setBounds(850, 367, 209, 35);
		panel_1.add(lblStanje);
		
		JComboBox<String> comboBox_4 = new JComboBox<String>();
		SwingFunkcije.populateComboBoxModelKlijent(comboBox_4, ManagerKlijent.sviklijenti);
		comboBox_4.setBounds(850, 511, 209, 32);
		panel_1.add(comboBox_4);
		
		
		
		
		
		JLabel lblKlijent = new JLabel("Klijent");
		lblKlijent.setHorizontalAlignment(SwingConstants.CENTER);
		lblKlijent.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblKlijent.setBounds(850, 465, 209, 35);
		panel_1.add(lblKlijent);
		
		JButton btnNewButton = new JButton("IZMENI");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		btnNewButton.setBounds(850, 583, 209, 32);
		panel_1.add(btnNewButton);
		
		JButton btnOtkazi = new JButton("OTKAZI");
		btnOtkazi.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnOtkazi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selektovanRed = table.getSelectedRow();
				if(selektovanRed != -1) {
					ManagerRecepcioner.otkaziTretman(ManagerZakazanTretman.svizakazani.get(selektovanRed));
					table.setValueAt(Stanje.OTKAZAO_SALON, selektovanRed, 3);
				}
				else {
					JOptionPane.showMessageDialog(null, "Morate selektovati tretman koji zelite da otkazete");
				}
				
			}
		});
		btnOtkazi.setBounds(850, 626, 209, 32);
		panel_1.add(btnOtkazi);
		
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				Kozmeticar k = null;
				if(dateChooser.getDate() != null) {
				LocalDateTime datum1 = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
				LocalDate d = datum1.toLocalDate();
				
				if(comboBox_1.getSelectedIndex() != -1) {
					k = ManagerKozmeticar.svikozmeticari.get(comboBox_1.getSelectedIndex());
				
				if (evt.getNewValue() instanceof Date) {

				ArrayList<LocalDateTime> slobodni = ManagerKozmeticar.nadjiSlobodneTermine(k, d, Main.salon.getPocetakradnogvremena(), Main.salon.getKrajradnogvremena());
				
				SwingFunkcije.populateComboBoxModelVreme1(comboBox_termin);
							
				}

				}
				//ArrayList<LocalDateTime> slobodni = ManagerKozmeticar.nadjiSlobodneTermine(k, datum, Main.salon.getPocetakradnogvremena(), Main.salon.getKrajradnogvremena());
		//OVDE STADOH		SwingFunkcije.populateComboBoxModelVreme(comboBox_termin, slobodni);
				}
			}
		});
		dateChooser.setBounds(850, 235, 209, 32);
		panel_1.add(dateChooser);
		
		JLabel lblNewLabel = new JLabel("Pozdrav, " + ManagerRecepcioner.nadjiRecepcioneraPoID(KorisnikID).getIme() + "!");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(10, 11, 974, 33);
		contentPane.add(lblNewLabel);
		
		JButton odjava = new JButton("Odjavi se");
		odjava.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Funkcionalnosti.openFrame("Start");
				dispose();
			}
		});
		odjava.setFont(new Font("Tahoma", Font.PLAIN, 18));
		odjava.setBounds(1022, 12, 125, 33);
		contentPane.add(odjava);
		selectionModel.addListSelectionListener(new ListSelectionListener() {
		    @Override
		    public void valueChanged(ListSelectionEvent e) {
		        if (!e.getValueIsAdjusting()) {
		            int selektovanRed = table.getSelectedRow();
		            int kolonaUsluga = 0;
		            int kolonaKozmeticara = 1;
		            int kolonaDatuma = 2;

		            if (selektovanRed != -1) {

		                uslugaTretman usluga = uslugaTretman.uslugaPoImenu(table.getValueAt(selektovanRed, kolonaUsluga).toString());
		                Kozmeticar koz = ManagerKozmeticar.nadjiKozmeticaraPoKorisnickomImenu(table.getValueAt(selektovanRed, kolonaKozmeticara).toString());
		                String pattern = "yyyy-MM-dd'T'HH:mm";
		                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

		                LocalDateTime localDateTime = LocalDateTime.parse(table.getValueAt(selektovanRed, kolonaDatuma).toString(), formatter);
		                LocalDate localDate = localDateTime.toLocalDate();
		                LocalTime localTime = localDateTime.toLocalTime();
		                Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());


		                if (usluga != null) {

		                    comboBox.setSelectedItem(usluga.getImeUsluge());
		                    comboBox_1.setSelectedItem(koz.getKorisnickoIme());
		                    dateChooser.setDate(date);
		                    comboBox_termin.setSelectedItem(localTime);
		                    comboBox_3.setSelectedItem(Stanje.valueOf(table.getValueAt(selektovanRed, 3).toString()));
		                    comboBox_4.setSelectedItem(ManagerKlijent.nadjiKlijentapoKorisnickomImenu(table.getValueAt(selektovanRed, 4).toString()).getKorisnickoIme());
		                    
		                    
		                   
		                }
		            }
		        }
		    }
		});
		
		//IZMENI
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if(table.getSelectedRow() == -1) {
					btnNewButton.setEnabled(false);
				}
				else {
					btnNewButton.setEnabled(true);
					//PROVERI KADA SE PROMENI USLUGA PUCA
					int selektovanRed = table.getSelectedRow();
					zakazanTretman zt = ManagerZakazanTretman.svizakazani.get(selektovanRed);
					Instant instant = dateChooser.getDate().toInstant();
					LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
					LocalDateTime termin = localDateTime.with(ManagerZakazanTretman.generisiTermine(Main.salon.getPocetakradnogvremena(), Main.salon.getKrajradnogvremena()).get(comboBox_termin.getSelectedIndex()));
					zt.setUslugu(uslugaTretman.uslugaPoImenu(comboBox.getSelectedItem().toString()));
					zt.setIzabraniKozmeticar(ManagerKozmeticar.nadjiKozmeticaraPoKorisnickomImenu(comboBox_1.getSelectedItem().toString()));
					zt.setTermin(termin);
					zt.setStanjeTretmana(Stanje.valueOf(comboBox_3.getSelectedItem().toString()));
					zt.setKlijent(ManagerKlijent.nadjiKlijentapoKorisnickomImenu(comboBox_4.getSelectedItem().toString()));
					zt.setNaplaceno(zt.getNaplaceno());
					ManagerZakazanTretman.azurirajTretman(zt, selektovanRed);
					table.setValueAt(comboBox.getSelectedItem().toString(), selektovanRed, 0);
					table.setValueAt(comboBox_1.getSelectedItem().toString(), selektovanRed, 1);
					table.setValueAt(termin.toString(), selektovanRed, 2);
					table.setValueAt(comboBox_3.getSelectedItem().toString(), selektovanRed, 3);
					table.setValueAt(comboBox_4.getSelectedItem().toString(), selektovanRed, 4);
					
					
					
				}
			}
		});
		
		listTretman.addListSelectionListener(new ListSelectionListener() {
	    	public void valueChanged(ListSelectionEvent e) {
	    		int selektovano = listTretman.getSelectedIndex();
	    		if(selektovano != -1) {
	    			
	    		
	    		uslugaTretman usluga = uslugaTretman.sveusluge.get(selektovano);
	    		ArrayList<Kozmeticar> kozmeticari = ManagerKozmeticar.kvalifikovaniKozmericari(usluga);
	    		SwingFunkcije.populateJListKozmeticar(listKozmeticar, kozmeticari);
	    		
	    		
	    		dateChooserZakazi.addPropertyChangeListener(new PropertyChangeListener() {
	    	    	public void propertyChange(PropertyChangeEvent evt) {
	    	    		
	    	    		if(listKozmeticar.getSelectedIndex() != -1) {
	    	    			
	    	    			Kozmeticar selektovanKozmeticar = ManagerKozmeticar.nadjiKozmeticaraPoKorisnickomImenu(listKozmeticar.getSelectedValue().toString());
		    	    		if (evt.getNewValue() instanceof Date) {
		    	    			
		                        Date selektovanDatum = (Date) evt.getNewValue();
		                        LocalDate selektovanDatumLocal = selektovanDatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		                        ArrayList<LocalDateTime> slobodniterminizadan = ManagerKozmeticar.nadjiSlobodneTermine(selektovanKozmeticar, selektovanDatumLocal, Main.salon.getPocetakradnogvremena(), Main.salon.getKrajradnogvremena());
		                        SwingFunkcije.populateJListTermini(listTermin, slobodniterminizadan);
		    	    			}
	    	    			
	    	    		}
	    	    		else {
	    	    			
	    	    			if (evt.getNewValue() instanceof Date) {
		                        //Date selektovanDatum = (Date) evt.getNewValue(); 
		                        //LocalDate selektovanDatumLocal = selektovanDatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		                        SwingFunkcije.populateJListTerminiBezKozmeticara(listTermin, ManagerZakazanTretman.generisiTermine(Main.salon.getPocetakradnogvremena(), Main.salon.getKrajradnogvremena()));
		                        
		    	    		}
	    	    			
	    	    		}
	    	    		
	    	    	}
	    	    }); //dataChooser
	    		
	    		}
	    	}

	    });
		
		
		
		
		cenaFilter.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if(tipFilter.getSelectedIndex() == 0 && uslugaFilter.getSelectedIndex() == 0 && cenaFilter.getSelectedIndex() == 0) {
	    			SwingFunkcije.populateJListUsluge(listTretman, uslugaTretman.sveusluge);
	    			listKozmeticar.setModel(new DefaultListModel<>());
	    		}
	    		else {
	    			
	    			uslugaTretman usluga = null;
	    		    if (uslugaFilter.getSelectedIndex() != 0) {
	    		        usluga = uslugaTretman.uslugaPoImenu(uslugaFilter.getSelectedItem().toString());
	    		    }
	    		    
	    			String tip = null;
	    		    if (tipFilter.getSelectedIndex() != 0) {
	    		        tip = tipFilter.getSelectedItem().toString();
	    		    }

	    		    double cena = 0;
	    		    if (cenaFilter.getSelectedIndex() != 0) {
	    		    	cena = Double.parseDouble(cenaFilter.getSelectedItem().toString().split(" ")[0]);
	    		    }
	    		    
	    		    SwingFunkcije.populateJListUslugeFilterRecepcioner(listTretman, usluga, tip, cena);
	    		    listKozmeticar.setModel(new DefaultListModel<>());
	    		}
	    	}
	    });
	    
	    //TRAJANJE FILTER
	    tipFilter.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if(tipFilter.getSelectedIndex() == 0 && uslugaFilter.getSelectedIndex() == 0 && cenaFilter.getSelectedIndex() == 0) {
	    			SwingFunkcije.populateJListUsluge(listTretman, uslugaTretman.sveusluge);
	    			listKozmeticar.setModel(new DefaultListModel<>());
	    		}
	    		else {
	    			uslugaTretman usluga = null;
	    		    if (uslugaFilter.getSelectedIndex() != 0) {
	    		        usluga = uslugaTretman.uslugaPoImenu(uslugaFilter.getSelectedItem().toString());
	    		    }
	    		    
	    			String tip = null;
	    		    if (tipFilter.getSelectedIndex() != 0) {
	    		        tip = tipFilter.getSelectedItem().toString();
	    		    }

	    		    double cena = 0;
	    		    if (cenaFilter.getSelectedIndex() != 0) {
	    		    	cena = Double.parseDouble(cenaFilter.getSelectedItem().toString().split(" ")[0]);
	    		    }
	    		    
	    		    SwingFunkcije.populateJListUslugeFilterRecepcioner(listTretman, usluga, tip, cena);
	    		    listKozmeticar.setModel(new DefaultListModel<>());
	    		}
	    	}
	    });
	    //TIP FILTER
	    uslugaFilter.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if(tipFilter.getSelectedIndex() == 0 && uslugaFilter.getSelectedIndex() == 0 && cenaFilter.getSelectedIndex() == 0) {
	    			SwingFunkcije.populateJListUsluge(listTretman, uslugaTretman.sveusluge);
	    			listKozmeticar.setModel(new DefaultListModel<>());
	    		}
	    		else {
	    			uslugaTretman usluga = null;
	    		    if (uslugaFilter.getSelectedIndex() != 0) {
	    		        usluga = uslugaTretman.uslugaPoImenu(uslugaFilter.getSelectedItem().toString());
	    		    }
	    		    
	    			String tip = null;
	    		    if (tipFilter.getSelectedIndex() != 0) {
	    		        tip = tipFilter.getSelectedItem().toString();
	    		    }

	    		    double cena = 0;
	    		    if (cenaFilter.getSelectedIndex() != 0) {
	    		    	cena = Double.parseDouble(cenaFilter.getSelectedItem().toString().split(" ")[0]);
	    		    }
	    		    
	    		    SwingFunkcije.populateJListUslugeFilterRecepcioner(listTretman, usluga, tip, cena);
	    		    listKozmeticar.setModel(new DefaultListModel<>());
	    		}
	    	}
	    });
	    
	    
	    zakaziRecepcioner.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		int redniBroj = listTermin.getSelectedIndex();
	    		
	    		if(listKozmeticar.getSelectedIndex() != -1) {   //IZABRAN KOZMETICAR
	    		
	    		if(redniBroj != -1) {
		    		uslugaTretman usluga = uslugaTretman.sveusluge.get(listTretman.getSelectedIndex());
	    			Klijent k = ManagerKlijent.nadjiKlijentapoKorisnickomImenu(comboBoxKlijent.getSelectedItem().toString());
	    			Kozmeticar selektovanKozmeticar = ManagerKozmeticar.nadjiKozmeticaraPoKorisnickomImenu(listKozmeticar.getSelectedValue().toString());
	    			Date selektovanDatum = dateChooserZakazi.getDate();
                    LocalDate selektovanDatumLocal = selektovanDatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    ArrayList<LocalDateTime> slobodniterminizadan = ManagerKozmeticar.nadjiSlobodneTermine(selektovanKozmeticar, selektovanDatumLocal, Main.salon.getPocetakradnogvremena(), Main.salon.getKrajradnogvremena());
	    			LocalDateTime selektovanTermin = slobodniterminizadan.get(redniBroj);
    	    		
    	    		if(listTretman.getSelectedIndex() == -1 || listTermin.getSelectedIndex() == -1 || dateChooserZakazi.getDate() == null) {
    	    			JOptionPane.showMessageDialog(null, "Nisu izabrana sva polja.");
    	    		}
    	    		else {
    	    			zakazanTretman z = ManagerRecepcioner.zakaziTretman(usluga, selektovanKozmeticar, selektovanTermin, k);
    	    			ManagerZakazanTretman.svizakazani.add(z);
    	    			JOptionPane.showMessageDialog(null, "Uspesno zakazan tretman sa terminom u " + z.getTermin().toLocalTime() + " za dan " + z.getTermin().toLocalDate() + ".");
    	    			tipFilter.setSelectedIndex(0);
    		    		uslugaFilter.setSelectedIndex(0);
    		    		cenaFilter.setSelectedIndex(0);
    		    		listTretman.setSelectedIndex(-1);
    		    		listKozmeticar.setSelectedIndex(-1);
    		    		dateChooserZakazi.setDate(null);
    		    		listTermin.setModel(new DefaultListModel<LocalTime>());
    		    		comboBoxKlijent.setSelectedIndex(0);
    		    		table.setModel(SwingFunkcije.tableModelZakazaniTretmani());
    	    		}
    	    		
	    			
	    		}
	    		else {
	    			JOptionPane.showMessageDialog(null, "Nije izabran termin.");

	    		}
	    		

	    		}
	    		else {
	    			JOptionPane.showMessageDialog(null, "Kozmeticar nije izabran.");
	    		}
	    		
	    	}
	    });
	    
	    
	    ocistiFiltere.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tipFilter.setSelectedIndex(0);
	    		uslugaFilter.setSelectedIndex(0);
	    		cenaFilter.setSelectedIndex(0);
	    		SwingFunkcije.populateJListUsluge(listTretman, uslugaTretman.sveusluge);
			}
		});
		
		
		
	}
}
