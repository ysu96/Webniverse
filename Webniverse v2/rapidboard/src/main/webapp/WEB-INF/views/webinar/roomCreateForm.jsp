<%--
  Created by IntelliJ IDEA.
  User: ysu96
  Date: 2021-10-14
  Time: 오후 2:12
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<main class="main">
    <section class="container" style="margin-top:56px">
        <div class="p-5 mb-4 bg-light rounded-3" style="background: url('/images/background6.jpg'); background-size: cover">
            <div class="container-fluid py-4 text-white">
                <h1 class="display-5 fw-bold">Symposium Management</h1>
                <p class="col-md-8 fs-4">웹 세미나 등록</p>
                <a href="/admin/webinar" class="btn btn-lg btn-primary">뒤로가기</a>
            </div>
        </div>

        <form id="roomCreateForm-${principal.member.memberId}" name="roomCreateForm" class="col-md-6 m-auto" enctype="multipart/form-data">
            <label for="roomTitle"><b>방 제목</b></label>
            <input type="text" name="roomTitle" id="roomTitle" placeholder= "방 제목" required="required" class="form-control" maxlength="80"/>

            <br>
            <label for="roomOwner"><b>개설자</b></label>
            <input type="text" name="roomOwner" id="roomOwner" placeholder= "개설자" required="required" class="form-control" maxlength="20"/>

            <br>
            <div>비밀번호</div>
            <input type="password" name="passwd" id="passwd" placeholder="비밀번호" class="form-control"/>

            <br>
            <div>시작 시간</div>
            <input type="datetime-local" name="startDate" id="startDate" class="form-control"/>

            <br>
            <div>종료 시간</div>
            <input type="datetime-local" name="endDate" id="endDate" class="form-control"/>

            <br>
            <div>대표 이미지</div>
            <input type="file" name="roomImage" class="form-control" accept="image/*"/>

            <br>
            <div>
                <button type="button" class="btn btn-primary" id="roomCreateBtn">생성</button>
                <a class="btn btn-outline-primary" href="/admin/webinar">취소</a>
            </div>
        </form>

    </section>
</main>
<script src="/js/webinar.js"></script>
</body>
<%@ include file="../layout/footer.jsp"%>
</html>
