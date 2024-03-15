function postHomeContent(){
    const homeContent = {
        isBeingUsed: document.getElementById('isBeingUsed').checked,
        category: document.getElementById('category').value,
        content: document.getElementById('content').value
    };
    console.log(homeContent);
    fetch("/api/def-cms/home-contents", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(homeContent)
    }).then(function(response) {
        if (response.ok) {
            return response.json();
        }
        else {
            alert("홈 컨텐츠 작성에 실패하였습니다.");
        }
    }).then(function(data) {
        location.href = "/def-cms/home-contents";
    }).catch(function(error) {
        console.error("Error:", error);
    });
}
