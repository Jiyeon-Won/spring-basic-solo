function movePage(str) {
    window.location.href = str;
}

function submit() {
    let parent = $(this).parent("form");
    let todoId = parent.find('#todo_id').val();
    let data = {
        id: todoId,
        title: parent.find('#title').val(),
        content: parent.find('#content').val(),
        person: parent.find('#person').val(),
        password: parent.find('#password').val()
    };
    if (isEmpty(todoId)) {
        $.ajax({
            url: "/todo",
            type: "post",
            data: JSON.stringify(data),
            success: function(response) {
            }
        });
    } else {
        $.ajax({
            url: "/todo",
            type: "put",
            data: JSON.stringify(data),
            success: function(response) {
            }
        });
    }
}

function isEmpty(value) {
    if (value === "" || value === null || value === undefined
        || ((typeof value === "object" && Object.keys(value).length === 0))) {
        return true;
    }
    return false;
}