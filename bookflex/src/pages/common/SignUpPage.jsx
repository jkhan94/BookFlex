import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axiosInstance'; // axiosInstance 경로를 확인하세요
import styles from './signup.module.css'; // CSS 모듈 임포트

const SignUpPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [name, setName] = useState('');
    const [address, setAddress] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [nickname, setNickname] = useState('');
    const [birthday, setBirthday] = useState('');
    const [authType, setAuthType] = useState('USER'); // Default to USER
    const [adminToken, setAdminToken] = useState('');

    const [errors, setErrors] = useState({});

    const navigate = useNavigate();

    const validate = () => {
        let isValid = true;
        let newErrors = {};

        // Username validation
        if (!/^[a-z0-9]{2,10}$/.test(username)) {
            newErrors.username = 'Username must be 2-10 lowercase letters or digits.';
            isValid = false;
        }

        // Password validation
        if (!/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{6,}$/.test(password)) {
            newErrors.password = 'Password must be at least 6 characters long and include uppercase, lowercase, number, and special character.';
            isValid = false;
        }

        // Email validation
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            newErrors.email = 'Email must be in a valid format.';
            isValid = false;
        }

        // Name validation
        if (!name) {
            newErrors.name = 'Name is required.';
            isValid = false;
        }

        // Phone number validation
        if (!/^(01[016789])-?[0-9]{3,4}-?[0-9]{4}$/.test(phoneNumber)) {
            newErrors.phoneNumber = 'Phone number must be in a valid format (e.g., 010-1234-5678).';
            isValid = false;
        }

        // Birthday validation
        if (!birthday) {
            newErrors.birthday = 'Birthday is required.';
            isValid = false;
        }

        setErrors(newErrors);
        return isValid;
    };

    const handleSignUp = async (e) => {
        e.preventDefault();

        if (!validate()) return;

        // Convert birthday to yyyy-MM-dd format
        const formattedBirthday = new Date(birthday).toISOString().split('T')[0];

        try {
            const response = await axiosInstance.post('/auth/signup', {
                username,
                password,
                email,
                name,
                address,
                phoneNumber,
                nickname,
                birthday: formattedBirthday,
                authType,
                ...(authType === 'ADMIN' && { adminToken }) // Include aminToken only if authType is ADMINd
            });

            // Sign-up 성공 후 로그인 페이지로 이동
            navigate('/login');
        } catch (error) {
            if(error.response.data.message === '인가되지 않은 사용자입니다.' ) {
                let newErrors = {};
                newErrors.adminToken = '잘못된 admin 토큰입니다';
                setErrors(newErrors);
            }
            console.error('Sign up error:', error.response ? error.response.data : error.message);
            alert('회원가입에 실패하였습니다. 다시 시도해 주세요.');
        }
    };

    return (
        <div className={styles.container}>
            <h2 className={styles.title}>Sign Up</h2>
            <form onSubmit={handleSignUp} className={styles.form}>
                <div className={styles.formGroup}>
                    <label htmlFor="username">Username:</label>
                    <input
                        id="username"
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                    {errors.username && <p className={styles.error}>{errors.username}</p>}
                </div>
                <div className={styles.formGroup}>
                    <label htmlFor="password">Password:</label>
                    <input
                        id="password"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                    {errors.password && <p className={styles.error}>{errors.password}</p>}
                </div>
                <div className={styles.formGroup}>
                    <label htmlFor="email">Email:</label>
                    <input
                        id="email"
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                    {errors.email && <p className={styles.error}>{errors.email}</p>}
                </div>
                <div className={styles.formGroup}>
                    <label htmlFor="name">Name:</label>
                    <input
                        id="name"
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                    {errors.name && <p className={styles.error}>{errors.name}</p>}
                </div>
                <div className={styles.formGroup}>
                    <label htmlFor="address">Address:</label>
                    <input
                        id="address"
                        type="text"
                        value={address}
                        onChange={(e) => setAddress(e.target.value)}
                    />
                </div>
                <div className={styles.formGroup}>
                    <label htmlFor="phoneNumber">Phone Number:</label>
                    <input
                        id="phoneNumber"
                        type="text"
                        value={phoneNumber}
                        onChange={(e) => setPhoneNumber(e.target.value)}
                        required
                    />
                    {errors.phoneNumber && <p className={styles.error}>{errors.phoneNumber}</p>}
                </div>
                <div className={styles.formGroup}>
                    <label htmlFor="nickname">Nickname:</label>
                    <input
                        id="nickname"
                        type="text"
                        value={nickname}
                        onChange={(e) => setNickname(e.target.value)}
                    />
                </div>
                <div className={styles.formGroup}>
                    <label htmlFor="birthday">Birthday:</label>
                    <input
                        id="birthday"
                        type="date"
                        value={birthday}
                        onChange={(e) => setBirthday(e.target.value)}
                    />
                    {errors.birthday && <p className={styles.error}>{errors.birthday}</p>}
                </div>
                <div className={styles.formGroup}>
                    <label htmlFor="authType">Role:</label>
                    <select
                        id="authType"
                        value={authType}
                        onChange={(e) => setAuthType(e.target.value)}
                    >
                        <option value="USER">User</option>
                        <option value="ADMIN">Admin</option>
                    </select>
                </div>
                {authType === 'ADMIN' && (
                    <div className={styles.formGroup}>
                        <label htmlFor="adminToken">Admin Token:</label>
                        <input
                            id="adminToken"
                            type="text"
                            value={adminToken}
                            onChange={(e) => setAdminToken(e.target.value)}
                            required={authType === 'ADMIN'}
                        />
                        {errors.adminToken && <p className={styles.error}>{errors.adminToken}</p>}
                    </div>
                )}
                <button type="submit" className={styles.button}>Sign Up</button>
            </form>
        </div>
    );
};

export default SignUpPage;
