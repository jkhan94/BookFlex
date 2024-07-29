import React from 'react'; // 스타일 모듈 (필요시)

const ProfilePage = () => {
    return (
        <div className={styles.container}>
            <h1 className={styles.title}>Profile Page</h1>
            <p className={styles.info}>Here you can manage your profile settings.</p>
            {/* 프로필 정보 및 설정 양식 */}
        </div>
    );
};

export default ProfilePage;