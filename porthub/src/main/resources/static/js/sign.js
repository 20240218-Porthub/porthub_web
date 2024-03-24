const checkPasswordMatch = () => {
    const password = document.getElementById("pw").value;
    const confirmPassword = document.getElementById("pwCheck").value;
    const errorElement = document.getElementById("password-match-error");

    if (password !== confirmPassword) {
        errorElement.style.display = "block";
        errorElement.textContent = "Passwords do not match";
        errorElement.style.color = "red";
        return false;
    } else {
        errorElement.style.display = "none";
        return true;
    }
}

const validatePassword = (errorElement) => { // errorElement를 함수의 매개변수로 추가
    const password = document.getElementById("pw").value;
    const passwordError = document.getElementById("password-error-message");
    const passwordPattern = /^.*(?=^.{8,16}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[~,!,@,#,$,*,(,),=,+,_,.,|]).*$/;

    if (!passwordPattern.test(password)) {
        errorElement.style.display = "block";
        passwordError.textContent = "Use 8 or more characters with a mix of letters, numbers & symbols";
        passwordError.style.color = "red";
        return false;
    } else {
        errorElement.style.display = "none"; // 에러 메시지를 숨깁니다.
        return true;
    }
}

// 폼이 제출될 때 패스워드 일치 여부 및 유효성을 모두 확인합니다.
const form = document.querySelector('form');
form.addEventListener('submit', (event) => {
    const matchValid = checkPasswordMatch();
    const vertifyCode = isCertification;
    const passwordValid = validatePassword(document.getElementById("password-error-message")); // validatePassword에 errorElement 전달


    if (!matchValid || !passwordValid || !vertifyCode || !checkName) {
        event.preventDefault(); // 폼 제출을 막습니다.
    }

});


//ajax 이메일 중복 처리
const emailCheck = () => {
    const email = document.getElementById("Email").value;
    console.log("입력한 email", email);
    $.ajax({
        type: "post",
        url: "/register/email-check",
        data: {
            "Email": email,
        },
        success: function (res) {
            console.log("요청 성공", res);
            if (res == true) {
                $("#VertifyEmail").css("display","block");
                checkemail = true;

            } else {
                $("#VertifyEmail").css("display","none");
                checkemail = false;
                alert("duplicated Email");
            }
        },
        error: function (err) {
            console.log("error", err);
        },
    })
}

const userCheck = () => {
    const username = document.getElementById("UserName").value;
    const checkResult = document.getElementById("check-result");
    console.log("입력한 UserName", username);
    $.ajax({
        type: "post",
        url: "/register/username-check",
        data: {
            "UserName": username
        },
        success: function (res) {
            console.log("요청 성공", res);
            if (res == "ok") {
                console.log("사용 가능한 UserName 입니다.");
                checkResult.style.color = "blue";
                checkResult.innerHTML = "사용 가능 UserName 입니다.";
                checkName = true;
            } else {
                console.log("이미 사용중인 UserName 입니다.");
                checkResult.style.color = "red";
                checkResult.innerHTML = "이미 사용중인 UserName 입니다.";
                checkName = false;
            }
        },
        error: function (err) {
            console.log("error", err);
        },
    })
}
var checkemail = false;
var checkName = false;
var isCertification = false;

function sendNumber(){
    if (checkemail == true) {
        $.ajax({
            url: "/Email",
            type: "post",
            crossDomain: true,
            headers: {'Access-Control-Allow-Origin': 'http://The web site allowed to access'},
            dataType: "json",
            data: {"Email": $("#Email").val()},
            success: function (data) {
                alert("인증번호 발송");
                $("#Confirm").attr("value", data);
                isCertification = false;
            },
            error: function (request, status, error) {
                alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
            }
        });
    } else {
        alert("duplicated Email")
    }
}


function confirmNumber(){
    var number1 = $("#EmailCode").val();
    var number2 = $("#Confirm").val();
    var checkResult2 = $("#VerifyBtn");


    if(number1 == number2 && number1 !== null && number1 !== ''){
        alert("인증 성공되었습니다.")
        checkResult2.css("background-color", "blueviolet");
        return isCertification = true;
    }else{
        alert("인증 불일치")
        checkResult2.css("background-color", "red");
        return isCertification = false;
    }
}

function togglePasswordVisibility() {
    var pwInput = document.getElementById("pw");
    var pwInput2 = document.getElementById("pwCheck");
    if (pwInput.type === "password") {
        pwInput.type = "text";
        pwInput2.type = "text";
    } else {
        pwInput.type = "password";
        pwInput2.type = "password";
    }
}