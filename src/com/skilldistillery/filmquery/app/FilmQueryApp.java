package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
//import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
//import com.skilldistillery.filmquery.entities.Actor;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) throws SQLException {
		FilmQueryApp app = new FilmQueryApp();
//		app.test();
		app.launch();
	}

	private void test() throws SQLException {
		System.out.println(db.findFilmById(57));
//		List<Actor> actors = db.findActorsByFilmId(57);
//		System.out.print("\nActors Present: ");
//				for (Actor actor : actors) {
//					System.out.print(actor);
//				}
//		System.out.println(db.findActorById(2));
	}

	private void launch() throws SQLException {
		Scanner input = new Scanner(System.in);

		System.out.print("Film Database Search Utility");
		startUserInterface(input);

		input.close();
	}

	private void startUserInterface(Scanner input) throws SQLException {

		int choice = 0;
		do {
			System.out.println("\n");
			System.out.println("Press 1 to search films by ID #");
			System.out.println("Press 2 to search database by keyword");
			System.out.println("Press 3 to close");
			try {
				choice = input.nextInt();
				switch (choice) {
				case 1:
					System.out.println("Enter movie ID # ");
					int id = input.nextInt();
					if (id > 1000 || id < 1) {
						System.out.println("Our inventory contains 1000 titles, 1-1000. Try again.");
					} else {
						System.out.println(db.findFilmById(id));
//						System.out.println("Actors Present: " + db.findActorsByFilmId(id));
					}

					break;
				case 2:
					System.out.println("Enter keyword: ");
					try {
						String keyword = input.next();
						System.out.println(db.findFilmbyKeyword(keyword));
						input.nextLine();
					} catch (Exception e) {
						System.out.println("Please enter a keyword");
					}
					break;

				case 3:
					System.out.println("Exiting utility...");

					break;
				default:
					System.out.println("Please enter a valid option"); //extreme ints
					break;
				}
			} catch (Exception e1) {
				System.out.println("Please enter a valid option"); //not ints
				input.nextLine();
			}
		} while (choice != 3);
		System.out.println("Utility closed");
	}
}
