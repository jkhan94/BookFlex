import React, { useState, useEffect } from 'react';
import axiosInstance from '../../api/axiosInstance';
import { useParams } from 'react-router-dom';
import styles from './CategoryPage.module.css'; // CSS 모듈 사용

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

    if (loading) return <div className={styles.loading}>Loading...</div>;

    return (
        <div className={styles.categoryPage}>
            <h1>{categoryName} Books</h1>
            {books.length === 0 ? (
                <p>No books found.</p>
            ) : (
                <div className={styles.bookCards}>
                    {books.map(book => (
                        <div key={book.bookId} className={styles.bookCard}>
                            <img src={book.photoImagePath} alt={book.bookName} className={styles.bookImage} />
                            <div className={styles.bookDetails}>
                                <h2 className={styles.CategoryBookTitle }>{book.bookName}</h2>
                                <p className={styles.bookAuthor}>Author: {book.author}</p>
                                <p className={styles.bookPrice}>Price: ${book.price.toFixed(2)}</p>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default CategoryPage;
