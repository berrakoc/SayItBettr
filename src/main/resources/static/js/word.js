let currentWordName=""
let currentWordPronunciation=""
async function fetchWordDetail() {
    const params = new URLSearchParams(window.location.search);
    const wordId = params.get('id');

    if (!wordId) return;

    const response = await fetch(`http://localhost:8080/word/${wordId}`);
    const word = await response.json();

    document.getElementById('word-name').textContent = word.title;
    document.getElementById('word-phonetic').textContent = word.phonetic;
    document.getElementById('word-meaning').textContent = word.meaning;

    currentWordName=word.title;
    currentWordPronunciation=word.pronunciation;

    const audioElement = document.getElementById("word-audio");
    audioElement.src = word.pronunciation;
    audioElement.load();
}

fetchWordDetail();

// Kullanıcı ID'sini almak için fonksiyon
async function getUserId() {
    try {
        const response = await fetch("http://localhost:8080/user/current-user");
        if (!response.ok) {
            throw new Error("Kullanıcı giriş yapmamış.");
        }
        const userData = await response.json();
        return userData.id; // Kullanıcının ID'sini döndür
    } catch (error) {
        console.error("Kullanıcı ID alınamadı:", error);
        return null; // Hata durumunda null dön
    }
}

document.addEventListener("DOMContentLoaded", function () {

    // ses kaydını başlat
    const recordButton = document.getElementById("record");
    const stopButton = document.getElementById("stop");
    const userAudio = document.getElementById("user-audio");
    const tryAgainButton = document.getElementById("try-again");
    const compareButton = document.getElementById("compare");

    let mediaRecorder;
    let audioChunks = [];

    // mikrofon erişimi
    navigator.mediaDevices.getUserMedia({ audio: true })
        .then(stream => {
            mediaRecorder = new MediaRecorder(stream);
            mediaRecorder.ondataavailable = event => {
                audioChunks.push(event.data);
            };

            mediaRecorder.onstop = () => {
                const audioBlob = new Blob(audioChunks, { type: "audio/wav" });
                const audioUrl = URL.createObjectURL(audioBlob);
                userAudio.src = audioUrl;
            };

            // kayıt başlat
            recordButton.onclick = () => {
                mediaRecorder.start();
                stopButton.disabled = false;
                recordButton.disabled = true;
            };

            // durdur
            stopButton.onclick = async () => {
                mediaRecorder.stop();
                recordButton.disabled = false;
                stopButton.disabled = true;

                mediaRecorder.onstop = async () => {
                    const audioBlob = new Blob(audioChunks, { type: "audio/wav" });

                    const userId = await getUserId();  // Kullanıcı ID'yi API'den al
                    if (!userId) {
                        alert("Kullanıcı kimliği alınamadı. Lütfen giriş yapın.");
                        return;
                    }


                    const formData = new FormData();
                    formData.append("audioFile", audioBlob, `${userId}_${currentWordName}.wav`);
                    formData.append("userId", userId);
                    formData.append("wordName", currentWordName);

                    fetch("http://localhost:8080/audio/upload", {
                        method: "POST",
                        body: formData
                    })
                        .then(response => response.text())
                        .then(data => console.log("Ses kaydedildi:", data))
                        .catch(error => console.error("Hata oluştu:", error));

                    const audioUrl = URL.createObjectURL(audioBlob);
                    userAudio.src = audioUrl;
                };
            };


        })
        .catch(error => {
            alert("Mikrofon erişimi sağlanamadı.");
        });

    // try again'e tıklanıldığında kaydı sıfırla
    tryAgainButton.onclick = () => {
        userAudio.src = "";
        audioChunks = [];
        recordButton.disabled = false;
        stopButton.disabled = true;
    };

    // compare'e tıklanıldığında accuracy göster
    compareButton.onclick = async () => {
        if (!userAudio.src || userAudio.src === "") {
            alert("Lütfen önce bir kayıt yapın.");
            return;
        }

        try {


            // Kullanıcı ses kaydını blob olarak al
            const response = await fetch(userAudio.src);
            const userAudioBlob = await response.blob();

            // Orijinal ses dosyasını blob olarak al
            const pronunciationResponse = await fetch(currentWordPronunciation);
            const pronunciationBlob = await pronunciationResponse.blob();

            console.log("User Audio Blob:", userAudioBlob);
            console.log("Pronunciation Audio Blob:", pronunciationBlob);

            // FormData oluştur
            const formData = new FormData();
            formData.append("file1", pronunciationBlob); // API'den gelen orijinal telaffuz dosyası
            formData.append("file2", userAudioBlob);
            console.log(formData);
            // Flask API'ye isteği yap
            const flaskResponse = await fetch("http://localhost:8080/voice/compare", {
                method: "POST",
                body: formData
            });

            const result = await flaskResponse.text();

            // Sonucu ekrana yazdır
            document.querySelector("h2").textContent = `Accuracy: ${result}%`;
        } catch (error) {
            console.error("Ses karşılaştırma hatası:", error);
            alert("Ses karşılaştırma sırasında bir hata oluştu.");
        }
    };

});