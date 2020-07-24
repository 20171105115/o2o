$(function () {
    //获取商品类别列表
    var categoryListUrl = '/o2o/shopadmin/getproductcategorylist';
    //添加商品类别
    var addCategoryUrl = '/o2o/shopadmin/addproductcategorys';
    //删除商品类别
    var deleteCategoryUrl = '/o2o/shopadmin/removeproductcategory';
    getList();

    $('.product-category-wrap').on('click','.row-product-category.temp .delete',
        function (e) {
            console.log($(this).parent().parent());
            $(this).parent().parent().remove();//父级的父级正好是最外边那个div
        });

    $('.product-category-wrap').on('click','.row-product-category.now .delete',
        function (e) {
            var target = e.currentTarget;//currentTarget 事件属性返回其监听器触发事件的节点，即当前处理该事件的元素、文档或窗口。
            $.confirm('确定吗',function () {//确认提示框
                $.ajax({
                    url : deleteCategoryUrl,
                    type : 'POST',
                    data : {
                        productCategoryId : target.dataset.id//这里选择的是delete也就是a标签的属性，其中有个Id
                    },
                    dataType : 'json',
                    success : function (data) {
                        if (data.success){
                            $.toast('删除成功');
                            getList();
                        } else {
                            $.toast('删除失败');
                        }
                    }
                })
            })
        })

    //获取分类信息
    function getList() {
        $.getJSON(categoryListUrl, function (data) {
            if (data.success) {
                var category = data.productCategoryList;

                var categoryHtml = '';
                category.map(function (item, index) {
                    categoryHtml += '<div class="row row-product-category now">'
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
    };

    //点击按钮 新增一条列表
    $('#new').click(function () {
        var tempCategoryHtml = '';
        tempCategoryHtml += '<div class="row row-product-category temp">'
            + '<div class="col-33">' + '<input type="text" class="category-input category" placeholder="类别名称">' + '</div>'
            + '<div class="col-33">' + '<input type="number" class="category-input priority" placeholder="优先级">' + '</div>'
            + '<div class="col-33">' + '<a href="#" class="button delete"'
            + '">删除</a>' + '</div></div>';
        $('.product-category-wrap').append(tempCategoryHtml);
    });

    //点击提交 提交分类列表
    $('#submit').click(function () {
        var tempArr = $('.temp');
        var productCategoryList = [];
        tempArr.map(function(index, item) {//这里注意  遍历元素时，需要用后面这个item
            var tempObj = {};
            tempObj.productCategoryName = $(item).find('.category').val();
            tempObj.priority = $(item).find('.priority').val();
            if (tempObj.productCategoryName && tempObj.priority) {
                productCategoryList.push(tempObj);
            }
        });

        $.ajax({
            url: addCategoryUrl,
            type: 'POST',
            data: JSON.stringify(productCategoryList),//转化成JSON字符串，传递到后台
            contentType: 'application/json',
            success: function (data) {
                if (data.success) {
                    $.toast("提交成功");
                    getList();
                }else {
                    $.toast("提交失败");
                }
            }
        });

    });


})