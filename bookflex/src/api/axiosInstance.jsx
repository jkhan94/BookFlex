import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080', // 기본 API URL
    headers: {
        'Content-Type': 'application/json',
    },
});

export default axiosInstance;