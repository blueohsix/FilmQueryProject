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

		try {
			Connection conn = DriverManager.getConnection(URL, userName, password);
			String sql = "SELECT film.id, film.title, film.description, film.release_year, language_id, language.name, "
			          + "film.rating, actor.first_name,  " 
						+ "actor.last_name, actor.id "
						+ "FROM film "
						+ "JOIN film_actor "  
						+ "ON film.id = film_actor.film_id " 
						+ "JOIN actor "
						+ "ON actor.id = film_actor.actor_id "
						+ "JOIN language "
						+ "ON film.language_id = language.id "
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
				String language = rs.getString("name");


				film = new Film(id, title, description, releaseYear, languageId, rating, language);
				film.setLanguage(language);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
	}

	public Actor findActorById(int actorId){
		Actor actor = null;
		try {
		Connection conn = DriverManager.getConnection(URL, userName, password);
		String sql = "SELECT id, first_name, last_name " 
				+ "FROM actor " 
				+ "WHERE id = ?";
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId){

		List<Actor> actors = new ArrayList<Actor>();
		try {
		Connection conn = DriverManager.getConnection(URL, userName, password);
		String sql = "SELECT actor.id, actor.first_name, actor.last_name" + "  "
				+ "FROM film "
				+ "  JOIN film_actor "
				+ "  ON film.id = film_actor.film_id " 
				+ "  JOIN actor " 
				+ "  ON actor.id = film_actor.actor_id  "
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actors;
	}

	@Override
	public List<Film> findFilmbyKeyword(String keyword){
		List<Film> films = new ArrayList<Film>();
		try {
		Connection conn = DriverManager.getConnection(URL, userName, password);
		
		String sql = "SELECT film.id, title, description," + "release_year, language_id, rental_duration,"
                + "rental_rate, length, replacement_cost, rating, special_features, language.name" + " FROM film"
                + " JOIN language ON film.language_id = language.id" + " WHERE title LIKE ? OR description LIKE ?";
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
			film.setLanguage(filmResult.getString("language.name"));
			films.add(film);

		}
		filmResult.close();
		stmt.close();
		conn.close();
		}
		
		catch (SQLException e) {
			System.out.println("No results found");
		}
		return films;
	}

}
