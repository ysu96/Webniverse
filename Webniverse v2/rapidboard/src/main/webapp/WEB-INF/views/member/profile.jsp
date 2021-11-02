<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="../layout/header.jsp" %>

<main class="main">
    <section class="container" style="margin-top:56px">
        <!--전체 리스트 시작-->

        <div class="p-5 mb-4 bg-light rounded-3" style="background: url('/images/background4.jpg'); background-size: cover">
            <div class="container-fluid py-4 text-white">
                <h1 class="display-5 fw-bold">My Page</h1>
                <p class="col-md-8 fs-4">유저의 정보와 글을 확인하세요.</p>

                <c:if test="${principal.member.memberId eq dto.member.memberId}">
                    <a href="/member/${principal.member.memberId}/update" class="btn btn-lg btn-primary">수정하기</a>
                </c:if>
            </div>
        </div>
        <table class="table caption-top">

            <thead>
            <tr class="table-active">
                <th scope="col" style="width: 50%">🙋‍♂️Username : ${dto.member.username}<br>
                    🙋‍♀️Name : ${dto.member.name}<br>
                    📭Email : ${dto.member.email}
                </th>
                <th scope="col" style="width: 50%" class="text-right">📃Posts : ${dto.postCount}
                    <br>🏷️Role : ${dto.member.role}<br>
                </th>
            </tr>
            </thead>
        </table>

        <hr/>
        <table class="table table-bordered table-hover caption-top" id="postList">
            <caption><b>My Posts</b></caption>
            <tr class="table-secondary">
                <th class="col-md-1">번호</th>
                <th>제목</th>
                <th class="col-md-2">글쓴이</th>
                <th class="col-md-2">시간</th>
                <th class="col-md-1">조회수</th>
            </tr>

            <c:forEach var="post" items="${dto.posts.content}">
                <tr>
                    <td>${post.postId}</td>
                    <td><a href="/post/list/${post.postId}" onclick="addViewCount(${post.postId})">${post.title}</a></td>
                    <td>${post.member.username}</td>
                    <td>${post.createDate}</td>
                    <td>${post.viewCount}</td>
                </tr>
            </c:forEach>
        </table>
    </section>

    <hr/>

    <nav>
        <ul class="pagination justify-content-center">

            <!-- 이전 5페이지-->
            <c:set var="isFirst" value="${dto.posts.first}"/>

            <c:if test="${isFirst eq false}">
                <li class="page-item">
                    <a class="page-link" href="/member/${dto.member.memberId}?page=${pageDto.prevStartIdx}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            </c:if>

            <!-- 처음 페이지면 이전 페이지 버튼 없음-->
            <c:if test="${isFirst eq false}">
                <li class="page-item">
                    <a class="page-link" href="/member/${dto.member.memberId}?page=${pageDto.curPage-1}" aria-label="Previous">
                        <span aria-hidden="true">&lt;</span>
                    </a>
                </li>
            </c:if>


            <c:forEach var="i" begin="${pageDto.startIdx}" end="${pageDto.endIdx}">
                <c:choose>
                    <%-- 현재 페이지면 active--%>
                    <c:when test="${pageDto.curPage eq i}">
                        <li class="page-item active"><a class="page-link" href="/member/${dto.member.memberId}?page=${i}">${i+1}</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item"><a class="page-link" href="/member/${dto.member.memberId}?page=${i}">${i+1}</a></li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>


            <!-- 마지막 페이지면 다음 페이지 버튼 없음-->
            <c:set var="isLast" value="${dto.posts.last}"/>

            <c:if test="${isLast eq false}">
                <li class="page-item">
                    <a class="page-link" href="/member/${dto.member.memberId}?page=${pageDto.curPage+1}" aria-label="Next">
                        <span aria-hidden="true">&gt;</span>
                    </a>
                </li>
            </c:if>

            <!-- 다음 5페이지 -->
            <c:if test="${isLast eq false}">
                <li class="page-item">
                    <a class="page-link" href="/member/${dto.member.memberId}?page=${pageDto.nextStartIdx}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </c:if>
        </ul>
    </nav>

</main>
<script src="/js/post.js"></script>
</body>
<%@ include file="../layout/footer.jsp"%>
</html>