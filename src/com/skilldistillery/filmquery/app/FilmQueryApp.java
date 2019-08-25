package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) throws SQLException {
		FilmQueryApp app = new FilmQueryApp();
//		app.test();
		app.launch();
	}

	private void test() throws SQLException {
//		System.out.println(db.findFilmById(57));
//		List<Actor> actors = db.findActorsByFilmId(57);
//		System.out.print("\nActors Present: ");
//				for (Actor actor : actors) {
//					System.out.print(actor);
//				}
//		System.out.println(db.findActorById(2));

	}

	private void launch() throws SQLException {
		Scanner input = new Scanner(System.in);

		System.out.println("Welcome to the Film Query Pro Application!");
		startUserInterface(input);

		input.close();
	}

	private void startUserInterface(Scanner input) throws SQLException {
		System.out.println("\n");
		System.out.println("Enter \"1\" to look up a movie by its ID#");
		System.out.println("Enter \"2\" to look up movie by a keyword");
		System.out.println("Enter \"3\" to exit the application.");

		int choice = input.nextInt();
		while ( choice != 3 )
			switch (choice) {
			case 1:
				System.out.println("Please enter a movie's ID# to look it up!");
					int id = input.nextInt();
					if(id > 1000 || id < 1) {
						System.out.println("Our inventory contains 1000 titles. Try again.");
					}
					else {
					System.out.println(db.findFilmById(id));
					}
					startUserInterface(input);
				break;
			case 2:
				System.out.println("Please enter a keyword to look up a movie");
				String keyword = input.next();
				
				db.findFilmbyKeyword(keyword);
				startUserInterface(input);

				break;
				
			case 3:
				System.out.println("Exiting application");
				break;
			default:
				System.out.println("Please enter a valid option");
			}
	}
}
