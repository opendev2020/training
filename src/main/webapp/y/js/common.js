function loading(btn){
    if($(btn).attr("class") == "btn btn-warning"){
        alert("请勿频繁点击！");
        return {isloading : true};
    }
    var h = $(btn).html();
    $(btn).attr("class", "btn btn-warning").html('<span class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span> 请稍后...');
    return {isloading : false, btn : btn, html : h};
}

function unloading(obj){
    $(obj.btn).attr("class", "btn btn-primary").html(obj.html);
}

function search(page, url, formid, vum) {
	var columnInputs = $("#" + formid + " input[name='columns']");
	var columnNames = [];
	columnInputs.each(function() {
		columnNames.push($(this).val());
	});
	console.log(columnNames);
	$("#" + formid + " input:text").each(function(index) {
		var value = $(this).val();
		if (value == '') {
			columnInputs.eq(index).val('-');
		}
	});
	//return false;
	$("#" + formid).ajaxSubmit({
		url : url,
		type : 'post',
		data : {
			rows : 20,
			page : page
		},
		success : function(data) {
			vum.datas = data.result;
			vum.pages = data.pages;
			columnInputs.each(function(index) {
				$(this).val(columnNames[index]);
			});
		},
		error : function(data) {
			alert('error');
		}
	});
}