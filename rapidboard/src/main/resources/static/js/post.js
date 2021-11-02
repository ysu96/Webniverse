var postF={
    init: function () {
        var _this = this;
        $('#createPostBtn').on('click', function (event){
            _this.createPost(event);
        });

        $('#deletePostBtn').on('click', function (event){
            _this.deletePost(event);
        });

        $('#updatePostBtn').on('click', function (event){
            _this.updatePost(event);
        });

        $("a[name=addViewCountBtn]").on('click', function (event){
            _this.addViewCount(event);
        });

        $("button[name=uploadfileDeleteBtn]").on('click', function (event){
            _this.deleteFile(event);
        });
    },

    createPost : function (event){
        console.log("create post");
        event.preventDefault();
        let formData = new FormData();
        let inputFile = $('input[name=inputFiles]');
        let files = inputFile[0].files;

        const memberId = $("form[name=postInputForm]").attr('id').replace("postInputForm-", "");
        let text = $("#post_content").val();
        text = text
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/(\n|\r\n|\r)/g, '<br>');
        $("#post_content").val(text);

        let title = $('#post_title').val();
        title = title
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/(\n|\r\n|\r)/g, '<br>');
        $("#post_title").val(title);

        formData.append('title', $('#post_title').val());
        formData.append('content', $("#post_content").val());
        formData.append('board_id', $('#post_board_id').val());
        for(var i=0; i<files.length; i++){
            formData.append('uploadFiles', files[i]);
        }

        $.ajax({
            type: "post",
            url: `/api/post/${memberId}`,
            data: formData,
            contentType: false,
            processData: false,
            dataType: "json"
        }).done(res=>{
            alert("글 쓰기 성공")
            location.replace("/"); //성공하면 이 페이지로 돌아감

        }).fail(error=>{
            console.log("post fail", error);
            alert(error.responseText);
            // history.back();
        });
    },

    deletePost : function (event){
        console.log("delete post");
        const postId = $("form[name=commentForm]").attr('id').replace("commentForm-", "");
        $.ajax({
            type: "delete",
            url: `/api/post/${postId}`,
            dataType: "json"
        }).done(res=>{
            // console.log("delete success", res);
            alert("글 삭제 성공")
            location.replace("/");

        }).fail(error=>{
            console.log("delete fail", error);
        });
    },

    updatePost : function (event){
        console.log("update post");
        event.preventDefault();
        let formData = new FormData();
        let inputFile = $('input[name=inputFiles]');
        let files = inputFile[0].files;

        const postId = $("form[name=updatePostForm]").attr('id').replace("updatePostForm-", "");
        let text = $("#content").val();
        text = text
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/(\n|\r\n|\r)/g, '<br>');
        $("#content").val(text);

        let title = $('#title').val();
        title = title
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/(\n|\r\n|\r)/g, '<br>');
        $("#title").val(title);

        formData.append('title', $('#title').val());
        formData.append('content', $("#content").val());
        formData.append('board_id', $('#board_id').val());
        for(var i=0; i<files.length; i++){
            formData.append('uploadFiles', files[i]);
        }


        $.ajax({
            type: "put",
            url: `/api/post/${postId}`,
            data: formData,
            contentType: false,
            processData: false,
            dataType: "json"
        }).done(res=>{
            // console.log("update success", res);
            alert("글 수정 성공")
            location.replace(`/post/list/${postId}`);
        }).fail(error=>{
            console.log("update fail", error);
            alert(error.responseText);
        });
    },

    addViewCount : function (event){
        console.log("add view count");
        const postId = $(event.target).attr('id').replace("addViewCountBtn-","");

        $.ajax({
            type: "get",
            url:`/api/post/${postId}`,
            dataType:"json"
        }).done(res=>{
        }).fail(error=>{
        })
    },

    deleteFile : function (event){
        const uploadfileId = $(event.target).attr('id').replace("uploadfileDeleteBtn-","");
        $.ajax({
            type:"delete",
            url:`/api/file/${uploadfileId}`,
            dataType: "json"
        }).done(res=>{
            location.reload();
        }).fail(error=>{
            console.log("delete fail", error);
        });

    }
}

postF.init();

