
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
const dataContainer = document.getElementById("flex-container");

database.ref("/agencije").once("value")
  .then((snapshot) => {
    dataContainer.innerHTML = "";

    snapshot.forEach((childSnapshot) => {
      const key = childSnapshot.key;
      const agencyData = childSnapshot.val();
      const destinacijaID =agencyData.destinacije;

      
      const div = document.createElement("div");
      div.classList.add("agency");

      const { adresa, brojTelefona, destinacije, email, godina, logo, naziv } = agencyData;

      const html = `
            <a href="agencija.html?id=${destinacijaID}" class="agencija-link" data-agencija-id="${destinacijaID}">
            <h3>${naziv}</h3>
            <img src="${logo}" class="agency-image">
            <h3>${godina}</h3>
            <h3>${adresa}</h3>
            <p>Telefon: ${brojTelefona}</p>
            <p>Email: ${email}</p>
            </a>

      `;

      div.innerHTML = html;
      dataContainer.appendChild(div);
    });
  })
  .catch((error) => {
    console.log("Greska:", error);
  });



