function editNotice() {
    const uuid = document.URL.substring(document.URL.lastIndexOf('/') + 1);
    const notice = {
        title: document.getElementById('title').value,
        category: document.getElementById('category').value,
        content: document.getElementById('content').value
    };

    fetch('/api/notices/' + uuid, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(notice)
    }).then(function(response) {
        if (response.ok) {
            return response.json();
        }
        else {
            alert('공지사항 수정에 실패하였습니다.');
        }
    }).then(function(data) {
        location.href = '/notices/' + uuid;
    }).catch(function(error) {
        console.error('Error:', error);
    });
}