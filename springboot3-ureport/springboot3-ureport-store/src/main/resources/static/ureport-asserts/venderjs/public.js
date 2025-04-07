$.ajaxSetup({
    contentType:"application/x-www-form-urlencoded;charset=utf-8",
    complete:function(XMLHttpRequest,textStatus){
        //通过XMLHttpRequest取得响应结果
        var res = XMLHttpRequest.responseText;
        try{
            var jsonData = JSON.parse(res);
            if(jsonData.code == -1){
                //如果超时就处理 ，指定要跳转的页面(比如登陆页)
                window.location.href=ctxPath+"login"
            }else{
                //正常情况就不统一处理了
            }
        }catch(e){
        }
    }
});