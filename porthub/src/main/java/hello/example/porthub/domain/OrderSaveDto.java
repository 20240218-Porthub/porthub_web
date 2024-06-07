package hello.example.porthub.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderSaveDto {
    private int OrderID;
    private String pg;
    private String pay_method;
    private String merchant_uid;
    private int goods_id;
    private String pay_name;
    private int amount;
    private String buyer_email;
    private String buyer_name;
    private String buyer_tel;
    private int buyer_postcode;
    private String buyer_addr;

}
