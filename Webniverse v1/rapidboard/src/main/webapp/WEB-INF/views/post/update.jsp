<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<main class="main">
    <section class="container mt-3">
        <form style="margin-left: 60px; margin-right: 60px" id="updatePostForm-${post.postId}" name="updatePostForm">
            <div style="display: none">
                <input type="text" name="board_id" id="board_id" value="${post.board.boardId}">
            </div>

            <div class="form-group mt-1">
                <label for="title"><b>제목</b></label>
                <input type="text" class="form-control" name="title" id="title" placeholder="제목을 입력하세요." maxlength="100" required="required" value="${post.title}">
            </div>

            <div class="form-group mt-1">
                <label for="content"><b>내용</b></label>
                <textarea class="form-control" name="content" id="content" rows="15" placeholder="내용을 입력하세요." required="required" maxlength="65535">${post.postcontent.postcontent}</textarea>
            </div>

            <c:forEach var="uploadfile" items="${uploadfiles}">
                <div>
                    <strong>첨부파일 : </strong>
                    <a href='#'>${uploadfile.originalFilename}</a>
                    <button type="button" id="uploadfileDeleteBtn-${uploadfile.uploadfileId}" name="uploadfileDeleteBtn">삭제</button>
                </div>
            </c:forEach>

            <div>
                <label for="inputFiles">파일을 선택해 주세요.</label>
                <input multiple="multiple" name="inputFiles" type="file" class="form-control" id="inputFiles">
            </div>

            <div class="modal-footer">
                <button type="button" id="updatePostBtn" class="btn btn-primary">저장</button>
                <a type="button" class="btn btn-primary" href="javascript:history.back()">취소</a>
            </div>

        </form>
    </section>
</main>
<script src="/js/post.js"></script>
</body>
<%@ include file="../layout/footer.jsp"%>
</html>