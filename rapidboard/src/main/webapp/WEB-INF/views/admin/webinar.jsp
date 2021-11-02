<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<section class="container" style="margin-top:56px">
    <div class="p-5 mb-4 bg-light rounded-3" style="background: url('/images/background6.jpg'); background-size: cover">
        <div class="container-fluid py-4 text-white">
            <h1 class="display-5 fw-bold">Symposium Management</h1>
            <p class="col-md-8 fs-4">웹 세미나 관리</p>
            <a href="/webinar/create" class="btn btn-lg btn-primary">회의 등록</a>
        </div>
    </div>

<%--    <div style="display: flex; justify-content: space-between; margin-bottom: 5px">--%>
<%--        <h4>Symposium Management</h4>--%>
<%--        <button type="button" class="btn btn-outline-primary">--%>
<%--            <a href="/webinar/create">Create Room</a>--%>
<%--        </button>--%>
<%--    </div>--%>

    <table class="table text-center table-hover" style="vertical-align: middle" id="roomList">
        <tr class="table-secondary text-wrap">
            <th class="col-md-2">개설자</th>
            <th>제목</th>
            <th style="width: 10%" >시작 시간</th>
            <th style="width: 10%">종료 시간</th>
            <th class="col-md-2">관리</th>
            <th class="col-md-1">신청자 목록</th>
        </tr>

        <c:forEach var="room" items="${rooms.content}">
            <tr>
                <td>
                    <div>
                        <c:choose>
                            <c:when test="${room.imageFilepath != null}">
                                <img height="80" width="80" src="/showImage/${room.webinarId}" alt="photo">

                            </c:when>
                            <c:otherwise>
                                <img height="80" width="80" src="/images/default_profile.jfif" alt="photo">

                            </c:otherwise>
                        </c:choose>
                        <br>
                            ${room.roomOwner}
                    </div>

                </td>
                <td style="text-align: left">${room.roomTitle}</td>
                <td style="font-size: 14px">
                        ${room.startDate.substring(0,4)}년${room.startDate.substring(5,7)}월${room.startDate.substring(8,10)}일
                            <br>${room.startDate.substring(11,13)}시 ${room.startDate.substring(14,16)}분
                </td>
                <td style="font-size: 14px">
                        ${room.endDate.substring(0,4)}년${room.endDate.substring(5,7)}월${room.endDate.substring(8,10)}일
                    <br>${room.endDate.substring(11,13)}시 ${room.endDate.substring(14,16)}분
                </td>
                <td style="font-size: 12px">
                    <c:choose>
                        <c:when test="${room.isStreaming eq 2}">
                            <h6>종료된 심포지엄</h6>
                            <button class="btn-sm bg-secondary text-white" style="width: 45%" type="button" id="deleteBtn-${room.webinarId}" name="deleteBtn">삭제</button>
                        </c:when>

                        <c:otherwise>

                            <c:choose>
                                <c:when test="${room.isMain == 1}">
                                    <button class="btn-sm w-100 mb-1 bg-success text-white" type="button">메인 화면</button>
                                </c:when>

                                <c:otherwise>
                                    <button class="btn-sm w-100 mb-1 bg-secondary text-white" type="button" id="registerBtn-${room.webinarId}" name="registerBtn">메인 화면 등록</button>
                                </c:otherwise>
                            </c:choose>

                            <button type="button" class="btn-sm bg-secondary text-white" style="width: 45%" data-bs-toggle="modal" data-bs-target="#roomUpdateModal" id="modalUpdateBtn-${room.webinarId}" name="modalUpdateBtn">수정</button>
                            <button class="btn-sm bg-secondary text-white" style="width: 45%" type="button" id="deleteBtn-${room.webinarId}" name="deleteBtn">삭제</button>
                        </c:otherwise>
                    </c:choose>

                </td>
                <td>
                    <c:choose>
                        <c:when test="${room.isStreaming eq 2}">
                            <a href="/webinar/log/${room.webinarId}" class="btn-sm bg-secondary text-white" type="button">통계보기</a>
                        </c:when>
                        <c:otherwise>
                            <a href="/webinar/participants/${room.webinarId}" class="btn-sm bg-secondary text-white" type="button">보기</a>
                        </c:otherwise>
                    </c:choose>

                </td>
            </tr>
        </c:forEach>
    </table>

    <!-- 웨비나 업데이트 모달 -->
    <div class="modal fade" id="roomUpdateModal" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">심포지엄 수정</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form enctype="multipart/form-data">
                        <div class="mb-1">
                            <label for="updateRoomTitle" class="col-form-label">방 제목</label>
                            <input type="text" class="form-control col-lg-10" id="updateRoomTitle" required="required">
                        </div>
                        <div class="mb-1">
                            <label for="updateRoomOwner" class="col-form-label">개설자</label>
                            <input type="text" class="form-control" id="updateRoomOwner" required="required">
                        </div>
                        <div class="mb-1">
                            <label for="updateRoomPassword" class="col-form-label">비밀번호</label>
                            <input type="password" class="form-control" id="updateRoomPassword" required="required">
                        </div>
                        <div class="mb-1">
                            <label for="updateStartDate" class="col-form-label">시작 시간</label>
                            <input type="datetime-local" class="form-control" id="updateStartDate" required="required">
                        </div>
                        <div class="mb-1">
                            <label for="updateEndDate" class="col-form-label">종료 시간</label>
                            <input type="datetime-local" class="form-control" id="updateEndDate" required="required">
                        </div>
                        <div class="mb-2">
                            <label for="updateRoomImage" class="col-form-label">대표 이미지</label>
                            <input type="file" accept="image/*" class="form-control" id="updateRoomImage" required="required">
                        </div>
                        <div class="mb-1">
                            <h6>스트리밍</h6>
                            <input type="radio" name="streamingStatus" id='wait' value="0"> 대기
                            <input type="radio" name="streamingStatus" id='start' value="1"> 시작
                            <input type="radio" name="streamingStatus" id='end' value="2"> 종료
                        </div>

                        <div style="display: none">
                            <input type="text" name="webinarId" id="webinarId">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                    <button type="button" id="updateBtn" class="btn btn-primary">수정</button>
                </div>
            </div>
        </div>
    </div>


    <hr/>

    <nav>
        <ul class="pagination justify-content-center">
            <!-- 이전 5페이지-->
            <c:set var="isFirst" value="${rooms.first}"/>
            <c:if test="${isFirst eq false}">
                <li class="page-item">
                    <a class="page-link" href="/admin/webinar?page=${pageDto.prevStartIdx}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            </c:if>

            <!-- 처음 페이지면 이전 페이지 버튼 없음-->
            <c:if test="${isFirst eq false}">
                <li class="page-item">
                    <a class="page-link" href="/admin/webinar?page=${pageDto.curPage-1}" aria-label="Previous">
                        <span aria-hidden="true">&lt;</span>
                    </a>
                </li>
            </c:if>


            <c:forEach var="i" begin="${pageDto.startIdx}" end="${pageDto.endIdx}">
                <c:choose>
                    <%-- 현재 페이지면 active--%>
                    <c:when test="${pageDto.curPage eq i}">
                        <li class="page-item active"><a class="page-link" href="/admin/webinar?page=${i}">${i+1}</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item"><a class="page-link" href="/admin/webinar?page=${i}">${i+1}</a></li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <!-- 마지막 페이지면 다음 페이지 버튼 없음-->
            <c:set var="isLast" value="${rooms.last}"/>
            <c:if test="${isLast eq false}">
                <li class="page-item">
                    <a class="page-link" href="/admin/webinar?page=${pageDto.curPage+1}" aria-label="Next">
                        <span aria-hidden="true">&gt;</span>
                    </a>
                </li>
            </c:if>

            <!-- 다음 5페이지 -->
            <c:if test="${isLast eq false}">
                <li class="page-item">
                    <a class="page-link" href="/admin/webinar?page=${pageDto.nextStartIdx}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </c:if>
        </ul>
    </nav>


</section>
<script src="/js/webinar.js"></script>

</body>
</html>
