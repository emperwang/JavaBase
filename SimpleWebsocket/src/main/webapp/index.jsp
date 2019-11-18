<html>
<body>
<h2>Hello World!</h2>

<input id="text" type="text">
<button onclick="send()">sendMsg</button>
<br/>
<button onclick="closeWebSocket()">close</button>
<br>
<div id="msg"></div>
</body>

<script type="text/javascript">
    var webscoket=null;
    if ("WebSocket" in window){
        webscoket = new WebSocket("ws://localhost:8080/simplews")
    }else{
        alert("doesn't support websocket")
    }

    webscoket.onerror=function (ev) {
        console.log("onerror");
        setMessageInnerHTML("web socket connect error");
    }
    webscoket.onclose=function (ev) {
        console.log("onclose");
        setMessageInnerHTML("web socket onclose");
        webscoket.close();
    }

    webscoket.onopen=function (ev) {
        console.log("onopen");
        setMessageInnerHTML("web socket onopen");
    }

    webscoket.onmessage = function (ev) {
        console.log("onmessage");
        console.log(ev)
        setMessageInnerHTML(ev.data);
    }

    window.onbeforeunload = function (ev) {
        closeWebSocket();
    }

    function send() {
        var message=document.getElementById("text").value;
        webscoket.send(message);
    }
    
    
    function closeWebSocket() {
        webscoket.close();
    }
    
    function setMessageInnerHTML(innerHtml) {
        document.getElementById("msg").innerHTML += innerHtml+ "<br/>";
    }
</script>
</html>
