/*
    The book project lets a user keep track of different books they would like to read, are currently
    reading, have read or did not finish.
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY
    WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
    PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.RatingScale;
import lombok.extern.java.Log;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Log
public class RatingStatistics {
    private final List<Book> readBooksRated;
    private final Statistics statistics;

    public RatingStatistics(Statistics statistics) {
        this.statistics = statistics;
        this.readBooksRated = findReadBooksWithRatings();
    }

    /**
     * @return the Book in the 'read' shelf with the highest rating
     * If there are multiple books with the same highest rating, the first one found will be returned
     */
    public Book findMostLikedBook() {
        if (readBooksRated.size() <= 1) {
            return null;
        }
        readBooksRated.sort(Comparator.comparing(Book::getRating));
        return readBooksRated.get(readBooksRated.size() - 1);
    }

    private List<Book> findReadBooksWithRatings() {
        return statistics.getReadShelfBooks()
                .stream()
                .filter(book -> book.getRating() != null)
                .collect(Collectors.toList());
    }

    /**
     * @return the Book in the 'read' shelf with the lowest rating
     * If there are multiple books with the same lowest rating, the first one found will be returned
     */
    public Book findLeastLikedBook() {
        if (readBooksRated.size() <= 1) {
            return null;
        }
        readBooksRated.sort(Comparator.comparing(Book::getRating));
        return readBooksRated.get(0);
    }

    /**
     * @return the average rating given to all books in the 'read'
     * If a book in the 'read' shelf does not have a rating, it is not included in the sum
     */
    public Double calculateAverageRatingGiven() {
        int numberOfRatings = readBooksRated.size();
        if (numberOfRatings <= 1) {
            return null;
        }
        return (calculateTotalRating() / numberOfRatings);
    }

    private double calculateTotalRating() {
        return readBooksRated.stream()
                             .mapToDouble(book -> {
                                 Double rating = RatingScale.toDouble(book.getRating());
                                 rating = (rating == null) ? 0.0 : rating;
                                 return rating;
                             })
                             .sum();
    }
}
