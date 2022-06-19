window.addEventListener('load', function(){
    document.querySelector('.posts-container').addEventListener('click', function(e){
        let target = e.target.parentElement.localName == 'button' ? e.target.parentElement : e.target;
        if (target.classList.contains('like-button') && target.dataset.commentId)
        {
            likeComment(e);
        }
        else if (target.classList.contains('post-button') && target.classList.contains('like-btn') && target.dataset.postId)
        {
            likePost(e);
        }
    });
});

function likeComment(e)
{
    let btn = getLikeBtn(e);
    if (btn)
    {
        btn.classList.toggle('active');
        if (window.stompClient != null)
        {
            window.stompClient.send("/app/comments/like", {}, btn.dataset.commentId);
        }
    }
}

function likePost(e)
{
    let btn = getLikeBtn(e);
    if (btn)
    {
        btn.classList.toggle('active');
        if (window.stompClient != null)
        {
            window.stompClient.send("/app/tweets/like", {}, btn.dataset.postId);
        }
    }
}

//function likeComment (e){
//    let btn = getLikeBtn(e);
//    like(btn, btn.nextElementSibling, `${location.origin}/api/likes/comment/${btn.dataset.commentId}`);
// }
//
//function likePost (e){
//    let btn = getLikeBtn(e);
//    let isLiked = like(btn, btn.parentElement.previousElementSibling.children[0], `${location.origin}/api/likes/post/${btn.dataset.postId}`, (isLiked) => {
//
//             let likeText = btn.children[1];
//             if (likeText && isLiked)
//             {
//                 likeText.textContent = isLiked == 'true'
//                     ? `${likeText.textContent}d`
//                     : likeText.textContent.substr(0, 4);
//             }
//         });
//}

function getLikeBtn (e)
{
    e.stopPropagation();
    e.preventDefault();
    return e.target.localName == 'button' ? e.target : e.target.parentElement;
}

function like (btn, likeCounter, apiUrl, callback)
{
    if (btn)
    {
        let postId = btn.dataset.postId;
        return fetch(apiUrl, {
                method: 'POST'
            })
            .then(response => {
                if (response.ok)
                {
                    return response.text();
                }
            })
            .then(isLiked =>{
                if (likeCounter)
                {
                    btn.classList.toggle('active');
                    let likeCounterTextParts = likeCounter.textContent.split(' ');
                    likeCounterTextParts[0] = Number(likeCounterTextParts[0]) + (isLiked == 'true' ? 1 : -1);
                    likeCounter.textContent = likeCounterTextParts.join(' ');
                }

                if (callback && typeof callback == 'function')
                {
                    callback(isLiked);
                }
            });
    }
}