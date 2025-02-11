document.addEventListener('DOMContentLoaded', () => {
    const form = document.querySelector("form");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();  // Sayfanın yeniden yüklenmesini engeller

        // Form elemanlarından verileri al
        const name = form.querySelector('input[placeholder="İsim"]').value.trim();
        const surName = form.querySelector('input[placeholder="Soyisim"]').value.trim();
        const mail = form.querySelector('input[placeholder="E-posta"]').value.trim();
        const password = form.querySelector('input[placeholder="Şifre"]').value.trim();

        // Kullanıcı verilerini bir nesne olarak topla
        const userData = {
            name,
            surName,
            mail,
            password
        };

        try {
            // API'ye POST isteği gönder
            const response = await fetch("http://localhost:8080/user/save", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(userData)
            });

            // Yanıt başarılıysa
            if (response.ok) {
                alert("Kayıt başarılı!");  // Kullanıcıya başarılı kayıt mesajı
                form.reset();  // Formu sıfırla
                window.location.href = "http://localhost:8080/start";  // Kullanıcıyı başka bir sayfaya yönlendir
            } else {
                alert("Bir hata oluştu. Lütfen tekrar deneyin.");  // Hata durumunda kullanıcıyı bilgilendir
            }
        } catch (error) {
            console.error("Hata:", error);
            alert("Sunucuya bağlanırken bir hata oluştu.");  // Bağlantı hatası durumunda kullanıcıyı bilgilendir
        }
    });
});
