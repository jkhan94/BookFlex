import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './ProfileModifyPage.module.css'; // CSS 모듈 임포트
import axiosInstance from "../../api/axiosInstance";

const ProfileModifyPage = () => {

    const [nickname, setNickname] = useState('');
    const [password, setPassword] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [address, setAddress] = useState('');
    const [errors, setErrors] = useState({});

    const navigate = useNavigate();

    const validate = () => {
        let isValid = true;
        let newErrors = {};

        if (!/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{6,}$/.test(password)) {
            newErrors.password = 'Password must be at least 6 characters long and include uppercase, lowercase, number, and special character.';
            isValid = false;
        }

        if (!/^(01[016789])-?[0-9]{3,4}-?[0-9]{4}$/.test(phoneNumber)) {
            newErrors.phoneNumber = 'Phone number must be in a valid format (e.g., 010-1234-5678).';
            isValid = false;
        }

        setErrors(newErrors);
        return isValid;
    }

    const onModify = (nickname, password, phoneNumber, address) => {
        const data = {
            nickname, password, phoneNumber, address
        }

        axiosInstance.put(`/users`, data)
            .then(response => {
                navigate('/main/profile');
            });
        alert('Profile updated successfully!');
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!validate()) {
            return;
        }
        onModify(nickname, password, phoneNumber, address);
    }

    return (
        <div className={styles.profileModifyPageContainer}>
            <h1 className={styles.profileModifyPageTitle}>Modify Your Profile</h1>
            <form id="profile-form" onSubmit={handleSubmit} className={styles.profileModifyPageForm}>
                <div className={styles.profileModifyPageFormGroup}>
                    <label htmlFor="username" className={styles.profileModifyPageLabel}>Nickname:</label>
                    <input
                        type="text"
                        id="username"
                        name="username"
                        value={nickname}
                        onChange={(e) => setNickname(e.target.value)}
                        required
                        className={styles.profileModifyPageInput}
                    />
                </div>
                <div className={styles.profileModifyPageFormGroup}>
                    <label htmlFor="password" className={styles.profileModifyPageLabel}>Password:</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        className={styles.profileModifyPageInput}
                    />
                    {errors.password && <p className={styles.profileModifyPageError}>{errors.password}</p>}
                </div>
                <div className={styles.profileModifyPageFormGroup}>
                    <label htmlFor="phoneNumber" className={styles.profileModifyPageLabel}>Phone Number:</label>
                    <input
                        type="tel"
                        id="phoneNumber"
                        name="phoneNumber"
                        value={phoneNumber}
                        onChange={(e) => setPhoneNumber(e.target.value)}
                        required
                        className={styles.profileModifyPageInput}
                    />
                    {errors.phoneNumber && <p className={styles.profileModifyPageError}>{errors.phoneNumber}</p>}
                </div>
                <div className={styles.profileModifyPageFormGroup}>
                    <label htmlFor="address" className={styles.profileModifyPageLabel}>Address:</label>
                    <input
                        type="text"
                        id="address"
                        name="address"
                        value={address}
                        onChange={(e) => setAddress(e.target.value)}
                        required
                        className={styles.profileModifyPageInput}
                    />
                </div>
                <button type="submit" className={styles.profileModifyPageButton}>Save Changes</button>
            </form>
        </div>
    );
};

export default ProfileModifyPage;
