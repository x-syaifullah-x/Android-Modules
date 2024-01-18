import * as firebaseAuth from "firebase/auth"

export class Aaa implements firebaseAuth.ApplicationVerifier {

    a = ""
    readonly type: string = "recaptcha"

    seta(aa: string) {
        this.a = aa
    }
    verify(): Promise<string> {
        return Promise.resolve(this.a)
    }

    _reset(){

    }
}