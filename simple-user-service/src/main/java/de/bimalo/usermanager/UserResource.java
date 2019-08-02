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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;

/**
 * Provides REST endpoints to manage users.
 *
 * @author mlohn
 */
@Path("/api/users")
@ApplicationScoped
public class UserResource {

    @Inject
    Logger log;

    @EJB
    private UserManager um;

    @GET
    public List<User> getUsers(@QueryParam("lastName") String lastName) {
        log.debug("getUsers() called.");
        List<User> users = new ArrayList<User>();
        if (lastName == null || lastName.isEmpty()) {
            users = um.findAll();
        } else {
            users = um.find(lastName);
        }
        log.debug("Found {} users in the database.", users.size());
        return users;
    }

    @GET
    @Path("/{id}")
    @Valid
    public Response getUser(@NotNull @PathParam("id") Long id) {
        log.debug("getUser(id={}) called.", id);
        User user = um.find(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(user).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Valid
    public Response deleteUser(@NotNull @PathParam("id") Long id) {
        log.debug("deleteUser(id={}) called.", id);
        um.deleteUser(id);
        return Response.ok().build();
    }

    @POST
    @Valid
    public Response addUser(User newUser) {
        log.debug("addUser(User={}) called.", newUser);
        List<User> newUsers = new ArrayList<User>(1);
        newUsers.add(newUser);
        um.addNewUsers(newUsers);
        return Response.status(Response.Status.CREATED).entity(newUser).build();
    }

    @POST
    @Valid
    @Path("/batch")
    public Response addUsers(User[] newUsers) {
        log.debug("addUsers() called.");
        um.addNewUsers(Arrays.asList(newUsers));
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Valid
    public Response updateUser(User user) {
        log.debug("updateUser(User={}) called.", user);
        um.updateUser(user);
        return Response.ok(user).build();
    }

    @DELETE
    @Path("/batch")
    public Response deleteAll() {
        log.debug("deleteAll() called.");
        um.deleteAll();
        return Response.ok().build();
    }

}
