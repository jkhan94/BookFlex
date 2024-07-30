import React, {useState, useEffect} from 'react';
import {useParams, useLocation} from 'react-router-dom';
import axiosInstance from '../../api/axiosInstance';


const BookUpdatePage = () => {
    const {productId} = useParams();
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
                        params: {mainCategory: bookRequestDto.mainCategory}
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
        } else {
            const fetchBook = async () => {
                try {
                    const response = await axiosInstance.get(`/books/${productId}`);
                    setBookRequestDto(response.data.data);
                } catch (error) {
                    console.error('There was an error!', error);
                    setResponseMessage('상품 조회에 실패하였습니다.');
                }
            };
            fetchBook();
        }
    }, [initialBook, productId]);

    const handleChange = (e) => {
        const {name, value} = e.target;
        setBookRequestDto((prevBook) => ({
            ...prevBook,
            [name]: value,
        }));
    };

    const handleFileChange = (e) => {
        setMultipartFile(e.target.files[0]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append('request', new Blob([JSON.stringify(bookRequestDto)], {type: 'application/json'}));
        formData.append('multipartFile', multipartFile);

        try {
            const response = await axiosInstance.put(`/books/${productId}`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            setResponseMessage('상품 정보 수정에 성공하였습니다.');
        } catch (error) {
            console.error('There was an error!', error);
            setResponseMessage('상품 정보 수정에 실패하였습니다.');
        }
    };

    return (
        <div className="book-update-container">
            <h1>상품 정보 수정</h1>
            <form onSubmit={handleSubmit} className="book-update-form">
                <div>
                    <label>상품명:</label>
                    <input
                        type="text"
                        name="bookName"
                        value={bookRequestDto.bookName}
                        onChange={handleChange}
                    />
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
                    <label>주요 카테고리:</label>
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
                    <label>부 카테고리:</label>
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
                <div>
                    <label>상품 상태:</label>
                    <select
                        name="status"
                        value={bookRequestDto.status}
                        onChange={handleChange}
                        required
                    >
                        <option value="" disabled>상태 선택</option>
                        <option value="ONSALE">판매 중</option>
                        <option value="SOLDOUT">품절</option>
                    </select>
                </div>

                <div>
                    <label>이미지:</label>
                    <input
                        type="file"
                        name="photoImagePath"
                        onChange={handleFileChange}
                    />
                </div>
                <button type="submit">수정 완료</button>
            </form>
            {responseMessage && <p>{responseMessage}</p>}
        </div>
    );
};

export default BookUpdatePage;
