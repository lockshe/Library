//分类管理
var seditIndex = undefined;
function sendEditing(){
    if (seditIndex == undefined){return true}
    if ($('#sdg').datagrid('validateRow', seditIndex)){
        $('#sdg').datagrid('endEdit', seditIndex);
        seditIndex = undefined;
        return true;
    } else {
        return false;
    }
}
function onClickRow(index){
    if (seditIndex != index){
        if (sendEditing()){
            $('#sdg').datagrid('selectRow', index)
                .datagrid('beginEdit', index);
            seditIndex = index;
        } else {
            $('#sdg').datagrid('selectRow', seditIndex);
        }
    }
}
function sappend(){
    if (sendEditing()){
        $('#sdg').datagrid('appendRow');
        seditIndex = $('#sdg').datagrid('getRows').length-1;
        $('#sdg').datagrid('selectRow', seditIndex)
            .datagrid('beginEdit', seditIndex);
    }
}
function sremoveit(){
    if (seditIndex == undefined){return}
    $('#sdg').datagrid('cancelEdit', seditIndex)
        .datagrid('deleteRow', seditIndex);
    seditIndex = undefined;
}
function saccept(){
    if (sendEditing()) {
        $('#sdg').datagrid('acceptChanges');
        var info=$("#sdg").datagrid("getSelected");
        var id=info.id;
        var name=info.name;
        $.ajax({
            url: '/category/'+id,
            type: 'put',
            async: true,
            dataType: 'json',
            data: {'name': name},
            success: function (data) {
                if (data.message === 'success') {
                    alert(data.message);
                }
                $('#sdg').datagrid('reload');
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
        url: "/category/add",
        data: $('#standardForm').serialize(),
        success: function (data) {
            if (data.status==="success") {
                alert("添加成功"); 
           	 	$("#standardWindow").window('close');
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