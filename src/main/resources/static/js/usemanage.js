var ueditIndex = undefined;
function uendEditing(){
    if (ueditIndex == undefined){return true}
    if ($('#udg').datagrid('validateRow', ueditIndex)){
        $('#udg').datagrid('endEdit', ueditIndex);
        ueditIndex = undefined;
        return true;
    } else {
        return false;
    }
}
function onClickRow(index){
    if (ueditIndex != index){
        if (uendEditing()){
            $('#udg').datagrid('selectRow', index)
                .datagrid('beginEdit', index);
            ueditIndex = index;
        } else {
            $('#udg').datagrid('selectRow', ueditIndex);
        }
    }
}
function uappend(){
    if (uendEditing()){
        $('#udg').datagrid('appendRow');
        ueditIndex = $('#udg').datagrid('getRows').length-1;
        $('#udg').datagrid('selectRow', ueditIndex)
            .datagrid('beginEdit', ueditIndex);
    }
}
function uremoveit(){
    if (ueditIndex == undefined){return}
    $('#udg').datagrid('cancelEdit', ueditIndex)
        .datagrid('deleteRow', ueditIndex);
    ueditIndex = undefined;
}
function uaccept(){
    if (uendEditing()) {
        $('#udg').datagrid('acceptChanges');
        var info=$("#dg").datagrid("getSelected");
        var id=info.id;
        var permission=info.permission;
        var auth=info.isDownloadable+info.isUploadable+info.isVisible+info.isDeletable+info.isUpdatable;

        $.ajax({
            url: 'admin/'+id+'/'+permission,
            type: 'put',
            async: true,
            dataType: 'json',
            data: {'auth': auth},
            success: function (data) {
                if (data.message == 'success') {
                    alert(data.message);
                }
                $('#udg').datagrid('reload');
            }
        });
    }
}

function add(){
	 $("#standardWindow").window('open');
}

function submit(){
	 $.ajax({
         type: "post",
         dataType: "json",
         url: "" ,
         data: $('#standardForm').serialize(),
         success: function (data) {
             if (data.status==="success") {
                 alert("添加成功");    
             }
             else{
             	alert(data.message);
             }
             ;
         },
         error : function() {
             alert("添加失败！");
         }
     });
}