window.addEventListener('load', function(){
    let form = document.querySelector('#tweet-form');
    form.addEventListener('submit', function(e){
        e.preventDefault();

        if (form.content && form.content.value != '')
        {
            fetch(`${location.origin}/tweet/create`, {
                method: "PUT",
                body: new FormData(form)
            })
            .then(response =>{
                if (response.ok)
                {
                    return response.text();
                }
            })
            .then(html =>{
                form.insertAdjacentHTML('afterend', html);
                form.content.value = '';
                form.elements['tweet-image'].value = '';
            });
        }
    });
});