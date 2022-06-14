window.addEventListener('load', function (){
    document.addEventListener('click', function (e){
        let followBtn = e.target.localName == 'button'
            ? e.target
            : e.target.parentElement;
        if (followBtn.classList.contains('follow-btn'))
        {
            e.preventDefault();
            e.stopPropagation();
            if (followBtn.dataset.userId)
            {
                follow(followBtn.dataset.userId, followBtn);
            }
        }
    });
});

function follow(userId, followBtn)
{
    fetch(`${location.origin}/follow/${userId}`, {
        method: "POST"
    })
    .then(response => {
        if (response.ok)
        {
            return response.text();
        }
    })
    .then(success => {
        if (followBtn)
        {
            followBtn.removeAttribute('data-user-id');
            followBtn.children[0].classList.add('d-none');
            followBtn.children[1].textContent = "Followed";
        }
    })
    .catch(e => console.log(e));
}