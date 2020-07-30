$(function () {
    var productId = getQueryString("productId");

    var getProductDetailUrl = '/o2o/frontend/listproductdetailpageinfo?productId=' + productId;

    getInfo();


    function getInfo() {
        $.getJSON(getProductDetailUrl,function (data) {
            if (data.success){
                var html = '';
                var product = data.product;
                $('#product-img').attr('src',product.imgAddr);
                $('#product-time').text(
                    new Date(product.lastEditTime).format('Y-m-d'));
                $('#product-name').text(product.productName);
                $('#product-desc').text(product.productDesc);

                //商品价格显示逻辑，如果原价现价都为空，则不显示价格
                if (product.normalPrice != undefined
                    && product.promotionPrice != undefined) {
                    // 如果现价和原价都不为空则都展示，并且给原价加个删除符号
                    $('#price').show();
                    $('#normalPrice').html(
                        '<del>' + '￥' + product.normalPrice + '</del>');
                    $('#promotionPrice').text('￥' + product.promotionPrice);
                } else if (product.normalPrice != undefined
                    && product.promotionPrice == undefined) {
                    // 如果原价不为空而现价为空则只展示原价
                    $('#price').show();
                    $('#promotionPrice').text('￥' + product.normalPrice);
                } else if (product.normalPrice == undefined
                    && product.promotionPrice != undefined) {
                    // 如果现价不为空而原价为空则只展示现价
                    $('#promotionPrice').text('￥' + product.promotionPrice);
                }

                var imgListHtml = '';
                // 遍历商品详情图列表，并生成批量img标签
                product.productImgList.map(function(item, index) {
                    imgListHtml += '<div> <img src="' + item.imgAddr
                        + '" width="100%" /></div>';
                });
                $('#imgList').html(imgListHtml);



            }
        })
    };

    // 点击后打开右侧栏
    $('#me').click(function() {
        $.openPanel('#panel-right-demo');
    });
    $.init();

})