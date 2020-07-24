$(function () {
    //先去访问后台，把shopId封装成currentShop存在session中
    //并且从后台再把shopId拿出来，把店铺信息的URL改成加shopId的（也就是修改店铺信息的)
    var shopId = getQueryString('shopId');
    var shopManagementUrl = '/o2o/shopadmin/getshopmanagementinfo?shopId='+shopId;
    $.getJSON(shopManagementUrl,function (data) {
        if (data.redirect){
            window.location.href = data.url;
        } else{
            if (data.shopId != undefined && data.shopId != null){
                shopId = data.shopId;
            }
            $('#shopInfo').attr('href','/o2o/shopadmin/shopoperation?shopId='+shopId);
        }
    });
})