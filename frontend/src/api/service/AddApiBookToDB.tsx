import React, {FormEvent, useState} from 'react';
import {BookModel} from "../model/BookModel";
import axios from "axios";
import {BookState} from "../model/BookState";
import Modal from "react-modal";

type AddBooksProps = {
    book: BookModel;
    reloadAllBooks: () => void
}

function AddApiBookToDB(props: AddBooksProps) {
    const[newBook, setNewBook] = useState<BookModel>();

    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false)
    const [book, setBook] = useState(
        {
            ...props.book
        }
    );

    const handleAddApiBook = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        axios.post("/api/books/", book)
            .then(response => {
                closeModal()
                return response.data
            })
            .catch(error => console.log("error =>" + error))
            .then(props.reloadAllBooks)


    setNewBook({
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
    })};

const openModal = () => {
    setModalIsOpen(true)
}

const closeModal = () => {
    setModalIsOpen(false)
}

function handleChange(event: any) {
    setBook({
        ...book,
        [event.target.name]: event.target.value
    })
}


return <>
    <button className="button-right" type={"submit"} onClick={openModal}>Add</button>
    <Modal className="modal"
           isOpen={modalIsOpen}
           onRequestClose={closeModal}
           contentLabel="Example Modal"
           ariaHideApp={false}
    >

        <form onSubmit={handleAddApiBook}>
            <br/>
            <label>
                Title:
                <input type="text"
                       id="title"
                       name="title"
                       value={book.volumeInfo.title}
                       onChange={handleChange}
                       placeholder="title"
                />
            </label>
            <br/>
            <label>
                Authors:
                <input type="text"
                       id="author"
                       name="author"
                       value={book.volumeInfo.authors}
                       onChange={handleChange}
                       placeholder="authors"
                />
            </label>
            <br/>
            <label htmlFor="bookState">New Book State:</label>
            <select value={book.bookState} name="bookState" id="bookState" onChange={handleChange}>
                <option value={BookState.AVAILABLE}>AVAILABLE</option>
                <option value={BookState.NOT_AVAILABLE}>NOT_AVAILABLE</option>
            </select>
            <br/><br/>
            <div className="modal-body">
                <p>Are you sure to add this book?</p>
            </div>
            <div>
                <button className="button-left">Add</button>
                <button className="button-right" onClick={() => closeModal()}>Close</button>
            </div>
        </form>
    </Modal>
</>;
}

export default AddApiBookToDB;