package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import entity.Klijent;
import enums.KarticaLojalnosti;
import funkcije.Funkcionalnosti;
import funkcije.ManagerKlijent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegistracijaFrame extends JFrame {


	private static final long serialVersionUID = 5139008710346955655L;
	private JPanel contentPane;
	private JTextField ime;
	private JPasswordField sifra;
	private JTextField telefon;
	private JTextField adresa;
	private JTextField username;
	private JTextField prezime;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistracijaFrame frame = new RegistracijaFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RegistracijaFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 691, 507);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblIme = new JLabel("Ime");
		lblIme.setVerticalAlignment(SwingConstants.BOTTOM);
		lblIme.setHorizontalAlignment(SwingConstants.CENTER);
		lblIme.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblIme.setBounds(115, 23, 200, 36);
		contentPane.add(lblIme);
		
		ime = new JTextField();
		ime.setColumns(10);
		ime.setBounds(115, 70, 200, 30);
		contentPane.add(ime);
		
		sifra = new JPasswordField();
		sifra.setBounds(115, 335, 200, 30);
		contentPane.add(sifra);
		
		JButton btnPovratakNaLogin = new JButton("Povratak na login");
		btnPovratakNaLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Funkcionalnosti.openFrame("Start");
				dispose();
			}
		});
		btnPovratakNaLogin.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnPovratakNaLogin.setBounds(115, 423, 447, 36);
		contentPane.add(btnPovratakNaLogin);
		
		JLabel lblPrezime = new JLabel("Prezime");
		lblPrezime.setVerticalAlignment(SwingConstants.BOTTOM);
		lblPrezime.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrezime.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPrezime.setBounds(362, 23, 200, 36);
		contentPane.add(lblPrezime);
		
		JButton btnRegistrujSe = new JButton("Registruj se");
		
		btnRegistrujSe.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnRegistrujSe.setBounds(362, 301, 200, 64);
		contentPane.add(btnRegistrujSe);
		
		JLabel lblPol = new JLabel("Pol");
		lblPol.setVerticalAlignment(SwingConstants.BOTTOM);
		lblPol.setHorizontalAlignment(SwingConstants.CENTER);
		lblPol.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPol.setBounds(115, 111, 200, 36);
		contentPane.add(lblPol);
		
		JLabel lblTelefon = new JLabel("Telefon");
		lblTelefon.setVerticalAlignment(SwingConstants.BOTTOM);
		lblTelefon.setHorizontalAlignment(SwingConstants.CENTER);
		lblTelefon.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTelefon.setBounds(362, 111, 200, 36);
		contentPane.add(lblTelefon);
		
		telefon = new JTextField();
		telefon.setColumns(10);
		telefon.setBounds(362, 158, 200, 30);
		contentPane.add(telefon);
		
		JLabel lblNewLabel_1_1 = new JLabel("Adresa");
		lblNewLabel_1_1.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1.setBounds(115, 199, 200, 36);
		contentPane.add(lblNewLabel_1_1);
		
		adresa = new JTextField();
		adresa.setColumns(10);
		adresa.setBounds(115, 246, 200, 30);
		contentPane.add(adresa);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Korisnicko ime");
		lblNewLabel_1_1_1.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1_1.setBounds(362, 199, 200, 36);
		contentPane.add(lblNewLabel_1_1_1);
		
		username = new JTextField();
		username.setColumns(10);
		username.setBounds(362, 246, 200, 30);
		contentPane.add(username);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("Sifra");
		lblNewLabel_1_1_2.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel_1_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1_2.setBounds(115, 301, 200, 22);
		contentPane.add(lblNewLabel_1_1_2);
		
		prezime = new JTextField();
		prezime.setColumns(10);
		prezime.setBounds(362, 70, 200, 30);
		contentPane.add(prezime);
		
		JComboBox<String> pol = new JComboBox<String>();
		pol.setModel(new DefaultComboBoxModel<String>(new String[] {"Muski", "Zenski"}));
		pol.setBounds(115, 158, 200, 30);
		contentPane.add(pol);
		
		JLabel status = new JLabel("");
		status.setForeground(new Color(0, 255, 0));
		status.setBounds(115, 375, 447, 30);
		contentPane.add(status);
		
		btnRegistrujSe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status.setText("");
				
				//if(ime.getSelectedText() != null && telefon.getSelectedText() != null && adresa.getSelectedText() != null && username.getSelectedText() != null && prezime.getSelectedText() != null && sifra.getPassword() != null) {
					String imePolje = ime.getText();
					String prezimePolje = prezime.getText();
					int polIndex = pol.getSelectedIndex();
					String telefonPolje = telefon.getText();
					String adresaPolje = adresa.getText();
					String usernamePolje = username.getText();
					char[] sifraSlova = sifra.getPassword();
					String sifraPolje = new String(sifraSlova);
					String polPolje = new String();
				
				
					if(polIndex == 0) {
						polPolje = "M";
					}
					else if(polIndex == 1) {
						polPolje = "Z";
					}
					
					if(imePolje == null || prezimePolje == null || telefonPolje == null || adresaPolje == null || usernamePolje == null || sifraPolje == null) {
						JOptionPane.showMessageDialog(null, "Nisu popunjena sva polja");
					}
					else {
						Klijent k = new Klijent(Funkcionalnosti.generisiID(), imePolje, prezimePolje, polPolje, telefonPolje, adresaPolje, usernamePolje, sifraPolje, KarticaLojalnosti.NEMA, 0);
						ManagerKlijent.sviklijenti.add(k);
						Klijent.sacuvajKlijente(ManagerKlijent.sviklijenti, "data/klijenti.csv");
						status.setText("Uspesna registracija!");
						
					}
				
				
				
				
			}
		});
	}
}
