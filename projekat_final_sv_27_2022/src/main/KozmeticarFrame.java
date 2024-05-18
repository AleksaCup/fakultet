package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import entity.Kozmeticar;
import funkcije.Funkcionalnosti;
import funkcije.ManagerKozmeticar;
import funkcije.SwingFunkcije;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.util.Date;
import javax.swing.JTabbedPane;
import com.toedter.calendar.JDateChooser;
import javax.swing.JLabel;
import java.awt.Font;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.beans.PropertyChangeEvent;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class KozmeticarFrame extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private JPanel panel;
	private JPanel panel_1;
	private JTable tabelaDodeljeni;
	private JTable tabelaRaspored;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KozmeticarFrame frame = new KozmeticarFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public KozmeticarFrame() {
		setTitle("PhiAcademy - Kozmeticar");
		int KorisnikID = Funkcionalnosti.ucitajID("data/id.csv");
		Kozmeticar kozmeticar = ManagerKozmeticar.nadjiKozmeticaraPoID(KorisnikID);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1160, 627);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 69, 1126, 511);
		contentPane.add(tabbedPane);
		
		panel = new JPanel();
		tabbedPane.addTab("Dodeljeni tretmani", null, panel, null);
		panel.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 1101, 461);
		panel.add(scrollPane);
		
		tabelaDodeljeni = new JTable();
		scrollPane.setViewportView(tabelaDodeljeni);
		tabelaDodeljeni.setModel(SwingFunkcije.tableModelZakazaniTretmaniKozmeticar(kozmeticar));
		
		panel_1 = new JPanel();
		tabbedPane.addTab("Raspored", null, panel_1, null);
		panel_1.setLayout(null);
		
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				Date datum = dateChooser.getDate();
				if(datum != null) {
					LocalDate datumLocal = datum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					tabelaRaspored.setModel(SwingFunkcije.tableModelRaspored(kozmeticar, datumLocal));
				}
				
			}
		});
		dateChooser.setBounds(60, 11, 122, 31);
		panel_1.add(dateChooser);
		
		JLabel lblDan_1 = new JLabel("Dan:");
		lblDan_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblDan_1.setBounds(10, 11, 52, 31);
		panel_1.add(lblDan_1);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 72, 1101, 400);
		panel_1.add(scrollPane_1);
		
		tabelaRaspored = new JTable();
		scrollPane_1.setViewportView(tabelaRaspored);
		
		JLabel lblNewLabel = new JLabel("Pozdrav, " + kozmeticar.getIme() + "!");
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
		odjava.setBounds(1011, 12, 125, 33);
		contentPane.add(odjava);

		//ArrayList<Kozmeticar> kozmeticarList = ManagerKozmeticar.svikozmeticari;

		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("ID");
		model.addColumn("Ime");
		model.addColumn("Prezime");
		model.addColumn("Pol");
		model.addColumn("Telefon");
		model.addColumn("Adresa");
		model.addColumn("Korisnicko Ime");
		model.addColumn("Lozinka");
		model.addColumn("Strucna Sprema");
		model.addColumn("Staz");
		model.addColumn("Bonus");
		model.addColumn("Plata");
		model.addColumn("Tretmani");
	}
}