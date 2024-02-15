// var token = $("meta[name='_csrf']").attr('content');
// var header = $("meta[name='_csrf_header']").attr('content');

// 이메일 찾기
function findEmail() {
    $.ajax({
        url: "/api/user/find/email",
        type: "post",
        contentType: "application/json",
        dataType: 'json',
        data: JSON.stringify({
            // stringify : JS 객체를 Json 문자열로 직렬화
            // 직렬화가 뭔지 공부해보자.
            "name": $('#name').val(),
            "gender": $('#gender').val(),
            "birth": $('#birth').val()
        }),
        success: function (data) {
            if (!data.emails.length) {
                alert("없어요");
            } else {
                $.each(data.emails, function (index, item) { // 데이터 =item
                    $("#demo").append(item + "<br>");
                });
            }
        },
        // beforeSend: function (xhr) {
        //     xhr.setRequestHeader(header, token);
        // }
    });
}