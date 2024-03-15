function editWorkout(){

    const uuid = document.URL.substring(document.URL.lastIndexOf('/') + 1);
    const stringList = [];
    $('.string-input').each(function() {
        stringList.push($(this).val());
    });

    // 체크박스에서 선택된 값을 가져옵니다.
    const agonistMuscles = [];
    $('input[name="agonistMuscles"]:checked').each(function() {
        agonistMuscles.push($(this).val());
    });

    const antagonistMuscles = [];
    $('input[name="antagonistMuscles"]:checked').each(function() {
        antagonistMuscles.push($(this).val());
    });

    const synergistMuscles = [];
    $('input[name="synergistMuscles"]:checked').each(function() {
        synergistMuscles.push($(this).val());
    });

    const workout = {
        workoutName: document.getElementById('workoutName').value,
        workoutDifficulty: document.getElementById('workoutDifficulty').value,
        agonistMuscles: agonistMuscles ,
        antagonistMuscles: antagonistMuscles,
        synergistMuscles: synergistMuscles,
        descriptions :stringList
    };

    // Create a FormData object to handle multipart/form-data
    const formData = new FormData();

    // Get the file input element
    const fileInput = document.getElementById('mediaFile');

    // Check if a file is selected
    if (fileInput.files && fileInput.files[0]) {
        formData.append("mediaFile", fileInput.files[0]);
    }
    formData.append("editWorkoutRequest", new Blob([JSON.stringify(workout)], {type: "application/json"}));

    $.ajax({
        url: '/api/def-cms/workouts/' + uuid,
        type: 'PUT',
        data: formData,
        processData: false,
        contentType: false,
        success: function(data) {
            location.href = '/workouts/' + data.uuid;
        },
        error: function(error) {
            console.error('Error:', error);
            alert('운동 수정에 실패하였습니다.');
        }
    });
}