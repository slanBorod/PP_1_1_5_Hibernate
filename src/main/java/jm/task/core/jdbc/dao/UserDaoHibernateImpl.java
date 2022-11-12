package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Table;

import javax.management.Query;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getSessionFactory;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Session session = Util.getSessionFactory().getCurrentSession()) {
            Transaction transaction = session.beginTransaction();

            session.createSQLQuery("CREATE TABLE IF NOT EXISTS users" +
                    "  (id BIGINT NOT NULL AUTO_INCREMENT, " +
                    "  name VARCHAR(45) NULL, " +
                    "  lastName VARCHAR(45) NULL,\n" +
                    "  age TINYINT(3) NULL,\n" +
                    "  PRIMARY KEY (id));").addEntity(User.class).executeUpdate();
            transaction.commit();
        } catch (Exception e) {

        }
    }

    @Override
    public void dropUsersTable() {
                try (Session session = Util.getSessionFactory().getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS users;").addEntity(User.class)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e){

        }
    }


    @Override
    public void saveUser(String name, String lastName, byte age) {
        try(Session session = Util.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();

            System.out.println("User с именем - " + name + " добавлен в базу данных");
        } catch (Exception e){

        }
    }

    @Override
    public void removeUserById(long id) {

        try (Session session = Util.getSessionFactory().getCurrentSession()){
            session.beginTransaction();
            session.createQuery("delete User where id= :id")
                    .setParameter("id", id).executeUpdate();
            session.getTransaction().commit();

            System.out.println("User with ID " + id + " has deleted!");

        } catch (Exception e) {

        }
    }

    @Override

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (Session session = Util.getSessionFactory().getCurrentSession()) {
            session.getTransaction().begin();
            userList = session.createQuery("from User", User.class)
                    .list();

            session.getTransaction().commit();

            System.out.println("All users are recorded");
        } catch (Exception e){

        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().getCurrentSession()){
            session.beginTransaction();
            session.createQuery("delete User").executeUpdate();
            session.getTransaction().commit();

            System.out.println("DB is empty!");
        } catch (Exception e){

        }
    }
}
