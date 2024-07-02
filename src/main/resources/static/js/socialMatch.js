// 매치 신청 버튼 클릭 이벤트
let submitBtn = document.querySelectorAll(".submitBtn")
submitBtn.forEach((target) => target.addEventListener("click", joinMatch));

async function joinMatch() {

    let socialMatchId = this.parentElement.getElementsByClassName("socialMatchId")[0].textContent
    // 현재 URL을 가져옵니다.
    let currentUrl = window.location.pathname;
    // 정규 표현식을 사용하여 '/social/sports' 부분을 추출합니다.
    let regex = /\/social\/(\w+)/;
    let sports = currentUrl.match(regex)[1];
    const headers = {
        "Content-Type": "application/json;charset=UTF-8",
        "x-requested-with": "XMLHttpRequest",
    };
    const body = socialMatchId
    try {
        const response = await fetch("http://localhost:8080/social/"+sports, {
            method: "POST", headers: headers, body: JSON.stringify(body)
        });

        if (response.status === 201) {
            window.location.reload()
        }
        let data = await response.text();
        alert(data)
    } catch (error) {
        alert("에러가 발생했습니다")
    }
}