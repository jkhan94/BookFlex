import React, { useEffect, useState } from 'react';
import axiosInstance from '../../api/axiosInstance';
import { useNavigate } from 'react-router-dom';
import './ReviewListPage.css';
import { isAdmin } from './tokenCheck';

function renderStars(rating) {
    return '★'.repeat(rating) + '☆'.repeat(5 - rating);
}

function Review({ review, onDelete }) {
    const navigate = useNavigate();

    useEffect(() => {
        if(!isAdmin())
            navigate('/main/dashboard');
    },[navigate]);

    const handleTitleClick = () => {
        navigate(`/admin/reviews/${review.id}`);
    };

    return (
        <div className="review">
            <div className="review-header">
                <span className="product-name">{review.product}</span>
                <span className="stars">{renderStars(review.star)}</span>
            </div>
            <p className="review-content">도서명 : {review.bookName}</p>
            <p className="review-content">
                제목 : <span className="review-title" onClick={handleTitleClick}>{review.title}</span>
            </p>
            <p className="review-content">작성자 : {review.nickname}</p>
            <p className="review-content">내용 : {review.content}</p>
            <div className="review-footer">
                <span>{review.author}</span>
                <span>{new Date(review.createdAt).toLocaleDateString('ko-KR', {
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric'
                })}</span>
                <button className="delete-button" onClick={() => onDelete(review.id)}>삭제</button>
            </div>
        </div>
    );
}

function ReviewListPage() {
    const [reviewData, setReviewData] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);

    const fetchReviews = (page = 1, bookName = '') => {
        axiosInstance.get('/reviews', {
            params: {
                page: page,
                size: 5,
                bookName: bookName
            }
        })
            .then(response => {
                setReviewData(response.data.data.content);
                setTotalPages(response.data.data.totalPages);
            })
            .catch(error => {
                console.error('There was an error fetching the reviews!', error);
            });
    };

    useEffect(() => {
        fetchReviews(currentPage, searchTerm);
    }, [currentPage, searchTerm]);

    const handleSearch = (event) => {
        const newSearchTerm = event.target.value;
        setSearchTerm(newSearchTerm);
        setCurrentPage(1);
    };

    const handlePageChange = (page) => {
        setCurrentPage(page);
    };

    const handleDelete = (reviewId) => {
        if (window.confirm('리뷰를 삭제하시겠습니까?')) {
            axiosInstance.delete(`/reviews/${reviewId}`)
                .then(response => {
                    alert(response.data.message);
                    fetchReviews(currentPage, searchTerm); // Refresh the review list
                })
                .catch(error => {
                    console.error('There was an error deleting the review!', error);
                });
        }
    };

    return (
        <div className="container">
            <h1>고객 리뷰</h1>
            <div className="search-container">
                <input
                    type="text"
                    className="search-input"
                    placeholder="도서명을 입력하세요"
                    value={searchTerm}
                    onChange={handleSearch}
                />
            </div>
            <div id="reviews">
                {reviewData.map((review, index) => (
                    <Review key={index} review={review} onDelete={handleDelete} />
                ))}
            </div>
            <div className="pagination">
                <button
                    onClick={() => handlePageChange(currentPage - 1)}
                    disabled={currentPage === 1}
                >
                    이전 페이지
                </button>

                <span>현재 페이지: {currentPage} / 총 페이지: {totalPages}</span>

                <button
                    onClick={() => handlePageChange(currentPage + 1)}
                    disabled={currentPage === totalPages}
                >
                    다음 페이지
                </button>
            </div>
        </div>
    );
}

export default ReviewListPage;
