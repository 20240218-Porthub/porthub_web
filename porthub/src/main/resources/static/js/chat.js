$(document).ready(function () {
    const API_ENDPOINTS = {
        FOLLOWINGS: '/api/followings',
        NEW_CHAT: '/chats/new',
        USER_DETAILS: '/api/user-details'
    };

    const $sendButton = $('#button-send');
    const $addChatButton = $('#addChatButton');
    const $followingsList = $('#followersList');
    const $searchInput = $('#searchInput');
    const $msgInput = $('#msg');
    const $msgArea = $('#msgArea');
    const $chatArea = $('#chatArea');

    var sessionId = window.location.pathname.split('/').pop();
    var stompClient = null;

    if (sessionId && sessionId !== "chat") {
        loadChatMessages(sessionId);
    }

    $('#send_message_btn').on('click', function () {
        var followingUserId = $(this).data('user-id');
        var content = $('#message_input').val().trim();
        if (content === '') {
            alert('Please type a message to start the chat.');
            return;
        }
        startNewChat(followingUserId, content);
        $('#message_input').val('');
    });

    $sendButton.on('click', sendMessage);
    $addChatButton.on('click', fetchFollowings);
    $msgInput.on('keypress', handleEnterKey);
    $searchInput.on('input', searchMessages);

    function createMessageElement(message) {
        var messageElement = document.createElement('div');
        messageElement.className = 'message-item';

        if (message.senderUserId == loggedInUserId) {
            messageElement.classList.add('sent');
        } else {
            messageElement.classList.add('received');
        }

        var contentElement = document.createElement('div');
        contentElement.className = 'message-content';
        var textElement = document.createElement('span');
        textElement.textContent = message.content;
        var timestampElement = document.createElement('span');
        timestampElement.className = 'message-timestamp';
        timestampElement.textContent = formatTimestamp(message.timestamp);

        contentElement.appendChild(textElement);
        contentElement.appendChild(timestampElement);
        messageElement.appendChild(contentElement);

        return messageElement;
    }

    function connect() {
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/public', function (message) {
                var chatMessage = JSON.parse(message.body);
                var messageElement = createMessageElement(chatMessage);
                $msgArea.append(messageElement);
                scrollToBottom();
            });
        });
    }

    function scrollToBottom() {
        $msgArea[0].scrollTop = $msgArea[0].scrollHeight;
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
                .data('user-id', following.id)
                .on('click', function() {
                    var recipientUserId = $(this).data('user-id');
                    startNewChat(recipientUserId);
                });
            followingListItem.append(startChatButton);
            $followingsList.append(followingListItem);
        });
    }

    function loadChatMessages(sessionId) {
        $msgArea.empty();
        $.ajax({
            url: '/api/chat-messages/' + sessionId,
            method: 'GET',
            dataType: 'json',
            success: function (response) {
                console.log('Chat messages response:', response);
                if (Array.isArray(response)) {
                    response.forEach(function (message) {
                        var messageElement = createMessageElement(message);
                        $msgArea.append(messageElement);
                    });
                    scrollToBottom(); // 메시지를 로드한 후 스크롤을 맨 아래로 이동
                } else {
                    console.error('Unexpected response format:', response);
                }
            },
            error: function (xhr, status, error) {
                console.error('Failed to load chat messages. Status:', status, 'Error:', error);
                console.log('Response Text:', xhr.responseText);
            }
        });
    }

    function startNewChat(followingUserId, content) {
        console.log('Starting new chat with user ID:', followingUserId, 'and content:', content);
        $.ajax({
            url: '/chats/new',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ recipientUserId: followingUserId, content: content }),
            success: function (sessionId) {
                console.log('New chat started successfully. Session ID:', sessionId);
                window.location.href = '/user/chat/' + sessionId;
            },
            error: function (xhr) {
                console.error('Failed to start a new chat. Please try again.', xhr.responseText);
            }
        });
    }

    function sendMessage() {
        if (stompClient && stompClient.connected) {
            var messageInput = $('#msg');
            var senderId = $('#senderId').val();
            var recipientId = $('#recipientId').val();
            var sessionId = $('#sessionId').val();
            var content = messageInput.val().trim();

            if (content === '') {
                alert('Please type a message.');
                return;
            }

            if (messageInput) {
                stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({
                    'senderUserId': senderId,
                    'recipientUserId': recipientId,
                    'content': content,
                    'timestamp': new Date().toISOString(),
                    'sessionId': sessionId
                }));

                var sentMessage = {
                    senderUserId: senderId,
                    content: content,
                    timestamp: new Date().toISOString()
                };
                var messageElement = createMessageElement(sentMessage);
                console.log('Message element:', sentMessage);
                if (recipientId == -2){
                    $msgArea.append(messageElement);  // 메시지를 즉시 표시
                }
                $msgArea.scrollTop($msgArea[0].scrollHeight);
                messageInput.val('');
            }
        } else {
            console.error('WebSocket connection is not active. Cannot send message.');
        }
    }

    function handleEnterKey(event) {
        if (event.which === 13) {
            event.preventDefault();
            if (stompClient && stompClient.connected) {
                sendMessage();
            } else {
                console.error('WebSocket connection is not active. Cannot send message.');
            }
        }
    }

    function formatTimestamp(timestamp) {
        var date = new Date(timestamp);
        var hours = date.getHours();
        var minutes = date.getMinutes();
        var ampm = hours >= 12 ? 'PM' : 'AM';
        hours = hours % 12;
        hours = hours ? hours : 12;
        minutes = minutes < 10 ? '0' + minutes : minutes;
        var strTime = hours + ':' + minutes + ' ' + ampm;
        return date.getMonth() + 1 + "/" + date.getDate() + "/" + date.getFullYear() + " " + strTime;
    }

    function searchMessages() {
        const searchQuery = $searchInput.val().toLowerCase();
        $('.message-item').each(function () {
            const messageText = $(this).find('.message-content').text().toLowerCase();
            $(this).toggle(messageText.includes(searchQuery));
        });
    }

    connect();
});