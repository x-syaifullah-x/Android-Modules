import * as firebaseApp from "firebase/app"
import * as firebaseAuth from "firebase/auth"
import logo from "/src/assets/logo.png"

const img = document.getElementById("sc_logo")
img.setAttribute("src", logo)

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
// https://developers.google.com/recaptcha/docs/display#render_param
window.recaptchaVerifier = new firebaseAuth.RecaptchaVerifier("sc_recaptcha_container", {
    "theme": "dark", // dark | light
    size: "compact", // compact | normal
    tabindex: 0,
    callback: async (response) => {
        try {
            RecaptchaCallback.onSubmit(response)
        } catch (error) {
        } finally {
            window.recaptchaVerifier.recaptcha.reset();
        }
    },
}, auth)
window.recaptchaVerifier.render().then(async (id) => {
    try {
        RecaptchaCallback.onRender()
    } catch (error) {

    }
})

function _getQueryParams() {
    const urlSearchParams = new URLSearchParams(window.location.search)
    return Object.fromEntries(urlSearchParams.entries())
}