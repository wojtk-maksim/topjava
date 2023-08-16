const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    },
    formatDataForUI(data) {
        data.dateTime = formatDateTime(data.dateTime);
    },
    serialize() {
        return "id=" + $('#id').val() + "&" +
            "dateTime=" + $('#dateTime').val().replace(" ", "T") + "&" +
            "description=" + $('#description').val() + "&" +
            "calories=" + $('#calories').val();
    }
}

function formatDateTime(dateTime) {
    return dateTime.substring(0, 16).replace("T", " ");
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (dateTime, type, row) {
                        if (type === "display") {
                            return formatDateTime(dateTime);
                        }
                        return dateTime;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-meal-excess", data.excess);
            }
        })
    );
});

// DateTimePicker
$('#dateTime').datetimepicker({
    format: 'Y-m-d H:i',
});

$('#startDate').datetimepicker({
    timepicker: false,
    format: 'Y-m-d',
});

$('#endDate').datetimepicker({
    timepicker: false,
    format: 'Y-m-d',
});

$('#startTime').datetimepicker({
    datepicker: false,
    format: 'H:i',
});

$('#endTime').datetimepicker({
    datepicker: false,
    format: 'H:i',
});