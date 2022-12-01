import React, {useState} from 'react';
import "./BookCard.css";

import {BookModel} from "../model/BookModel";
import addApiBookToDB from "../service/AddApiBookToDB";
import axios from "axios";
import {BookState} from "../model/BookState";
import AddApiBookToDB from "../service/AddApiBookToDB";

type BookCardProps = {
    book: BookModel;
    reloadAllBooks: () => void

}

function BookCard(props: BookCardProps) {



    return <>

        <div className={"book-card"}>
            {props.book.volumeInfo === undefined ? ""
                :
                <h3 className="book-title">{props.book.volumeInfo.title}</h3>}
                <p className="book-authors">{props.book.volumeInfo.authors}</p>
                <p className="book-authors">{props.book.volumeInfo.industryIdentifiers
                .map( ( {type, identifier} ) => {
                return <p key={props.book.id}>{type}{identifier}</p>
            })
            }</p>

            {props.book.volumeInfo.imageLinks === undefined ? ""
                :
                <img className="book-image" src={props.book.volumeInfo.imageLinks.thumbnail}
                     alt={props.book.volumeInfo.title}/>
            }
            <a href={props.book.volumeInfo.previewLink} className={"link-details"} target="_blank" rel="noreferrer">More Info</a>

        </div>
        <AddApiBookToDB book={props.book} reloadAllBooks={props.reloadAllBooks}/>
    </>
}

export default BookCard;