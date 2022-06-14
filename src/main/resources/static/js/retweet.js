window.addEventListener('load', function (){
    let posts = document.querySelector('.posts-container');
    if (posts)
    {
        posts.addEventListener('click', function (e){
            let btn = e.target.localName == 'button'
                ? e.target
                : e.target.parentElement;
            if (btn.classList.contains('post-button') && btn.classList.contains('retweet-btn'))
            {
                e.preventDefault();
                e.stopPropagation();
                if (btn.dataset.postId)
                {
                    retweet(btn);
                }
            }
        });
    }
});

function retweet(btn)
{
    fetch(`${location.origin}/api/posts/retweet/${btn.dataset.postId}`, {
        method: 'POST'
    })
    .then(response => {
        if (response.ok)
        {
            return response.text();
        }
    })
    .then(success => {
        if (success)
        {
            btn.classList.toggle('active');
            if (success == 'true')
            {
                btn.removeAttribute('data-user-id');
                btn.children[1].innerText = 'Retweeted';
            }
        }
    });
}