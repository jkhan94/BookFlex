import React, { useEffect, useState } from 'react';
import axios from 'axios';
import axiosInstance from "../../api/axiosInstance";

const BestSellerPage = () => {
    const [bestSellers, setBestSellers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchBestSellers = async () => {
            try {
                const response = await axiosInstance.get('/sales/bestseller');
                setBestSellers(response.data.data);  // Assuming the data you need is in response.data.data
            } catch (error) {
                setError('베스트셀러 정보를 가져오는 데 실패했습니다.');
            } finally {
                setLoading(false);
            }
        };

        fetchBestSellers();
    }, []);

    if (loading) return <p>로딩 중...</p>;
    if (error) return <p>{error}</p>;

    return (
        <div>
            <h1>베스트셀러</h1>
            <ul>
                {bestSellers.map((book) => (
                    <li key={book.bookId}>
                        <img src={book.imagePath} alt={book.bookName} style={{ width: '100px', height: '150px' }} />
                        <h2>{book.bookName}</h2>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default BestSellerPage;
