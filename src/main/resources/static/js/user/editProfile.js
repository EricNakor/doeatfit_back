// const header = $("meta[name='_csrf_header']").attr('content');
// const token = $("meta[name='_csrf']").attr('content');

// 비밀번호 수정
function editPasswd() {
    $.ajax({
        url: "/api/user/passwd",
        type: "put",
        contentType: "application/json",
        dataType: 'json',
        data: JSON.stringify({
            // stringify : JS 객체를 Json 문자열로 직렬화
            // 직렬화가 뭔지 공부해보자.
            "currentPasswd": $('#currentPasswd').val(),
            "newPasswd": $('#newPasswd').val()
        }),
        success: function (data) {
            if (data.success == false) {
                alert("변경 실패");
            } else {
                alert("변경 성공");
                location.replace("/my-page");
            }
            $("#isPasswdEdited").attr("value", data.success);
        },
        // beforeSend: function (xhr) {
        //     xhr.setRequestHeader(header, token);
        // }
    });
}

// 닉네임수정
function editNickName() {
    $.ajax({
        url: "/api/user/nickname",
        type: "put",
        contentType: "application/json",
        dataType: 'json',
        data: JSON.stringify({
            "newNickName": $('#newNickName').val()
        }),
        success: function (data) {
            if (data.success == false) {
                alert("변경 실패");
            } else {
                alert("변경 성공");
                location.replace("/my-page");
            }
            $("#isNickNameEdited").attr("value", data.success);
        },
        // beforeSend: function (xhr) {
        //     xhr.setRequestHeader(header, token);
        // }
    });
}

// 프로필 이미지 저장
function saveProfileImg() {
    $.ajax({
        url: "/api/user/profile-img",
        type: "post",
        contentType: "multipart/form-data",
        data: $('#profileImgFile').val(),
        success: function (data) {
            if (data.success == false) {
                alert("변경 실패");
            } else {
                alert("변경 성공");
                location.reload();
            }
        },
        // beforeSend: function (xhr) {
        //     xhr.setRequestHeader(header, token);
        // }
    });
}