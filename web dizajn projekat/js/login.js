

// POPUPI ZA LOGIN I REGISTREACIJU
function showPopup(popupId) {
    var popup = document.getElementById(popupId + "Popup");
    var overlay = document.getElementById("overlay");
  
    popup.style.display = "block";
    overlay.style.display = "block";
  }
  
  function hidePopup(popupId) {
    var popup = document.getElementById(popupId + "Popup");
    var overlay = document.getElementById("overlay");
  
    popup.style.display = "none";
    overlay.style.display = "none";
  }
  
  document.getElementById("overlay").addEventListener("click", function(event) {
    event.stopPropagation();
  });
  

  document.getElementById("loginPopup").addEventListener("submit", function(event) {
    event.preventDefault();
  
    var korisnicko = document.getElementById("loginKorisnickoIme").value;
    var sifra = document.getElementById("loginSifra").value;
    var logovan = false;

    database.ref("/korisnici").once("value")
      .then((snapshot) => {
        snapshot.forEach((childSnapshot) => {
          const korisniciId = childSnapshot.key;
          const korisniciData = childSnapshot.val();
            korisnickoime = korisniciData.korisnickoIme;
            lozinka = korisniciData.lozinka;

            if (korisnicko == korisnickoime && sifra == lozinka){
                var adminAgencija = document.getElementById("adminAgencija");
                var adminKorisnik = document.getElementById("adminKorisnik");
            
                adminAgencija.style.display = "block";
                adminKorisnik.style.display = "block";

                logovan = true;

                hidePopup('login');

                alert(`Uspesno ste se ulogovali kao korisnik "${korisnicko}"`);
            }
            
        });

        if(!logovan){
            alert("Netacni podaci, pokusajte ponovo!");
          }
          
      })
      .catch((error) => {
        console.log("Greska: ", error);
      });

      
  
  });
  
  document.getElementById("registerPopup").addEventListener("submit", function(event) {
    event.preventDefault();

    var noviKorisnikData = {
        korisnickoIme: document.getElementById("registerKorisnickoIme").value,
        lozinka: document.getElementById("registerSifra").value,
        ime: document.getElementById("registerIme").value,
        prezime: document.getElementById("registerPrezime").value,
        email: document.getElementById("registerEmail").value,
        datumRodjenja: document.getElementById("registerDatumRodjenja").value,
        adresa: document.getElementById("registerAdresa").value,
        telefon: document.getElementById("registerTelefon").value
    };

     database.ref("/korisnici").push(noviKorisnikData)
      .then(() => {
        console.log(`Uspesno ste se registrovali: ${noviKorisnikData.korisnickoIme}`);
      })
      .catch((error) => {
        console.log("Greska prilikom registracije:", error);
      });
  });
  