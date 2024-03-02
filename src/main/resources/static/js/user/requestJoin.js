// 회원 가입
function requestJoin() {
    let passwd = $('#passwd1').val();

    if (passwd !== $('#passwd2').val()) {
        alert("비밀번호를 다시 확인해주세요.");
        return;
    }

    $.ajax({
        url: "/api/user/join",
        type: "post",
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            "email": $('#email').val(),
            "passwd": passwd,
            "name": $('#name').val(),
            "nickName": $('#nickName').val(),
            "gender": $('input[name=gender]:checked').val(),
            "birth": $('#birth').val()
        }),
        success: function (data) {
            if (data.success === false) {
                alert("회원가입 실패")
                location.reload();
            } else {
                location.replace("/login");
            }
        }
        // beforeSend: function (xhr) {
        //     xhr.setRequestHeader(header, token);
        // }
    });
}