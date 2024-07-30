import axios from 'axios';


// Create an instance of axios
const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080', // 기본 API URL
    headers: {
        'Content-Type': 'application/json',
    },
});

// Request interceptor to add token to headers
axiosInstance.interceptors.request.use(
    (config) => {
        // 토큰을 가져옵니다.
        const accessToken = localStorage.getItem('Authorization');
        if(!accessToken) {
            console.log('노 토큰')
        }
        if (accessToken) {
            // 토큰을 헤더에 추가합니다.
            config.headers.Authorization = accessToken;
        }

        return config;
    },
    (error) => {
        // 요청 오류를 반환합니다.
        return Promise.reject(error);
    }
);

export default axiosInstance;