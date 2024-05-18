// KONFIGURACIJA BAZE
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
  
  // FUNKCIJA ZA UBACIVANJE ELEMENATA IZ BAZE U DROPDOWN
  function populateDestinationDropdown() {
    const destinationSelect = document.getElementById("destinationSelect");
    
    destinationSelect.innerHTML = "";

    const defaultoption = document.createElement("option");
    defaultoption.value = 0;
    defaultoption.text = "Izaberite";
    destinationSelect.appendChild(defaultoption)


    document.getElementById("cena").disabled = true;
    document.getElementById("maxosoba").disabled = true;
    document.getElementById("naziv").disabled = true;
    document.getElementById("opis").disabled = true;
    document.getElementById("prevoz").disabled = true;
    document.getElementById("tip").disabled = true;
    
    database.ref("/destinacije").once("value")
      .then((snapshot) => {
        snapshot.forEach((childSnapshot) => {
            const destinationList = childSnapshot.val();
            for (const destination in destinationList) {
                if (Object.hasOwnProperty.call(destinationList, destination)) {
                    const element = destinationList[destination];

                    const option = document.createElement("option");
                    option.value = destination;
                    option.text = element.naziv;
                    destinationSelect.appendChild(option);
                    
                }
            }

        });
      })
      .catch((error) => {
        console.log("Greska sa bazom:", error);
      });
  }
  
  // FUNKCIJA ZA POPUNJAVANJE POLJA KADA SE SELEKTUJE IZ DROPDOWNA
  function populateFormFields(destination) {

    const destinationRef = database.ref(`/destinacije`);

    destinationRef.once("value")
      .then((snapshot) => {
        snapshot.forEach((childSnapshot) => {
          const agencijakljucevi = childSnapshot.key;
          const destinationList = childSnapshot.val();
  
          if (destination in destinationList) {
            const destinationData = destinationList[destination];

            document.getElementById("cena").value = destinationData.cena;
            document.getElementById("maxosoba").value = destinationData.maxOsoba;
            document.getElementById("naziv").value = destinationData.naziv;
            document.getElementById("opis").value = destinationData.opis;
            document.getElementById("prevoz").value = destinationData.prevoz;
            document.getElementById("tip").value = destinationData.tip;
          }
        });
      })
      .catch((error) => {
        console.log("Greska sa bazom:", error);
      });

  }
  
  // FUNKCIJA ZA UPDATE
  function handleFormSubmit(event) {
    event.preventDefault();
    
    const confirmed = window.confirm("Da li si siguran da zelis da izmenis podatke?");

    if (confirmed) {
        const selectedDestinationId = document.getElementById("destinationSelect").value;
        const updatedData = {
        cena: document.getElementById("cena").value,
        maxOsoba: document.getElementById("maxosoba").value,
        naziv: document.getElementById("naziv").value,
        opis: document.getElementById("opis").value,
        prevoz: document.getElementById("prevoz").value,
        tip: document.getElementById("tip").value
        };
        


        database.ref(`/destinacije`).once("value")
      .then((snapshot) => {
        snapshot.forEach((childSnapshot) => {

            const agencije = childSnapshot.val();
            const agencijeID = childSnapshot.key;
            if (selectedDestinationId in agencije) {
                const destinationData = agencije[selectedDestinationId];
                console.log(agencijeID);
                console.log(selectedDestinationId);
                console.log(destinationData);

                database.ref(`/destinacije/${agencijeID}/${selectedDestinationId}`).update(updatedData)
                .then(() => {
                    alert(`Podaci za destinaciju ${destinationData.naziv} su uspesno azurirani!`);
                })
                .catch((error) => {
                    alert(`Greska prilikom azuriranja baze: ${error}`);
                });

            }
        });
    })
    .catch((error) => {
      console.log("Greska sa bazom:", error);
    });

    }
  }

    // BRISANJE IZ BAZE
    function handleDelete() {
        const selectedDestinationId = document.getElementById("destinationSelect").value;
        const selectedDestinationName = document.getElementById("destinationSelect").options[document.getElementById("destinationSelect").selectedIndex].text;
        
        const confirmed = window.confirm(`Da li si siguran da želiš da izbrišeš destinaciju "${selectedDestinationName}"?`);
        
        if (confirmed) {
        database.ref(`/destinacije`).once("value")
        .then((snapshot) => {
          snapshot.forEach((childSnapshot) => {
  
              const agencije = childSnapshot.val();
              const agencijeID = childSnapshot.key;
              if (selectedDestinationId in agencije) {
                  const destinationData = agencije[selectedDestinationId];
  
                  database.ref(`/destinacije/${agencijeID}/${selectedDestinationId}`).remove()
                  .then(() => {
                      alert(`Destinacija "${selectedDestinationName}" je uspešno izbrisana!`);
                  })
                  .catch((error) => {
                      alert(`Greska prilikom brisanja destinacije: ${error}`);
                  });
  
  
              }
          });
      })
      .catch((error) => {
        console.log("Greska sa bazom:", error);
      });
        }
  }


  // LISTENER ZA IZMENU  
  document.getElementById("editForm").addEventListener("submit", handleFormSubmit);

  // LISTENER ZA BRISANJE
  document.getElementById("deleteBtn").addEventListener("click", handleDelete);


  // LISTENER ZA PROMENU IZBORA U DROPDOWNU
    document.getElementById("destinationSelect").addEventListener("change", function () {
        const selectedDestinationId = this.value;
        populateFormFields(selectedDestinationId);
        document.getElementById("cena").disabled = false;
        document.getElementById("maxosoba").disabled = false;
        document.getElementById("naziv").disabled = false;
        document.getElementById("opis").disabled = false;
        document.getElementById("prevoz").disabled = false;
        document.getElementById("tip").disabled = false;
    });
  
  // LOADUJ DATU U DROPDOWNU
  populateDestinationDropdown();
