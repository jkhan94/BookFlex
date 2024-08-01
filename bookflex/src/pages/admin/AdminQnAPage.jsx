import React, {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import axiosInstance from "../../api/axiosInstance";
import styles from "../admin/adminqna.module.css";

// QnaItem 컴포넌트 정의
const QnaItem = ({qna, onReplyClick, onDeleteClick}) => (
    <tr>
        <td>{qna.qnaType}</td>
        <td>{qna.inquiry}</td>
        <td>{qna.createdAt}</td>
        <td>{qna.reply}</td>
        <td>
            {qna.reply === '답변대기' && (
                <div className={styles.buttonContainer}>
                    <button onClick={() => onReplyClick(qna)}>답변하기</button>
                    <button onClick={() => onDeleteClick(qna.qnaId)}>삭제하기</button>
                </div>
            )}
        </td>
    </tr>
);

const PAGE_SIZE = 5; // 페이지 당 QnA 항목 수

const AdminQnAPage = () => {
    const [qnaList, setQnaList] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1); // 1부터 시작
    const [totalPages, setTotalPages] = useState(1); // 전체 페이지 수
    const [selectedQna, setSelectedQna] = useState(null); // 선택된 QnA 저장
    const [replyText, setReplyText] = useState('');
    const [isReplyModalOpen, setIsReplyModalOpen] = useState(false); // 팝업 열기/닫기 상태

    const navigate = useNavigate();

    const fetchQnaList = async () => {
        const token = localStorage.getItem('Authorization');
        setLoading(true);
        try {
            const response = await axiosInstance.get('/qnas/admin', {
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

            if (response.data) {
                setQnaList(response.data.content);
                setTotalPages(response.data.totalPages);
            } else {
                throw new Error('API response is not an array');
            }
        } catch (err) {
            setError(err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchQnaList();
    }, [currentPage]);

    const handlePreviousPage = () => {
        if (currentPage > 1) {
            setCurrentPage(currentPage - 1);
        }
    };

    const handleNextPage = () => {
        if (currentPage < totalPages) {
            setCurrentPage(currentPage + 1);
        }
    };

    const handleReplyClick = (qna) => {
        setSelectedQna(qna);
        setReplyText(''); // 답변 텍스트 초기화
        setIsReplyModalOpen(true);
    };

    const handleCloseModal = () => {
        setIsReplyModalOpen(false);
        setSelectedQna(null);
    };

    const handleReplySubmit = async (e) => {
        e.preventDefault();

        if (!replyText) {
            setError('답변 내용을 입력해주세요.');
            return;
        }

        const token = localStorage.getItem('Authorization');
        try {
            await axiosInstance.post(`/qnas/admin/${selectedQna.qnaId}`, {reply: replyText}, {
                headers: {
                    Authorization: token,
                    'Content-Type': 'application/json',
                }
            });

            handleCloseModal();
            fetchQnaList(); // 답변 후 QnA 목록 새로 고침
        } catch (err) {
            setError('답변 제출에 실패했습니다.');
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
            await axiosInstance.delete(`/qnas/admin/${qnaId}`, {
                headers: {
                    Authorization: token,
                    'Content-Type': 'application/json',
                }
            });

            fetchQnaList(); // 삭제 후 QnA 목록 새로 고침
        } catch (err) {
            setError('문의 삭제에 실패했습니다.');
        }
    };

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error: {error.message}</div>;
    }

    return (
        <div>
            <h1>Admin Q&A List</h1>
            <table className={styles.qnaTable}>
                <thead>
                <tr>
                    <th>문의 유형</th>
                    <th>문의 내용</th>
                    <th>생성일</th>
                    <th>답변</th>
                    <th>작업</th>
                    {/* 작업 열 추가 */}
                </tr>
                </thead>
                <tbody>
                {qnaList.map(qna => (
                    <QnaItem
                        key={qna.qnaId}
                        qna={qna}
                        onReplyClick={handleReplyClick}
                        onDeleteClick={handleDeleteClick}
                    />
                ))}
                </tbody>
            </table>
            <div className={styles.pagination}>
                <button onClick={handlePreviousPage} disabled={currentPage === 1}>이전</button>
                <span>페이지 {currentPage} / {totalPages}</span>
                <button onClick={handleNextPage} disabled={currentPage === totalPages}>다음</button>
            </div>

            {isReplyModalOpen && (
                <div className={styles.modal}>
                    <div className={styles.modalContent}>
                        <h2>답변 추가</h2>
                        <p><strong>문의 내용:</strong> {selectedQna?.inquiry}</p>
                        <form onSubmit={handleReplySubmit}>
                            <textarea
                                value={replyText}
                                onChange={(e) => setReplyText(e.target.value)}
                                placeholder="답변을 입력하세요..."
                                rows="4"
                                required
                            />
                            <div>
                                <button type="submit">답변 제출</button>
                                <button type="button" onClick={handleCloseModal}>닫기</button>
                            </div>
                        </form>
                        {error && <div className={styles.error}>{error}</div>}
                    </div>
                </div>
            )}
        </div>
    );
};

export default AdminQnAPage;
