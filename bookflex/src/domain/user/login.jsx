import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './login.module.css'; // CSS 모듈 임포트

const LoginPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin = async () => {
        // 임시 로그인 로직 (실제 인증 로직을 적용해야 함)
        if (username === 'admin' && password === 'admin') {
            navigate('/admin');
        } else {
            navigate('/main');
        }
    };

    const handleSignUp = () => {
        navigate('/signup');
    };

    return (
        <div className={styles.container}>
            <h2 className={styles.title}>Login Page</h2>
            <input
                className={styles.input}
                type="text"
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
            />
            <input
                className={styles.input}
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <button className={styles.button} onClick={handleLogin}>Login</button>
            <button className={styles.secondaryButton} onClick={handleSignUp}>Sign Up</button>
        </div>
    );
};

export default LoginPage;
