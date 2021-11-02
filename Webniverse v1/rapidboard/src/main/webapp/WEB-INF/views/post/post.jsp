<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<main class="main">
    <section class="container mt-3">
        <form style="margin-left: 60px; margin-right: 60px" id="postInputForm-${principal.member.memberId}" name="postInputForm" enctype="multipart/form-data">
            <select class="form-select" name="board_id" id="post_board_id">
                <c:forEach var="board" items="${boards}">
                    <option value="${board.boardId}">${board.name}</option>
                </c:forEach>
            </select>
            <div class="form-group mt-1">
                <label for="post_title"><b>제목</b></label>
               <input type="text" class="form-control" name="title" id="post_title" placeholder="제목을 입력하세요." maxlength="100" required>
            </div>

            <div class="form-group mt-1">
                <label for="post_content"><b>내용</b></label>
                <textarea class="form-control" name="content" id="post_content" rows="15" placeholder="내용을 입력하세요" required maxlength="65535"></textarea>
            </div>

            <div>
                <label for="inputFiles">파일을 선택해 주세요.</label>
                <input multiple="multiple" name="inputFiles" type="file" class="form-control" id="inputFiles">
            </div>

            <div class="modal-footer">
                <button type="button" id="createPostBtn" class="btn btn-primary">저장</button>
                <a type="button" class="btn btn-primary" href="javascript:history.back()">취소</a>
            </div>

        </form>
    </section>
</main>
<script src="/js/post.js"></script>
</body>
<%@ include file="../layout/footer.jsp"%>
</html>