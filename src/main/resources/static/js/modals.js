window.addEventListener("load", function (e){
    let followersBtn = document.querySelector('.user-followers-btn');
    if (followersBtn)
    {
        followersBtn.addEventListener('click', function (e){
            e.preventDefault();
            e.stopPropagation();
            if (e.target.dataset.userId)
            {
                showModal(`${location.origin}/followers/${e.target.dataset.userId}`);
            }
        });
    }

    let followingBtn = document.querySelector('.user-followings-btn');
    if (followingBtn)
    {
        followingBtn.addEventListener('click', function (e){
            e.preventDefault();
            e.stopPropagation();
            if (e.target.dataset.userId)
            {
                showModal(`${location.origin}/followings/${e.target.dataset.userId}`);
            }
        });
    }

    let posts = document.querySelector('.posts-container');
    if (posts)
    {
        posts.addEventListener('click', function (e){
            let btn = e.target.localName == 'a'
                ? e.target
                : e.target.parentElement;
            if (btn.dataset.postId)
            {
                if (btn.classList.contains('post-edit-btn'))
                {
                    editPost(btn, e);
                }
                else if (btn.classList.contains('post-delete-btn'))
                {
                    deletePost(btn, e);
                }
            }
        });
    }
});

function showModal(modalUrl, updModal) {
    fetch(modalUrl, {
        method: "GET"
    })
    .then(response => {
        if (response.ok)
        {
            return response.text();
        }
    })
    .then(html => {
        let modalDiv = document.createElement('div');
        modalDiv.innerHTML = html;
        modalDiv = modalDiv.firstChild;
        document.body.appendChild(modalDiv);

        let modal = new bootstrap.Modal(modalDiv);
        modalDiv.addEventListener('hidden.bs.modal', function (){
            modal.dispose();
            modalDiv.remove();
        });
        if (updModal && typeof updModal === 'function')
        {
            updModal(modalDiv);
        }
        modal.show();
    });
}

function deletePost(btn, e){
    if (e)
    {
        e.preventDefault();
        e.stopPropagation();
    }
    fetch(`${location.origin}/${btn.dataset.postId}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (response.ok)
        {
            return response.text();
        }
    })
    .then(isDeleted => {
        if (isDeleted == 'true')
        {
            document.querySelector(`.post-${btn.dataset.postId}`).remove();
        }
    });
}

function editPost(btn, e)
{
    if (e)
    {
        e.preventDefault();
        e.stopPropagation();
    }
    showModal(`${location.origin}/edit/${btn.dataset.postId}/dialog`, (modal) =>{
        modal.addEventListener('click', function (e){
            let saveBtn = e.target;
            if (saveBtn.classList.contains('edit-tweet-save-btn'))
            {
                e.stopPropagation();
                e.preventDefault();

                let form = document.querySelector('.edit-post-form');
                fetch(`${location.origin}/edit/${btn.dataset.postId}`, {
                    method: 'POST',
                    body: new FormData(form)
                })
                .then(response => {
                    if (response.ok)
                    {
                        return response.text();
                    }
                })
                .then(html =>{
                    let post = document.createElement('div');
                    post.innerHTML =  html;
                    document.querySelector(`.post-${btn.dataset.postId}`).innerHTML = post.firstChild.innerHTML;
                    bootstrap.Modal.getInstance(modal).hide();
                })
                .then(t => {
                    let fileName = `user-photos/posts/${btn.dataset.postId}/temp`;
                    let data = new FormData();
                    data.append('fileName', fileName);
                    fetch(`${location.origin}/api/files/temp`,{
                        method: "DELETE",
                        body: data
                    });
                });
            }
        });

        modal.addEventListener('change', function (e){
            let fileInput = e.target;
            if (fileInput.id == 'new-edit-tweet-image')
            {
                let form = document.querySelector('.edit-post-form');
                fetch(`${location.origin}/api/files/temp/save/${btn.dataset.postId}`, {
                    method: 'PUT',
                    body: new FormData(form)
                })
                .then(response => {
                    if (response.ok)
                    {
                        return response.text();
                    }
                })
                .then(img => {
                    let postImg = document.querySelector('.edit-post-thumbnail img');
                    if (postImg)
                    {
                        postImg.src = img;
                    }
                });
            }
        })
    });
}