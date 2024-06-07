package hello.example.porthub.repository;

import com.ibm.icu.text.BidiTransform;
import hello.example.porthub.domain.MemberDto;
import hello.example.porthub.domain.OrderSaveDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepository {
    private final SqlSessionTemplate sql;

    public int insertOrder(OrderSaveDto orderSaveDto) {
        sql.insert("payment.insertOrder", orderSaveDto);
        return orderSaveDto.getOrderID();
    }

    public OrderSaveDto selectOrder(int OrderID){ return sql.selectOne("payment.selectOrder", OrderID);}

    public int UpdateUserPaid(MemberDto memberDto){ return sql.update("payment.updatePaid",memberDto);}
}
