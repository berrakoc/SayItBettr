// Fetch işlemi yapan yardımcı fonksiyon
async function fetchUserData() {
    const response = await fetch('/user/current-user', {
        method: 'GET',
        credentials: 'same-origin'
    });

    if (!response.ok) {
        throw new Error('Kullanıcı girişi yapılmamış');
    }

    return await response.json();
}

// Değerlendirme sayısını almak için API çağrısı
async function fetchEvaluationCount(userId) {
    try {
        const response = await fetch(`/evaluation/user/${userId}/count`, {
            method: 'GET',
            credentials: 'same-origin'
        });

        if (!response.ok) {
            throw new Error('Değerlendirme sayısı alınamadı');
        }

        // JSON verisini al
        const data = await response.json();

        console.log('API yanıtı: ', data); // Yanıtı konsola yazdır

        return data; // Sayfa için değer döndür
    } catch (error) {
        console.error('Hata: ', error);
        throw error; // Hata durumu
    }
}

// Değerlendirme sayısını almak için API çağrısı
async function fetchEvaluationCountByLevel(userId, level) {
    try {
        const response = await fetch(`evaluation/user/${userId}/level/${level}/count`, {
            method: 'GET',
            credentials: 'same-origin' // Çerezlerle birlikte gönderilsin
        });

        if (!response.ok) {
            throw new Error('Değerlendirme sayısı alınamadı');
        }

        // JSON verisini al
        const data = await response.json();

        console.log('API yanıtı: ', data); // Yanıtı konsola yazdır

        return data; // Sayfa için değer döndür
    } catch (error) {
        console.error('Hata: ', error);
        throw error; // Hata durumu
    }
}

async function fetchwordsCountByLevel(level) {
    try {
        const response = await fetch(`word/level/${level}/count`, {
            method: 'GET',
            credentials: 'same-origin' // Çerezlerle birlikte gönderilsin
        });

        if (!response.ok) {
            throw new Error('Değerlendirme sayısı alınamadı');
        }

        // JSON verisini al
        const data = await response.json();

        console.log('API yanıtı: ', data); // Yanıtı konsola yazdır

        return data; // Sayfa için değer döndür
    } catch (error) {
        console.error('Hata: ', error);
        throw error; // Hata durumu
    }
}

async function fetchUserRank(userId) {
    try {
        const response = await fetch(`/evaluation/user/${userId}/rank`, {
            method: 'GET',
            credentials: 'same-origin' // Çerezlerle birlikte gönderilsin
        });

        if (!response.ok) {
            throw new Error('Değerlendirme sayısı alınamadı');
        }

        // JSON verisini al
        const data = await response.json();

        console.log('API yanıtı: ', data); // Yanıtı konsola yazdır

        return data; // Sayfa için değer döndür
    } catch (error) {
        console.error('Hata: ', error);
        throw error; // Hata durumu
    }
}
async function fetchUserScore(userId) {
    try {
        const response = await fetch(`/evaluation/user/${userId}/score`, {
            method: 'GET',
            credentials: 'same-origin' // Çerezlerle birlikte gönderilsin
        });

        if (!response.ok) {
            throw new Error('Değerlendirme sayısı alınamadı');
        }

        // JSON verisini al
        const data = await response.json();

        console.log('API yanıtı: ', data); // Yanıtı konsola yazdır

        return data; // Sayfa için değer döndür
    } catch (error) {
        console.error('Hata: ', error);
        throw error; // Hata durumu
    }
}

async function fetchAndDisplayHistory(userId) {
    try {
        // API çağrısı yap
        const response = await fetch(`evaluation/user/${userId}/last5`);

        // Eğer API'den hata alırsak
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const evaluations = await response.json();

        // Yanıt formatını kontrol et
        if (!evaluations || !Array.isArray(evaluations)) {
            throw new Error("Invalid response data format");
        }

        const historyContainer = document.querySelector('.history-entries');
        if (!historyContainer) {
            console.error("History container not found!");
            return;
        }

        historyContainer.innerHTML = ''; // Önceki içeriği temizle

        evaluations.forEach(evaluation => {
            const wordTitle = evaluation.word?.title || "Unknown";
            const accuracy = evaluation.accuracy ?? "N/A";

            const historyItem = document.createElement('h5');
            historyItem.classList.add('history-item');
            historyItem.textContent = `${wordTitle} → ${accuracy}% Accuracy`;

            historyContainer.appendChild(historyItem);
        });
    } catch (error) {
        console.error('Error fetching history:', error);
    }
}



document.addEventListener("DOMContentLoaded", async function () {
    try {
        // Kullanıcı bilgilerini alalım
        const user = await fetchUserData();
        const userId = user.id;

        // Kullanıcı bilgilerini DOM'a ekleyelim
        const userNameElement = document.getElementById('user-name');
        const solvedWordsElement = document.getElementById('solved-words');

        userNameElement.textContent = user.name;

        const evaluationCount = await fetchEvaluationCount(userId);
        solvedWordsElement.textContent = evaluationCount;

        // Seviye başına değerlendirme sayısını al
        const evaluationCountByLevel1 = await fetchEvaluationCountByLevel(userId, 1);
        const evaluationCountByLevel2 = await fetchEvaluationCountByLevel(userId, 2);
        const evaluationCountByLevel3 = await fetchEvaluationCountByLevel(userId, 3);
        const evaluationCountByLevel4 = await fetchEvaluationCountByLevel(userId, 4);

        // Sayfada yazdırma işlemi
        document.getElementById('solved-word-level-1').textContent = evaluationCountByLevel1;
        document.getElementById('solved-word-level-2').textContent = evaluationCountByLevel2;
        document.getElementById('solved-word-level-3').textContent = evaluationCountByLevel3;
        document.getElementById('solved-word-level-4').textContent = evaluationCountByLevel4;

        const totalwordByLevel1 = await fetchwordsCountByLevel(1)
        const totalwordByLevel2 = await fetchwordsCountByLevel(2)
        const totalwordByLevel3 = await fetchwordsCountByLevel(3)
        const totalwordByLevel4 = await fetchwordsCountByLevel(4)

        // Sayfada yazdırma işlemi
        document.getElementById('total-word-level-1').textContent = totalwordByLevel1;
        document.getElementById('total-word-level-2').textContent = totalwordByLevel2;
        document.getElementById('total-word-level-3').textContent = totalwordByLevel3;
        document.getElementById('total-word-level-4').textContent = totalwordByLevel4;


        document.getElementById('user-rank').textContent = await fetchUserRank(userId);
        document.getElementById('user-score').textContent = await fetchUserScore(userId);

        await fetchAndDisplayHistory(userId);
    } catch (error) {
        console.error(error);
        document.getElementById('profile-info').textContent = 'Bir hata oluştu. Lütfen tekrar deneyin.';
    }
});

