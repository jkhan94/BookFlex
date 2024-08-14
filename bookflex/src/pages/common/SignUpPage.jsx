import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
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
            newErrors.username = '유저명은 2-10자의 소문자 또는 숫자로 입력해야 합니다.';
            isValid = false;
        }

        // Password validation
        if (!/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{6,}$/.test(password)) {
            newErrors.password = '비밀번호는 최소 6자 이상이며, 대문자, 소문자, 숫자 및 특수문자를 포함해야 합니다.';
            isValid = false;
        }

        // Email validation
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            newErrors.email = '올바른 이메일 형식을 입력해야 합니다.';
            isValid = false;
        }

        // Name validation
        if (!name) {
            newErrors.name = '이름을 입력해야 합니다.';
            isValid = false;
        }

        // Phone number validation
        if (!/^(01[016789])-?[0-9]{3,4}-?[0-9]{4}$/.test(phoneNumber)) {
            newErrors.phoneNumber = '전화번호는 유효한 형식(예: 010-1234-5678)이어야 합니다.';
            isValid = false;
        }

        // Birthday validation
        if (!birthday) {
            newErrors.birthday = '생년월일을 입력해야 합니다.';
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
                ...(authType === 'ADMIN' && {adminToken}) // Include adminToken only if authType is ADMIN
            });

            // Sign-up 성공 후 로그인 페이지로 이동
            navigate('/login');
        } catch (error) {
            if (error.response.data.message === '인가되지 않은 사용자입니다.') {
                let newErrors = {};
                newErrors.adminToken = '잘못된 admin 토큰입니다';
                setErrors(newErrors);
            } else if (error.response.data.message === '이미 존재하는 유저입니다.') {
                alert('이미 존재하는 유저ID 입니다. 다시 시도해 주세요.');
            } else {
                alert('회원가입에 실패하였습니다. 다시 시도해 주세요.');
            }
            console.error('Sign up error:', error.response ? error.response.data : error.message);
        }
    };

    return (
        <div className={styles.container}>
            <h2 className={styles.title}>회원가입</h2>
            <form onSubmit={handleSignUp} className={styles.form}>
                <div className={styles.formGroup}>
                    <label htmlFor="username">유저명:</label>
                    <input
                        id="username"
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                    {/* 안내 메시지 */}
                    <p className={styles.info}>유저명은 2-10자의 소문자 또는 숫자로 입력해야 합니다.</p>
                    {errors.username && <p className={styles.error}>{errors.username}</p>}
                </div>
                <div className={styles.formGroup}>
                    <label htmlFor="password">비밀번호:</label>
                    <input
                        id="password"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                    {/* 안내 메시지 */}
                    <p className={styles.info}>비밀번호는 최소 6자 이상이며, 대문자, 소문자, 숫자 및 특수문자를 포함해야 합니다.</p>
                    {errors.password && <p className={styles.error}>{errors.password}</p>}
                </div>
                <div className={styles.formGroup}>
                    <label htmlFor="email">이메일:</label>
                    <input
                        id="email"
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                    {/* 안내 메시지 */}
                    <p className={styles.info}>올바른 이메일 형식을 입력해야 합니다.</p>
                    {errors.email && <p className={styles.error}>{errors.email}</p>}
                </div>
                <div className={styles.formGroup}>
                    <label htmlFor="name">이름:</label>
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
                    <label htmlFor="address">주소:</label>
                    <input
                        id="address"
                        type="text"
                        value={address}
                        onChange={(e) => setAddress(e.target.value)}
                    />
                </div>
                <div className={styles.formGroup}>
                    <label htmlFor="phoneNumber">전화번호:</label>
                    <input
                        id="phoneNumber"
                        type="text"
                        value={phoneNumber}
                        onChange={(e) => setPhoneNumber(e.target.value)}
                        required
                    />
                    {/* 안내 메시지 */}
                    <p className={styles.info}>전화번호는 유효한 형식(예: 010-1234-5678)이어야 합니다.</p>
                    {errors.phoneNumber && <p className={styles.error}>{errors.phoneNumber}</p>}
                </div>
                <div className={styles.formGroup}>
                    <label htmlFor="nickname">닉네임:</label>
                    <input
                        id="nickname"
                        type="text"
                        value={nickname}
                        onChange={(e) => setNickname(e.target.value)}
                    />
                </div>
                <div className={styles.formGroup}>
                    <label htmlFor="birthday">생년월일:</label>
                    <input
                        id="birthday"
                        type="date"
                        value={birthday}
                        onChange={(e) => setBirthday(e.target.value)}
                        required
                    />
                    {errors.birthday && <p className={styles.error}>{errors.birthday}</p>}
                </div>
                <div className={styles.formGroup}>
                    <label htmlFor="authType">권한:</label>
                    <select
                        id="authType"
                        value={authType}
                        onChange={(e) => setAuthType(e.target.value)}
                    >
                        <option value="USER">사용자</option>
                        <option value="ADMIN">관리자</option>
                    </select>
                </div>
                {authType === 'ADMIN' && (
                    <div className={styles.formGroup}>
                        <label htmlFor="adminToken">관리자 토큰:</label>
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
                <button type="submit" className={styles.button}>회원가입</button>
            </form>
        </div>
    );
};

export default SignUpPage;
