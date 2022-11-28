import React, {FormEvent, useState} from 'react';
import {ResponseBookModel} from "../model/ResponseBookModel";
import axios from "axios";
import BookCard from "./BookCard";

function GetBooksFromApi() {

    const [searchText, setSearchText] = useState<string>("")
    const [result, setResult] = useState<ResponseBookModel>()



    const getBooksFromApi = () => {
        axios.get("/api/books/" + searchText)
            .then(response =>
                response.data)
            .catch(error => console.error(error))
            .then(setResult)
    }


    function handleChange(event: any) {
        const searchText = event.target.value;
        setSearchText(searchText);
    }

    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        getBooksFromApi();

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

                {Array.isArray(result)
                    ? result.map((current, index) =>
                        <div className={"book-card"}>
                            <BookCard key={index} book={current}/>
                        </div>)
                    : ""}
            </div>

        </div>
    );

}

export default GetBooksFromApi;