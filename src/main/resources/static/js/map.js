function initialize() {
    const texts = document.getElementsByClassName("map_select_tx")
    const imgs = document.getElementsByClassName("map_img")
    const resets = document.getElementsByClassName("reset")
    const stamp = document.getElementsByClassName("stamp_")

    
    // 텍스트 색, 이미지, 리셋 버튼 초기화
    function resetAll() {
        for (var text of texts) {
            text.style.color = "#000"
        }
        for (var img of imgs) {
            img.style.display = "none"
        }
        for (var reset of resets) {
            reset.style.display = "none"
        }
    }

    // 위치 클릭했을 때
    function textClick(event) {
        const textId = event.target.id;
        resetAll();
        for (var s of stamp) {
            s.style.display = "none"
        }

        if (textId == "booth") {
            event.target.style.color = '#F06786';
            imgs[1].style.display = "block";
            resets[0].style.display = "block"
        }
        else if (textId == "food") {
            event.target.style.color = '#FAD07C';
            imgs[2].style.display = "block";
            resets[1].style.display = "block"
        }
        else if (textId == "stage") {
            event.target.style.color = '#6C5C9C';
            imgs[3].style.display = "block";
            resets[2].style.display = "block"
        }
    }

    // 리셋 버튼 클릭
    function defaultState(event) {
        resetAll();
        imgs[0].style.display = "block";

        for (var s of stamp) {
            s.style.display = "inline"
        }
    }
    
    // 이벤트 리스너
    for (var text of texts) {
        text.addEventListener("click", textClick);
    }

    for (var reset of resets) {
        reset.addEventListener("click", defaultState);
    }

    
}

// DOMContentLoaded 이벤트가 발생할 때 initialize 함수를 호출
document.addEventListener("DOMContentLoaded", initialize);