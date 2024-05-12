$(document).ready(function () {
    const loggedInUsername = /*[[${username}]]*/ 'defaultUsername';
    console.log(`Logged-in User: ${loggedInUsername}`);

    const API_ENDPOINTS = {
        FOLLOWERS: '/api/followers',
        NEW_CHAT: '/api/chats/new'
    };

    const $sendButton = $('#button-send');
    const $addChatButton = $('#addChatButton');
    const $followersList = $('#followersList');
    const $searchInput = $('#searchInput');
    const $msgInput = $('#msg');
    const $msgArea = $('#msgArea');

    $sendButton.on('click', sendMessage);
    $addChatButton.on('click', fetchFollowers);
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

    function fetchFollowers() {
        $.ajax({
            url: API_ENDPOINTS.FOLLOWERS,
            method: 'GET',
            success: function (response) {
                renderFollowers(response);
            },
            error: function () {
                console.error('Failed to fetch followers. Please try again.');
            }
        });
    }

    function renderFollowers(followers) {
        $followersList.empty();
        followers.forEach(function (follower) {
            const followerListItem = $('<li>').text(follower.username);
            const startChatButton = $('<button>')
                .text('Start Chat')
                .on('click', function () {
                    startNewChat(follower.username);
                });
            followerListItem.append(startChatButton);
            $followersList.append(followerListItem);
        });
    }

    function startNewChat(followerUsername) {
        $.ajax({
            url: API_ENDPOINTS.NEW_CHAT,
            method: 'POST',
            data: {
                followerUsername: followerUsername
            },
            success: function (response) {
                console.log(`New chat started with ${followerUsername}`);
            },
            error: function () {
                console.error(`Failed to start a new chat with ${followerUsername}. Please try again.`);
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