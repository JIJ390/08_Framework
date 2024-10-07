/* 선택된 이미지 미리 보기 관련 요소 모두 얻어오기 */

/* [querySelector(), querySelectorAll() 의 문제점]
- 호출 되었을 시점의 요소 형태 그대로를 얻어옴

  [getELementsByClassName()]
- 요소를 얻어와서 실시간으로 변화되는 상태를 계속 추적함 
 */
const previewList = document.getElementsByClassName("preview");
const inputImageList = document.getElementsByClassName("inputImage");
const deleteImageList = document.getElementsByClassName("delete-image");

// 마지막으로 선택된 파일을 저장할 배열
const lastValidFiles = [null, null, null, null, null];


/** 미리보기 함수
 * @param  file : <input type="file"> 에서 선택된 파일
 * @param {*} order : 이미지 순서
 */
const updatePreview = (file, order) => {

  // 선택된 파일이 지정된 크기를 초과한 경우 선택 막기
  // 서버에 설정하긴 했으나 사용자가 알기 어려움
  const maxSize = 1024 * 1024 * 10 // 1MB 를 byte 단위로 작성 byte, kb, mb

  if (file.size > maxSize) {  // 파일 크기 초과 시
    alert("10 MB 이하의 이미지만 선택해 주세요");
    
    // 미리보기는 안되도 크기가 초과된 파일이 선택되어 있음!!!
    // 이 if 문 실행후에 lastValidFiles 백업이 됨

    // 이전 선택된 파일이 없는데 크기 초과 파일을 선택한 경우
    if (lastValidFiles[order] === null) {
      inputImageList[order].value = ""; // 선택 파일 삭제
      return;
    }

    // 이전 선택된 파일이 있는 경우
    const dataTransfer = new DataTransfer();

    // DataTransfer가 가지고 있는 files 필드에 
    // lastValidFiles[order] 추가 (이전에 선택한 파일)
    // for 문 안이 아니기 때문에
    // order 로 변경
    dataTransfer.items.add(lastValidFiles[order]);
    // 전달용 객체에 파일 담기

    // input의 files 변수에 lastValidFile이 추가된 files 대입
    inputImageList[order].files = dataTransfer.files;

    // 이전 선택된 파일로 미리보기 되돌리기
    updatePreview(lastValidFiles[i], i); 


    return;
  }

  // 선택된 이미지 백업
  lastValidFiles[order] = file;

  // JS 에서 제공하는 파일을 읽어오는 객체
  const reader = new FileReader();

  // file 을 읽어 오는데 
  // DataURL 형식으로 읽어옴
  // DataURL : 파일 전체 데이터가 브라우저가 해석할 수 있는 
  //           긴 주소형태 문자열로 변환
  reader.readAsDataURL(file);

    // 선택된 파일이 다 인식 되었을때
  reader.addEventListener("load", e => {
    previewList[order].src = e.target.result;
     // e.target.result == 파일이 저장된 주소형태 문자열 (reader == e.target?)
 
     // myPage.js 에서 가져옴 statusCheck 는 경우에 따른 제출여부를 따지는 것이기 때문에
     // 필요헚어서 구문 삭제
  })

}

//---------------------------------------------------------

/* input 태그, x 버튼에 이벤트 리스너 추가 */

for (let i = 0; i < inputImageList.length; i++) {

  // input 태그에 이미지 선택 시 미리보기 함수 호출
  inputImageList[i].addEventListener("change", e => {
    const file = e.target.files[0];

    if (file === undefined) { // 선택 취소 시

      // 해당(i) input 에서 이전에 선택한 파일이 없는 경우
      if (lastValidFiles[i] === null) return;

      // 이전에 선택한 파일이 있는 경우

      const dataTransfer = new DataTransfer();

      // DataTransfer가 가지고 있는 files 필드에 
      // lastValidFiles[i] 추가 (이전에 선택한 파일)
      dataTransfer.items.add(lastValidFiles[i]);

      // input의 files 변수에 lastValidFile이 추가된 files 대입
      inputImageList[i].files = dataTransfer.files;

      // 이전 선택된 파일로 미리보기 되돌리기
      updatePreview(lastValidFiles[i], i); 

      return;
    }

    updatePreview(file, i);
  })

  /* X 버튼 클릭 시 미리보기, 선택된 파일 삭제 */

  deleteImageList[i].addEventListener("click", () => {

    previewList[i].src = ""; // 미리보기 제거

    // input 내용 삭제, 경로를 직접 다룰 수 없어 value 로!
    inputImageList[i].value = ""; // 선택된 파일 삭제

    lastValidFiles[i] = null; // 백업본(최근 선택된 파일)

  })

} // for end



/* 제목 내용 미작성 시 제출 불가 */
const form = document.querySelector("#boardWriteFrm");

form.addEventListener("submit", e => {
  // 제목, 내용 input 태그(요소)
  const boardTitle   = document.querySelector("[name=boardTitle]")
  const boardContent = document.querySelector("[name=boardContent]")

  if (boardTitle.value.trim().length === 0) {
    alert("제목을 작성해 주세요");
    boardTitle.focus();
    e.preventDefault();
    return;
  }

  if (boardContent.value.trim().length === 0) {
    alert("내용을 작성해 주세요");
    boardContent.focus();
    e.preventDefault();
    return;
  }

})