//权限管理
var aeditIndex = undefined;
function aendEditing(){
    if (aeditIndex == undefined){return true}
    if ($('#adg').datagrid('validateRow', aeditIndex)){
        $('#adg').datagrid('endEdit', aeditIndex);
        aeditIndex = undefined;
        return true;
    } else {
        return false;
    }
}
function onClickRow(index){
    if (aeditIndex != index){
        if (aendEditing()){
            $('#adg').datagrid('selectRow', index)
                .datagrid('beginEdit', index);
            aeditIndex = index;
        } else {
            $('#adg').datagrid('selectRow', aeditIndex);
        }
    }
}
function aappend(){
    if (aendEditing()){
        $('#adg').datagrid('appendRow');
        aeditIndex = $('#adg').datagrid('getRows').length-1;
        $('#adg').datagrid('selectRow', aeditIndex)
            .datagrid('beginEdit', aeditIndex);
    }
}
function aremoveit(){
    if (aeditIndex == undefined){return}
    $('#adg').datagrid('cancelEdit', aeditIndex)
        .datagrid('deleteRow', aeditIndex);
    aeditIndex = undefined;
}

function aaccept() {
	if (aendEditing()) {
		$('#adg').datagrid('acceptChanges');
		var info = $("#adg").datagrid("getSelected");
		var id = info.id;
		var permission = info.permission;
		var auth = info.isDownloadable + info.isUploadable + info.isVisible
				+ info.isDeletable + info.isUpdatable;
		$.ajax({
			url : 'admin/' + id + '/' + permission,
			type : 'put',
			async : true,
			dataType : 'json',
			data : {
				'auth' : auth
			},
			success : function(data) {
				if (data.message == 'success') {
					alert(data.message);
				}
				$('#udg').datagrid('reload');
			}
		});
	}
}