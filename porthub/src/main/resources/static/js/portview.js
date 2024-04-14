function redirectToPortfolio(portfolioID) {
    window.location.href = '/ports/views/' + portfolioID;
}

function redirectToProfileClick(element) {
    var userName = element.getAttribute('data-username');
    window.location.href = '/profile/' + userName;
}

function checkLogin() {
    var isLoggedIn = false; // 로그인 상태를 나타내는 변수 (임시로 false로 설정)
    isLoggedIn = [[${isLoggedIn}]];
    if (isLoggedIn) {
        showReportPopup();
    } else {
        // 로그인되어 있지 않으면 로그인 요청 팝업을 보여줍니다.
        var check_login = confirm("로그인이 필요합니다. 로그인 하시겠습니까?");
        if (check_login) {
            window.location.href = '/login';
        }
    }
}

function showReportPopup() {
    document.getElementById('reportPopup').style.display = 'block';
}

function hideReportPopup() {
    document.getElementById('reportPopup').style.display = 'none';
}

function submitReport() {
    var result = confirm("제출 하시겠습니까?");
    var reportText = document.getElementById('reportTextArea').value;
    if (result) {
        // 사용자가 제출을 확인하면 보고서를 서버로 전송
        fetch('/ports/views/report', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json' // Content-Type을 application/json으로 변경
            },
            body: JSON.stringify({ contents: reportText }) // 보고서 내용을 객체에 추가하여 JSON 형식으로 변환하여 전송
        })
            .then(response => {
                if (response.ok) {
                    console.log('보고서가 성공적으로 전송되었습니다.');
                } else {
                    console.error('보고서 전송 중 오류가 발생했습니다.');
                }
                // 팝업 닫기
                hideReportPopup();
            })
            .catch(error => {
                console.error('보고서 전송 중 오류가 발생했습니다:', error);
                // 팝업 닫기
                hideReportPopup();
            });
    }
}

function confirmCancel() {
    var result = confirm("취소 하시겠습니까?");
    if (result) {
        hideReportPopup();
    }
}

