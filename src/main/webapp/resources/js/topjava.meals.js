const mealAjaxUrl = "profile/meals/";
const filterAjaxUrl = mealAjaxUrl + "filter"
const filterForm = $('#filter');
const filterInputs = document.querySelectorAll('#filter input');
const hasFilters = () => {
    filterInputs.forEach((input) => {
        if (input.value !== null) {
            return true;
        }
    });
    return false;
}

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});

// Override function from topjava.commons.js
function updateTable() {
    if (hasFilters) {
        filter();
    } else {
        update();
    }
}

function filter() {
    $.ajax({
        type: "GET",
        url: filterAjaxUrl,
        data: filterForm.serialize()
    }).done(function (data) {
        drawTable(data);
    });
}

function clearFilter() {
    filterInputs.forEach((input) => input.value = null);
    update();
}
