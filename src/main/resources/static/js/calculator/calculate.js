// var token = $("meta[name='_csrf']").attr('content');
// var header = $("meta[name='_csrf_header']").attr('content');

// 계산하기
function calculate() {
    $.ajax({
        url: "/api/calculate",
        type: "post",
        contentType: "application/json",
        dataType: 'json',
        data: JSON.stringify({
            "gender": $('#gender').val(),
            "age": $('#age').val(),
            "height": $('#height').val(),
            "weight": $('#weight').val(),
            "bmr": $('#bmr').val(),
            "activityLevel": $('#activityLevel').val(),
            "dietGoal": $('#dietGoal').val()
        }),
        success: function (data) {
            location.href = '/calculator/result/' + data.uuid;
        }
        // beforeSend: function (xhr) {
        //     xhr.setRequestHeader(header, token);
        // }
    });
}

// 결과 삭제하기
function deleteHistory(uuid) {
    $.ajax({
        url: "/api/calculator/result/" + uuid,
        type: "delete",
        dataType: 'json',
        success: function (data) {
            location.reload();
        }
        // beforeSend: function (xhr) {
        //     xhr.setRequestHeader(header, token);
        // }
    });
}

// 3끼, 4끼, 5끼 계산하기
function mealPerDay() {

    // 일일 섭취 영양소 결과 가져오기
    const carb = parseInt(document.getElementById('carb').innerHTML);
    const protein = parseInt(document.getElementById('protein').innerHTML);
    const fat = parseInt(document.getElementById('fat').innerHTML);

    // 선택된 식사 횟수
    const mealNum = parseInt($('input[name="meal"]:checked').val());

    // 영양소 계산
    const carbPerServing = (carb / mealNum).toFixed(1);
    const proteinPerServing = (protein / mealNum).toFixed(1);
    const fatPerServing = (fat / mealNum).toFixed(1);

    // 결과 출력
    const mealResultText = `
    <p><strong>탄수화물: ${carbPerServing.toLocaleString()} g</p>
    <p><strong>단백질: ${proteinPerServing.toLocaleString()} g</p>
    <p><strong>지방: ${fatPerServing.toLocaleString()} g</p>
  `;

    $('#mealResult').html(mealResultText);
}