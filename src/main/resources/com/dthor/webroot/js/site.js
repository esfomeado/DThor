jQuery(document).ready(function($) {
    $('#nav-main, .brand').localScroll();
    $('#news, .container').localScroll();

});

$(document).on('change', '.btn-file :file', function() {
    var input = $(this),
            numFiles = input.get(0).files ? input.get(0).files.length : 1,
            label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
    input.trigger('fileselect', [numFiles, label]);
});

$(document).ready(function() {
    $('.btn-file :file').on('fileselect', function(event, numFiles, label) {

        var input = $(this).parents('.input-group').find(':text'),
                log = numFiles > 1 ? numFiles + ' files selected' : label;

        if (input.length) {
            input.val(log);
        } else {
            if (log)
                alert(log);
        }

    });
});

$(document).ready(function() {
    if (document.getElementById("result") !== null) {
        location.replace("#uploadbox");
    }
});

$(document).on("click", ".torrent_link", function() {
    var myFileId = $(this).data('id');
    myFileId = myFileId.substring(1, myFileId.length);
    $(".modal-header span").html(myFileId);
});

$('button#submit').click(function(e) {
    $.ajax({
        url: '/delete',
        type: 'POST',
        data: {deleteKey: $('.modal-body #deleteKey').val(),
            key: $('.modal-header #fileId').html()
        },
        success: function(response) {
            alert(response);
            if (response === "Sucess!") {
                $('#deleteTorrent').modal('hide');
                location.reload();
            }
        },
        error: function(jqXHR, e) {
            alert('error' + e);
        }
    });
    e.preventDefault();
});