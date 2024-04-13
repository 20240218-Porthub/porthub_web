function metasave(){
    const metaform=document.getElementById("metadata");
    if(confirm("저장하시겠습니까?")){
        alert("저장했습니다.");
        metaform.submit();
    }
}

function metacancle(){
    if(confirm("변경된 내용을 저장하지않고 돌아가시겠습니까?")){
        location.href="/profile/index";
    }
}

function previewimg(param){
    if (param.files && param.files[0]) {
        var reader = new FileReader();
        reader.onload = function(e) {
            param.nextElementSibling.src = e.target.result;
        };
        reader.readAsDataURL(param.files[0]);
    } else {
        param.nextElementSibling.src = "";
    }
}