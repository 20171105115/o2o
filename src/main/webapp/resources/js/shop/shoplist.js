$(function () {
    var getShopListUrl = '/o2o/shopadmin/getshoplist';

    getShopList();

    //访问后台，获取店铺列表的值，以及用户信息的值
    function getShopList(){
        $.getJSON(getShopListUrl, function (data) {
            if (data.success) {
                $('#user-name').text(data.user.name);

                var shopListHtml = '';
                data.shopList.map(function (item, index) {
                    shopListHtml += '<div class="row row-shop">' + '<div class="col-40">' + item.shopName
                        + '</div>' + '<div class="col-40">' + shopStatus(item.enableStatus)
                        + '</div>' + '<div class="col-20">' + goShop(item.enableStatus, item.shopId) + '</div>'
                        + '</div>'
                });
                $('.shop-wrap').html(shopListHtml);
            }
        });
    }

    //根据状态返回对应的值
    function shopStatus(enableStatus) {
        if (enableStatus == 0){
            return '审核中';
        }
        if (enableStatus == 1){
            return '审核通过';
        }
        if (enableStatus == -1){
            return '店铺非法';
        }
    };

    //根据状态返回url，如果是审核通过，就能操作，审核不通过只能看
    var goShop = function (enableStatus, shopId) {
        if (enableStatus == 1){
            return '<a href="/o2o/shopadmin/shopmanagement?shopId=' + shopId + '">进入</a>';
        } else {
            return '';
        }
    };
})