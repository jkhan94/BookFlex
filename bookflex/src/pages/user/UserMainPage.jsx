// MainPage.jsx
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from "../../api/axiosInstance";
import styles from './UserMainPage.module.css';

const MainPage = () => {
    const [bestSellers, setBestSellers] = useState([]);
    const [newBooks, setNewBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [searchInput, setSearchInput] = useState('');
    const [books, setBooks] = useState([]);
    const [page, setPage] = useState(1);
    const [size, setSize] = useState(10);
    const [totalPages, setTotalPages] = useState(1);
    const [sortBy, setSortBy] = useState('createdAt');
    const [isAsc, setIsAsc] = useState(true);
    const [status, setStatus] = useState('ONSALE');
    const [message, setMessage] = useState('');

    const navigate = useNavigate();

    useEffect(() => {
        const fetchBestSellers = async () => {
            try {
                const response = await axiosInstance.get('/books/bestseller');
                setBestSellers(response.data.data);
            } catch (error) {
                setError('베스트셀러 정보를 가져오는 데 실패했습니다.');
            } finally {
                setLoading(false);
            }
        };

        fetchBestSellers();
    }, []);

    useEffect(() => {
        const fetchNewBooks = async () => {
            try {
                const response = await axiosInstance.get('/books/new');
                setNewBooks(response.data.data);
            } catch (error) {
                setError('신규 서적 정보를 가져오는 데 실패했습니다.');
            } finally {
                setLoading(false);
            }
        };

        fetchNewBooks();
    }, []);

    useEffect(() => {
        fetchBooks();
    }, [searchInput, page, size, sortBy, isAsc, status]);

    const fetchBooks = async () => {
        try {
            const response = await axiosInstance.get('/books', {
                params: {
                    page,
                    size,
                    sortBy,
                    direction: isAsc,
                    status,
                    bookName: searchInput,
                },
            });
            setBooks(response.data.data.content);
            setTotalPages(response.data.data.totalPages);
            setMessage(response.data.message);
        } catch (error) {
            console.error('Error fetching books:', error);
            if (error.response && error.response.status === 404) {
                setBooks([]);
                setMessage('등록된 상품이 존재하지 않습니다.');
            } else {
                setMessage('상품 조회에 실패하였습니다.');
            }
        }
    };

    const handleBookDetailClick = (bookId) => {
        navigate(`/books/${bookId}`); // 상세 페이지로 이동
    };

    const handleSearchInputChange = (e) => {
        setSearchInput(e.target.value);
        setPage(1); // 검색 시 첫 페이지로 이동
    };

    const handlePreviousPage = () => {
        if (page > 1) {
            setPage(page - 1);
        }
    };

    const handleNextPage = () => {
        if (page < totalPages) {
            setPage(page + 1);
        }
    };

    const handleSortChange = (e) => {
        setIsAsc(e.target.value === 'true');
    };

    const handleSortByChange = (e) => {
        setSortBy(e.target.value);
    };

    const handleStatusChange = (e) => {
        setStatus(e.target.value);
    };

    function renderStars(rating) {
        return '★'.repeat(rating) + '☆'.repeat(5 - rating);
    }

    return (
        <div className={styles.container}>
            <div className={styles.searchContainer}>
                <input
                    type="text"
                    placeholder="책 제목으로 검색"
                    value={searchInput}
                    onChange={handleSearchInputChange}
                    className={styles.searchInput}
                />
            </div>

            <div className={styles.filterContainer}>
                <div className={styles.sortOptions}>
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
                <select onChange={handleSortChange} className={styles.sortDropdown}>
                    <option value="true">오름차순</option>
                    <option value="false">내림차순</option>
                </select>

            </div>

            <div className={styles.bookListContainer}>
                <h2>상품 목록</h2>
                <table className={styles.bookTable}>
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
                                <img className={styles.bookImage} src={book.photoImagePath} alt={book.name} />
                            </td>
                            <td>
                                    <span
                                        className={styles.bookName}
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
                            <td className={styles.stars}>{renderStars(parseInt(book.avgStar, 10))}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
                <div className={styles.pagination}>
                    <button onClick={handlePreviousPage} disabled={page === 1}>이전</button>
                    <span>{page} / {totalPages}</span>
                    <button onClick={handleNextPage} disabled={page === totalPages}>다음</button>
                </div>

            </div>


            {bestSellers.length > 0 && (
                <div className={styles.bestSellers}>
                    <h2>실시간 베스트셀러</h2>
                    <div className={styles.bestSellersGrid}>
                        {bestSellers.map((book) => (
                            <div key={book.bookId} className={styles.bestSellerItem}>
                                <span
                                    className="book-name"
                                    onClick={() => handleBookDetailClick(book.bookId)}
                                >
                                    <img
                                        src={book.imagePath}

                                        className={styles.bookImage}
                                    />
                                    <h3 className={styles.bookName}>{book.bookName}</h3>
                                </span>
                            </div>
                        ))}
                    </div>
                </div>
            )}

            {newBooks.length > 0 && (
                <div className={styles.newBooks}>
                    <h2>신규 서적</h2>
                    <div className={styles.newBooksGrid}>
                        {newBooks.map((book) => (
                            <div key={book.bookId} className={styles.newBookItem}>
                                <span
                                    className="book-name"
                                    onClick={() => handleBookDetailClick(book.bookId)}
                                >
                                    <img
                                        src={book.photoImagePath}

                                        className={styles.bookImage}
                                    />
                                    <h3 className={styles.bookName}>{book.bookName}</h3>
                                </span>
                            </div>
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
};

export default MainPage;
