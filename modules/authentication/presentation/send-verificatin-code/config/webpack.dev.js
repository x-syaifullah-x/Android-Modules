import { merge } from "webpack"
import * as common from "./webpack.common.js"

export default merge(common, {
    mode: "development"
})
