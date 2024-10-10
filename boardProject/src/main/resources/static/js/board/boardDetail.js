
// 현재 상세 조회한 게시글 번호
const boardNo = location.pathname.split("/")[3];

/* 좋아요 하트 클릭 시 */
const boardLike = document.querySelector("#boardLike");

boardLike.addEventListener("click", e => {

  // 1. 로그인 여부 검사
  if (loginCheck === false) {
    alert("로그인 후 이용해 주세요");
    return;
  }



  // 2. 비동기로 좋아요 요청
  fetch("/board/like", {
    method : "POST",  // delete 와 insert를 경우에 따라 진행하기 때문에 그냥 post
    headers : {"Content-Type" : "application/json"},
    body : boardNo
  })
  .then(response => {
    if (response.ok) return response.json();
    throw new Error("좋아요 실패");
  })
  .then(result => {
    // console.log("result : ", result);

    // 좋아요 결과가 담긴 result 객체의 check 값에 따라
    // 하트 아이콘을 비우기 / 채우기 지정
    if (result.check === 'insert') {
      boardLike.classList.add("fa-solid");
      boardLike.classList.remove("fa-regular");
    } else {
      boardLike.classList.add("fa-regular");
      boardLike.classList.remove("fa-solid");
    }

    // 좋아요 하트 다음 형제 요소의 내용을
    // result.count 로 변경
    boardLike.nextElementSibling.innerText = result.count;
  })
  .catch(err => console.error(err));

})


// -------------------------------------------------------------------------------

/* 삭제 버튼 클릭 시
   - 삭제 버튼 클릭 시 "정말 삭제 하시겠습니까?" confirm()
   - /editBoard/delete, POST 방식, 동기식 요청

   -> form 태그 생성(동기식 POST) + 게시글 번호가 세팅된 input
   -> body 태그 제일 아래 추가해서 submit()

   - 서버(java)에서 로그인한 회원의 회원 번호를 얻어와
     로그인한 회원이 작성한 글이 맞는지 SQL 에서 검사
*/

const deleteBtn = document.querySelector("#deleteBtn");

deleteBtn?.addEventListener("click", () => {

  if (confirm("정말 삭제 하시겠습니까?") == false) return;

  const url = "/editBoard/delete";    // 요청 주소
  // 게시글 번호 == 전역 변수 boardNo

  const form = document.createElement("form");
  form.action = url;            // 요청 주소
  form.method = "POST";         // 메소드 지정

  // input 태그 생성
  const input = document.createElement("input");
  input.type  = "hidden";
  input.name  = "boardNo";
  input.value = boardNo;  

  form.append(input); // form 자식으로 input 추가

  // body 태그 자식으로 form 추가
  document.querySelector("body").append(form);

  form.submit(); // 제출
  
});


//---------------------------------------------

/* 수정 버튼 클릭 시
  - /editBoard/{boardCode}/{boardNo}/update, POST, 동기식
  -> form 태그 생성
  -> body 태그 제일 아래 추가해서 submit()

  - 서버(Java) 에서 로그인한 회원의 회원 번호를 얻어와
    로그인한 회원이 작성한 글이 맞는지 SQL 에서 검사
    
    맞긍 경우 수정화면으로 전환
*/

const updateBtn = document.querySelector("#updateBtn");

updateBtn?.addEventListener("click", () => {

  const form = document.createElement("form");

  // /editBoard/{boardCode}/{boardNo}/update}
  form.action = location.pathname.replace("board", "editBoard") + "/updateView";            // 요청 주소
  form.method = "POST";         // 메소드 지정


  // body 태그 자식으로 form 추가
  document.querySelector("body").append(form);

  form.submit(); // 제출
})