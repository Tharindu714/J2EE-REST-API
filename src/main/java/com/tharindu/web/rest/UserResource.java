package com.tharindu.web.rest;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.*;

// Base URI: /api/users
@Path("/users")
public class UserResource {

    // --- Context injections (available in *any* method) ---
    @Context
    private UriInfo uriInfo;                  // URI details (path, query, etc.)
    @Context
    private HttpServletRequest request;       // raw servlet request
    @Context
    private ServletContext servletContext;    // servlet container context
    @Context
    private ServletConfig servletConfig;      // servlet config (init-params)
    @Context
    private HttpHeaders httpHeaders;          // all request headers
    @Context
    private SecurityContext securityContext;  // security info

    public UserResource() {
        System.out.println(">>> UserResource() constructor called");
    }

    // A simple in-memory list to demonstrate.
    private static final Map<String,User> STORE = new LinkedHashMap<>();
    static {
        for (int i=1; i<=5; i++) {
            User u = new User();
            u.setId(""+i);
            u.setUsername("User"+i);
            u.setEmail("user"+i+"@example.com");
            STORE.put(u.getId(), u);
        }
    }

    // ---------------------------------------
    // 1) GET all users: no params
    // ---------------------------------------
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAll() {
        System.out.println("listAll(): base URI is " + uriInfo.getAbsolutePath());
        return Response.ok(new ArrayList<>(STORE.values())).build();
    }

    // ---------------------------------------
    // 2) GET one user by path-param
    //    /api/users/{id}
    // ---------------------------------------
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        System.out.println("getById(): id = " + id);
        User u = STORE.get(id);
        if (u == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(u).build();
    }

    // ---------------------------------------
    // 3) GET with query-param
    //    /api/users/search?name=Foo
    // ---------------------------------------
    @GET
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByName(@QueryParam("name") String name) {
        System.out.println("searchByName(): name = " + name);
        List<User> out = new ArrayList<>();
        for (User u : STORE.values()) {
            if (u.getUsername().toLowerCase().contains(name.toLowerCase())) {
                out.add(u);
            }
        }
        return Response.ok(out).build();
    }

    // ---------------------------------------
    // 4) GET with matrix-param
    //    /api/users/matrix;type=premium
    // ---------------------------------------
    @GET
    @Path("matrix")
    @Produces(MediaType.TEXT_PLAIN)
    public String matrixExample(@MatrixParam("type") String type) {
        // NOTE: matrix params require WebConfig: request.setUriConneg(true); etc.
        System.out.println("matrixExample(): type = " + type);
        return "Matrix type: " + type;
    }

    // ---------------------------------------
    // 5) POST JSON payload + header + cookie
    // ---------------------------------------
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUserJson(
            User user,
            @HeaderParam("X-Client-Version") String clientVersion,
            @CookieParam("SESSIONID") String sessionId
    ) {
        System.out.println("createUserJson(): -> email=" + user.getEmail());
        System.out.println("createUserJson(): header X-Client-Version=" + clientVersion);
        System.out.println("createUserJson(): cookie SESSIONID=" + sessionId);

        // assign new ID and store
        String id = String.valueOf(STORE.size() + 1);
        user.setId(id);
        STORE.put(id, user);

        // return 201 Created + Location header
        UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(id);
        return Response.created(ub.build()).entity(user).build();
    }

    // ---------------------------------------
    // 6) POST form-url-encoded payload
    // ---------------------------------------
    @POST
    @Path("form")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String createUserForm(
            @FormParam("name") String name,
            @FormParam("email") String email
    ) {
        System.out.println("createUserForm(): name=" + name + ", email=" + email);
        return "Created user: " + name + " / " + email;
    }

    // ---------------------------------------
    // 7) PUT to update existing
    // ---------------------------------------
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(
            @PathParam("id") String id,
            User user
    ) {
        System.out.println("updateUser(): id=" + id + ", new email=" + user.getEmail());
        if (!STORE.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        user.setId(id);
        STORE.put(id, user);
        return Response.ok(user).build();
    }

    // ---------------------------------------
    // 8) DELETE
    // ---------------------------------------
    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") String id) {
        System.out.println("deleteUser(): id=" + id);
        User removed = STORE.remove(id);
        if (removed == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    // ---------------------------------------
    // 9) BeanParam example: collect multiple @QueryParam
    // ---------------------------------------
    public static class PagingBean {
        @QueryParam("start") @DefaultValue("0") private int  start;
        @QueryParam("size")  @DefaultValue("10") private int size;
        // getters omitted for brevity
        public int getStart() { return start; }
        public int getSize()  { return size;  }
    }

    @GET
    @Path("paged")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaged(@BeanParam PagingBean page) {
        System.out.println("getPaged(): start=" + page.getStart() + ", size=" + page.getSize());
        List<User> all = new ArrayList<>(STORE.values());
        int end = Math.min(page.getStart() + page.getSize(), all.size());
        List<User> sub = all.subList(page.getStart(), end);
        return Response.ok(sub).build();
    }

    @GET
    @Path("/new/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Map<String, Object> getUser(@PathParam("id") String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        map.put("name", "Tharindu");
        map.put("email", "tharindu@gmail.com");

        System.out.println(map);
        return map;
    }

    @GET
    @Path("/resp/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") Integer id) {
        Map<String, String> map2 = new HashMap<>();
        map2.put("id", String.valueOf(id));
        map2.put("name", "Amith");
        map2.put("email", "amith@gmail.com");

        System.out.println(map2);
        return Response.ok(map2).build(); //pass success response

        //return Response.notModified().build(); --> pass not modified response
        //return Response.serverError().entity(map2).build(); --> pass server error response
        //return Response.status(400).entity(map2).build(); --> pass bad request
        //return Response.temporaryRedirect(uriInfo.getAbsolutePath()).build();
    }
}