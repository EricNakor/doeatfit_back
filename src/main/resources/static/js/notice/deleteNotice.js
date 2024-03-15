function deleteNotice() {
    const uuid = document.URL.substring(document.URL.lastIndexOf('/') + 1);
    fetch('/api/def-cms/notices/' + uuid, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
    }).then(function(response) {
        if (response.ok) {
            return response.json();
        }
        else {
            alert('공지사항을 삭제하지 못했습니다.');
        }
    }).then(function(data) {
        location.href = '/notices';
    }).catch(function(error) {
        console.error('Error:', error);
    });
}