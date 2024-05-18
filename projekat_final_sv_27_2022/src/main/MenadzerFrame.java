package main;

import java.awt.Color;

import java.awt.EventQueue;
import funkcije.*;
import entity.*;
import enums.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


import org.knowm.xchart.*;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.style.*;
import org.knowm.xchart.style.AxesChartStyler.TextAlignment;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.*;
import org.knowm.xchart.internal.chartpart.Chart;
import javax.swing.JScrollBar;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;

public class MenadzerFrame extends JFrame {


	private static final long serialVersionUID = 6346930582134358854L;
	private JPanel contentPane;
	private JTextField imeK;
	private JTextField prezimeK;
	private JTextField telefonK;
	private JTextField adresaK;
	private JTextField usernameK;
	private JTextField lozinkaK;
	private JTextField strucnaK;
	private JTextField stazK;
	private JTextField bonusK;
	private JTable tabelaKozmeticar;
	private JTextField imeM;
	private JTextField prezimeM;
	private JTextField telefonM;
	private JTextField adresaM;
	private JTextField usernameM;
	private JTextField lozinkaM;
	private JTextField strucnaM;
	private JTextField stazM;
	private JTextField bonusM;
	private JTable tabelaMenadzer;
	private JTextField imeR;
	private JTextField prezimeR;
	private JTextField telefonR;
	private JTextField adresaR;
	private JTextField usernameR;
	private JTextField lozinkaR;
	private JTextField strucnaR;
	private JTextField stazR;
	private JTextField bonusR;
	private JTable tabelaRecepcioner;
	private JTable tabelaKartica;
	private JTable tabelaIzvestaji;
	private JTextField plataR;
	private JTextField plataM;
	private JTextField plataK;
	private JTable cenovnik;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenadzerFrame frame = new MenadzerFrame();
					frame.setVisible(true);
					
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public MenadzerFrame() {
		setTitle("PhiAcademy - Menadzer");
		String pathSalon = "data/salon.csv";
		int KorisnikID = Funkcionalnosti.ucitajID("data/id.csv");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1165, 798);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 61, 1126, 689);
		contentPane.add(tabbedPane);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Pregled zaposlenih", null, panel_1, null);
		panel_1.setLayout(null);
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBounds(10, 11, 1101, 647);
		panel_1.add(tabbedPane_1);
		
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		tabbedPane_1.addTab("Recepcioner", null, panel_3, null);
		
		imeR = new JTextField();
		imeR.setColumns(10);
		imeR.setBounds(189, 37, 96, 20);
		panel_3.add(imeR);
		
		prezimeR = new JTextField();
		prezimeR.setColumns(10);
		prezimeR.setBounds(189, 68, 96, 20);
		panel_3.add(prezimeR);
		
		JLabel lblNewLabel_1_6_1 = new JLabel("Prezime:");
		lblNewLabel_1_6_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_6_1.setBounds(25, 68, 84, 20);
		panel_3.add(lblNewLabel_1_6_1);
		
		JLabel lblNewLabel_1_1_4_1 = new JLabel("Ime:");
		lblNewLabel_1_1_4_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1_4_1.setBounds(25, 37, 84, 20);
		panel_3.add(lblNewLabel_1_1_4_1);
		
		JComboBox<String> polR = new JComboBox<String>();
		polR.setModel(new DefaultComboBoxModel<String>(new String[] {"M", "Z"}));
		polR.setBounds(189, 105, 96, 22);
		panel_3.add(polR);
		
		JLabel lblNewLabel_1_2_1_1 = new JLabel("Pol:");
		lblNewLabel_1_2_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_2_1_1.setBounds(25, 103, 84, 20);
		panel_3.add(lblNewLabel_1_2_1_1);
		
		telefonR = new JTextField();
		telefonR.setColumns(10);
		telefonR.setBounds(189, 138, 96, 20);
		panel_3.add(telefonR);
		
		adresaR = new JTextField();
		adresaR.setColumns(10);
		adresaR.setBounds(189, 169, 96, 20);
		panel_3.add(adresaR);
		
		JLabel lblNewLabel_1_3_2_1 = new JLabel("Adresa:");
		lblNewLabel_1_3_2_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_3_2_1.setBounds(25, 169, 84, 20);
		panel_3.add(lblNewLabel_1_3_2_1);
		
		JLabel lblNewLabel_1_1_1_2_1 = new JLabel("Telefon:");
		lblNewLabel_1_1_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1_1_2_1.setBounds(25, 138, 84, 20);
		panel_3.add(lblNewLabel_1_1_1_2_1);
		
		usernameR = new JTextField();
		usernameR.setColumns(10);
		usernameR.setBounds(189, 200, 96, 20);
		panel_3.add(usernameR);
		
		lozinkaR = new JTextField();
		lozinkaR.setColumns(10);
		lozinkaR.setBounds(189, 231, 96, 20);
		panel_3.add(lozinkaR);
		
		JLabel lblNewLabel_1_3_1_1_1 = new JLabel("Lozinka:");
		lblNewLabel_1_3_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_3_1_1_1.setBounds(25, 231, 84, 20);
		panel_3.add(lblNewLabel_1_3_1_1_1);
		
		JLabel lblNewLabel_1_1_1_1_1_1 = new JLabel("Korisnicko ime:");
		lblNewLabel_1_1_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1_1_1_1_1.setBounds(25, 200, 121, 20);
		panel_3.add(lblNewLabel_1_1_1_1_1_1);
		
		strucnaR = new JTextField();
		strucnaR.setColumns(10);
		strucnaR.setBounds(189, 262, 96, 20);
		panel_3.add(strucnaR);
		
		stazR = new JTextField();
		stazR.setColumns(10);
		stazR.setBounds(189, 293, 96, 20);
		panel_3.add(stazR);
		
		JLabel lblNewLabel_1_4_1_1 = new JLabel("Staz:");
		lblNewLabel_1_4_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_4_1_1.setBounds(25, 293, 84, 20);
		panel_3.add(lblNewLabel_1_4_1_1);
		
		JLabel lblNewLabel_1_1_2_1_1 = new JLabel("Strucna sprema:");
		lblNewLabel_1_1_2_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1_2_1_1.setBounds(25, 262, 154, 20);
		panel_3.add(lblNewLabel_1_1_2_1_1);
		
		bonusR = new JTextField();
		bonusR.setColumns(10);
		bonusR.setBounds(189, 324, 96, 20);
		panel_3.add(bonusR);
		
		JLabel lblNewLabel_1_1_3_1_1 = new JLabel("Bonus:");
		lblNewLabel_1_1_3_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1_3_1_1.setBounds(25, 324, 142, 20);
		panel_3.add(lblNewLabel_1_1_3_1_1);
		
		JButton dodajR = new JButton("DODAJ RECEPCIONERA");
		
		dodajR.setBounds(25, 504, 260, 23);
		panel_3.add(dodajR);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(295, 37, 791, 558);
		panel_3.add(scrollPane_1);
		
		tabelaRecepcioner = new JTable(SwingFunkcije.tableModelRecepcioner());
		scrollPane_1.setViewportView(tabelaRecepcioner);
		

		
		ListSelectionModel selectionModel = tabelaRecepcioner.getSelectionModel();
		
		JButton obrisiR = new JButton("OBRISI RECEPCIONERA");
		
		obrisiR.setBounds(25, 538, 260, 23);
		panel_3.add(obrisiR);
		
		JButton azurirajR = new JButton("AZURIRAJ RECEPCIONERA");
		
		azurirajR.setBounds(25, 572, 260, 23);
		panel_3.add(azurirajR);
		
		plataR = new JTextField();
		plataR.setColumns(10);
		plataR.setBounds(189, 355, 96, 20);
		panel_3.add(plataR);
		
		JLabel lblNewLabel_1_1_3_1_1_1 = new JLabel("Pocetna plata:");
		lblNewLabel_1_1_3_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1_3_1_1_1.setBounds(25, 355, 142, 20);
		panel_3.add(lblNewLabel_1_1_3_1_1_1);
		
		JPanel panel_4 = new JPanel();
		panel_4.setLayout(null);
		tabbedPane_1.addTab("Menadzer", null, panel_4, null);
		
		imeM = new JTextField();
		imeM.setColumns(10);
		imeM.setBounds(189, 37, 96, 20);
		panel_4.add(imeM);
		
		prezimeM = new JTextField();
		prezimeM.setColumns(10);
		prezimeM.setBounds(189, 68, 96, 20);
		panel_4.add(prezimeM);
		
		JLabel lblNewLabel_1_6 = new JLabel("Prezime:");
		lblNewLabel_1_6.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_6.setBounds(25, 68, 84, 20);
		panel_4.add(lblNewLabel_1_6);
		
		JLabel lblNewLabel_1_1_4 = new JLabel("Ime:");
		lblNewLabel_1_1_4.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1_4.setBounds(25, 37, 84, 20);
		panel_4.add(lblNewLabel_1_1_4);
		
		JComboBox<String> polM = new JComboBox<String>();
		polM.setModel(new DefaultComboBoxModel<String>(new String[] {"M", "Z"}));
		polM.setBounds(189, 105, 96, 22);
		panel_4.add(polM);
		
		JLabel lblNewLabel_1_2_1 = new JLabel("Pol:");
		lblNewLabel_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_2_1.setBounds(25, 103, 84, 20);
		panel_4.add(lblNewLabel_1_2_1);
		
		telefonM = new JTextField();
		telefonM.setColumns(10);
		telefonM.setBounds(189, 138, 96, 20);
		panel_4.add(telefonM);
		
		adresaM = new JTextField();
		adresaM.setColumns(10);
		adresaM.setBounds(189, 169, 96, 20);
		panel_4.add(adresaM);
		
		JLabel lblNewLabel_1_3_2 = new JLabel("Adresa:");
		lblNewLabel_1_3_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_3_2.setBounds(25, 169, 84, 20);
		panel_4.add(lblNewLabel_1_3_2);
		
		JLabel lblNewLabel_1_1_1_2 = new JLabel("Telefon:");
		lblNewLabel_1_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1_1_2.setBounds(25, 138, 84, 20);
		panel_4.add(lblNewLabel_1_1_1_2);
		
		usernameM = new JTextField();
		usernameM.setColumns(10);
		usernameM.setBounds(189, 200, 96, 20);
		panel_4.add(usernameM);
		
		lozinkaM = new JTextField();
		lozinkaM.setColumns(10);
		lozinkaM.setBounds(189, 231, 96, 20);
		panel_4.add(lozinkaM);
		
		JLabel lblNewLabel_1_3_1_1 = new JLabel("Lozinka:");
		lblNewLabel_1_3_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_3_1_1.setBounds(25, 231, 84, 20);
		panel_4.add(lblNewLabel_1_3_1_1);
		
		JLabel lblNewLabel_1_1_1_1_1 = new JLabel("Korisnicko ime:");
		lblNewLabel_1_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1_1_1_1.setBounds(25, 200, 121, 20);
		panel_4.add(lblNewLabel_1_1_1_1_1);
		
		strucnaM = new JTextField();
		strucnaM.setColumns(10);
		strucnaM.setBounds(189, 262, 96, 20);
		panel_4.add(strucnaM);
		
		stazM = new JTextField();
		stazM.setColumns(10);
		stazM.setBounds(189, 293, 96, 20);
		panel_4.add(stazM);
		
		JLabel lblNewLabel_1_4_1 = new JLabel("Staz:");
		lblNewLabel_1_4_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_4_1.setBounds(25, 293, 84, 20);
		panel_4.add(lblNewLabel_1_4_1);
		
		JLabel lblNewLabel_1_1_2_1 = new JLabel("Strucna sprema:");
		lblNewLabel_1_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1_2_1.setBounds(25, 262, 154, 20);
		panel_4.add(lblNewLabel_1_1_2_1);
		
		bonusM = new JTextField();
		bonusM.setColumns(10);
		bonusM.setBounds(189, 324, 96, 20);
		panel_4.add(bonusM);
		
		JLabel lblNewLabel_1_1_3_1 = new JLabel("Bonus:");
		lblNewLabel_1_1_3_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1_3_1.setBounds(25, 324, 142, 20);
		panel_4.add(lblNewLabel_1_1_3_1);
		
		JButton dodajM = new JButton("DODAJ MENADZERA");
		
		dodajM.setBounds(25, 504, 260, 23);
		panel_4.add(dodajM);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(295, 37, 791, 558);
		panel_4.add(scrollPane_2);
		
		tabelaMenadzer = new JTable(SwingFunkcije.tableModelMenadzer());
		scrollPane_2.setViewportView(tabelaMenadzer);
		
		
		//SELEKTOVANJE LISTE
		ListSelectionModel selectionModelMenadzer = tabelaMenadzer.getSelectionModel();
		
		JButton obrisiM = new JButton("OBRISI MENADZERA");
		obrisiM.setBounds(25, 538, 260, 23);
		panel_4.add(obrisiM);
		
		JButton azurirajM = new JButton("AZURIRAJ MENADZERA");
		azurirajM.setBounds(25, 572, 260, 23);
		panel_4.add(azurirajM);
		
		plataM = new JTextField();
		plataM.setColumns(10);
		plataM.setBounds(189, 355, 96, 20);
		panel_4.add(plataM);
		
		JLabel lblNewLabel_1_1_3_1_2 = new JLabel("Pocetna plata:");
		lblNewLabel_1_1_3_1_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1_3_1_2.setBounds(25, 355, 142, 20);
		panel_4.add(lblNewLabel_1_1_3_1_2);
		
		JPanel panel_2 = new JPanel();
		tabbedPane_1.addTab("Kozmeticar", null, panel_2, null);
		panel_2.setLayout(null);
		
		imeK = new JTextField();
		imeK.setBounds(189, 37, 96, 20);
		panel_2.add(imeK);
		imeK.setColumns(10);
		
		prezimeK = new JTextField();
		prezimeK.setBounds(189, 68, 96, 20);
		panel_2.add(prezimeK);
		prezimeK.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Prezime:");
		lblNewLabel_1.setBounds(25, 68, 84, 20);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_2.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Ime:");
		lblNewLabel_1_1.setBounds(25, 37, 84, 20);
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_2.add(lblNewLabel_1_1);
		
		JComboBox<String> polK = new JComboBox<String>();
		polK.setBounds(189, 105, 96, 22);
		polK.setModel(new DefaultComboBoxModel<String>(new String[] {"M", "Z"}));
		panel_2.add(polK);
		
		JLabel lblNewLabel_1_2 = new JLabel("Pol:");
		lblNewLabel_1_2.setBounds(25, 103, 84, 20);
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_2.add(lblNewLabel_1_2);
		
		telefonK = new JTextField();
		telefonK.setBounds(189, 138, 96, 20);
		telefonK.setColumns(10);
		panel_2.add(telefonK);
		
		adresaK = new JTextField();
		adresaK.setBounds(189, 169, 96, 20);
		adresaK.setColumns(10);
		panel_2.add(adresaK);
		
		JLabel lblNewLabel_1_3 = new JLabel("Adresa:");
		lblNewLabel_1_3.setBounds(25, 169, 84, 20);
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_2.add(lblNewLabel_1_3);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Telefon:");
		lblNewLabel_1_1_1.setBounds(25, 138, 84, 20);
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_2.add(lblNewLabel_1_1_1);
		
		usernameK = new JTextField();
		usernameK.setBounds(189, 200, 96, 20);
		usernameK.setColumns(10);
		panel_2.add(usernameK);
		
		lozinkaK = new JTextField();
		lozinkaK.setBounds(189, 231, 96, 20);
		lozinkaK.setColumns(10);
		panel_2.add(lozinkaK);
		
		JLabel lblNewLabel_1_3_1 = new JLabel("Lozinka:");
		lblNewLabel_1_3_1.setBounds(25, 231, 84, 20);
		lblNewLabel_1_3_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_2.add(lblNewLabel_1_3_1);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("Korisnicko ime:");
		lblNewLabel_1_1_1_1.setBounds(25, 200, 121, 20);
		lblNewLabel_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_2.add(lblNewLabel_1_1_1_1);
		
		strucnaK = new JTextField();
		strucnaK.setBounds(189, 262, 96, 20);
		strucnaK.setColumns(10);
		panel_2.add(strucnaK);
		
		stazK = new JTextField();
		stazK.setBounds(189, 293, 96, 20);
		stazK.setColumns(10);
		panel_2.add(stazK);
		
		JLabel lblNewLabel_1_4 = new JLabel("Staz:");
		lblNewLabel_1_4.setBounds(25, 293, 84, 20);
		lblNewLabel_1_4.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_2.add(lblNewLabel_1_4);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("Strucna sprema:");
		lblNewLabel_1_1_2.setBounds(25, 262, 154, 20);
		lblNewLabel_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_2.add(lblNewLabel_1_1_2);
		
		bonusK = new JTextField();
		bonusK.setBounds(189, 324, 96, 20);
		bonusK.setColumns(10);
		panel_2.add(bonusK);
		
		JLabel lblNewLabel_1_5 = new JLabel("Obucen za:");
		lblNewLabel_1_5.setBounds(25, 432, 121, 20);
		lblNewLabel_1_5.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_2.add(lblNewLabel_1_5);
		
		JLabel lblNewLabel_1_1_3 = new JLabel("Bonus:");
		lblNewLabel_1_1_3.setBounds(25, 324, 142, 20);
		lblNewLabel_1_1_3.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_2.add(lblNewLabel_1_1_3);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(189, 386, 96, 107);
		panel_2.add(scrollPane);
		
		JList<tipTretmana> listaTipovi = new JList<>();
		scrollPane.setViewportView(listaTipovi);
		SwingFunkcije.populateJListTipovi(listaTipovi, tipTretmana.svitipovi);
		
		JButton dodajK = new JButton("DODAJ KOZMETICARA");
		dodajK.setBounds(25, 504, 260, 23);
		panel_2.add(dodajK);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(295, 37, 791, 558);
		panel_2.add(scrollPane_3);
		

		tabelaKozmeticar = new JTable(SwingFunkcije.tableModelKozmeticar());
		scrollPane_3.setViewportView(tabelaKozmeticar);

		
		//SELEKTOVAN KOZMETICAR
		ListSelectionModel selectionModelKozmeticar = tabelaKozmeticar.getSelectionModel();
		
		JButton obrisiK = new JButton("OBRISI KOZMETICARA");
		obrisiK.setBounds(25, 538, 260, 23);
		panel_2.add(obrisiK);
		
		JButton azurirajK = new JButton("AZURIRAJ KOZMETICARA");
		azurirajK.setBounds(25, 572, 260, 23);
		panel_2.add(azurirajK);
		
		plataK = new JTextField();
		plataK.setColumns(10);
		plataK.setBounds(189, 355, 96, 20);
		panel_2.add(plataK);
		
		JLabel lblNewLabel_1_1_3_1_3 = new JLabel("Pocetna plata:");
		lblNewLabel_1_1_3_1_3.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1_3_1_3.setBounds(25, 355, 142, 20);
		panel_2.add(lblNewLabel_1_1_3_1_3);
		
		JPanel panel_5 = new JPanel();
		tabbedPane.addTab("Prihodi i rashodi", null, panel_5, null);
		panel_5.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("Od:");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_2.setBounds(396, 53, 202, 30);
		panel_5.add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("Do:");
		lblNewLabel_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_2_1.setBounds(396, 146, 202, 30);
		panel_5.add(lblNewLabel_2_1);
		
		JButton btnNewButton_2 = new JButton("Prihodi");
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		btnNewButton_2.setBounds(396, 310, 202, 40);
		panel_5.add(btnNewButton_2);
		
		JButton btnNewButton_2_1 = new JButton("Rashodi");
		btnNewButton_2_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		btnNewButton_2_1.setBounds(396, 434, 202, 40);
		panel_5.add(btnNewButton_2_1);
		
		JLabel prihodiLabel = new JLabel("Prihodi za izabrani period:");
		prihodiLabel.setForeground(new Color(255, 128, 0));
		prihodiLabel.setHorizontalAlignment(SwingConstants.CENTER);
		prihodiLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		prihodiLabel.setBounds(10, 361, 978, 23);
		panel_5.add(prihodiLabel);
		
		
		JLabel rashodiLabel = new JLabel("Rashodi za izabrani period:");
		rashodiLabel.setForeground(new Color(255, 128, 0));
		rashodiLabel.setHorizontalAlignment(SwingConstants.CENTER);
		rashodiLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		rashodiLabel.setBounds(10, 485, 978, 23);
		panel_5.add(rashodiLabel);
		
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setBounds(396, 93, 202, 30);
		panel_5.add(dateChooser);
		
		JDateChooser dateChooser_1 = new JDateChooser();
		dateChooser_1.setBounds(396, 187, 202, 30);
		panel_5.add(dateChooser_1);
		
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(dateChooser.getDate() != null && dateChooser_1.getDate() != null) {
					LocalDateTime pocetak = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
					LocalDateTime kraj = dateChooser_1.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
					if(pocetak.isAfter(kraj)) {
						JOptionPane.showMessageDialog(null, "Pocetak ne sme biti posle kraja.");
					}
					else {
						prihodiLabel.setText("Prihodi za izabrani period: " + ManagerZakazanTretman.prihodiZaOdredjeniPeriod(pocetak, kraj) + " RSD");
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Morate pupuniti sva polja!");
				}
				
				
			}
		});
		
		btnNewButton_2_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LocalDateTime pocetak = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
				LocalDateTime kraj = dateChooser_1.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
				if(pocetak.isAfter(kraj)) {
					JOptionPane.showMessageDialog(null, "Pocetak ne sme biti posle kraja.");
				}
				else {
					rashodiLabel.setText("Rashodi za izabrani period: " + ManagerZakazanTretman.rashodiZaOdredjeniPeriod(pocetak, kraj) + " RSD");
				}
				
			}
		});
		
		JPanel panel_6 = new JPanel();
		tabbedPane.addTab("Kartica lojalnosti", null, panel_6, null);
		panel_6.setLayout(null);
		
		JLabel lblNewLabel_4 = new JLabel("Postavi iznos za \r\nkarticu lojalnosti:");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_4.setBounds(10, 125, 300, 36);
		panel_6.add(lblNewLabel_4);
		
		ArrayList<Klijent> svi = new ArrayList<>();
		for(Klijent k : ManagerKlijent.sviklijenti) {
			ManagerKlijent.proveriUslovZaKL(k);
			if(k.getKl().equals(KarticaLojalnosti.IMA)) {
				svi.add(k);
			}
		}
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(10, 219, 978, 454);
		panel_6.add(scrollPane_4);
		
		tabelaKartica = new JTable();
		scrollPane_4.setViewportView(tabelaKartica);
		
		tabelaKartica.setModel(SwingFunkcije.tableModelUslovZaKL(svi));
		
		JButton postaviKL = new JButton("POSTAVI");
		
		postaviKL.setBounds(692, 125, 282, 36);
		panel_6.add(postaviKL);
		
		JSlider slider = new JSlider();
		
		slider.setSnapToTicks(true);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setMaximum(50000);
		slider.setMinimum(1000);
		slider.setBounds(340, 130, 282, 31);
		panel_6.add(slider);
		
		JLabel vrednostSlider = new JLabel("");
		vrednostSlider.setFont(new Font("Tahoma", Font.BOLD, 20));
		vrednostSlider.setHorizontalAlignment(SwingConstants.CENTER);
		vrednostSlider.setBounds(340, 160, 282, 23);
		panel_6.add(vrednostSlider);
		
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				vrednostSlider.setText("" + slider.getValue() + "");
			}
		});
		
		JPanel panel_7 = new JPanel();
		tabbedPane.addTab("Izvestaji", null, panel_7, null);
		panel_7.setLayout(null);
		
		JDateChooser datumOd = new JDateChooser();
		datumOd.setBounds(114, 42, 112, 31);
		panel_7.add(datumOd);
		
		JDateChooser datumDo = new JDateChooser();
		datumDo.setBounds(114, 104, 112, 31);
		panel_7.add(datumDo);
		
		JButton izvrseniKozmeticar = new JButton("IZVRSENI TRETMANI KOZMETICAR");
		izvrseniKozmeticar.setFont(new Font("Tahoma", Font.BOLD, 11));
		izvrseniKozmeticar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				if(datumOd.getDate() == null || datumDo.getDate() == null) {
					JOptionPane.showMessageDialog(null, "Nisu izabrana sva polja.");
				}
				else if(datumOd.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(datumDo.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())){
					JOptionPane.showMessageDialog(null, "Pocetak ne sme biti posle kraja.");
				}
				else {
					LocalDate pocetak = datumOd.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					LocalDate kraj = datumDo.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					tabelaIzvestaji.setModel(SwingFunkcije.tableModelIzvestajiKozmeticari(pocetak, kraj));
				}
			}
		});
		izvrseniKozmeticar.setBounds(10, 195, 260, 40);
		panel_7.add(izvrseniKozmeticar);
		
		JButton potvrdjeniOtkazani = new JButton("POTVRDJENI OTKAZANI");
		potvrdjeniOtkazani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if(datumOd.getDate() == null || datumDo.getDate() == null) {
					JOptionPane.showMessageDialog(null, "Nisu izabrana sva polja.");
				}
				else if(datumOd.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(datumDo.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())){
					JOptionPane.showMessageDialog(null, "Pocetak ne sme biti posle kraja.");
				}
				else {
					LocalDate pocetak = datumOd.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					LocalDate kraj = datumDo.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					tabelaIzvestaji.setModel(SwingFunkcije.tableModelIzvestajiZakazaniOtkazani(pocetak, kraj));
				}
			}
		});
		potvrdjeniOtkazani.setBounds(290, 195, 260, 40);
		panel_7.add(potvrdjeniOtkazani);
		
		JButton uslugeSve = new JButton("SVE USLUGE");
		uslugeSve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if(datumOd.getDate() == null || datumDo.getDate() == null) {
					JOptionPane.showMessageDialog(null, "Nisu izabrana sva polja.");
				}
				
				else if(datumOd.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(datumDo.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())){
					JOptionPane.showMessageDialog(null, "Pocetak ne sme biti posle kraja.");
				}
				else {
					LocalDate pocetak = datumOd.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					LocalDate kraj = datumDo.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					tabelaIzvestaji.setModel(SwingFunkcije.tableModelIzvestajiUsluga(pocetak, kraj));
				}
			}
		});
		uslugeSve.setBounds(570, 195, 260, 40);
		panel_7.add(uslugeSve);
		
		JScrollPane scrollPane_5 = new JScrollPane();
		scrollPane_5.setBounds(10, 275, 1101, 375);
		panel_7.add(scrollPane_5);
		tabelaIzvestaji = new JTable();
		scrollPane_5.setViewportView(tabelaIzvestaji);
		
		JButton UslovZaKarticu = new JButton("USLOV ZA KARTICU LOJALNOSTI");
		UslovZaKarticu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Klijent> svi = new ArrayList<>();
				for(Klijent k : ManagerKlijent.sviklijenti) {
					ManagerKlijent.proveriUslovZaKL(k);
					if(k.getKl().equals(KarticaLojalnosti.IMA)) {
						svi.add(k);
					}
				}
				
				tabelaIzvestaji.setModel(SwingFunkcije.tableModelUslovZaKL(svi));
				
			}
		});
		UslovZaKarticu.setBounds(851, 195, 260, 40);
		panel_7.add(UslovZaKarticu);
		
		JLabel lblNewLabel = new JLabel("Od:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel.setBounds(10, 42, 94, 31);
		panel_7.add(lblNewLabel);
		
		JLabel lblDo = new JLabel("Do:");
		lblDo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblDo.setBounds(10, 104, 94, 31);
		panel_7.add(lblDo);
		
		JLabel lblNewLabel_5 = new JLabel("Broj potrebnih tretmana za bonus:");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_5.setBounds(689, 42, 300, 31);
		panel_7.add(lblNewLabel_5);
		
		JScrollBar scrollBar = new JScrollBar();
		
		
		
		scrollBar.setOrientation(JScrollBar.HORIZONTAL);
		scrollBar.setMaximum(30);
		scrollBar.setBounds(999, 42, 112, 31);
		panel_7.add(scrollBar);
		
		JButton btnNewButton = new JButton("POSTAVI USLOV");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Funkcionalnosti.sacuvajBonus(scrollBar.getValue(),"data/bonus.csv");	
				JOptionPane.showMessageDialog(null, "Minimalan broj tretmana za bonus je postavljen na " + scrollBar.getValue());
				}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton.setBounds(689, 117, 422, 40);
		panel_7.add(btnNewButton);
		
		JLabel lblVrednost = new JLabel("");
		lblVrednost.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblVrednost.setHorizontalAlignment(SwingConstants.CENTER);
		lblVrednost.setBounds(999, 84, 112, 22);
		panel_7.add(lblVrednost);
		
		JLabel lblNewLabel_3 = new JLabel("Pozdrav, " + ManagerMenadzer.nadjiMenadzeraPoID(KorisnikID).getIme() + "!");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_3.setBounds(10, 11, 974, 33);
		contentPane.add(lblNewLabel_3);
		
		JButton odjava = new JButton("Odjavi se");
		odjava.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Funkcionalnosti.openFrame("Start");
				dispose();
			}
		});
		odjava.setFont(new Font("Tahoma", Font.PLAIN, 18));
		odjava.setBounds(1011, 12, 125, 33);
		contentPane.add(odjava);
		
		
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		
		//CHART ZA STANJA TRETMANA
        JPanel stanjeTretmana = new JPanel();
        tabbedPane.addTab("Dijagram stanja tretmana", stanjeTretmana);


        PieChart chart = new PieChartBuilder().width(800).height(600).title("Status kozmetickih tretmana u predhodnih 30 dana").build();
        PieStyler styler = chart.getStyler();
        styler.setSeriesColors(new Color[] { Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.CYAN });

        chart.addSeries("OTKAZAO SALON", SwingFunkcije.chartUdeoStanja(Stanje.OTKAZAO_SALON));
        chart.addSeries("IZVRSEN", SwingFunkcije.chartUdeoStanja(Stanje.IZVRSEN));
        chart.addSeries("ZAKAZAN", SwingFunkcije.chartUdeoStanja(Stanje.ZAKAZAN));
        chart.addSeries("NIJE SE POJAVIO", SwingFunkcije.chartUdeoStanja(Stanje.KLIJENT_SE_NIJE_POJAVIO));
        chart.addSeries("OTKAZAO KLIJENT", SwingFunkcije.chartUdeoStanja(Stanje.OTKAZAO_KLIJENT));

        XChartPanel chartPanel = new XChartPanel(chart);

        stanjeTretmana.add(chartPanel);
        
        //CHART ZA KOZMETICARE
        JPanel efikasnostKozmeticara = new JPanel();
        tabbedPane.addTab("Dijagram kozmeticara", efikasnostKozmeticara);


        PieChart chart1 = new PieChartBuilder().width(800).height(600).title("Status kozmetickih tretmana u predhodnih 30 dana").build();
        PieStyler styler1 = chart1.getStyler();
        styler1.setSeriesColors(new Color[] { Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.CYAN });
        
        for(Kozmeticar k : ManagerKozmeticar.svikozmeticari) {
        	chart1.addSeries(k.getKorisnickoIme(), SwingFunkcije.chartUdeoKozmeticara(k));
        }


        XChartPanel chartPanel1 = new XChartPanel(chart1);

        efikasnostKozmeticara.add(chartPanel1);


        
        
        //CHART ZA PRIHODE
        JPanel prihodPoTipuPanel = new JPanel();
        tabbedPane.addTab("Prihod po tipu tretmana", prihodPoTipuPanel);

        XYChart chart2 = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Prihod po tipu tretmana")
                .xAxisTitle("Mesec")
                .yAxisTitle("Prihod")
                .theme(ChartTheme.Matlab)
                .build();

        chart2.getStyler().setPlotGridLinesVisible(false);
        chart2.getStyler().setXAxisLabelRotation(45);
        chart2.getStyler().setXAxisLabelAlignment(TextAlignment.Right);


        String[] meseci = {"Januar", "Februar", "Mart", "April", "Maj", "Jun", "Jul", "August", "Septembar", "Oktobar", "Novembar", "Decembar"};
        double[] meseciBroj = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

        for(tipTretmana tip : tipTretmana.svitipovi) {
        	XYSeries series = chart2.addSeries(tip.getImeTretmana(), meseciBroj, SwingFunkcije.prihodiChart(tip));
        	series.setLineStyle(SeriesLines.SOLID);
            series.setMarker(SeriesMarkers.CIRCLE);
        }
        XChartPanel chartPanel2 = new XChartPanel(chart2);
        prihodPoTipuPanel.add(chartPanel2);
        
        
        
        
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////        
        
        
        
		postaviKL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Klijent> svi = new ArrayList<>();
                int vrednost = slider.getValue();
				Main.salon.setVrednostZaKL(vrednost);
				Salon.sacuvajSalon(Main.salon, pathSalon);
				
				for(Klijent k : ManagerKlijent.sviklijenti) {
					if(ManagerKlijent.proveriUslovZaKarticu(k)) {
						svi.add(k);
					}
				}
				

				
				tabelaKartica.setModel(SwingFunkcije.tableModelUslovZaKL(svi));
				
				
			}
		});
		selectionModel.addListSelectionListener(new ListSelectionListener() {
		    @Override
		    public void valueChanged(ListSelectionEvent e) {
		        if (!e.getValueIsAdjusting()) {
		        	int selektovanRed = tabelaRecepcioner.getSelectedRow();
		            if (selektovanRed != -1) {
		                //int ID = Integer.parseInt(tabelaRecepcioner.getValueAt(selektovanRed, 0).toString());
		                String ime = tabelaRecepcioner.getValueAt(selektovanRed, 1).toString();
						String prezime = tabelaRecepcioner.getValueAt(selektovanRed, 2).toString();
						String pol = tabelaRecepcioner.getValueAt(selektovanRed, 3).toString();
						String telefon = tabelaRecepcioner.getValueAt(selektovanRed, 4).toString();
						String adresa = tabelaRecepcioner.getValueAt(selektovanRed, 5).toString();
						String username = tabelaRecepcioner.getValueAt(selektovanRed, 6).toString();
						String lozinka = tabelaRecepcioner.getValueAt(selektovanRed, 7).toString();
						String strucnaSprema = tabelaRecepcioner.getValueAt(selektovanRed, 8).toString();
						String staz = tabelaRecepcioner.getValueAt(selektovanRed, 9).toString();
						String bonus = tabelaRecepcioner.getValueAt(selektovanRed, 10).toString();
						String plata = tabelaRecepcioner.getValueAt(selektovanRed, 11).toString();
						
						imeR.setText(ime);
						prezimeR.setText(prezime);
						polR.setSelectedItem(pol);
						telefonR.setText(telefon);
						adresaR.setText(adresa);
						usernameR.setText(username);
						lozinkaR.setText(lozinka);
						strucnaR.setText(strucnaSprema);
						stazR.setText(staz);
						bonusR.setText(bonus);
						plataR.setText(plata);
		            }

		        }
		    }
		});
		
		
		//DODAJ RECEPCIONERA
		dodajR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(tabelaRecepcioner.getSelectedRow() == -1) {
					
					String rIme = imeR.getText();
					String rPrezime = prezimeR.getText();
					String rPol = polR.getSelectedItem().toString();
					String rTelefon = telefonR.getText();
					String rAdresa = adresaR.getText();
					String rUsername = usernameR.getText();
					String rLozinka = lozinkaR.getText();
					String rStrucnaSprema = strucnaR.getText();
					String rStaz = stazR.getText();
					String rBonus = bonusR.getText();
					String rPlata = plataR.getText();
					
					if (rIme.isEmpty() || rPrezime.isEmpty() || rPol.isEmpty() || rTelefon.isEmpty() || rAdresa.isEmpty() || rUsername.isEmpty() || rLozinka.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Nisu popunjena sva polja.");
					} 
					else if(!Funkcionalnosti.slobodanUsername(rUsername)){
						    JOptionPane.showMessageDialog(null, "Korisnicko ime je zauzeto.");
					}
					else if (!SwingFunkcije.isDouble(rStrucnaSprema) || !SwingFunkcije.isDouble(rStaz) || !SwingFunkcije.isDouble(rBonus) || !SwingFunkcije.isDouble(rPlata)) {
						JOptionPane.showMessageDialog(null, "Polja 'strucna sprema', 'staz', 'bonus', 'plata' moraju biti brojevi.");
					}
					else {
						Recepcioner r = new Recepcioner(Funkcionalnosti.generisiID(), rIme, rPrezime, rPol, rTelefon, rAdresa, rUsername, rLozinka, Double.parseDouble(rStrucnaSprema), Double.parseDouble(rStaz), Double.parseDouble(rBonus), Double.parseDouble(rPlata));
						ManagerRecepcioner.svirecepcioneri.add(r);
						Recepcioner.sacuvajRecepcionere(ManagerRecepcioner.svirecepcioneri, "data/recepcioneri.csv");
						tabelaRecepcioner.setModel(SwingFunkcije.tableModelRecepcioner());
						SwingFunkcije.isprazniPolja(imeR, prezimeR, polR, telefonR, adresaR, usernameR, lozinkaR, strucnaR, stazR, bonusR, plataR);
						JOptionPane.showMessageDialog(null, "Korisnik uspesno dodat.");
					}
					
				}
				else {
					JOptionPane.showMessageDialog(null, "Za dodavanje novog korisnika ne sme biti selektovan postojeci korisnik.");
				}
				
				
			}
		});
		
		//AZURIRAJ RECEPCIONERA
		azurirajR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tabelaRecepcioner.getSelectedRow() != -1) {
					
					String rIme = imeR.getText();
					String rPrezime = prezimeR.getText();
					String rPol = polR.getSelectedItem().toString();
					String rTelefon = telefonR.getText();
					String rAdresa = adresaR.getText();
					String rUsername = usernameR.getText();
					String rLozinka = lozinkaR.getText();
					String rStrucnaSprema = strucnaR.getText();
					String rStaz = stazR.getText();
					String rBonus = bonusR.getText();
					String rPlata = plataR.getText();
					
					if (rIme.isEmpty() || rPrezime.isEmpty() || rPol.isEmpty() || rTelefon.isEmpty() || rAdresa.isEmpty() || rUsername.isEmpty() || rLozinka.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Nisu popunjena sva polja.");
					} 
					else if (!SwingFunkcije.isDouble(rStrucnaSprema) || !SwingFunkcije.isDouble(rStaz) || !SwingFunkcije.isDouble(rBonus) || !SwingFunkcije.isDouble(rPlata)) {
						JOptionPane.showMessageDialog(null, "Polja 'strucna sprema', 'staz', 'bonus', 'plata' moraju biti brojevi.");
					}
					else {
						int ID = Integer.parseInt(tabelaRecepcioner.getValueAt(tabelaRecepcioner.getSelectedRow(), 0).toString());
						Recepcioner selektovan = ManagerRecepcioner.nadjiRecepcioneraPoID(ID);
						if(selektovan.getKorisnickoIme().equals(rUsername) || Funkcionalnosti.slobodanUsername(rUsername)) {
							
							selektovan.setIme(rIme);
							selektovan.setPrezime(rPrezime);
							selektovan.setPol(rPol);
							selektovan.setTelefon(rTelefon);
							selektovan.setAdresa(rAdresa);
							selektovan.setKorisnickoIme(rUsername);
							selektovan.setLozinka(rLozinka);
							selektovan.setStrucnaSprema(Double.parseDouble(rStrucnaSprema));
							selektovan.setStaz(Double.parseDouble(rStaz));
							selektovan.setBonus(Double.parseDouble(rBonus));
							selektovan.setPlata(Double.parseDouble(rPlata));
							ManagerRecepcioner.svirecepcioneri.set(tabelaRecepcioner.getSelectedRow(), selektovan);
							Recepcioner.sacuvajRecepcionere(ManagerRecepcioner.svirecepcioneri, "data/recepcioneri.csv");
							tabelaRecepcioner.setModel(SwingFunkcije.tableModelRecepcioner());
							SwingFunkcije.isprazniPolja(imeR, prezimeR, polR, telefonR, adresaR, usernameR, lozinkaR, strucnaR, stazR, bonusR, plataR);
							JOptionPane.showMessageDialog(null, "Korisnik uspesno azuriran.");
						}
						else {
							JOptionPane.showMessageDialog(null, "Korisnicko ime zauzeto.");
						}
						
						
						
					}
					
				}
				else {
					JOptionPane.showMessageDialog(null, "Niste izabrali korisnika za azuriranje.");
				}
			}
		});
		
		//OBRISI RECEPCIONERA
		obrisiR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(tabelaRecepcioner.getSelectedRow() != -1) {
					ManagerRecepcioner.svirecepcioneri.remove(tabelaRecepcioner.getSelectedRow());
					Recepcioner.sacuvajRecepcionere(ManagerRecepcioner.svirecepcioneri, "data/recepcioneri.csv");
					tabelaRecepcioner.setModel(SwingFunkcije.tableModelRecepcioner());
					SwingFunkcije.isprazniPolja(imeR, prezimeR, polR, telefonR, adresaR, usernameR, lozinkaR, strucnaR, stazR, bonusR, plataR);
					JOptionPane.showMessageDialog(null, "Korisnik uspesno obrisan.");
				}
				else {
					JOptionPane.showMessageDialog(null, "Niste izabrali korisnika za brisanje.");
				}
			}
		});
		selectionModelMenadzer.addListSelectionListener(new ListSelectionListener() {
		    @Override
		    public void valueChanged(ListSelectionEvent e) {
		        if (!e.getValueIsAdjusting()) {
		        	int selektovanRed = tabelaMenadzer.getSelectedRow();
		            if (selektovanRed != -1) {
		                //int ID = Integer.parseInt(tabelaMenadzer.getValueAt(selektovanRed, 0).toString());
		                String ime = tabelaMenadzer.getValueAt(selektovanRed, 1).toString();
						String prezime = tabelaMenadzer.getValueAt(selektovanRed, 2).toString();
						String pol = tabelaMenadzer.getValueAt(selektovanRed, 3).toString();
						String telefon = tabelaMenadzer.getValueAt(selektovanRed, 4).toString();
						String adresa = tabelaMenadzer.getValueAt(selektovanRed, 5).toString();
						String username = tabelaMenadzer.getValueAt(selektovanRed, 6).toString();
						String lozinka = tabelaMenadzer.getValueAt(selektovanRed, 7).toString();
						String strucnaSprema = tabelaMenadzer.getValueAt(selektovanRed, 8).toString();
						String staz = tabelaMenadzer.getValueAt(selektovanRed, 9).toString();
						String bonus = tabelaMenadzer.getValueAt(selektovanRed, 10).toString();
						String plata = tabelaMenadzer.getValueAt(selektovanRed, 11).toString();
						
						imeM.setText(ime);
						prezimeM.setText(prezime);
						polM.setSelectedItem(pol);
						telefonM.setText(telefon);
						adresaM.setText(adresa);
						usernameM.setText(username);
						lozinkaM.setText(lozinka);
						strucnaM.setText(strucnaSprema);
						stazM.setText(staz);
						bonusM.setText(bonus);
						plataM.setText(plata);
		            }

		        }
		    }
		});
		
		//DODAJ MENADZERA
		dodajM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tabelaMenadzer.getSelectedRow() == -1) {
					
					String mIme = imeM.getText();
					String mPrezime = prezimeM.getText();
					String mPol = polM.getSelectedItem().toString();
					String mTelefon = telefonM.getText();
					String mAdresa = adresaM.getText();
					String mUsername = usernameM.getText();
					String mLozinka = lozinkaM.getText();
					String mStrucnaSprema = strucnaM.getText();
					String mStaz = stazM.getText();
					String mBonus = bonusM.getText();
					String mPlata = plataM.getText();
					
					if (mIme.isEmpty() || mPrezime.isEmpty() || mPol.isEmpty() || mTelefon.isEmpty() || mAdresa.isEmpty() || mUsername.isEmpty() || mLozinka.isEmpty() || mStrucnaSprema.isEmpty() || mStaz.isEmpty() || mBonus.isEmpty() || mPlata.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Nisu popunjena sva polja.");
					} 
					else if(!Funkcionalnosti.slobodanUsername(mUsername)){
						    JOptionPane.showMessageDialog(null, "Korisnicko ime je zauzeto.");
					}
					else if (!SwingFunkcije.isDouble(mStrucnaSprema) || !SwingFunkcije.isDouble(mStaz) || !SwingFunkcije.isDouble(mBonus) || !SwingFunkcije.isDouble(mPlata)) {
						JOptionPane.showMessageDialog(null, "Polja 'strucna sprema', 'staz', 'bonus', 'plata' moraju biti brojevi.");
					}
					else {
						Menadzer m = new Menadzer(Funkcionalnosti.generisiID(), mIme, mPrezime, mPol, mTelefon, mAdresa, mUsername, mLozinka, Double.parseDouble(mStrucnaSprema), Double.parseDouble(mStaz), Double.parseDouble(mBonus), Double.parseDouble(mPlata));
						ManagerMenadzer.svimenadzeri.add(m);
						Menadzer.sacuvajMenadzere(ManagerMenadzer.svimenadzeri, "data/menadzeri.csv");
						tabelaMenadzer.setModel(SwingFunkcije.tableModelMenadzer());
						SwingFunkcije.isprazniPolja(imeM, prezimeM, polM, telefonM, adresaM, usernameM, lozinkaM, strucnaM, stazM, bonusM, plataM);
						JOptionPane.showMessageDialog(null, "Korisnik uspesno dodat.");
					}
					
				}
				else {
					JOptionPane.showMessageDialog(null, "Za dodavanje novog korisnika ne sme biti selektovan postojeci korisnik.");
				}
			}
		});
		
		//OBRISI MENADZERA
		obrisiM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tabelaMenadzer.getSelectedRow() != -1) {
					ManagerMenadzer.svimenadzeri.remove(tabelaMenadzer.getSelectedRow());
					Menadzer.sacuvajMenadzere(ManagerMenadzer.svimenadzeri, "data/menadzeri.csv");
					tabelaMenadzer.setModel(SwingFunkcije.tableModelMenadzer());
					SwingFunkcije.isprazniPolja(imeM, prezimeM, polM, telefonM, adresaM, usernameM, lozinkaM, strucnaM, stazM, bonusM, plataM);
					JOptionPane.showMessageDialog(null, "Korisnik uspesno obrisan.");
				}
				else {
					JOptionPane.showMessageDialog(null, "Niste izabrali korisnika za brisanje.");
				}
			}
		});
		
		//AZURIRAJ MENADZERA
		azurirajM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tabelaMenadzer.getSelectedRow() != -1) {
					
					String mIme = imeM.getText();
					String mPrezime = prezimeM.getText();
					String mPol = polM.getSelectedItem().toString();
					String mTelefon = telefonM.getText();
					String mAdresa = adresaM.getText();
					String mUsername = usernameM.getText();
					String mLozinka = lozinkaM.getText();
					String mStrucnaSprema = strucnaM.getText();
					String mStaz = stazM.getText();
					String mBonus = bonusM.getText();
					String mPlata = plataM.getText();
					
					if (mIme.isEmpty() || mPrezime.isEmpty() || mPol.isEmpty() || mTelefon.isEmpty() || mAdresa.isEmpty() || mUsername.isEmpty() || mLozinka.isEmpty() || mStrucnaSprema.isEmpty() || mStaz.isEmpty() || mBonus.isEmpty() || mPlata.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Nisu popunjena sva polja.");
					} 
					else if (!SwingFunkcije.isDouble(mStrucnaSprema) || !SwingFunkcije.isDouble(mStaz) || !SwingFunkcije.isDouble(mBonus) || !SwingFunkcije.isDouble(mPlata)) {
						JOptionPane.showMessageDialog(null, "Polja 'strucna sprema', 'staz', 'bonus', 'plata' moraju biti brojevi.");
					}
					else {
						int ID = Integer.parseInt(tabelaMenadzer.getValueAt(tabelaMenadzer.getSelectedRow(), 0).toString());
						Menadzer selektovan = ManagerMenadzer.nadjiMenadzeraPoID(ID);
						if(selektovan.getKorisnickoIme().equals(mUsername) || Funkcionalnosti.slobodanUsername(mUsername)) {
							
							selektovan.setIme(mIme);
							selektovan.setPrezime(mPrezime);
							selektovan.setPol(mPol);
							selektovan.setTelefon(mTelefon);
							selektovan.setAdresa(mAdresa);
							selektovan.setKorisnickoIme(mUsername);
							selektovan.setLozinka(mLozinka);
							selektovan.setStrucnaSprema(Double.parseDouble(mStrucnaSprema));
							selektovan.setStaz(Double.parseDouble(mStaz));
							selektovan.setBonus(Double.parseDouble(mBonus));
							selektovan.setPlata(Double.parseDouble(mPlata));
							ManagerMenadzer.svimenadzeri.set(tabelaMenadzer.getSelectedRow(), selektovan);
							Menadzer.sacuvajMenadzere(ManagerMenadzer.svimenadzeri, "data/menadzeri.csv");
							tabelaMenadzer.setModel(SwingFunkcije.tableModelMenadzer());
							SwingFunkcije.isprazniPolja(imeM, prezimeM, polM, telefonM, adresaM, usernameM, lozinkaM, strucnaM, stazM, bonusM, plataM);
							JOptionPane.showMessageDialog(null, "Korisnik uspesno azuriran.");
						}
						else {
							JOptionPane.showMessageDialog(null, "Korisnicko ime zauzeto.");
						}
						
						
						
					}
					
				}
				else {
					JOptionPane.showMessageDialog(null, "Niste izabrali korisnika za azuriranje.");
				}
			}
		});
		selectionModelKozmeticar.addListSelectionListener(new ListSelectionListener() {
		    @Override
		    public void valueChanged(ListSelectionEvent e) {
		        if (!e.getValueIsAdjusting()) {
		        	int selektovanRed = tabelaKozmeticar.getSelectedRow();
		            if (selektovanRed != -1) {
		                //int ID = Integer.parseInt(tabelaKozmeticar.getValueAt(selektovanRed, 0).toString());
		                String ime = tabelaKozmeticar.getValueAt(selektovanRed, 1).toString();
						String prezime = tabelaKozmeticar.getValueAt(selektovanRed, 2).toString();
						String pol = tabelaKozmeticar.getValueAt(selektovanRed, 3).toString();
						String telefon = tabelaKozmeticar.getValueAt(selektovanRed, 4).toString();
						String adresa = tabelaKozmeticar.getValueAt(selektovanRed, 5).toString();
						String username = tabelaKozmeticar.getValueAt(selektovanRed, 6).toString();
						String lozinka = tabelaKozmeticar.getValueAt(selektovanRed, 7).toString();
						String strucnaSprema = tabelaKozmeticar.getValueAt(selektovanRed, 8).toString();
						String staz = tabelaKozmeticar.getValueAt(selektovanRed, 9).toString();
						String bonus = tabelaKozmeticar.getValueAt(selektovanRed, 10).toString();
						String plata = tabelaKozmeticar.getValueAt(selektovanRed, 11).toString();
						String obucenZa = tabelaKozmeticar.getValueAt(selektovanRed, 12).toString();
						
						
						imeK.setText(ime);
						prezimeK.setText(prezime);
						polK.setSelectedItem(pol);
						telefonK.setText(telefon);
						adresaK.setText(adresa);
						usernameK.setText(username);
						lozinkaK.setText(lozinka);
						strucnaK.setText(strucnaSprema);
						stazK.setText(staz);
						bonusK.setText(bonus);
						plataK.setText(plata);

						
						listaTipovi.clearSelection();
						String tipoviRaw = obucenZa.substring(1, obucenZa.length() - 1);
						String[] tipovi = tipoviRaw.split(", ");
						DefaultListModel<tipTretmana> listModel = (DefaultListModel<tipTretmana>) listaTipovi.getModel();
						int[] selectedIndices = new int[tipovi.length];

						for (int i = 0; i < listModel.getSize(); i++) {
						    tipTretmana item = listModel.getElementAt(i);
						    for (int j = 0; j < tipovi.length; j++) {
						        if (item.getImeTretmana().equals(tipovi[j])) {
						            selectedIndices[j] = i;
						        }
						    }
						}

						listaTipovi.setSelectedIndices(selectedIndices);
						
						
		            }

		        }
		    }
		});
		
		
		//DODAJ KOZMETICARA
		dodajK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tabelaKozmeticar.getSelectedRow() == -1) {
					
					String kIme = imeK.getText();
					String kPrezime = prezimeK.getText();
					String kPol = polK.getSelectedItem().toString();
					String kTelefon = telefonK.getText();
					String kAdresa = adresaK.getText();
					String kUsername = usernameK.getText();
					String kLozinka = lozinkaK.getText();
					String kStrucnaSprema = strucnaK.getText();
					String kStaz = stazK.getText();
					String kBonus = bonusK.getText();
					String kPlata = plataK.getText();
					ArrayList<tipTretmana> selektovaniTipovi = (ArrayList<tipTretmana>) listaTipovi.getSelectedValuesList();
					
					if (kIme.isEmpty() || kPrezime.isEmpty() || kPol.isEmpty() || kTelefon.isEmpty() || kAdresa.isEmpty() || kUsername.isEmpty() || kLozinka.isEmpty() || kStrucnaSprema.isEmpty() || kStaz.isEmpty() || kBonus.isEmpty() || kPlata.isEmpty() || selektovaniTipovi == null) {
						JOptionPane.showMessageDialog(null, "Nisu popunjena sva polja.");
					} 
					else if(!Funkcionalnosti.slobodanUsername(kUsername)){
						    JOptionPane.showMessageDialog(null, "Korisnicko ime je zauzeto.");
					}
					else if (!SwingFunkcije.isDouble(kStrucnaSprema) || !SwingFunkcije.isDouble(kStaz) || !SwingFunkcije.isDouble(kBonus) || !SwingFunkcije.isDouble(kPlata)) {
						JOptionPane.showMessageDialog(null, "Polja 'strucna sprema', 'staz', 'bonus', 'plata' moraju biti brojevi.");
					}
					else {
						Kozmeticar k = new Kozmeticar(Funkcionalnosti.generisiID(), kIme, kPrezime, kPol, kTelefon, kAdresa, kUsername, kLozinka, Double.parseDouble(kStrucnaSprema), Double.parseDouble(kStaz), Double.parseDouble(kBonus), Double.parseDouble(kPlata), selektovaniTipovi);
						ManagerKozmeticar.svikozmeticari.add(k);
						Kozmeticar.sacuvajKozmeticare(ManagerKozmeticar.svikozmeticari, "data/kozmeticari.csv");
						tabelaKozmeticar.setModel(SwingFunkcije.tableModelKozmeticar());
						SwingFunkcije.isprazniPolja(imeK, prezimeK, polK, telefonK, adresaK, usernameK, lozinkaK, strucnaK, stazK, bonusK, plataK);
						listaTipovi.setSelectedValue(null, true);
						JOptionPane.showMessageDialog(null, "Korisnik uspesno dodat.");
					}
					
				}
				else {
					JOptionPane.showMessageDialog(null, "Za dodavanje novog korisnika ne sme biti selektovan postojeci korisnik.");
				}
			}
		});
		
		//OBRISI KOZMETICARA
		obrisiK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tabelaKozmeticar.getSelectedRow() != -1) {
					ManagerKozmeticar.svikozmeticari.remove(tabelaKozmeticar.getSelectedRow());
					Kozmeticar.sacuvajKozmeticare(ManagerKozmeticar.svikozmeticari, "data/kozmeticari.csv");
					tabelaKozmeticar.setModel(SwingFunkcije.tableModelKozmeticar());
					SwingFunkcije.isprazniPolja(imeK, prezimeK, polK, telefonK, adresaK, usernameK, lozinkaK, strucnaK, stazK, bonusK, plataK);
					listaTipovi.setSelectedValue(null, true);
					JOptionPane.showMessageDialog(null, "Korisnik uspesno obrisan.");
					
				}
				else {
					JOptionPane.showMessageDialog(null, "Niste izabrali korisnika za brisanje.");
				}
			}
		});
		
		//AZURIRAJ KOZMETICARA
		azurirajK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(tabelaKozmeticar.getSelectedRow() != -1) {
					
					String kIme = imeK.getText();
					String kPrezime = prezimeK.getText();
					String kPol = polK.getSelectedItem().toString();
					String kTelefon = telefonK.getText();
					String kAdresa = adresaK.getText();
					String kUsername = usernameK.getText();
					String kLozinka = lozinkaK.getText();
					String kStrucnaSprema = strucnaK.getText();
					String kStaz = stazK.getText();
					String kBonus = bonusK.getText();
					String kPlata = plataK.getText();
					ArrayList<tipTretmana> selektovaniTipovi = (ArrayList<tipTretmana>) listaTipovi.getSelectedValuesList();
					
					if (kIme.isEmpty() || kPrezime.isEmpty() || kPol.isEmpty() || kTelefon.isEmpty() || kAdresa.isEmpty() || kUsername.isEmpty() || kLozinka.isEmpty() || kStrucnaSprema.isEmpty() || kStaz.isEmpty() || kBonus.isEmpty() || kPlata.isEmpty() || selektovaniTipovi == null) {
						JOptionPane.showMessageDialog(null, "Nisu popunjena sva polja.");
					} 
					else if (!SwingFunkcije.isDouble(kStrucnaSprema) || !SwingFunkcije.isDouble(kStaz) || !SwingFunkcije.isDouble(kBonus) || !SwingFunkcije.isDouble(kPlata)) {
						JOptionPane.showMessageDialog(null, "Polja 'strucna sprema', 'staz', 'bonus', 'plata' moraju biti brojevi.");
					}
					else {
						int ID = Integer.parseInt(tabelaKozmeticar.getValueAt(tabelaKozmeticar.getSelectedRow(), 0).toString());
						Kozmeticar selektovan = ManagerKozmeticar.nadjiKozmeticaraPoID(ID);
						if(selektovan.getKorisnickoIme().equals(kUsername) || Funkcionalnosti.slobodanUsername(kUsername)) {
							

							
							selektovan.setIme(kIme);
							selektovan.setPrezime(kPrezime);
							selektovan.setPol(kPol);
							selektovan.setTelefon(kTelefon);
							selektovan.setAdresa(kAdresa);
							selektovan.setKorisnickoIme(kUsername);
							selektovan.setLozinka(kLozinka);
							selektovan.setStrucnaSprema(Double.parseDouble(kStrucnaSprema));
							selektovan.setStaz(Double.parseDouble(kStaz));
							selektovan.setBonus(Double.parseDouble(kBonus));
							selektovan.setPlata(Double.parseDouble(kPlata));
							selektovan.setObucenZa(selektovaniTipovi);
							ManagerKozmeticar.svikozmeticari.set(tabelaKozmeticar.getSelectedRow(), selektovan);
							Kozmeticar.sacuvajKozmeticare(ManagerKozmeticar.svikozmeticari, "data/kozmeticari.csv");
							tabelaKozmeticar.setModel(SwingFunkcije.tableModelKozmeticar());
							SwingFunkcije.isprazniPolja(imeK, prezimeK, polK, telefonK, adresaK, usernameK, lozinkaK, strucnaK, stazK, bonusK, plataK);
							listaTipovi.setSelectedValue(null, true);
							JOptionPane.showMessageDialog(null, "Korisnik uspesno azuriran.");
						}
						else {
							JOptionPane.showMessageDialog(null, "Korisnicko ime zauzeto.");
						}
						
						
						
					}
					
				}
				else {
					JOptionPane.showMessageDialog(null, "Niste izabrali korisnika za azuriranje.");
				}
			}
		});
		
		Integer vrednost = (Integer) scrollBar.getValue();
		lblVrednost.setText(vrednost.toString());
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Cenovnik", null, panel, null);
		panel.setLayout(null);
		
		JScrollPane scrollPane_6 = new JScrollPane();
		scrollPane_6.setBounds(10, 11, 1101, 453);
		panel.add(scrollPane_6);
		
		cenovnik = new JTable(SwingFunkcije.tableModelZakazaniCenovnik());
		scrollPane_6.setViewportView(cenovnik);
		
		JScrollBar scrollBarCene = new JScrollBar();
		scrollBarCene.setMaximum(10000);
		scrollBarCene.setMinimum(500);
		
		scrollBarCene.setOrientation(JScrollBar.HORIZONTAL);
		scrollBarCene.setBounds(10, 532, 154, 31);
		panel.add(scrollBarCene);
		
		JLabel ceneSlider = new JLabel("");
		ceneSlider.setFont(new Font("Tahoma", Font.BOLD, 18));
		ceneSlider.setHorizontalAlignment(SwingConstants.CENTER);
		ceneSlider.setBounds(10, 577, 154, 31);
		panel.add(ceneSlider);
		
		if(cenovnik.getSelectedRow() != -1) {
			Double cena = uslugaTretman.sveusluge.get(cenovnik.getSelectedRow()).getCena();
			ceneSlider.setText(cena.toString());
		}
		
		JButton promeniCenu = new JButton("PROMENI");
		
		promeniCenu.setFont(new Font("Tahoma", Font.PLAIN, 18));
		promeniCenu.setBounds(10, 619, 154, 31);
		panel.add(promeniCenu);
		
		JLabel lblCenovnik = new JLabel("Izaberi novu cenu");
		lblCenovnik.setHorizontalAlignment(SwingConstants.CENTER);
		lblCenovnik.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblCenovnik.setBounds(10, 490, 154, 31);
		panel.add(lblCenovnik);
		
		scrollBar.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				Integer vrednost = (Integer) scrollBar.getValue();
				lblVrednost.setText(vrednost.toString());
			}
		});
		
		scrollBarCene.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				Integer vrednostCenovnik = (Integer) scrollBarCene.getValue();
				ceneSlider.setText(vrednostCenovnik.toString());
			}
		});
		
		promeniCenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cenovnik.getSelectedRow() != -1) {
					Integer vrednostTretman = (Integer) scrollBarCene.getValue();
					Double cenaTretmana = Double.parseDouble(vrednostTretman.toString());
					uslugaTretman usluga = uslugaTretman.sveusluge.get(cenovnik.getSelectedRow());
					usluga.setCena(cenaTretmana);
					uslugaTretman.sveusluge.set(cenovnik.getSelectedRow(), usluga);
					uslugaTretman.sacuvajUsluge(uslugaTretman.sveusluge, "data/usluge.csv");
					cenovnik.setValueAt(cenaTretmana, cenovnik.getSelectedRow(), 4);
					
				}
				else {
					JOptionPane.showMessageDialog(null, "Niste izabrali uslugu");
				}
				
			}
		});
	}
}
