// 매치 신청 버튼 클릭 이벤트
document.querySelector(".submitBtn").addEventListener("click", function (e) {
    reservation();
});

async function reservation() {
    // 현재 URL을 가져옵니다.
    let currentUrl = window.location.pathname;
    // 정규 표현식을 사용하여 '/reservation/sports' 부분을 추출합니다.
    let regex = /\/reservation\/(\w+)/;
    let sports = currentUrl.match(regex)[1];

    let groundId = document.getElementsByName("groundId")[0].value;
    let date = document.getElementsByName("date")[0].value;
    let time = document.getElementsByName("time")[0].value;
    let gender = document.getElementsByName("gender")[0].value;
    let maxSize = document.getElementsByName("maxSize")[0].value;
    let match = document.getElementsByName("match")[0].value;
    let mercenarySize = document.getElementsByName("mercenarySize")[0].value;
    let userIdList = Array.from(document.querySelectorAll('input[name="userIdList"]:checked'))
        .map(input => input.value);

    const headers = {
        "Content-Type": "application/json;charset=UTF-8",
        "x-requested-with": "XMLHttpRequest",
    };
    const body = {
        groundId: groundId,
        date: date,
        time: time,
        gender: gender,
        maxSize: maxSize,
        match: match,
        mercenarySize: mercenarySize,
        userIdList : userIdList
    };

    try {
        const response = await fetch("http://localhost:8080/reservation/"+sports, {
            method: "POST", headers: headers, body: JSON.stringify(body)
        });

        if (response.status === 201) {
            window.location.href = `/${match.toLowerCase()}/${sports}?date=${date}&location=전체&gender=${gender}`;
        }
        let data = await response.text();
        alert(data)
    } catch (error) {
        alert("에러가 발생했습니다")
    }
}