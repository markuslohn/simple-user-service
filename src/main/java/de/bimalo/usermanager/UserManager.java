/*
 * The MIT License
 *
 * Copyright 2019 mlohn.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.bimalo.usermanager;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;

/**
 * A facade to manage users in a database.
 *
 * @author mlohn
 */
@Stateless
public class UserManager {

    @Inject
    private Logger log;

    @PersistenceContext
    private EntityManager em;

    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public User find(Long id) {
        return em.find(User.class, id);
    }

    public List<User> find(String lastName) {
        User foundUser = null;

        String wildcardInsideLastName = lastName.contains("%") ? lastName + "%" : lastName;
        log.debug("Lookup users with lastname= {}...", wildcardInsideLastName);
        Query query = em.createQuery("SELECT u FROM User u WHERE u.lastName LIKE :lastName", User.class);
        query.setParameter("lastName", wildcardInsideLastName);
        return query.getResultList();
    }

    public void addNewUsers(List<User> newUsers) {
        newUsers.forEach(newUser -> em.persist(newUser));
    }

    public void updateUser(User user) {
        em.merge(user);
    }

    public void deleteUser(Long id) {
        User user = em.find(User.class, id);
        if (user != null) {
            em.remove(user);
        }
    }

    public void deleteAll() {
        log.info("Delete all records from user table...");
        Query query = em.createNativeQuery("DELETE FROM User");
        query.executeUpdate();
    }
}
