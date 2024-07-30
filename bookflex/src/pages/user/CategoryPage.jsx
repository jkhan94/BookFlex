import React, { useState, useEffect } from 'react';
import axiosInstance from '../../api/axiosInstance';
import { useParams } from 'react-router-dom';
import './CategoryPage.module.css'; // 스타일을 위한 CSS 파일 임포트

const CategoryPage = () => {
    const { categoryName } = useParams();
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchBooks = async () => {
            try {
                const response = await axiosInstance.get(`/books/category/${encodeURIComponent(categoryName)}`, {
                    params: {
                        page: 0,
                        size: 10,
                        direction: 'asc'
                    }
                });
                setBooks(response.data.data.content); // API 응답에서 책 목록을 추출
            } catch (error) {
                console.error('Error fetching books:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchBooks();
    }, [categoryName]);

    if (loading) return <div>Loading...</div>;

    return (
        <div className="category-page">
            <h1>{categoryName} Books</h1>
            {books.length === 0 ? (
                <p>No books found.</p>
            ) : (
                <div className="book-cards">
                    {books.map(book => (
                        <div key={book.bookId} className="book-card">
                            <img src={book.photoImagePath} alt={book.bookName} className="book-image" />
                            <div className="book-details">
                                <h2 className="book-title">{book.bookName}</h2>
                                <p className="book-author">Author: {book.author}</p>
                                <p className="book-price">Price: ${book.price.toFixed(2)}</p>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default CategoryPage;
