$(function () {
    var initUrl = '/o2o/shop/getshopinitinfo';
    var registerShopUrl = '/o2o/shopadmin/registershop';
    getShopInitInfo();

    function getShopInitInfo() {
        $.getJSON(initUrl, function (data) {
            if (data.success){
                var tempHtml = '';
                var tempAreaHtml = '';
                data.shopCategoryList.map(function (item,index) {
                    //数组中的每个元素都会执行这个函数,item当前值,index索引值
                    tempHtml += '<option data-id="' + item.shopCategoryId + '">'
                    + '</option>';
                });
                data.areaList.map(function (item,index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">'
                    + '</option>';
                });
                $('#shop-category').html(tempHtml);
                $('#area').html(tempAreaHtml);
            }
        });

        $('#submit').click(function () {
            var shop = {};
            shop.shopName = $('#shop-name').val();
            shop.shopDesc = $('#shop-desc').val();
            shop.shopAddr = $('#shop-addr').val();
            shop.phone = $('#shop-phone').val();
            shop.shopCategory = {
                shopCategoryId : $('#shop-category').find('option').not(function () {
                    return !this.selected;

                }).data('id')
            };
            shop.area={
                areaId : $('#area').find('option').not(function () {
                    //find 搜索所有与指定表达式匹配的元素。这个函数是找出正在处理的元素的后代元素的好方法。
                    //not 从匹配元素的集合中删除与指定表达式匹配的元素
                    return !this.selected
                }).data('id')//data-xx在一个div上存取数据
            };
            var shopImg = $('#shop-img')[0].files[0];//获得一张图片的方法就是：$('xx')[0].files[0]
            var formData = new formData();//用一些键值对来模拟一系列表单控件
            formData.append('shopImg',shopImg);
            formData.append('shopStr',JSON.stringify(shop));
            $.ajax({
                url:registerShopUrl,
                type:'POST',
                data : formData,
                contentType : false,
                processData : false,
                cache : false,
                success : function (data) {
                    if (data.success){
                        $.toast('提交成功');
                    } else {
                        $.toast('提交失败' + data.errMsg);
                    }
                }
            });

        })

    }
})