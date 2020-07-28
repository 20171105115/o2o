$(function () {


    var getProductListUrl = '/o2o/shopadmin/getproductlist?pageIndex=1&pageSize=100';

    var statusUrl = '/o2o/shopadmin/modifyproduct';

    getProductList();

    function getProductList() {
        $.getJSON(getProductListUrl, function (data) {
            if (data.success) {
                var productList = data.productList;
                var productHtml = '';
                productList.map(function (item, index) {
                    var textPo = "下架";
                    var contraryStatus = 0;
                    if (item.enableStatus == 0) {
                        //若状态值wei0，则是下架的商品 ，操作变为上架
                        textPo = "上架";
                        contraryStatus = 1;
                    } else {
                        contraryStatus = 0;
                    }
                    productHtml += '' + '<div class="row row-product">'
                        + '<div class="col-33">' + item.productName + '</div>'
                        + '<div class="col-20">' + item.priority + '</div>'
                        + '<div class="col-40">'
                        + '<a href="#" class="edit" data-id="' + item.productId
                        + '" data-status="' + item.enableStatus
                        + '">编辑</a>'
                        + '<a href="#" class="status" data-id="' + item.productId + '" data-status="'
                        + contraryStatus + '">' + textPo + '</a>'
                        + '<a href="#" class="preview" data-id="'
                        + item.productId + '" data-status="' + item.enableStatus
                        + '">预览</a>'
                        + '</div>'
                        + '</div>';
                });
                $('.product-wrap').html(productHtml);
            }
        })
    };

    // 将class为product-wrap里面的a标签绑定上点击的事件
    $('.product-wrap').on('click', 'a', function (e) {
        var target = $(e.currentTarget);//currentTarget 事件属性返回其监听器触发事件的节点，即当前处理该事件的元素、文档或窗口。
        if (target.hasClass('edit')) {
            // 如果有class edit则点击就进入店铺信息编辑页面，并带有productId参数
            window.location.href = '/o2o/shopadmin/productoperation?productId='
                + e.currentTarget.dataset.id;
        } else if (target.hasClass('status')) {
            // 如果有class status则调用后台功能上/下架相关商品，并带有productId参数
            changeItemStatus(e.currentTarget.dataset.id,
                e.currentTarget.dataset.status);
        } else if (target.hasClass('preview')) {
            // 如果有class preview则去前台展示系统该商品详情页预览商品情况
            window.location.href = '/o2o/frontend/productdetail?productId='
                + e.currentTarget.dataset.id;
        }
    });

    function changeItemStatus(id, enableStatus) {
        // 定义product json对象并添加productId以及状态(上架/下架)
        var product = {};
        product.productId = id;
        product.enableStatus = enableStatus;
        $.confirm('确定么?', function () {//跳出一个弹窗，是或者否
            // 上下架相关商品
            $.ajax({
                url: statusUrl,
                type: 'POST',
                data: {
                    productStr: JSON.stringify(product),
                    statusChange: true
                },
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        $.toast('操作成功！');
                        getProductList();
                    } else {
                        $.toast('操作失败！');
                    }
                }
            });
        });
    }

})