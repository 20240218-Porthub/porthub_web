$(document).ready(function () {
    const loggedInUsername = /*[[${username}]]*/ 'defaultUsername';
    console.log("Logged-in User: " + loggedInUsername);

    $("#button-send").on("click", (e) => {
        send();
    });

    const websocket = new WebSocket("ws://localhost:8080/ws/chat");

    websocket.onmessage = onMessage;
    websocket.onopen = onOpen;
    websocket.onclose = onClose;

    function send() {
        let msg = document.getElementById("msg").value;
        console.log(loggedInUsername + ": " + msg);
        websocket.send(loggedInUsername + ": " + msg);
        document.getElementById("msg").value = '';
    }

    function onMessage(msg) {
        var data = msg.data;
        var arr = data.split(":");
        var sessionId = arr[0];
        var message = arr.slice(1).join(":");

        var messageDiv = "<div class='message-item'><div class='message-content'>" + message + "</div></div>";
        if (sessionId === loggedInUsername) {
            messageDiv = "<div class='message-item sent'>" + messageDiv + "</div>";
        } else {
            messageDiv = "<div class='message-item received'>" + messageDiv + "</div>";
        }
        $("#msgArea").append(messageDiv);
    }

    function onOpen(evt) {
        console.log("Connection opened");
    }

    function onClose(evt) {
        console.log("Connection closed");
    }

    // Search functionality
    $("#searchInput").on("input", function () {
        var searchQuery = $(this).val().toLowerCase();
        $(".message-list .message-item").each(function () {
            var messageText = $(this).find(".message-content").text().toLowerCase();
            if (messageText.includes(searchQuery)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    });
});