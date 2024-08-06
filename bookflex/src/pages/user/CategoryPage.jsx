import React, { useState, useEffect } from 'react';
import axiosInstance from '../../api/axiosInstance';
import { useParams, Link } from 'react-router-dom';
import styles from './CategoryPage.module.css'; // CSS 모듈 사용

const CategoryPage = () => {
    const { categoryName } = useParams();
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null); // 에러 상태 추가

    useEffect(() => {
        const fetchBooks = async () => {
            setLoading(true);
            setError(null); // 에러 상태 초기화

            try {
                const response = await axiosInstance.get(`/books/category/${encodeURIComponent(categoryName)}`, {
                    params: {
                        page: 0,
                        size: 10,
                        direction: 'asc'
                    }
                });

                const books = response.data.data.content;
                setBooks(books); // API 응답에서 책 목록을 추출
            } catch (error) {
                console.error('Error fetching books:', error);
                setError('재고가 존재 하지 않습니다.'); // 에러 상태 설정
            } finally {
                setLoading(false);
            }
        };

        fetchBooks();

        return () => {
            // 컴포넌트 언마운트 시 클린업 코드 (필요시 추가)
        };
    }, [categoryName]);

    if (loading) return <div className={styles.loading}>Loading...</div>;

    if (error) return <div className={styles.error}>{error}</div>; // 에러 메시지 표시

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
                                <Link to={`/books/${book.bookId}`} className={styles.bookLink}>
                                    <h2 className={styles.CategoryBookTitle}>{book.bookName}</h2>
                                </Link>
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
