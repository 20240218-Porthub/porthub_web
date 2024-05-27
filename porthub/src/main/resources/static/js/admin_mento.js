$(function() {
    $('#accept').click(function () {
        let processid=$(this).parents(".mento-item").attr("id");
        let mentoid=$(this).parent().prev().children("#mento-id").text()
        console.log($(this).parent().prev().children("#mento-id").text())
        $.ajax({
            method:"post",
            url:`/admin/mento/accept/${processid}`,
            success:function(rsp){
                console.log(rsp)
                $("#accept-mento").append('<div class="mento-item" id="'+processid+'">\n' +
                    '                                <div><span>id : </span><span>'+mentoid+'</span></div>\n' +
                    '                                    <button id="delete">삭제</button>\n' +
                    '                                </div>\n' +
                    '                            </div>');
                alert("수정 완료")
            },
            error:function(e){
                alert("수정 실패")
            }
        })
        $(this).parents(".mento-item").remove();
    })

    $('#deny').click(function () {
        let processid=$(this).parents(".mento-item").attr("id");
        $.ajax({
            method: "post",
            url: `/admin/mento/deny/${processid}`,
            success: function (rsp) {
                $(this).parents(".mento-item").remove();
                alert("수정 완료")
            },
            error: function (e) {
                alert("수정 실패")
            }
        })
    })

    $('#delete').click(function () {
        let processid=$(this).parents(".mento-item").attr("id");
        $.ajax({
            method: "post",
            url: `/admin/mento/deny/${processid}`,
            success: function (rsp) {
                $(this).parents(".mento-item").remove();
                alert("수정 완료")
            },
            error: function (e) {
                alert("수정 실패")
            }
        })
    })
})