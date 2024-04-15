$(document).ready(function(){
    $('#summernote').summernote({
        height:300,
        minHeight:300,
        maxHeight:null,
        focus:true,
        lang:"ko-KR",
        toolbar:[
            ['fontname', ['fontname']],
            ['fontsize', ['fontsize']],
            ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
            ['color', ['forecolor','color']],
            ['table', ['table']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['height', ['height']],
            ['insert',['picture','link','video']],
            ['view', ['fullscreen', 'help']]
        ],
        fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋움체','바탕체'],
        fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72']
    })
})

function continuemento(e){
    $('.layer1').css('display','block');
}

function continuemento2(e){
    $('.layer2').css('display','block');
}

$(function(){
    $('.mentoback').click(function(){
        $('.layer1').css('display','none');
    })

    $('.mentoback2').click(function(){
        $('.layer2').css('display','none');
    })


    $('.createchapter').click(function(){
        $('.chapter').append('<div class="fraged port-title chaptercontent">\n' +
            '                    <label for="title">chapter'+($(".chaptercontent").length+1)+':</label>\n' +
            '                    <input type="file" class="input-file" name="mentofile[]" form="mentoForm">\n' +
            '                </div>')
    })

    $('.deletechapter').click(function(){
        if($(".chaptercontent").length!=1) {
            $('.chapter').children().last().remove();
        }
    })
})
