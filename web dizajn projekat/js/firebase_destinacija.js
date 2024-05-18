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
const opisContainer = document.getElementById("opisdestinacije");
const slikeContainer = document.getElementById("slikedestinacije");
const infoContainer = document.getElementById("infodestinacije");
const pozadina = document.getElementById("pozadina");

const urlParams = new URLSearchParams(window.location.search);
const destinacijaId = urlParams.get("id");

database.ref(`/destinacije`).once("value")
.then((snapshot) => {
    opisContainer.innerHTML = "";
    slikeContainer.innerHTML = "";
    infoContainer.innerHTML = "";

    snapshot.forEach((childSnapshot) => {
    const key = childSnapshot.key;
    const data = childSnapshot.val();

                        for (const key in data) {
                            if (Object.hasOwnProperty.call(data, key)) {
                                const element = data[key];

                                if(key == destinacijaId){

                                    
                                    const opisdiv = document.createElement("div");
                                    opisdiv.classList.add("deskripcija");

                                    const slikediv = document.createElement("div");
                                    slikediv.classList.add("galerija");

                                    const infodiv = document.createElement("div");
                                    infodiv.classList.add("agency");

                                    const cena = element.cena;
                                    const maxOsoba = element.maxOsoba;
                                    const naziv = element.naziv;
                                    const opis = element.opis;
                                    const prevoz = element.prevoz;
                                    const slike = element.slike;
                                    const tip = element.tip;

                                    const htmlopis = `
                                            <h1>${naziv}</h1>
                                            <p>${opis}</p>
                                            
                                    `;

                                    
                                    let htmlslike = '';
                                    for (let i = 0; i < slike.length; i++) {
                                        htmlslike += `<img src="${slike[i]}" class="slikedestinacije">`;
                                    }
                                    

                                    const htmlinfo = `

                                            <h3>Cena: ${cena} RSD</h3>
                                            <h5>Velicina grupe: ${maxOsoba}</h5>
                                            <h5>Prevoz: ${prevoz}</h5>
                                            <h5>Tip putovanja: ${tip}</h5>

                                            
                                    `;

                                    opisdiv.innerHTML = htmlopis;
                                    slikediv.innerHTML = htmlslike;
                                    infodiv.innerHTML = htmlinfo;
                                    opisContainer.appendChild(opisdiv);
                                    slikeContainer.appendChild(slikediv);
                                    infoContainer.appendChild(infodiv);

                                    pozadina.style.backgroundImage = `url("${slike[1]}")`;
                                    console.log(`url("${slike[1]}")`);
                                }
                                
                                
                            }
                        }

    });
})
.catch((error) => {
    console.log("Greska:", error);
});