import React from 'react';
import {ApiBookModel} from "../model/ApiBookModel";
import "./BookCard.css";
import {ResponseBookModel} from "../model/ResponseBookModel";

type BookCardProps = {
    book: ResponseBookModel;

}

function BookCard(props: BookCardProps) {

    return <>

        <div className={"book-card"}>
            <h3 className="book-title">{props.book.volumeInfo.title}</h3>
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
            {/*<a href={props.book.volumeInfo.previewLink} className={"link-details"} target="_blank" rel="noreferrer">More*/}
            {/*    Info</a>*/}
        </div>
        {/*<AddApiBook book={props.book} reloadAllBooks={props.reloadAllBooks}/>*/}
    </>
}

export default BookCard;