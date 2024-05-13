package hello.example.porthub.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import hello.example.porthub.domain.OrderSaveDto;
import hello.example.porthub.repository.MemberRepository;
import hello.example.porthub.repository.PaymentRepository;
import hello.example.porthub.service.PaymentService;
import hello.example.porthub.service.RefundService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final RefundService refundService;

    private IamportClient iamportClient;

    @Value("${imp.api.key}")
    private String apiKey;

    @Value("${imp.api.secretkey}")
    private String secretKey;

    @PostConstruct
    private void init(){
        this.iamportClient=new IamportClient(apiKey,secretKey);
    }


    @PostMapping("/order/payment")
    public ResponseEntity<String> paymentComplete(@RequestBody OrderSaveDto orderSaveDto) throws IOException {
        log.info("orderSaveDto="+orderSaveDto);
        String orderNumber=String.valueOf(orderSaveDto.getMerchant_uid());
        try {
            int orderID=paymentService.saveOrder(orderSaveDto);
            log.info("orderID="+orderID);
            log.info("결제 성공 : 주문 번호 {}", orderNumber);
            log.info("response="+ResponseEntity.ok().build());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.info("exception="+e.getCause());
            log.info("주문 상품 환불 진행 : 주문 번호 {}", orderNumber);
            String token = refundService.getToken(apiKey, secretKey);
            refundService.refundRequest(token, orderNumber, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/payment/validation/{imp_uid}")
    @ResponseBody
    public IamportResponse<Payment> validateIamport(@PathVariable String imp_uid) throws IamportResponseException, IOException {
        log.info("here");
        IamportResponse<Payment> payment = iamportClient.paymentByImpUid(imp_uid);
        log.info("결제 요청 응답. 결제 내역 - 주문 번호: {}", payment.getResponse().getMerchantUid());
        return payment;
    }

}
