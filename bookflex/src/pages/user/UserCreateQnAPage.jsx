import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from "../../api/axiosInstance";
import styles from './usercreateqna.module.css';

const UserCreateQnaPage = () => {
    const [email, setEmail] = useState('');
    const [qnaType, setQnaType] = useState('');
    const [inquiry, setInquiry] = useState('');
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState('');
    const [qnaTypes, setQnaTypes] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('Authorization');

        const fetchQnaTypes = async () => {
            try {
                const response = await axiosInstance.get('/qnas/types', {
                    headers: {
                        Authorization: token,
                        'Content-Type': 'application/json',
                    },
                });
                setQnaTypes(response.data);
            } catch (err) {
                console.error('Failed to fetch Qna Types', err);
                setError('문의 유형을 불러오지 못했습니다.');
            }
        };

        fetchQnaTypes();
    }, []);

    const createQna = async (e) => {
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
            setSuccess('문의가 접수되었습니다.');
            setError('');
            setEmail('');
            setQnaType('');
            setInquiry('');
        } catch (err) {
            setError('문의 제출에 실패했습니다.');
            setSuccess('');
        }
    };

    return (
        <div className={styles.qnaRequestForm}>
            <h1>문의하기</h1>
            {error && <div className={styles.error}>{error}</div>}
            {success && <div className={styles.success}>{success}</div>}
            <form onSubmit={createQna}>
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
                        {qnaTypes.map((type) => (
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
                <div className={styles.buttonGroup}>
                    <button className={styles.button} type="submit">문의하기</button>
                    <button className={styles.secondaryButton} type="button" onClick={() => navigate('/main/qna')}>문의내역
                        화면으로
                    </button>
                </div>
            </form>
        </div>
    );
};

export default UserCreateQnaPage;
