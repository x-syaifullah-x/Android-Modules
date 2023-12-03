const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();
const express = require("express");
const { log } = require("firebase-functions/logger");
const app = express()
const cors = require("cors")({ origin: true })
app.use(cors);
app.set("json spaces", 4)

// Create and Deploy Your First Cloud Functions
// https://firebase.google.com/docs/functions/write-firebase-functions
/* http://domain/project_name/us-central1/user/check?phoneNumber=628 */
/* http://127.0.0.1:5001/x-x-x-projects/us-central1/user/check?phoneNumber=628 */
app.get("/check", async (request, response) => {
  const result = {}
  try {
    /* replace query [+]628 with [%2B]628 */
    const phoneNumber = request.query.phoneNumber
    const user = await admin.auth().getUserByPhoneNumber(phoneNumber)
    result.exist = true
  } catch (error) {
    result.exist = false
  }
  response.send(result)
})

exports.user = functions.https.onRequest(app)

// {
//       email: 'a@gmail.com',
//       emailVerified: false,
//       displayName: 'a',
//       photoURL: '',
//       phoneNumber: null,
//       disabled: false,
//       providerData: [
//         {
//           providerId: 'password',
//           email: 'a@gmail.com',
//           federatedId: 'a@gmail.com',
//           rawId: 'a@gmail.com',
//           displayName: 'a',
//           photoUrl: ''
//         }
//       ],
//       customClaims: {},
//       passwordSalt: null,
//       passwordHash: null,
//       tokensValidAfterTime: null,
//       uid: 'unGFoId0sZDps3BBsLp1YlITssgF',
//       metadata: UserRecordMetadata {
//         creationTime: '2023-12-04T02:57:45.174Z',
//         lastSignInTime: '2023-12-04T02:57:45.174Z'
//       },
//       mfaInfo: [],
//       toJSON: [Function (anonymous)]
//     }
//     {"severity":"WARNING","message":"Function returned undefined, expected Promise or value"}
exports.userOnCreate = functions.auth.user().onCreate((user) => {
  console.log(user)
});

// {
//       email: 'a@gmail.com',
//       emailVerified: false,
//       displayName: 'a',
//       photoURL: '',
//       phoneNumber: null,
//       disabled: false,
//       providerData: [
//         {
//           providerId: 'password',
//           email: 'a@gmail.com',
//           federatedId: 'a@gmail.com',
//           rawId: 'a@gmail.com',
//           displayName: 'a',
//           photoUrl: ''
//         }
//       ],
//       customClaims: {},
//       passwordSalt: null,
//       passwordHash: null,
//       tokensValidAfterTime: null,
//       uid: 'unGFoId0sZDps3BBsLp1YlITssgF',
//       metadata: UserRecordMetadata {
//         creationTime: '2023-12-04T02:57:45.174Z',
//         lastSignInTime: '2023-12-04T02:57:45.174Z'
//       },
//       mfaInfo: [],
//       toJSON: [Function (anonymous)]
//     }
//     {"severity":"WARNING","message":"Function returned undefined, expected Promise or value"}
exports.userOnDelete = functions.auth.user().onDelete((user) => {
  console.log(user)
});