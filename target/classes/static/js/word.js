document.addEventListener("DOMContentLoaded", function () {
    // URLden id  al
    const urlParams = new URLSearchParams(window.location.search);
    const wordId = urlParams.get("id");

    if (wordId) {
        // APIden kelime detaylarını alınıyor
        fetch(`http://localhost:8080/word/${wordId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(word => {
                // kelime başlığını, fonetiğini ve anlamını güncelle
                document.querySelector(".card h2").textContent = word.title;
                document.querySelector(".details p:nth-child(1) span").textContent = word.phonetic;
                document.querySelector(".details p:nth-child(2) span").textContent = word.meaning;

                // ses dosyasını güncelle
                const audioPlayer = document.querySelector(".audio-player source");
                audioPlayer.src = word.pronunciation;
                document.querySelector(".audio-player").load();
            })
            .catch(error => {
                alert("Kelime bilgileri yüklenirken bir hata oluştu.");
            });
    } else {
        alert("Kelime ID'si URL parametrelerinde bulunamadı.");
    }

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
            stopButton.onclick = () => {
                mediaRecorder.stop();
                recordButton.disabled = false;
                stopButton.disabled = true;
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
    compareButton.onclick = () => {
        const accuracy = Math.floor(Math.random() * 100); // simülasyon
        document.querySelector("h2").textContent = `Accuracy: ${accuracy}%`;
    };
});
