import * as firebaseApp from "firebase/app"
import * as firebaseAuth from "firebase/auth"

const queryParams = _getQueryParams()
const phoneNumber = queryParams.phoneNumber
if (phoneNumber != undefined && phoneNumber.length > 0) {
    const config = {
        apiKey: "AIzaSyB2xm59WDvXEQDyWxqiDVwZExt3OOmlMh8",
        authDomain: "x-x-x-test.firebaseapp.com",
        databaseURL: "https://x-x-x-test-default-rtdb.asia-southeast1.firebasedatabase.app",
        projectId: "x-x-x-test",
        storageBucket: "x-x-x-test.appspot.com",
        messagingSenderId: "560568355737",
        appId: "1:560568355737:web:1ebaae631fe64588d62108",
        measurementId: "G-1G2LQV6JLF"
    }
    const app = firebaseApp.initializeApp(config)
    const auth = firebaseAuth.getAuth(app)
    auth.languageCode = queryParams.languageCode
    const recaptchaContainer = "recaptcha_container"
    const elementBody = document.getElementsByTagName("body")[0]
    elementBody.innerHTML += `<div id="${recaptchaContainer}"></div>`
    window.recaptchaVerifier = new firebaseAuth.RecaptchaVerifier(recaptchaContainer, {
        callback: async (response) => {
            // eslint-disable-next-line no-undef
            RecaptchaCallback.onSubmit(response)
        }
    }, auth)
    window.recaptchaVerifier.render().then(async (id) => {
        // eslint-disable-next-line no-undef
        RecaptchaCallback.onRender()
    })
} else {
    console.log("required query param: phoneNumber=%2b628x");
}

function _getQueryParams() {
    const urlSearchParams = new URLSearchParams(window.location.search)
    return Object.fromEntries(urlSearchParams.entries())
}