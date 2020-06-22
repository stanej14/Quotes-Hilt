# Quotes application
## Client for Favqs api.

### Technology
The application was written based on MVVM framework using Repositories and UseCases. The app is experimenting with the newly released library Hilt which is based on Dagger for DI.

Used technologies and libraries: Coroutines, LiveData, Retrofit + Moshi, Hilt, Crypto

### Functionalities:
* Quote of the day.
* Quote detail.
* Favorite state reflected in Quote detail.
* Search.
* Show quotes by tag after clicking on a tag.
* Login, token is remembered across sessions.
* Refresh Feed.
* Refresh Quote of the Day.
* Favorite and unfavorite actions in the Quote Detail.

### Next actions:
* Currently app is treating all errors the same. It just shows a snackbar that an Error has happened. We need to update Logic in ErrorHandler and parse errors coming in success responses from Favqs api.
* Profile is missing.
* Logout is missing.
* User's token is treated like it's neverendin without any refresh token logic.
* Paging for feed is missing.
* End-to-End tests.
* Complete unit test coverage.
* Dagger compatibility tests.
* Split OkHttpClients to one with user session and the other without.
