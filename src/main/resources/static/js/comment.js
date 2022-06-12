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
    data.append('postId', input.dataset.postId);
    data.append('commentContent', input.value);
    fetch(`${location.origin}/comment/create`, {
        method: "PUT",
        body: data
    })
    .then(response =>{
        if (response.ok)
        {
            return response.text();
        }
    })
    .then(html =>{
        document.querySelector(`.post-comments-${postId}`).insertAdjacentHTML('afterbegin', html);
        input.value = '';

        let commentCounter = document.querySelector(`.comments-counter-${postId}`);
        if (commentCounter)
        {
            commentCounter.textContent = `${Number(commentCounter.textContent.split(' ')[0]) + 1} Comments`;
        }
    })
    .catch(e => console.log(e));
}