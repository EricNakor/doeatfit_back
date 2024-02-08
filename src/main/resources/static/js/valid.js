// const header = $("meta[name='_csrf_header']").attr('content');
// const token = $("meta[name='_csrf']").attr('content');

// 이메일 중복검사
function checkEmail() {
            var email = $('#email').val();
             $.ajax({
                url:"/api/user/check/duplicate/email",
                type : "get",
                dataType : 'json',
                data : {
                    "email" : email
                },
                success : function(data) {
                    if (data.success == false) {
                        alert("사용 가능한 이메일입니다.");
                    } else {
                        alert("중복된 이메일입니다.");
                    }
                    $("#duplicatedEmail").attr("value",data.success);
                },
                // beforeSend: function(xhr){
                // xhr.setRequestHeader(header, token);
                // }
            });
    }

// 닉네임 중복검사
function checkNickName(nickName) {
             $.ajax({
                url:"/api/user/check/duplicate/nickname",
                type : "get",
                dataType : 'json',
                data : {
                    "nickName" : nickName
                },
                success : function(data) {
                    if (data.success == false) {
                        alert("사용 가능한 닉네임입니다.");
                    } else {
                        alert("중복된 닉네임입니다.");
                    }
                    $("#duplicatedNickName").attr("value",data.success);
                },
                // beforeSend: function(xhr){
                // xhr.setRequestHeader(header, token);
                // }
            });
    }