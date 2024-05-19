 function goToDetailPage(portfolioId) {
    // 클릭된 이미지의 포트폴리오 ID를 사용하여 상세 페이지로 이동
    window.location.href = '/ports/views/' + portfolioId;
}

    function goToProfilePage(UserName) {
    window.location.href = '/profile/' + UserName;
}

    window.onload = function() {
    // div 요소 가져오기
    var divElements = document.querySelectorAll('.text-wrapper-5');

    // 각 div 요소에 대해 실행
    divElements.forEach(function(divElement) {
    // data-src 속성 값 가져오기
    var src = divElement.getAttribute('data-src');
    if (divElement.getAttribute('data-src') == null) {
    src = 'https://porthub2.s3.ap-northeast-2.amazonaws.com/None_Thumbnail.jpeg';
}

    // 파일 경로로부터 파일의 확장자를 추출하는 함수
    function getFileExtension(url) {
    // URL에서 마지막 슬래시(/) 뒤의 문자열을 가져옴
    var filename = url.substring(url.lastIndexOf('/') + 1);
    // 파일명에서 마지막 점(.) 뒤의 문자열을 가져와서 소문자로 변환하여 반환
    return filename.split('.').pop().toLowerCase();
}

    // 확장자에 따라 적절한 HTML 태그를 생성하는 함수
    function createHTMLTag(src) {
    var fileExtension = getFileExtension(src);
    if (fileExtension === 'mp3' || fileExtension === 'wav' || fileExtension === 'ogg') {
    return '<audio controls class="image"><source src="' + src + '" type="audio/' + fileExtension + '"></audio>';
} else if (fileExtension === 'mp4' || fileExtension === 'avi' || fileExtension === 'mov') {
    return '<video controls class="image"><source src="' + src + '" type="video/' + fileExtension + '"></video>';
} else if (fileExtension === 'jpeg' || fileExtension === 'png' || fileExtension === 'jpg' || fileExtension === 'gif') {
    return '<img src="' + src + '" class="image">';
} else {
    return '<img src="https://porthub2.s3.ap-northeast-2.amazonaws.com/None_Thumbnail.jpeg" class="image">';
}
}

    // 생성된 HTML 태그를 div 요소 안에 추가
    divElement.innerHTML = createHTMLTag(src);
});
};

 function navigateToCategory(buttonElement) {
     const categoryName = buttonElement.innerText || buttonElement.textContent;
     let url; // url 변수를 함수 스코프 밖에서 선언

     if (categoryName === "All") {
         url = '/' + 'main'; // 변수 url에 값을 할당
     } else {
         url = '/' + encodeURIComponent(categoryName); // 변수 url에 값을 할당
     }

     window.location.href = url; // 변수 url을 사용하여 페이지 이동
 }

 function sortPortfolio() {
     const sortOrder = document.getElementById("sort-options").value;
     const categoryName = /*[[${CategoryName}]]*/ 'All'; // Use Thymeleaf to get CategoryName
     const url = "/" + categoryName + "?order=" + sortOrder; // url 변수를 const로 선언

     window.location.href = url; // 변수 url을 사용하여 페이지 이동
 }
