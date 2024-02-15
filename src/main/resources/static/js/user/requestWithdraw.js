// 회원 탈퇴
function requestWithdraw() {
    $.ajax({
        url: "/api/user/withdraw",
        type: "delete",
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            "passwd": $('#passwd').val()
        }),
        success: function (data) {
            if (data.success === false) {
                alert("회원탈퇴 실패")
                location.reload();
            } else {
                location.replace("/login");
            }
        },
        // beforeSend: function (xhr) {
        //     xhr.setRequestHeader(header, token);
        // }
    });
}