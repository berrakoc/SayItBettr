document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("loginForm");

    if (loginForm) {
        loginForm.addEventListener("submit", async function (e) {
            e.preventDefault();

            const email = document.getElementById("email").value.trim();
            const password = document.getElementById("password").value.trim();

            const loginData = {
                email: email,
                password: password
            };

            try {
                const response = await fetch("http://localhost:8080/user/login", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(loginData)
                });

                if (response.ok) {
                    window.location.href = "http://localhost:8080/home";
                } else {
                    const errorData = await response.json();
                    alert(errorData.message || "Invalid email or password. Please try again.");
                }
            } catch (error) {
                console.error("Hata:", error);
                alert("Sunucuya bağlanırken bir hata oluştu: " + error.message);
            }
        });
    } else {
        console.error("Login formu bulunamadı!");
    }
});
