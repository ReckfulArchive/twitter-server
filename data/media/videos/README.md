# Videos

Videos taken from:

* Byron's own tweets
* Tweets that Byron retweeted
* Tweets that Byron quoted
* Byron's replies

## Files

* [tweets-with-videos.txt](tweets-with-videos.txt) - a list of tweets that contained videos that were downloaded.
* [bytes](bytes) - directory with all the videos downloaded as simple bytes, no ffmpeg post-processing or anything
  like that.
* [yt-dlp](yt-dlp) - directory with the same videos, but downloaded via [yt-dlp](https://github.com/yt-dlp/yt-dlp),
  so each video has a thumbnail, a description and some json metadata.
* [by-tweet-id](by-tweet-id) - directory with the videos copied from [yt-dlp](yt-dlp), but the filenames represent
  tweet ids that contain given video; copied purely for convenience.
