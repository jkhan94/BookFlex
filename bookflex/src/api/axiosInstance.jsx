import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080', // 기본 API URL
    headers: {
        'Content-Type': 'application/json',
    },
});

const refreshToken = async () => {
    try {
        const response = await axiosInstance.post('/auth/refresh', {});
        const accessToken = response.data.accessToken;
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
        if(config.url === 'auth/login') {
            return config;
        }
        console.log("요청 가로채기");
        const access_token = localStorage.getItem('Authorization');
        const refresh_token = localStorage.getItem('refreshToken');

        if (config.url === '/auth/refresh') {
            console.log("요청세팅이 되는건가요");
            console.log(refresh_token);
            config.headers.setAuthorization(refresh_token);
        } else {
            console.log("액세스 토큰 자동보내주기");
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
            config.url === '/auth/refresh' ||
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
