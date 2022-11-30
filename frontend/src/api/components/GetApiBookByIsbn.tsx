import React, {FormEvent, useState} from 'react';
import axios from "axios";
import BookCard from "./BookCard";
import "./BookGallery.css";
import {ResponseBookModel} from "../model/ResponseBookModel";
import {BookState} from "../model/BookState";

function GetApiBookByIsbn() {
    // const [isbn, setIsbn] = useState<string>("")
    const [text, setText] = useState<string>("")
    const [result, setResult] = useState<ResponseBookModel>({
        "totalItems": 0,
        "items": [
            {
                "id": 0,
                "volumeInfo": {
                    "title": "",
                    "authors": [],
                    "industryIdentifiers": [
                        {
                            "type": "",
                            "identifier": ""
                        }
                    ],
                    "imageLinks": {
                        "thumbnail": ""
                    },
                    "previewLink": "",
                }, bookState: BookState.AVAILABLE
            }
        ]
    });

    const[searchBy, setSearchBy] = useState<"isbn" | "text">()


const arg2 = "isbn/"+text;
const arg1 = "search/"+text;
const arg = (searchBy==="isbn" ? arg2 : arg1 )

    const getBookByIsbnFromApi = () => {
        axios.get("/api/books/"+ arg)
            .then(response =>
            response.data)
            .catch(error => console.error(error))
            .then(setResult)
    }


    function handleChange(event: any) {
        const text = event.target.value;
        setText(text);

    }

    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        getBookByIsbnFromApi()

    }

    function handleClick(){
        const a= searchBy==="isbn" ? "text":"isbn"
        setSearchBy(a)
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
            <button onClick={handleClick}>SearchByState: {searchBy}</button>

            <div className={"book-cards"}>

                {Array.isArray(result.items)
                    ? result.items.map((current, index) =>
                        <div className={"book-card"}>
                            <BookCard key={index} book={current}/>
                        </div>)
                    : ""}
            </div>

        </div>
    );

}

export default GetApiBookByIsbn;