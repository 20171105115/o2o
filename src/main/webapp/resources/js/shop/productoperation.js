$(function () {
    var productId = getQueryString('productId');
    //获取商品列表
    var getCategoryUrl = '/o2o/shopadmin/getproductcategorylist';
    //获取商品原来信息
    var getProductUrl = '/o2o/shopadmin/getproductbyid?productId='+productId;
    //是否为修改
    var Flag = false;
    //新增时候的Url
    var addUrl = '/o2o/shopadmin/addproduct';
    //修改时候的Url
    var editUrl = '/o2o/shopadmin/modifyproduct';

    if (productId) {
        Flag = true;
    }



    if (Flag) {//修改
        getInfo(productId);
    } else {//添加
        getCategory();
    }

    function getInfo(productId) {
        $.getJSON(getProductUrl,function (data) {
            if (data.success){
                var product = data.product;
                $('#product-name').val(product.productName);
                $('#product-desc').val(product.productDesc);
                $('#priority').val(product.priority);
                $('#normal-price').val(product.normalPrice);
                $('#promotion-price').val(product.promotionPrice);

                var categoryHtml = '';
                var categoryList = data.productCategoryList;
                var optionSelected = product.productCategory.productCategoryId;
                categoryList
                    .map(function(item, index) {
                        var isSelect = optionSelected === item.productCategoryId ? 'selected'
                            : '';
                        categoryHtml += '<option data-value="'
                            + item.productCategoryId
                            + '"'
                            + isSelect
                            + '>'
                            + item.productCategoryName
                            + '</option>';
                    });
                $('#category').html(categoryHtml);
            }
        })
    }

    //获取分类
    function getCategory() {
        $.getJSON(getCategoryUrl, function (data) {
            if (data.success) {
                var category = data.productCategoryList;
                var categoryHtml = '';
                category.map(function (value) {
                    categoryHtml += '<option data-value="' + value.productCategoryId
                        + '">' + value.productCategoryName + '</option>'
                });
                $('#category').html(categoryHtml);
            }
        })
    };

    $('#submit').click(function () {
        var url = Flag ? editUrl : addUrl;
        var product = {};
        if (Flag) {
            product.productId = productId;
        }
        product.productName = $('#product-name').val();
        product.productDesc = $('#product-desc').val();
        product.priority = $('#priority').val();
        product.normalPrice = $('#normal-price').val();
        product.promotionPrice = $('#promotion-price').val();
        product.productCategory = {
            productCategoryId : $('#category').find('option').not(
                function() {
                    return !this.selected;
                }).data('value')
        };

        //生成表单对象
        var formData = new FormData();
        var productStr = JSON.stringify(product);
        formData.append("productStr", productStr);
        var thumbnail = $('#small-img')[0].files[0];//因为是单个文件  所以获取[0]下标的元素
        formData.append("thumbnail", thumbnail);
        //遍历商品详情图控件，获取其中的文件流,存入formData对象中
        $('.detail-img').map(function (index, item) {
            if ($('.detail-img')[index].files.length > 0) {//判断该控件是否有文件
                formData.append("productImg" + index,
                    $('.detail-img')[index].files[0]);
            }
        })

        //判断验证码
        var verifyCodeActual = $('#j_captcha').val();
        if (verifyCodeActual) {//如果已经输入，则提交
            formData.append('verifyCodeActual', verifyCodeActual);
            $.ajax({
                url: url,
                type: 'POST',
                data: formData,
                processData: false,// 告诉jQuery不要去处理发送的数据(必须设置)
                contentType: false, // 告诉jQuery不要去设置Content-Type请求头（必须设置）
                success: function (data) {
                    if (data.success) {
                        $.toast('提交成功');
                    } else {
                        $.toast('提交失败' + data.errMsg)
                    }
                    $('#captcha_img').click();
                }
            })
        } else {
            $.toast('请输入验证码');
            return;
        }
    });

    //判断详情图控件中的input是否是最后一个，不是的话则➕1
    $('.detail-img-div').on('click', '.detail-img:last-child', function () {
        if ($('.detail-img').length < 6) {
            $('#detail-img').append('<input type="file" class="detail-img">');//追加到最后一个的后面
        }
    });
})