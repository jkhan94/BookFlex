import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './login.module.css'; // CSS 모듈 임포트
import axiosInstance from "../../api/axiosInstance"; // 수정된 Axios 인스턴스 임포트

const LoginPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const onLogin = (username, password) => {
        const data = {
            username,
            password,
        };

        axiosInstance.post('/auth/login', data)
            .then(response => {
                const { accessToken, refreshToken, auth } = response.data;

                // 로그인 성공 후, 모든 axios 요청에 accessToken을 자동으로 포함시키기 위해 설정
                //axiosInstance.defaults.headers.common['Authorization'] = accessToken;
                console.log('Login successful!', accessToken);
                // 토큰을 localStorage에 저장 (선택 사항)
                localStorage.setItem('Authorization', accessToken);
                localStorage.setItem('refreshToken', refreshToken);

                console.log('Authorization header after login:', axiosInstance.defaults.headers.common['Authorization']);

                // 추가적인 처리 (예: 역할에 따라 리디렉션)
                if (auth === 'ADMIN') {
                    navigate('/admin');
                } else {
                    navigate('/main/dashboard');
                }

            })
            .catch(error => {
                // 에러 처리
                console.error('Login error:', error);
                const errorMessage = error.response?.data?.message || '로그인에 실패하였습니다. 사용자 이름 또는 비밀번호를 확인하세요.';
                setError(errorMessage);
            });
    };

    const handleLogin = (e) => {
        e.preventDefault(); // 폼 제출 기본 동작 방지
        onLogin(username, password);
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
