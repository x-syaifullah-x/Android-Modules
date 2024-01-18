import * as firebaseApp from "firebase/app"
import * as firebaseAuth from "firebase/auth"
import { Aaa } from "./a.ts";

const queryParams = _getQueryParams()
const config = {
    apiKey: "AIzaSyDVB5Yivc3wAR20o5kMLDfb9gLNQBaUWaM",
    authDomain: "x-x-x-projects.firebaseapp.com",
    projectId: "x-x-x-projects",
    storageBucket: "x-x-x-projects.appspot.com",
    messagingSenderId: "1098413132051",
    appId: "1:1098413132051:web:4b45b454104bb139cc72e6",
    measurementId: "G-J06E9DLM3M"
};

const app = firebaseApp.initializeApp(config)
const auth = firebaseAuth.getAuth(app)
auth.languageCode = queryParams.languageCode
const recaptchaContainer = "recaptcha_container"
// const elementBody = document.getElementById("aaaa")
// elementBody.innerHTML += `<div id="${recaptchaContainer}"></div>`
window.recaptchaVerifier = new firebaseAuth.RecaptchaVerifier("recaptcha_container", {
    callback: async (response) => {
        const phoneAuthProvider = new firebaseAuth.PhoneAuthProvider(auth)
        const a = new Aaa()
        a.a = response
        const verificationId = await phoneAuthProvider.verifyPhoneNumber("+628123456789", a)
        const phoneAuthCredential = firebaseAuth.PhoneAuthProvider.credential(verificationId, "123456")
        const userCredential = await firebaseAuth.signInWithEmailAndPassword(
            auth, "a@gmail.com", "123456"
        )
        const result = await firebaseAuth.linkWithCredential(userCredential.user, phoneAuthCredential)
        const aa = await firebaseAuth.multiFactor().getSession()
        aa.response
    }
}, auth)

window.recaptchaVerifier.render().then(async (id) => {
    // eslint-disable-next-line no-undef
    RecaptchaCallback.onRender()
})

function _getQueryParams() {
    const urlSearchParams = new URLSearchParams(window.location.search)
    return Object.fromEntries(urlSearchParams.entries())
}