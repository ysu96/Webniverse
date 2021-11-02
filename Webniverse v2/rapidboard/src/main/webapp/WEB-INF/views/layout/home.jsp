<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<main class="main" >

    <section class="container " style="margin-top:56px">
<%--        <div class="p-5 mb-4 bg-light rounded-3" style="background: url('https://images.pexels.com/photos/956981/milky-way-starry-sky-night-sky-star-956981.jpeg?auto=compress&cs=tinysrgb&fit=crop&h=627&w=1200'); background-size: cover">--%>
        <div class="p-5 mb-4 bg-light rounded-3" style="background: url('/images/background1.jpg'); background-size: cover">
            <div class="container-fluid py-4 text-white">
                <h1 class="display-5 fw-bold">Share your ideas.</h1>
                <p class="col-md-8 fs-4">게시판에 글을 작성하고 사람들과 소통해보세요.</p>
                <a href="/post/list" class="btn btn-lg btn-primary">이동하기</a>
            </div>
        </div>

        <div class="row align-items-md-stretch ">
            <div class="col-md-6 ">
                <div class="h-100 p-4 text-white rounded-3 border" style="background-color: #212529">
                    <div class="mb-5">
                        <h2 style="display: inline" class="fw-bold">Registration</h2>

                        <a  style="display: inline; float: right" href="/webinar/list" class="btn btn-outline-light"><i class="fas fa-arrow-right"></i></a>
                    </div>
                    <div>
                        <c:choose>
                            <c:when test="${mainRoom eq null}">
                                <div class="h-80 p-3 text-black bg-white rounded-3 mb-3">
                                    <p>등록된 심포지엄이 없습니다.</p>
                                </div>
                                <a href="/webinar/list" class="btn btn-outline-light" type="button">더보기</a>
                            </c:when>
                            <c:otherwise>
                                <div class="h-80 p-3 text-black bg-white rounded-3 mb-3 row" >
                                    <div class="col-2 text-center" >
                                        <c:choose>
                                            <c:when test="${mainRoom.imageFilepath != null}">
                                                <img height="80" width="80" src="/showImage/${mainRoom.webinarId}" alt="photo">

                                            </c:when>
                                            <c:otherwise>
                                                <img height="80" width="80" src="/images/default_profile.jfif" alt="photo">
                                            </c:otherwise>
                                        </c:choose>

                                            ${mainRoom.roomOwner}
                                    </div>
                                    <div class="col-5 text-center">
                                        <h5 class="text-center"><b>주제</b></h5>
                                            ${mainRoom.roomTitle}
                                    </div>

                                    <div class="col-5">
                                        <h5 class="text-center"><b>일정</b></h5>
                                            ${mainRoom.startDate.substring(0,4)}년${mainRoom.startDate.substring(5,7)}월${mainRoom.startDate.substring(8,10)}일
                                        ${mainRoom.startDate.substring(11,13)}시 ${mainRoom.startDate.substring(14,16)}분 ~
                                        <br>${mainRoom.endDate.substring(0,4)}년${mainRoom.endDate.substring(5,7)}월${mainRoom.endDate.substring(8,10)}일 ${mainRoom.endDate.substring(11,13)}시 ${mainRoom.endDate.substring(14,16)}분

                                    </div>

                                </div>
                                <c:choose>
                                    <c:when test="${mainRoom.isStreaming == 2}">
                                        <button class="btn btn-outline-light" type="button" disabled>종료됨</button>
                                    </c:when>
                                    <c:when test="${mainRoom.isStreaming == 1}">
                                        <img src="/images/on-air.png" alt="" width="50" height="40">
                                        <a class="btn btn-outline-light" style="float:right;" type="button" href="/webinar/enter/${mainRoom.webinarId}">입장하기</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="btn btn-outline-light" style="float:right;" type="button" disabled>준비중</a>
                                    </c:otherwise>
                                </c:choose>

                            </c:otherwise>
                        </c:choose>
                    </div>

                </div>
            </div>
            <div class="col-md-6">
                <div class="h-40 p-4 mb-3 bg-dark text-white border rounded-3">
                    <div style="height: 45%; justify-content: space-between; display: flex;">
                        <div>
                            <span class="fw-bold fs-4">Webniverse</span>
                            <p>웹 속의 작은 우주</p>
                            <p>전 세계 사람들과 여행하세요.</p>
                        </div>
                        <div class="col-3">
                            <img src="/images/homeIcon.png" alt="" width="128px" height="128px" >
                        </div>
                    </div>
                </div>
                <div class="h-40 p-4 bg-dark text-white border rounded-3">
                    <div style="height: 45%; justify-content: space-between; display: flex;">
                        <div>
                            <span class="fw-bold fs-4">Support</span>
                            <p>070.1234.5678</p>
                            <p>문의사항이 있을 시 연락바랍니다.</p>
                        </div>
                        <div class="col-3">
                            <img src="/images/phoneIcon.png" alt="" width="128" height="128" >
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </section>
    <%@ include file="../layout/footer.jsp"%>
</main>
</body>

</html>