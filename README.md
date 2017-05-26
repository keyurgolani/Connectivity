# ConnActivity

ConnActivity is Connecting people using Activities
An Android Application for Social Media and Connectivity

## Installation

1. To download the project:

	`$ git clone https://github.com/keyurgolani/Connectivity.git`
2. To install the backend server:

	`$ cd Connectivity/backend`

	`$ npm install`
3. To install other support tools:

	Install [MySQL](https://www.mysql.com/) and [MongoDB](https://www.mongodb.com/) on your machine or get a cloud instance of them.
4. Now to install the application, install the APK file provided in the `Connectivity` folder or open the `Connectivity/Connectivity` folder as a project in [Android Studio](https://developer.android.com/studio/index.html)
5. Now edit the `properties.properties` files in `Connectivity/backend` and `Connectivity/Connectivity/app/src/main/assets` to change the connectivity and REST API details. If not sure, leave the values with `METHOD` prefix as is.
6. Now, to start the backend server:

	`$ mongod` - Making sure MongoDB is up and running. If already running, skip this step.

	`$ cd Connectivity/backend`

	`$ npm start`
7. Start the application from either Android Studio into emulator or directly your android phone.

## Usage

- Make sure to provide right email ID while signing up. You will need a verification code after signing up that is sent to your email.
- Make sure to use the application to the fullest. It is a great app. And please leave feedback.

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## History

[History File](history.log)

## Credits

- Keyur Golani - Well of course me. I did a lot of work here.
- Rushin Naik - A constant ally in the development. He worked on the whole stack across the project helping wherever and whenever needed.
- Gaurav Chodwadia - Thanks to you for such a beautiful UI and a dynamic presentation layer for the app.
- Ekta Sorathia - Thank you too for wiring everything up with the backend HTTP calls and very initial DB design.

## License

Don't understand licenses much! ;)
