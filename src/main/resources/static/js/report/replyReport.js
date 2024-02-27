function replyReport() {
    const uuid = document.URL.substring(document.URL.lastIndexOf('/') + 1);
    const formData = new FormData();
    const reply = {
        reportStatus: document.getElementById('status').value,
        reply: $('#reply').val()
    };

    formData.append("replyReportDto",
        new Blob([JSON.stringify(reply)],
            {type: "application/json"}))

    // 파일 리스트를 formData 에 추가
    const fileInput = document.getElementById('replyFiles');

    if (fileInput.files && fileInput.files.length > 0) {
        for (const file of fileInput.files) {
            formData.append('replyFiles', file);
        }
    }

    $.ajax({
        url: "/api/reports/reply/" + uuid,
        type: "put",
        dataType: 'json',
        contentType: false,
        processData:false,
        data: formData,
        success: function (data) {
            if (data.success === false) {
                alert("답변 작성 실패")
                location.reload();
            } else {
                location.href = '/reports/all';
            }
        }
    });
}