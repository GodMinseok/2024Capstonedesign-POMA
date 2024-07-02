// 매치 신청 버튼 클릭 이벤트
let submitBtn = document.querySelectorAll(".submitBtn")
submitBtn.forEach((target) => target.addEventListener("click", joinMatch));

async function joinMatch() {
    let teamMatchId = this.parentElement.getElementsByClassName("teamMatchId")[0].textContent
    // 현재 URL을 가져옵니다.
    let currentUrl = window.location.pathname;
    // 정규 표현식을 사용하여 '/team/sports' 부분을 추출합니다.
    let regex = /\/team\/(\w+)/;
    let sports = currentUrl.match(regex)[1];


    try {
        const response = await fetch("http://localhost:8080/teamMatch/" + teamMatchId + "/user", {
            method: "GET"
        });

        let data = await response.text()
        if (response.status === 200) {
            let html = new DOMParser().parseFromString(data,'text/html').documentElement
            const script = html.querySelector('script');
            const newScript = document.createElement('script');
            newScript.src = script.src
            script.remove()
            document.querySelector("html").innerHTML = html.innerHTML
            document.body.appendChild(newScript);
        } else {
            alert(data)
        }

    } catch (error) {
        console.error("에러가 발생했습니다")
    }
}