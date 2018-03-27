<html>
    <body>
        <form action="/editprofile" method="post">
            <h2>Edit Profile of ${username}</h2>
            <input type="submit" name="home" value="back to home">
            <p>To edit any of the attribute in your profile, change the text in the textbox and press edit.</p><br/>
            <p>${error}</p>

                Username: [<input type="text" name="newusername" value=${username}>]

                <input type="submit" name="xusername" value="edit"><br/>
                <br><br>
                First name: [<input type="text" name="newfn" value=${fname}>}]
                <input type="submit" name="xfirstname" value="edit"><br/>
                <br><br>
                Last name: [<input type="text" name="newln" value=${lname}>]
                <input type="submit" name="xlastname" value="edit"><br/>
                <br><br>
        </form>
    </body>
</html>
