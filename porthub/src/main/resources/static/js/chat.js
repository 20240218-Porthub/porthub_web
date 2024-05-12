$(document).ready(function () {
    var loggedInUsername;

    $.ajax({
        url: '/api/user',
        method: 'GET',
        success: function (response) {
            loggedInUsername = response.username;  // Assign value here
            console.log(`Logged-in User: ${loggedInUsername}`);
        },
        error: function () {
            console.error('Failed to fetch logged-in user. Please try again.');
        }
    });

    $('.startChatButton').on('click', function() {
        var followingUserId = $(this).data('user-id');
        var content = $('#msg').val(); // Fetch the message content from the input
        if (content.trim() === '') {
            alert('Please type a message to start the chat.');
            return; // Prevent sending an empty message
        }
        // ㅇㅣ ㅂㅜㅂㅜㄴㅇㅔ ㅅㅓㅂㅓㅇㅔ ㅇㅛㅊㅓㅇ ㅂㅗㄴㅐㄱㅗ, ㅇㅣㅁㅣ ㅊㅐㅌㅣㅇ ㅇㅣㅆㄴㅡㄴㅈㅣ ㅎㅗㅏㄱㅇㅣㄴ ㄹㅗㅈㅣㄱ ㅍㅣㄹㅇㅛ
        startNewChat(followingUserId, content);
        $('#msg').val(''); // Clear the input field after sending
    });

    const API_ENDPOINTS = {
        FOLLOWINGS: '/api/followings',
        NEW_CHAT: '/chats/new'
    };

    const $sendButton = $('#button-send');
    const $addChatButton = $('#addChatButton');
    const $followingsList = $('#followingsList');
    const $searchInput = $('#searchInput');
    const $msgInput = $('#msg');
    const $msgArea = $('#msgArea');

    $sendButton.on('click', sendMessage);
    $addChatButton.on('click', fetchFollowings);
    $msgInput.on('keypress', handleEnterKey);
    $searchInput.on('input', searchMessages);

    const websocket = initializeWebSocket();

    function initializeWebSocket() {
        const ws = new WebSocket('ws://localhost:8080/ws/chat');
        ws.onmessage = handleMessage;
        ws.onopen = handleOpen;
        ws.onclose = handleClose;
        ws.onerror = handleError;
        return ws;
    }

    function fetchFollowings() {
        console.log('Fetching followings...');
        $.ajax({
            url: API_ENDPOINTS.FOLLOWINGS,
            method: 'GET',
            success: function (response) {
                renderFollowings(response);
            },
            error: function () {
                console.error('Failed to fetch followings. Please try again.');
            }
        });
    }

    function renderFollowings(followings) {
        console.log('Rendering followings:', followings);
        $followingsList.empty();
        followings.forEach(function (following) {
            const followingListItem = $('<li>').text(following.username);
            const startChatButton = $('<button>')
                .text('Start Chat')
                .data('user-id', following.id)  // Ensure data-user-id is set properly
                .on('click', function() {  // Directly attach the event handler
                    var recipientUserId = $(this).data('user-id');
                    startNewChat(recipientUserId);
                });
            followingListItem.append(startChatButton);
            $followingsList.append(followingListItem);
        });
    }


    function startNewChat(followingUserId, content) {
        console.log('Starting new chat with user ID:', followingUserId, 'and content:', content);
        $.ajax({
            url: '/chats/new',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ recipientUserId: followingUserId, content: content }),
            success: function(response) {
                console.log('New chat started successfully:', response);
            },
            error: function(xhr) {
                console.error('Failed to start a new chat. Please try again.', xhr.responseText);
            }
        });
    }

    function sendMessage() {
        const message = $msgInput.val().trim();
        if (message !== '') {
            console.log(`${loggedInUsername}: ${message}`);
            websocket.send(`${loggedInUsername}: ${message}`);
            $msgInput.val('');

        }
    }

    function handleEnterKey(event) {
        if (event.which === 13) {
            event.preventDefault();
            sendMessage();
        }
    }

    function handleMessage(event) {
        const data = event.data;
        const [sessionId, ...messageParts] = data.split(':');
        const message = messageParts.join(':');
        const sanitizedMessage = escapeHtml(message);
        const messageClass = sessionId === loggedInUsername ? 'sent' : 'received';
        const messageHtml = `<div class="message-item ${messageClass}">
                                 <div class="message-content">${sanitizedMessage}</div>
                             </div>`;
        $msgArea.append(messageHtml);
    }

    function escapeHtml(text) {
        const escape = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#039;'
        };
        return text.replace(/[&<>"']/g, function (match) {
            return escape[match];
        });
    }

    function handleOpen() {
        console.log('WebSocket connection opened');
    }

    function handleClose() {
        console.log('WebSocket connection closed');
        // Implement reconnection logic here
    }

    function handleError(error) {
        console.error('WebSocket error:', error);
        // Implement error handling and user feedback here
    }

    function searchMessages() {
        const searchQuery = $searchInput.val().toLowerCase();
        $('.message-list .message-item').each(function () {
            const messageText = $(this).find('.message-content').text().toLowerCase();
            $(this).toggle(messageText.includes(searchQuery));
        });
    }
});