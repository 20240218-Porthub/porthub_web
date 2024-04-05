
function previewFile(event) {
    var input = event.target;
    var preview = input.nextElementSibling;

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
document.querySelectorAll('.content-input').forEach(function(textarea) {
    textarea.addEventListener('input', function() {
        var markdown = this.value;
        var html = marked(markdown);
        this.parentElement.querySelector('.markdown').innerHTML = html;
    });
});

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
        <div class="markdown-container">
            <button class="markdown-btn" data-action="# " title="H1">#</button>
            <button class="markdown-btn" data-action="## " title="H2">##</button>
            <button class="markdown-btn" data-action="### " title="H3">###</button>
            <button class="markdown-btn" data-action="#### " title="H4">####</button>
            <button class="markdown-btn" data-action="**" title="Bold">Bold</button>
            <button class="markdown-btn" data-action="_" title="Italic">Italic</button>
            <button class="markdown-btn" data-action="~~" title="Strikethrough">Strikethrough</button>
            <button class="markdown-btn" data-action="> " title="Blockquote">Quote</button>
            <button class="markdown-btn" data-action="[링크텍스트](링크 등록)" title="Link">Link</button>
            <button class="markdown-btn" data-action="\`\`\`\n\n\`\`\`" title="Code Block">Code Block</button>
        </div>
        <div class="left-section-container">
            <textarea class="content-input" id="content" name="content" required></textarea>
            <div class="markdown-text-container">
                <div class="markdown"></div>
            </div>
        </div>
    `;

    // Create new right section
    var newRightSection = document.createElement('div');
    newRightSection.classList.add('fraged', 'right-section');
    newRightSection.innerHTML = `
        <label for="file">파일 선택:</label>
        <input type="file" class="input-file" name="file" required accept="image/*, video/*, audio/*" onchange="previewFile(event)">
        <div class="select-file" style="display: none;"></div>
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
    newWriteContainer.appendChild(newRightSection);
    newWriteContainer.appendChild(newLeftSection);

    // Append new write container to newSection container
    newSection.appendChild(newWriteContainer);

    // Find the reference element
    var referenceElement = document.querySelector('.section-link');

    // Insert the new section before the reference element
    writeContainer.insertBefore(newSection, referenceElement);
    newLeftSection.querySelector('.content-input').addEventListener('input', function() {
        var markdown = this.value;
        var html = marked(markdown);
        this.parentElement.querySelector('.markdown').innerHTML = html;
    });

    newLeftSection.querySelector('.content-input').addEventListener('mouseenter', function() {
        this.parentElement.nextElementSibling.style.display = 'flex';
    });

    // Bind click event to markdown buttons in the new section
    newLeftSection.querySelectorAll('.markdown-btn').forEach(function(btn) {
        btn.addEventListener('click', function() {
            var textarea = btn.closest('.left-section').querySelector('.content-input');
            var action = btn.getAttribute('data-action');
            insertMarkdown(action, textarea);
        });
    });
}

function insertMarkdown(text, textarea) {
    var start = textarea.selectionStart;
    var end = textarea.selectionEnd;
    var selectedText = textarea.value.substring(start, end);
    var replacement = text + selectedText + text;
    if (start === end) {
        replacement = text;
    }
    textarea.value = textarea.value.substring(0, start) + replacement + textarea.value.substring(end);
    textarea.focus();
    textarea.setSelectionRange(start + replacement.length, start + replacement.length);

    // Update markdown preview
    var markdown = textarea.value;
    var html = marked(markdown);
    textarea.parentElement.nextElementSibling.querySelector('.markdown').innerHTML = html;
}

