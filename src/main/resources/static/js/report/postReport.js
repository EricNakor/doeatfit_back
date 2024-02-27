function postReport() {
    const formData = new FormData();
    const report = {
        reportCategory: document.getElementById('category').value,
        title: $('#title').val(),
        content: $('#content').val()
    };

    formData.append("postReportRequestDto",
        new Blob([JSON.stringify(report)],
            {type: "application/json"}))

    // 파일 리스트를 formData 에 추가
    const fileInput = document.getElementById('reportFiles');

    if (fileInput.files && fileInput.files.length > 0) {
        for (const file of fileInput.files) {
            formData.append('reportFile', file);
        }
    }

    $.ajax({
        url: "/api/reports",
        type: "post",
        dataType: 'json',
        contentType: false,
        processData: false,
        data: formData,
        success: function (data) {
            if (data.success === false) {
                alert("문의 작성 실패")
                location.reload();
            } else {
                location.href = '/my-page/reports/' + data.uuid;
            }
        }
    });
}