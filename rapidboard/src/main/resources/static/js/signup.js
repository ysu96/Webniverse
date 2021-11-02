var signupF = {
    init: function () {
        var _this = this;
        $('#usernameCheckBtn').on('click', function (event) {
            _this.usernameCheck(event);
        });
    },

    usernameCheck: function (event) {
        const username = $("input[name=username]").val();

        $.ajax({
            type: "post",
            url: `/duplicate/${username}`,
            dataType: "json"

        }).done(res => {
            console.log("username check success", res);

            $("#checkUsernameInfo").text(res.message);
            $("#checkUsernameInfoForm").css("display", "block");
            if (res.code == 1) {
                $("#checkUsernameInfo").css("color", "blue");
                $("#isValidateChecked").val(1);
            } else {
                $("#checkUsernameInfo").css("color", "red");
                $("#isValidateChecked").val(0);
            }
        }).fail(error => {
            console.log("check fail", error);
        });
    }
}

signupF.init();