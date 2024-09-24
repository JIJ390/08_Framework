
function findAddress() {
  new daum.Postcode({
    oncomplete: function (data) {
      // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

      // 각 주소의 노출 규칙에 따라 주소를 조합한다.
      // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
      var addr = ''; // 주소 변수
      // var extraAddr = ''; // 참고항목 변수, 필요 없음

      //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
      if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
        addr = data.roadAddress;
      } else { // 사용자가 지번 주소를 선택했을 경우(J)
        addr = data.jibunAddress;
      }

      // 우편번호와 주소 정보를 해당 필드에 넣는다.
      document.getElementById('postcode').value = data.zonecode;
      document.getElementById("address").value = addr;
      // 커서를 상세주소 필드로 이동한다.
      document.getElementById("detailAddress").focus();
    }
  }).open();
}

const searchAddress = document.querySelector("#searchAddress");
searchAddress.addEventListener("click", findAddress);   // 함수 코드를 그대로 가져옴, () 붙이면 실행한다는 뜻

//-------------------------------------------------------------------------------------

/*** 회원 가입 유효성 검사 ***/

/* 필수 입력 항목의 유효성 검사 여부를 체크하기 위한 객체(체크리스트) */
const checkObj = {
  "memberEmail"     : false,
  "memberPw"        : false,
  "memberPwConfirm" : false,
  "memberNickname"  : false,
  "memberTel"       : false
}


/* ----- 이메일 유효성 검사 ----- */

// 1) 이메일 유효성 검사에 필요한 요소 얻어오기
const memberEmail  = document.querySelector("#memberEmail");    // 이메일 input 태그
const emailMessage = document.querySelector("#emailMessage");   // 이메일 span 태그 

// 2) 이메일 메시지를 미리 작성
const emailMessageObj = {}; // 빈 객체
emailMessageObj.normal  = "메일을 받을 수 있는 이메일을 입력해주세요.";
emailMessageObj.invaild = "알맞은 이메일 형식으로 작성해 주세요.";
emailMessageObj.duplication = "이미 사용중인 이메일입니다.";
emailMessageObj.check       = "사용 가능한 이메일입니다.";

// 3) 이메일이 입력될 때마다 유효성 검사를 수행
memberEmail.addEventListener("input", e => {
  // 입력된 값 얻어오기
  const inputEmail = memberEmail.value.trim();

  // 3-1) 입력된 이메일이 없을 경우
  if (inputEmail.length === 0) {
    // 이메일 메시지를 normal 상태 메시지로 변경
    emailMessage.innerText = emailMessageObj.normal;

    // #emalilMessage 에 색상 관련 클래스를 모두 제거
    emailMessage.classList.remove("confirm", "error");

    // checkObj 에서 memberEmail 을 false 로 변경
    checkObj.memberEmail = false;

    memberEmail.value = ""; // 잘목 입력된 값(띄어쓰기) 제거

    return;
  }

  // 3-2) 이메일 형식이 맞는지 검사 (정규 표현식을 이용한 검사)

  // 이메일 형식 정규 표현식 객체
  // 아무문자(영어,숫자,특문) @ 영어,숫자,하이픈 . 영어(2 글자 이상)
  const regEx = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

  // 입력 값(inputEmail) 이 이메일 형식이 아닌 경우
  if (regEx.test(inputEmail) === false) {
    emailMessage.innerText = emailMessageObj.invaild; // 유효 X 메시지

    emailMessage.classList.add("error");
    emailMessage.classList.remove("confirm");

    checkObj.memberEmail = false;

    return;
  }

  // 3-3) 입력된 이메일이 이미 존재할 경우(중복 검사, AJAX)
  fetch("/member/emailCheck?email=" + inputEmail)
  .then(response => {
    if(response.ok) { // 응답 상태코드 200(성공) 인 경우
      return response.text(); // 응답 결과를 Text 형태로 반환(parsing)
    }
    throw new Error("이메일 중복 검사 에러");
  })
  .then(count => {
    // 매개 변수 count : 첫 번째 then 에서 return 된 값이 저장된 변수

    if (count == 1) {
      emailMessage.innerText = emailMessageObj.duplication; // 유효 X 메시지

      emailMessage.classList.add("error");
      emailMessage.classList.remove("confirm");
  
      checkObj.memberEmail = false;
  
      return;
    }

    // 3-4) 유효한 이메일!!
    emailMessage.innerText = emailMessageObj.check;   

    emailMessage.classList.add("confirm");
    emailMessage.classList.remove("error");

    checkObj.memberEmail = true;

  })
  .catch(err => console.error(err));

});


// ---------------------------------------------------
/* 닉네임 유효성 검사 */
// 1) 닉네임 유효성 검사에 사용되는 요소 얻어오기
const memberNickname = document.querySelector("#memberNickname");
const nickMessage    = document.querySelector("#nickMessage");

// 2) 닉네임 메시지를 미리 작성
const nickMessageObj = {}; // 빈 객체
nickMessageObj.normal  = "한글, 영어, 숫자로만 3~10글자.";
nickMessageObj.invaild = "유효하지 않은 닉네임 형식입니다.";
nickMessageObj.duplication = "이미 사용중인 닉네임입니다.";
nickMessageObj.check       = "사용 가능한 닉네임입니다.";

// 3) 닉네임 입력 시마다 유효성 검사
memberNickname.addEventListener("input", () => {
  const inputNickname = memberNickname.value.trim();

  // 3-1) 입력된 닉네임이 없을 경우
  if (inputNickname.length === 0) {
    nickMessage.innerText = nickMessageObj.normal;

    nickMessage.classList.remove("confirm", "error");
      
    checkObj.memberNickname = false;

    memberNickname.value = ""; // 잘목 입력된 값(띄어쓰기) 제거

    return;
  }

  // 3-2) 닉네임 유효성 검사(정규 표현식)
  const regEx = /^[a-zA-Z0-9가-힣]{3,10}$/; // 한글,영어,숫자로만 3~10글자

  if (regEx.test(inputNickname) === false) {
    nickMessage.innerText = nickMessageObj.invaild;

    nickMessage.classList.add("error");
    nickMessage.classList.remove("confirm");
      
    checkObj.memberNickname = false;

    return;
  }

  // 3-3) 닉네임 중복 검사
  fetch("/member/nicknameCheck?nickname=" + inputNickname)
  .then(response => {
    if(response.ok) { // 응답 상태코드 200(성공) 인 경우
      return response.text(); // 응답 결과를 Text 형태로 반환(parsing)
    }
    throw new Error("이메일 중복 검사 에러");
  })
  .then(count => {
    // 매개 변수 count : 첫 번째 then 에서 return 된 값이 저장된 변수
    if (count == 1) {
      nickMessage.innerText = nickMessageObj.duplication; // 유효 X 메시지

      nickMessage.classList.add("error");
      nickMessage.classList.remove("confirm");
  
      checkObj.memberNickname = false;
  
      return;
    }
    // 3-4) 유효한 닉네임
    nickMessage.innerText = nickMessageObj.check;

    nickMessage.classList.add("confirm");
    nickMessage.classList.remove("error");
      
    checkObj.memberNickname = true;
  })
  .catch(err => console.error(err));

});


//-----------------------------------------------------------
// 전화번호 유효성 검사
const memberTel  = document.querySelector("#memberTel");
const telMessage = document.querySelector("#telMessage");

const telMessageObj = {};
telMessageObj.normal  = "전화번호를 입력해주세요.(- 제외)";
telMessageObj.invalid = "유효하지 않은 전화번호 형식입니다.";
telMessageObj.check   = "유효한 전화번호 형식입니다.";

memberTel.addEventListener("input", () => {

  const inputTel = memberTel.value.trim();

  if (inputTel.length === 0) {
    telMessage.innerHTML = telMessageObj.normal;
    telMessage.classList.remove("confirm", "error");
    checkObj.memberTel = false;
    memberTel.value = "";
    return;
  }

  const regEx = /^010[0-9]{8}$/;  // 010 으로 시작 이후 숫자 8개

  if (regEx.test(inputTel) === false) {
    telMessage.innerHTML = telMessageObj.invalid;

    telMessage.classList.add("error");
    telMessage.classList.remove("confirm");

    checkObj.memberTel = false;
    return;
  }

  telMessage.innerHTML = telMessageObj.check;

  telMessage.classList.add("confirm");
  telMessage.classList.remove("error");

  checkObj.memberTel = true;
})

//-----------------------------------------------------------
// 비밀번호 유효성 검사

const memberPw = document.querySelector("#memberPw");
const memberPwConfirm = document.querySelector("#memberPwConfirm");
const pwMessage = document.querySelector("#pwMessage");

const pwMessageObj = {};
pwMessageObj.normal = "영어,숫자,특수문자 1글자 이상, 6~20자 사이.";
pwMessageObj.invaild = "유효하지 않은 비밀번호 형식입니다.";
pwMessageObj.vaild = "유효한 비밀번호 형식입니다.";
pwMessageObj.error = "비밀번호가 일치하지 않습니다.";
pwMessageObj.check = "비밀번호가 일치 합니다.";


memberPw.addEventListener("input", () => {
  
  const inputPw = memberPw.value.trim();

  if(inputPw.length === 0){ // 비밀번호 미입력
    pwMessage.innerText = pwMessageObj.normal;
    pwMessage.classList.remove("confirm", "error");
    checkObj.memberPw = false;
    memberPw.value = "";
    return;
  }

  // 비밀번호 정규표현식 검사
  const lengthCheck = inputPw.length >= 6 && inputPw.length <= 20;
  const letterCheck = /[a-zA-Z]/.test(inputPw); // 영어 알파벳 포함
  const numberCheck = /\d/.test(inputPw); // 숫자 포함
  const specialCharCheck = /[\!\@\#\_\-]/.test(inputPw); // 특수문자 포함

  // 조건이 하나라도 만족하지 못하면
  if ( !(lengthCheck && letterCheck && numberCheck && specialCharCheck) ) {
    pwMessage.innerText = pwMessageObj.invaild;
    pwMessage.classList.remove("confirm");
    pwMessage.classList.add("error");
    checkObj.memberPw = false;
    return;
  }

  pwMessage.innerText = pwMessageObj.vaild;
  pwMessage.classList.remove("error");
  pwMessage.classList.add("confirm");
  checkObj.memberPw = true;


  // 비밀번호 확인이 작성된 상태에서
  // 비밀번호가 입력된 경우
  if(memberPwConfirm.value.trim().length > 0) {
    checkPw();  // 같은지 비교하는 함수 호출
  }
});

/* ----- 비밀번호, 비밀번호 확인 같은지 검사하는 함수 ----- */
function checkPw() {
  // 같은 경우
  if (memberPw.value === memberPwConfirm.value) {
    pwMessage.innerText = pwMessageObj.check;
    pwMessage.classList.add("confirm");
    pwMessage.classList.remove("error");
    checkObj.memberPwConfirm = true;
    return;
  }

  // 다른 경우
  pwMessage.innerText = pwMessageObj.error;
  pwMessage.classList.add("error");
  pwMessage.classList.remove("confirm");
  checkObj.memberPwConfirm = false;
};

/* ----- 비밀번호 확인이 입력되었을 때 ----- */
memberPwConfirm.addEventListener("input", () => {
  // 비밀번호 input 에 작성된 값이 유효한 형식일 때만 비교
  if(checkObj.memberPw === true) {
    checkPw();
    return;
  }

  // 비밀번호 input 에 작성된 값이 유효하지 않은 경우
  checkObj.memberPwConfirm = false;
});