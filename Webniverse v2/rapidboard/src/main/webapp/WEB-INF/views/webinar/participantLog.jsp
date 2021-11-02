<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>
<input type="hidden" value="${room.webinarId}" id="webinarId">

<section class="container" style="margin-top:56px">
    <div class="p-5 mb-4 bg-light rounded-3" style="background: url('/images/background6.jpg'); background-size: cover">
        <div class="container-fluid py-4 text-white">
            <h1 class="display-5 fw-bold">${room.roomTitle} </h1>
            <p class="col-md-8 fs-4">참여자 통계</p>

            <a class="btn btn-lg btn-primary" href="/excel/download/${room.webinarId}"><img src="/images/iconExcel.png" alt="" width="30" height="30"> 다운로드</a>
            <a href="/admin/webinar" class="btn btn-lg btn-primary" style="height: 51px">뒤로가기</a>
        </div>
    </div>

    <div style="display: flex; justify-content: space-between" class="mt-3">

    </div>

    <div class="row align-items-md-stretch h-100">
        <div class="col-md-12">
<%--            <span class="fs-4">전체 멤버</span>--%>
            <table class="table text-center table-hover mt-3" style="vertical-align: middle" id="memberList">
                <tr class="table-secondary text-wrap">
                    <th style="width: 10%">Username</th>
                    <th style="width: 10%" >Name</th>
                    <th style="width: 20%">Email</th>
                    <th style="width: 10%">입/퇴장</th>
                    <th style="width: 15%">시간</th>
                    <th style="width: 20%">디바이스 정보</th>
                    <th style="width: 20%">OS</th>
                </tr>
                <c:forEach var="log" items="${logs}">
                    <tr>
                        <td>${log.username}</td>
                        <td>${log.name}</td>
                        <td>${log.email}</td>
                        <td>${log.type}</td>
                        <td>${log.logDate.substring(0,25)}</td>

                        <td>${log.deviceInfo}</td>
                        <td>${log.osInfo}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

    </div>

</section>
<script src="/js/webinar.js"></script>

</body>
</html>
