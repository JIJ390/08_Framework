/* 다음 주소 API 로 주소 검색하기 */

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

/* 주소 검색 버튼 클릭 시 */
/* 버튼 있을 때만!!! 활성화 */
if (document.querySelector("#findAddressBtn") !== null) {
  document.querySelector("#findAddressBtn")
    .addEventListener("click", findAddress);
};


// 함수명만 작성하는 경우 : 함수 내부에 작성된 코드가 그래도 들어옴
// 함수명() 작성하는 경우 : 함수에 정의된 내용을 실행



//000000000000000000000----------------------------------------------------------------------
/* =============유효성 검사(Validation)================ */

// 입력 값이 유효한 형태인지 표시하는 객체(체크리스트)
const checkObj = {
  "memberNickname" : true,
  "memberTel" : true
};

/* 닉네임 검사 */
// - 3 글자 이상
// - 중복 X
const memeberNickname = document.querySelector("#memberNickname");

// 기존 닉네임
const originalNickname = memeberNickname?.value;

// 객체?.속성 --> ? : 안전탐색 연산자
// - 객체가 null 또는 undefined 가 아니면 수행 (삼항연산자 유사함)

memeberNickname?.addEventListener("input", () => {
  // input 이벤트 : 입력과 관련된 모든 동작 (JS 를 이용한 값 세팅 제외)

  // 입력된 값 얻어오기(약쪽 공백 제거)
  const inputValue = memberNickname.value.trim();

  // 3 글자 이상 확인
  if (inputValue.length < 3) {
    memeberNickname.classList.add("red");
    memeberNickname.classList.remove("green");

    // 변수 memeberNickname 과 다름 객체의 key임!
    // 닉네임이 유효하지 않다고 기록
    checkObj.memberNickname = false;

    return;
  } 

  // 입력된 닉네임이 기존 닉네임과 같을 경우
  if (inputValue === originalNickname) {
    memeberNickname.classList.remove("green");
    memeberNickname.classList.remove("red");

    // 변수 memeberNickname 과 다름 객체의 key임!
    // 닉네임이 유효하지 않다고 기록
    checkObj.memberNickname = true;
    return;
  }

  /* 닉네임 유효성 검사 */
  /* JS 에서 닉네임을 입력받아 유효성 검사하는 코드 생성
[조건]
- 영어 또는 숫자 또는 한글만 작성 가능
- 3 글자 ~ 10 글자 */


// ^ : 시작
// $ : 끝     ex ^a~~b$ a 로 시작해서 b 로 끝나는 문자열
// [] : 한 칸(한 문자) 에 들어갈 수 있는 문자 패턴 기록
// + : 1 개 이상

//처음부터 끝까지 영어 숫자 한글만 들어갈 수 있다!

  const lengthCheck = inputValue.length >= 3 && inputValue.length <= 10;
  const validCharactersCheck = /^[a-zA-Z0-9가-힣]+$/.test(inputValue); // 영어, 숫자, 한글만 허용
  // 단자음 ex) ㅁㄴㅇㄴ 못하게 함

  if (!(lengthCheck && validCharactersCheck)) {
    memeberNickname.classList.add("red");
    memeberNickname.classList.remove("green");

    checkObj.memberNickname = false;

    return;
  }


  // 비동기로 입력된 닉네임
  // DB 에 존재하는지 확인하는 Ajax 코드(fetch() API) 작성

  // get 방식 요청 (쿼리스트링으로 파라미터 전달)
  fetch("/myPage/checkNickname?input=" + inputValue)
  .then(response => {
    if(response.ok) { // 응답 상태코드 200(성공) 인 경우
      return response.text(); // 응답 결과를 Text 형태로 반환
    }

    throw new Error("중복 검사 실패 : " + response.status);
  })
  .then(result => {
    // result == 첫 번째 then 에서 return 된 값

    if (result > 0) { // 중복인 경우
      memeberNickname.classList.add("red");
      memeberNickname.classList.remove("green");
  
      checkObj.memberNickname = false;
  
      return;
    }

    memeberNickname.classList.remove("red");
    memeberNickname.classList.add("green");

    checkObj.memberNickname = true; // 체크리스트에 true 기록

  })
  .catch(err => console.error(err));


});

// ----------------

/* 전화번호 유효성 검사 */

const memberTel = document.querySelector("#memberTel");

memberTel?.addEventListener("input", () => {

  // 입력된 전화 번호 
  const inputTel = memberTel.value.trim();

  // 전화번호 정규식 검사
  const validFormat = /^010\d{8}$/; 

  // 입력 받은 전화번호가 유효한 형식이 아닌 경우
  if(!validFormat.test(inputTel)){
    memberTel.classList.add("red");
    memberTel.classList.remove("green");
    checkObj.memberTel = false;
    return;
  } 

  // 유효한 경우
  memberTel.classList.add("green");
  memberTel.classList.remove("red");
  checkObj.memberTel = true;
});


//-------------------
/* 내 정보 수정 form 제출 시 */
const updateInfo = document.querySelector("#updateInfo");

updateInfo?.addEventListener("submit", e => {
  // checkObj 에 작성된 값 검사하기
  // -> 닉네임, 전화번호 유효한지 검사

  // for ~ in 구문 : JS 객체의 key 값을 하나씩 접근하는 반복문

  for(let key in checkObj) {
    // 닉네임, 전화번호 중 유효하지 않은 값이 있을 경우! 
    if (checkObj[key] === false) {

      let str = " 유효하지 않습니다"
      switch(key) {
        case "memberNickname" : str = "닉네임이"   + str; break;
        case "memberTel"      : str = "전화번호가" + str; break;
      }

      // 출력
      alert(str);

      // form 제출 막기
      e.preventDefault();

      // 유효하지 않은 곳에 포커스(커서활성화)
      document.getElementById(key).focus();

      return;
    } 
  } // for in end

  /* 주소 유효성 검사 */
  // 전부 비어있거나 전부 채워져있거나!!
  // const postcode = document.querySelector("#postcode").value.trim();
  // const address = document.querySelector("#address").value.trim();
  // const detailAddress = document.querySelector("#detailAddress").value.trim();

  const addr = document.querySelectorAll("[name = memberAddress]");

  let empty = 0;      // 비어있는 input 의 개수
  let notEmpty = 0;   // 비어있지 않은 input 의 개수

  // for ~ of 향상된 for 문
  for (let item of addr) {
    let len = item.value.trim().length // 작성된 값의 길이 

    if (len > 0) notEmpty++;      // 비어있지 않을 경우
    else         empty++;         // 비어있을 경우
  }

  // empty, notEmpty 중 3 이 하나도 없을 경우
  if (empty < 3 && notEmpty < 3) {
    alert("주소가 유효하지 않습니다(모두 작성 또는 미작성 요함)");
    e.preventDefault();
    return;
  }

});

// -------------------------------------------------------------------------------
// 비밀번호 변경 페이지 유효성 검사!!!

// 비밀번호 변경 form 태그
const changePw = document.querySelector("#changePw");

// 비밀번호 '변경' 버튼 클릭 시 또는 form 태그 내부 엔터 입력 시
// == submit (제출)
changePw?.addEventListener("submit", e => {
  // e.preventDefault(); // 기본 이벤트 막기
  // form 의 기본 이벤트인 제출 막기
  // -> 요효성 검사 조건이 만족되지 않을 떄 수행

  // 입력 요소 모두 얻어오기
  const currentPw    = document.querySelector("#currentPw");
  const newPw        = document.querySelector("#newPw");
  const newPwConfirm = document.querySelector("#newPwConfirm");
/* 1. 현재 비밀번호 
   새 비밀번호
   새 비밀번호 확인
   입력 여부 체크 */
  let str; // undefined JS 에서 자료형은 값이 대입될 때 정해지기 때문

  if (newPwConfirm.value.trim().length == 0) str = "새 비밀번호 확인을 입력해 주세요";
  if (newPw.value.trim().length == 0)     str = "새 비밀번호를 입력해 주세요";
  if (currentPw.value.trim().length == 0) str = "현재 비밀번호를 입력해 주세요";

  if (str !== undefined) { // 입력되지 않은 값이 존재
    alert(str);
    e.preventDefault();
    return; // 이후 검사 생략하고 종료/ submit 이벤트 핸들러 종료
  }

/* 2. 비밀번호가 알맞은 형태로 작성되었는가 확인
  - 영어(대소문자 가리지 X) 1 글자 이상
  - 숫자 1 글자 이상
  - 특수문자(! @ # _ -) 1 글자 이상
  - 최소 6 글자 최대 20 글자 */

  
  /* 정규 표현식 */
  // https://developer.mozilla.org/ko/docs/Web/JavaScript/Guide/Regular_expressions

  // - 문자열에서 특정 문자 조합을 찾기 위한 패턴

  // ex) 숫자가 3개이상 작성된 문자열 조합 찾기
  // "12abc"   -> X
  // "444"     -> O
  // "1q2w3e"  -> O

  /* [JS 정규 표현식 객체 생성 방법] 
  1. /정규표현식/
  2. new RegExp("정규표현식")

  https://regexr.com/     regexr.com
  */

  const lengthCheck = newPw.value.length >= 6 && newPw.value.length <= 20;
  const letterCheck = /[a-zA-Z]/.test(newPw.value); // 영어 알파벳 포함
  const numberCheck = /\d/.test(newPw.value); // 숫자 포함
  const specialCharCheck = /[!@#_-]/.test(newPw.value); // 특수문자 포함

  // 조건이 하나라도 만족하지 못하면
  if (!(lengthCheck && letterCheck && numberCheck && specialCharCheck)) { 
      alert("영어, 숫자, 특수문자 1 글자 6~20 자 사이로 입력해주세요");
      e.preventDefault();
      return;
  } 


  // 3. 새 비밀번호, 새 비밀번호 확인이 같은지 체크
  if (newPwConfirm.value !== newPw.value) {
    alert("새 비밀번호가 일치하지 않습니다");
    e.preventDefault();
    return;
  }

});


//--------------------------------------------------------------------------
/* 회원 탈퇴 페이지 유효성 검사 */
const secession = document.querySelector("#secession");
secession?.addEventListener("submit", e => {
  // 1) 비밀번호 입력 확인
  const memberPw = document.querySelector("#memberPw");

  if (memberPw.value.trim().length === 0) { // 미입력 시
    alert("비밀번호를 입력해 주세요");
    e.preventDefault();
    return;
  }

  // 2) 체크 되었는지 검사
  const agree = document.querySelector("#agree");

  if(agree.checked === false){ // 체크가 되어있지 않은 경우
    alert("약관을 확인하고 동의해 주세요");
    e.preventDefault();
    return;
  }

  // 3) confirm dldyd 정말 탈퇴할 것인지 확인
  if (confirm("정말 탈퇴하시겠습니까?") === false) {
    // 취소 클릭 시
    alert("탈퇴 취소");
    e.preventDefault();
    return;
  }
});