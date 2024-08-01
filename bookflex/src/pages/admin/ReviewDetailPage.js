import React, {useEffect, useState} from 'react';
import axiosInstance from '../../api/axiosInstance';
import {useParams, useNavigate} from 'react-router-dom';
import './ReviewDetailPage.css'; // Add styles if needed

function renderStars(rating) {
    return '★'.repeat(rating) + '☆'.repeat(5 - rating);
}

function ReviewDetailPage() {
    const {reviewId} = useParams(); // Get review ID from URL parameters
    const [review, setReview] = useState(null);
    const [currentUser, setCurrentUser] = useState(null); // Current logged-in user info
    const navigate = useNavigate();

    // Fetch review details
    useEffect(() => {
        axiosInstance.get(`/reviews/${reviewId}`)
            .then(response => {
                setReview(response.data.data); // Adjust based on actual API response structure
            })
            .catch(error => {
                console.error('Error fetching the review details:', error);
            });
    }, [reviewId]);

    // Fetch current user information
    useEffect(() => {
        axiosInstance.get('/users') // Adjust endpoint as needed
            .then(response => {
                setCurrentUser(response.data.data);
            })
            .catch(error => {
                console.error('Error fetching the current user data:', error);
            });
    }, []);

    // Delete review with confirmation
    const handleDelete = () => {
        if (window.confirm('리뷰를 삭제하시겠습니까?')) {
            axiosInstance.delete(`/reviews/${reviewId}`)
                .then(() => {
                    navigate('/admin/Review-List'); // Redirect to reviews list page after deletion
                })
                .catch(error => {
                    console.error('Error deleting the review:', error);
                });
        }
    };

    // Render loading state or review details
    if (!review) {
        return <div>Loading...</div>;
    }

    return (
        <div className="review-detail-container">
            <h1>리뷰 상세</h1>
            <div className="review-detail">
                <h2 className="review-title">제목: {review.title}</h2>
                <div className="review-header">
                    <span className="book-name">도서명: {review.bookName}</span>
                    <span className="stars">{renderStars(parseInt(review.star, 10))}</span>
                </div>
                <h3 className="review-writer">작성자: {review.nickname}</h3>
                <p className="review-content">내용: {review.content}</p>
                <div className="review-footer">
                    <span>작성일: {new Date(review.createdAt).toLocaleDateString('ko-KR', {
                        year: 'numeric',
                        month: 'long',
                        day: 'numeric'
                    })}</span>
                    {/* Show delete button only if current user is the author */}
                    <button className="delete-button" onClick={handleDelete}>삭제</button>
                </div>
            </div>
        </div>
    );
}

export default ReviewDetailPage;
