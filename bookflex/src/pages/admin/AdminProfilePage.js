import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axiosInstance from '../../api/axiosInstance';
import styles from './AdminProfilePage.module.css';

function AdminProfilePage() {
  const [profile, setProfile] = useState(null);
  const { userId } = useParams();

  const navigate = useNavigate();

  useEffect(() => {
    fetchProfile();
  }, [userId]);

  const fetchProfile = () => {
    axiosInstance.get(`/users/${userId}`)
        .then(response => {
          setProfile(response.data);
        })
        .catch(error => {
          console.error('프로필 정보를 가져오는 중 오류가 발생했습니다!', error);
        });
  };

  const handleEditClick = (id) => {
    navigate(`/admin/users/${id}/edit`);
  };

  if (!profile) {
    return <div>로딩 중...</div>;
  }

  return (
      <div className={styles.container}>
        <h1>회원정보</h1>
        <div className={styles.profileInfo}>
          <h2>{profile.name}</h2>
        </div>
        <ul className={styles.infoList}>
          <li className={styles.infoItem}>
            <span className={styles.infoLabel}>아이디</span>
            <span className={styles.infoValue}>{profile.username}</span>
          </li>
          <li className={styles.infoItem}>
            <span className={styles.infoLabel}>닉네임</span>
            <span className={styles.infoValue}>{profile.nickname}</span>
          </li>
          <li className={styles.infoItem}>
            <span className={styles.infoLabel}>이메일</span>
            <span className={styles.infoValue}>{profile.email}</span>
          </li>
          <li className={styles.infoItem}>
            <span className={styles.infoLabel}>전화번호</span>
            <span className={styles.infoValue}>{profile.phoneNumber}</span>
          </li>
          <li className={styles.infoItem}>
            <span className={styles.infoLabel}>주소</span>
            <span className={styles.infoValue}>{profile.address}</span>
          </li>
          <li className={styles.infoItem}>
            <span className={styles.infoLabel}>생년월일</span>
            <span className={styles.infoValue}>{profile.birthday}</span>
          </li>
          <li className={styles.infoItem}>
            <span className={styles.infoLabel}>회원 등급</span>
            <span className={styles.infoValue}>{profile.grade}</span>
          </li>
          <li className={styles.infoItem}>
            <span className={styles.infoLabel}>회원 상태</span>
            <span className={styles.infoValue}>{profile.userState}</span>
          </li>
          <li className={styles.infoItem}>
            <span className={styles.infoLabel}>가입일</span>
            <span className={styles.infoValue}>{new Date(profile.createdAt).toLocaleDateString('ko-KR')}</span>
          </li>
        </ul>
        <button className={styles.editBtn} onClick={()=>handleEditClick(profile.id)}>
          정보 수정
        </button>
      </div>
  );
}

export default AdminProfilePage;
