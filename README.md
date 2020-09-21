# CinematO

This is an Android Application Project, where i cloned the ImDB application with functionality to search upcoming movie, T.V shows and celebrities information.
The json data is used from <themoviedb.org> and json parser is used to store the data in local database (Rooms) using SQLite.

Functionality includes, listing based on genre and simple listing, adding favourite movies, genres, searching for a particular movie, tv show, celeb, fragments 
for better interaction with user etc.

-> Opening the application first time we meet with the following activity screen:
https://user-images.githubusercontent.com/40485599/93789802-f0c0bb00-fc4f-11ea-9f38-6431addaa55b.png
here in just one activity screen multiple fragments are used each with it's own functionality.


-> Now from the first activity we can choose different genre for movies and will be directed to the next menu with the list of all movies under same genre:
https://user-images.githubusercontent.com/40485599/93789905-12ba3d80-fc50-11ea-9f19-44cca8e8fe49.png
here the list used is dynamic listing so as to save space and make the list view more responsive.


-> In the main activity multiple fragments are used and the listing are done dynamically instead of static to make it feel responsive:
https://user-images.githubusercontent.com/40485599/93790030-2fef0c00-fc50-11ea-853b-2919df7c5dcf.png
here the list for most popular movies is shown which keeps on changing depending upon the popularity of the movie.


-> You can save your favourite movie by clicking on the heart present on the movie icon:
https://user-images.githubusercontent.com/40485599/93790121-4d23da80-fc50-11ea-81b2-6b0ef5da1f60.png
this will directly save your movie in the favourite movie section.


-> To save your favourite genre you can either click on the star or long press the genre icon :
https://user-images.githubusercontent.com/40485599/93790185-60cf4100-fc50-11ea-961a-98db03dfef30.png
this will directly save your genre in the favourite genre section.


-> If we open the sliding menu in the left (just as google playstore) we can find two option to choose from:
https://user-images.githubusercontent.com/40485599/93789824-f74f3280-fc4f-11ea-9874-e32301b38bf2.png


