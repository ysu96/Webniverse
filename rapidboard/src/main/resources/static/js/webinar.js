var webinarF={
    init: function () {
        var _this = this;
        $('#roomCreateBtn').on('click', function (event){
            _this.createRoom(event);
        });

        $('button[name=registerBtn]').on('click', function (event){
            _this.registerRoom(event);
        });

        $('button[name=deleteBtn]').on('click', function (event){
            _this.deleteRoom(event);
        });

        $('button[name=modalUpdateBtn]').on('click', function (event){
            _this.modalUpdateRoom(event);
        });

        $('#updateBtn').on('click', function (event){
           _this.updateRoom(event);
        });

        $('#addParticipantBtn').on('click', function (event){
            _this.addParticipant(event);
        });


        $('#deleteParticipantBtn').on('click', function (event){
            _this.deleteParticipant(event);
        });
    },

    createRoom : function (event){
        console.log("create room");
        event.preventDefault();
        let formData = new FormData();

        console.log($("#startDate").val());

        let text = $("#roomTitle").val();
        text = text
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/(\n|\r\n|\r)/g, '<br>');
        $("#roomTitle").val(text);

        let title = $('#roomOwner').val();
        title = title
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/(\n|\r\n|\r)/g, '<br>');
        $("#roomOwner").val(title);

        formData.append('roomTitle', $('#roomTitle').val());
        formData.append('roomUrlId', $('#roomUrlId').val());
        formData.append('roomOwner', $("#roomOwner").val());
        formData.append('passwd', $("#passwd").val());
        formData.append('startDate', $("#startDate").val());
        formData.append('endDate', $("#endDate").val());

        let inputFile = $('input[name=roomImage]')[0];
        if (inputFile.files[0] != null) {
            formData.append('roomImage', inputFile.files[0]);
        }


        $.ajax({
            type: "post",
            url: `/api/webinar/create`,
            data: formData,
            contentType: false,
            processData: false,
            dataType: "json"
        }).done(res=>{
            alert("방 생성 성공")
            location.replace("/admin/webinar"); //성공하면 이 페이지로 돌아감

        }).fail(error=>{
            console.log("방 생성 실패", error);
            alert("방 생성 실패 : \n"+ error.responseText);
        });
    },

    registerRoom : function (event){
        console.log("register room");
        const webinarId = $(event.target).attr('id').replace("registerBtn-", "");

        $.ajax({
            type:"PUT",
            url:`/api/webinar/main/${webinarId}`,
            dataType: "json"

        }).done(res=>{
            location.reload();
        }).fail(error=>{

        });
    },

    deleteRoom : function (event){
        console.log("delete room");
        const webinarId = $(event.target).attr('id').replace("deleteBtn-","");

        $.ajax({
            type:"DELETE",
            url:`/api/webinar/delete/${webinarId}`,
            dataType: "json"

        }).done(res=>{
            alert("삭제 성공")
            location.reload();
        }).fail(error=>{
            alert("delete fail! \n"+error.responseText);
        });
    },

    modalUpdateRoom : function (event){
        console.log("modal update room");
        const webinarId = $(event.target).attr('id').replace("modalUpdateBtn-","");

        $.ajax({
            type:"GET",
            url:`/api/webinar/get/${webinarId}`,
            dataType: "json"

        }).done(res=>{
            console.log(res.webinarId);
            $('#updateRoomTitle').val(res.roomTitle);
            $('#updateRoomOwner').val(res.roomOwner);
            $('#updateRoomPassword').val(res.password);
            $('#updateStartDate').val(res.startDate.slice(0,10)+'T'+res.startDate.slice(11));
            $('#updateEndDate').val(res.endDate.slice(0,10)+'T'+res.endDate.slice(11));
            $('#webinarId').val(res.webinarId);

            if(res.isStreaming == 0) $('#wait').prop('checked', true);
            else if(res.isStreaming == 1) $('#start').prop('checked', true);
            else $('#end').prop('checked', true);

        }).fail(error=>{

        });
    },

    updateRoom : function (event){
        console.log("update room");
        const webinarId = $('#webinarId').val();

        event.preventDefault();
        let formData = new FormData();

        let title = $("#updateRoomTitle").val();
        title = title
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/(\n|\r\n|\r)/g, '<br>');
        $("#updateRoomTitle").val(title);

        let owner = $('#updateRoomOwner').val();
        owner = owner
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/(\n|\r\n|\r)/g, '<br>');
        $("#updateRoomOwner").val(owner);

        formData.append('roomTitle', $('#updateRoomTitle').val());
        formData.append('roomOwner', $("#updateRoomOwner").val());
        formData.append('passwd', $("#updateRoomPassword").val());
        formData.append('startDate', $("#updateStartDate").val());
        formData.append('endDate', $("#updateEndDate").val());
        formData.append('isStreaming', $('input[name="streamingStatus"]:checked').val());

        let inputFile = $('#updateRoomImage')[0];
        if (inputFile.files[0] != null) {
            formData.append('roomImage', inputFile.files[0]);
        }

        $.ajax({
            type: "put",
            url: `/api/webinar/update/${webinarId}`,
            data: formData,
            contentType: false,
            processData: false,
            dataType: "json"
        }).done(res=>{
            alert("심포지엄 수정 성공")
            location.replace("/admin/webinar"); //성공하면 이 페이지로 돌아감

        }).fail(error=>{
            console.log("심포지엄 수정 실패", error);
            alert("심포지엄 수정 실패 : \n"+ error.responseText);
        });

    },

    addParticipant : function (event){
        // isChecked 1인거 다 찾아서 ajax로 보내
        // 서버에선 리스트에 추가하고 다시 프론트로 보내줘
        let ids = $.makeArray($("[isChecked=1]").map(function(){
            return $(this).attr("memberId");
        }));
        let webinarId = $('#webinarId').val();

        if(ids.length === 0){
            alert("멤버를 선택해주세요!");
            return;
        }

        const data = {
            participants: ids,
            webinarId: webinarId
        }

        $.ajax({
            type:"post",
            url:`/api/webinar/addParticipant`,
            data: JSON.stringify(data),
            contentType:"application/json; charset=utf-8",
            dataType:"json"
        }).done(res=>{
            console.log("success");
            location.reload();
        }).fail(error=>{
        });
    },

    deleteParticipant : function (event){
        // isChecked2 1인거 다 찾아서 ajax로 보내
        // 서버에선 리스트에 빼기하고 다시 프론트로 보내줘
        let ids = $.makeArray($("[isChecked2=1]").map(function(){
            return $(this).attr("participantId");
        }));
        let webinarId = $('#webinarId').val();

        if(ids.length === 0){
            alert("멤버를 선택해주세요!");
            return;
        }

        const data = {
            participants: ids,
            webinarId: webinarId
        }

        $.ajax({
            type:"DELETE",
            url:`/api/webinar/deleteParticipant`,
            data: JSON.stringify(data),
            contentType:"application/json; charset=utf-8",
            dataType:"json"
        }).done(res=>{
            console.log("success");
            location.reload();
        }).fail(error=>{
        });
    }
}

webinarF.init();

let selectMember = function (event){
    let isChecked = $(event.target).parent().attr('isChecked');
    console.log(isChecked);
    if(isChecked === 1){
        $(event.target).parent().css("background-color", "transparent");
        $(event.target).parent().attr('isChecked', 0);
    }
    else{
        $(event.target).parent().css("background-color", "#80bdff");
        $(event.target).parent().attr('isChecked', 1);
    }

}

let selectMember2 = function (event){
    let isChecked = $(event.target).parent().attr('isChecked2');
    console.log(isChecked);
    if(isChecked === 1){
        $(event.target).parent().css("background-color", "transparent");
        $(event.target).parent().attr('isChecked2', 0);
    }
    else{
        $(event.target).parent().css("background-color", "#80bdff");
        $(event.target).parent().attr('isChecked2', 1);
    }

}