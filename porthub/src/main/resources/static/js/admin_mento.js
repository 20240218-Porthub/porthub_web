$(function() {
    $(document).on("click","#accept",function(){
        let accordionitem=$(this).parents('.accordion-item')
        let processid=accordionitem.children(".accordion-collapse").attr("id").replace('flush-collapse','');
        let mentoid=accordionitem.find('.accordion_userid').text()
        let company=accordionitem.find('.mentocompany').val()
        let univ=accordionitem.find('.mentouniv').val()
        let issue=accordionitem.find('.mentoissue').val()
        let data={
            company:company,
            univ:univ,
            issue:issue,
        }
        console.log(mentoid)
        $.ajax({
            method:"post",
            url:`/admin/mento/accept/${processid}`,
            data:data,
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
        accordionitem.parent().remove();
    })

    $('#deny').click(function () {
        let accordionitem=$(this).parents('.accordion-item')
        let processid=accordionitem.children(".accordion-collapse").attr("id").replace('flush-collapse','');
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

    $('')


})