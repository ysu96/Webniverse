<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<main class="main">
    <section class="container">

        <sec:authorize access="hasRole('ROLE_ADMIN')">
            <h4>Administrator</h4>
        </sec:authorize>

        <table class="table table-bordered table-hover caption-top" id="postList">
            <caption><b>📚 ${boardName} : ${posts.totalElements}</b></caption>
            <tr>
                <th class="col-md-1">번호</th>
                <th>제목</th>
                <th class="col-md-2">글쓴이</th>
                <th class="col-md-2">시간</th>
                <th class="col-md-1">조회수</th>
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
                <!-- 이전 5페이지-->
                <c:set var="isFirst" value="${posts.first}"/>
                <c:if test="${isFirst eq false}">
                    <li class="page-item">
                        <a class="page-link" href="/post/list?page=${pageDto.prevStartIdx}" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                </c:if>

                <!-- 처음 페이지면 이전 페이지 버튼 없음-->
                <c:if test="${isFirst eq false}">
                    <li class="page-item">
                        <a class="page-link" href="/post/list?page=${pageDto.curPage-1}" aria-label="Previous">
                            <span aria-hidden="true">&lt;</span>
                        </a>
                    </li>
                </c:if>


                <c:forEach var="i" begin="${pageDto.startIdx}" end="${pageDto.endIdx}">
                    <c:choose>
                        <%-- 현재 페이지면 active--%>
                        <c:when test="${pageDto.curPage eq i}">
                            <li class="page-item active"><a class="page-link" href="/post/list?page=${i}">${i+1}</a></li>
                        </c:when>
                        <c:otherwise>
                            <li class="page-item"><a class="page-link" href="/post/list?page=${i}">${i+1}</a></li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <!-- 마지막 페이지면 다음 페이지 버튼 없음-->
                <c:set var="isLast" value="${posts.last}"/>
                <c:if test="${isLast eq false}">
                    <li class="page-item">
                        <a class="page-link" href="/post/list?page=${pageDto.curPage+1}" aria-label="Next">
                            <span aria-hidden="true">&gt;</span>
                        </a>
                    </li>
                </c:if>

                <!-- 다음 5페이지 -->
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