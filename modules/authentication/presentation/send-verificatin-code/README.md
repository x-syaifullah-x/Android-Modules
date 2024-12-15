# RECAPTCHA WEB

### After Hosting
```
after hosting add your domain to:
    Firebase Console -> Project -> Authentication -> Settings -> Authorized domains -> Add domain
```

### DEPLOY
```sh
firebase deploy --only hosting:send-verification-code
```