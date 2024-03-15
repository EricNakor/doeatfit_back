function postWorkout(){
    const stringList = [];
    $('.string-input').each(function() {
        stringList.push($(this).val());
    });

    // 체크박스에서 선택된 값을 가져옵니다.
    const agonistMuscleValues = [];
    $('input[name="agonistMuscles"]:checked').each(function() {
        agonistMuscleValues.push($(this).val());
    });

    const antagonistMuscleValues = [];
    $('input[name="antagonistMuscles"]:checked').each(function() {
        antagonistMuscleValues.push($(this).val());
    });

    const synergistMuscleValues = [];
    $('input[name="synergistMuscles"]:checked').each(function() {
        synergistMuscleValues.push($(this).val());
    });

    const workout = {
        workoutName: document.getElementById('workoutName').value,
        workoutDifficulty: document.getElementById('workoutDifficulty').value,
        agonistMuscles: agonistMuscleValues ,
        antagonistMuscles: antagonistMuscleValues,
        synergistMuscles: synergistMuscleValues,
        descriptions :stringList,
    };

    // Create a FormData object to handle multipart/form-data
    const formData = new FormData();

    formData.append("postWorkoutRequest", new Blob([JSON.stringify(workout)], {type: "application/json"}));

    // Get the file input element
    const fileInput = document.getElementById('mediaFile');

    // Check if a file is selected
    if (fileInput.files && fileInput.files[0]) {
        formData.append('mediaFile', fileInput.files[0]);
    }

    fetch('/api/def-cms/workouts', {
        method: 'POST',
        body: formData
    }).then(function(response) {
        if (response.ok) {
            return response.json();
        }
        else {
            alert('운동 작성에 실패하였습니다.');
        }
    }).then(function(data) {
        location.href = '/workouts/' + data.uuid;
    }).catch(function(error) {
        console.error('Error:', error);
    });
}