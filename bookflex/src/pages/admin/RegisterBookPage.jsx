import React, {useEffect, useState} from 'react';
import axiosInstance from "../../api/axiosInstance";

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
    const [responseMessage, setResponseMessage] = useState('');
    const [accessToken, setAccessToken] = useState(''); // 액세스 토큰 상태 추가

    useEffect(() => {
        // 실제로 액세스 토큰을 가져오는 로직 추가
        const token = localStorage.getItem('Authorization');
        setAccessToken(token);


    })


    const handleChange = (e) => {
        const { name, value } = e.target;
        setBookRequestDto({
            ...bookRequestDto,
            [name]: value,
        });
    };

    const handleFileChange = (e) => {
        setMultipartFile(e.target.files[0]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData();
        formData.append('request', new Blob([JSON.stringify(bookRequestDto)], { type: 'application/json' }));
        formData.append('multipartFile', multipartFile);

        try {
            const token = localStorage.getItem('accessToken'); // 액세스 토큰을 로컬 스토리지에서 가져옵니다
            const response = await axiosInstance.post('/books', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    Authorization: accessToken, // 액세스 토큰을 Authorization 헤더에 추가합니다
                },
            });
            setResponseMessage(response.data.message);
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
                    <label>작가명:</label>
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
                <div>
                    <label>상품 상태:</label>
                    <input
                        type="text"
                        name="status"
                        value={bookRequestDto.status}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>주요 카테고리:</label>
                    <input
                        type="text"
                        name="mainCategory"
                        value={bookRequestDto.mainCategory}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>부 카테고리:</label>
                    <input
                        type="text"
                        name="subCategory"
                        value={bookRequestDto.subCategory}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>파일 업로드:</label>
                    <input
                        type="file"
                        onChange={handleFileChange}
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
