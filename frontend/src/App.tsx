import React, {useEffect, useState} from 'react';
import './App.css';
import GetBooksFromApi from './api/service/GetBooksFromApi';
import axios from "axios";
import {BookModel} from "./api/model/BookModel";

import AddApiBookToDB from "./api/service/AddApiBookToDB";


function App() {
    const [books, setBooks] = useState<BookModel[]>([]);

        const fetchAllBooks = () => {
            axios.get("/api/books/")
                .then(response => response.data)
                .catch(error => console.error("GET Error: " + error))
                .then(setBooks)
        }
    useEffect(fetchAllBooks, [])

    return <>
        <GetBooksFromApi reloadAllBooks={fetchAllBooks}/>
    </>;

}

export default App;
