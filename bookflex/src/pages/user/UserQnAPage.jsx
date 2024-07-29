import React, {useEffect, useState} from 'react';
import styles from './userqna.module.css';
import axiosInstance from "../../api/axiosInstance";
import {useNavigate} from 'react-router-dom';

// QnaItem 컴포넌트 정의
const QnaItem = ({qna}) => (
    <div className="qna-item">
        <p className="qna-item-field">
            {qna.qnaType} &nbsp;
            {qna.inquiry} &nbsp;
            {qna.createdAt} &nbsp;
            {qna.reply}
        </p>
    </div>
);

const PAGE_SIZE = 5; // 페이지 당 QnA 항목 수

const UserQnaPage = () => {
    const [qnaList, setQnaList] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [hasMore, setHasMore] = useState(true); // 더 많은 데이터가 있는지 확인

    const navigate = useNavigate();

    useEffect(() => {
        const fetchQnaList = async () => {
            const token = localStorage.getItem('Authorization');
            try {
                const response = await axiosInstance.get('/qnas', {
                    headers: {
                        Authorization: token,
                        'Content-Type': 'application/json',
                    },
                    params: {
                        page: currentPage,
                        size: PAGE_SIZE,
                        sortBy: 'createdAt'
                    }
                });

                if (Array.isArray(response.data.data)) {
                    setQnaList(response.data.data);
                    // 반환된 데이터가 PAGE_SIZE보다 작으면 더 이상 데이터가 없다고 판단
                    setHasMore(response.data.data.length === PAGE_SIZE);
                } else {
                    throw new Error('API response is not an array');
                }
            } catch (err) {
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        fetchQnaList();
    }, [currentPage]);

    const handlePreviousPage = () => {
        if (currentPage > 1) {
            setCurrentPage(currentPage - 1);
        }
    };

    const handleNextPage = () => {
        if (hasMore) {
            setCurrentPage(currentPage + 1);
        }
    };

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error: {error.message}</div>;
    }

    const createQna = () => {
        navigate('/main/qna/createqna');
    };

    return (
        <div>
            <h1>QnA List</h1>
            <ul className={styles.qnaspan}>
                {qnaList.map(qna => (
                    <QnaItem key={qna.qnaId} qna={qna}/>
                ))}
            </ul>
            <div className={styles.pagination}>
                <button onClick={handlePreviousPage} disabled={currentPage === 1}>Previous</button>
                <span>Page {currentPage}</span>
                <button onClick={handleNextPage} disabled={!hasMore}>Next</button>
            </div>
            <br></br>
            <div>
                <button type="button" onClick={createQna}>문의 남기기</button>
            </div>
        </div>
    );
};

export default UserQnaPage;
