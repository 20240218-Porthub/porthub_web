package hello.example.porthub.repository;

import hello.example.porthub.domain.OrderSaveDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepository {
    private final SqlSessionTemplate sql;

    public int insertOrder(OrderSaveDto orderSaveDto) { return sql.insert("payment.insertOrder", orderSaveDto); }
}
