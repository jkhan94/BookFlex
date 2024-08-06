import React, {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
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
    const [searchInput, setSearchInput] = useState(''); // 검색창의 입력값 상태 추가
    const [message, setMessage] = useState('');

    const navigate = useNavigate();

    useEffect(() => {
        fetchBooks();
    }, [page, size, sortBy, isAsc, status, bookName]);

    const fetchBooks = async () => {
        console.log(bookName);
        try {
            const response = await axiosInstance.get('/books', {
                params: {
                    page,
                    size,
                    sortBy,
                    direction: isAsc,
                    status,
                    bookName: bookName, // 공백 제거
                },
            });
            console.log('Response:', response); // 응답 데이터 로그 출력
            setBooks(response.data.data.content);
            setTotalPages(response.data.data.totalPages); // 총 페이지 수 업데이트
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
        if (page > 1) {
            setPage(page - 1);
        }
    };

    const handleNextPage = () => {
        console.log(page, totalPages);
        if (page < totalPages) {
            setPage(page + 1);
        }
    };

    const handleRegisterClick = () => {
        navigate('/admin/register-book');
    };

    const handleBookDetailClick = (bookId) => {
        navigate(`/admin/books/${bookId}`);
    };

    const handleSearchInputChange = (e) => {
        setSearchInput(e.target.value);
    };

    const handleSearch = () => {
        console.log(searchInput);
        setBookName(searchInput); // 검색창의 입력값을 bookName 상태로 설정, 공백 제거
    };

    const handleSortChange = (e) => {
        const value = e.target.value === 'true'; // 문자열을 불리언으로 변환
        setIsAsc(value);
    };

    const handleSortByChange = (e) => {
        const value = e.target.value;
        setSortBy(value);
    };

    const handleStatusChange = (e) => {
        const value = e.target.value;
        setStatus(value);
    };

    function renderStars(rating) {
        return '★'.repeat(rating) + '☆'.repeat(5 - rating);
    }

    return (
        <div className="book-list-container">
            <h1 className="title">상품 목록</h1> {/* 타이틀 추가 */}
            <nav className="navbar">
                <div className="search-login">
                    <input
                        type="text"
                        placeholder="상품명으로 검색해보세요."
                        value={searchInput}
                        onChange={handleSearchInputChange}
                    />
                    <button onClick={handleSearch}>Search</button>
                </div>
            </nav>

            <div className="filter-container">
                <button onClick={handleRegisterClick} className="register-button">상품 등록</button>
                <div className="sort-options">
                    <label>
                        <input
                            type="radio"
                            name="sortBy"
                            value="createdAt"
                            checked={sortBy === 'createdAt'}
                            onChange={handleSortByChange}
                        />
                        등록일자순
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="sortBy"
                            value="price"
                            checked={sortBy === 'price'}
                            onChange={handleSortByChange}
                        />
                        가격순
                    </label>
                </div>
                <select onChange={handleSortChange} className="sort-dropdown">
                    <option value="true">오름차순</option>
                    <option value="false">내림차순</option>
                </select>
                <select onChange={handleStatusChange} className="status-dropdown">
                    <option value="">전체 조회</option>
                    <option value="ONSALE">판매 중</option>
                    <option value="SOLDOUT">품절</option>
                </select>
            </div>

            <table className="book-table">
                <thead>
                <tr>
                    <th>IMAGE</th>
                    <th>상품명</th>
                    <th>대분류</th>
                    <th>소분류</th>
                    <th>가격</th>
                    <th>재고 수량</th>
                    <th>등록 일자</th>
                    <th>상태</th>
                    <th>평점</th>
                </tr>
                </thead>
                <tbody>
                {books.map((book) => (
                    <tr key={book.id}>
                        <td>
                            <img className="book-image" src={book.photoImagePath} alt={book.name}/>
                        </td>
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
                        <td>{new Date(book.createdAt).toLocaleString()}</td>
                        <td>{book.status}</td>
                        <td className="stars">{renderStars(parseInt(book.avgStar, 10))}</td>

                    </tr>
                ))}
                </tbody>
            </table>
            <div className="pagination">
                <button onClick={handlePreviousPage} disabled={page === 1}>이전</button>
                <span>{page} / {totalPages}</span>
                <button onClick={handleNextPage} disabled={page === totalPages}>다음</button>
            </div>
            {message && <p>{message}</p>}
        </div>
    );
};

export default BookList;
