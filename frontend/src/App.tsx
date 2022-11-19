import React, {useEffect, useState} from 'react';
import './App.css';
import {BookModel} from "./BookModel";
import axios from "axios";

function App() {
    const [books, setBooks] = useState<BookModel[]>([])

    const fetchBooks = () => {
        axios.get("/api/books")
            .then(response => response.data)
            .catch(error => console.log("GET Error" + error))
            .then(data => setBooks(data))
    }

    useEffect(() => {
        fetchBooks()
    }, [])


    const listBooks = books.map((book) =>
        <li>Title {book.title}
            Author {book.author}
            ISBN {book.isbn}</li>
    );
    return <>

        <ul>{listBooks}</ul>
    </>;


}

export default App;
