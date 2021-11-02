<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
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

    <!-- Custom styles for this template -->
    <link href="/css/style.css" rel="stylesheet">
</head>
<body class="text-center">

    <main class="form-signin">
        <form id="signupForm" action="/auth/signup" method="POST">
            <img class="mb-4" src="/images/logo3.jpg" alt="" width="250" height="150">
            <h1 class="h3 mb-3 fw-normal">Please sign up</h1>
            <br/>
            <div class="form-floating">
                <input type="text" class="form-control" id="floatingInput" placeholder="Username" name="username" required="required" maxlength="100" minlength="2">
                <label for="floatingInput">Username</label>
                <a id="usernameCheckBtn" class="btn btn-outline-primary" style="height: 15pt; width: 50%; font-size: 10px; margin-bottom: 5px"><b>Check</b></a>
            </div>

            <div id="checkUsernameInfoForm" style="display: none">
                <p style="color:red;" id="checkUsernameInfo"></p>
            </div>

            <div class="form-floating">
                <input type="password" class="form-control" id="floatingPassword" placeholder="Password" name="password" required="required" maxlength="100">
                <label for="floatingPassword">Password</label>
            </div>

            <div class="form-floating">
                <input type="text" class="form-control" id="floatingInput2" placeholder="Name" name="name" required="required" maxlength="255">
                <label for="floatingInput2">Name</label>
            </div>

            <div class="form-floating">
                <input type="email" class="form-control" id="floatingInput3" placeholder="Email" name="email" required="required" maxlength="255">
                <label for="floatingInput3">Email</label>
            </div>

            <div class="checkbox mb-3">
                <label>
                    <input type="checkbox" value="ROLE_ADMIN" name="role"> Administrator
                </label>
            </div>

            <input type="number" value="0" id="isValidateChecked" name="isValidateCheck" style="display: none">

            <button form="signupForm" class="w-100 btn btn-lg btn-primary" type="submit">Sign up</button>

            <a style="margin-top: 10px" type="button" class="w-100 btn btn-lg btn-primary" href="/auth/signin">Cancel</a>

            <p class="mt-5 mb-3 text-muted">&copy; Gooroomee Toy Project</p>
        </form>
    </main>
</body>
<script src="/js/signup.js"></script>
</html>
