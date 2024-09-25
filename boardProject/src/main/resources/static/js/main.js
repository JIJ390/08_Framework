
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


// ------------------------------------------------------------------------
/* 메인 페이지 회원 목록 비동기 조회 함수 */
const selectMemberList = () => {

  // 1) 비동기로 모든 회원의
  // 회원 번호, 이메일, 탈퇴상태 조회하기

  fetch("/selectMemberList")
  .then(response => {
    // 응답 성공 시 JSON 형태의 응답 데이터를 JS 객체로 변경
    if (response.ok) return response.json();
    throw new Error("조회 오류");
  })
  .then(list => {
    // console.log(list);

    // 1) #memberList 기존 내용 없애기
    memberList.innerHTML = "";

    // 2) 조회 결과인 list 를 반복 접근해서
    //    #memberList 에 조회된 내용으로 tr, td, th 만들어 넣기

    //  for of
    //  for in
    //  forEach
    list.forEach(member => {
      // 매개변수 member == 조회된 list 에서 하나씩 꺼낸 요소
      
      // tr 요소 만들기
      const tr = document.createElement("tr");
      
      // th 요소 만들어서 회원 정보 세팅
      const th1 = document.createElement("th");
      th1.innerText = member.memberNo;

      const td2 = document.createElement("td");
      td2.innerText = member.memberEmail;
      
      const th3 = document.createElement("th");
      th3.innerText = member.memberDelFl;
      
      const th4 = document.createElement("th");
      const loginBtn = document.createElement("button");
      loginBtn.innerText = "로그인";
      th4.append(loginBtn);

      const th5 = document.createElement("th");
      const initBtn = document.createElement("button");
      initBtn.innerText = "비밀번호 초기화";
      th5.append(initBtn);

      const th6 = document.createElement("th");
      const changeBtn = document.createElement("button");
      changeBtn.innerText = "탈퇴 상태 변경";
      th6.append(changeBtn);

      tr.append(th1, td2, th3, th4, th5, th6);

      memberList.append(tr);
      
    })

  })
  .catch(err => console.error(err));

};