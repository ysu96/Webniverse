<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="../layout/header.jsp" %>

<main class="main">
    <section class="container" style="margin-top:56px">
        <div class="p-5 mb-4 bg-light rounded-3" style="background: url('/images/background5.png'); background-size: cover">
            <div class="container-fluid py-4 text-white">
                <h1 class="display-5 fw-bold">${dto.boardName} - (${dto.post.postId})</h1>
<%--                <p class="col-md-8 fs-4">게시판에 글을 작성하고 사람들과 소통해보세요.</p>--%>
                <br>
                <a href="/post/list" class="btn btn-lg btn-primary">목록</a>
            </div>
        </div>


            <table class="table caption-top">
                <thead>
                <tr class="table-active">
                    <th scope="col" style="width: 80%">🍀제목 : ${dto.post.title}<br>
                        🙋‍♂️작성자 : ${dto.post.member.username}</th>
                    <th scope="col" style="width: 20%" class="text-right">👁️조회수 : ${dto.post.viewCount}
                        <br> 📅작성일 : ${dto.post.createDate}</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td colspan="2" style="border-bottom: none">
                        <pre>
                            <div id="in_content">
                                <p id="content">${dto.postcontent.postcontent}</p>
                            </div>
                        </pre>
                    </td>
                </tr>
                </tbody>
                <tfoot>
                <c:if test="${dto.uploadfiles.size() != null}">
                    <tr>
                        <td colspan="2" >
                            <c:forEach var="uploadfile" items="${dto.uploadfiles}">
                                <div>
                                    <strong>첨부파일 : </strong>
                                    <a href='/download/${uploadfile.uploadfileId}'>${uploadfile.originalFilename}</a>
                                </div>
                            </c:forEach>

                        </td>
                    </tr>
                </c:if>

                </tfoot>
            </table>


        <p>댓글 : ${dto.post.commentCount}</p>
        <div class="mb-3">
            <sec:authorize access="isAuthenticated()">
                <form id="commentForm-${dto.post.postId}" name="commentForm" style="display: flex; justify-content: space-between">
                    <div>
                        <input type="text" placeholder="댓글 달기..." size="90%" id="commentInput" maxlength="100" required="required"/>
                        <button id="commentBtn" type="button" class="btn btn-outline-primary">게시</button>
                    </div>

                    <div>
                        <button type="button" class="btn btn-primary" id="commentShowBtn">댓글 접기</button>
                        <c:choose>
                            <c:when test="${dto.pageOwnerState}">
                                <a type="button" class="btn btn-primary" href="/post/update/${dto.post.postId}">수정</a>
                                <button type="button" class="btn btn-primary" id="deletePostBtn">삭제</button>
                            </c:when>
                        </c:choose>
                    </div>
                </form>
            </sec:authorize>
        </div>

        <div style="display: block" name="commentShow" id="commentShow-${dto.post.postId}">

            <div id="postCommentList-${dto.post.postId}">
                <c:forEach var="comment" items="${dto.comments.content}">

                    <c:set var="cId" value="${comment.member.memberId}"/>
                    <c:set var="pId" value="${principal.member.memberId}"/>
                    <c:set var="userRole" value="${principal.member.role}"/>

                    <%--댓글--%>
                    <style>
                        #postCommentItem-${comment.commentId} .myindent {
                            text-indent: calc(30px * ${comment.depth});
                            font-size: large;
                            margin-bottom: 5px;
                        }
                    </style>

                    <div id="postCommentItem-${comment.commentId}">
                        <div class="myindent">
                            <c:choose>
                                <c:when test="${comment.isDeleted eq 1}">
                                <span style="font-style: italic; font-weight: bold; color: blue;">
                                    - 삭제된 댓글입니다.
                                </span>
                                </c:when>
                                <c:otherwise>
                                    <b><a href="/member/${comment.member.memberId}">${comment.member.username}</a> :
                                        <c:if test="${comment.parent.member.username != null}">
                                            <span style="color:mediumblue"><a href="/member/${comment.parent.member.memberId}">@${comment.parent.member.username}</a> </span>
                                        </c:if>
                                    </b> ${comment.commentContent}

                                    <sub>${comment.createDate}</sub>

                                    <sec:authorize access="isAuthenticated()">
                                        <c:if test="${comment.depth <= 2}">
                                            <button type="button" name="recommentBoxBtn" id="recommentBoxBtn-${comment.commentId}" class="btn btn-sm btn-outline-primary">답글</button>
                                        </c:if>
                                    </sec:authorize>

                                    <%--댓글 작성자 or 관리자면 삭제, 수정버튼 --%>
                                    <c:if test="${cId eq pId || userRole=='ROLE_ADMIN'}">
                                        <button type="button" name="updateBoxBtn" id="updateBoxBtn-${comment.commentId}" class="btn btn-sm btn-outline-primary" >수정</button>
                                        <button type="button" name="deleteCommentBtn" id="deleteCommentBtn-${comment.commentId}" class="btn btn-sm btn-outline-danger">삭제</button>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>

                                <%--수정 입력창--%>
                            <form style="display: none; margin-top:5px" name="commentUpdateForm" id="commentUpdateForm-${comment.commentId}">
                                <input type="text" size="70" maxlength="100" name="commentUpdateInput" id="commentUpdateInput-${comment.commentId}" placeholder="${comment.commentContent}" required="required">
                                <button type="button" name="commentUpdateBtn" class="btn btn-outline-primary">수정</button>
                            </form>


                                <%--대댓글 입력창--%>
                            <form style="display: none; margin-top: 5px" name="recommentInputForm" id="recommentInputForm-${comment.commentId}">
                                <input type="text" size="70" maxlength="100" name="recommentInput" id="recommentInput-${comment.commentId}" placeholder="${comment.member.username}에게 답글 입력.." required="required">
                                <button type="button" name="recommentBtn" class="btn btn-outline-primary">게시</button>
                            </form>

                        </div>
                    </div>
                </c:forEach>
            </div>

            <hr/>

            <%--페이지네이션--%>
            <nav>
                <ul class="pagination justify-content-center">
                    <%--첫 페이지 여부--%>
                    <c:set var="isFirst" value="${dto.comments.first}"/>

                    <!-- 이전 5페이지-->
                    <c:if test="${isFirst eq false}">
                        <li class="page-item">
                            <a class="page-link" href="/post/list/${dto.post.postId}?page=${pageDto.prevStartIdx}">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </c:if>

                    <!-- 처음 페이지면 이전 페이지 버튼 없음-->
                    <c:if test="${isFirst eq false}">
                        <li class="page-item">
                            <a class="page-link" href="/post/list/${dto.post.postId}?page=${pageDto.curPage-1}">
                                <span aria-hidden="true">&lt;</span>
                            </a>
                        </li>
                    </c:if>

                    <c:forEach var="i" begin="${pageDto.startIdx}" end="${pageDto.endIdx}">
                        <c:choose>
                            <%-- 현재 페이지면 active--%>
                            <c:when test="${pageDto.curPage eq i}">
                                <li class="page-item active"><a class="page-link"
                                                                href="/post/list/${dto.post.postId}?page=${i}">${i+1}</a></li>
                            </c:when>
                            <c:otherwise>
                                <li class="page-item"><a class="page-link"
                                                         href="/post/list/${dto.post.postId}?page=${i}">${i+1}</a></li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <!-- 마지막 페이지면 다음 페이지 버튼 없음-->
                    <c:set var="isLast" value="${dto.comments.last}"/>

                    <c:if test="${isLast eq false}">
                        <li class="page-item">
                            <a class="page-link" href="/post/list/${dto.post.postId}?page=${pageDto.curPage+1}">
                                <span aria-hidden="true">&gt;</span>
                            </a>
                        </li>
                    </c:if>

                    <!-- 다음 5페이지 -->
                    <c:if test="${isLast eq false}">
                        <li class="page-item">
                            <a class="page-link" href="/post/list/${dto.post.postId}?page=${pageDto.nextStartIdx}">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </div>
    </section>
</main>
<script src="/js/post.js"></script>
<script src="/js/comment.js"></script>
</body>
<%@ include file="../layout/footer.jsp"%>
</html>