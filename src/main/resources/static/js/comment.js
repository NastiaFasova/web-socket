window.addEventListener('load', function (){
    document.querySelector('.posts-container').addEventListener('keypress', function (e){
       if (e.key == 'Enter' && e.target.localName == 'input' && e.target.classList.contains('post-reply-content'))
       {
          createComment(e)
       }
    });
});

function createComment(e)
{
    let input = e.target;
    if (!input || input.value === '' || !input.dataset.postId)
    {
        return;
    }
    let data = new FormData();
    let postId = input.dataset.postId;
    data.append('postId', postId);
    data.append('commentContent', input.value);
    fetch(`${location.origin}/api/comments/create`, {
        method: "PUT",
        body: data
    })
    .then(response =>{
        if (response.ok)
        {
            return response.text();
        }
    })
    .then(commentId =>{
        if (window.stompClient != null)
        {
            window.stompClient.send("/app/comments", {}, JSON.stringify({id : Number(commentId), postId: Number(postId)}));
        }
        input.value = '';
    })
    .catch(e => console.log(e));
}