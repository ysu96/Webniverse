<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<main class="main">
    <section class="container">
        <form id="profileUpdateForm-${principal.member.memberId}" name="profileUpdateForm" class="col-md-6">
            <div>Username</div>
            <input type="text" name="username" placeholder= "Username"
                   value="${principal.member.username}" required="required" class="form-control" disabled/>

            <div>Name</div>
            <input type="text" name="name" placeholder="Name"
                    value="${principal.member.name}" required="required" class="form-control"/>

            <div>Currnet Password</div>
            <input type="password" name="currentPassword" placeholder="Currnet Password"  required="required" class="form-control"/>

            <div>[New Password If You Want]</div>
            <input type="password" name="newPassword" placeholder="New Password" class="form-control" style="background-color: #e2e2e2;"/>

            <div>Email</div>
            <input type="text" name="email" placeholder="email"
                   value="${principal.member.email}" disabled class="form-control"/>


            <div>
                <button type="button" class="btn btn-primary" id="profileUpdateBtn">제출</button>
                <a class="btn btn-outline-primary" href="/member/${principal.member.memberId}">취소</a>
            </div>
        </form>

        <br>
        <button type="button" class="btn btn-warning" id="deleteMemberBtn">회원 탈퇴</button>



    </section>
</main>
<script src="/js/update.js"></script>
<script src="/js/signup.js"></script>
</body>
<%@ include file="../layout/footer.jsp"%>
</html>