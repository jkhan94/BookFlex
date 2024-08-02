import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
export function FailPage() {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    useEffect(() => {
        // 페이지 로드 시 /main/cart로 리디렉션
        alert(`주문이 실패했습니다. 에러 코드: ${searchParams.get("code")} \n실패 사유: ${searchParams.get("message")}`);
        navigate("/main/cart");
    }, [navigate]);
    return (
        <div className="result wrapper">
            <div className="box_section">
                <h2>
                    결제 실패
                </h2>
                <p>{`에러 코드: ${searchParams.get("code")}`}</p>
                <p>{`실패 사유: ${searchParams.get("message")}`}</p>
            </div>
        </div>
    );
}
export default FailPage;