import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import axiosInstance from '../../api/axiosInstance';

export function SuccessPage() {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();

    useEffect(() => {
        // 쿼리 파라미터로부터 필요한 데이터 추출
        const orderId = searchParams.get("orderId");
        const amount = searchParams.get("amount");
        const paymentKey = searchParams.get("paymentKey");

        // 서버에 결제 성공 데이터 전송
        async function confirmPayment() {
            try {
                const response = await axiosInstance.post("/payments/success", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        orderId,
                        amount,
                        paymentKey,
                    }),
                });

                // 서버 응답 처리
                if (!response.ok) {
                    const { message, code } = await response.json();
                    navigate(`/fail?message=${message}&code=${code}`);
                    return;
                }

                // 성공 시 주문 내역 페이지로 리다이렉트
                navigate(`main/payment-history`);
            } catch (error) {
                console.error('Error processing payment: ', error);
                navigate(`/fail?message=Error processing payment&code=500`);
            }
        }

        confirmPayment();
    }, [navigate, searchParams]);

    return (
        <div className="result wrapper">
            <div className="box_section">
                <h2>결제 성공</h2>
                <p>{`주문번호: ${searchParams.get("orderId")}`}</p>
                <p>{`결제 금액: ${Number(searchParams.get("amount")).toLocaleString()}원`}</p>
                <p>{`Payment Key: ${searchParams.get("paymentKey")}`}</p>
            </div>
        </div>
    );
}

export default SuccessPage;
