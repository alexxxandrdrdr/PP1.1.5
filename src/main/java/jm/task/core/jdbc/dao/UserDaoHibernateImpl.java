package jm.task.core.jdbc.dao;


import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;


public class UserDaoHibernateImpl implements UserDao {
    Util util = new Util();
    private SessionFactory SESSION_FACTORY =  util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("""
                    CREATE TABLE IF NOT EXISTS USER (
                    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                    NAME VARCHAR(255),
                    LASTNAME VARCHAR(255),
                    AGE TINYINT)""").executeUpdate();

            transaction.commit();
            session.close();

        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void dropUsersTable() {
        try(Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("DROP TABLE IF EXISTS USER").executeUpdate();
            transaction.commit();

        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try(Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();

        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try(Session session = SESSION_FACTORY.openSession()) {

            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            transaction.commit();

        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();

        try(Session session = SESSION_FACTORY.openSession()) {

            Transaction transaction = session.beginTransaction();
            session.createQuery("from User", User.class).getResultStream().forEach(users::add);
            transaction.commit();
            session.close();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try(Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("delete from User").executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }

    }
}
