import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './ProfilePage.module.css'; // CSS 모듈 임포트
import axiosInstance from "../../api/axiosInstance";

const ProfilePage = () => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [address, setAddress] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [nickname, setNickname] = useState('');
    const [grade, setGrade] = useState('');
    const [birthday, setBirthday] = useState('');
    const [createdAt, setCreatedAt] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const getProfileResDto = async () => {
            try {
                const response = await axiosInstance.get(`/users`, {});
                setUsername(response.data.username);
                setEmail(response.data.email);
                setAddress(response.data.address);
                setPhoneNumber(response.data.phoneNumber);
                setNickname(response.data.nickname);
                setGrade(response.data.grade);
                setBirthday(response.data.birthday);
                setCreatedAt(response.data.createdAt);
            } catch (error) {
                console.error("Profile Check error", error.response ? error.response.data : error.message);
                alert('프로필 조회에 실패하였습니다. 다시 시도해 주세요.');
                if (error.response?.status === 401) {
                    navigate('/login'); // 토큰이 만료되어 재발급에도 실패한 경우 로그인 페이지로 리디렉션
                }
            }
        };

        getProfileResDto();
    }, [navigate]);

    const modifyBtnClick = (e) => {
        navigate('/main/profile-modify');
    }

    return (
        <div className={styles.container}>
            <h1 className={styles.title}>Profile Page</h1>
            <p className={styles.info}>Here you can manage your profile settings.</p>
            <div className={styles.profile}>
                <div className={styles.field}>
                    <label className={styles.label}>Username:</label>
                    <span className={styles.value}>{username}</span>
                </div>
                <div className={styles.field}>
                    <label className={styles.label}>Email:</label>
                    <span className={styles.value}>{email}</span>
                </div>
                <div className={styles.field}>
                    <label className={styles.label}>Address:</label>
                    <span className={styles.value}>{address}</span>
                </div>
                <div className={styles.field}>
                    <label className={styles.label}>Phone Number:</label>
                    <span className={styles.value}>{phoneNumber}</span>
                </div>
                <div className={styles.field}>
                    <label className={styles.label}>Nickname:</label>
                    <span className={styles.value}>{nickname}</span>
                </div>
                <div className={styles.field}>
                    <label className={styles.label}>Grade:</label>
                    <span className={styles.value}>{grade}</span>
                </div>
                <div className={styles.field}>
                    <label className={styles.label}>Birthday:</label>
                    <span className={styles.value}>{birthday}</span>
                </div>
                <div className={styles.field}>
                    <label className={styles.label}>Created At:</label>
                    <span className={styles.value}>{createdAt}</span>
                </div>
            </div>
            <button className="modify-button" onClick={modifyBtnClick}>Profile Modify</button>
        </div>
    );
};

export default ProfilePage;
