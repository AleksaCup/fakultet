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
const dataContainer = document.getElementById("destinacijebaza");
const naslov = document.getElementById("naslovAgencija");

const urlParams = new URLSearchParams(window.location.search);
const agencijaId = urlParams.get("id");

database.ref("/agencije").once("value")
.then((snapshot) => {
    dataContainer.innerHTML = "";
    naslov.innerText = "";

    snapshot.forEach((childSnapshot) => {
    const key = childSnapshot.key;
    const podaci = childSnapshot.val();
    console.log(podaci.destinacije == agencijaId);
    if(podaci.destinacije == agencijaId){
        naslov.innerText = `Dobrodosli u agenciju ${podaci.naziv}`;
    }

    });

})


database.ref("/destinacije").once("value")
.then((snapshot) => {
    dataContainer.innerHTML = "";

    snapshot.forEach((childSnapshot) => {
    const key = childSnapshot.key;
    const data = childSnapshot.val();

    if(agencijaId == key){
           
            database.ref(`/destinacije/${key}`).once("value")
            .then((destinacijaSnapshot) => {
                const destinacijaData = destinacijaSnapshot.val();
                const destinacijaKey = destinacijaSnapshot.key;

                console.log(destinacijaKey);

                for (const kljuc in destinacijaData) {
                    if (Object.hasOwnProperty.call(destinacijaData, kljuc)) {
                        const element = destinacijaData[kljuc];
                        console.log(kljuc);

                        const div = document.createElement("div");
                        div.classList.add("agency");

                        const cena = element.cena;
                        const maxOsoba = element.maxOsoba;
                        const naziv = element.naziv;

                        const slike = element.slike;
                        

                        const html = `
                                <a href="destinacija.html?id=${kljuc}" class="destinacija-link" data-destinacija-id="${kljuc}">
                                <img src="${slike[0]}" class="agency-image">
                                <h3>${naziv}</h3>
                                </a>
                                
                        `;

                        div.innerHTML = html;
                        dataContainer.appendChild(div);
                        
                    }
                }
                
            })
            .catch((error) => {
                console.log("Greška pri dobavljanju podataka za destinaciju:", error);
            });

        //KRAJ IFA
    }

    });
})
.catch((error) => {
    console.log("Greska:", error);
});