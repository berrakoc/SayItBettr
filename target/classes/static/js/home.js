function handleButtonClick(button) {
    const buttons = document.querySelectorAll('.buttons-container button');
    buttons.forEach(btn => btn.classList.remove('active'));
    button.classList.add('active');
}


function handleButtonClick(level) {
    // Hide all content divs
    const contents = document.querySelectorAll('.content');
    contents.forEach(content => content.style.display = 'none');

    // Show the selected level's content
    const selectedContents = document.querySelectorAll(`.content.${level}`);
    selectedContents.forEach(content => content.style.display = 'block');
}