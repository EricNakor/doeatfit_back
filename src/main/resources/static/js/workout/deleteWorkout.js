function deleteWorkout() {
    const uuid = document.URL.substring(document.URL.lastIndexOf('/') + 1);
    fetch('/api/def-cms/workouts/' + uuid, {
        method: 'DELETE'
    }).then(function(response) {
        if (response.ok) {
            return response.json();
        }
        else {
            alert('운동을 삭제하지 못했습니다.');
        }
    }).then(function(data) {
        location.href = '/workouts';
    }).catch(function(error) {
        console.error('Error:', error);
    });
}