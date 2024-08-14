import React, { useEffect, useState } from 'react';
import axios from 'axios';
import styles from './MemberListPage.module.css';
import {useNavigate} from "react-router-dom";
import axiosInstance from "../../api/axiosInstance";
import { isAdmin } from './tokenCheck';

function MemberListPage() {
    const [members, setMembers] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const navigate = useNavigate();

    useEffect(() => {
        if(!isAdmin())
            navigate('/main/dashboard');
    },[navigate]);

    useEffect(() => {
        fetchMembers(currentPage, searchTerm);
    }, [currentPage, searchTerm]);


    const fetchMembers = (page = 1, username = '') => {
        axiosInstance.get('/users/all', {
            params: {
                page: page,
                size: 10,
                sortBy: 'createdAt',
                isAsc: true,
                username: username
            }
        })
            .then(response => {
                setMembers(response.data.content);
                setTotalPages(response.data.totalPages);
            })
            .catch(error => {
                console.error('회원 목록을 가져오는 중 오류가 발생했습니다!', error);
            });
    };
    console.log(members)
    const handleSearch = (event) => {
        setSearchTerm(event.target.value);
        setCurrentPage(1);
    };

    const handlePageChange = (direction) => {
        setCurrentPage(prevPage => prevPage + direction);
    };

    const handleClearSearch = () => {
        setSearchTerm('');
        setCurrentPage(1);
    };

    const handleProfileDetailClick = (id) => {
        navigate(`/admin/users/${id}`);
    };

    return (
        <div className={styles.container}>
            <h1>회원 목록 조회</h1>
            <div className={styles.searchBar}>
                <input
                    type="text"
                    id="searchInput"
                    placeholder="회원아이디 검색..."
                    value={searchTerm}
                    onChange={handleSearch}
                />
                {searchTerm && (
                    <button className={styles.clearButton} onClick={handleClearSearch}>&times;</button>
                )}
                <button onClick={() => fetchMembers(currentPage, searchTerm)}>검색</button>
            </div>
            <table>
                <thead>
                <tr>
                    <th>회원번호</th>
                    <th>회원ID</th>
                    <th>이름</th>
                    <th>가입일</th>
                    <th>구매금액</th>
                    <th>회원등급</th>
                    <th>회원상태</th>
                </tr>
                </thead>
                <tbody id="memberList">
                {members.map(member => (
                    <tr key={member.id}>
                        <td  className={styles.memberName}
                             onClick={() => handleProfileDetailClick(member.id)}>{member.id}</td>
                        <td
                            className={styles.memberName}
                            onClick={() => handleProfileDetailClick(member.id)}
                        >
                            {member.username}
                        </td>
                        <td  className={styles.memberName}
                             onClick={() => handleProfileDetailClick(member.id)}>{member.name}</td>
                        <td  className={styles.memberName}
                             onClick={() => handleProfileDetailClick(member.id)}>{new Date(member.createdAt).toLocaleDateString('ko-KR')}</td>
                        <td  className={styles.memberName}
                             onClick={() => handleProfileDetailClick(member.id)}>{member.purchaseTotal}원</td>
                        <td  className={styles.memberName}
                             onClick={() => handleProfileDetailClick(member.id)}>{member.grade}</td>
                        <td  className={styles.memberName}
                             onClick={() => handleProfileDetailClick(member.id)}>{member.state}</td>
                    </tr>
                ))}
                </tbody>
            </table>
            <div className={styles.pagination}>
                <button
                    onClick={() => handlePageChange(-1)}
                    disabled={currentPage === 1}
                >
                    이전
                </button>
                <button
                    onClick={() => handlePageChange(1)}
                    disabled={currentPage === totalPages}
                >
                    다음
                </button>
            </div>
        </div>
    );
}

export default MemberListPage;
