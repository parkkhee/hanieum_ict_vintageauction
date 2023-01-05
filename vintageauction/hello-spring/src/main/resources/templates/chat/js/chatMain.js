/*
TODO:
 1)클라이언트 단에서 이 스크립트를 호출
 2)호출하는 쪽에서 아래의 데이터/이벤트 가져오기
 -> 가져올 데이터: 로그인 유저 id, 수신할 유저 id, 채팅방 id
 */

var enterForm = document.querySelector('#enterForm');  //채팅 입장(웹소켓 연결) 버튼
var messageForm = document.querySelector('#messageForm');  //채팅 보내기 버튼
var messageInput = document.querySelector('#message');  //채팅 내용 입력 공간
var messageArea = document.querySelector('#messageArea');  //채팅 표시할 공간
var currentUser = document.getElementsByTagName("span")[0].innerHTML;  //현재 유저
var receiver = document.getElementsByTagName("span")[1].innerHTML;  //채팅 받을 유저
var chatroomId = document.getElementsByTagName("span")[2].innerHTML;  //채팅방Id

var stompClient = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];



function connect(event) {
    event.preventDefault();

    console.log("connected");
    console.log("보내는 이(현재 유저):", currentUser);
    console.log("받는 이:", receiver);
    console.log("현재 채팅방:", chatroomId);

    if(currentUser) {

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected);
    }
}


function onConnected() {
    // user 개인 구독
    stompClient.subscribe('/room/' + chatroomId + '/queue/messages', onMessageReceived);


    //connectingElement.classList.add('hidden');
}



function sendMessage(event) {
    event.preventDefault();
    var messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        var chatMessage = {
            sender: currentUser,
            receiver: receiver,
            content: messageInput.value,
            chatroom: chatroomId,
            type: 'CHAT'
        };

        stompClient.send('/room/'+chatroomId+'/queue/messages', {}, JSON.stringify(chatMessage)); //json 직렬화해서 보내기
        stompClient.send('/app/chat', {}, JSON.stringify({'content': messageInput.value,'senderId':currentUser,
            'receiverId': receiver, 'chatroomId': chatroomId}));
        messageInput.value = '';
    }


}


function onMessageReceived(payload) {
    //구독한 destination으로 수신한 메시지 파싱
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');


    messageElement.classList.add('chat-message');

    //뷰에 유저 아이콘 표시하기
    var avatarElement = document.createElement('i');
    var avatarText = document.createTextNode(message.sender[0]);
    avatarElement.appendChild(avatarText);
    avatarElement.style['background-color'] = getAvatarColor(message.sender);

    messageElement.appendChild(avatarElement);

    var currentUserElement = document.createElement('span');
    var currentUserText = document.createTextNode(message.sender);
    currentUserElement.appendChild(currentUserText);
    messageElement.appendChild(currentUserElement);

    //뷰에 채팅 내용 표시하기
    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

enterForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);