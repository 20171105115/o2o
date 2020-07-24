$(function () {
    //获取商品类别列表
    var categoryListUrl = '/o2o/shopadmin/getproductcategorylist';
    //添加商品类别
    var addCategoryUrl = '/o2o/shopadmin/addproductcategorys';
    //删除商品类别
    var deleteCategoryUrl = '/o2o/shopadmin/removeproductcategory';
    getList();
    function getList(){
        $.getJSON(categoryListUrl,function (data) {
            if (data.success){
                var category = data.productCategoryList;

                var categoryHtml = '';
                category.map(function (item,index) {
                    categoryHtml += '<div class="row row-product-category">'
                        + '<div class="col-33">' + item.productCategoryName + '</div>'
                        + '<div class="col-33">' + item.priority + '</div>'
                        + '<div class="col-33"><a href="#" class="button delete" data-id="'
                        + item.productCategoryId
                        + '">删除</a></div>'
                        + '</div>'
                });
                $('.product-category-wrap').html(categoryHtml);
            }
        })
    }


})