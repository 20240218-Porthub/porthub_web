function goToDetailPage(portfolioId) {
    // 클릭된 이미지의 포트폴리오 ID를 사용하여 상세 페이지로 이동
    window.location.href = '/ports/views/' + portfolioId;
}


window.onload = function() {
    // div 요소 가져오기
    var divElements = document.querySelectorAll('.text-wrapper-5');

    // 각 div 요소에 대해 실행
    divElements.forEach(function(divElement) {
        // data-src 속성 값 가져오기
        var src = divElement.getAttribute('data-src');
        if (divElement.getAttribute('data-src') == null) {
            src = '/images/None_Thumbnail.jpeg';
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
            } else if (fileExtension === 'jpeg' || fileExtension === 'png' || fileExtension === 'jpg' || fileExtension === 'gif' || fileExtension === 'avif') {
                return '<img src="' + src + '" class="image">';
            } else {
                return '<img src="' + src + '" class="image">';
            }
        }

        // 생성된 HTML 태그를 div 요소 안에 추가
        divElement.innerHTML = createHTMLTag(src);
    });
};
