import {IsbnModel} from "./IsbnModel";


export type ApiBookModel = {
    id: string,
    volumeInfo: {
        title: string;
        authors: string[];
        previewLink: string;
        imageLinks: {
            thumbnail: string;
        };
         industryIdentifiers: IsbnModel[];
    };
}
