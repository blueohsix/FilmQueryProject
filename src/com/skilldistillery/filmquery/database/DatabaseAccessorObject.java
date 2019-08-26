package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

//TEMPLATE FOR CONNECTING TO A DATABASE
public class DatabaseAccessorObject implements DatabaseAccessor {

	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	private final String userName = "student";
	private final String password = "student";

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	@Override
	public Film findFilmById(int filmId) {
		Film film = null;
		List<Actor> actors = new ArrayList<Actor>();

		try {
			Connection conn = DriverManager.getConnection(URL, userName, password);
			String sql = "SELECT film.id, film.title, film.description, film.release_year, film.language_id, "
			          + "film.rating, actor.first_name, " 
						+ "actor.last_name, actor.id "
						+ "FROM film "
						+ "JOIN film_actor "  
						+ "ON film.id = film_actor.film_id " 
						+ "JOIN actor "
						+ "ON actor.id = film_actor.actor_id "
						+ "WHERE film.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();


			while (rs.next()) {
				int id = rs.getInt("film.id");
				String title = rs.getString("title");
				String description = rs.getNString("description");
				int releaseYear = rs.getInt("release_year");
				int languageId = rs.getInt("language_id");
				String rating = rs.getString("rating");
//				while (rs.next()) {
//					Actor actor = new Actor();
//					actor.setId(rs.getInt("id"));
//					actor.setFirstName(rs.getString("first_name"));
//					actor.setLastName(rs.getString("last_name"));
//					actors.add(actor);
//				}

				film = new Film(id, title, description, releaseYear, languageId, rating);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return film;
	}

	public Actor findActorById(int actorId) throws SQLException {
		Actor actor = null;
		Connection conn = DriverManager.getConnection(URL, userName, password);
		String sql = "SELECT id, first_name, last_name FROM actor WHERE id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, actorId);
		ResultSet actorResult = stmt.executeQuery();
		while (actorResult.next()) {
			actor = new Actor(actorId, sql, sql);
			actor.setId(actorResult.getInt("id"));
			actor.setFirstName(actorResult.getString("first_name"));
			actor.setLastName(actorResult.getString("last_name"));
		}
		actorResult.close();
		stmt.close();
		conn.close();
		return actor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) throws SQLException {

		List<Actor> actors = new ArrayList<Actor>();

		Connection conn = DriverManager.getConnection(URL, userName, password);
		String sql = "SELECT actor.id, actor.first_name, actor.last_name" + "  FROM film " + "  JOIN film_actor "
				+ "  ON film.id = film_actor.film_id " + "  JOIN actor " + "  ON actor.id = film_actor.actor_id  "
				+ "  WHERE film.id = ?";

		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);
		ResultSet actorResult = stmt.executeQuery();

		while (actorResult.next()) {
			Actor actor = new Actor();
			actor.setId(actorResult.getInt("id"));
			actor.setFirstName(actorResult.getString("first_name"));
			actor.setLastName(actorResult.getString("last_name"));
			actors.add(actor);

		}
		actorResult.close();
		stmt.close();
		conn.close();
		return actors;
	}

	@Override
	public List<Film> findFilmbyKeyword(String keyword) throws SQLException {
		List<Film> films = new ArrayList<Film>();
		
		Connection conn = DriverManager.getConnection(URL, userName, password);
		String sql = "SELECT film.id, film.title, film.description, film.release_year, film.language_id, "
					+ "film.rating "
							+ "FROM film "
							+ "WHERE title like ? "
							+ "OR description like ? ";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, ("%" + keyword + "%"));
		stmt.setString(2, ("%" + keyword + "%"));
		ResultSet filmResult = stmt.executeQuery();

		while (filmResult.next()) {
			Film film = new Film();
			film.setId(filmResult.getInt("film.id"));
			film.setTitle(filmResult.getString("film.title"));
			film.setDescription(filmResult.getString("film.description"));
			film.setReleaseYear(filmResult.getInt("film.release_year"));
			film.setRating(filmResult.getString("film.rating"));
			films.add(film);
//			while (filmResult.next()) {
//				Actor actor = new Actor();
//				actor.setId(filmResult.getInt("actor.id"));
//				actor.setFirstName(filmResult.getString("actor.first_name"));
//				actor.setLastName(filmResult.getString("actor.last_name"));
//				actors.add(actor);
//			}
		}
		filmResult.close();
		stmt.close();
		conn.close();
		
		if (films.size() == 0) {
			System.out.println("No results found");
		}
		return films; 
		
//		if (films.size() == 0) {
//			System.out.println("No results found");
//			return;
//		} else {
//			for (Film film : films) {
//				System.out.print(film);
//			}
//		}
		
			/* Wanted to use a sysout instead of a return because of output formatting. It seems that the instructions 
			 * don't allow this though.   
			 * Returning a list of films places [ ] around the entire list and modifiying the 
			 * toString would not make these go away. Using a sysout inside this method and not having 
			 * a return type removes the [ ].
			 * If you'd like to beautify this output: comment the lines 140-143, uncomment 145-150, 
			 * change the return type to void, then adjust the return type on line 13 in DatabaseAccessor.java */
	}

}
