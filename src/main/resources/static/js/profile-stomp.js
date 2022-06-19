window.addEventListener('load', function (){
            if (window.stompClient != null)
            {
                window.stompClient.connect({}, successConnection, function(){
                    if (window.socket != null)
                    {
                        window.stompClient = Stomp.over(window.socket);
                    }
                });
            }

            function successConnection()
            {
                window.stompClient.subscribe('/topic/tweets/user', function(postOutput) {
                    let post = JSON.parse(postOutput.body);
                    let profileIdElement = document.querySelector('[data-profile-id]');
                    if (profileIdElement && profileIdElement.dataset.profileId == post.authorId && post.html)
                    {
                        window.appendTweet(post);
                    }
                });
                window.stompClient.subscribe('/topic/comments', function(commentResponse) {
                    let comment = JSON.parse(commentResponse.body);
                    if (comment.html)
                    {
                        window.appendComment(comment.postId, comment.html);
                    }
                });
                window.stompClient.subscribe('/topic/comments/like', function(likeResponse){
                    let like = JSON.parse(likeResponse.body);
                    if (like)
                    {
                        window.appendCommentLike(like.commentId, like.liked, like.currentUser);
                    }
                });
                window.stompClient.subscribe('/topic/tweets/like', function(likeResponse){
                    let like = JSON.parse(likeResponse.body);
                    if (like)
                    {
                        window.appendTweetLike(like.postId, like.liked, like.currentUser);
                    }
                });
                window.stompClient.subscribe('/topic/tweets/delete', function(deleteResponse){
                    let response = JSON.parse(deleteResponse.body);
                    if (response && response.success)
                    {
                        window.deletePost(response.postId);
                    }
                });
                window.stompClient.subscribe('/topic/tweets/edit', function(postEditResponse){
                    let post = JSON.parse(postEditResponse.body);
                    if (post)
                    {
                        window.editPost(post);
                    }
                });
                window.stompClient.subscribe('/topic/users/edit', function(userEditResponse){
                    let user = JSON.parse(userEditResponse.body);
                    if (user)
                    {
                        window.editUser(user);
                    }
                });
                window.stompClient.subscribe('/topic/tweets/save', function(resultResponse){
                    let res = JSON.parse(resultResponse.body);
                    if (res)
                    {
                        window.savePost(res.postId, res.success);
                    }
                });
            }
});