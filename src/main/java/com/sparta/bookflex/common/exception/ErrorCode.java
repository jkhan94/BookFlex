package com.sparta.bookflex.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 기본
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD REQUEST"),

    // 권한
    UNAUTHORIZED_ACTION(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    UNAUTHORIZED_ACTION_CARD(HttpStatus.FORBIDDEN, "카드 생성/수정/삭제에 대한 권한이 없습니다."),

    // 사용자
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    EXIST_USER(HttpStatus.CONFLICT, "이미 존재하는 유저입니다."),
    USER_BLOCKED(HttpStatus.BAD_GATEWAY, "회원탈퇴한 유저입니다."),
    USER_BANNED(HttpStatus.FORBIDDEN, "차단된 유저입니다"),
    FAIL_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "아이디나 비밀번호가 정확하지 않습니다."),
    PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, "현재 비밀번호와 사용자의 비밀번호가 일치하지 않습니다."),
    PASSWORD_REUSED(HttpStatus.BAD_REQUEST, "동일한 비밀번호로는 변경하실 수 없습니다."),
    USER_NOT_AUTHENTICATED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    USER_NOT_WRITER(HttpStatus.FORBIDDEN, "작성자만 수정할 수 있습니다."),
    USER_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "인가되지 않은 사용자입니다."),

    // 보드
    BOARD_CREATE_MISSING_DATA(HttpStatus.BAD_REQUEST, "보드 이름과 한 줄 설명은 필수입니다."),
    BOARD_ALREADY_DELETED(HttpStatus.NOT_FOUND, "이미 삭제된 보드입니다."),
    BOARD_INVITE_ALREADY_MEMBER(HttpStatus.CONFLICT, "이미 해당 보드에 초대된 사용자입니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "보드를 찾을 수 없습니다."),
    BOARD_NO_PERMISSION(HttpStatus.BAD_REQUEST, "해당 보드에 접근할 권한이 없습니다."),

    // 섹션
    SECTION_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 섹션 이름입니다."),
    SECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "섹션을 찾을 수 없습니다."),

    // 카드
    CARD_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 카드 이름입니다."),
    CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "카드를 찾을 수 없습니다."),

    // 댓글
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),

    // 책
    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "책을 찾을 수 없습니다."),
    BOOKSTATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 상태를 찾을 수 없습니다."),
    CANNOT_EXCEED(HttpStatus.BAD_REQUEST, "구매수량이 재고수량을 초과하였습니다."),

    // 카테고리
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    CATEGORY_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 카테고리입니다."),
    NOT_LEAF_CATEGORY(HttpStatus.BAD_REQUEST, "하위 카테고리를 선택해주세요."),

    // 토큰
    EXPIRED_TOKEN(HttpStatus.PAYMENT_REQUIRED, "이미 만료된 토큰입니다"),

    // 고객문의
    QNA_CREATE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "문의를 남길 수 없습니다."),
    QNA_DELETE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "문의를 삭제할 수 없습니다."),
    QNA_VIEW_NOT_ALLOWED(HttpStatus.FORBIDDEN, "문의를 조회할 수 없습니다."),
    QNA_NOT_FOUND(HttpStatus.NOT_FOUND, "문의 내역을 찾을 수 없습니다."),
    QNA_DELETE_NOT_ALLOWED_REPLY_EXISTS(HttpStatus.BAD_REQUEST, "답변 완료된 문의는 삭제할 수 없습니다."),
    REPLY_CREATE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "답변을 남길 수 없습니다."),
    REPLY_ALREADY_EXISTS(HttpStatus.CONFLICT, "답변 완료된 문의입니다."),

    // 리뷰
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),

    ///장바구니
    BASKET_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니을 찾을 수 없습니다."),
    BASKET_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니에서 책을 착을 수 없습니다." ),
    BASKET_ITEM_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 장바구니 내에 책이 존재합니다" ),

    //주문

    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),
    ORDER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 상태를 찾을 수 없습니다."),
    ORDER_STATUS_NOT_CHANGED(HttpStatus.BAD_REQUEST, "주문 상태가 변경전과 동일합니다.."   ),
    //결제

    FAIL_REQUEST_TOSS(HttpStatus.BAD_REQUEST, "토스와 연결을 실패했습니다"),
    PAYMENT_CREATION_FAILED(HttpStatus.BAD_REQUEST, "결제 생성 과정에서 예상치 못한 오류가 발생했습니다."),

    // 쿠폰
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND,"쿠폰을 찾을 수 없습니다."),
    COUPON_CREATE_NOT_ALLOWED(HttpStatus.FORBIDDEN,"쿠폰을 생성할 수 없습니다."),
    COUPON_VIEW_NOT_ALLOWED(HttpStatus.FORBIDDEN,"쿠폰을 조회할 수 없습니다."),
    COUPON_UPDATE_NOT_ALLOWED(HttpStatus.FORBIDDEN,"쿠폰을 변경할 수 없습니다."),
    COUPON_DELETE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "쿠폰을 삭제할 수 없습니다."),
    COUPON_ISSUE_NOT_ALLOWED(HttpStatus.FORBIDDEN,"쿠픈을 발급받을 수 없습니다."),
    COUPON_CANNOT_BE_ISSUED(HttpStatus.BAD_REQUEST,"발급받을 수 없는 쿠폰입니다."),
    COUPON_ALREADY_ISSUED(HttpStatus.CONFLICT,"이미 발급받은 쿠폰입니다."),
    COUPON_NOT_ISSUED(HttpStatus.NOT_FOUND, "발급되지 않은 쿠폰입니다."),
    COUPON_ALREADY_USED(HttpStatus.BAD_REQUEST,"이미 사용된 쿠폰입니다."),
    COUPON_CANNOT_BE_ISSUED_TO_ALL(HttpStatus.BAD_REQUEST,"쿠폰 수량이 부족합니다."),
    ORDER_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND,"주문에 책을 찾을 수 없습니다."),;

    private final HttpStatus status;
    private final String message;
}