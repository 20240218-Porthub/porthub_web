package hello.example.porthub.service;

import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.domain.OrderSaveDto;
import hello.example.porthub.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public int saveOrder(OrderSaveDto orderSaveDto) {
        return paymentRepository.insertOrder(orderSaveDto);
    }

    public OrderSaveDto selectOrder(int OrderID){ return paymentRepository.selectOrder(OrderID);}

    public int UpdateUserPaid(MemberDto memberDto){return paymentRepository.UpdateUserPaid(memberDto);}
}
