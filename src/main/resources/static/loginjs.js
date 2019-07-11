(function() {
    if (!String.prototype.trim) {
        (function() {
            // Make sure we trim BOM and NBSP
            var rtrim = /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g;
            String.prototype.trim = function() {
                return this.replace(rtrim, '');
            };
        })();
    }

    [].slice.call( document.querySelectorAll( 'input.input__field' ) ).forEach( function( inputEl ) {
        // in case the input is already filled..
        if( inputEl.value.trim() !== '' ) {
            classie.add( inputEl.parentNode, 'input--filled' );
        }

        // events:
        inputEl.addEventListener( 'focus', onInputFocus );
        inputEl.addEventListener( 'blur', onInputBlur );
    } );

    function onInputFocus( ev ) {
        classie.add( ev.target.parentNode, 'input--filled' );
    }

    function onInputBlur( ev ) {
        if( ev.target.value.trim() === '' ) {
            classie.remove( ev.target.parentNode, 'input--filled' );
        }
    }
})();

$(function() {
    $('#login #login-password').focus(function() {
        $('.login-owl').addClass('password');
    }).blur(function() {
        $('.login-owl').removeClass('password');
    });
    $('#login #register-password').focus(function() {
        $('.register-owl').addClass('password');
    }).blur(function() {
        $('.register-owl').removeClass('password');
    });
    $('#login #register-repassword').focus(function() {
        $('.register-owl').addClass('password');
    }).blur(function() {
        $('.register-owl').removeClass('password');
    });
    $('#login #forget-password').focus(function() {
        $('.forget-owl').addClass('password');
    }).blur(function() {
        $('.forget-owl').removeClass('password');
    });
});

function goto_register(){
    $("#register-username").val("");
    $("#register-password").val("");
    $("#register-repassword").val("");
    $("#register-code").val("");
    $("#tab-2").prop("checked",true);
}

function goto_login(){
    $("#login-username").val("");
    $("#login-password").val("");
    $("#tab-1").prop("checked",true);
}

function goto_forget(){
    $("#forget-username").val("");
    $("#forget-password").val("");
    $("#forget-code").val("");
    $("#tab-3").prop("checked",true);
}

//邮箱验证
function checkEmail(str){
    var re = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/; 
    if (re.test(str)) {
      alert("ok");
    } else {
      alert("nope");
    }
  }

function login(){//登录
    var username = $("#login-username").val(),
        password = $("#login-password").val(),
        flag = false;
    //判断用户名密码是否为空
    if(username == ""){
        $.pt({
            target: $("#login-username"),
            position: 'r',
            align: 't',
            width: 'auto',
            height: 'auto',
            content:"用户名不能为空"
        });
        flag = true;
    }
    if(password == ""){
        $.pt({
            target: $("#login-password"),
            position: 'r',
            align: 't',
            width: 'auto',
            height: 'auto',
            content:"密码不能为空"
        });
        flag = true;
    }

    if(flag){
        return false;
    }else{//登录
        //调用后台登录验证的方法
            $.ajax({
                //几个参数需要注意一下
                type: "post",//方法类型
                dataType: "json",//预期服务器返回的数据类型
                url: "/user/login" ,//url
                data: $('#form1').serialize(),
                success: function (data) {
                console.log(data);//打印服务端返回的数据(调试用)
                    if (data.status==="success") {
                        alert("SUCCESS");
                        window.location.href = "/index";      
                    }
                    else{
                    	alert(data.message);
                    }
                    ;
                },
                error : function() {
                    alert("异常！");
                }
            });
        }

}

//注册
function register(){
    var username = $("#register-username").val(),
    	email=$("#register-email").val(),
        password = $("#register-password").val(),
        repassword = $("#register-repassword").val(),
        code = $("#register-code").val(),
        flag = false,
        validatecode = null;
    //判断用户名密码是否为空
    if(username == ""){
        $.pt({
            target: $("#register-username"),
            position: 'r',
            align: 't',
            width: 'auto',
            height: 'auto',
            content:"用户名不能为空"
        });
        flag = true;
    }
    if(password == ""){
        $.pt({
            target: $("#register-password"),
            position: 'r',
            align: 't',
            width: 'auto',
            height: 'auto',
            content:"密码不能为空"
        });
        flag = true;
    }else{
        if(password != repassword){
            $.pt({
                target: $("#register-repassword"),
                position: 'r',
                align: 't',
                width: 'auto',
                height: 'auto',
                content:"两次输入的密码不一致"
            });
            flag = true;
        }
    }
    
    //用户名只能是15位以下的字母或数字
    var regExp = new RegExp("^[a-zA-Z0-9_]{6,15}$");
    if(!regExp.test(username)){
        $.pt({
            target: $("#register-username"),
            position: 'r',
            align: 't',
            width: 'auto',
            height: 'auto',
            content:"用户名必须为6到15位的字母或数字"
        });
        flag = true;
    }
    
    //邮箱格式检查
    var re = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/; 
    if (!re.test(email)) {
    	$.pt({
            target: $("#register-email"),
            position: 'r',
            align: 't',
            width: 'auto',
            height: 'auto',
            content:"邮箱格式不对"
        });
        flag = true;
    } 
    
    if(flag){
    	return false;
    }
    //检查用户名是否已经存在
    //调后台代码检查用户名是否已经被注册
        $.ajax({
            //几个参数需要注意一下
            type: "POST",//方法类型
            dataType: "json",//预期服务器返回的数据类型
            url: "/user/register" ,//url
            data: $('#form3').serialize(),
            success: function (data) {
                console.log(data);//打印服务端返回的数据(调试用)
                if (data.status==="success") {
                    alert("SUCCESS");
                }
                else{
                	alert(data.message);
                }
                ;
            },
            error : function() {
                alert("该号已被注册！");
            }
        });

    //检查注册码是否正确
    //调后台方法检查注册码
    // if(code != '11111111'){
    //     $.pt({
    //         target: $("#register-code"),
    //         position: 'r',
    //         align: 't',
    //         width: 'auto',
    //         height: 'auto',
    //         content:"注册码不正确"
    //     });
    //     flag = true;
    // }


    if(flag){
        return false;
    }else{//注册
        spop({
            template: '<h4 class="spop-title">注册成功</h4>即将于3秒后返回登录',
            position: 'top-center',
            style: 'success',
            autoclose: 3000,
            onOpen : function(){
                var second = 2;
                var showPop = setInterval(function(){
                    if(second == 0){
                        clearInterval(showPop);
                    }
                    $('.spop-body').html('<h4 class="spop-title">注册成功</h4>即将于'+second+'秒后返回登录');
                    second--;
                },1000);
            },
            onClose : function(){
                goto_login();
            }
        });
        return false;
    }
}

//重置密码
function forget(){
    var username = $("#forget-username").val(),
        password = $("#forget-password").val(),
        code = $("#forget-code").val(),
        flag = false,
        validatecode = null;
    //判断用户名密码是否为空
    if(username == ""){
        $.pt({
            target: $("#forget-username"),
            position: 'r',
            align: 't',
            width: 'auto',
            height: 'auto',
            content:"用户名不能为空"
        });
        flag = true;
    }
    if(password == ""){
        $.pt({
            target: $("#forget-password"),
            position: 'r',
            align: 't',
            width: 'auto',
            height: 'auto',
            content:"密码不能为空"
        });
        flag = true;
    }
    //用户名只能是15位以下的字母或数字
    var regExp = new RegExp("^[a-zA-Z0-9_]{6,15}$");
    if(!regExp.test(username)){
        $.pt({
            target: $("#forget-username"),
            position: 'r',
            align: 't',
            width: 'auto',
            height: 'auto',
            content:"用户名必须为6到15位以下的字母或数字"
        });
        flag = true;
    }
    //检查用户名是否存在
    //调后台方法

        $.ajax({
            //几个参数需要注意一下
            type: "POST",//方法类型
            dataType: "json",//预期服务器返回的数据类型
            url: "/users/login" ,//url
            data: $('#form2').serialize(),
            success: function (result) {
                console.log(result);//打印服务端返回的数据(调试用)
                if (result.resultCode == 200) {
                    alert("SUCCESS");
                }
                ;
            },
            error : function() {
                alert("异常！");
            }
        });
    //检查注册码是否正确
    //这个暂时不用考虑
    // if(code != '11111111'){
    //     $.pt({
    //         target: $("#forget-code"),
    //         position: 'r',
    //         align: 't',
    //         width: 'auto',
    //         height: 'auto',
    //         content:"注册码不正确"
    //     });
    //     flag = true;
    // }



    if(flag){
        return false;
    }else{//重置密码
        spop({
            template: '<h4 class="spop-title">重置密码成功</h4>即将于3秒后返回登录',
            position: 'top-center',
            style: 'success',
            autoclose: 3000,
            onOpen : function(){
                var second = 2;
                var showPop = setInterval(function(){
                    if(second == 0){
                        clearInterval(showPop);
                    }
                    $('.spop-body').html('<h4 class="spop-title">重置密码成功</h4>即将于'+second+'秒后返回登录');
                    second--;
                },1000);
            },
            onClose : function(){
                goto_login();
            }
        });
        return false;
    }
}


function refreshCaptcha(){
    $('.captcha').attr("src", '/user/verify?rnd=' + (new Date()).getTime());
}





