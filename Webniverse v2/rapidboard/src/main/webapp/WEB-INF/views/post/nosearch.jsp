<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<main class="main">
    <section class="container" style="margin-top:56px">
<%--        <sec:authorize access="hasRole('ROLE_ADMIN')">--%>
<%--            <h3>관리자입니다.</h3>--%>
<%--        </sec:authorize>--%>

        <div class="p-5 mb-4 bg-light rounded-3" style="background: url('/images/background5.png'); background-size: cover">
            <div class="container-fluid py-4 text-white">
                <h1 class="display-5 fw-bold">${keyword} 검색 결과가 없습니다.</h1>
                <p class="col-md-8 fs-4">다른 검색어를 입력해보세요.</p>
                <a href="/post/list" class="btn btn-lg btn-primary">전체 목록</a>
            </div>
        </div>

        <hr/>
    </section>
</main>
<script src="/js/post.js"></script>
</body>
<%@ include file="../layout/footer.jsp"%>
</html>





