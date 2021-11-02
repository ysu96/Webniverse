<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<main class="main">
    <section class="container" style="margin-top:56px">

        <div class="p-5 mb-4 bg-light rounded-3" style="background: url('/images/background4.jpg'); background-size: cover">
            <div class="container-fluid py-4 text-white">
                <h1 class="display-5 fw-bold">Update Info</h1>
                <p class="col-md-8 fs-4">유저 정보를 수정하세요.</p>
                <a href="/member/${principal.member.memberId}" class="btn btn-lg btn-primary">마이페이지</a>
            </div>
        </div>

        <form id="profileUpdateForm-${principal.member.memberId}" name="profileUpdateForm" class="col-md-6 m-auto">
            <div class="fs-5 fw-bold">Username</div>
            <input type="text" name="username" placeholder= "Username"
                   value="${principal.member.username}" required="required" class="form-control" disabled/>
            <br>
            <div class="fs-5 fw-bold">* Name</div>
            <input type="text" name="name" placeholder="Name"
                    value="${principal.member.name}" required="required" class="form-control"/>

            <br>
            <div class="fs-5 fw-bold">* Currnet Password</div>
            <input type="password" name="currentPassword" placeholder="Currnet Password"  required="required" class="form-control"/>

            <br>
            <div class="fs-5 fw-bold">[New Password If You Want]</div>
            <input type="password" name="newPassword" placeholder="New Password" class="form-control" style="background-color: #e2e2e2;"/>

            <br>
            <div class="fs-5 fw-bold">Email</div>
            <input type="text" name="email" placeholder="email"
                   value="${principal.member.email}" disabled class="form-control"/>

            <br>

            <div style="float:right;">
                <button type="button" class="btn btn-primary" id="profileUpdateBtn">제출</button>
                <a class="btn btn-outline-primary" href="/member/${principal.member.memberId}">취소</a>
                <button type="button" class="btn btn-warning" id="deleteMemberBtn">회원 탈퇴</button>
            </div>
        </form>





    </section>
</main>
<script src="/js/update.js"></script>
<script src="/js/signup.js"></script>
</body>
<%@ include file="../layout/footer.jsp"%>
</html>