//package com.sparta.bookflex.domain.email;
//
//import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
//import com.sparta.bookflex.domain.user.service.UserService;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import lombok.RequiredArgsConstructor;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//import org.thymeleaf.spring6.SpringTemplateEngine;
//
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class EmailService {
//    private final JavaMailSender javaMailSender;
//    private final SpringTemplateEngine templateEngine;
//    private final UserService userService;
//
//    // 메일 양식 작성
//    public MimeMessage createEmailForm(EmailMessage emailMessage, OrderBook orderBook) throws MessagingException, UnsupportedEncodingException {
//
//        MimeMessage message = javaMailSender.createMimeMessage();
//        message.addRecipients(MimeMessage.RecipientType.TO, emailMessage.getTo());
//        message.setSubject(emailMessage.getSubject());
//
//        StringBuilder sb = new StringBuilder();
//
//        List<String> bookNameList = new ArrayList<>();
//        orderBook.getOrderItemList().stream().forEach(orderItem -> bookNameList.add(orderItem.getBook().getBookName()));
//
//        for (String bookName : bookNameList) {
//            sb.append(bookName + ", ");
//        }
//        // 메일 내용
//        String msgOfEmail = "";
//        msgOfEmail += "<div style='margin:20px;'>";
//        msgOfEmail += "<h1> 안녕하세요 BookFelx 입니다. </h1>";
//        msgOfEmail += "<br>";
//        msgOfEmail += "<p>배송 현황 안내드립니다.<p>";
//        msgOfEmail += "<br>";
//        msgOfEmail += "<p>감사합니다.<p>";
//        msgOfEmail += "<br>";
//        msgOfEmail += "<div align='center' style='border:1px solid black; font-family:verdana';>";
//        msgOfEmail += "<h3 style='color:blue;'>배송현황입니다.</h3>";
//        msgOfEmail += "<div style='font-size:130%'>";
//        msgOfEmail += "주문번호 : <strong>";
//        msgOfEmail += orderBook.getId() + "</strong><div><br/> ";
//        msgOfEmail += "상품명 : <strong>";
//        msgOfEmail +=  sb + "</strong><div><br/> ";
//        msgOfEmail += "배송현황 : <strong>";
//        msgOfEmail +=  orderBook.getStatus().getDesscription() + "</strong><div><br/> ";
//
//        msgOfEmail += "</div>";
//
//        message.setFrom("ejong12311@gmail.com");
//        message.setText(msgOfEmail, "utf-8", "html");
//
//        return message;
//    }
//
//    //실제 메일 전송
//    public void sendEmail(EmailMessage emailMessage, OrderBook orderBook) throws MessagingException, UnsupportedEncodingException {
//
//        //메일전송에 필요한 정보 설정
//        MimeMessage emailForm = createEmailForm(emailMessage, orderBook);
//        //실제 메일 전송
//        javaMailSender.send(emailForm);
//
//        //인증 코드 반환
//    }
//
//
//}
//
