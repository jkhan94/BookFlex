import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axiosInstance';
import './BookDetailPages.css';

const BookDetailPage = () => {
    const { bookId } = useParams();
    const navigate = useNavigate();
    const [book, setBook] = useState(null);
    const [reviews, setReviews] = useState([]);
    const [reviewPage, setReviewPage] = useState(1);
    const [reviewTotalPages, setReviewTotalPages] = useState(10);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchBook = async () => {
            try {
                const response = await axiosInstance.get(`/books/${bookId}`);
                setBook(response.data.data);
            } catch (error) {
                console.error('There was an error!', error);
                setError('상품 조회에 실패하였습니다.');
            }
        };

        fetchBook();
    }, [bookId]);

    useEffect(() => {
        const fetchReviews = async () => {
            try {
                const response = await axiosInstance.get(`/reviews/book/${bookId}`, {
                    params: {
                        page: reviewPage,
                        size: 5,
                        direction: true,
                        sortBy: 'createdAt',
                    },
                });
                setReviews(response.data.data.content);
                setReviewTotalPages(response.data.data.totalPages);
            } catch (error) {
                console.error('There was an error!', error);
                setError('리뷰 조회에 실패하였습니다.');
            }
        };

        fetchReviews();
    }, [bookId, reviewPage]);

    const handleEditClick = () => {
        navigate(`/admin/books/${bookId}/edit`, { state: { book } });
    };

    const handlePreviousReviewPage = () => {
        if (reviewPage > 1) {
            setReviewPage(reviewPage - 1);
        }
    };

    const handleNextReviewPage = () => {
        if (reviewPage < reviewTotalPages) {
            setReviewPage(reviewPage + 1);
        }
    };

    if (error) {
        return <p>{error}</p>;
    }

    if (!book) {
        return <p>Loading...</p>;
    }

    return (
        <div className="book-detail-container">
            <h1>상품 상세</h1>
            <div className="book-detail">
                <div className="book-image">
                    <img src={book.photoImagePath} alt={book.bookName} />
                </div>
                <div className="book-info">
                    <div>
                        <label>상품명:</label>
                        <span>{book.bookName}</span>
                    </div>
                    <div>
                        <label>출판사:</label>
                        <span>{book.publisher}</span>
                    </div>
                    <div>
                        <label>작가:</label>
                        <span>{book.author}</span>
                    </div>
                    <div>
                        <label>카테고리:</label>
                        <span>{book.subCategoryName}</span>
                    </div>
                    <div>
                        <label>상품가격:</label>
                        <span>{book.price.toLocaleString()}원</span>
                    </div>
                    <div>
                        <label>재고:</label>
                        <span>{book.stock}권</span>
                    </div>
                    <div>
                        <label>상품 설명:</label>
                        <p>{book.bookDescription}</p>
                    </div>
                    <div>
                        <label>상품 상태:</label>
                        <p>{book.status}</p>
                    </div>
                    <div>
                        <label>등록 일자:</label>
                        <p>{new Date(book.createdAt).toLocaleString()}</p>
                    </div>
                    <button onClick={handleEditClick}>수정</button>
                </div>
            </div>

            <div className="reviews-section">
                <h2>상품 리뷰</h2>
                {reviews.length > 0 ? (
                    <>
                        <ul className="reviews-list">
                            {reviews.map((review) => (
                                <li key={review.createdAt}>
                                    <h3>{review.title}</h3>
                                    <p>{review.content}</p>
                                    <p>별점: {review.star}</p>
                                    <p>작성자: {review.username}</p>
                                    <p>작성일: {new Date(review.createdAt).toLocaleString()}</p>
                                    {review.createdAt !== review.modifiedAt && (
                                        <p>수정일: {new Date(review.modifiedAt).toLocaleString()}</p>
                                    )}
                                </li>
                            ))}
                        </ul>
                        <div className="pagination">
                            <button onClick={handlePreviousReviewPage} disabled={reviewPage === 1}>이전</button>
                            <span>{reviewPage} / {reviewTotalPages}</span>
                            <button onClick={handleNextReviewPage} disabled={reviewPage === reviewTotalPages}>다음</button>
                        </div>
                    </>
                ) : (
                    <p>리뷰가 없습니다.</p>
                )}
            </div>
        </div>
    );
};

export default BookDetailPage;
