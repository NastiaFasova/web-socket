//window.addEventListener('load', function(){
//    let form = document.forms[0];
//    if (form)
//    {
//        form.addEventListener('submit', function(e){
//            if (form.id.value && stompClient != null)
//            {
//               stompClient.send('/app/users/edit', {}, JSON.stringify({
//                    id: form.id.value,
//                    name: form.fullName.value,
//                    desc: form.description.value,
//                    avatar: form.elements['user-avatar'] && form.elements['user-avatar'].value != ''
//                        ? form.elements['user-avatar'].value.substr(form.elements['user-avatar'].value.lastIndexOf('\\') + 1)
//                        : '',
//                    panorama: form.elements['bg'] && form.elements['bg'].value != ''
//                        ? form.elements['bg'].value.substr(form.elements['bg'].value.lastIndexOf('\\') + 1)
//                        : ''
//               }));
//            }
//        });
//    }
//});