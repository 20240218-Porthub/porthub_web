const extentionarr=["aac","ai","bmp","cs","css","csv","doc","docx","exe","gif","heic","html","java","jpg","js","json","jsx","key","m4p","md","mdx","mov","mp3","mp4","otf","pdf","php","png","ppt","pptx","psd","py","raw","rb","sass","scss","sh","sql","svg","tiff","tsx","ttf","txt","wav","woff","xls","xlsx","xml","yml"]

window.onload = function() {
    // div 요소 가져오기
    var divElements = document.querySelectorAll('.mentofile');

    // 각 div 요소에 대해 실행
    divElements.forEach(function(divElement) {
        var src = divElement.getAttribute('data-src');
        var decodesrc=decodeURIComponent(src);
        var filename = decodesrc.substring(decodesrc.lastIndexOf('/') + 1);
        var extention=filename.substring(filename.lastIndexOf('.')+1);
        var title=filename.substring(0,filename.lastIndexOf('.'));
        if(extentionarr.indexOf(extention)!=-1){
            divElement.querySelector(".mentothumbnail").innerHTML="<i class='bi bi-filetype-"+extention+"'></i>";
            divElement.querySelector(".mentotitle").innerText=title;
        }
        else{
            divElement.querySelector(".mentothumbnail").innerHTML="<i class='bi bi-file-earmark-text'></i>";
            divElement.querySelector(".mentotitle").innerText=title;
        }
    });
};

$(function(){
    $('.delete-mentoring').click(function(){
        let mentoringtitle=$(this).prev().text()
        let mentoringpar=$(this).parent()
        let mentoringid=mentoringpar.attr('id')
        if(confirm("멘토링 제목: "+mentoringtitle+"을 삭제하시겠습니까? *이미 결제된 사용자에게는 컨텐츠가 남아있습니다.")){
            $.ajax({
                method:"post",
                url:'/mentoring/delete',
                data:{
                  "MentoringID":mentoringid
                },
                success:function(rsp){
                    mentoringpar.remove()
                    alert("삭제 완료")
                },
                error:function(e){
                    alert("삭제 실패")
                }
            })
        }else{
            alert("취소했습니다.")
        }
    })
})