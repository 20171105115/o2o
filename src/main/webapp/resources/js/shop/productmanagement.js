$(function () {


    var getProductListUrl = '/o2o/shopadmin/getproductlist';

    getProductList();

    function getProductList() {
        $.getJSON(getProductListUrl,function (data) {
            if (data.success){
                var productList = data.productList;
                var productHtml = '';
                productList.map(function (item,index) {
                    productHtml += '<div class="row row-product">'
                    + '<div class="col-33">' + item.productName + '</div>'
                    + '<div class="col-20">' + item.priority + '</div>'
                    + '<div class="col-40">'
                        + '<a href="/o2o/shopadmin/productoperation?productId=' + item.productId +'">编辑</a>'
                        + '<a href="/o2o/shopadmin/removeproduct?productId= ' +item.productId + '">上架</a>'
                        + '<a href="#">预览</a>'
                    + '</div>'
                    + '</div>'
                });
                $('.product-wrap').html(productHtml);
            }
        })
    }

})