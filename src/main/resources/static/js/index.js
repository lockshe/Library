var app = new Vue({
    el: "#index",
    data: {
        username: "",
        realname:"",
        permission: 1,
        loginTime: "",
        passwordVerify: "",
        passwordConfirm: "",
        emailErrorTip: "",
        emailVerifyStatus: ""
    }
});

var search = "";

Vue.component('paging-more', {
    template: '<button class="btn btn-link btn-block btn-lg" onclick="offset += 1;getPage();"><b>获取更多</b></button><br/><br/>'
});

var userInfo = {};

function getPage() {
    if (currentTab === "#downloaded-content") {
        getUserDownloaded();
    } else if (currentTab === "#uploaded-content") {
        getUserUploaded();
    } else {
        getResource(getOrderBy());
    }
}



/**
 * 更新密码
 */
function updatePassword() {
    var oldPassword = $("#old-password").val();
    var newPassword = $("#new-password").val();
    var confirmNewPassword = $("#confirm-new-password").val();
       $.ajax({
            url: "/user/password",
            type: "PUT",
            data: {oldPassword: oldPassword, newPassword: newPassword},
            success: function (data) {
            	var json = JSON.parse(data);
                if (json.status==="success") {
                    alert(json.message);
                    window.location.href = "/index";
                } else {
                    alert(json.message);
                }
            }
        });
}

function saveInfo() {
    var email = $("#email").val();
    var realname=$("#realname").val();
    
    $.ajax({
        url: '/user/info',
        type: 'PUT',
        data: {
            realname: realname,
            email: email
        },
        success: function (data) {
            layer.closeAll();
            var json = JSON.parse(data);
            if(json.status==="success"){
                $("#email").val(email);
                $("#realname").val(realname);
            	alert(json.message);
            	window.location.href = "/index";
            }
            else{
            	alert(json.message);
            }
            
        }
    	});
}

function getUserInfo() {
	$.ajax({
        url: "/user/info",
        type: "GET",
        success: function (data) {
            var json = JSON.parse(data);
            app.permission = json.permission;
            app.username = json.username;
            app.loginTime = timestampToTime(json.loginTime-28439000);
            $("#email").val(json.email);
            $("#realname").val(json.realname);
        }
    });

}

function timestampToTime(timestamp) {
    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    var D = date.getDate() + ' ';
    var h = date.getHours() + ':';
    var m = date.getMinutes() + ':';
    var s = date.getSeconds();
    return Y+M+D+h+m+s;
}

function showAvatarModal() {
    layer.open({
        type: 1,
        title: false,
        closeBtn: 0,
        offset: 'ct',
        shadeClose: true,
        content: "<input id='file-input' class='form-control file' multiple data-max-file-count='100' data-preview-file-type='img' name='file' type='file'/>"
    });
    $("#file-input").fileinput({
        uploadUrl: "/user/uploadavatar",
        uploadAsync: true,
        maxFileCount: 1,
        maxFilePreviewSize: 10485760
    }).on('fileuploaded', function (event, data) {
    	 var json = JSON.parse(data);
    	    console.log(json);
    	    if (json.status === "success") {
    	    	window.location.href = "/index";
    	    } else {
    	        alert("上传失败，文件不合法");
    	    }
    });
}



function getOrderBy() {
    return $("#order-by").val() + " " + $("#order-way").val();
}

$(document).ready(function () {
    $("#search").keyup(function () {
        /** @namespace window.event.keyCode */
        if (window.event.keyCode === 13) {
            search = $('#search').val();
            getPage();
        }
    });
})

// $(document).ready(function () {
//     $("#search").keyup(function () {
//         /** @namespace window.event.keyCode */
//         if (window.event.keyCode === 13) {
//             search = $('#search').val();
//             offset = 0;
//             getPage();
//         }
//     });
//     $(".content-filter").change(function () {
//         offset = 0;
//         getResource(getOrderBy());
//     });
//     $(".email-verify-code").keyup(function () {
//         var code = event.srcElement.value;
//         if (code.length === 6) {
//             $.ajax({
//                 url: "/common/" + code + "/verification", type: "PUT", success: function (data) {
//                     var json = JSON.parse(data);
//                     app.emailVerifyStatus = json.status === "success" ? "" : "验证码错误";
//                 }
//             });
//         } else {
//             app.emailVerifyStatus = "";
//         }
//     });
//     $(".email").keyup(function () {
//         checkEmailChange(event.srcElement.value);
//     });
//     $(".password").keyup(function () {
//         var len = event.srcElement.value.length;
//         if (len >= userConfig.password.minLength && len <= userConfig.password.maxLength) {
//             app.passwordVerify = "";
//         } else {
//             app.passwordVerify = "密码长度限定为" + userConfig.password.minLength + "至" + userConfig.password.maxLength + "位";
//         }
//     });
//     $(".confirm-password").keyup(function () {
//         app.passwordConfirm = (event.srcElement.value === $("#new-password").val()) ? "" : "两次输入的密码不一样";
//     });
//     $(".sendVerifyCode").click(function () {
//         sendVerifyCode($("#email").val(), event.srcElement);
//     });
//
//     $("a.nav-link[href='" + location.hash + "-tab']").click();
//     getTabContent(location.hash);
//     $(".nav-link").click(function () {
//         getTabContent($(event.srcElement).attr("href"));
//     });
// });

function getTabContent(href) {
    if (href.startsWith("uploaded", 1)) {
        offset = 0;
        window.location.hash = "uploaded";
        getUserUploaded();
    } else if (href.startsWith("downloaded", 1)) {
        offset = 0;
        window.location.hash = "downloaded";
        getUserDownloaded();
    } else if (href.startsWith("bio", 1)) {
        window.location.hash = "bio";
        getUserInfo();
    } else {
        offset = 0;
        window.location.hash = "resource";
        getResource("");
    }
}

var offset = 0;

// function checkEmailChange(email) {
//     var isChange = email !== userInfo.email;
//     if (isEmail(email)) {
//         if (isChange) {
//             $.get("/user/email/exists", {email: email}, function (data) {
//                 var json = JSON.parse(data);
//                 app.emailErrorTip = json.exists ? "该邮箱已经被注册啦" : "";
//             });
//         }
//         app.emailErrorTip = "";
//     } else {
//         app.emailErrorTip = "邮箱格式不正确";
//     }
//     $(".verify-code-div").css("display", isChange && userConfig.emailVerify ? "block" : "none");
// }


var currentTab = "#resources-content";

function getUserDownloaded() {
    currentTab = "#downloaded-content";
    layer.load(1);
    $.get("/file/user/downloaded", {offset: offset, search: search}, function (data) {
        layer.closeAll();
        try {
            setResources(JSON.parse(data), currentTab);
        } catch (e) {
            window.location.href = "/signin";
        }
    });
}

function getUserUploaded() {
    currentTab = "#uploaded-content";
    layer.load(1);
    $.get("/file/user/uploaded", {search: search}, function (data) {
        layer.closeAll();
        try {
            setResources(JSON.parse(data), currentTab);
        } catch (e) {
            window.location.href = "/signin";
        }
    });
}
function preview(obj){
    var id=obj.id;
        $.ajax({
            type: "GET",
            url: "/file/preview",
            data: {id:id},
            dataType: "json",
            success: function(data){
                if(data.status==="success"){
                	window.open("/preview/"+data.filename);
                }
                else{
                    alert(data.message);
                }
            },
            error:function () {
            	alert("该文件不支持预览");
            }
        });
    
}



function down(obj){
    var id=obj.id;
    var name=obj.name;
        $.ajax({
            type: "GET",
            url: "/file/down",
            data: {	id:id,
            		filename:name,
            		},
            dataType: "json",
            success: function(data){
                if(data.status==="success"){
                	window.open("/file/download?id="+id+"&filename="+name);
                }
                else{
                   alert(data.message);
                }
            },
            error:function () {
                layer.message('请求失败')
            }
        });
}

function getmyPage() {
	currentTab = "#myresources-content";
	$.get("/file/my", {
		search:search
	}, function(data) {
		json=JSON.parse(data);
		if(json.status==="error"){
			alert(json.message);
			window.location.href = "/index";
			return;
		}
			
		layer.closeAll();
		setmyResources(JSON.parse(data), currentTab);
	});
}

/*function setmyResources(resources, tabId) {
   var contentHtml = "";
   search = "";
   if (resources.length < 1) {
       offset -= 1;
       alerts("糟糕，没有数据了");
   } else {
       $.each(resources, function (i, resource) { 	
           var isDownloaded = "#downloaded-content" === tabId;
           var date = isDownloaded ? resource.downloadTime : resource.createTime;
           contentHtml += "<div class='row content-box rounded' data-id='" + resource.id + "'><div class='col-12 col-sm-12'><br/><div class='row'>" +
           (isMobile() ? "" : "<div class='col-sm-1 col-0'><img src=user/getavatar/" + resource.username + " class='rounded avatar'/></div>") +
           "<div class='col-sm-11 col-12'><h4><a data-toggle='tooltip' class='visit-url' href='" + resource.visitUrl + "' target='_blank' data-description='" + resource.description + "' title='" + resource.description + "'>" + resource.fileName + "</a>" +
           ("#uploaded-content" === tabId ? "&emsp;<a href='javascript:;' class='font-1' onclick='editFile();'>编辑</a>&emsp;<a href='javascript:;' class='font-1' onclick='removeFile();'>删除</a>" : "") +
           "</h4><p>上传者：<b>" + resource.username + "</b>&emsp;" + (isDownloaded ? "下载" : "上传") + "时间：<b>" + timestampToTime(resource.createTime-28439000)+ "</b>&emsp;文件大小：<b>" + formatSize(resource.size) + "</b>&emsp;分类：<b class='file-category'>" + resource.categoryName + "</b>" +
           "&emsp;预览次数：<b>" + resource.checkTimes + "</b>&emsp;下载次数：<b>" + resource.downloadTimes + "</b>" +"&emsp;删除：<b><a href='javascript:void(0);' id="+resource.id+" onclick='shanchu(this);'>删除</a> "+"</b>"+"&emsp;修改：<b><a href='javascript:void(0);' id="+resource.id+" onclick='update(this);'>修改</a> "+"</b>"+
           "</p></div></div><br/></div></div><br/>";
       });
       if (offset > 0) {
           $(tabId).append(contentHtml);
       } else {
           $(tabId).html(contentHtml);
       }
       $('[data-toggle="tooltip"]').tooltip();
       setCSS();
   }
}*/
function setmyResources(resources, tabId) {
	   var contentHtml = "";
	   search = "";
	   if (resources.length < 1) {
	       offset -= 1;
	       alerts("糟糕，没有数据了");
	   } else {
	       $.each(resources, function (i, resource) { 	
	           var isDownloaded = "#downloaded-content" === tabId;
	           var date = isDownloaded ? resource.downloadTime : resource.createTime;
	           contentHtml += "<div class='row content-box rounded' data-id='" + resource.id + "'><div class='col-12 col-sm-12'><br/><div class='row'>" +
	               (isMobile() ? "" : "<div class='col-sm-1 col-0'><img src=user/getavatar/" + resource.username + " class='rounded avatar'/></div>") +
	               "<div class='col-sm-11 col-12'><h4><a data-toggle='tooltip' class='visit-url' href='" + resource.visitUrl + "' target='_blank' data-description='" + resource.description + "' title='" + resource.description + "'>" + resource.fileName + "</a>" +
	               "&emsp;<a href='javascript:void(0);' class='font-1' onclick='editFile();'>编辑</a>&emsp;<a href='javascript:void(0);' class='font-1' onclick='removeFile();'>删除</a>" +
	               "</h4><p>上传者：<b>" + resource.username + "</b>&emsp;" + (isDownloaded ? "下载" : "上传") + "时间：<b>" + new Date(date).format("yyyy-MM-dd hh:mm:ss") + "</b>&emsp;文件大小：<b>" + formatSize(resource.size) + "</b>&emsp;分类：<b class='file-category'>" + resource.categoryName + "</b>" +
	               "&emsp;详细信息：<b class='file-tag'>" + resource.tag + "</b>&emsp;下载次数：<b>" + resource.downloadTimes + 
	               "</p></div></div><br/></div></div><br/>";
	       });
	       if (offset > 0) {
	           $(tabId).append(contentHtml);
	       } else {
	           $(tabId).html(contentHtml);
	       }
	       $('[data-toggle="tooltip"]').tooltip();
	       setCSS();
	   }
	}

function getResource(orderBy) {
    currentTab = "#resources-content";
    layer.load(1);
    $.get("/file/view", {
        offset: offset,
        categoryId: $("#category").val(),
        orderBy: orderBy,
        search: search
    }, function (data) {
        layer.closeAll();
        setResources(JSON.parse(data), currentTab);
    });
}


function setResources(resources, tabId) {
    var contentHtml = "";
    search = "";
    if (resources.length < 1) {
        alerts("糟糕，没有数据了");
    } else {
        $.each(resources, function (i, resource) {
            /** @namespace resource.fileName */
            /** @namespace resource.createTime */
            /** @namespace resource.categoryName */
            /** @namespace resource.checkTimes */
            /** @namespace resource.downloadTimes */
            /** @namespace resource.visitUrl */
            /** @namespace resource.downloadTime */
            /**@namespace resource.address*/
            /**@namespace resource.sureaddress*/
            /**
             * 暂时不考虑查看次数
             * @code &emsp;查看次数：<b>" + resource.checkTimes + "</b>
             */
            var isDownloaded = "#downloaded-content" === tabId;
            var date = isDownloaded ? resource.downloadTime : resource.createTime;
            contentHtml += "<div class='row content-box rounded' data-id='" + resource.id + "'><div class='col-12 col-sm-12'><br/><div class='row'>" +
                (isMobile() ? "" : "<div class='col-sm-1 col-0'><img src=user/getavatar/" + resource.username + " class='rounded avatar'/></div>") +
                "<div class='col-sm-11 col-12'><h4><a data-toggle='tooltip' class='visit-url' href='" + resource.visitUrl + "' target='_blank' data-description='" + resource.description + "' title='" + resource.description + "'>" + resource.fileName + "</a>" +
                ("#uploaded-content" === tabId ? "&emsp;<a href='javascript:;' class='font-1' onclick='editFile();'>编辑</a>&emsp;<a href='javascript:;' class='font-1' onclick='removeFile();'>删除</a>" : "") +
                "</h4><p>上传者：<b>" + resource.username + "</b>&emsp;" + (isDownloaded ? "下载" : "上传") + "时间：<b>" + timestampToTime(resource.createTime-28439000)+ "</b>&emsp;文件大小：<b>" + formatSize(resource.size) + "</b>&emsp;分类：<b class='file-category'>" + resource.categoryName + "</b>" +
                "&emsp;预览次数：<b>" + resource.checkTimes + "</b>&emsp;下载次数：<b>" + resource.downloadTimes + "</b>" +"&emsp;预览：<b><a href='javascript:void(0);' id="+resource.id+" onclick='preview(this);'>预览</a> "+"</b>"+"&emsp;下载：<b><a href='javascript:void(0);' id="+resource.id+" name="+resource.fileName+" onclick='down(this);'>下载</a> "+"</b>"+
                "</p></div></div><br/></div></div><br/>";
        });
        
        $(tabId).html(contentHtml);
        $('[data-toggle="tooltip"]').tooltip();
    }
}


var srcContentBox;

function editFile() {
    var contentBox = $(event.srcElement).parents(".content-box");
    srcContentBox = contentBox;
    $("#edit-file-id").val($(contentBox).attr("data-id"));
    $("#edit-file-name").val($(contentBox).find("data-fileName").text());
    $("#edit-file-category").val($(contentBox).find("data.file-category").text());
    $("#edit-file-description").val($(contentBox).find("a.visit-url").attr("data-description"));
    $("#edit-file-modal").modal("show");
}

function saveFileInfo() {
    var name = $("#edit-file-name").val();
    var category = $("#edit-file-category").val();
    var tag = $("#edit-file-tag").val();
    var description = $("#edit-file-description").val();
    if (isEmpty(name)) {
        alerts("文件名不能为空");
    } else {
        layer.load(1);
        $.ajax({
            url: "/file/" + $("#edit-file-id").val(),
            type: "PUT",
            data: {
                name: name,
                category: category,
                tag: tag,
                description: description
            },
            success: function (data) {
                layer.closeAll();
                var json = JSON.parse(data);
                if (json.status === "success") {
                    $(srcContentBox).find("a.visit-url").text(name);
                    $(srcContentBox).find("b.file-category").text(category);
                    $(srcContentBox).find("b.file-tag").text(tag);
                    $(srcContentBox).find("a.visit-url").attr("data-description", description);
                    var href = $(srcContentBox).find("a.visit-url").attr("href");
                    $(srcContentBox).find("a.visit-url").attr("href", href.substr(0, href.lastIndexOf("/") + 1) + name);
                    $("#edit-file-modal").modal("hide");
                    alerts("保存成功");
                } else {
                    alerts(json.message);
                }
            }
        })
        ;
    }
}

function removeFile() {
    var contentBox = $(event.srcElement).parents(".content-box");
    layer.confirm('是否确定删除文件【' + $(contentBox).find("a.visit-url").text() + '】', {
        btn: ['确定', '删除'],
        skin: 'layui-layer-molv'
    }, function () {
        var id = $(contentBox).attr("data-id");
        layer.load(1);
        $.ajax({
            url: "/file/" + id, type: "DELETE", success: function (data) {
                layer.closeAll();
                var json = JSON.parse(data);
                if (json.status === "success") {
                    $(contentBox).remove();
                    layer.msg("删除成功");
                } else {
                    alerts(json.message);
                }
            }, error: function () {
                layer.closeAll();
            }
        });
    });
}



function shanchu(obj){
	 var id=obj.id;
     $.ajax({
         type: "DELETE",
         url: "/file/"+id,        
         dataType: "json",
         success: function(data){
             if(data.status==="success"){
            	alert(data.message);
             	window.open("/index");
             }
             else{
                 alert(data.message);
             }
         },
         error:function () {
         	alert("删除异常");
         }
     });
}

function update(obj){
	
}


function uploadfile(){
	 $.ajax({
         type: "GET",
         url: "/file/up",       
         dataType: "json",
         success: function(data){
             if(data.status==="success"){
             	window.open("/upload.html");
             }
             else{
                 alert(data.message);
                 window.location.href = "/index";
             }
         },
         error:function () {
         	alert("异常");
         }
     });
}