function fetchWords(level) {
    fetch(`http://localhost:8080/word/level/${level}`)
        .then(response => response.json())
        .then(data => {
            const wordListDiv = document.getElementById("wordList");
            wordListDiv.innerHTML = "";

            data.forEach(word => {
                console.log("Fetched word:", word);

                const wordDiv = document.createElement("div");
                wordDiv.classList.add("content");

                if (level === 1) wordDiv.classList.add("easy");
                else if (level === 2) wordDiv.classList.add("medium");
                else if (level === 3) wordDiv.classList.add("hard");
                else if (level === 4) wordDiv.classList.add("extreme");

                wordDiv.textContent = word.title;

                wordDiv.addEventListener("click", function () {
                    if (!word.id) {
                        console.error("Kelimenin ID'si bulunamadı!");
                        return;
                    }
                    window.location.href = `http://localhost:8080/word-detail?id=${word.id}`;
                });

                wordListDiv.appendChild(wordDiv);
            });
        })
        .catch(error => console.error("Error fetching words:", error));
}
