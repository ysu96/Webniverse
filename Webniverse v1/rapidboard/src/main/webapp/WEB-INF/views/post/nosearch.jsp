<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<main class="main">
    <section class="container">
        <sec:authorize access="hasRole('ROLE_ADMIN')">
            <h3>관리자입니다.</h3>
        </sec:authorize>

        <h4>${keyword} 검색 결과가 없습니다.</h4>

        <hr/>

    </section>
</main>
<script src="/js/post.js"></script>
</body>
<%@ include file="../layout/footer.jsp"%>
</html>





