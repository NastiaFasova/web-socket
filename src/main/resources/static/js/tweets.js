window.addEventListener('load', function(){
    let form = document.querySelector('#tweet-form');
    form.addEventListener('submit', function(e){
        e.preventDefault();

        if (form.content && form.content.value != '')
        {
            fetch(`${location.origin}/api/posts/create`, {
                method: "PUT",
                body: new FormData(form)
            })
            .then(response =>{
                if (response.ok)
                {
                    return response.text();
                }
            })
            .then(postId =>{
                form.reset();
                if (window.stompClient != null)
                {
                    window.stompClient.send("/app/tweets", {}, JSON.stringify({id : Number(postId)}));
                }
            });
        }
    });
});