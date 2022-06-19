var stompClient = null;
var socket = null;

window.addEventListener('load', function (){
       socket = new SockJS(`${location.origin}/tweets`);
       stompClient = Stomp.over(socket);
});

window.addEventListener('unload', function(){
    if(stompClient != null) {
        stompClient.disconnect();
    }
});

function appendTweet(post)
{
    if (post)
    {
        let posts = document.querySelector('.posts-container');
        if (posts.firstElementChild && posts.firstElementChild.id == 'tweet-form')
        {
            posts.firstElementChild.insertAdjacentHTML('afterend', post.html);
        }
        else
        {
            posts.insertAdjacentHTML('afterbegin', post.html);
        }
        let currentUserIdElem = document.querySelector('.header [data-user-id]');
        if (currentUserIdElem)
        {
            let currentUserId = currentUserIdElem.dataset.userId;
            if (currentUserId != post.authorId)
            {
                let retweetBtn = posts.querySelector(`.post-${post.postId} .post-button.retweet-btn`);
                if (retweetBtn)
                {
                    retweetBtn.dataset.postId = post.postId;
                }
                let menu = posts.querySelector('.dropdown-menu');
                if (menu)
                {
                    menu.classList.toggle('d-none');
                }
            }
        }
        let postThumb = posts.querySelector(`.post-${post.postId}-thumb`);
        if (postThumb && postThumb.classList.contains('d-none'))
        {
            postThumb.classList.toggle('d-none');
        }
    }
}

function appendComment(postId, html)
{
    if (postId && html)
    {
        let commentsContainer = document.querySelector(`.post-comments-${postId}`);
        if (commentsContainer)
        {
            commentsContainer.insertAdjacentHTML('afterbegin', html);
            let post = document.querySelector(`.post-${postId}`);
            if (post)
            {
                let commentCounter = post.querySelector('.post-button.comment-btn').children[1];
                if (commentCounter)
                {
                    addToCounter(commentCounter, 1);
                }
                commentCounter = post.querySelector('.post-rate').children[1];
                if (commentCounter)
                {
                    addToCounter(commentCounter, 1);
                }
            }
        }
    }
}

function appendCommentLike(commentId, isLiked, isCurrentUser)
{
    var likeBtn = document.querySelector(`.like-button[data-comment-id='${commentId}']`);
    if (likeBtn && likeBtn.nextElementSibling)
    {
        addToCounter(likeBtn.nextElementSibling, isLiked ? 1 : -1);
    }
}

function appendTweetLike(postId, isLiked, isCurrentUser)
{
    let post = document.querySelector(`.post-${postId}`);
    if (post)
    {
        let likeCounter = post.querySelector('.post-rate').children[0];
        if (likeCounter)
        {
            addToCounter(likeCounter, isLiked ? 1 : -1);
        }
    }
}

function savePost(postId, result)
{
    let btn = document.querySelector(`.post-${postId} .post-button.save-btn`);
    if (btn)
    {
        addToCounter(btn.parentElement.previousElementSibling.children[2], result ? 1 : -1);
    }
}

function addToCounter(counter, operand)
{
    let counterTextParts = counter.textContent.split(' ');
    counterTextParts[0] = (Number(counterTextParts[0]) + Number(operand));

    counter.textContent = counterTextParts.join(' ');
}

function deletePost(postId)
{
    let post = document.querySelector(`.post-${postId}`);
    if (post)
    {
        post.remove();
    }
}

function editUser(user)
{
    let elements = document.querySelectorAll(`[data-user-id='${user.userId}']`);
    if (elements)
    {
        for (let el of elements)
        {
            if (el.classList.contains('follow-user-bg') || el.id == 'panorama')
            {
                if (user.panorama && user.panorama != '')
                {
                    el.style.backgroundImage = `url(${location.origin}${user.panorama})`;
                }
            }
            else if (el.localName == 'h6' || el.localName == 'h5')
            {
                if (user.fullName && user.fullName != el.textContent)
                {
                    el.textContent = user.fullName;
                }
            }
            else if (el.classList.contains('avatar') && el.localName == 'img')
            {
                if (user.avatar && user.avatar != '')
                {
                    el.src = `${location.origin}${user.avatar}`;
                }
            }
            else if (el.classList.contains('profile-desc'))
            {
                if (user.description)
                {
                    el.children[0].textContent = user.description;
                }
            }
        }
    }
}

function editPost(postInfo)
{
    if (postInfo.postId)
    {
        let post = document.querySelector(`.post-${postInfo.postId}`);
        if (post)
        {
            let postThumbnail = post.querySelector(`.post-thumbnail`);
            if (postThumbnail && postInfo.image && postInfo.image != '')
            {
                if (!postThumbnail.children[0])
                {
                    let image = document.createElement('img');
                    image.src = `${location.origin}${postInfo.image}`;
                    image.classList.add(`post-${postInfo.postId}-thumb`);
                    postThumbnail.appendChild(image);
                }
                else
                {
                    postThumbnail.children[0].src = `${location.origin}${postInfo.image}`;
                }
            }
            let postContent = document.querySelector(`.post-${postInfo.postId}-content`);
            if (postContent && postInfo.content)
            {
                postContent.textContent = postInfo.content;
            }
        }
    }
}
