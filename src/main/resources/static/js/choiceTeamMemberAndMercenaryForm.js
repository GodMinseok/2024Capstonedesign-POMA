
// 매치 신청 버튼 클릭 이벤트
document.querySelector("#applyButton").addEventListener("click", function (e) {
    joinMatch();
});

async function joinMatch() {
    let mercenaryMatchId = document.getElementById('mercenaryMatchId').textContent;
    let sports = document.getElementById('sports').textContent.toLowerCase();
    let userIdList = Array.from(document.querySelectorAll('input[name="userIdList"]:checked'))
        .map(input => input.value);

    let mercenaryMaxSize = document.getElementsByName("mercenaryMaxSize")[0].value
    const headers = {
        "Content-Type": "application/json;charset=UTF-8",
        "x-requested-with": "XMLHttpRequest",
    };
    const body = {userIdList: userIdList, mercenaryMaxSize: mercenaryMaxSize};
    try {
        const response = await fetch("http://localhost:8080/mercenaryMatch/" + mercenaryMatchId + "/user", {
            method: "POST", headers: headers, body: JSON.stringify(body)
        });

        let data = await response.text()
        if (response.status === 201) {
            alert(data)
            window.location.href = "/mercenary/" + sports
        } else {
            alert(data)
        }

    } catch (error) {
        console.error("에러가 발생했습니다")
    }
}
