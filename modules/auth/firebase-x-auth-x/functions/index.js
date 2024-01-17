const firebase_functions = require("firebase-functions")
const firebase_functions_v2_identity = require("firebase-functions/v2/identity")
const admin = require("firebase-admin")
admin.initializeApp()
const express = require("express")
const { log } = require("firebase-functions/logger")
const { UserInfo } = require("firebase-admin/auth")
const app = express()
const cors = require("cors")({ origin: true })
app.use(cors)
app.set("json spaces", 4)

// Create and Deploy Your First Cloud Functions
// https://firebase.google.com/docs/functions/write-firebase-functions
/* http://domain/project_name/us-central1/user/check?phoneNumber=628 */
/* http://127.0.0.1:5001/x-x-x-projects/us-central1/user/check?phoneNumber=628 */
app.get("/check", async (request, response) => {
  const result = {}
  try {
    const adminAuth = admin.auth()
    const body = request.query
    const query = request.query
    result.exist = true
  } catch (error) {
    result.exist = false
  }
  response.send(result)
})

exports.user = firebase_functions.https.onRequest(app)

// exports.onCreateUser = functions.auth.user().onCreate((user) => {
// })

// exports.onDeleteUser = functions.auth.user().onDelete((user) => {
// })

// exports.beforeUserCreated = firebase_functions_v2_identity.beforeUserCreated(event => {
//   throw new firebase_functions_v2_identity.HttpsError("already-exists", "already-exists")
// })

// exports.beforeUserSignedIn = firebase_functions_v2_identity.beforeUserSignedIn((event) => {
// })