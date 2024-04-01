function addNewSection() {
    var writeContainer = document.querySelector('.port-write');

    // Create new section container
    var newSection = document.createElement('div');
    newSection.classList.add('section', 'section-forth');

    // Create new write container
    var newWriteContainer = document.createElement('div');
    newWriteContainer.classList.add('write-container');

    // Create new left section
    var newLeftSection = document.createElement('div');
    newLeftSection.classList.add('fraged', 'left-section');
    newLeftSection.innerHTML = `
        <label for="content">내용:</label>
        <textarea class="content" name="content" required></textarea>
    `;

    // Create new right section
    var newRightSection = document.createElement('div');
    newRightSection.classList.add('fraged', 'right-section');
    newRightSection.innerHTML = `
        <label for="file">파일 선택:</label>
        <input type="file" id="file" class="input-file" name="file" required accept="image/*" onchange="previewImage(event)">
        <img class="select-file" id="preview" src="#" alt="미리보기 이미지">
    `;

    // Create delete button wrapper
    var deleteButtonWrapper = document.createElement('div');
    deleteButtonWrapper.classList.add('delete-button-wrapper');
    // Create delete button
    var deleteButton = document.createElement('button');
    deleteButton.textContent = '삭제';
    deleteButton.classList.add('delete-button'); // Add class
    deleteButton.classList.add('w-btn');
    deleteButton.addEventListener('click', function() {
        writeContainer.removeChild(newSection);
    });
    // Append delete button to delete button wrapper
    deleteButtonWrapper.appendChild(deleteButton);

    // Append delete button wrapper to newSection
    newSection.appendChild(deleteButtonWrapper);

    // Append new sections to newWriteContainer
    newWriteContainer.appendChild(newLeftSection);
    newWriteContainer.appendChild(newRightSection);

    // Append new write container to newSection container
    newSection.appendChild(newWriteContainer);

    // Find the reference element
    var referenceElement = document.querySelector('.second-fifth');

    // Insert the new section before the reference element
    writeContainer.insertBefore(newSection, referenceElement);
}


function previewImage(event) {
    var input = event.target;
    var preview = input.nextElementSibling;

    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function(e) {
            preview.src = e.target.result;
            preview.style.display = 'block';
        };

        reader.readAsDataURL(input.files[0]);
    } else {
        preview.src = '#';
        preview.style.display = 'none';
    }
}
function previewFile(event) {
    var input = event.target;
    var preview = document.getElementById('prevthumb');

    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function(e) {
            if (input.files[0].type.startsWith('image/')) {
                preview.innerHTML = '<img src="' + e.target.result + '" alt="미리보기 이미지" style="\n' +
                    '    display: block;\n' +
                    '    max-height: 80%;\n' +
                    '    position: relative;\n' +
                    '    max-width: 100%;" >';
            } else if (input.files[0].type.startsWith('video/')) {
                preview.innerHTML = '<video src="' + e.target.result + '" controls style="\n' +
                    '    display: block;\n' +
                    '    max-height: 80%;\n' +
                    '    position: relative;\n' +
                    '    max-width: 100%;"></video>';
            } else if (input.files[0].type.startsWith('audio/')) {
                preview.innerHTML = '<audio src="' + e.target.result + '" controls style="\n' +
                    '    display: block;\n' +
                    '    max-height: 80%;\n' +
                    '    position: relative;\n' +
                    '    max-width: 100%;"></audio>';
            }

            preview.style.display = 'block';
        };

        reader.readAsDataURL(input.files[0]);
    } else {
        preview.style.display = 'none';
    }
}