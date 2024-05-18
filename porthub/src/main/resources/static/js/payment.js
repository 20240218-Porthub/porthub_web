$(() =>{
    let pg = "";
    let payMethod = "";

    $('#cartPay').on("click", () => {
        pg = "html5_inicis";
        payMethod = "card"
    });

    $('#phonePay').on("click", () => {
        pg = "danal";
        payMethod = "MOBILE";
    });

    $('#kakaoPay').on("click", () => {
        pg = "kakaopay";
        payMethod = "card"
    });

    $(document).on("click", "#do-pay", () => {
        let name = $(".goods-name").text();
        const orderNumber = createOrderNum();

        let order = {};
        // const price = parseInt($(e).text().replace(/[^0-9]/g, ''));
        // let discountRate = parseInt($('.orderProductDiscount').eq(i).text().replace(/[^0-9]/g, ''));
        // if (!discountRate) {
        //     discountRate = 0;
        // }
        order.pg= pg;
        order.pay_method = payMethod;
        order.merchant_uid = orderNumber;
        order.pay_name = name;
        order.amount= parseInt($(".goods-price").text());
        order.buyer_name = "ghdtjq";
        order.buyer_email="ghdtjq1111@gmail.com"
        order.buyer_tel = "010123445687";
        order.buyer_postcode = "12345";
        order.buyer_addr = "110-4564";

        // iamport 초기화 및 결제 요청
        IMP.init('imp70034134');
        IMP.request_pay({
            pg: pg,
            pay_method : payMethod,
            merchant_uid : orderNumber,
            name : name,
            amount: parseInt($(".goods-price").text()), // 결제 가격
            buyer_name : "ghdtjq",
            buyer_email:"ghdtjq1111@gmail.com",
            buyer_tel : "010123445687",
            buyer_postcode : "12345",
            buyer_addr : "110-4564"
        }, function(rsp) {
            if (rsp.success) {
                $.ajax({
                    method: "post",
                    url: `/payment/validation/${rsp.imp_uid}`
                }).then(res => {
                    if (parseInt($(".goods-price").text()) == res.response.amount) {
                        $.ajax({
                            url: "/order/payment",
                            method: "post",
                            data: JSON.stringify(order),
                            headers: {'Content-Type': 'application/json'}
                        }).then(res => {
                            let msg = '결제가 완료되었습니다.';
                            msg += '상점 거래ID : ' + rsp.merchant_uid;
                            msg += '결제 금액 : ' + rsp.paid_amount;
                            alert(msg)
                            window.location.href=`/payment/confirm/${res}`
                        }).catch((error) => {
                            alert("주문정보 저장을 실패 했습니다.")
                        });
                    }
                }).catch(error => {

                    alert('결제에 실패하였습니다. ' + rsp.error_msg);
                });
            } else {
                alert(rsp.error_msg);
            }
        });
    })
});

// 주문번호 생성 함수
function createOrderNum() {
    const date = new Date();
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");

    let orderNum = year + month + day;
    for (let i = 0; i < 5; i++) {
        orderNum += Math.floor(Math.random() * 8);
    }
    return parseInt(orderNum);
}