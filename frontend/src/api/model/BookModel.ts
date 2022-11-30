import {BookState} from "./BookState";

export type BookModel =
    {
        "id": number,
        "volumeInfo": {
            "title": string,
            "authors": [],
            "industryIdentifiers": [
                {
                    "type": string,
                    "identifier": string
                }
            ],
            "imageLinks": {
                "thumbnail": string
            },
            "previewLink": string
        },
        bookState: BookState,
    }
