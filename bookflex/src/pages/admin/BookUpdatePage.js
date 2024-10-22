import React, { useState, useEffect } from 'react';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axiosInstance';

const BookUpdatePage = () => {
    const { productId } = useParams();
    const location = useLocation();
    const initialBook = location.state?.book;
    const [bookRequestDto, setBookRequestDto] = useState({
        bookName: '',
        publisher: '',
        author: '',
        price: '',
        stock: '',
        bookDescription: '',
        status: '',
        mainCategory: '',
        subCategory: '',
    });
    const [multipartFile, setMultipartFile] = useState(null);
    const [responseMessage, setResponseMessage] = useState('');
    const [mainCategories, setMainCategories] = useState([]);
    const [subCategories, setSubCategories] = useState([]);
    const [thumbnail, setThumbnail] = useState(''); // 이미지 썸네일 상태 추가
    const navigate = useNavigate();

    useEffect(() => {
        // 주요 카테고리 로드
        const fetchMainCategories = async () => {
            try {
                const response = await axiosInstance.get('/categories/main');
                console.log('Fetched main categories:', response.data);
                setMainCategories(response.data);
            } catch (error) {
                console.error('Failed to fetch main categories', error);
                setMainCategories([]);
            }
        };

        fetchMainCategories();
    }, []);

    useEffect(() => {
        // 서브 카테고리 로드
        const fetchSubCategories = async () => {
            if (bookRequestDto.mainCategory) {
                try {
                    const response = await axiosInstance.get('/categories/sub', {
                        params: { mainCategory: bookRequestDto.mainCategory }
                    });
                    console.log('Fetched sub categories:', response.data);
                    setSubCategories(response.data);
                } catch (error) {
                    console.error('Failed to fetch sub categories', error);
                    setSubCategories([]);
                }
            } else {
                setSubCategories([]);
            }
        };

        fetchSubCategories();
    }, [bookRequestDto.mainCategory]);

    useEffect(() => {
        if (initialBook) {
            setBookRequestDto(initialBook);
            setThumbnail(initialBook.photoImagePath); // 썸네일 설정
        } else {
            const fetchBook = async () => {
                try {
                    const response = await axiosInstance.get(`/books/${productId}`);
                    const bookData = response.data.data;
                    setBookRequestDto(bookData);
                    setThumbnail(bookData.photoImagePath); // 썸네일 설정
                } catch (error) {
                    console.error('There was an error!', error);
                    setResponseMessage('상품 조회에 실패하였습니다.');
                }
            };
            fetchBook();
        }
    }, [initialBook, productId]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setBookRequestDto((prevBook) => ({
            ...prevBook,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append('request', new Blob([JSON.stringify(bookRequestDto)], { type: 'application/json' }));
        if (multipartFile) {
            formData.append('multipartFile', multipartFile);
        }

        try {
            const response = await axiosInstance.put(`/books/${productId}`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            setResponseMessage('상품 정보 수정에 성공하였습니다.');
            navigate(`/admin/books/${response.data.data.bookId}`);
        } catch (error) {
            console.error('There was an error!', error);
            setResponseMessage('상품 정보 수정에 실패하였습니다.');
        }
    };

    const handleFileChange = (e) => {
        const file = e.target.files[0];
        setMultipartFile(file);

        // 이미지 썸네일 미리보기 설정
        const reader = new FileReader();
        reader.onloadend = () => {
            setThumbnail(reader.result);
        };
        reader.readAsDataURL(file);
    };

    return (
        <div className="book-update-container">
            <h1>상품 정보 수정</h1>
            <form onSubmit={handleSubmit} className="book-update-form">
                <div>
                    <label>이미지:</label>
                    {thumbnail && ( // 썸네일 미리보기 추가
                        <div>
                            <img src={thumbnail} alt="Thumbnail" style={{ width: '200px', height: 'auto' }} />
                        </div>
                    )}
                    <input
                        type="file"
                        name="photoImagePath"
                        onChange={handleFileChange}
                    />
                </div>
                <div>
                    <label>책 이름:</label>
                    <input
                        type="text"
                        name="bookName"
                        value={bookRequestDto.bookName}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label>대분류:</label>
                    <select
                        name="mainCategory"
                        value={bookRequestDto.mainCategory}
                        onChange={handleChange}
                        required
                    >
                        <option value="" disabled>주요 카테고리 선택</option>
                        {mainCategories.map((category) => (
                            <option key={category.categoryName} value={category.categoryName}>
                                {category.categoryName}
                            </option>
                        ))}
                    </select>
                </div>
                <div>
                    <label>소분류:</label>
                    <select
                        name="subCategory"
                        value={bookRequestDto.subCategory}
                        onChange={handleChange}
                        required
                        disabled={!bookRequestDto.mainCategory}
                    >
                        <option value="" disabled>부 카테고리 선택</option>
                        {subCategories.map((subCategory) => (
                            <option key={subCategory.categoryName} value={subCategory.categoryName}>
                                {subCategory.categoryName}
                            </option>
                        ))}
                    </select>
                </div>
                <div>
                    <label>출판사:</label>
                    <input
                        type="text"
                        name="publisher"
                        value={bookRequestDto.publisher}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label>작가:</label>
                    <input
                        type="text"
                        name="author"
                        value={bookRequestDto.author}
                        onChange={handleChange}
                    />
                </div>

                <div>
                    <label>상품가격:</label>
                    <input
                        type="number"
                        name="price"
                        value={bookRequestDto.price}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label>재고:</label>
                    <input
                        type="number"
                        name="stock"
                        value={bookRequestDto.stock}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label>상품 설명:</label>
                    <textarea
                        name="bookDescription"
                        value={bookRequestDto.bookDescription}
                        onChange={handleChange}
                    />
                </div>

                <button type="submit">수정 완료</button>
            </form>
            {responseMessage && <p>{responseMessage}</p>}
        </div>
    );
};

export default BookUpdatePage;
