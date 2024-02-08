// var header = $("meta[name='_csrf_header']").attr('content');
// var token = $("meta[name='_csrf']").attr('content');

function sendNumber(){

        $("#mail_number").css("display","block");

        $.ajax({
            url:"/api/email/send/auth",
            type:"post",
            dataType:"json",
            contentType:"application/json",
            data:JSON.stringify({"email" : $("#email").val()}),
            success: function(data){
                alert("인증번호 발송");
                },
            // beforeSend: function(xhr){
            //     xhr.setRequestHeader(header, token);
            //     }
            });
    }

function confirmNumber(){
    $.ajax({
        url:"/api/email/verify",
        type:"get",
        dataType:"json",
        data:{"email" : $("#email").val(), "authNum" : $("#authNum").val()},
        success: function(data){
                if (data.success == true) {
                    alert("인증되었습니다.");
                } else {
                    alert("인증번호를 다시 확인해주세요.");
                }
                $("#verified").attr("value",data.success);
            },
        // beforeSend: function(xhr){
        //     xhr.setRequestHeader(header, token);
        //     }
    });
}