const base64Decode = (str) => {
    try {
        // Base64 문자열에서 URL 및 파일명 안전한 변형을 원래 형태로 변환
        const base64 = str.replace(/-/g, '+').replace(/_/g, '/');
        // Base64 디코딩 후 문자열 반환
        return decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
    } catch (error) {
        console.error('Base64 decoding failed:', error);
        return null;
    }
};

const jwtDecode = (token) => {
    if (!token) {
        return null;
    }

    try {
        const parts = token.split('.');
        if (parts.length !== 3) {
            throw new Error('Invalid JWT token format');
        }

        const payload = parts[1]; // 두 번째 부분이 페이로드
        const decodedPayload = base64Decode(payload);

        if (decodedPayload) {
            return JSON.parse(decodedPayload);
        } else {
            return null;
        }
    } catch (error) {
        console.error('JWT decoding failed:', error);
        return null;
    }
};

export const isAdmin = () => {
    const token = localStorage.getItem('Authorization');
    if (!token) {
        return false;
    }

    try {
        const decodedToken = jwtDecode(token);
        return decodedToken.auth === 'ADMIN';
    } catch (error) {
        console.error('Invalid token:', error);
        return false;
    }
};

export default isAdmin();