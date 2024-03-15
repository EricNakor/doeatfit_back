function editRole(){
    const editRoleRequest ={
        email: document.getElementById('email').value,
        newUserRole: document.getElementById('roleEnum').value
    };

    fetch("/api/def-cms/user/role", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(editRoleRequest)
    }).then(function(response) {
        if (response.ok) {
            return response.json();
        }
        else {
            alert("유저 역할을 수정하지 못했습니다.");
        }
    }).then(function(data) {
        location.href = "/def-cms/users";
    }).catch(function(error) {
        console.error('Error:', error);
    });
}