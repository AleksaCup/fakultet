package testovi;

import static org.junit.Assert.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;



import entity.*;
import funkcije.*;

import org.junit.Test;

public class FunkcionalnostiTest {

	@Test
    public void testDateToString() {
        LocalDateTime datum = LocalDateTime.of(2023, 6, 9, 10, 30);
        String ocekivano = "09/06/2023 10:30";
        assertEquals(ocekivano, Funkcionalnosti.dateToString(datum));
    }

    @Test
    public void testStringToDate() {
        String datumString = "09/06/2023 10:30";
        LocalDateTime ocekivano = LocalDateTime.of(2023, 6, 9, 10, 30);
        assertEquals(ocekivano, Funkcionalnosti.stringToDate(datumString));
    }

    @Test
    public void testDatumUString() {
        LocalDate datum = LocalDate.of(2023, 6, 9);
        String ocekivano = "09/06/2023";
        assertEquals(ocekivano, Funkcionalnosti.datumUString(datum));
    }

    @Test
    public void testStringUDatum() {
        String datumString = "09/06/2023";
        LocalDate ocekivano = LocalDate.of(2023, 6, 9);
        assertEquals(ocekivano, Funkcionalnosti.stringUDatum(datumString));
    }
    
    @Test
    public void testUcitajID() {
        String path = "data/testID.csv";
        int ocekivano = 123;
        napraviDatoteku(path, "123");
        assertEquals(ocekivano, Funkcionalnosti.ucitajID(path));
        izbrisiDatoteku(path);
    }

    @Test
    public void testSacuvajID() {
        String path = "data/testID.csv";
        int ID = 123;
        Funkcionalnosti.sacuvajID(ID, path);
        String kontent = citajizDatoteke(path);
        assertEquals("123\n", kontent);
        izbrisiDatoteku(path);
    }


    private void napraviDatoteku(String path, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String citajizDatoteke(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            StringBuilder kontent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
            	kontent.append(line).append("\n");
            }
            return kontent.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void izbrisiDatoteku(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }
    
    @Test
    public void testLoginNeuspesan() {
        String username = "wunderkind77";
        String sifra = "aleksa123";

        int dobijeniID = Funkcionalnosti.login(username, sifra);
        assertEquals(-1, dobijeniID);

    }
    
    @Test
    public void testLoginUspesan() {
        String username = "aleksacup9";
        String sifra = "aleksa123";
        int dobijeniID = Funkcionalnosti.login(username, sifra);
        assertEquals(77, dobijeniID);
    }
    

}
