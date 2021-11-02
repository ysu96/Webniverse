var commentF={
    init : function () {
        var _this = this;
        $('#commentBtn').on('click', function (event){
           _this.postComment(event);
        });

        $('#commentInput').on("keydown", function (event) {
            if (event.keyCode == 13) {
                _this.postComment(event);
            }
        });

        $('button[name=commentUpdateBtn]').on('click', function (event){
            _this.updateComment(event);
        });

        $('input[name=commentUpdateInput]').on("keydown", function (event) {
            if (event.keyCode == 13) {
                _this.updateComment(event);
            }
        });

        $('button[name=recommentBtn]').on('click', function (event){
            _this.postRecomment(event);
        });

        $('input[name=recommentInput]').on("keydown", function (event) {
            if (event.keyCode == 13) {
                _this.postRecomment(event);
            }
        });

        $('button[name=deleteCommentBtn]').on('click', function (event){
            _this.deleteComment(event);
        });

        $('button[name=recommentBoxBtn]').on('click', function (event){
            _this.openRecommentInputBox(event);
        });

        $('button[name=updateBoxBtn]').on('click', function (event){
            _this.openUpdateInputBox(event);
        });

        $('#commentShowBtn').on('click', function (event){
            _this.getCommentsList(event);
        })
    },

    postComment : (function (event){
        return function (event){
            console.log('post comment');
            const postId = $("form[name=commentForm]").attr('id').replace("commentForm-", "");
            let commentInput = $(`#commentInput`).val();
            commentInput = commentInput
                .replace(/&/g, "&amp;")
                .replace(/</g, "&lt;")
                .replace(/>/g, "&gt;")
                .replace(/(\n|\r\n|\r)/g, '<br>');

            const data = {
                content: commentInput,
                postId: postId,
                depth:0
            }

            if (data.content === "") {
                alert("댓글을 작성해주세요!");
                return;
            }

            $.ajax({
                type:"post",
                url:`/api/comment`,
                data:	JSON.stringify(data),
                contentType:"application/json; charset=utf-8",
                dataType:"json"
            }).done(res=>{
                location.reload();
            }).fail(error=>{
            });
        };
    }()),

    updateComment : (function (event){
        return function (event){
            console.log("update comment");
            const commentId = $(event.target).parent().attr('id').replace("commentUpdateForm-", "");
            let commentInput = $(`#commentUpdateInput-${commentId}`).val();
            commentInput = commentInput
                .replace(/&/g, "&amp;")
                .replace(/</g, "&lt;")
                .replace(/>/g, "&gt;")
                .replace(/(\n|\r\n|\r)/g, '<br>');

            const data = {
                content: commentInput,
                commentId: commentId
            }
            if (data.content === "") {
                alert("댓글을 작성해주세요!");
                return;
            }

            $.ajax({
                type:"put",
                url:`/api/comment`,
                data:	JSON.stringify(data),
                contentType:"application/json; charset=utf-8",
                dataType:"json"

            }).done(res=>{
                location.reload();
            }).fail(error=>{
            });
        }
    }()),

    postRecomment : (function (event){
        return function (event){
            console.log("post recomment");
            const commentId = $(event.target).parent().attr('id').replace("recommentInputForm-","");
            let recommentInput = $(`#recommentInput-${commentId}`).val();

            recommentInput = recommentInput
                .replace(/&/g, "&amp;")
                .replace(/</g, "&lt;")
                .replace(/>/g, "&gt;")
                .replace(/(\n|\r\n|\r)/g, '<br>');

            const data = {
                content: recommentInput,
                commentId: commentId //parentId
            }

            if (data.content === "") {
                alert("댓글을 작성해주세요!");
                return;
            }

            $.ajax({
                type:"post",
                url:`/api/comment`,
                data:	JSON.stringify(data),
                contentType:"application/json; charset=utf-8",
                dataType:"json"
            }).done(res=>{
                location.reload();
            }).fail(error=>{
            });
        }
    }()),

    deleteComment : function (event){
        console.log("delete comment");
        const commentId = $(event.target).attr('id').replace("deleteCommentBtn-","");

        $.ajax({
            type:"delete",
            url:`/api/comment/${commentId}`,
            dataType:"json"

        }).done(res=>{
            location.reload();
        }).fail(error=>{
        });
    },

    openRecommentInputBox : function (event){
        const commentId = $(event.target).attr('id').replace("recommentBoxBtn-","");
        let state = $(`#recommentInputForm-${commentId}`).css("display");

        if(state == 'none') {
            var grpl = $("form[name=recommentInputForm]").length;
            for(var i=0; i<grpl; i++) {
                $("form[name=recommentInputForm]").eq(i).css("display", "none");
                $("form[name=commentUpdateForm]").eq(i).css("display", "none");
            }

            $(`#recommentInputForm-${commentId}`).css("display","block");
        }
        else{
            $(`#recommentInputForm-${commentId}`).css("display","none");
            $(`#recommentInput-${commentId}`).val(null);
        }
    },

    openUpdateInputBox : function (event){
        const commentId = $(event.target).attr('id').replace("updateBoxBtn-","");
        let state = $(`#commentUpdateForm-${commentId}`).css("display");

        if(state == 'none'){
            var grpl = $("form[name=commentUpdateForm]").length;
            for(var i=0; i<grpl; i++) {
                $("form[name=recommentInputForm]").eq(i).css("display", "none");
                $("form[name=commentUpdateForm]").eq(i).css("display", "none");
            }

            $(`#commentUpdateForm-${commentId}`).css("display","block");

        }
        else{
            $(`#commentUpdateForm-${commentId}`).css("display","none");
            $(`#commentUpdateInput-${commentId}`).val(null);
        }
    },

    // commentShow : function (commentListDto){
    //     console.log(commentListDto.comments);
    //     const data = { pageDto : commentListDto.pageDto, comments : commentListDto.comments};
    //     var request = $.ajax({
    //         url: "/comment/list",
    //         method: "POST",
    //         data:	JSON.stringify(data),
    //         contentType:"application/json; charset=utf-8",
    //         dataType: "html"
    //         //DATA TYPE
    //         //html(text/html), json(application/json), xml(text/xml), text(text/plain)
    //     });
    //     request.done(function( data ) {
    //         console.log(data);
    //         // $('#commentShow').html(data);
    //     });
    //     request.fail(function( jqXHR, textStatus ) {
    //         alert( "Request failed: " + textStatus );
    //     });


        // $('div[name=commentShow]').html('/post/commentList.jsp');
        //
        // const state = $('#commentShow').css("display");
        // if(state == 'none'){
        //     $('#commentShow').css("display", "block");
        // }
        // else{
        //     $('#commentShow').css("display", "none");
        // }
    // },

    getCommentsList : function (event){
        const state = $('div[name=commentShow]').css("display");
        if(state == 'none'){
            $('div[name=commentShow]').css("display", "block");
            $('#commentShowBtn').text("댓글 접기");
        }
        else{
            $('div[name=commentShow]').css("display", "none");
            $('#commentShowBtn').text("댓글 보기");

        }

        // const postId = $("div[name=commentShow]").attr('id').replace("commentShow-", "");
        // $.ajax({
        //     type:"get",
        //     url:`/api/comment/${postId}`,
        //     dataType:"json"
        // }).done(res=>{
        //     console.log(res);
        //     this.commentShow(res);
        // }).fail(error=>{
        //     console.log("fail")
        // });
    }

}
commentF.init();


