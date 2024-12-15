import * as path from "path"
import HtmlWebpackPlugin from "html-webpack-plugin"
import { fileURLToPath } from "url"

const __filename = fileURLToPath(import.meta.url)

const __dirname = path.dirname(__filename)

export const entry = {
    app: "./src/index.js"
}
export const resolve = {
    alias: { src: path.resolve(__dirname, "../src") }
}
export const output = {
    path: path.resolve(__dirname, "../firebase/public"),
    filename: "bundle.js"
}
export const plugins = [
    new HtmlWebpackPlugin({
        template: "./src/template/index.html",
        favicon: "./src/assets/logo.png",
        filename: "index.html"
    })
]
export const performance = {
    hints: process.env.NODE_ENV === "production" ? "warning" : false
}
export const devtool = "source-map"

export const module = {
    rules: [
        // {
        //     test: /\.s?[ac]ss$/,
        //     use: [
        //         MiniCssExtractPlugin.loader,
        //         {
        //             loader: "css-loader",
        //             options: {
        //                 sourceMap: true
        //             }
        //         },
        //         {
        //             loader: "sass-loader",
        //             options: {
        //                 sourceMap: true
        //             }
        //         }
        //     ]
        // },
        // {
        //     test: /\.woff(2)?(\?v=[0-9]\.[0-9]\.[0-9])?$/,
        //     loader: "url-loader?limit=10000&mimetype=application/font-woff"
        // },
        // {
        //     test: /\.html$/,
        //     include: [
        //         path.resolve(__dirname, "../src/static")
        //     ],
        //     loader: "file-loader"
        // },
        // {
        //     test: /\.(ttf|eot|webp|png)(\?v=[0-9]\.[0-9]\.[0-9])?$/,
        //     loader: "file-loader"
        // },
        {
            test: /\.html$/i,
            include: [
                path.resolve(__dirname, "../src")
            ],
            loader: "html-loader"
        },
        // {
        //     test: /\.(ttf|eot|webp|png|gif)(\?v=[0-9]\.[0-9]\.[0-9])?$/,
        //     loader: "file-loader"
        // }
        {
            test: /\.(png|jpe?g|gif)$/i,
            use: [
                {
                    loader: "file-loader",
                },
            ],
        },
    ]
}
