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
    const passwordValid = validatePassword(document.getElementById("password-error-message")); // validatePassword에 errorElement 전달

    if (!matchValid || !passwordValid) {
        event.preventDefault(); // 폼 제출을 막습니다.
    }
});
