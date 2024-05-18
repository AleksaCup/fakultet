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
  
  function populateAgencyDropdown() {
    const agencySelect = document.getElementById("agencySelect");
    
    agencySelect.innerHTML = "";

    const defaultoption = document.createElement("option");
    defaultoption.value = 0;
    defaultoption.text = "Izaberite";
    agencySelect.appendChild(defaultoption)
    
    database.ref("/agencije").once("value")
      .then((snapshot) => {
        snapshot.forEach((childSnapshot) => {
          const agencyId = childSnapshot.key;
          const agencyData = childSnapshot.val();
          const option = document.createElement("option");
          option.value = agencyId;
          option.text = agencyData.naziv;
          agencySelect.appendChild(option);
        });
      })
      .catch((error) => {
        console.log("Error fetching agency data:", error);
      });
  }
  
  // FUNKCIJA ZA POPUNJAVANJE POLJA FORME
  function populateFormFields(agencyId) {
    const agencyRef = database.ref(`/agencije/${agencyId}`);
    
    agencyRef.once("value")
      .then((snapshot) => {
        const agencyData = snapshot.val();

        document.getElementById("address").value = agencyData.adresa;
        document.getElementById("phoneNumber").value = agencyData.brojTelefona;
        document.getElementById("email").value = agencyData.email;
        document.getElementById("year").value = agencyData.godina;
        document.getElementById("logo").value = agencyData.logo;
        document.getElementById("name").value = agencyData.naziv;
      })
      .catch((error) => {
        console.log("Greska:", error);
      });
  }
  
  // FUNKCIJA ZA UPDATE
  function handleFormSubmit(event) {
    event.preventDefault();
    
    const confirmed = window.confirm("Da li si siguran da zelis da izmenis podatke?");

    if (confirmed) {

        const selectedAgencyId = document.getElementById("agencySelect").value;
        const selectedAgencyName = document.getElementById("agencySelect").options[document.getElementById("agencySelect").selectedIndex].text;
        const updatedData = {
        adresa: document.getElementById("address").value,
        brojTelefona: document.getElementById("phoneNumber").value,
        email: document.getElementById("email").value,
        godina: document.getElementById("year").value,
        logo: document.getElementById("logo").value,
        naziv: document.getElementById("name").value
        };
        
        database.ref(`/agencije/${selectedAgencyId}`).update(updatedData)
        .then(() => {
            alert(`Podaci za agenciju "${selectedAgencyName}" su uspesno azurirani!`);
        })
        .catch((error) => {
            alert(`Greska prilikom azuriranja baze: ${error}`);
        });
    }
  }

  // FUNKCIJA ZA BRISANJE
    function handleDelete() {
        const selectedAgencyId = document.getElementById("agencySelect").value;
        const selectedAgencyName = document.getElementById("agencySelect").options[document.getElementById("agencySelect").selectedIndex].text;
        
        const confirmed = window.confirm(`Da li si siguran da želiš da izbrišeš agenciju "${selectedAgencyName}"?`);
        
        if (confirmed) {
        database.ref(`/agencije/${selectedAgencyId}`)
            .remove()
            .then(() => {
            alert(`Agencija "${selectedAgencyName}" je uspešno izbrisana!`);
            })
            .catch((error) => {
            alert(`Greška prilikom brisanja agencije: ${error}`);
            });
        }
  }
  
  // LISTENER ZA UPDATE
  document.getElementById("editForm").addEventListener("submit", handleFormSubmit);

  // LISTENER ZA DELETE
  document.getElementById("deleteBtn").addEventListener("click", handleDelete);

  // LISTENER ZA POPUNJAVANJE POLJA
    document.getElementById("agencySelect").addEventListener("change", function () {
        const selectedAgencyId = this.value;
        populateFormFields(selectedAgencyId);
    });
  
  populateAgencyDropdown();
