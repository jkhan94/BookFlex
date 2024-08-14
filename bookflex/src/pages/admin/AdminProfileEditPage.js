import React, {useState, useEffect} from 'react';
import {useParams, useNavigate} from 'react-router-dom';
import axiosInstance from '../../api/axiosInstance';
import styles from './AdminProfileEditPage.module.css';
import { isAdmin } from './tokenCheck';

function EditProfilePage() {
    const {userId} = useParams();
    const navigate = useNavigate();
    const [profile, setProfile] = useState(null);
    const [userEditRequestDto, setUserEditRequestDto] = useState({
        grade: '',
        state: '',
    });

    useEffect(() => {
        if(!isAdmin())
            navigate('/main/dashboard');
    },[navigate]);

    useEffect(() => {
        fetchProfile();
    }, [userId]);

    const fetchProfile = () => {
        axiosInstance.get(`/users/${userId}`)
            .then(response => {
                setProfile(response.data);
                setUserEditRequestDto({
                    grade: response.data.grade,
                    state: response.data.userState,
                });
            })
            .catch(error => {
                console.error('프로필 정보를 가져오는 중 오류가 발생했습니다!', error);
            });
    };

    const handleSubmit = (event) => {
        event.preventDefault();

        if (userEditRequestDto.grade === '' || userEditRequestDto.state === '') {
            alert('모든 정보를 올바르게 입력해주세요.');
            return;
        }

        axiosInstance.put(`/users/${userId}/state`, userEditRequestDto)
            .then(response => {
                alert('회원정보가 성공적으로 수정되었습니다.');
                navigate(`/admin/users/${userId}`);
            })
            .catch(error => {
                console.error('회원정보 수정 중 오류가 발생했습니다!', error);
                alert('회원정보 수정 중 오류가 발생했습니다.');
            });
    };

    const handleChange = (e) => {
        const {name, value} = e.target;
        setUserEditRequestDto((prevDto) => ({
            ...prevDto,
            [name]: value,
        }));
    };

    if (!profile) {
        return <div>로딩 중...</div>;
    }

    return (
        <div className={styles.container}>
            <h1>회원정보 수정</h1>
            <div className={styles.profileInfo}>
                <h2>{profile.name}</h2>
            </div>
            <form className={styles.form} onSubmit={handleSubmit}>
                <div className={styles.formGroup}>
                    <label className={styles.formLabel}>아이디</label>
                    <div className={styles.formValue}>{profile.username}</div>
                </div>
                <div className={styles.formGroup}>
                    <label className={styles.formLabel}>닉네임</label>
                    <div className={styles.formValue}>{profile.nickname}</div>
                </div>
                <div className={styles.formGroup}>
                    <label className={styles.formLabel}>이메일</label>
                    <div className={styles.formValue}>{profile.email}</div>
                </div>
                <div className={styles.formGroup}>
                    <label className={styles.formLabel}>전화번호</label>
                    <div className={styles.formValue}>{profile.phoneNumber}</div>
                </div>
                <div className={styles.formGroup}>
                    <label className={styles.formLabel}>주소</label>
                    <div className={styles.formValue}>{profile.address}</div>
                </div>
                <div className={styles.formGroup}>
                    <label className={styles.formLabel}>생년월일</label>
                    <div className={styles.formValue}>{profile.birthday}</div>
                </div>
                <div className={styles.formGroup}>
                    <label className={styles.formLabel}>회원 등급</label>
                    <div className={styles.formValue}>{profile.grade}</div>
                </div>
                <div className={styles.formGroup}>
                    <label className={styles.formLabel}>회원 상태</label>
                    <select
                        className={styles.formSelect}
                        name="state"
                        value={userEditRequestDto.state}
                        onChange={handleChange}
                        required
                    >
                        <option value="">상태를 선택해주세요</option>
                        <option value="ACTIVE">정상</option>
                        <option value="BAN">밴</option>
                    </select>
                </div>
                <div className={styles.formGroup}>
                    <label className={styles.formLabel}>가입일자</label>
                    <div className={styles.formValue}>{new Date(profile.createdAt).toLocaleDateString('ko-KR')}</div>
                </div>
                <button className={styles.submitBtn} type="submit">수정 완료</button>
            </form>
        </div>
    );
}

export default EditProfilePage;
