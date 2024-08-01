import React from 'react';
import styles from './signup.module.css'; // CSS 모듈 import

const Signup = () => {
    return (
        <div className={styles.container}>
            <form className={styles.form}>
                <h1 className={styles.title}>Sign Up</h1>

                <div className={styles.formGroup}>
                    <label className={styles.label} htmlFor="username">Username</label>
                    <input
                        className={styles.input}
                        type="text"
                        id="username"
                        name="username"
                    />
                    {/* Example error message */}
                    <div className={styles.error}>This field is required.</div>
                </div>

                <div className={styles.formGroup}>
                    <label className={styles.label} htmlFor="email">Email</label>
                    <input
                        className={styles.input}
                        type="email"
                        id="email"
                        name="email"
                    />
                </div>

                <div className={styles.formGroup}>
                    <label className={styles.label} htmlFor="password">Password</label>
                    <input
                        className={styles.input}
                        type="password"
                        id="password"
                        name="password"
                    />
                </div>

                <button className={styles.button} type="submit">Sign Up</button>
            </form>
        </div>
    );
};

export default Signup;
