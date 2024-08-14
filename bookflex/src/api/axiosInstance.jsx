import axios from 'axios';

const axiosInstance = axios.create({
    // baseURL: 'https://bookflex-sparta.com/api', // 기본 API URL
    baseURL: 'http://localhost:8080/api', // 기본 API URL
    headers: {
        'Content-Type': 'application/json',
    },
});

const refreshToken = async () => {
    try {
        const response = await axiosInstance.post('/auth/refresh', {});
        const {accessToken} = response.data.data;
        localStorage.setItem('Authorization', accessToken);
        console.log("재발급성공");
        return accessToken;
    } catch (error) {
        console.error('재발급 에러:', error.response?.data || error.message);
        throw error;
    }
};

// 요청 인터셉터
axiosInstance.interceptors.request.use(
    async (config) => {
        if(config.url === '/api/auth/login') {
            return config;
        }
        const access_token = localStorage.getItem('Authorization');
        const refresh_token = localStorage.getItem('refreshToken');

        if (config.url === '/api/auth/refresh') {
            console.log("리프레쉬 토큰 보내기");
            config.headers.setAuthorization(refresh_token);
        } else {
            console.log("액세스 토큰 보내주기");
            config.headers.setAuthorization(access_token);
        }
        return config;
    }
);

axiosInstance.interceptors.response.use(
    (response) => {
        return response;
    },

    async (error) => {
        const {config, response} = error;
        console.log(config.sent);
        if (
            config.url === '/api/auth/refresh' ||
            response?.status !== 402 ||
            config.sent) {
            return Promise.reject(error);
        }

        config.sent = true;
        const access_token = await refreshToken();
        if(access_token) {
            config.headers.Authorization = access_token;
        }

        return axiosInstance(config);
    }
)

export default axiosInstance;