<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<section class="container" style="margin-top:56px">
    <div class="p-5 mb-4 bg-light rounded-3" style="background: url('/images/background4.jpg'); background-size: cover">
        <div class="container-fluid py-4 text-white">
            <h1 class="display-5 fw-bold">Symposium List</h1>
            <p class="col-md-8 fs-4">화상 세미나에 입장해보세요.</p>
            <a href="/" class="btn btn-lg btn-primary">메인으로</a>
        </div>
    </div>

    <table class="table table-bordered table-hover text-center" style="vertical-align: middle" id="roomList">
        <tr class="table-secondary table-wrap">
            <th class="col-md-2">개설자</th>
            <th>제목</th>
            <th style="width: 10%" >시작 시간</th>
            <th style="width: 10%">종료 시간</th>
            <th class="col-md-2">입장</th>
        </tr>

        <c:forEach var="room" items="${rooms.content}">
            <tr>
                <td>
                    <c:choose>
                        <c:when test="${room.imageFilepath != null}">
                            <img height="80" width="80" src="/showImage/${room.webinarId}" alt="photo">
                            ${room.roomOwner}
                        </c:when>
                        <c:otherwise>
                            <img height="80" width="80" src="/images/default_profile.jfif" alt="photo">
                            ${room.roomOwner}
                        </c:otherwise>
                    </c:choose>

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
                <td>
                    <c:choose>
                        <c:when test="${room.isStreaming eq 2}">
                            <button class="btn btn-secondary" disabled><a class="nav-link text-white" href="/webinar/enter/${room.webinarId}">종료</a></button>
                        </c:when>
                        <c:when test="${room.isStreaming eq 0}">
                            <button class="btn btn-warning" disabled><a class="nav-link text-black" href="/webinar/enter/${room.webinarId}">준비중</a></button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-outline-primary"><a class="nav-link" href="/webinar/enter/${room.webinarId}">입장하기</a></button>
                        </c:otherwise>
                    </c:choose>

                </td>
            </tr>
        </c:forEach>
    </table>

    <hr/>

    <nav>
        <ul class="pagination justify-content-center">
            <!-- 이전 5페이지-->
            <c:set var="isFirst" value="${rooms.first}"/>
            <c:if test="${isFirst eq false}">
                <li class="page-item">
                    <a class="page-link" href="/webinar/list?page=${pageDto.prevStartIdx}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            </c:if>

            <!-- 처음 페이지면 이전 페이지 버튼 없음-->
            <c:if test="${isFirst eq false}">
                <li class="page-item">
                    <a class="page-link" href="/webinar/list?page=${pageDto.curPage-1}" aria-label="Previous">
                        <span aria-hidden="true">&lt;</span>
                    </a>
                </li>
            </c:if>


            <c:forEach var="i" begin="${pageDto.startIdx}" end="${pageDto.endIdx}">
                <c:choose>
                    <%-- 현재 페이지면 active--%>
                    <c:when test="${pageDto.curPage eq i}">
                        <li class="page-item active"><a class="page-link" href="/webinar/list?page=${i}">${i+1}</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item"><a class="page-link" href="/webinar/list?page=${i}">${i+1}</a></li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <!-- 마지막 페이지면 다음 페이지 버튼 없음-->
            <c:set var="isLast" value="${rooms.last}"/>
            <c:if test="${isLast eq false}">
                <li class="page-item">
                    <a class="page-link" href="/webinar/list?page=${pageDto.curPage+1}" aria-label="Next">
                        <span aria-hidden="true">&gt;</span>
                    </a>
                </li>
            </c:if>

            <!-- 다음 5페이지 -->
            <c:if test="${isLast eq false}">
                <li class="page-item">
                    <a class="page-link" href="/webinar/list?page=${pageDto.nextStartIdx}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </c:if>
        </ul>
    </nav>
</section>

</body>
</html>
