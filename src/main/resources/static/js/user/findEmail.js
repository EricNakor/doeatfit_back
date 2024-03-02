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
            "gender": $('input[name=gender]:checked').val(),
            "birth": $('#birth').val()
        }),
        success: function (data) {
            if (!data.emails.length) {
                alert("없어요");
            } else {
                // 비우기
                $("#demo").replaceWith("<p id=\"demo\"></p>");
                //todo: 다시 검색 시 새로 노출될 수 있도록
                // 검색 시 현재 창 이동되어 검색된 이메일 정보와
                // 비밀번호 찾기, 로그인 하러가기 버튼 노출
                // front 작업 시 재 고려

                // 리스트 추가
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