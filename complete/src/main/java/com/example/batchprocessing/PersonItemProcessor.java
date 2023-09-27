package com.example.batchprocessing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

	private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

	private final JdbcTemplate jdbcTemplate;

	public PersonItemProcessor(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Person process(final Person person) throws Exception {
		final String firstName = person.getFirstName().toUpperCase();
		final String lastName = person.getLastName().toUpperCase();

		final Person transformedPerson = new Person(firstName, lastName);

		jdbcTemplate.query("SELECT first_name, last_name FROM people",
				(rs, row) -> new Person(
						rs.getString(1),
						rs.getString(2))
		).forEach(p -> log.info("Found <{{}}> in the database.", p));

		log.info("Converting (" + person + ") into (" + transformedPerson + ")");

		return transformedPerson;
	}

}
