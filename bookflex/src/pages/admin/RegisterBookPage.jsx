import React, {useEffect, useState} from 'react';
import axiosInstance from "../../api/axiosInstance";
import {useNavigate} from "react-router-dom"; // axiosInstance는 Axios 인스턴스를 설정한 파일입니다.

function RegisterBookPage() {
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
    const [thumbnail, setThumbnail] = useState(''); // 이미지 썸네일 상태 추가
    const [responseMessage, setResponseMessage] = useState('');
    const [mainCategories, setMainCategories] = useState([]);
    const [subCategories, setSubCategories] = useState([]);
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

    const handleChange = (e) => {
        const {name, value} = e.target;
        setBookRequestDto({
            ...bookRequestDto,
            [name]: value,
        });
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

    const handleSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData();
        formData.append('request', new Blob([JSON.stringify(bookRequestDto)], {type: 'application/json'}));
        formData.append('multipartFile', multipartFile);

        try {
            const response = await axiosInstance.post('/books', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            setResponseMessage(response.data.message);
            navigate(`/admin/books/${response.data.data.bookId}`); // 상품 상세 페이지로 이동
        } catch (error) {
            console.error('There was an error!', error);
            setResponseMessage('상품 등록에 실패하였습니다.');
        }
    };

    return (
        <div>
            <h1>상품 등록</h1>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>이미지:</label>
                    {thumbnail && ( // 썸네일 미리보기 추가
                        <div>
                            <img src={thumbnail} alt="Thumbnail" style={{width: '200px', height: 'auto'}}/>
                        </div>
                    )}
                    <input
                        type="file"
                        onChange={handleFileChange}
                        required
                    />
                </div>

                <div>
                    <label>책 이름:</label>
                    <input
                        type="text"
                        name="bookName"
                        value={bookRequestDto.bookName}
                        onChange={handleChange}
                        required
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
                        required
                    />
                </div>
                <div>
                    <label>저자:</label>
                    <input
                        type="text"
                        name="author"
                        value={bookRequestDto.author}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>가격:</label>
                    <input
                        type="number"
                        name="price"
                        value={bookRequestDto.price}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>재고량:</label>
                    <input
                        type="number"
                        name="stock"
                        value={bookRequestDto.stock}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>책 설명:</label>
                    <textarea
                        name="bookDescription"
                        value={bookRequestDto.bookDescription}
                        onChange={handleChange}
                        required
                    />
                </div>
                <button type="submit">등록</button>
            </form>
            {responseMessage && <p>{responseMessage}</p>}
        </div>
    );
}

export default RegisterBookPage;
