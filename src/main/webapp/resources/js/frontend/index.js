$(function () {


    var getMainPageUrl = '/o2o/frontend/listmainpageinfo';

    getInfo();
    function getInfo() {
        $.getJSON(getMainPageUrl, function (data) {
            if (data.success) {
                var categoryHtml = '';
                var categoryList = data.shopCategoryList;
                categoryList.map(function (item, index) {
                    categoryHtml += '<div class="col-50 shop-classify" data-category="' + item.shopCategoryId + '">'
                        + '<div class="word">'
                        + '<p class="shop-title">' + item.shopCategoryName + '</p>'
                        + '<p class="shop-desc">' + item.shopCategoryDesc + '</p>'
                        + '</div>'
                        + '<div class="shop-classify-img-warp">'
                        + '<img class="shop-img" src="' + item.shopCategoryImg + '">'
                        + '</div>'
                        + '</div>'
                });
                $('.row').html(categoryHtml);

                var headLindHtml = '';
                var headLineList = data.headLineList;
                headLineList.map(function (item, index) {
                    headLindHtml += '<div class="swiper-slide img-wrap">'
                        + '<a href="' + item.lineLink + '" externail>'
                        + '<img class="banner-img" src="' + item.lineImg + '" alt="' + item.lineName + '">'
                        + '</a></div>';
                });
                $('.swiper-wrapper').html(headLindHtml);
                //设置轮播图的轮换时间
                $('.swiper-container').swiper({
                    autoplay: 3000,
                    //对轮播图进行操作时，轮播图不停止
                    autoplayDisableOnInteraction: false
                });
            }
        })
    };
    $('#me').click(function() {
        $.openPanel('#panel-right-demo');
    });

    $('.row').on('click','.shop-classify',function (e) {
        var shopCategoryId = e.currentTarget.dataset.category;
        var newUrl = '/o2o/frontend/shoplist?parentId='+shopCategoryId;
        window.location.href = newUrl;
    })



})