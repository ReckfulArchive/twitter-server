function changeTab(tabID, tabName) {
    var i, tweets, tabButton;

    // Hide all tab content
    tweets = document.getElementsByClassName("tweet");
    for (i = 0; i < tweets.length; i++) {
        let tweet = tweets[i];
        if (tabName === "tweets") {
            if (tweet.classList.contains("reply-tweet")) {
                if (tweet.style.display !== "none") {
                    tweet.style.display = "none"
                }
            } else {
                if (tweet.style.display !== "flex") {
                    tweet.style.display = "flex";
                }
            }
        } else if (tabName === "tweets-with-replies") {
            if (tweet.style.display !== "flex") {
                tweet.style.display = "flex";
            }
        } else if (tabName === "media-tweets") {
            if (tweet.classList.contains("media-tweet")) {
                if (tweet.style.display !== "flex") {
                    tweet.style.display = "flex"
                }
            } else {
                if (tweet.style.display !== "none") {
                    tweet.style.display = "none";
                }
            }
        }
    }

    // Get all tab buttons and remove "active"
    tabButton = document.getElementsByClassName("tab-button");
    for (i = 0; i < tabButton.length; i++) {
        tabButton[i].classList.remove("active");
    }

    document.getElementById(tabID).classList.add("active");
}
