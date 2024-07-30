import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from "../../api/axiosInstance";
import styles from './userqna.module.css';

// QnaItem 컴포넌트 정의
const QnaItem = ({ qna, onDeleteClick }) => (
    <tr>
        <td>{qna.qnaType}</td>
        <td>{qna.inquiry}</td>
        <td>{qna.createdAt}</td>
        <td>{qna.reply}</td>
        <td>
            {qna.reply === '답변대기' && (
                <button onClick={() => onDeleteClick(qna.qnaId)}>삭제하기</button>
            )}
        </td>
    </tr>
);

const PAGE_SIZE = 5; // 페이지 당 QnA 항목 수

const UserQnaPage = () => {
    const [qnaList, setQnaList] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [hasMore, setHasMore] = useState(true); // 더 많은 데이터가 있는지 확인
    const [showPopup, setShowPopup] = useState(false); // 팝업 열기/닫기 상태
    const [email, setEmail] = useState('');
    const [qnaType, setQnaType] = useState('');
    const [inquiry, setInquiry] = useState('');
    const [qnaTypes, setQnaTypes] = useState([]); // 문의 유형 데이터
    const [successMessage, setSuccessMessage] = useState(''); // 성공 메시지

    useEffect(() => {
        const token = localStorage.getItem('Authorization');

        const fetchQnaList = async () => {
            setLoading(true);
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

        const fetchQnaTypes = async () => {
            try {
                const response = await axiosInstance.get('/qnas/types', {
                    headers: {
                        Authorization: token,
                        'Content-Type': 'application/json',
                    },
                });
                setQnaTypes(response.data.data);
            } catch (err) {
                console.error('Failed to fetch Qna Types', err);
                setError('문의 유형을 불러오지 못했습니다.');
            }
        };

        fetchQnaList();
        fetchQnaTypes();
    }, [currentPage]);

    const fetchQnaList = async () => {
        const token = localStorage.getItem('Authorization');
        setLoading(true);
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

    const handleDeleteClick = (qnaId) => {
        if (window.confirm('정말로 이 문의를 삭제하시겠습니까?')) {
            deleteQna(qnaId);
        }
    };

    const deleteQna = async (qnaId) => {
        const token = localStorage.getItem('Authorization');
        try {
            await axiosInstance.delete(`/qnas/${qnaId}`, {
                headers: {
                    Authorization: token,
                    'Content-Type': 'application/json',
                }
            });

            fetchQnaList(); // 삭제 후 QnA 목록을 새로 고침
        } catch (err) {
            setError('문의 삭제에 실패했습니다.');
        }
    };

    const handleSubmitQna = async (e) => {
        e.preventDefault();

        if (!email || !qnaType || !inquiry) {
            setError('모든 필드를 입력해주세요.');
            return;
        }

        const token = localStorage.getItem('Authorization');
        const requestData = { email, qnaType, inquiry };

        try {
            const response = await axiosInstance.post('/qnas', requestData, {
                headers: {
                    Authorization: token,
                    'Content-Type': 'application/json',
                },
            });
            setSuccessMessage(response.data.message || '문의가 성공적으로 제출되었습니다.');
            setEmail('');
            setQnaType('');
            setInquiry('');
            setError('');
            fetchQnaList(); // 새로운 문의가 성공적으로 제출된 후 QnA 목록을 새로 고침
        } catch (err) {
            setError('문의 제출에 실패했습니다.');
        } finally {
            setShowPopup(false);
        }
    };

    const handleCloseSuccessMessage = () => {
        setSuccessMessage('');
    };

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error: {error.message}</div>;
    }

    return (
        <div>
            <h1>QnA List</h1>
            <table className={styles.qnaTable}>
                <thead>
                <tr>
                    <th>문의 유형</th>
                    <th>문의 내용</th>
                    <th>생성일</th>
                    <th>답변</th>
                    <th>삭제</th> {/* 삭제 열 추가 */}
                </tr>
                </thead>
                <tbody>
                {qnaList.map(qna => (
                    <QnaItem
                        key={qna.qnaId}
                        qna={qna}
                        onDeleteClick={handleDeleteClick}
                    />
                ))}
                </tbody>
            </table>
            <div className={styles.pagination}>
                <button onClick={handlePreviousPage} disabled={currentPage === 1}>Previous</button>
                <span>Page {currentPage}</span>
                <button onClick={handleNextPage} disabled={!hasMore}>Next</button>
            </div>
            <br />
            <button type="button" onClick={() => setShowPopup(true)}>문의 남기기</button>

            {showPopup && (
                <div className={styles.popup}>
                    <div className={styles.popupContent}>
                        <h2>문의 남기기</h2>
                        <form onSubmit={handleSubmitQna}>
                            <div className={styles.formGroup}>
                                <label htmlFor="email">이메일</label>
                                <input
                                    type="email"
                                    id="email"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    required
                                />
                            </div>
                            <div className={styles.formGroup}>
                                <label htmlFor="qnaType">문의 유형</label>
                                <select
                                    id="qnaType"
                                    value={qnaType}
                                    onChange={(e) => setQnaType(e.target.value)}
                                    required
                                >
                                    <option value="">유형 선택</option>
                                    {qnaTypes.map(type => (
                                        <option key={type} value={type}>
                                            {type}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div className={styles.formGroup}>
                                <label htmlFor="inquiry">문의 내용</label>
                                <textarea
                                    id="inquiry"
                                    value={inquiry}
                                    onChange={(e) => setInquiry(e.target.value)}
                                    required
                                />
                            </div>
                            <button type="submit">문의하기</button>
                            <button type="button" onClick={() => setShowPopup(false)}>닫기</button>
                        </form>
                        {error && <div className={styles.error}>{error}</div>}
                    </div>
                </div>
            )}

            {successMessage && (
                <div className={styles.confirmModal}>
                    <div className={styles.confirmModalContent}>
                        <p>{successMessage}</p>
                        <button onClick={handleCloseSuccessMessage}>확인</button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default UserQnaPage;
