import React, { useState } from 'react';
import { useLocation,useNavigate } from 'react-router-dom';
import axiosInstance from "../../api/axiosInstance"; // axios를 사용하여 HTTP 요청을 보냅니다.


function RegisterReviewPage() {
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [star, setStar] = useState('');

    const location = useLocation();
    const navigate = useNavigate();
    const queryParams = new URLSearchParams(location.search);
    const orderId = queryParams.get('orderId');
    const itemId = queryParams.get('itemId');
    const handleSubmit = (e) => {
        e.preventDefault();

        if (!title || !content || !star) {
            alert('모든 필드를 입력해 주세요.');
            return;
        }

        const reviewData = {
            title,
            content,
            star: parseInt(star, 10),
        };

        // API 요청 보내기
        axiosInstance.post(`/sale/${itemId}/reviews`, reviewData) // 여기에 실제 saleId를 전달해야 합니다.
            .then(response => {
                alert('리뷰가 성공적으로 등록되었습니다!');
                setTitle('');
                setContent('');
                setStar('');
                navigate('/main/dashboard')
            })
            
            .catch(error => {
                console.error('리뷰 등록에 실패했습니다!', error);
            });
    };

    return (
        <div className="container">
            <h1>리뷰 등록</h1>
            <form onSubmit={handleSubmit}>
                <label htmlFor="title">도서명 : </label>
                <label htmlFor="title">제목</label>
                <input
                    type="text"
                    id="title"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    required
                />

                <label htmlFor="content">내용</label>
                <textarea
                    id="content"
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    required
                ></textarea>

                <label htmlFor="star">별점</label>
                <select
                    id="star"
                    value={star}
                    onChange={(e) => setStar(e.target.value)}
                    required
                >
                    <option value="">별점을 선택하세요</option>
                    {[5, 4, 3, 2, 1].map(value => (
                        <option key={value} value={value}>
                            {value} ★
                        </option>
                    ))}
                </select>

                <button type="submit">리뷰 등록하기</button>
            </form>
        </div>
    );
}

export default RegisterReviewPage;
