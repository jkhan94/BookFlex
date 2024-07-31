import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080', // 기본 API URL
    headers: {
        'Content-Type': 'application/json',
    },
});

const accessToken = localStorage.getItem('accessToken');
if (accessToken) {
    axiosInstance.defaults.headers.common['Authorization'] = accessToken;
}



export default axiosInstance;