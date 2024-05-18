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