import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from "../../api/axiosInstance";
import './BookList.css'; // 필요한 CSS 파일을 가져옵니다.

const BookList = () => {
    const [books, setBooks] = useState([]);
    const [page, setPage] = useState(1);
    const [size, setSize] = useState(10);
    const [totalPages, setTotalPages] = useState(10); // 총 페이지 수 상태 추가
    const [sortBy, setSortBy] = useState('createdAt');
    const [isAsc, setIsAsc] = useState(true);
    const [status, setStatus] = useState('ONSALE');
    const [bookName, setBookName] = useState('');
    const [message, setMessage] = useState('');
    const [accessToken, setAccessToken] = useState(''); // 액세스 토큰 상태 추가

    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('Authorization');
        setAccessToken(token);

        fetchBooks();
    }, [page, size, sortBy, isAsc, status, bookName]);

    const fetchBooks = async () => {
        try {
            const response = await axiosInstance.get('/books', {
                headers: {
                    Authorization: accessToken
                },
                params: {
                    page,
                    size,
                    sortBy,
                    direction: isAsc,
                    status,
                    bookName,
                },
            });
            console.log('Response:', response); // 응답 데이터 로그 출력
            setBooks(response.data.data);
            setTotalPages(response.data.totalPages); // 총 페이지 수 업데이트
            setMessage(response.data.message);
        } catch (error) {
            console.error('Error fetching books:', error); // 오류 로그 출력
            if (error.response && error.response.status === 404) {
                setBooks([]);
                setMessage('등록된 상품이 존재하지 않습니다.');
            } else {
                setMessage('상품 조회에 실패하였습니다.');
            }
        }
    };

    const handlePreviousPage = () => {
        if (page > 0) {
            setPage(page - 1);
        }
    };

    const handleNextPage = () => {
        if (page < totalPages - 1) {
            setPage(page + 1);
        }
    };

    const handleRegisterClick = () => {
        navigate('/register-book');
    };

    const handleBookDetailClick = (bookId) => {
        navigate(`/admin/books/${bookId}`);
    };

    return (
        <div className="book-list-container">
            <nav className="navbar">
                <ul className="nav-links">
                    <li>Main</li>
                    <li>New</li>
                    <li>Best</li>
                    <li>Category</li>
                    <li>Customer</li>
                    <li>Info</li>
                </ul>
                <div className="search-login">
                    <input type="text" placeholder="Search" />
                    <button>Search</button>
                    <button>로그인</button>
                    <button>회원가입</button>
                </div>
            </nav>

            <button onClick={handleRegisterClick} className="register-button">상품 등록</button>

            <table className="book-table">
                <thead>
                <tr>
                    <th>IMAGE</th>
                    <th>상품명</th>
                    <th>대분류</th>
                    <th>소분류</th>
                    <th>가격</th>
                    <th>재고 수량</th>
                </tr>
                </thead>
                <tbody>
                {books.map((book) => (
                    <tr key={book.id}>
                        <td><input type="checkbox"/><img src={book.photoImagePath} alt={book.name}/></td>
                        <td>
                            <span
                                className="book-name"
                                onClick={() => handleBookDetailClick(book.bookId)}
                            >
                                {book.bookName}
                            </span>
                        </td>
                        <td>{book.mainCategoryName}</td>
                        <td>{book.subCategoryName}</td>
                        <td>{book.price.toLocaleString()}</td>
                        <td>{book.stock}</td>
                    </tr>
                ))}
                </tbody>
            </table>
            <div className="pagination">
                <button onClick={handlePreviousPage} disabled={page === 0}>이전</button>
                <span>{page} / {totalPages}</span>
                <button onClick={handleNextPage} disabled={page === totalPages - 1}>다음</button>
            </div>
        </div>
    );
};

export default BookList;
