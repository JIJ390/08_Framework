/* 할 일 추가 관련 요소를 얻어와 변수에 저장 */

const todoTitle  = document.querySelector("#todoTitle");
const todoDetail = document.querySelector("#todoDetail");
const addBtn     = document.querySelector("#addBtn");

// 추가 버튼(#addBtn) 클릭시

addBtn.addEventListener("click", () => {
  // 클릭된 순간 화면에 작성되어있는 제목 내용 얻어오기
  const title  = todoTitle.value;
  const detail = todoDetail.value;

  // Ajax(비동기) POST 방식으로 요청하기 위한 fetch() API 코드 작성 

  // HTTP 통신 시
  // - header : 요청 관련 정보 (어떤 데이터, 어디서 요청)
  // - body   : POST/PUT/DELETE 요청 시 전달할 데이터
  fetch("/todo/add", { // 지정된 주소로 비동기 요청(ajax)
    method : "POST", // 데이터 전달 방식을 POST로 지정
    headers: {"Content-Type": "application/json"}, // 요청 데이터의 형식을 JSON으로 지정
    body : JSON.stringify( {"todoTitle" : title, "todoDetail" : detail} ) 
    // JS객체를 JSON 형태(문자열화, stringify)로 변환하여 Body에 추가
    })
    .then(response => response.text()) // 요청에 대한 응답 객체(response)를 필요한 형태로 파싱
    // response.text() : 컨트롤러 반환 값을 text 형태로 변환해서
    //                   아래 두 번째 then 매개 변수로 전달
    //                   이 경우 1 + 알파가 반환값으로 오는데 이것을 1로 변환 시킴
    .then(result => {
      console.log("result : ", result);

      if (result > 0) {
        alert("할 일 추가 성공");
        // 화면상에서 작성한 내용들 지우기 (input 이라 value)
        todoTitle.value  = "";
        todoDetail.value = "";
      } else {
        alert("추가 실패");
      }
    }); // 첫 번째 then에서 파싱한 데이터를 이용한 동작 작성

});


//////////////////////////////////////////////////////////////////////////////

const inputTodoNo = document.querySelector("#inputTodoNo");
const searchBtn   = document.querySelector("#searchBtn");

searchBtn.addEventListener("click", () => {

  // #inputTodoNo 에 작성된 값 얻어오기
  const todoNo = inputTodoNo.value;

  // 비동기로 todoNo를 서버에 전달 후 
  // 해당하는 할 일 제목 가져오기(fetch)
  // -GET 방식 (주소에 제출할 값이 쿼리 스트링 형태로 담긴다!!!)
  fetch("/todo/searchTitle?todoNo=" + todoNo)
  .then(response => response.text()) 
  .then(todoTitle => {
    // 매개 변수 todoTitle 
    // == 서버에서 반환 받은 "할 일 제목" 이 담긴 변수
    alert(todoTitle);
  });


});