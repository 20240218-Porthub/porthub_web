/*<![CDATA[*/
$(document).ready(function () {
    var loggedInUsername;

    $.ajax({
        url: '/api/user',
        method: 'GET',
        success: function (response) {
            loggedInUsername = response.username;
            console.log(`Logged-in User: ${loggedInUsername}`);
        },
        error: function () {
            console.error('Failed to fetch logged-in user. Please try again.');
        }
    });

    $('.startChatButton').on('click', function() {
        var followingUserId = $(this).data('user-id');
        var content = $('#msg').val();
        if (content.trim() === '') {
            alert('Please type a message to start the chat.');
            return;
        }
        startNewChat(followingUserId, content);
        $('#msg').val('');
    });

    $('.chat-link').on('click', function (event) {
        event.preventDefault();
        var sessionId = $(this).attr('href').split('/').pop();
        loadChatMessages(sessionId);
    });

    const API_ENDPOINTS = {
        FOLLOWINGS: '/api/followings',
        NEW_CHAT: '/chats/new'
    };

    const $sendButton = $('#button-send');
    const $addChatButton = $('#addChatButton');
    const $followingsList = $('#followersList');
    const $searchInput = $('#searchInput');
    const $msgInput = $('#msg');
    const $msgArea = $('#msgArea');

    $sendButton.on('click', sendMessage);
    $addChatButton.on('click', fetchFollowings);
    $msgInput.on('keypress', handleEnterKey);
    $searchInput.on('input', searchMessages);

    function createMessageElement(message) {
        var messageElement = document.createElement('div');
        messageElement.className = 'message-item';

        var senderElement = document.createElement('span');
        senderElement.className = 'message-sender';
        senderElement.textContent = message.senderUserId + ': ';

        var contentElement = document.createElement('span');
        contentElement.className = 'message-content';
        contentElement.textContent = message.content;

        var timestampElement = document.createElement('span');
        timestampElement.className = 'message-timestamp';
        timestampElement.textContent = formatTimestamp(message.timestamp);

        messageElement.appendChild(senderElement);
        messageElement.appendChild(contentElement);
        messageElement.appendChild(timestampElement);

        return messageElement;
    }

    var stompClient = null;

    function connect() {
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/public', function (message) {
                var chatMessage = JSON.parse(message.body);
                var messageElement = createMessageElement(chatMessage);
                $msgArea.append(messageElement);
            });
        });
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
        // Clear the current chat messages
        $msgArea.empty();

        // Fetch chat messages from the server
        $.ajax({
            url: '/user/chat/' + sessionId,
            method: 'GET',
            dataType: 'json',  // Ensure the response is treated as JSON
            success: function (response) {
                // Log the response to debug
                console.log('Chat messages response:', response);

                // Check if the response is an array
                if (Array.isArray(response)) {
                    // Display the retrieved chat messages
                    response.forEach(function (message) {
                        var messageElement = createMessageElement(message);
                        $msgArea.append(messageElement);
                    });
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
            success: function(response) {
                console.log('New chat started successfully:', response);
            },
            error: function(xhr) {
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

            stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({
                'senderUserId': senderId,
                'recipientUserId': recipientId,
                'content': messageInput.val(),
                'timestamp': new Date().toISOString(),
                'sessionId': sessionId
            }));
            messageInput.val('');

            // Append the sent message to the chat area
            var sentMessage = {
                senderUserId: senderId,
                content: messageInput.val(),
                timestamp: new Date().toISOString()
            };
            var messageElement = createMessageElement(sentMessage);
            $msgArea.append(messageElement);
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

