function login(){
    const form = document.getElementById("loginForm");
    let formData = new FormData(form);

    fetch("/login", {
        method: "POST",
        body: formData
    }).then(function(response) {
        if (response.ok) {
            location.href = "/my-page";
        }
        else {
            alert("로그인 실패.");
        }

    }).catch(function(error) {
        console.error("Error:", error);
    });
}
