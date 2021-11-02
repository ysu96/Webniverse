<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>
<input type="hidden" value="${room.webinarId}" id="webinarId">

<section class="container" style="margin-top:56px">
    <div class="p-5 mb-4 bg-light rounded-3" style="background: url('/images/background6.jpg'); background-size: cover">
        <div class="container-fluid py-4 text-white">
            <h1 class="display-5 fw-bold">Participant Management</h1>
            <p class="col-md-8 fs-4">${room.roomTitle} - (${room.roomOwner})</p>
            <a href="/admin/webinar" class="btn btn-lg btn-primary">확인</a>
        </div>
    </div>

    <div class="row align-items-md-stretch h-100">
        <div class="col-md-6">
            <span class="fs-4">전체 멤버</span>
            <button type="button" class="btn btn-outline-primary" style="float: right" id="addParticipantBtn">추가</button>
            <table class="table text-center table-hover mt-3" style="vertical-align: middle" id="memberList">
                <tr class="table-secondary text-wrap">
                    <th style="width: 15%">Username</th>
                    <th style="width: 15%" >Name</th>
                    <th style="width: 20%">Email</th>
                </tr>
                <c:forEach var="member" items="${members}">
                    <tr onclick="selectMember(event)" isChecked="0" memberId="${member.memberId}">
                        <td>${member.username}</td>
                        <td>${member.name}</td>
                        <td>${member.email}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div class="col-md-6">
            <span class="fs-4">추가한 멤버</span>
            <button type="button" class="btn btn-outline-primary" style="float: right" id="deleteParticipantBtn">제외</button>
            <table class="table text-center table-hover mt-3" style="vertical-align: middle" id="participantList">
                <tr class="table-secondary text-wrap">
                    <th style="width: 15%">Username</th>
                    <th style="width: 15%" >Name</th>
                    <th style="width: 20%">Email</th>
                </tr>

                <c:forEach var="participant" items="${participants}">
                    <tr onclick="selectMember2(event)" isChecked2="0" participantId="${participant.memberId}">
                        <td>${participant.username}</td>
                        <td>${participant.name}</td>
                        <td>${participant.email}</td>
                    </tr>
                </c:forEach>

            </table>

        </div>
    </div>






</section>
<script src="/js/webinar.js"></script>

</body>
</html>
