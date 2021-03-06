<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<main class="main">
    <section class="container">

        <sec:authorize access="hasRole('ROLE_ADMIN')">
            <h4>Administrator</h4>
        </sec:authorize>

        <table class="table table-bordered table-hover caption-top" id="postList">
            <caption><b>π ${boardName} : ${posts.totalElements}</b></caption>
            <tr>
                <th class="col-md-1">λ²νΈ</th>
                <th>μ λͺ©</th>
                <th class="col-md-2">κΈμ΄μ΄</th>
                <th class="col-md-2">μκ°</th>
                <th class="col-md-1">μ‘°νμ</th>
            </tr>

            <c:forEach var="post" items="${posts.content}">
                <tr>
                    <td>${post.postId}</td>
                    <td><a href="/post/list/${post.postId}" name="addViewCountBtn" id="addViewCountBtn-${post.postId}">${post.title}</a> (${post.commentCount})</td>
                    <td>${post.member.username}</td>
                    <td>${post.createDate}</td>
                    <td>${post.viewCount}</td>
                </tr>
            </c:forEach>
        </table>

        <hr/>

        <nav>
            <ul class="pagination justify-content-center">
                <!-- μ΄μ  5νμ΄μ§-->
                <c:set var="isFirst" value="${posts.first}"/>
                <c:if test="${isFirst eq false}">
                    <li class="page-item">
                        <a class="page-link" href="/post/list?page=${pageDto.prevStartIdx}" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                </c:if>

                <!-- μ²μ νμ΄μ§λ©΄ μ΄μ  νμ΄μ§ λ²νΌ μμ-->
                <c:if test="${isFirst eq false}">
                    <li class="page-item">
                        <a class="page-link" href="/post/list?page=${pageDto.curPage-1}" aria-label="Previous">
                            <span aria-hidden="true">&lt;</span>
                        </a>
                    </li>
                </c:if>


                <c:forEach var="i" begin="${pageDto.startIdx}" end="${pageDto.endIdx}">
                    <c:choose>
                        <%-- νμ¬ νμ΄μ§λ©΄ active--%>
                        <c:when test="${pageDto.curPage eq i}">
                            <li class="page-item active"><a class="page-link" href="/post/list?page=${i}">${i+1}</a></li>
                        </c:when>
                        <c:otherwise>
                            <li class="page-item"><a class="page-link" href="/post/list?page=${i}">${i+1}</a></li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <!-- λ§μ§λ§ νμ΄μ§λ©΄ λ€μ νμ΄μ§ λ²νΌ μμ-->
                <c:set var="isLast" value="${posts.last}"/>
                <c:if test="${isLast eq false}">
                    <li class="page-item">
                        <a class="page-link" href="/post/list?page=${pageDto.curPage+1}" aria-label="Next">
                            <span aria-hidden="true">&gt;</span>
                        </a>
                    </li>
                </c:if>

                <!-- λ€μ 5νμ΄μ§ -->
                <c:if test="${isLast eq false}">
                    <li class="page-item">
                        <a class="page-link" href="/post/list?page=${pageDto.nextStartIdx}" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </c:if>
            </ul>
        </nav>

    </section>
</main>
<script src="/js/post.js"></script>
</body>

<%@ include file="../layout/footer.jsp"%>
</html>