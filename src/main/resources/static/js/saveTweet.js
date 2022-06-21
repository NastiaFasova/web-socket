window.addEventListener('load', function (){
    document.addEventListener('click', function(e){
        let btn = e.target.localName == 'button'
            ? e.target
            : e.target.parentElement;
        if (btn.classList.contains('post-button') && btn.classList.contains('save-btn'))
        {
            e.preventDefault();
            e.stopPropagation();
            if (btn.dataset.postId)
            {
                saveTweet(btn, btn.parentElement.previousElementSibling.children[2]);
            }
        }
    });
});

function saveTweet(btn, counter)
{
    fetch(`${location.origin}/api/posts/save/${btn.dataset.postId}`, {
        method: 'POST'
    })
    .then(response => {
        if (response.ok)
        {
            return response.text();
        }
    })
    .then(isSaved => {
        btn.classList.toggle('active');
    });
}