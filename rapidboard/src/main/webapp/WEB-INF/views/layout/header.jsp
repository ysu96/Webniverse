<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!-- 세션정보 접근 -->
<sec:authorize access="isAuthenticated()">
    <sec:authentication property="principal" var="principal"/> <!-- principal property : UserDetails-->
</sec:authorize>


<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rapid Board</title>

    <!-- 제이쿼리 -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

    <!-- bootstrap-->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-/bQdsTh/da6pkI1MST/rWKFNjaCP5gBSY4sEBT38Q/9RBh9AH40zEOg7Hlq2THRZ" crossorigin="anonymous"></script>

    <!-- Fontawesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css" />
    <!-- Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@100;200;300;400;500;600;700&display=swap" rel="stylesheet">

    <script src="/js/board.js"></script>

    <style type="text/css">
        a { text-decoration:none }
    </style>
</head>

<body>

    <header class="header">
        <div class="bg-dark fixed-top">
            <nav class="navbar navbar-expand-lg navbar-light ">
                <div class="container-fluid ">
                    <a class="navbar-brand" href="/">
                        <img src="/images/astronomy_moon_galaxy_planet_space_system_universe_icon_156880.png" alt="" width="30" height="30">
                    </a>

                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                            <li class="nav-item">
                                <a class="nav-link text-white" href="/">Webniverse</a>
                            </li>

                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle text-white" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false" aria-haspopup="true">
                                    Boards
                                </a>
                                <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                                    <li><a class="dropdown-item" href="/post/list">전체보기</a></li>
                                    <c:forEach var="board" items="${boards}">
                                        <li><a class="dropdown-item" href="/post/${board.boardId}/list">${board.name}</a></li>
                                    </c:forEach>

                                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                                        <li><hr class="dropdown-divider"></li>
                                        <li><button type="button" class="dropdown-item" data-bs-toggle="modal" data-bs-target="#boardCreateModal">Create Board</button></li>
                                        <li><button type="button" class="dropdown-item" data-bs-toggle="modal" data-bs-target="#boardDeleteModal">Delete Board</button></li>
                                    </sec:authorize>

                                </ul>
                            </li>

                            <li class="nav-item">
                                <a class="nav-link text-white" href="/webinar/list">Rooms</a>
                            </li>

                            <sec:authorize access="hasRole('ROLE_ADMIN')">
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle text-white" href="#" id="navbarDropdownAdmin" role="button" data-bs-toggle="dropdown" aria-expanded="false" aria-haspopup="true">Admin</a>
                                    <ul class="dropdown-menu" aria-labelledby="navbarDropdownAdmin">
                                        <li><a class="dropdown-item" href="/admin/webinar">심포지엄 관리</a></li>
                                        <li><a class="dropdown-item" href="/admin/member">회원 관리</a></li>
                                    </ul>
                                </li>

                            </sec:authorize>


                            <sec:authorize access="isAuthenticated()">
                                <li class="nav-item">
                                    <a class="nav-link text-white" href="/member/${principal.member.memberId}">My Page</a>
                                </li>
                            </sec:authorize>
                            <sec:authorize access="isAnonymous()">
                                <li class="nav-item">
                                    <a class="nav-link text-white" href="/auth/signin">My Page</a>
                                </li>
                            </sec:authorize>

                            <sec:authorize access="isAuthenticated()">
                                <li class="nav-item">
                                    <a class="nav-link text-white" href="/logout">Logout</a>
                                </li>
                            </sec:authorize>
                            <sec:authorize access="isAnonymous()">
                                <li class="nav-item">
                                    <a class="nav-link text-white" href="/auth/signin">Login</a>
                                </li>
                            </sec:authorize>
                        </ul>

                        <form class="d-flex" action="/post/search" method="get">
                            <select class="form-select w-50" name="searchType">
                                <option value="title" selected>제목</option>
                                <option value="content">내용</option>
                                <option value="writer">작성자</option>
                                <option value="titleContent">제목+내용</option>
                            </select>
                            <input style="margin-left: 5px" class="form-control me-2" type="search" placeholder="Search" name="keyword" required="required" minlength="2">
                            <button class="btn btn-outline-light" type="submit">Search</button>
                        </form>
                        <sec:authorize access="isAuthenticated()">
                            <a style="margin-left: 5px" type="button" class="btn btn-outline-light" href="/post/post/${principal.member.memberId}">Post</a>
                        </sec:authorize>
                    </div>
                </div>
            </nav>
        </div>

        <!-- 게시판 생성 모달 -->
        <div class="modal fade" id="boardCreateModal" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Create Board</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form>
                            <div class="mb-3">
                                <label for="create_board_name" class="col-form-label">게시판 이름:</label>
                                <input type="text" class="form-control" id="create_board_name" required="required" placeholder="Korean, English, Number is allowed">
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="button" id="createBoardBtn" class="btn btn-primary">Create</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- 게시판 삭제 모달 -->
        <div class="modal fade" id="boardDeleteModal" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Delete Board</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form>
                            <div class="mb-3">
                                <label for="delete_board_id" class="col-form-label">게시판 이름:</label>
                                <select class="form-select" id="delete_board_id">
                                    <c:forEach var="board" items="${boards}">
                                        <option value="${board.boardId}">${board.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="button" id="deleteBoardBtn" class="btn btn-primary">Delete</button>
                    </div>
                </div>
            </div>
        </div>

        <script src="/js/board.js"></script>
    </header>
