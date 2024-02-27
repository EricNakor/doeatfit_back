$('#search-form').on('submit', function (e) {
    e.preventDefault();
    $.ajax({
        url: "api/reports/search",
        type: "get",
        data: $(this).serialize(),
        success: function (data) {
            let list = data; // list = new Array(); 짤린부분에서 미리 배열 선언해줫음
            for (var i = 0; i < list.length; i++)
                alert(list[i].status);
        }
    });
});