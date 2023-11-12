# Smarthack Hackaton 2023

Together with 3 other team members, I've developed an Android application which uses an AI chatbot in order to track three main skills which we chose, as a team, such that
the user can interact with the chatbot in a natural manner, and all the processing is done 'behind the scenes'.

I have personally been responsible with the authentification system, the database construction and the charts which can be seen in the Fitness,Dezvoltare and Studying activities in the project.

My team has developed the overall design and properly engineered the AI chatbot model such that it could properly perform the aforementioned functionalities.

After the login system, you can directly chat to the model and, based on your responses, the 'trained' OpenAI API will give back 2 different responses, for 2 different threads:
1. First thread gives a response to the user, on the front end.
2. The second thread gives the backend a JSON object, under the form of [Skill:Hours] such that we could process it and add it to the database, in case it finds any in the user's prompt.

 Tools:
Android Studio
Chart library
SQLite
OpenAI API
