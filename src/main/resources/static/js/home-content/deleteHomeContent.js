function deleteHomeContent(){
    const uuid = document.URL.substring(document.URL.lastIndexOf('/') + 1);
    fetch("/api/def-cms/home-contents/" + uuid, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json"
        },
    }).then(function(response) {
        if (response.ok) {
            return response.json();
        }
        else {
            alert("홈컨텐츠를 삭제하지 못했습니다.");
        }
    }).then(function(data) {
        location.href = "/def-cms/home-contents";
    }).catch(function(error) {
        console.error("Error:", error);
    });
}