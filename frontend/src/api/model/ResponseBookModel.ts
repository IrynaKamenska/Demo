
import {BookModel} from "./BookModel";

export type ResponseBookModel =
    {
        "totalItems": number,
        "items":
            BookModel[]
    }

