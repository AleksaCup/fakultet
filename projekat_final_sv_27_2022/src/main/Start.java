package main;

import funkcije.*;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;


import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class Start extends JFrame {
	
	public static int ID;

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JButton prijavaBtn;
	private JButton registracijaBtn;
	private JPasswordField passwordField;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Start frame = new Start();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Start() {
		setTitle("PhiAcademy - Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 584, 546);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Korisnicko ime:");
		lblNewLabel.setBounds(206, 64, 152, 36);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(206, 111, 152, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(206, 187, 152, 20);
		contentPane.add(passwordField);
		
		Funkcionalnosti.setujStanjaPrilikomPokretanjaAplikacije();
		
		prijavaBtn = new JButton("Prijavi se");
		prijavaBtn.setBounds(206, 235, 152, 36);
		getRootPane().setDefaultButton(prijavaBtn);
		
		prijavaBtn.setFont(new Font("Verdana", Font.PLAIN, 14));
		contentPane.add(prijavaBtn);
		
		
		
		JLabel lblSifra = new JLabel("Sifra:");
		lblSifra.setVerticalAlignment(SwingConstants.BOTTOM);
		lblSifra.setHorizontalAlignment(SwingConstants.CENTER);
		lblSifra.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblSifra.setBounds(206, 142, 152, 36);
		contentPane.add(lblSifra);
		
		registracijaBtn = new JButton("Registruj se");
		registracijaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Funkcionalnosti.openFrame("Registracija");
				dispose();
			}
		});
		
		registracijaBtn.setFont(new Font("Verdana", Font.PLAIN, 14));
		registracijaBtn.setBounds(206, 278, 152, 36);
		contentPane.add(registracijaBtn);
		
		

		
		
		prijavaBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				
				String kor = textField.getText().trim();
				char[] loz = passwordField.getPassword();
				String sifra = new String(loz);
				int id = Funkcionalnosti.login(kor, sifra);

				if(id != -1) {
					if(ManagerKlijent.nadjiKlijentapoID(id) != null) {
						Funkcionalnosti.openFrame("Klijent");
						dispose();
					}
					else if(ManagerKozmeticar.nadjiKozmeticaraPoID(id) != null) {
						Funkcionalnosti.openFrame("Kozmeticar");
						dispose();
					}
					else if(ManagerRecepcioner.nadjiRecepcioneraPoID(id) != null) {
						Funkcionalnosti.openFrame("Recepcioner");
						dispose();
					}
					else if(ManagerMenadzer.nadjiMenadzeraPoID(id) != null) {
						Funkcionalnosti.openFrame("Menadzer");
						dispose();
					}
					
				}
				
				
				
				
			}
			
		});
		

	}
}
