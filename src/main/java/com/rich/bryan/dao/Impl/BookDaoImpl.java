package com.rich.bryan.dao.Impl;

import com.rich.bryan.dao.BookDao;
import com.rich.bryan.entity.Book;
import com.rich.bryan.entity.BooksUsers;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;


@Repository
public class BookDaoImpl implements BookDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Object[]> getBooks(String username){
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("select b, bu.dateCreated from BooksUsers bu " +
                "join bu.book b join fetch b.publisher join fetch b.authors where bu.user.username=:username")
                .setParameter("username", username);

        List<Object[]> objects = query.list();

        return objects;
    }

    @Override
    public void deleteBook(Long id, String name){
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("from BooksUsers bu where bu.book.id=:id and bu.user.username=:name")
                .setParameter("id", id).setParameter("name", name);
        BooksUsers booksUsers = (BooksUsers) query.list();

        session.delete(booksUsers);
    }

    @Override
    public List<Book> getAuthor(Long id, String username) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("select b from BooksUsers bu join bu.book b " +
                "join fetch b.authors a join fetch b.publisher join bu.user u where a.id =:id and u.username =:username")
                .setParameter("id", id).setParameter("username", username);

        List<Book> books = query.list();

        return books;
    }

    @Override
    public Book getSingleBook(String isbn13) throws NoResultException {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("from Book b " +
                "join fetch b.authors join fetch b.publisher where b.isbn13 =:isbn13")
                .setParameter("isbn13", isbn13);

        Book book = (Book) query.uniqueResult();
        return book;
    }
}