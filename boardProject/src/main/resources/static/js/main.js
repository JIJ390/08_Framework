
// 쿠키에 저장된 여러 값 중 key 가 일치하는 value 반환
function getCookie(key) {
  
  // 1.cookie 전부 얻어오기 (string)
  const cookies = document.cookie; // "K=V;K=V;..."

  // 2.";" 을 구분자로 삼아서 배열 형태로 쪼개기(split)
  const arr = cookies.split(";"); // ["K=V", "K=V", ...]
  // console.log(arr);

  const cookieObj = {};
  
  // 3. 쪼개진 배열 요소를 하나씩 꺼내서
  //    JS 객체에 K:V 형태로 추가
  for (let entry of arr) {
    // entry == "k=v"
    // -> "=" 기준으로 쪼개기
    const temp = entry.split("="); // ["K", "V"]

    cookieObj[temp[0]] = temp[1];
  }

  // console.log(cookieObj);

  // console.log(cookieObj[key]);

  return cookieObj[key];

}

// 페이지 로딩 후
document.addEventListener("DOMContentLoaded", () => {
  const saveEmail = getCookie("saveEmail");

  // 저장된 이메일일 없을 경우
  if (saveEmail == undefined) return;

  const memberEmail = document.querySelector("[name=memberEmail");
  const checkbox    = document.querySelector("[name=saveEmail");

  // 로그인이 되어있는 상태 (로그인 이메일 입력창이 표시되지 않을때)
  if (memberEmail == null) return;

  // 이메일 입력란에 저장된 이메일 출력
  memberEmail.value = saveEmail;

  // 이메일 저장 체크박스를 체크 상태로 바꾸기
  checkbox.checked = true;
})