<!DOCTYPE html>
<html>
<body>
	<h1>TransMate</h1>
	<p>TransMate is a Java-based client-server application designed to manage reservations for a transport company with multiple offices throughout the country. The application is built using the networking feature in Java to enable communication between the client and server components.</p>
	<h2>Server Component</h2>
	<p>The server component of the application is responsible for managing trips and reservations, which are stored in an SQLite database.</p>
	<h2>Client Component</h2>
	<p>The client component of the application is a desktop application built using JavaFX, which provides an interactive user interface. After logging in, the user is presented with a window that displays all available trips, including the destination, departure date and time, and the number of available seats. The user can search for a specific trip by entering the destination, departure date, and time, and the application will display a list of all reserved seats for that trip, including the seat number and the name of the client who reserved it.</p>
	<p>The user can also reserve seats for clients on a specific trip by entering the client's name and the number of seats they want to reserve. After a reservation is made, the application updates the list of available trips and seats for all users connected to the server. Additionally, the client's name is displayed in the search results for the reserved seats.</p>
	<h2>Real-Time Data Access</h2>
	<p>To ensure that all users have real-time access to the most up-to-date information, the application uses the observer pattern to update the user interface whenever a change is made to the reservation database. This allows all users to see the same information at the same time, ensuring data consistency and integrity.</p>
	<h2>Multiple Users</h2>
	<p>Multiple users can log in simultaneously to the server, each representing an office of the transport company. The server manages all incoming requests and updates the database accordingly, ensuring data consistency and integrity.</p>
	<h2>Conclusion</h2>
	<p>In conclusion, TransMate is a comprehensive client-server application that uses Java networking, SQLite, JavaFX, and the observer pattern to provide a robust and user-friendly solution for managing reservations for a transport company with multiple offices.</p>
</body>
</html>
