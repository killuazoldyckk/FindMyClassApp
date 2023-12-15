<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    <style>
        *{
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        .formLogin{
            width: 100%;
            max-width: 500px;
            text-align: center;
            margin: auto;
        }

        .registerButton{
            text-decoration: none;
        }

        .loginBtn{
            padding-left: 2rem ;
            padding-right: 2rem;
        }

    </style>
</head>
<body>
    <form class="formLogin p-4" id="loginForm" >
        <h2>Login</h2>
        <div class="form-floating mb-3">
            <input type="email" class="form-control" id="floatingInput" placeholder="name@example.com">
            <label for="floatingInput">Email address</label>
        </div>
        <div class="form-floating">
            <input type="password" class="form-control" id="floatingPassword" placeholder="Password">
            <label for="floatingPassword">Password</label>
        </div>
        <button type="submit" class="loginBtn btn btn-primary mt-3" >Login</button>
        <p class="mb-0 mt-2">Belum punya akun ?</p>
        <a href="register.php" class="registerButton">Daftar</a>
    </form>
    <script type="module">
        // Import the functions you need from the SDKs you need
        import { initializeApp } from "https://www.gstatic.com/firebasejs/10.7.1/firebase-app.js";
        import { getAuth, signInWithEmailAndPassword } from "https://www.gstatic.com/firebasejs/10.7.1/firebase-auth.js";

        // Your web app's Firebase configuration
        // For Firebase JS SDK v7.20.0 and later, measurementId is optional
        const firebaseConfig = {
            apiKey: "AIzaSyAGQ6CUM9ik5KcYeMe3zROC2tC0G06i75Y",
            authDomain: "findmyclassjava.firebaseapp.com",
            databaseURL: "https://findmyclassjava-default-rtdb.asia-southeast1.firebasedatabase.app",
            projectId: "findmyclassjava",
            storageBucket: "findmyclassjava.appspot.com",
            messagingSenderId: "362186543525",
            appId: "1:362186543525:web:ff24f5cd22d95ceb73d84c",
            measurementId: "G-4W36PE8TMW"
        };

        // Initialize Firebase
        const app = initializeApp(firebaseConfig);
        const auth = getAuth();
        const loginForm = document.getElementById('loginForm');

        loginForm.addEventListener('submit',(e) => {

            e.preventDefault(); // Prevent form submission

            var email = document.getElementById('floatingInput').value;
            var password = document.getElementById('floatingPassword').value;

            signInWithEmailAndPassword(auth, email, password)
            .then((userCredential) => {
                // Signed in 
                const user = userCredential.user;
                window.location.href = 'jadwal.php';
                         })
            .catch((error) => {
                const errorCode = error.code;
                const errorMessage = error.message;

                alert(errorMessage);
            });

        })
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>

</body>
</html>
