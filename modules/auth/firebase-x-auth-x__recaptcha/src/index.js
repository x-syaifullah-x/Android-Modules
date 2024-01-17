import * as firebaseApp from "firebase/app"
import * as firebaseAuth from "firebase/auth"

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

function _getQueryParams() {
    const urlSearchParams = new URLSearchParams(window.location.search)
    return Object.fromEntries(urlSearchParams.entries())
}