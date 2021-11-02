<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rapid Board</title>

    <!-- bootstrap-->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-/bQdsTh/da6pkI1MST/rWKFNjaCP5gBSY4sEBT38Q/9RBh9AH40zEOg7Hlq2THRZ" crossorigin="anonymous"></script>

    <!-- Custom styles for this template -->
    <link href="/css/style.css" rel="stylesheet">
</head>
<body class="text-center" style="background: url('/images/background2.jpg'); background-size: cover">

<main class="form-signin">
    <img class="mb-4" src="/images/astronomy_moon_galaxy_planet_space_system_universe_icon_156880.png" alt="" width="250" height="250">
    <h1 class="h3 mb-3 fw-bold text-white">Please sign in!</h1>

    <form action="/auth/signin" method="POST" class="bg-white p-2 rounded-3">
        <!--로그인 실패 메시지 출력-->
        <%
            String s = request.getAttribute("loginFailMsg").toString();
            out.println("<p style=\"color:red;\"><b>" + s + "</b></p>");
        %>

        <div class="form-floating">
            <input type="text" class="form-control" id="floatingInput" placeholder="Username" name="username" required="required" maxlength="100">
            <label for="floatingInput">Username</label>
        </div>
        <div class="form-floating">
            <input type="password" class="form-control" id="floatingPassword" placeholder="Password" name="password" required="required" maxlength="100">
            <label for="floatingPassword">Password</label>
        </div>

        <button class="w-100 btn btn-lg btn-primary" type="submit">Sign in</button>

        <a style="margin-top: 10px" type="button" class="w-100 btn btn-lg btn-primary" href="/auth/signup">Sign up</a>


    </form>
    <p class="mt-5 mb-3 text-muted">&copy; Webniverse Corp</p>
</main>

</body>
</html>
