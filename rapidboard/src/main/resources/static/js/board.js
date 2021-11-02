var boardF={
    init : function () {
        var _this = this;
        $('#createBoardBtn').on('click', function (event){
           _this.createBoard(event);
        });

        $('#deleteBoardBtn').on('click', function (event){
            _this.deleteBoard(event);
        });
    },

    createBoard : function (event){
        console.log("create board");
        const boardname = $(`#create_board_name`).val();
        if (boardname === "") {
            alert("게시판이름을 작성해주세요!");
            return;
        }
        $.ajax({
            type:"post",
            url:`/api/board/create/${boardname}`,
            dataType: "json"
        }).done(res=>{
            location.replace("/");
        }).fail(error=>{
            alert(error.responseText);
        });
    },

    deleteBoard : function (event){
        console.log("delete board");
        const boardId = $('#delete_board_id').val();
        $.ajax({
            type:"delete",
            url:`/api/board/delete/${boardId}`,
            dataType: "json"

        }).done(res=>{
            location.replace("/");
        }).fail(error=>{

        });
    }

}

boardF.init();