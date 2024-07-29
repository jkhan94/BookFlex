import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './login.module.css'; // CSS 모듈 임포트
import axiosInstance from "../../api/axiosInstance"; // 수정된 Axios 인스턴스 임포트

const LoginPage = ({ onLogin }) => { // onLogin prop 추가
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault(); // 폼 제출 기본 동작 방지

        try {
            const response = await axiosInstance.post('/auth/login', {
                username,
                password,
            });

            // 로그인 성공 시 처리 로직
            console.log('Login successful!', response.data.accessToken);

            // 토큰을 localStorage에 저장
            const token = response.data.accessToken;
            localStorage.setItem('Authorization', token);

            console.log('Response Data:', response.data);
            // 응답에서 역할 정보 추출
            const { auth } = response.data.data;
            console.log(auth);

            // 역할에 따라 리디렉션
            if (auth === 'ADMIN') {
                onLogin('admin'); // onLogin prop 호출
                navigate('/admin');
            } else {
                onLogin('user'); // onLogin prop 호출
                navigate('/main');
            }
        } catch (err) {
            // 로그인 실패 시 에러 처리
            console.error('Login failed:', err);
            const errorMessage = err.response?.data?.message || '로그인에 실패하였습니다. 사용자 이름 또는 비밀번호를 확인하세요.';
            setError(errorMessage);
        }
    };

    const handleSignUp = () => {
        navigate('/signup');
    };

    return (
        <div className={styles.container}>
            <h2 className={styles.title}>Book Flex</h2>
            <form onSubmit={handleLogin}>
                <label>
                    Username:
                    <input
                        className={styles.input}
                        type="text"
                        placeholder="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </label>
                <br />
                <label>
                    Password:
                    <input
                        className={styles.input}
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </label>
                <br />
                {error && <p className={styles.error}>{error}</p>}
                <button className={styles.button} type="submit">Login</button>
                <button className={styles.secondaryButton} type="button" onClick={handleSignUp}>Sign Up</button>
            </form>
        </div>
    );
};

export default LoginPage;
