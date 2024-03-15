function postNotice() {
    const notice = {
        title: document.getElementById('title').value,
        category: document.getElementById('category').value,
        content: document.getElementById('content').value
    };

    fetch('/api/def-cms/notices', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(notice)
    }).then(function(response) {
        if (response.ok) {
            return response.json();
        }
        else {
            alert('공지사항 작성에 실패하였습니다.');
        }
    }).then(function(data) {
        location.href = '/notices/' + data.uuid;
    }).catch(function(error) {
        console.error('Error:', error);
    });
}