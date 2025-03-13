let currentWordName=""
let currentWordPronunciation=""
let currentWordId=""
let userWordPronunciation=""
let userId=""

async function fetchWordDetail() {
    const params = new URLSearchParams(window.location.search);
    const wordId = params.get('id');
    currentWordId=wordId;
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
        userId=userData.id;
        return userData.id;
    } catch (error) {
        console.error("Kullanıcı ID alınamadı:", error);
        return null;
    }
}


async function evaluateAndSave(result) {
    const cleanedResult = result.replace(/[^\d.-]/g, '');
    const wordAccuracy = parseFloat(cleanedResult);
    const wordId = currentWordId;
    const pronunciation = `uploads/${userWordPronunciation}`;

    let coefficient;

    if (wordId < 13) {
        coefficient = 0.3;
    } else if (wordId < 25) {
        coefficient = 0.5;
    } else if (wordId < 37) {
        coefficient = 0.7;
    } else {
        coefficient = 0.9;
    }

    const wordScore = Math.round(wordAccuracy * coefficient);

    const userData = {
        userId: userId,  // Tanımlı olduğundan emin ol
        wordId: wordId,
        pronunciation: pronunciation,
        accuracy: wordAccuracy,
        score: wordScore,
        date: new Date().toISOString() // ISO 8601 formatında tarih
    };

    fetch("http://localhost:8080/evaluation/save", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(userData)
    })
        .then(response => response.json())
        .then(data => {
            console.log("Evaluation saved:", data);
        })
        .catch(error => {
            console.error("Error saving evaluation:", error);
        });
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
                console.log(audioUrl);
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
                    console.log(audioBlob);
                    const userId = await getUserId();
                    if (!userId) {
                        alert("Kullanıcı kimliği alınamadı. Lütfen giriş yapın.");
                        return;
                    }

                    userWordPronunciation=`${userId}_${currentWordName}.wav`
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
            document.getElementById("result").textContent = result.replace('%', '');
            return evaluateAndSave(result.replace('%', ''));
        } catch (error) {
            console.error("Ses karşılaştırma hatası:", error);
            alert("Ses karşılaştırma sırasında bir hata oluştu.");
        }
    };

});
