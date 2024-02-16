// var header = $("meta[name='_csrf_header']").attr('content');
// var token = $("meta[name='_csrf']").attr('content');

// 임시 비밀번호를 발급받기 위한 가입정보 찾기
function findUserInfo() {
             $.ajax({
                url:"/api/user/find/info",
                type : "post",
                contentType : "application/json",
                dataType : 'json',
                data :JSON.stringify({
                    "email" : $('#email').val(),
                    "name" : $('#name').val(),
                    "gender" : $('#gender').val(),
                    "birth" : $('#birth').val()
                }),
                success : function(data) {
                   if (data.success == true) {
                           alert("있어요.");
                           setTempPasswd();
                       } else {
                           alert("없어요.");
                       }
                       $("#exist").attr("value",data.success);
                },
                 // beforeSend: function(xhr){
                 // xhr.setRequestHeader(header, token);
                 // }
            });
    }

// 임시 비밀번호 설정
function setTempPasswd() {
            var email =
             $.ajax({
                url:"/api/user/send-temp-passwd",
                type : "post",
                contentType : "application/json",
                data :JSON.stringify({
                    "email" : $('#email').val(),
                    "name" : $('#name').val(),
                    "gender" : $('#gender').val(),
                    "birth" : $('#birth').val()
                }),
                 // beforeSend: function(xhr){
                 // xhr.setRequestHeader(header, token);
                 // }
            });
    }