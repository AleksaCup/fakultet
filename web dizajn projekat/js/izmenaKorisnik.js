const firebaseConfig = {
    apiKey: "AIzaSyDuZV_t1VWAeWuprBQg2_FlARV-1u8y2qI",
    authDomain: "webdizajnprojekat-aleksacup.firebaseapp.com",
    databaseURL: "https://webdizajnprojekat-aleksacup-default-rtdb.europe-west1.firebasedatabase.app",
    projectId: "webdizajnprojekat-aleksacup",
    storageBucket: "webdizajnprojekat-aleksacup.appspot.com",
    messagingSenderId: "548765235634",
    appId: "1:548765235634:web:e1c9d84e2e04185cd4ccab"
  };
  
  firebase.initializeApp(firebaseConfig);



const database = firebase.database();
  

  function populateUserDropdown() {
    const userSelect = document.getElementById("UserSelect");
    
    userSelect.innerHTML = "";

    const defaultoption = document.createElement("option");
    defaultoption.value = 0;
    defaultoption.text = "Izaberite";
    userSelect.appendChild(defaultoption)
    
    database.ref("/korisnici").once("value")
      .then((snapshot) => {
        snapshot.forEach((childSnapshot) => {
          const korisnikId = childSnapshot.key;
          const korisnikData = childSnapshot.val();
          const option = document.createElement("option");
          option.value = korisnikId;
          option.text = korisnikData.korisnickoIme;
          userSelect.appendChild(option);
        });
      })
      .catch((error) => {
        console.log("Error fetching agency data:", error);
      });
  }

  function populateFormFields(korisnikId) {
    const korisnikRef = database.ref(`/korisnici/${korisnikId}`);
    
    korisnikRef.once("value")
      .then((snapshot) => {
        const korisnikData = snapshot.val();

        document.getElementById("adresa").value = korisnikData.adresa;
        document.getElementById("datumRodjenja").value = korisnikData.datumRodjenja;
        document.getElementById("email").value = korisnikData.email;
        document.getElementById("ime").value = korisnikData.ime;
        document.getElementById("korisnickoIme").value = korisnikData.korisnickoIme;
        document.getElementById("lozinka").value = korisnikData.lozinka;
        document.getElementById("prezime").value = korisnikData.prezime;
        document.getElementById("telefon").value = korisnikData.telefon;
      })
      .catch((error) => {
        console.log("Greska:", error);
      });
  }
  
  function handleFormSubmit(event) {
    event.preventDefault();
    
    const confirmed = window.confirm("Da li si siguran da zelis da izmenis podatke?");

    if (confirmed) {

        const selectedUserId = document.getElementById("UserSelect").value;
        const selectedUserName = document.getElementById("UserSelect").options[document.getElementById("UserSelect").selectedIndex].text;
        const updatedData = {
        adresa: document.getElementById("adresa").value,
        datumRodjenja: document.getElementById("datumRodjenja").value,
        email: document.getElementById("email").value,
        ime: document.getElementById("ime").value,
        korisnickoIme: document.getElementById("korisnickoIme").value,
        lozinka: document.getElementById("lozinka").value,
        prezime: document.getElementById("prezime").value,
        telefon: document.getElementById("telefon").value
        };
        
        database.ref(`/korisnici/${selectedUserId}`).update(updatedData)
        .then(() => {
            alert(`Podaci za korisnika "${selectedUserName}" su uspesno azurirani!`);
        })
        .catch((error) => {
            alert(`Greska prilikom azuriranja baze: ${error}`);
        });
    }
  }

    function handleDelete() {
        const selectedUserId = document.getElementById("UserSelect").value;
        const selectedUserName = document.getElementById("UserSelect").options[document.getElementById("UserSelect").selectedIndex].text;
        
        const confirmed = window.confirm(`Da li si siguran da želiš da izbrišeš korisnika "${selectedUserName}"?`);
        
        if (confirmed) {
        database.ref(`/korisnici/${selectedUserId}`)
            .remove()
            .then(() => {
            alert(`Korisnik "${selectedUserName}" je uspešno izbrisan!`);
            })
            .catch((error) => {
            alert(`Greška prilikom brisanja korisnika: ${error}`);
            });
        }
  }
  
  document.getElementById("editForm").addEventListener("submit", handleFormSubmit);


  document.getElementById("deleteBtn").addEventListener("click", handleDelete);


    document.getElementById("UserSelect").addEventListener("change", function () {
        const selectedUserId = this.value;
        populateFormFields(selectedUserId);
    });
  

  populateUserDropdown();
