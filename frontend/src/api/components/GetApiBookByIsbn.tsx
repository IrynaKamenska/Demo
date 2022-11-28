import React, {FormEvent,  useState} from 'react';
import axios from "axios";
import BookCard from "./BookCard";
import "./BookGallery.css";
import {ResponseBookModel} from "../model/ResponseBookModel";

function GetApiBookByIsbn() {
    const [isbn, setIsbn] = useState<string>("")
    const [result, setResult] = useState<ResponseBookModel>(
        {
            id: "",
            "volumeInfo": {
                "title": "",
                "authors": [],
                "industryIdentifiers": [
                    {
                        "type": "",
                        "identifier": ""
                    },
                    {
                        "type": "",
                        "identifier": ""
                    }
                ],
                "imageLinks": {
                    "thumbnail": ""
                }
            }
        }
    );


    const getBookByIsbnFromApi = () => {
        axios.get("/api/books/isbn/" + isbn)
            .then(response =>
            response.data)
            .catch(error => console.error(error))
            .then(setResult)
    }


    function handleChange(event: any) {
        const isbn = event.target.value;
        setIsbn(isbn);
    }

    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        getBookByIsbnFromApi()

    }


    return (
        <div>
            <form onSubmit={handleSubmit}>
                <input className={"search-input"} type="text"
                       placeholder={"Search for Books"}
                       autoComplete={"off"}
                       onChange={handleChange}
                />
                <button type={"submit"}>Search</button>
            </form>

            <div className={"book-cards"}>
                <div className={"book-card"}>
                    <BookCard key={result.id} book={result}/>
                </div>


                {/*{Array.isArray(books)
                    ? books.map((current, index) =>
                        <div className={"book-card"}>
                            <BookCard key={index} book={current}/>
                        </div>)
                    : <div className={"book-card"}>
                        <BookCard book={books}/>
                    </div>}*/}


                {/*   {books.map((current, index) =>
                    <div className={"book-card"}>
                        <BookCard key={index} book={current}/>
                    </div>)}*/}
            </div>

        </div>
    );

}

export default GetApiBookByIsbn;