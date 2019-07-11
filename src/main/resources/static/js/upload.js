$("#file-input").fileinput({
    uploadUrl: "/file/upload",
    uploadAsync: true,
    maxFileCount: 1,
    previewFileType: ['image', 'html', 'text', 'video', 'audio', 'flash'],
    uploadExtraData: function () {
        return {
        	categoryId:$("#category-id").val()
            //description: $("#description").val(),
        };
       
    },
    maxFilePreviewSize: 512000000000
}).on('fileuploaded', function (event, data, previewId, index) {
    var json = JSON.parse(data);
    console.log(json);
    if (json.status === "success") {
    	window.location.href = "/index";
    } else {
        alert("上传失败，文件不合法");
    }
});

$(document).on('ready', function () {
    $("#file-input").fileinput({
        maxFilePreviewSize: 10240
    });
});
