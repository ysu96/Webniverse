var updateF = {
    init: function () {
        var _this = this;
        $('#profileUpdateBtn').on('click', function (event){
            _this.updateProfile(event);
        });

        $('#deleteMemberBtn').on('click', function (event){
            _this.deleteMember(event);
        });
    },

    updateProfile : function (event){
        event.preventDefault();
        const memberId = $("form[name=profileUpdateForm]").attr('id').replace("profileUpdateForm-", "");
        const data = $("form[name=profileUpdateForm]").serialize();
        console.log(data);
        $.ajax({
            type:"put",
            url:`/api/member/${memberId}`,
            data: data,
            contentType: "application/x-www-form-urlencoded; charset=utf-8", //데이터의 타입,  &으로 분리되고, "=" 기호로 값과 키를 연결하는 key-value tuple로 인코딩되는 값
            dataType: "json" //반환받을 타입

        }).done(res=>{
            console.log("update success", res);
            alert("정보 수정 완료!")
            location.replace(`/member/${memberId}`);
        }).fail(error=>{
            console.log("update fail", error);
            alert(error.responseText);
        });
    },

    deleteMember : function (event){
        const memberId = $("form[name=profileUpdateForm]").attr('id').replace("profileUpdateForm-", "");
        $.ajax({
            type:"delete",
            url:`/api/member/${memberId}`,
            dataType: "json"
        }).done(res=>{
            console.log("탈퇴 성공", res);
            alert("탈퇴 완료!")
            location.href=`/logout`; //성공하면 이 페이지로 돌아감

        }).fail(error=>{
            console.log("탈퇴 실패", error.responseJSON.message);
            alert(error.responseJSON.message);
            location.href=`/`;
        });
    }

}

updateF.init();
