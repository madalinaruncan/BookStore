package ro.ubb.bookstore.domain.validators;

import ro.ubb.bookstore.domain.Book;

public class BookValidator implements Validator<Book>{
    @Override
    public void validate(Book book) throws ValidatorException {
        StringBuilder sb = new StringBuilder();

        if(book.getTitle().equals("")){
            sb.append("The title field cannot be empty!\n");
        }

        if(book.getAuthor_firstname().equals("")){
            sb.append("Author's firstname cannot be empty!\n");
        }

        if(book.getAuthor_lastname().equals("")){
            sb.append("Author's lastname cannot be empty!\n");
        }

        if(book.getRelease_year() > 2023){
            sb.append("Book's realease year cannot be bigger then 2023.\n");
        }

        if (book.getPrice() < 0) {
            sb.append("Book's price cannot be negative!\n");
        }

        if (sb.length() > 0) {
            throw new ValidatorException(sb.toString());
        }
    }
}
