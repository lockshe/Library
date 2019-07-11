//文件管理
var editIndex = undefined;
function endEditing(){
    if (editIndex == undefined){return true}
    if ($('#dg').datagrid('validateRow', editIndex)){
        $('#dg').datagrid('endEdit', editIndex);
        editIndex = undefined;
        return true;
    } else {
        return false;
    }
}
function onClickRow(index){
    if (editIndex != index){
        if (endEditing()){
            $('#dg').datagrid('selectRow', index)
                .datagrid('beginEdit', index);
            editIndex = index;
        } else {
            $('#dg').datagrid('selectRow', editIndex);
        }
    }
}
function append(){
    if (endEditing()){
        $('#dg').datagrid('appendRow');
        editIndex = $('#dg').datagrid('getRows').length-1;
        $('#dg').datagrid('selectRow', editIndex)
            .datagrid('beginEdit', editIndex);
    }
}
function removeit(){
    if (editIndex == undefined){return}
    $('#dg').datagrid('cancelEdit', editIndex)
        .datagrid('deleteRow', editIndex);
    editIndex = undefined;
}

function accept(){
    if (endEditing()) {
        $('#dg').datagrid('acceptChanges');
        var info=$("#dg").datagrid("getSelected");
        var id=info.id;
        var auth=info.isDownloadable+info.isUploadable+info.isVisible+info.isDeletable+info.isUpdatable;
        $.ajax({
            url: '/file/'+id+'/auth',
            type: 'put',
            async: true,
            dataType: 'json',
            data: {'auth':auth},
            success: function (data) {
                if (data.message === 'success') {
                    alert(data.message);
                }
                $('#dg').datagrid('reload');
            }
        });
    }
}
function reject(){
    $('#dg').datagrid('rejectChanges');
    editIndex = undefined;
}
function getChanges(){
    var rows = $('#dg').datagrid('getChanges');
    alert(rows.length+'行没有被保存');
    }
function fileup(){
    window.location.href="upload.html";
}

